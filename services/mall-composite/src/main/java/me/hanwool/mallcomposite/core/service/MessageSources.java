package me.hanwool.mallcomposite.core.service;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

public interface MessageSources {

    String OUTPUT_ORDERS = "output-orders";
    String OUTPUT_COUPONS = "output-coupons";

    @Output(OUTPUT_ORDERS)
    MessageChannel outputOrders();

    @Output(OUTPUT_COUPONS)
    MessageChannel outputCoupons();
}
