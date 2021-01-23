package me.hanwool.mallutilapp.value;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

//@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
public class Money {

    public static final Money FREE = new Money(BigDecimal.ZERO);
    public static final Money ONE = new Money(BigDecimal.ONE);
    private BigDecimal amount;

    public Money(BigDecimal amount) {
        this.amount = amount;
    }

    /* 연산 */
    public Money add(Money money) {
        return new Money(this.amount.add(money.amount));
    }

    public Money subtract(Money money) {
        return new Money(this.amount.subtract(money.amount));
    }

    public Money multiplyQty(Integer quantity) {
        return new Money(this.amount.multiply(new BigDecimal(quantity)));
    }

}
