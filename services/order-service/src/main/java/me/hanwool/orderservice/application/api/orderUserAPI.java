package me.hanwool.orderservice.application.api;

import lombok.extern.slf4j.Slf4j;
import me.hanwool.mallutilapp.dto.*;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import me.hanwool.mallutilapp.exception.*;

@Slf4j
@RestController
@RequestMapping(value = "/api", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
public class orderUserAPI {

    @GetMapping("/order/{orderId}")
    public OrderDTO getOrder(@PathVariable Long orderId) {

        log.debug("getOrder orderId :{}", orderId);
        // test
        if (orderId == 13) throw new NotFoundException("No order found for orderId: " + orderId);

        return new OrderDTO(orderId);
    }

}
