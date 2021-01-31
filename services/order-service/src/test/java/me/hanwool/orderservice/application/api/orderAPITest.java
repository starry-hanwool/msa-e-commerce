package me.hanwool.orderservice.application.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.hanwool.mallutilapp.dto.OrderDTO;
import me.hanwool.orderservice.domain.Orders;
import me.hanwool.orderservice.domain.repository.OrderRepository;
import me.hanwool.orderservice.domain.service.OrderServiceImpl;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(OrderAPI.class)
@ActiveProfiles("logging-test")
@DisplayName("주문 API TEST")
class orderAPITest {

    private static final String ORDER_API_URI = "/api/order/";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderRepository repository;

    @MockBean
    private OrderServiceImpl service;

    @Autowired
    private ObjectMapper mapper;

    @Nested
    @DisplayName("주문 정보 조회")
    class getOrderById {

        @Test
        @DisplayName("성공")
        void getOrderByIdSuccess() throws Exception {
            // given
            final String requestOrderNum = "20120112111";
            final Orders orders = Orders.builder()
                    .orderNum(requestOrderNum)
                    .build();
            given(service.getOrder(requestOrderNum)).willReturn(orders);

            // when, then
            getExpectedBody(String.valueOf(requestOrderNum), HttpStatus.OK)
                    .andExpect(jsonPath("$.orderNum").value(requestOrderNum));
        }

        @Test
        @DisplayName("실패 - 정보 없음")
        void getOrderByIdNotFound() throws Exception {
            // given
            final String requestOrderNum = "2021112222";
            given(service.getOrder(requestOrderNum)).willReturn(null);

            // when, then
            mockMvc.perform(get(ORDER_API_URI + requestOrderNum)
                    .accept(APPLICATION_JSON_VALUE))
                    .andExpect(status().is4xxClientError());
        }
    }

    //

    private ResultActions postExpectedBody(OrderDTO orderAggregate, HttpStatus expectedStatus) throws Exception {
        String content = mapper.writeValueAsString(orderAggregate);

        return mockMvc.perform(post(ORDER_API_URI)
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .content(content))
                .andExpect(status().is(expectedStatus.value()));
    }

    private ResultActions getExpectedBody(String productIdPath, HttpStatus expectedStatus) throws Exception {
        return mockMvc.perform(get(ORDER_API_URI + productIdPath)
                .accept(APPLICATION_JSON_VALUE))
                .andExpect(status().is(expectedStatus.value()));
    }

}