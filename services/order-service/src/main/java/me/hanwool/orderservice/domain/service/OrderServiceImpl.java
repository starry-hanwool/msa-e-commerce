package me.hanwool.orderservice.domain.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.hanwool.mallutilapp.dto.OrderDTO;
import me.hanwool.mallutilapp.value.OrderStatus;
import me.hanwool.orderservice.domain.Orders;
import me.hanwool.orderservice.domain.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository repository;

    private final ProductServiceImpl productService;

    // 가주문 생성
    @Transactional
    @Override
    public OrderDTO createOrder(OrderDTO orderDTO) {

        // 가주문 저장 전 금액 등 유효성 확인
        // ...

        Orders order = Orders.builder()
                .orderLineList(null)
                .createdDate(LocalDateTime.now())
                .status(OrderStatus.CREATED)
                .build();
        order.generateOrderNum();

        Orders result = repository.save(order);
        log.debug("saved {} order :{}", result.getStatus(), result.getOrderNum());

        return OrderDTO.builder()
                .orderNum(result.getOrderNum())
                .status(result.getStatus())
                .build();
    }

    @Override
    public Orders getOrder(String orderNum) {
        return repository.findByOrderNum(orderNum);
    }
}
