package me.hanwool.mallcomposite.domain.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.hanwool.mallutilapp.dto.*;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderCompositeServiceImpl {

    private final OrderCompositeIntegration integration;

    public Mono<OrderDTO> placeOrder(CreateOrderDTO requestOrder) throws Exception {
        log.debug("placeOrder");

        return Mono.just(integration.placeOrderComposite(requestOrder));
    }

    public OrderDTO getOrderDetails(String orderNum) {
        log.debug("getOrderDetails - orderNum : {}", orderNum);

        Mono<OrderDTO> order = integration.getOrder(orderNum);

        return order.block();
    }

}
