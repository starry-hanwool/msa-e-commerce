package me.hanwool.orderservice.application.integration;

import me.hanwool.mallutilapp.dto.ItemDTO;
import me.hanwool.mallutilapp.dto.OrderDTO;
import me.hanwool.mallutilapp.dto.OrderLineDTO;
import me.hanwool.mallutilapp.value.Money;
import org.junit.Before;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment=RANDOM_PORT)
@DirtiesContext
@Rollback(value = false) // test
@DisplayName("메시지 통합 테스트")
class OrderAPIIntegrationTest {

    @Before
    public void setUp() {

    }

    @Test
    @DisplayName("가주문 생성 성공")
    public void createOrderCompleted() throws Exception {

        final String createdOrderNum = "202101101111";

        List<OrderLineDTO> orderLineList = new ArrayList<>();
        orderLineList.add(OrderLineDTO.builder()
                .item(ItemDTO.builder()
                        .price(new Money(new BigDecimal(12000)))
                        .build())
                .build());

        OrderDTO requestOrder = OrderDTO.builder()
                .orderNum(createdOrderNum)
                .orderLineList(orderLineList)
                .build();

    }
}