package ch.schule.bank.junit5;

import ch.schule.Account;
import ch.schule.AccountInverseBalanceComparator;
import ch.schule.SavingsAccount;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class AccountInverseBalanceComparatorTests {
    @Test
    void sortsLowerBalanceFirst() {
        Account low = new SavingsAccount("S-1");
        Account high = new SavingsAccount("S-2");
        high.deposit(1, 1000);

        assertTrue(new AccountInverseBalanceComparator().compare(low, high) < 0);
    }
}
