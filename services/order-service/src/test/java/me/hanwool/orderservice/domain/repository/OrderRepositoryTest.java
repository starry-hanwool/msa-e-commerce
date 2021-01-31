package me.hanwool.orderservice.domain.repository;

import me.hanwool.orderservice.domain.Item;
import me.hanwool.orderservice.domain.OrderLine;
import me.hanwool.orderservice.domain.Orders;
import me.hanwool.orderservice.domain.value.MoneyJPA;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class OrderRepositoryTest {

    @Autowired
    private OrderRepository repository;

    @Test
    @DisplayName("성공")
    void createOrderSuccess() {
        final String createdOrderNum = "202101101111";

        List<OrderLine> orderLineList = new ArrayList<>();
        orderLineList.add(OrderLine.builder()
                .item(Item.builder()
                        .price(new MoneyJPA(new BigDecimal(12000)))
                        .build())
                .build());

        Orders requestOrder = Orders.builder()
                .orderNum(createdOrderNum)
                .orderLineList(orderLineList)
                .build();

        Orders resultOrder = repository.save(requestOrder);

        assertEquals(createdOrderNum, resultOrder.getOrderNum());
    }
}