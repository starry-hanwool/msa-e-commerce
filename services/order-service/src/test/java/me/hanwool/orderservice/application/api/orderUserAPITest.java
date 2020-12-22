package me.hanwool.orderservice.application.api;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;


@SpringBootTest
@AutoConfigureWebTestClient
class orderUserAPITest {

    // test !!!!!
    private static final int ORDER_ID_OK = 1;
    private static final int ORDER_ID_NOT_FOUND = 13;
    ////////////

    @Autowired
    private WebTestClient client;

    @Test
    void getOrderById() {
        client.get()
                .uri("/api/order/" + ORDER_ID_OK)
//                .accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON_VALUE)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.orderId").isEqualTo(ORDER_ID_OK);
    }
}