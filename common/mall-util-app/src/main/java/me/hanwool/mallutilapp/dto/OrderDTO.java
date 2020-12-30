package me.hanwool.mallutilapp.dto;

import lombok.*;

@Builder
//@Value
@Getter
//@RequiredArgsConstructor
@AllArgsConstructor
//@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class OrderDTO {

    private Long orderId;
    private Long productId;

}
