package me.hanwool.mallutilapp.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.*;

import java.util.List;

@Getter
@NoArgsConstructor
@JsonDeserialize(builder = CreateOrderDTO.defaultBuilder.class)
@ToString
public class CreateOrderDTO {

    private List<OrderLineDTO> orderLineList;
    private Long couponId;

    @JsonCreator
    @Builder(builderClassName = "defaultBuilder")
    public CreateOrderDTO(@JsonProperty("orderLineList") List<OrderLineDTO> orderLineList, @JsonProperty("couponId") Long couponId) {
        this.orderLineList = orderLineList;
        this.couponId = couponId;
    }
}
