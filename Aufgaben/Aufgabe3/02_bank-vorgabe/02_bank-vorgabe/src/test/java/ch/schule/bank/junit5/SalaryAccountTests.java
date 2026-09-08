package ch.schule.bank.junit5;

import ch.schule.SalaryAccount;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;

class SalaryAccountTests {
    @Test
    void respectsCreditLimit() {
        SalaryAccount account = new SalaryAccount("P-1000", -1000);

        assertFalse(account.withdraw(1, 1001));
    }
}
