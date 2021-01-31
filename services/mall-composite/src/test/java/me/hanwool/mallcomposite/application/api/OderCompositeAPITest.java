package me.hanwool.mallcomposite.application.api;

import me.hanwool.mallcomposite.domain.service.OrderCompositeIntegration;
import me.hanwool.mallcomposite.domain.service.OrderCompositeServiceImpl;
import me.hanwool.mallutilapp.dto.CreateOrderDTO;
import me.hanwool.mallutilapp.dto.OrderDTO;
import me.hanwool.mallutilapp.value.OrderStatus;
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
            final String createdOrderNum = "20210131221624721523117";

            CreateOrderDTO requestOrder = CreateOrderDTO.builder()
                    .orderLineList(null)
                    .build();

            OrderDTO returnOrder = OrderDTO.builder()
                    .orderNum(createdOrderNum)
                    .orderLineList(null)
                    .status(OrderStatus.COMPLETE)
                    .build();

            given(service.placeOrder(any(CreateOrderDTO.class))).willReturn(Mono.just(returnOrder));

            // API 요청
            client.post()
                    .uri(OPEN_API_ORDER_URI)
                    .accept(APPLICATION_JSON)
                    .contentType(APPLICATION_JSON)
                    .body(Mono.just(requestOrder), CreateOrderDTO.class)
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody()
                    .jsonPath("$.orderNum").isEqualTo(createdOrderNum)
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
            final String requestOrderNum = "20210131221624721523117";
            OrderDTO returnOrder = OrderDTO.builder()
                    .orderNum(requestOrderNum)
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
                    .consumeWith(document("get-order"));
        }

        @Test
        @DisplayName("실패 - 정보 없음")
        void getOrderDetailsByIdNotFound() throws Exception {
            // given
            final String requestOrderNum = "20210131221624721523117";

            given(service.getOrderDetails(requestOrderNum)).willReturn(null);

            // when, then
            client.get()
                    .uri(OPEN_API_ORDER_URI + requestOrderNum)
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