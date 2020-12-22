package me.hanwool.mallcomposite.core.service;

import me.hanwool.mallutilapp.dto.*;

import me.hanwool.mallcomposite.core.OrderAggregate;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class OrderCompositeService {

    public OrderAggregate createOrderAggregate(OrderDTO _order, CouponDTO _coupon) {

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
