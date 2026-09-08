package ch.schule.bank.junit5;

import ch.schule.Bank;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BankTests {
    @Test
    void createsSavingsAccount() {
        Bank bank = new Bank();

        assertEquals("S-1000", bank.createSavingsAccount());
    }
}
