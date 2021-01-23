package me.hanwool.orderservice.domain.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.hanwool.mallutilapp.dto.ItemDTO;
import me.hanwool.mallutilapp.dto.OrderDTO;
import me.hanwool.mallutilapp.dto.OrderLineDTO;
import me.hanwool.mallutilapp.event.Event;
import me.hanwool.mallutilapp.value.Money;
import org.junit.Before;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.cloud.stream.test.binder.MessageCollector;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringExtension;
//import org.springframework.test.web.reactive.server.WebTestClient;
//import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment=RANDOM_PORT)
//@AutoConfigureWebTestClient
@DirtiesContext
//@ActiveProfiles("logging-test")
@Rollback(value = false) // test
@DisplayName("메시지 통합 테스트")
class MessageProcessorTest {

//    private static final String OPEN_API_ORDER_URI = "/open-api/order/";

/*    @Autowired
    private WebTestClient client;

    @Autowired
    private WebClient.Builder builder;*/

//    @Autowired
//    private OrderCompositeServiceImpl service;

//    @Autowired
//    private MessageSources pipe;
//
//    @Autowired
//    private Sink sink;

//    @Autowired
//    private MessageCollector collector;

    @Before
    public void setUp() {

    }

    @Test
    @DisplayName("가주문 생성 성공")
    public void createOrderCompleted() throws Exception {

        final Long createdOrderNum = 202101101111L;

        List<OrderLineDTO> orderLineList = new ArrayList<>();
        orderLineList.add(OrderLineDTO.builder()
                .item(ItemDTO.builder()
                        .price(new Money(new BigDecimal(12000)))
                        .build())
                .build());

        OrderDTO requestOrder = OrderDTO.builder()
                .orderNum(createdOrderNum)
                .orderLineList(orderLineList)
                .build();

        // 가주문 생성 요청 받으면
//        sink.input().send(MessageBuilder.withPayload(new Event(Event.Type.ORDER_CREATE, createdOrderNum, requestOrder)).build());

        // 생성 완료 메시지 발행
//        assertEquals(1, collector.forChannel(pipe.outputComposite()).size());

        /*String payload = collector.forChannel(pipe.outputComposite())
                .poll()
                .getPayload().toString();
        Event event = new ObjectMapper().readValue(payload, Event.class);*/

//        GenericMessage<String> inMsg = (GenericMessage<String>) collector.forChannel(pipe.outputComposite()).poll();
//        Event event = new ObjectMapper().readValue(inMsg.getPayload(), Event.class);
//
//        assertTrue(event instanceof Event);
//        assertTrue(event.getData() instanceof OrderDTO);
//
//        OrderDTO responseOrder = (OrderDTO)event.getData();
//        assertEquals(responseOrder.getStatus(), OrderStatus.CREATED);

    }
}