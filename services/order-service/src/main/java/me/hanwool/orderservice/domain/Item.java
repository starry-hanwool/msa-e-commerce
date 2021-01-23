package me.hanwool.orderservice.domain;

import lombok.*;
import me.hanwool.orderservice.domain.value.MoneyJPA;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
public class Item {

    @Id @GeneratedValue
    private Long itemId;

    private String name;

    @Embedded
    private MoneyJPA price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "productId")
    private Product product;

    @OneToOne(mappedBy = "item", fetch = FetchType.LAZY)
    private OrderLine orderLine;
}
