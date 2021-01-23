package me.hanwool.mallutilapp.port;

import me.hanwool.mallutilapp.dto.CouponDTO;
import me.hanwool.mallutilapp.dto.OrderDTO;

public interface OrderCompositeService {

    OrderDTO createOrderDTO(OrderDTO _order, CouponDTO _coupon);
}
