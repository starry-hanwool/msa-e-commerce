package me.hanwool.mallutilapp.dto;

import lombok.*;

@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class OrderLineDTO {

    private Long orderLineId;

//    private OrderDTO orders;

//    private ProductDTO product;

    private ItemDTO item;

    private int quantity;
}
