package me.hanwool.orderservice.domain.service;

import me.hanwool.orderservice.domain.Orders;

public interface OrderService {

    Orders getOrder(Long orderId);
}
