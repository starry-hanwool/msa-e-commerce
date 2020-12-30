package me.hanwool.orderservice.application.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.hanwool.mallutilapp.dto.*;
import me.hanwool.mallutilapp.value.ResponseCode;
import me.hanwool.orderservice.domain.Orders;
import me.hanwool.orderservice.domain.mapper.OrderMapper;
import me.hanwool.orderservice.domain.service.OrderServiceImpl;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
public class orderUserAPI {

    private final OrderServiceImpl orderService;

    @GetMapping("/order/{orderId}")
    public ResponseEntity getOrder(@PathVariable Long orderId) {

        log.debug("getOrder orderId :{}", orderId);
        OrderDTO response = null;

        try {
//            ModelMapper modelMapper = new ModelMapper();
//            response = modelMapper.map(orderService.getOrder(orderId), OrderDTO.class);
            response = OrderMapper.INSTANCE.ordersToDTO(orderService.getOrder(orderId));

        } catch (Exception e) {
            log.debug(e.getMessage());
            return ResponseEntity.badRequest().body(new ErrorResponse(ResponseCode.FAIL));
        }

        return ResponseEntity.ok().body(response);
    }

}
