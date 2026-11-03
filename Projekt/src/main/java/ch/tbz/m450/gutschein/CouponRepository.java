package ch.tbz.m450.gutschein;

import java.util.Optional;

public interface CouponRepository {
    Optional<Coupon> findByCode(String code);
}
