package me.hanwool.mallutilapp.dto;

import lombok.*;

@Getter
public class OrderDTO {

    private final Long orderId;

    public OrderDTO() {
        orderId = null;
    }

    public OrderDTO(Long orderId) {
        this.orderId = orderId;
    }
}
