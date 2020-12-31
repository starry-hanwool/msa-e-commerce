package me.hanwool.mallcomposite.core.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.hanwool.mallutilapp.dto.*;

import me.hanwool.mallutilapp.dto.OrderAggregate;
import me.hanwool.mallutilapp.exception.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderCompositeServiceImpl {

    private final OrderCompositeIntegration integration;

    public OrderAggregate getOrder(Long orderId) {

        log.debug("getOrder - orderId : {}", orderId);

        OrderDTO order = integration.getOrder(orderId);
//        if (order == null) throw new NotFoundException("No order found for orderId: " + orderId);

        CouponDTO coupon = null;

        return createOrderAggregate(order, coupon);
    }

    private OrderAggregate createOrderAggregate(OrderDTO _order, CouponDTO _coupon) {

        log.debug("createOrderAggregate - orderId : {}", _order.getOrderId());

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

        return OrderAggregate.builder()
                .orderId(orderId)
                .couponId(couponId)
                .build();
    }
}
