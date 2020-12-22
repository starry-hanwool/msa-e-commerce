package me.hanwool.mallcomposite.core.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import me.hanwool.mallutilapp.dto.*;
import me.hanwool.mallcomposite.core.coupon.ports.CouponService;
import me.hanwool.mallcomposite.core.order.ports.OrderService;
import me.hanwool.mallcomposite.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

@Slf4j
@Component
public class OrderCompositeIntegration implements CouponService, OrderService {

    private final RestTemplate restTemplate;
    private final ObjectMapper mapper;

    private final String couponServiceUrl;
    private final String orderServiceUrl;

    @Autowired
    public OrderCompositeIntegration(RestTemplate restTemplate,
                                     ObjectMapper mapper,
                                     @Value("${app.coupon-service.host}") String couponServiceHost,
                                     @Value("${app.coupon-service.port}") int couponServicePort,
                                     @Value("${app.order-service.host}") String orderServiceHost,
                                     @Value("${app.order-service.port}") int orderServicePort) {
        this.restTemplate = restTemplate;
        this.mapper = mapper;
        this.couponServiceUrl = "http://" + couponServiceHost + ":" + couponServicePort + "/api/coupon/";
        this.orderServiceUrl = "http://" + orderServiceHost + ":" + orderServicePort + "/api/order/";

        init();
    }

    private void init() {
        restTemplate.getInterceptors().add(new ClientHttpRequestInterceptor() {
            @Override
            public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
                request.getHeaders().set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
                return execution.execute(request, body);
            }
        });
    }

    @Override
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

    }

    @Override
//    @GetMapping(value = "/order/{orderId}", consumes = MediaType.APPLICATION_JSON_VALUE)
//    public OrderDTO getOrder(@PathVariable Long orderId) {
    public OrderDTO getOrder(Long orderId) {
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
        }
    }
}
