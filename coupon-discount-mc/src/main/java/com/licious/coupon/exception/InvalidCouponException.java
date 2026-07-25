package com.licious.coupon.exception;

/**
 * Umbrella for business-rule violations on coupon redemption (inactive,
 * expired, minimum cart value not met, usage limit exceeded, no eligible
 * items in cart) - all map to the same 409 response, distinguished by
 * message rather than by a growing family of near-identical exception types.
 */
public class InvalidCouponException extends RuntimeException {
    public InvalidCouponException(String message) {
        super(message);
    }
}
