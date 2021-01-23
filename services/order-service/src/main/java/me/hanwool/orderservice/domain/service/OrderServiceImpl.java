package me.hanwool.orderservice.domain.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.hanwool.mallutilapp.dto.OrderDTO;
import me.hanwool.mallutilapp.value.Money;
import me.hanwool.mallutilapp.value.OrderStatus;
import me.hanwool.orderservice.domain.Item;
import me.hanwool.orderservice.domain.OrderLine;
import me.hanwool.orderservice.domain.Orders;
import me.hanwool.orderservice.domain.repository.OrderRepository;
import me.hanwool.orderservice.domain.value.MoneyJPA;
import org.apache.kafka.common.errors.InvalidRequestException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository repository;

    private final ProductServiceImpl productService;

    // 가주문 생성
    @Transactional
    @Override
    public OrderDTO createOrder(OrderDTO orderDTO) {

        // 가주문 저장 전 금액 등 유효성 확인
//        if(!validatePrice(orderDTO)) {
//            throw new InvalidRequestException("Invalid price");
//        }

        Orders order = Orders.builder()
                .orderNum(orderDTO.getOrderNum())
                .status(OrderStatus.CREATED)
//                .orderLineList(orderDTO.getOrderLineList().stream() // null point
//                        .filter(e -> e != null)
//                        .map(e -> OrderLine.builder()
//                                .item(Item.builder()
//                                        .price(new MoneyJPA(new BigDecimal(20000)))
//                                        .build())
//                                .build())
//                        .collect(Collectors.toList()))
                .build();

        Orders result = repository.save(order);
        log.debug("saved {} order :{}", result.getStatus(), result.getOrderNum());

        return OrderDTO.builder()
                .orderNum(result.getOrderNum())
                .status(result.getStatus())
                .build();

    }

    @Override
    public Orders getOrder(Long orderNum) {
//    public Orders getOrder(Long orderId) {

        return repository.findByOrderNum(orderNum);
//        Optional<Orders> result = repository.findById(orderId);

//        return result.orElse(null);
    }

    // 금액 재확인
    private boolean validatePrice(OrderDTO orderDTO) {

        boolean result = true;

        // 요청 금액
        Money requestTotalPrice = orderDTO.getOrderLineList().stream()
                .map(ol -> { return ol.getItem().getPrice().multiplyQty(ol.getQuantity()); })
                .reduce(Money.FREE, (subTotal, money) -> subTotal.add(money));

        // 실제 금액
//        Money dbTotalPrice = productService.getTotalPrice(orderDTO.getOrderLineList());

//        orderDTO.getOrderLineList().stream()
//                .map(ol -> { return ProductService.getItemPriceById(ol.getItem().getItemId()); })
////                .map(ol -> { return ol.getItem().getItemId(); })
//                .reduce(Money.FREE, (subTotal, money) -> subTotal.add(money));

//        for(OrderLineDTO orderLineDTO : orderDTO.getOrderLineList()) {
//            += orderLineDTO.getItem().getPrice();
//
//            += ProductService.getItemById(orderLineDTO.getItem().getItemId());
//        result =
//        }

        return result;
    }
}
