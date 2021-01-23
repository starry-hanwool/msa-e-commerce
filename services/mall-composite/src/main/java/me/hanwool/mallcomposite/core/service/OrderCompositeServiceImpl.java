package me.hanwool.mallcomposite.core.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.hanwool.mallutilapp.dto.*;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderCompositeServiceImpl {

    private final OrderCompositeIntegration integration;

    public Mono<OrderDTO> placeOrder(OrderDTO requestOrder) throws Exception {
//    public Mono<OrderDTO> placeOrder(OrderDTO requestOrder) {
//    public OrderDTO placeOrder(OrderDTO requestOrder) {
        log.debug("placeOrder");

        return Mono.just(integration.placeOrderComposite(requestOrder));
//        return integration.placeOrderComposite(requestOrder);
    }

    public OrderDTO getOrderDetails(Long orderNum) {
        log.debug("getOrderDetails - orderNum : {}", orderNum);

        Mono<OrderDTO> order = integration.getOrder(orderNum);
//        OrderDTO order = integration.getOrder(orderId);

        CouponDTO coupon = null;

        return createOrderDTO(order.block(), coupon);
    }

    private OrderDTO createOrderDTO(OrderDTO _order, CouponDTO _coupon) {
        log.debug("createOrderDTO - orderId : {}", _order.getOrderId());

        Long orderId = null;
        Long couponId = null;

        Optional<OrderDTO> order = Optional.ofNullable(_order);
        if(order.isPresent()) {
            orderId = order.get().getOrderId();
        }
        Optional<CouponDTO> coupon = Optional.ofNullable(_coupon);
        if(coupon.isPresent()) {
            couponId = coupon.get().getCouponId();
        }

        return OrderDTO.builder()
                .orderId(orderId)
                .couponId(couponId)
                .build();
    }

}
