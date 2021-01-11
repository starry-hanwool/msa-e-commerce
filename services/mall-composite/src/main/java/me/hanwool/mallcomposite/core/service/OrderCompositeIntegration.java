package me.hanwool.mallcomposite.core.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import me.hanwool.mallutilapp.dto.*;
import me.hanwool.mallutilapp.event.Event;
import me.hanwool.mallutilapp.port.CouponService;
import me.hanwool.mallutilapp.port.OrderService;
import me.hanwool.mallutilapp.exception.NotFoundException;
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
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.io.IOException;

@Slf4j
@EnableBinding(MessageSources.class)
@Component
public class OrderCompositeIntegration implements CouponService, OrderService {

    private final WebClient.Builder webClientBuilder;
    private WebClient webClient;

    private final ObjectMapper mapper;

    private final String couponServiceUrl;
    private final String orderServiceUrl;

    private MessageSources messageSources;

    @Autowired
    public OrderCompositeIntegration(RestTemplate restTemplate,
                                     WebClient.Builder webClientBuilder,
                                     ObjectMapper mapper,
                                     MessageSources messageSources,
                                     @Value("${app.coupon-service.host}") String couponServiceHost,
                                     @Value("${app.coupon-service.port}") int couponServicePort,
                                     @Value("${app.order-service.host}") String orderServiceHost,
                                     @Value("${app.order-service.port}") int orderServicePort) {
        this.webClientBuilder = webClientBuilder;
        this.mapper = mapper;
        this.messageSources = messageSources;
        this.couponServiceUrl = "http://" + couponServiceHost + ":" + couponServicePort + "/api/coupon/";
        this.orderServiceUrl = "http://" + orderServiceHost + ":" + orderServicePort + "/api/order/";

    }

    public OrderDTO placeOrderComposite(OrderAggregate requestOrder) {

        // 주문번호 생성 -> key로 사용(날짜 + 주문자번호 인코딩?)
        Long orderNum = 202101081111L;

        // 쿠폰 확인 및 주문번호와 함께 사용요청 ( 실패시 보상 트랜잭션! )
//        if (requestOrder.getReviews() != null) {
//            body.getReviews().forEach(r -> {
//                Review review = new Review(body.getProductId(), r.getReviewId(), r.getAuthor(), r.getSubject(), r.getContent(), null);
//                integration.createReview(review);
//            });
//        }
        /*Long couponId = requestOrder.getCouponId();
        if(couponId != null) {
            String url = couponServiceUrl + couponId; // couponId + "/" + orderNum !!!!!!!!!!!!!!!!!!!!!!!
            Mono<CouponDTO> coupon = webClient.get().uri(url).retrieve().bodyToMono(CouponDTO.class).log();
//        Mono<CouponDTO> coupon = webClient.get().uri(url).retrieve().bodyToMono(CouponDTO.class).log().onErrorMap(WebClientResponseException.class, ex -> handleException(ex));
        }*/

        // 주문 요청
//        Long createdOrderId = 1L; // test
        messageSources.outputOrders().send(MessageBuilder.withPayload(new Event(Event.Type.CREATE, orderNum, requestOrder)).build());

        return OrderDTO.builder()
                .orderNum(orderNum)
                .build();
    }

    /*@Override
    public OrderDTO createOrder(OrderDTO body) {
        // 메시지 전송 !!!
        messageSources.outputOrders().send(MessageBuilder.withPayload(new Event(Event.Type.CREATE, body.getProductId(), body)).build());
        return body;
    }*/

/*    @Override
//    @GetMapping(value = "/coupon/{couponId}", consumes = MediaType.APPLICATION_JSON_VALUE)
//    public CouponDTO getCoupon(@PathVariable Long couponId) {
    public CouponDTO getCoupon(Long couponId) {
        try {
            String url = couponServiceUrl + couponId;
            log.debug("call getCoupon API on URL: {}", url);

            CouponDTO coupon = restTemplate.getForObject(url, CouponDTO.class);
            log.debug("Found a coupon with id: {}", coupon.getCouponId());

            return coupon;

        } catch (HttpClientErrorException ex) {

            switch (ex.getStatusCode()) {

                case NOT_FOUND:
                    throw new NotFoundException("NotFound");
//                    throw new NotFoundException(getErrorMessage(ex));

//                case UNPROCESSABLE_ENTITY :
//                    throw new InvalidInputException(getErrorMessage(ex));

                default:
                    log.warn("Got a unexpected HTTP error: {}, will rethrow it", ex.getStatusCode());
                    log.warn("Error body: {}", ex.getResponseBodyAsString());
                    throw ex;
            }
        }

    }*/

    @Override
    public Mono<OrderDTO> getOrder(Long orderId) {
//    public OrderDTO getOrder(Long orderId) {

        String url = orderServiceUrl + orderId;
        return webClient.get().uri(url).retrieve().bodyToMono(OrderDTO.class).log();
//        return webClient.get().uri(url).retrieve().bodyToMono(OrderDTO.class).log().onErrorMap(WebClientResponseException.class, ex -> handleException(ex));
/*
        try {
            String url = orderServiceUrl + orderId;
            log.debug("call getOrder API on URL: {}", url);

            OrderDTO order = restTemplate.getForObject(url, OrderDTO.class);
            log.debug("Found a order with id: {}", order.getOrderId());

            return order;

        } catch (HttpClientErrorException ex) {

            switch (ex.getStatusCode()) {

                case NOT_FOUND:
                    throw new NotFoundException("errrrrrrrrrrrr");
//                    throw new NotFoundException(getErrorMessage(ex));

//                case UNPROCESSABLE_ENTITY :
//                    throw new InvalidInputException(getErrorMessage(ex));

                default:
                    log.warn("Got a unexpected HTTP error: {}, will rethrow it", ex.getStatusCode());
                    log.warn("Error body: {}", ex.getResponseBodyAsString());
                    throw ex;
            }
        }*/
    }
}
