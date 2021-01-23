package me.hanwool.orderservice.domain;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
public class Product {

    @Id @GeneratedValue
    private Long productId;

    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "orderId")
    private Orders orders;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<Item> itemList;

}
