package me.hanwool.orderservice.domain;

import lombok.*;
import org.springframework.data.annotation.Version;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Builder
@Entity
@Getter
//@RequiredArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Orders {

    @Id @GeneratedValue
    private Long orderId;

    private Long orderNum;

    @Version
    private int version;

    private Long productId;

    private LocalDateTime createdDate;

}
