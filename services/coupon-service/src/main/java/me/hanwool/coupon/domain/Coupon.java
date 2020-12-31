package me.hanwool.coupon.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "coupons")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Coupon {

    @Id
    private String couponId;

    @Version
    private Integer version;

    private String name;

    @Indexed(unique = true)
    private Long productId;

    public Coupon(Integer version, String name, Long productId) {
        this.version = version;
        this.name = name;
        this.productId = productId;
    }
}
