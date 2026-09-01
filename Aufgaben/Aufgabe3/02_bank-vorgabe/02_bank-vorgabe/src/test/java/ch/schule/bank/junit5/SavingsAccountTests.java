package ch.schule.bank.junit5;

import ch.schule.SavingsAccount;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SavingsAccountTests {
    @Test
    void cannotGoBelowZero() {
        SavingsAccount account = new SavingsAccount("S-1000");
        account.deposit(1, 5000);

        assertFalse(account.withdraw(2, 5001));
        assertTrue(account.withdraw(2, 5000));
        assertFalse(account.withdraw(3, -1));
    }
}
