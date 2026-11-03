package ch.tbz.m450.gutschein;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.nio.file.Path;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class JsonCouponRepositoryIntegrationTest {
    @Test
    void calculatesWithRealJsonRepositoryAndService() {
        CouponRepository repository = new JsonCouponRepository(
                Path.of("src", "test", "resources", "coupons.json"));
        CouponCalculationService service = new CouponCalculationService(repository,
                Clock.fixed(Instant.parse("2026-11-02T12:00:00Z"), ZoneOffset.UTC));

        assertEquals(new BigDecimal("90.00"),
                service.calculate(List.of(new BigDecimal("100.00")), "SCHULE10"));
    }
}
