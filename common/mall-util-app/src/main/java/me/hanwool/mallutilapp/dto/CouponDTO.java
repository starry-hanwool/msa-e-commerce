package me.hanwool.mallutilapp.dto;

import lombok.Getter;

@Getter
public class CouponDTO {

    private final Long couponId;

    public CouponDTO(Long couponId) {
        this.couponId = couponId;
    }
}
