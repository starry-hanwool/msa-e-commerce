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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//@Transactional
@ExtendWith(SpringExtension.class)
@WebMvcTest(OrderAPI.class)
@ActiveProfiles("logging-test")
@DisplayName("주문 API TEST")
class orderAPITest {

    private static final String ORDER_API_URI = "/api/order/";

    @Autowired
    private MockMvc mockMvc;

//    @InjectMocks
//    private OrderAPI orderAPI;

    @MockBean
    private OrderRepository repository;

    @MockBean
    private OrderServiceImpl service;

    @Autowired
    private ObjectMapper mapper;

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

/*    @Nested
    @DisplayName("주문 요청")
    class placeOrder {

        @Test
        @DisplayName("가주문 생성 성공")
        void placeOrderSuccess() throws Exception {
            // given
            final Long requestOrderNum = 20210112L;

            final OrderDTO requestOrderAgg = OrderDTO.builder()
//            final OrderAggregate requestOrderAgg = OrderAggregate.builder()
                    .orderNum(requestOrderNum)
                    .build();

//            final Orders requestOrder = Orders.builder()
//                    .orderNum(requestOrderNum)
//                    .build();

            given(service.createOrder(requestOrderAgg)).willReturn(requestOrderAgg);
//            given(service.createOrder(requestOrder)).willReturn(requestOrder);

            postExpectedBody(requestOrderAgg, HttpStatus.OK)
                    .andExpect(jsonPath("$.orderNum").value(requestOrderNum));

        }
    }*/

    @Nested
    @DisplayName("주문 정보 조회")
    class getOrderById {

        @Test
        @DisplayName("성공")
        void getOrderByIdSuccess() throws Exception {
            // given
            final Long requestOrderNum = 20120112111L;
//            final Long requestOrderId = 1L;
            final Orders orders = Orders.builder()
//                    .orderId(requestOrderId)
                    .orderNum(requestOrderNum)
                    .build();
            given(service.getOrder(requestOrderNum)).willReturn(orders);

            // when, then
            getExpectedBody(String.valueOf(requestOrderNum), HttpStatus.OK)
//            getExpectedBody(String.valueOf(requestOrderId), HttpStatus.OK)
//                    .jsonPath("$.orderId").isEqualTo(requestOrderId);
                    .andExpect(jsonPath("$.orderNum").value(requestOrderNum));
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

    private ResultActions postExpectedBody(OrderDTO orderAggregate, HttpStatus expectedStatus) throws Exception {
//    private ResultActions postExpectedBody(OrderAggregate orderAggregate, HttpStatus expectedStatus) throws Exception {
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