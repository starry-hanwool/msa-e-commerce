package me.hanwool.mallutilapp.dto;

import lombok.*;

import java.util.List;

@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@EqualsAndHashCode
public class ProductDTO {

    private Long productId;

    private String name;

//    private OrderDTO orders;

    private List<ItemDTO> itemList;
}
