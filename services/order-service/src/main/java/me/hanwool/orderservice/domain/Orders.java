package me.hanwool.orderservice.domain;

import lombok.Builder;
import lombok.RequiredArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Builder
@RequiredArgsConstructor
public class Orders {

    @Id @GeneratedValue
    private Long OrderId;

}
