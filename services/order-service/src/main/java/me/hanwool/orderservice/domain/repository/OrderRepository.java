package me.hanwool.orderservice.domain.repository;

import me.hanwool.orderservice.domain.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Orders, Long> {

    Orders findByOrderNum(String orderNum);

}
