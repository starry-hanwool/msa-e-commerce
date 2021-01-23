package me.hanwool.mallcomposite.application.api;

import me.hanwool.mallcomposite.core.service.OrderCompositeIntegration;
import me.hanwool.mallcomposite.core.service.OrderCompositeServiceImpl;
import me.hanwool.mallutilapp.dto.OrderDTO;
import me.hanwool.mallutilapp.value.ResponseCode;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.webtestclient.WebTestClientRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.webtestclient.WebTestClientRestDocumentation.document;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@WebFluxTest(OderCompositeAPI.class)
@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@DirtiesContext
@ActiveProfiles("logging-test")
class OderCompositeAPITest {

    private static final String OPEN_API_ORDER_URI = "/open-api/order/";

    @Autowired
    private ApplicationContext context;

    private WebTestClient client;

    @MockBean
    private WebClient.Builder builder;

    @MockBean
    private OrderCompositeIntegration integration;

    @MockBean
    private OrderCompositeServiceImpl service;

    @BeforeEach
    void setup(RestDocumentationContextProvider restDocumentation) {
        this.client = WebTestClient.bindToApplicationContext(this.context)
                .configureClient()
                .filter(documentationConfiguration(restDocumentation))
                .build();
    };

    @Nested
    @DisplayName("주문 요청")
    class placeOrder {

        @Test
        @DisplayName("성공")
        void placeOrderSuccess() throws Exception {
            // given
//            final Long createdOrderId = 1L;
            final Long createdOrderNum = 202101101111L;

            OrderDTO requestOrder = OrderDTO.builder()
                    .build();
//            OrderDTO requestOrder = mock(OrderDTO.class);

            OrderDTO returnOrder = OrderDTO.builder()
                    .orderNum(202101101111L)
                    .build();
//            OrderDTO returnOrder = mock(OrderDTO.class);
//            given(returnOrder.getOrderNum()).willReturn(createdOrderNum);
//            StepVerifier.create(requestOrder).expectNext()
//            doReturn(returnOrder).when(requestOrderSpy);

//            given(service.placeOrder(requestOrder)).willReturn(Mono.just(returnOrder));
            given(service.placeOrder(any(OrderDTO.class))).willReturn(Mono.just(returnOrder));
//            given(service.placeOrder(any(OrderDTO.class))).willReturn(returnOrder);

            // API 요청 성공하면
            client.post()
                    .uri(OPEN_API_ORDER_URI)
                    .accept(APPLICATION_JSON)
                    .contentType(APPLICATION_JSON)
                    .body(Mono.just(requestOrder), OrderDTO.class)
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody()
//                    .jsonPath("$.orderId").isEqualTo(createdOrderId)
                    .jsonPath("$.orderNum").isEqualTo(202101101111L)
//                    .jsonPath("$.orderNum").isEqualTo(202101101111L)
                    .consumeWith(document("post-order"));
        }
    }

    @Nested
    @DisplayName("주문 정보 확인")
    class getOrderDetailsById {

        @Test
        @DisplayName("성공")
        void getOrderDetailsByIdSuccess() throws Exception {
            // given
            final Long requestOrderNum = 20210118L;
//            final Long requestOrderId = 1L;
            OrderDTO returnOrder = OrderDTO.builder()
                    .orderNum(requestOrderNum)
//                    .orderId(requestOrderId)
                    .couponId(1L)
                    .build();
            given(service.getOrderDetails(requestOrderNum)).willReturn(returnOrder);

            // when, then
            client.get()
                    .uri(OPEN_API_ORDER_URI + requestOrderNum)
                    .accept(APPLICATION_JSON)
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody()
                    .jsonPath("$.orderNum").isEqualTo(requestOrderNum)
//                    .jsonPath("$.orderId").isEqualTo(requestOrderNum)
                    .consumeWith(document("get-order"));
        }

        @Test
        @DisplayName("실패 - 정보 없음")
        void getOrderDetailsByIdNotFound() throws Exception {
            // given
            final Long requestOrderId = 1L;

            given(service.getOrderDetails(requestOrderId)).willReturn(null);

            // when, then
            client.get()
                    .uri(OPEN_API_ORDER_URI + requestOrderId)
                    .accept(APPLICATION_JSON)
                    .exchange()
                    .expectStatus().is4xxClientError()
                    .expectBody()
                    .jsonPath("$.code").isEqualTo(ResponseCode.NOT_FOUND.code);
        }
    }

    private WebTestClient.BodyContentSpec getExpectedBody(String idPath, HttpStatus expectedStatus) {
        return client.get()
                .uri(OPEN_API_ORDER_URI + idPath)
                .accept(APPLICATION_JSON)
                .exchange()
                .expectStatus().isEqualTo(expectedStatus)
                .expectBody();
    }

}