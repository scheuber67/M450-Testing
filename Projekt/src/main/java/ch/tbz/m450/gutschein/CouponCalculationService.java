package ch.tbz.m450.gutschein;

import java.math.BigDecimal;
import java.time.Clock;
import java.util.List;

public class CouponCalculationService {
    private final CouponRepository couponRepository;
    private final Clock clock;

    public CouponCalculationService(CouponRepository couponRepository, Clock clock) {
        this.couponRepository = couponRepository;
        this.clock = clock;
    }

    public BigDecimal calculate(List<BigDecimal> articlePrices, String code) {
        BigDecimal orderValue = articlePrices.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
        Coupon coupon = couponRepository.findByCode(code)
                .orElseThrow(() -> new InvalidCouponException("Unbekannter Gutschein: " + code));
        // Die Mindestbestellwert-Regel folgt im nächsten Schritt.
        return orderValue;
    }
}
