package me.hanwool.orderservice.domain;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Builder
//@Entity
//@Value
@Getter
//@RequiredArgsConstructor
//@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Orders {

//    @Id @GeneratedValue
    private Long orderId;

    private Long productId;

    private LocalDateTime createdDate;

}
