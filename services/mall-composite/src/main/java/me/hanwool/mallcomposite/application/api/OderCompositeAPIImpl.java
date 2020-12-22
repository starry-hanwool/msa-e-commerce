package me.hanwool.mallcomposite.application.api;

import lombok.RequiredArgsConstructor;
import me.hanwool.mallutilapp.dto.*;
import me.hanwool.mallcomposite.core.OrderAggregate;
import me.hanwool.mallcomposite.core.service.OrderCompositeIntegration;
import me.hanwool.mallcomposite.core.service.OrderCompositeService;
import me.hanwool.mallcomposite.exception.NotFoundException;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/open-api", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
public class OderCompositeAPIImpl implements OderCompositeAPI {

//    @Autowired
    private final OrderCompositeService compositeService;
    private final OrderCompositeIntegration integration;

    @Override
    @GetMapping("/order/{orderId}")
    public OrderAggregate getOrder(@PathVariable Long orderId) {
        OrderDTO order = integration.getOrder(orderId);
        if (order == null) throw new NotFoundException("No order found for orderId: " + orderId);

        CouponDTO coupon = null;

        return compositeService.createOrderAggregate(order, coupon);
    }

}
