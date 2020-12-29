package me.hanwool.mallutilapp.port;

import me.hanwool.mallutilapp.dto.CouponDTO;
import me.hanwool.mallutilapp.dto.OrderAggregate;
import me.hanwool.mallutilapp.dto.OrderDTO;

public interface OrderCompositeService {

    OrderAggregate createOrderAggregate(OrderDTO _order, CouponDTO _coupon);
}
