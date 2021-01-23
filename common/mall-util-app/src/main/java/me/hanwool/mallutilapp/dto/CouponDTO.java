package me.hanwool.mallutilapp.dto;

import lombok.*;

@Builder
@Getter
//@NoArgsConstructor(access = AccessLevel.PROTECTED)
//@AllArgsConstructor
public class CouponDTO {

    private final Long couponId;

    public CouponDTO(Long couponId) {
        this.couponId = couponId;
    }
}
