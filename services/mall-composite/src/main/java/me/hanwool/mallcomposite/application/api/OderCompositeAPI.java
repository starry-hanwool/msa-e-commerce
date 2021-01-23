package me.hanwool.mallcomposite.application.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.hanwool.mallutilapp.dto.*;
import me.hanwool.mallcomposite.core.service.OrderCompositeServiceImpl;
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
    public Mono<ResponseEntity<OrderDTO>> placeOrderComposite(@RequestBody OrderDTO body) throws Exception {

        log.debug("postOrderDetails - ");

        // 쿠폰 유효성 체크
//      CouponDTO = compositeService.getCheckedCoupon();
        // 주문 생성 요청
//        Mono<OrderDTO> createdOrder = compositeService.placeOrder(body);
//        Mono<OrderDTO> createdOrder = Mono.just(OrderDTO.builder().orderId(1L).build()); // test
//        Mono<OrderDTO> createdOrder = Mono.empty();

//        log.debug("postOrderDetails - compositeService {}, createdOrder:{}", compositeService, createdOrder);

//        if (createdOrder == null) {
//            throw new NotFoundException();
//        }

//        OrderDTO order = compositeService.placeOrder(body);
//        log.debug("postOrderDetails - {}", order);
//
//        return ResponseEntity.ok().body(order);

//        return ResponseEntity.ok().body(compositeService.placeOrder(body));
        return compositeService.placeOrder(body).map(o -> ResponseEntity.ok(o))
                .defaultIfEmpty(ResponseEntity.badRequest().build());

//        return createdOrder.flatMap(v -> ResponseEntity.ok(createdOrder)).switchIfEmpty(v -> ResponseEntity.ok(createdOrder));
        /*return createdOrder
                .map(order -> {
                    if (order == null) {
                        return Mono.error(new NotFoundException());
                    }
                    return Mono.just(ResponseEntity.ok(order));
                })
//                .map(order -> ResponseEntity.ok(order))
//                .cast(ResponseEntity.class)
                .defaultIfEmpty(ResponseEntity.notFound().build());
//                .switchIfEmpty(Mono.error(new NotFoundException()));*/
    }

    @GetMapping(value = "/order/{orderNum}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity getOrderDetails(@PathVariable Long orderNum) {

        log.debug("getOrderDetails - request orderNum : {}", orderNum);

        OrderDTO response = null;
        response = compositeService.getOrderDetails(orderNum);

        if (response == null) {
            throw new NotFoundException();
        }

        return ResponseEntity.ok().body(response);
    }

    /*@GetMapping("/users/{userId}")
    public Mono<ResponseEntity<User>> get(@PathVariable Long userId) {
        Mono<User> userMono = userService.getById(userId); return userMono.map((user) -> { if (user.isAdult()) { return ResponseEntity.ok().header("X-User-Adult", "true").build(); } return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); }); }
*/
    /*@PutMapping("/tweets/{id}")
    public Mono<ResponseEntity<Tweet>> updateTweet(@PathVariable(value = "id") String tweetId,
                                                   @Valid @RequestBody Tweet tweet) {
        return tweetRepository.findById(tweetId)
                .flatMap(existingTweet -> {
                    existingTweet.setText(tweet.getText());
                    return tweetRepository.save(existingTweet);
                })
                .map(updatedTweet -> new ResponseEntity<>(updatedTweet, HttpStatus.OK))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }*/
}
