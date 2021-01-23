package me.hanwool.orderservice.domain.service;

import me.hanwool.orderservice.domain.Orders;
import me.hanwool.orderservice.domain.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@Transactional
@DisplayName("주문 서비스 TEST")
class OrderServiceImplTest {

    @Mock
    private OrderRepository repository;

    @InjectMocks
    private OrderServiceImpl service;

    @BeforeEach
    void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Nested
    @DisplayName("주문 생성")
    class createOrder {

        @Test
        @DisplayName("성공")
        void createOrderSuccess() {

        }
    }

    @Nested
    @DisplayName("주문 조회")
    class getOrder {

        @Test
        @DisplayName("성공")
        void getOrderSuccess() {
            // given
            final Long requestOrderNum = 2021011211L;
/*            Optional<Orders> order = Optional.of(Orders.builder()
//                    .orderId(requestOrderId)
                    .orderNum(requestOrderNum)
                    .build());*/
            Orders order = Orders.builder()
//                    .orderId(requestOrderId)
                    .orderNum(requestOrderNum)
                    .build();
            given(repository.findByOrderNum(requestOrderNum)).willReturn(order);
//            given(repository.findById(requestOrderId)).willReturn(order);

            // when
            Orders returnedOrder = service.getOrder(requestOrderNum);
//            Orders returnedOrder = service.getOrder(requestOrderId);

            // then
            assertEquals(requestOrderNum, returnedOrder.getOrderNum());
//            assertEquals(requestOrderId, returnedOrder.getOrderId());
        }

    }

}