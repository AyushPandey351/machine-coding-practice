package com.licious.coupon.repository;

import com.licious.coupon.entity.Coupon;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CouponRepository extends JpaRepository<Coupon, Long> {

    Optional<Coupon> findByCodeIgnoreCase(String code);

    boolean existsByCodeIgnoreCase(String code);

    /**
     * SELECT ... FOR UPDATE keyed by code - the apply-coupon path locks this
     * row for the duration of the transaction so concurrent redemptions of
     * the same coupon can't all read currentUsageCount below the limit and
     * all proceed (the same oversell race as inventory stock, here against
     * totalUsageLimit instead of stock quantity).
     */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select c from Coupon c where lower(c.code) = lower(:code)")
    Optional<Coupon> findByCodeForUpdate(@Param("code") String code);

    /** Same lock, keyed by id - used by removeCoupon so it serializes against a concurrent apply. */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select c from Coupon c where c.id = :id")
    Optional<Coupon> findByIdForUpdate(@Param("id") Long id);
}
