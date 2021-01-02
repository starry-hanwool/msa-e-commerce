package me.hanwool.mallcomposite.application.api;

import me.hanwool.mallcomposite.core.service.OrderCompositeServiceImpl;
import me.hanwool.mallutilapp.dto.OrderAggregate;
import me.hanwool.mallutilapp.value.ResponseCode;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.restdocs.webtestclient.WebTestClientRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.webtestclient.WebTestClientRestDocumentation.document;

@WebFluxTest(OderCompositeAPI.class)
@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
class OderCompositeAPITest {

    private static final String OPEN_API_ORDER_URI = "/open-api/order/";

    @Autowired
    private ApplicationContext context;

    private WebTestClient client;

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
    @DisplayName("주문 정보 확인")
    class getOrderDetailById {

        @Test
        @DisplayName("성공")
        void getOrderByIdSuccess
                () throws Exception {
            // given
            final Long requestOrderId = 1L;
            OrderAggregate returnOrder = OrderAggregate.builder()
                    .orderId(requestOrderId)
                    .couponId(1L)
                    .build();
            given(service.getOrder(requestOrderId)).willReturn(returnOrder);

            // when, then
            client.get()
                    .uri(OPEN_API_ORDER_URI + requestOrderId)
                    .accept(APPLICATION_JSON)
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody()
                    .jsonPath("$.orderId").isEqualTo(requestOrderId)
                    .consumeWith(document("get-order-api"));
        }

        @Test
        @DisplayName("실패 - 정보 없음")
        void getOrderByIdNotFound() throws Exception {
            // given
            final Long requestOrderId = 1L;

            given(service.getOrder(requestOrderId)).willReturn(null);

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


    /*@Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp(RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(documentationConfiguration(restDocumentation)).build();
    }

    @Test
    @DisplayName("주문 정보 확인")
    void getOrderById() throws Exception {
        this.mockMvc.perform(get("/open-api/order/" + ORDER_ID_OK)
                .contentType(APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderId").value(ORDER_ID_OK))
                .andDo(document("get-order-api"));
    }*/

//    @Test
//    public void getOrderNotFound() {
//
//        client.get()
//                .uri("/open-api/order/" + ORDER_ID_NOT_FOUND)
//                .accept(APPLICATION_JSON)
//                .exchange()
//                .expectStatus().isNotFound()
//                .expectHeader().contentType(APPLICATION_JSON)
//                .expectBody()
//                .jsonPath("$.path").isEqualTo("/open-api/order/" + ORDER_ID_NOT_FOUND)
//                .jsonPath("$.message").isEqualTo("NOT FOUND: " + ORDER_ID_NOT_FOUND);
//    }
}