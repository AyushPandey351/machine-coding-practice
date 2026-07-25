package com.licious.coupon.repository;

import com.licious.coupon.entity.CouponRedemption;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponRedemptionRepository extends JpaRepository<CouponRedemption, Long> {

    long countByCoupon_IdAndUser_Id(Long couponId, Long userId);
}
