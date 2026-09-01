package ch.schule.bank.junit5;

import ch.schule.SalaryAccount;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SalaryAccountTests {
    @Test
    void respectsCreditLimit() {
        SalaryAccount account = new SalaryAccount("P-1000", -10000);

        assertTrue(account.withdraw(1, 10000));
        assertFalse(account.withdraw(2, 1));
        assertFalse(account.withdraw(2, -1));
        assertEquals(-10000, account.getBalance());
    }
}
