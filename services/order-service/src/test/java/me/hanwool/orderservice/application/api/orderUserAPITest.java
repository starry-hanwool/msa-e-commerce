package me.hanwool.orderservice.application.api;

import me.hanwool.mallutilapp.value.ResponseCode;
import me.hanwool.orderservice.domain.Orders;
import me.hanwool.orderservice.domain.service.OrderServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;


@SpringBootTest
@AutoConfigureWebTestClient
@DisplayName("주문 API TEST")
class orderUserAPITest {

    private static final String ORDER_API_URI = "/api/order/";

    @Autowired
    private WebTestClient client;

    @MockBean
    private OrderServiceImpl service;

    @Nested
    @DisplayName("주문 정보 조회")
    class getOrderById {

        @Test
        @DisplayName("성공")
        void getOrderByIdSuccess() {
            // given
            final Long requestOrderId = 1L;
            final Orders orders = Orders.builder()
                    .orderId(requestOrderId)
                    .build();
            given(service.getOrder(requestOrderId)).willReturn(orders);

            // when, then
            client.get()
                    .uri(ORDER_API_URI + requestOrderId)
                    .accept(APPLICATION_JSON)
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody()
                    .jsonPath("$.orderId").isEqualTo(requestOrderId);
        }

        @Test
        @DisplayName("실패 - 정보 없음")
       void getOrderByIdNotFound() {
            // given
            final Long requestOrderId = 1L;

            given(service.getOrder(requestOrderId)).willReturn(null);

            // when, then
            client.get()
                    .uri(ORDER_API_URI + requestOrderId)
                    .accept(APPLICATION_JSON)
                    .exchange()
                    .expectStatus().is4xxClientError()
                    .expectBody()
                    .jsonPath("$.code").isEqualTo(ResponseCode.NOT_FOUND.code);
        }
    }

}