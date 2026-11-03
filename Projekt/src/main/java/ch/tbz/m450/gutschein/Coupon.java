package ch.tbz.m450.gutschein;

import java.math.BigDecimal;
import java.time.LocalDate;

public record Coupon(
        String code,
        BigDecimal percentage,
        BigDecimal minimumOrderValue,
        LocalDate expiresOn) {
}
