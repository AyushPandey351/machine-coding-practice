package com.licious.coupon.controller;

import com.licious.coupon.dto.ApplyCouponRequest;
import com.licious.coupon.dto.ApplyCouponResponse;
import com.licious.coupon.dto.CouponCreateRequest;
import com.licious.coupon.dto.CouponResponse;
import com.licious.coupon.service.CouponService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/coupons")
@RequiredArgsConstructor
public class CouponController {

    private final CouponService couponService;

    @PostMapping
    public ResponseEntity<CouponResponse> createCoupon(@Valid @RequestBody CouponCreateRequest request) {
        CouponResponse response = CouponResponse.from(couponService.createCoupon(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/apply")
    public ResponseEntity<ApplyCouponResponse> applyCoupon(@Valid @RequestBody ApplyCouponRequest request) {
        return ResponseEntity.ok(couponService.applyCoupon(request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeCoupon(@PathVariable Long id) {
        couponService.removeCoupon(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<CouponResponse>> listCoupons() {
        List<CouponResponse> coupons = couponService.listCoupons().stream()
                .map(CouponResponse::from).toList();
        return ResponseEntity.ok(coupons);
    }
}
