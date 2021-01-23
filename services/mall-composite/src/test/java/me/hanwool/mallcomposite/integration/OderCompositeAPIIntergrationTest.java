package me.hanwool.mallcomposite.integration;

import me.hanwool.mallcomposite.core.service.OrderCompositeServiceImpl;
import me.hanwool.mallutilapp.dto.OrderDTO;
import me.hanwool.mallutilapp.event.Event;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.junit.Before;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.stream.test.binder.MessageCollector;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.requestreply.RequestReplyFuture;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.*;
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

    //    @Autowired
//    private OrderCompositeIntegration integration;
//
    @Autowired
    private OrderCompositeServiceImpl service;

//    @Autowired
//    private MessageSources pipe;

    @Autowired
    private ReplyingKafkaTemplate<Long, Event, Event> replyingKafkaTemplate;

    @Autowired
    private MessageCollector collector;

    @Before
    public void setUp() {

    }

    @Test
    @DisplayName("주문 요청 성공")
    public void createCompositeOrder() throws Exception {

//        OrderDTO composite = OrderDTO.builder()
//                .orderId(1L)
//                .couponId(22L)
//                .build();
//        postAndVerifyProduct(composite, OK);

//        final Long createdOrderNum = 202101101111L;

        OrderDTO requestOrder = OrderDTO.builder()
                .build();

//        OrderDTO returnOrder = OrderDTO.builder()
//                .orderNum(202101101111L)
//                .build();

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

//        pipe.outputOrders().send(MessageBuilder.withPayload(new Event(Event.Type.CREATE, createdOrderNum, requestOrder)).build());

//        Object payload = collector.forChannel(pipe.outputOrders())
//                .poll()
//                .getPayload();
//        assertEquals(expectedValue, payload);

        // 발행된 메시지 확인
//        assertEquals(1, collector.forChannel(pipe.outputOrders()).size());


//        assertNotNull(collector.forChannel(pipe.outputOrders()));

    }


}
