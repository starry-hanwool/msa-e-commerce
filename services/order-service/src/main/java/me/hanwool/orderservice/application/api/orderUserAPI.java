package me.hanwool.orderservice.application.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.hanwool.mallutilapp.dto.*;
import me.hanwool.mallutilapp.exception.NotFoundException;
import me.hanwool.mallutilapp.value.ResponseCode;
import me.hanwool.orderservice.domain.Orders;
import me.hanwool.orderservice.domain.service.OrderServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/order")
public class orderUserAPI {

    private final OrderServiceImpl orderService;

    @GetMapping(value = "/{orderId}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity getOrder(@PathVariable Long orderId) {
//    public ResponseEntity getOrder(@PathVariable Long orderId) {

        log.debug("getOrder - orderId :{}", orderId);
        OrderDTO response = null;

//        try {
//            ModelMapper modelMapper = new ModelMapper();
//            response = modelMapper.map(orderService.getOrder(orderId), OrderDTO.class);
//            response = OrderMapper.INSTANCE.ordersToDTO(orderService.getOrder(orderId));
            Orders orders = orderService.getOrder(orderId);
            if(orders == null) {
                throw new NotFoundException();
            }

            response = OrderDTO.builder()
                    .orderId(orders.getOrderId())
                    .build();

//        } catch (Exception e) {
//            log.debug(e.getMessage());
//            return ResponseEntity.badRequest().body(new ErrorResponse(ResponseCode.FAIL));
//        }

        return ResponseEntity.ok().body(response);
    }

//    @PostMapping(consumes = APPLICATION_JSON_VALUE)

}
