package me.hanwool.orderservice.domain;

import javax.persistence.*;
import java.util.List;

@Entity
public class Item {

    @Id @GeneratedValue
    private Long itemId;

    private String name;

    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL)
    private List<ProductItem> productItemList;
}
