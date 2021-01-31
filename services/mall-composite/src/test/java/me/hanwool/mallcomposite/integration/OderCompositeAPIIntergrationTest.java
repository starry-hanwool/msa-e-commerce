package me.hanwool.mallcomposite.integration;

import me.hanwool.mallcomposite.domain.service.OrderCompositeServiceImpl;
import me.hanwool.mallutilapp.dto.OrderDTO;
import me.hanwool.mallutilapp.event.Event;
import org.junit.Before;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment=RANDOM_PORT)
@AutoConfigureWebTestClient
@DirtiesContext
@DisplayName("API 통합 테스트")
public class OderCompositeAPIIntergrationTest {

    private static final String OPEN_API_ORDER_URI = "/open-api/order/";

    @Autowired
    private WebTestClient client;

    @Autowired
    private WebClient.Builder builder;

    @Autowired
    private OrderCompositeServiceImpl service;

    @Autowired
    private ReplyingKafkaTemplate<Long, Event, Event> replyingKafkaTemplate;

    @Before
    public void setUp() {

    }

    @Test
    @DisplayName("주문 요청 성공")
    public void createCompositeOrder() throws Exception {

        OrderDTO requestOrder = OrderDTO.builder()
                .build();

        // API 요청 성공하면
        client.post()
                .uri(OPEN_API_ORDER_URI)
                .accept(APPLICATION_JSON)
                .contentType(APPLICATION_JSON)
                .body(Mono.just(requestOrder), OrderDTO.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.orderNum").isNotEmpty();

    }


}
