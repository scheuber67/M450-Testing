package ch.schule.bank.junit5;

import ch.schule.Account;
import ch.schule.AccountBalanceComparator;
import ch.schule.SavingsAccount;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class AccountBalanceComparatorTests {
    @Test
    void sortsHigherBalanceFirst() {
        Account low = new SavingsAccount("S-1");
        Account high = new SavingsAccount("S-2");
        high.deposit(1, 1000);

        assertTrue(new AccountBalanceComparator().compare(low, high) > 0);
    }
}
