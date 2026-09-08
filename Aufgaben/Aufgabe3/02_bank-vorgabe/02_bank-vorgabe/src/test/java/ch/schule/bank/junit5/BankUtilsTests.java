package ch.schule.bank.junit5;

import ch.schule.BankUtils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BankUtilsTests {
    @Test
    void formatsBankDate() {
        assertEquals("01.01.1970", BankUtils.formatBankDate(0));
    }
}
