package me.hanwool.coupon.domain.repository;

import me.hanwool.coupon.domain.Coupon;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;
/*
MongoDB
 */
public interface CouponRepository extends PagingAndSortingRepository<Coupon, Long> {

    Optional<Coupon> findByProductId(Long productId);

}
