package ch.schule.bank.junit5;

import ch.schule.PromoYouthSavingsAccount;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PromoYouthSavingsAccountTests {
    @Test
    void addsBonus() {
        PromoYouthSavingsAccount account = new PromoYouthSavingsAccount("Y-1000");
        account.deposit(1, 10000);

        assertEquals(10100, account.getBalance());
    }
}
