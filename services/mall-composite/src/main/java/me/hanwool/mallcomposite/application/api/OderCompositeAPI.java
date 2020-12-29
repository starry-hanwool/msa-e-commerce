package me.hanwool.mallcomposite.application.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.hanwool.mallutilapp.dto.*;
import me.hanwool.mallcomposite.core.service.OrderCompositeServiceImpl;
import me.hanwool.mallutilapp.exception.NotFoundException;
import me.hanwool.mallutilapp.value.ResponseCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/open-api", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
public class OderCompositeAPI {

    private final OrderCompositeServiceImpl compositeService;

    @GetMapping("/order/{orderId}")
    public ResponseEntity getOrder(@PathVariable Long orderId) {

        OrderAggregate response = null;
        try {
            response = compositeService.getOrder(orderId);
        } catch (Exception e) {
            log.debug(e.getMessage());
            return ResponseEntity.badRequest().body(new ErrorResponse(ResponseCode.FAIL));
        }

        return ResponseEntity.ok().body(response);

    }

}
