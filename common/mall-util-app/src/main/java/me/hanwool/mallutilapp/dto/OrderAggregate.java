package me.hanwool.mallutilapp.dto;

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
