package me.hanwool.orderservice.domain;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import me.hanwool.mallutilapp.value.OrderStatus;
import me.hanwool.orderservice.domain.value.MoneyJPA;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Orders {

    @Id @GeneratedValue
    private Long orderId;

    @Column(nullable = false, unique = true, length = 40)
    private String orderNum;

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
    public Orders(Long orderId, String orderNum, int version, List<OrderLine> orderLineList, OrderStatus status, LocalDateTime createdDate) {
        this.orderId = orderId;
        this.orderNum = orderNum;
        this.orderLineList = orderLineList;
        this.status = status;
        this.createdDate = createdDate;
    }

    // 주문번호 생성
    public void generateOrderNum() {
        String now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String randomStr = String.valueOf(1000 + new SecureRandom().nextInt(1000000000));
        String orderNum = now + randomStr;

        // 주문번호 중복시 처리 필요
        // ...

        this.orderNum = orderNum;
    }
}
