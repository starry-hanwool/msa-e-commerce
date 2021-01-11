package me.hanwool.coupon.domain.repository;

import me.hanwool.coupon.domain.Coupon;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

import java.util.Optional;
/*
MongoDB
 */
public interface CouponRepository extends ReactiveMongoRepository<Coupon, Long> {

    Flux<Coupon> findByProductId(Long productId);

}
