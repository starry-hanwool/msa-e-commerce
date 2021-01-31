package me.hanwool.orderservice.domain.service;

import me.hanwool.mallutilapp.dto.OrderDTO;
import me.hanwool.orderservice.domain.Orders;

public interface OrderService {

    OrderDTO createOrder(OrderDTO order);

    Orders getOrder(String orderNum);

}
