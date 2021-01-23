package me.hanwool.mallutilapp.dto;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.*;
import me.hanwool.mallutilapp.value.OrderStatus;

import java.util.List;

@Getter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonDeserialize(builder = OrderDTO.defaultBuilder.class)
@EqualsAndHashCode(of = {"orderNum"})
@ToString
public class OrderDTO {

    private Long orderId;
    private Long orderNum;
    private List<OrderLineDTO> orderLineList;
    private Long couponId;
    private OrderStatus status;

    @JsonCreator
    @Builder(builderClassName = "defaultBuilder")
    public OrderDTO(@JsonProperty("orderId") Long orderId, @JsonProperty("orderNum") Long orderNum,
                    @JsonProperty("orderLineList") List<OrderLineDTO> orderLineList, @JsonProperty("couponId") Long couponId,
                    @JsonProperty("status") OrderStatus status) {
        this.orderId = orderId;
        this.orderNum = orderNum;
        this.orderLineList = orderLineList;
        this.couponId = couponId;
        this.status = status;
    }
}
