package me.hanwool.mallutilapp.port;

import me.hanwool.mallutilapp.dto.*;
import reactor.core.publisher.Mono;

public interface OrderService {

    Mono<OrderDTO> getOrder(String orderNum);
}
