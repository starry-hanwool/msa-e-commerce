package me.hanwool.mallcomposite.domain.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import me.hanwool.mallutilapp.dto.*;
import me.hanwool.mallutilapp.event.Event;
import me.hanwool.mallutilapp.port.CouponService;
import me.hanwool.mallutilapp.port.OrderService;
import me.hanwool.mallutilapp.exception.NotFoundException;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.requestreply.RequestReplyFuture;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ExecutionException;

@Slf4j
@RestController
public class OrderCompositeIntegration implements CouponService, OrderService {

    private WebClient.Builder webClientBuilder;
    private WebClient webClient;

    private final ObjectMapper mapper;

    private final String couponServiceUrl;
    private final String orderServiceUrl;

    @Value("${kafka.output.orders.topic}")
    private String OUTPUT_ORDERS;

    private final ReplyingKafkaTemplate<String, Event, Event> replyingKafkaTemplate;

    @Autowired
    public OrderCompositeIntegration(RestTemplate restTemplate,
                                     WebClient.Builder webClientBuilder,
                                     ObjectMapper mapper,
                                     ReplyingKafkaTemplate<String, Event, Event> replyingKafkaTemplate,
                                     @Value("${app.coupon-service.host}") String couponServiceHost,
                                     @Value("${app.coupon-service.port}") int couponServicePort,
                                     @Value("${app.order-service.host}") String orderServiceHost,
                                     @Value("${app.order-service.port}") int orderServicePort
                                     ) {
        this.webClient = webClientBuilder.build();
        this.mapper = mapper;
        this.replyingKafkaTemplate = replyingKafkaTemplate;
        this.couponServiceUrl = "http://" + couponServiceHost + ":" + couponServicePort + "/api/coupon/";
        this.orderServiceUrl = "http://" + orderServiceHost + ":" + orderServicePort + "/api/order/";
    }

    public OrderDTO placeOrderComposite(CreateOrderDTO requestOrder) throws ExecutionException, InterruptedException {

        String key = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));

        OrderDTO orderDTO = OrderDTO.builder()
                .orderLineList(null)
                .build();

        log.debug("placeOrderComposite - key : {}", key);

        // 주문 요청
        Event event = new Event(Event.Type.ORDER_CREATE, key, orderDTO);
        ProducerRecord<String, Event> record = new ProducerRecord<String, Event>(OUTPUT_ORDERS, key, event);
        RequestReplyFuture<String, Event, Event> future = replyingKafkaTemplate.sendAndReceive(record);

        // 가주문 정보 응답
        ConsumerRecord<String, Event> response = future.get();
        Event receivedEvent = response.value();
        OrderDTO result = mapper.convertValue(receivedEvent.getData(), OrderDTO.class);

        log.debug("received event: {}", receivedEvent.toString());

        // 쿠폰 및 결제 요청 등의 작업 ( 실패시 보상 트랜잭션 )


        // 주문 승인 요청 및 완료 이벤트 받기


        return result;
    }

    @Override
    public Mono<OrderDTO> getOrder(String orderNum) {
        String url = orderServiceUrl + orderNum;
        return webClient.get().uri(url).retrieve().bodyToMono(OrderDTO.class).log().onErrorMap(WebClientResponseException.class, ex -> handleException(ex));
    }

    private Throwable handleException(Throwable ex) {

        if (!(ex instanceof WebClientResponseException)) {
            log.warn("Got a unexpected error: {}, will rethrow it", ex.toString());
            return ex;
        }

        WebClientResponseException wcre = (WebClientResponseException)ex;

        switch (wcre.getStatusCode()) {

            case NOT_FOUND:
                return new NotFoundException(getErrorMessage(wcre));

            default:
                log.warn("Got a unexpected HTTP error: {}, will rethrow it", wcre.getStatusCode());
                log.warn("Error body: {}", wcre.getResponseBodyAsString());
                return ex;
        }
    }

    private String getErrorMessage(WebClientResponseException ex) {
        try {
            return mapper.readValue(ex.getResponseBodyAsString(), ErrorResponse.class).getMsg();
        } catch (IOException ioex) {
            return ex.getMessage();
        }
    }
}
