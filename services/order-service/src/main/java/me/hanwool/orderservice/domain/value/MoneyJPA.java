package me.hanwool.orderservice.domain.value;

import me.hanwool.mallutilapp.value.Money;

import javax.persistence.Embeddable;
import java.math.BigDecimal;

@Embeddable
public class MoneyJPA extends Money {

    public MoneyJPA() {
        super();
    }

    public MoneyJPA(BigDecimal amount) {
        super(amount);
    }
}
