package me.hanwool.orderservice.application.api;

import me.hanwool.mallutilapp.handler.APIExceptionHandler;
import me.hanwool.mallutilapp.value.ResponseCode;
import me.hanwool.orderservice.domain.Orders;
import me.hanwool.orderservice.domain.service.OrderServiceImpl;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//@Transactional
//@SpringBootTest
//@AutoConfigureWebTestClient
//@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
@WebMvcTest(OrderAPI.class)
//@ExtendWith(MockitoExtension.class)
@DisplayName("주문 API TEST")
class orderAPITest {

    private static final String ORDER_API_URI = "/api/order/";

    @Autowired
    private MockMvc mockMvc;
//    private WebTestClient client;

//    @InjectMocks
//    private OrderAPI orderAPI;

    @MockBean
//    @Autowired
//    @Mock
    private OrderServiceImpl service;

    /*@BeforeEach
    public void setup() {
//        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders
                .standaloneSetup(orderAPI)
                .setControllerAdvice(new APIExceptionHandler())
//                .addDispatcherServletCustomizer(
//                        dispatcherServlet -> dispatcherServlet.setThrowExceptionIfNoHandlerFound(true))
//                .setControllerAdvice(
//                        APIExceptionHandler.class)
                .build();
    }*/

    @Nested
    @DisplayName("주문 정보 조회")
    class getOrderById {

        @Test
        @DisplayName("성공")
        void getOrderByIdSuccess() throws Exception {
            // given
            final Long requestOrderId = 1L;
            final Orders orders = Orders.builder()
                    .orderId(requestOrderId)
                    .build();
            given(service.getOrder(requestOrderId)).willReturn(orders);

            // when, then
            /*mockMvc.perform(get(ORDER_API_URI + requestOrderId)
                    .accept(APPLICATION_JSON_VALUE))
//                .contentType(APPLICATION_JSON_VALUE))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.orderId").value(requestOrderId));*/

            getExpectedBody(String.valueOf(requestOrderId), HttpStatus.OK)
//                    .jsonPath("$.orderId").isEqualTo(requestOrderId);
                    .andExpect(jsonPath("$.orderId").value(requestOrderId));
        }

        @Test
        @DisplayName("실패 - 정보 없음")
        void getOrderByIdNotFound() throws Exception {
            // given
            final Long requestOrderId = 1L;
            given(service.getOrder(requestOrderId)).willReturn(null);

            // when, then
            mockMvc.perform(get(ORDER_API_URI + requestOrderId)
                    .accept(APPLICATION_JSON_VALUE))
                    .andExpect(status().is4xxClientError());

        }
    }

    //

    private ResultActions getExpectedBody(String productIdPath, HttpStatus expectedStatus) throws Exception {
//    private WebTestClient.BodyContentSpec getExpectedBody(String productIdPath, HttpStatus expectedStatus) {

        return mockMvc.perform(get(ORDER_API_URI + productIdPath)
                .accept(APPLICATION_JSON_VALUE))
//                .contentType(APPLICATION_JSON_VALUE))
                .andExpect(status().is(expectedStatus.value()));
//                .andExpect(jsonPath("$.orderId").value(ORDER_ID_OK))
//        return client.get()
//                .uri(ORDER_API_URI + productIdPath)
//                .accept(APPLICATION_JSON)
//                .exchange()
//                .expectStatus().isEqualTo(expectedStatus)
//                .expectBody();
    }

}