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

/*    @PostMapping(value = "/", produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity placeOrder(@RequestBody OrderDTO orderAggregate) {
//    public ResponseEntity placeOrder(@RequestBody OrderAggregate orderAggregate) {

//        OrderAggregate response = null;
//        OrderDTO response = null;

//        Orders requestOrder = Orders.builder()
//                .orderNum(orderAggregate.getOrderNum())
//                .build();

        // 가주문 생성
        OrderDTO response = orderService.createOrder(orderAggregate);
//        Orders result = orderService.createOrder(requestOrder);

//        response = OrderAggregate.builder()
//                .orderNum(result.getOrderNum())
//                .build();

        return ResponseEntity.ok().body(response);
    }*/

    @GetMapping(value = "/{orderNum}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity getOrder(@PathVariable Long orderNum) {

        log.debug("getOrder - orderNum :{}", orderNum);
        OrderDTO response = null;

        Orders orders = orderService.getOrder(orderNum);
        if(orders == null) {
            throw new NotFoundException();
        }

        response = OrderDTO.builder()
                .orderId(orders.getOrderId())
                .orderNum(orders.getOrderNum())
                .build();

        return ResponseEntity.ok().body(response);
    }

//    @PostMapping(consumes = APPLICATION_JSON_VALUE)

}
