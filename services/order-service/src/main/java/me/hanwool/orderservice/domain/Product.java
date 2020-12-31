package me.hanwool.orderservice.domain;

import javax.persistence.*;
import java.util.List;

@Entity
public class Product {

    @Id @GeneratedValue
    private Long productId;

    private String name;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<ProductItem> productItemList;
}
