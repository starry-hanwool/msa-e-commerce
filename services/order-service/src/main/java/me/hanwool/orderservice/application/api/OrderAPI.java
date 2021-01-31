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

    @GetMapping(value = "/{orderNum}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity getOrder(@PathVariable String orderNum) {

        log.debug("getOrder - orderNum :{}", orderNum);

        Orders orders = orderService.getOrder(orderNum);
        if(orders == null) {
            throw new NotFoundException();
        }

        OrderDTO response = OrderDTO.builder()
                .orderNum(orders.getOrderNum())
                .build();

        return ResponseEntity.ok().body(response);
    }

}
