package com.licious.coupon.service;

import com.licious.coupon.dto.ApplyCouponRequest;
import com.licious.coupon.dto.ApplyCouponResponse;
import com.licious.coupon.dto.CartItemRequest;
import com.licious.coupon.dto.CouponCreateRequest;
import com.licious.coupon.entity.Coupon;
import com.licious.coupon.entity.CouponRedemption;
import com.licious.coupon.entity.Product;
import com.licious.coupon.entity.User;
import com.licious.coupon.exception.*;
import com.licious.coupon.repository.CouponRedemptionRepository;
import com.licious.coupon.repository.CouponRepository;
import com.licious.coupon.repository.ProductRepository;
import com.licious.coupon.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class CouponServiceImpl implements CouponService {

    private final CouponRepository couponRepository;
    private final CouponRedemptionRepository redemptionRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public Coupon createCoupon(CouponCreateRequest request) {
        if (couponRepository.existsByCodeIgnoreCase(request.code())) {
            throw new DuplicateCouponException("Coupon already exists with code: " + request.code());
        }

        Set<Product> applicableProducts = new HashSet<>();
        if (request.applicableProductIds() != null) {
            for (Long productId : request.applicableProductIds()) {
                Product product = productRepository.findById(productId)
                        .orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + productId));
                applicableProducts.add(product);
            }
        }

        Coupon coupon = new Coupon(request.code(), request.discountType(), request.discountValue(),
                request.minCartValue(), request.maxDiscountAmount(), applicableProducts,
                request.usageLimitPerUser(), request.totalUsageLimit(), request.expiryDate());
        return couponRepository.save(coupon);
    }

    @Override
    @Transactional
    public ApplyCouponResponse applyCoupon(ApplyCouponRequest request) {
        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + request.userId()));

        // Lock the coupon row for the whole validate-and-redeem sequence below,
        // so two concurrent applications of the same code can't both read
        // currentUsageCount under the limit and both proceed.
        Coupon coupon = couponRepository.findByCodeForUpdate(request.couponCode())
                .orElseThrow(() -> new CouponNotFoundException("Coupon not found with code: " + request.couponCode()));

        if (!coupon.isActive()) {
            throw new InvalidCouponException("Coupon " + coupon.getCode() + " is no longer active");
        }
        if (coupon.isExpired(Instant.now())) {
            throw new InvalidCouponException("Coupon " + coupon.getCode() + " has expired");
        }
        if (coupon.hasReachedGlobalLimit()) {
            throw new InvalidCouponException("Coupon " + coupon.getCode() + " has reached its total usage limit");
        }
        long alreadyUsedByUser = redemptionRepository.countByCoupon_IdAndUser_Id(coupon.getId(), user.getId());
        if (alreadyUsedByUser >= coupon.getUsageLimitPerUser()) {
            throw new InvalidCouponException(
                    "User " + user.getId() + " has already redeemed coupon " + coupon.getCode() + " the maximum number of times");
        }

        BigDecimal cartTotal = BigDecimal.ZERO;
        BigDecimal eligibleAmount = BigDecimal.ZERO;
        for (CartItemRequest item : request.items()) {
            Product product = productRepository.findById(item.productId())
                    .orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + item.productId()));
            BigDecimal lineTotal = product.getPrice().multiply(BigDecimal.valueOf(item.quantity()));
            cartTotal = cartTotal.add(lineTotal);
            if (coupon.appliesTo(product.getId())) {
                eligibleAmount = eligibleAmount.add(lineTotal);
            }
        }

        if (eligibleAmount.compareTo(coupon.getMinCartValue()) < 0) {
            throw new InvalidCouponException(
                    "Cart's eligible amount " + eligibleAmount + " is below coupon " + coupon.getCode()
                            + "'s minimum of " + coupon.getMinCartValue());
        }

        BigDecimal discountAmount = coupon.computeDiscount(eligibleAmount);
        BigDecimal finalTotal = cartTotal.subtract(discountAmount);

        coupon.incrementUsage();
        redemptionRepository.save(new CouponRedemption(coupon, user, cartTotal, discountAmount, finalTotal));

        return new ApplyCouponResponse(coupon.getCode(), cartTotal, eligibleAmount, discountAmount, finalTotal);
    }

    @Override
    @Transactional
    public void removeCoupon(Long id) {
        Coupon coupon = couponRepository.findByIdForUpdate(id)
                .orElseThrow(() -> new CouponNotFoundException("Coupon not found with id: " + id));
        coupon.deactivate();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Coupon> listCoupons() {
        return couponRepository.findAll();
    }
}
