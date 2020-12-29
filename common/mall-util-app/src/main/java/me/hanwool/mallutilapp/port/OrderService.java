package me.hanwool.mallutilapp.port;

import me.hanwool.mallutilapp.dto.*;

public interface OrderService {

    OrderDTO getOrder(Long orderId);
}
