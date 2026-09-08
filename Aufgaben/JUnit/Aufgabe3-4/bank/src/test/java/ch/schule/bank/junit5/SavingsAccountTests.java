package ch.schule.bank.junit5;

import ch.schule.SavingsAccount;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;

class SavingsAccountTests {
    @Test
    void preventsOverdraft() {
        SavingsAccount account = new SavingsAccount("S-1000");

        assertFalse(account.withdraw(1, 1));
    }
}
