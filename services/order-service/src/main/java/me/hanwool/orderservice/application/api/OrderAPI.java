package me.hanwool.orderservice.application.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.hanwool.mallutilapp.dto.*;
import me.hanwool.mallutilapp.exception.NotFoundException;
import me.hanwool.orderservice.domain.Orders;
import me.hanwool.orderservice.domain.service.OrderServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/order")
public class OrderAPI {

    private final OrderServiceImpl orderService;

    @GetMapping(value = "/{orderId}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity getOrder(@PathVariable Long orderId) {

        log.debug("getOrder - orderId :{}", orderId);
        OrderDTO response = null;

        Orders orders = orderService.getOrder(orderId);
        if(orders == null) {
            throw new NotFoundException();
        }

        response = OrderDTO.builder()
                .orderId(orders.getOrderId())
                .build();

        return ResponseEntity.ok().body(response);
    }

//    @PostMapping(consumes = APPLICATION_JSON_VALUE)

}
