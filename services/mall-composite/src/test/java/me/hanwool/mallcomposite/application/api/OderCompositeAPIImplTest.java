package me.hanwool.mallcomposite.application.api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@SpringBootTest //
@AutoConfigureWebTestClient
class OderCompositeAPIImplTest {

    // test !!!!!
    private static final int ORDER_ID_OK = 1;
    private static final int ORDER_ID_NOT_FOUND = 2;
    ////////////

    @Autowired
    private WebTestClient client;

    @BeforeEach
    void setup() {};

    @Test
    @DisplayName("주문 정보 확인")
    void getOrderById() {
        client.get()
                .uri("/open-api/order/" + ORDER_ID_OK)
                .header(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON_VALUE)
//                .accept(APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.orderId").isEqualTo(ORDER_ID_OK);
    }

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