package ch.tbz.m450.gutschein;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

class JsonCouponRepositoryIntegrationTest {
    @TempDir
    Path temporaryDirectory;

    @Test
    void rejectsMissingJsonFile() {
        Path missingFile = temporaryDirectory.resolve("missing.json");

        IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> new JsonCouponRepository(missingFile));

        assertEquals("Gutscheine konnten nicht geladen werden", exception.getMessage());
        assertInstanceOf(NoSuchFileException.class, exception.getCause());
    }

    @Test
    void rejectsMalformedJsonFile() throws IOException {
        Path malformedFile = temporaryDirectory.resolve("malformed.json");
        Files.writeString(malformedFile, "[{ invalid json }]");

        IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> new JsonCouponRepository(malformedFile));

        assertEquals("Gutscheine konnten nicht geladen werden", exception.getMessage());
        assertInstanceOf(JsonProcessingException.class, exception.getCause());
    }

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
