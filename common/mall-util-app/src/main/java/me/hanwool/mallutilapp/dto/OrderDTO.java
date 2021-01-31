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

    private String orderNum;
    private List<OrderLineDTO> orderLineList;
    private OrderStatus status;

    @JsonCreator
    @Builder(builderClassName = "defaultBuilder")
    public OrderDTO(@JsonProperty("orderNum") String orderNum,
                    @JsonProperty("orderLineList") List<OrderLineDTO> orderLineList,
                    @JsonProperty("status") OrderStatus status) {
        this.orderNum = orderNum;
        this.orderLineList = orderLineList;
        this.status = status;
    }
}
