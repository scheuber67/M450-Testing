package ch.tbz.m450.gutschein;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CouponCalculationServiceTest {
    @Test
    void rejectsOrderBelowMinimumValue() {
        CouponRepository repository = mock(CouponRepository.class);
        Coupon coupon = new Coupon(
                "SCHULE10", new BigDecimal("10"), new BigDecimal("50.00"),
                java.time.LocalDate.of(2026, 12, 31));
        when(repository.findByCode("SCHULE10")).thenReturn(java.util.Optional.of(coupon));
        CouponCalculationService service = new CouponCalculationService(
                repository, Clock.fixed(Instant.parse("2026-11-02T12:00:00Z"), ZoneOffset.UTC));

        assertThrows(InvalidCouponException.class,
                () -> service.calculate(List.of(new BigDecimal("49.99")), "SCHULE10"));
    }

    @Test
    void acceptsOrderExactlyAtMinimumAndRoundsHalfUp() {
        CouponRepository repository = mock(CouponRepository.class);
        when(repository.findByCode("SCHULE10")).thenReturn(java.util.Optional.of(new Coupon(
                "SCHULE10", new BigDecimal("10"), new BigDecimal("50"),
                java.time.LocalDate.of(2026, 12, 31))));
        CouponCalculationService service = new CouponCalculationService(repository,
                Clock.fixed(Instant.parse("2026-11-02T12:00:00Z"), ZoneOffset.UTC));

        assertEquals(new BigDecimal("49.50"),
                service.calculate(List.of(new BigDecimal("20.00"), new BigDecimal("30.00")), "SCHULE10"));
    }

    @Test
    void acceptsCouponOnItsExpiryDate() {
        CouponRepository repository = mock(CouponRepository.class);
        when(repository.findByCode("SCHULE10")).thenReturn(java.util.Optional.of(new Coupon(
                "SCHULE10", new BigDecimal("10"), new BigDecimal("50"),
                java.time.LocalDate.of(2026, 11, 2))));
        CouponCalculationService service = new CouponCalculationService(repository,
                Clock.fixed(Instant.parse("2026-11-02T23:59:59Z"), ZoneOffset.UTC));

        assertEquals(new BigDecimal("45.00"),
                service.calculate(List.of(new BigDecimal("50")), "SCHULE10"));
    }

    @Test
    void rejectsUnknownAndExpiredCoupons() {
        CouponRepository repository = mock(CouponRepository.class);
        when(repository.findByCode("UNKNOWN")).thenReturn(java.util.Optional.empty());
        when(repository.findByCode("ALT20")).thenReturn(java.util.Optional.of(new Coupon(
                "ALT20", new BigDecimal("20"), new BigDecimal("10"),
                java.time.LocalDate.of(2026, 11, 1))));
        CouponCalculationService service = new CouponCalculationService(repository,
                Clock.fixed(Instant.parse("2026-11-02T12:00:00Z"), ZoneOffset.UTC));

        assertThrows(InvalidCouponException.class,
                () -> service.calculate(List.of(new BigDecimal("50")), "UNKNOWN"));
        assertThrows(InvalidCouponException.class,
                () -> service.calculate(List.of(new BigDecimal("50")), "ALT20"));
    }
}
