package ch.schule.bank.junit5;

import ch.schule.Account;
import ch.schule.SavingsAccount;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AccountTests {
    @Test
    void depositIncreasesBalance() {
        Account account = new SavingsAccount("S-1000");
        account.deposit(1, 1000);

        assertEquals(1000, account.getBalance());
    }
}
