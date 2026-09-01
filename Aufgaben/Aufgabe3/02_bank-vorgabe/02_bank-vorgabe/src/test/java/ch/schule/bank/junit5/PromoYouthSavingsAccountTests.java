package ch.schule.bank.junit5;

import ch.schule.PromoYouthSavingsAccount;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PromoYouthSavingsAccountTests {
    @Test
    void addsOnePercentBonus() {
        PromoYouthSavingsAccount account = new PromoYouthSavingsAccount("Y-1000");

        assertTrue(account.deposit(1, 10000));
        assertEquals(10100, account.getBalance());
        assertFalse(account.deposit(2, -100));
    }
}
