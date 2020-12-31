package me.hanwool.orderservice.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class OrderLine {

    @Id @GeneratedValue
    private Long orderLineId;

    private Long productId;

    private int quantity;

}
