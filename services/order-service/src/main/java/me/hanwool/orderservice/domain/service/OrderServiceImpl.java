package me.hanwool.orderservice.domain.service;

import me.hanwool.mallutilapp.dto.OrderDTO;
import me.hanwool.mallutilapp.exception.NotFoundException;
import me.hanwool.orderservice.domain.Orders;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceImpl implements OrderService {

    @Override
    public Orders getOrder(Long orderId) {
        if (orderId == 13) {
            return null;
        }
//        if (orderId == 13) throw new NotFoundException("No order found for orderId: " + orderId);

        return Orders.builder()
                .orderId(orderId)
                .productId(0L)
                .build();
    }
}
