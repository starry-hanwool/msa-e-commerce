package me.hanwool.mallcomposite.core.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import me.hanwool.mallcomposite.infra.config.KafkaConfig;
import me.hanwool.mallutilapp.dto.*;
import me.hanwool.mallutilapp.event.Event;
import me.hanwool.mallutilapp.port.CouponService;
import me.hanwool.mallutilapp.port.OrderService;
import me.hanwool.mallutilapp.exception.NotFoundException;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.requestreply.RequestReplyFuture;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.PollableChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
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

    private final ReplyingKafkaTemplate<Long, Event, Event> replyingKafkaTemplate;

    @Autowired
    public OrderCompositeIntegration(RestTemplate restTemplate,
                                     WebClient.Builder webClientBuilder,
                                     ObjectMapper mapper,
                                     ReplyingKafkaTemplate<Long, Event, Event> replyingKafkaTemplate,
                                     @Value("${app.coupon-service.host}") String couponServiceHost,
                                     @Value("${app.coupon-service.port}") int couponServicePort,
                                     @Value("${app.order-service.host}") String orderServiceHost,
                                     @Value("${app.order-service.port}") int orderServicePort) {
        this.webClient = webClientBuilder.build();
        this.mapper = mapper;
        this.replyingKafkaTemplate = replyingKafkaTemplate;
        this.couponServiceUrl = "http://" + couponServiceHost + ":" + couponServicePort + "/api/coupon/";
        this.orderServiceUrl = "http://" + orderServiceHost + ":" + orderServicePort + "/api/order/";
    }

    public OrderDTO placeOrderComposite(OrderDTO requestOrder) throws ExecutionException, InterruptedException {

        // 주문번호 생성 -> key로 사용(날짜 + 주문자번호 인코딩?)
        Long orderNum = 202101081111L;
        OrderDTO orderDTO = OrderDTO.builder()
                .orderNum(orderNum)
                .couponId(requestOrder.getCouponId())
                .status(requestOrder.getStatus())
                .build();

        log.debug("placeOrderComposite - orderNum : {}", orderNum);

        // 주문 요청
//        ConsumerRecord<Long, Event> response = null;
        Event event = new Event(Event.Type.ORDER_CREATE, orderNum, orderDTO);
        ProducerRecord<Long, Event> record = new ProducerRecord<Long, Event>(OUTPUT_ORDERS, orderNum, event);
        RequestReplyFuture<Long, Event, Event> future = replyingKafkaTemplate.sendAndReceive(record);

        ConsumerRecord<Long, Event> response = future.get();
        Event receivedEvent = response.value();
        OrderDTO result = mapper.convertValue(receivedEvent.getData(), OrderDTO.class);

        log.debug("received event: {}", receivedEvent.toString());

        // 쿠폰 확인 및 주문번호와 함께 사용요청 ( 실패시 보상 트랜잭션! )
//        Event couponEvent = new Event(Event.Type.COUPON_PENDING, orderNum, orderDTO);
//        ProducerRecord<Long, Event> couponRecord = new ProducerRecord<Long, Event>(OUTPUT_COUPONS, orderNum, couponEvent);
//        RequestReplyFuture<Long, Event, Event> couponFuture = replyingKafkaTemplate.sendAndReceive(couponRecord);
//
//        ConsumerRecord<Long, Event> couponResponse = future.get();
//        Event receivedCouponEvent = response.value();
//        OrderDTO couponResult = mapper.convertValue(receivedCouponEvent.getData(), OrderDTO.class);
//
//        log.debug("received coupon event: {}", receivedCouponEvent.toString());

        // 주문 승인 요청 및 완료 이벤트 받기



        return result;
    }

    @Override
    public Mono<OrderDTO> getOrder(Long orderNum) {
//    public OrderDTO getOrder(Long orderId) {

        String url = orderServiceUrl + orderNum;
//        return webClient.get().uri(url).retrieve().bodyToMono(OrderDTO.class).log();
        return webClient.get().uri(url).retrieve().bodyToMono(OrderDTO.class).log().onErrorMap(WebClientResponseException.class, ex -> handleException(ex));
//        return webClient.get().uri(url).exchange().map(OrderDTO.class).log().onErrorMap(WebClientResponseException.class, ex -> handleException(ex));
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
