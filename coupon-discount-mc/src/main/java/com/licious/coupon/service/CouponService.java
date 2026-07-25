package com.licious.coupon.service;

import com.licious.coupon.dto.ApplyCouponRequest;
import com.licious.coupon.dto.ApplyCouponResponse;
import com.licious.coupon.dto.CouponCreateRequest;
import com.licious.coupon.entity.Coupon;

import java.util.List;

public interface CouponService {

    Coupon createCoupon(CouponCreateRequest request);

    ApplyCouponResponse applyCoupon(ApplyCouponRequest request);

    void removeCoupon(Long id);

    List<Coupon> listCoupons();
}
