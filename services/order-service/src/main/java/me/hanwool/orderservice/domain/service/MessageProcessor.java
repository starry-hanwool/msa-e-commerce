package me.hanwool.orderservice.domain.service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import me.hanwool.mallutilapp.dto.OrderDTO;
import me.hanwool.mallutilapp.event.Event;
import me.hanwool.mallutilapp.exception.EventProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MessageProcessor {

    private final OrderServiceImpl orderService;

//    private final ObjectMapper mapper;

    @Autowired
    public MessageProcessor(OrderServiceImpl orderService) {
        this.orderService = orderService;
    }

    @KafkaListener(topics = "${kafka.input.topic}")
    @SendTo
    public Event process(Event event) {

        log.info("received {} event. key : {}", event.getEventType(), event.getKey());
        log.debug("received event : {}", event.toString());

        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES); //

        switch (event.getEventType()) {

            case ORDER_CREATE:
                OrderDTO orderDTO = mapper.convertValue(event.getData(), OrderDTO.class);
//                OrderDTO orderDTO = mapper.convertValue(event.getData(), new TypeReference<OrderDTO>() {});

                Long orderNum = orderDTO.getOrderNum();
                log.info("Create order with orderNum: {}", orderNum);

                // 가주문 생성
                OrderDTO result = orderService.createOrder(orderDTO);

                // 생성 완료
//                messageSources.outputComposite().send(MessageBuilder.withPayload(new Event(Event.Type.ORDER_CREATED, orderNum, result)).build());
                // 주문 거부

                return new Event(Event.Type.ORDER_CREATED, orderNum, result);

            default:
                String errorMessage = "Incorrect event type: " + event.getEventType() + ", expected event";
                log.warn(errorMessage);
                throw new EventProcessingException(errorMessage);
        }

    }
}
