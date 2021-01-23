package me.hanwool.orderservice.domain.service;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

public interface MessageSources {

//    String INPUT_ORDERS = "input-orders";
    String OUTPUT_COMPOSITE = "output-composite";

//    @Input(INPUT_ORDERS)
//    SubscribableChannel inputOrders();

//    @Output(OUTPUT_COMPOSITE)
//    MessageChannel outputComposite();
}
