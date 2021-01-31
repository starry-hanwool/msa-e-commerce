package me.hanwool.mallcomposite.application.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.hanwool.mallutilapp.dto.*;
import me.hanwool.mallcomposite.domain.service.OrderCompositeServiceImpl;
import me.hanwool.mallutilapp.exception.NotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/open-api")
public class OderCompositeAPI {

    private final OrderCompositeServiceImpl compositeService;

    // 주문요청
    @PostMapping(value = "/order", produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<OrderDTO>> placeOrderComposite(@RequestBody CreateOrderDTO body) throws Exception {
//    public Mono<ResponseEntity<OrderDTO>> placeOrderComposite(@RequestBody OrderDTO body) throws Exception {
        log.debug("postOrderDetails - ");

//        OrderDTO requestBody = OrderDTO.builder()
//                .couponId(body.getCouponId())
//                .orderLineList(body.getOrderLineList())
//                .build();

//        return compositeService.placeOrder(requestBody).map(o -> ResponseEntity.ok(o))
        return compositeService.placeOrder(body).map(o -> ResponseEntity.ok(o))
                .defaultIfEmpty(ResponseEntity.badRequest().build());
    }

    // 주문확인
    @GetMapping(value = "/order/{orderNum}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity getOrderDetails(@PathVariable String orderNum) {
        log.debug("getOrderDetails - request orderNum : {}", orderNum);

        OrderDTO response = compositeService.getOrderDetails(orderNum);

        if (response == null) {
            throw new NotFoundException();
        }

        return ResponseEntity.ok().body(response);
    }
}
