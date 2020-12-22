package me.hanwool.mallcomposite.core.order.ports;

import me.hanwool.mallutilapp.dto.*;

public interface OrderService {

    OrderDTO getOrder(Long orderId);
}
