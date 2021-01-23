package me.hanwool.orderservice.domain;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import me.hanwool.mallutilapp.value.Money;
import me.hanwool.mallutilapp.value.OrderStatus;
import me.hanwool.orderservice.domain.value.MoneyJPA;
import org.springframework.data.annotation.Version;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.util.Assert;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
//@Builder
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Orders {

    @Id @GeneratedValue
    private Long orderId;

    @Column(nullable = false)
    private Long orderNum;

    @OneToMany(mappedBy = "orders", cascade = CascadeType.ALL)
    private List<OrderLine> orderLineList;

    private Long couponId; // 쿠폰 애그리거트

    @Embedded
    private MoneyJPA totalPrice; // 총 금액

    @Embedded
    private MoneyJPA finalPrice; // (할인 적용 후) 최종 금액

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @DateTimeFormat(pattern = "yyyy/MM/dd kk:mm:ss")
    private LocalDateTime createdDate;

    @Builder
    public Orders(Long orderId, Long orderNum, int version, List<OrderLine> orderLineList, OrderStatus status, LocalDateTime createdDate) {
//        Assert.notNull(orderNum, "orderNum must not be null");
//        Assert.notNull(status, "status must not be null");
        this.orderId = orderId;
        this.orderNum = orderNum;
        this.orderLineList = orderLineList;
        this.status = status;
        this.createdDate = createdDate;
    }

//    public boolean validate() {
//
//
//    }
}
