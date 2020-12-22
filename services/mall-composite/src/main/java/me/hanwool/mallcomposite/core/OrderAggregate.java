package me.hanwool.mallcomposite.core;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

//@RequiredArgsConstructor
@Builder
@Getter
public class OrderAggregate {

    private Long orderId;
    private Long couponId;

}
