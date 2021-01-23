package me.hanwool.orderservice.domain.repository;

import me.hanwool.orderservice.domain.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Orders, Long> {



}
