package me.hanwool.orderservice.domain;

import me.hanwool.orderservice.domain.Item;

import javax.persistence.*;
import java.util.List;

@Entity
public class ProductItem {

    @Id @GeneratedValue
    private Long productItemId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "productId")
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ItemId")
    private Item item;

}
