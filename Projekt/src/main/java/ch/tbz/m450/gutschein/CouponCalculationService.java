package ch.tbz.m450.gutschein;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Clock;
import java.time.LocalDate;
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
        LocalDate today = LocalDate.now(clock);
        if (orderValue.compareTo(coupon.minimumOrderValue()) < 0) {
            throw new InvalidCouponException("Mindestbestellwert nicht erreicht");
        }
        // Der Gutschein gilt am Ablaufdatum noch; erst der Folgetag ist ungültig.
        if (today.isAfter(coupon.expiresOn())) {
            throw new InvalidCouponException("Gutschein ist abgelaufen");
        }
        BigDecimal discount = orderValue
                .multiply(coupon.percentage())
                .divide(new BigDecimal("100"));
        // Geldbeträge werden kaufmännisch auf zwei Nachkommastellen gerundet.
        return orderValue.subtract(discount).setScale(2, RoundingMode.HALF_UP);
    }
}
