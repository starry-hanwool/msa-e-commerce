package me.hanwool.mallcomposite.core.coupon.ports;

import me.hanwool.mallutilapp.dto.*;

public interface CouponService {

//    @GetMapping(value = "/coupon/{couponId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    CouponDTO getCoupon(Long couponId);

}
