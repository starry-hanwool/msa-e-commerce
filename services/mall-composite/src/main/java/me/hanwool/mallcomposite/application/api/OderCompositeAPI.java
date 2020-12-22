package me.hanwool.mallcomposite.application.api;

import me.hanwool.mallcomposite.core.OrderAggregate;

public interface OderCompositeAPI {

    OrderAggregate getOrder(Long orderId);
}
