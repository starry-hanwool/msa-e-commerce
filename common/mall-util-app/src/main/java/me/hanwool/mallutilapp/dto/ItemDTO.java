package me.hanwool.mallutilapp.dto;

import lombok.*;
import me.hanwool.mallutilapp.value.Money;

import java.util.List;

@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ItemDTO {
    private Long itemId;

    private String name;

    private Money price;

    private ProductDTO productDTO;

//    private List<ProductItem> productItemList;
}
