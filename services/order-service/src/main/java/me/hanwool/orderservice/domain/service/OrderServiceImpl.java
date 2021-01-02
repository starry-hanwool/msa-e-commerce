package me.hanwool.orderservice.domain.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.hanwool.mallutilapp.dto.OrderDTO;
import me.hanwool.mallutilapp.exception.NotFoundException;
import me.hanwool.orderservice.domain.Orders;
import me.hanwool.orderservice.domain.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository repository;

    @Override
    public Orders getOrder(Long orderId) {

        Optional<Orders> order = repository.findById(orderId);

        return order.orElse(null);
    }
}
