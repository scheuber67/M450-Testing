package ch.tbz.m450.gutschein;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

public class JsonCouponRepository implements CouponRepository {
    private final List<Coupon> coupons;

    public JsonCouponRepository(Path jsonFile) {
        try {
            try (var input = Files.newInputStream(jsonFile)) {
                this.coupons = new ObjectMapper().findAndRegisterModules()
                        .readValue(input, new TypeReference<>() {
                        });
            }
        } catch (IOException exception) {
            throw new IllegalStateException("Gutscheine konnten nicht geladen werden", exception);
        }
    }

    @Override
    public Optional<Coupon> findByCode(String code) {
        return coupons.stream().filter(coupon -> coupon.code().equals(code)).findFirst();
    }
}
