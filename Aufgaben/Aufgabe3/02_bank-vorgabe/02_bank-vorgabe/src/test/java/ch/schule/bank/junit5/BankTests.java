package ch.schule.bank.junit5;

import ch.schule.Account;
import ch.schule.AccountBalanceComparator;
import ch.schule.AccountInverseBalanceComparator;
import ch.schule.Bank;
import ch.schule.SavingsAccount;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

class BankTests {
    @Test
    void testBank() {
        createAccounts();
        depositAndWithdraw();
        totalBalanceAndReference();
        printAndSortAccounts();
        comparatorsHandleEqualBalances();
    }

    private void createAccounts() {
        Bank bank = new Bank();
        assertEquals("S-1000", bank.createSavingsAccount());
        assertEquals("Y-1001", bank.createPromoYouthSavingsAccount());
        assertEquals("P-1002", bank.createSalaryAccount(-5000));
        assertNull(bank.createSalaryAccount(5000));
    }

    private void depositAndWithdraw() {
        Bank bank = new Bank();
        String id = bank.createSavingsAccount();

        assertTrue(bank.deposit(id, 1, 10000));
        assertTrue(bank.withdraw(id, 2, 4000));
        assertEquals(6000, bank.getBalance(id));
        assertFalse(bank.deposit("unknown", 1, 100));
        assertFalse(bank.withdraw("unknown", 1, 100));
        assertEquals(0, bank.getBalance("unknown"));
    }

    private void totalBalanceAndReference() {
        Bank bank = new Bank();
        String id = bank.createSavingsAccount();
        bank.deposit(id, 1, 5000);
        assertEquals(-5000, bank.getBalance());

        Account account = new SavingsAccount("S-2000");
        bank.setAccount(account);
        assertSame(account, bank.getAccount());
    }

    private void printAndSortAccounts() {
        Bank bank = new Bank();
        String first = bank.createSavingsAccount();
        String second = bank.createSavingsAccount();
        bank.deposit(first, 0, 1000);
        bank.deposit(second, 30, 2000);

        assertTrue(output(() -> bank.print(first)).contains(first));
        assertTrue(output(() -> bank.print(second, 1970, 2)).contains("Monat: 2.1970"));

        String top = output(bank::printTop5);
        String bottom = output(bank::printBottom5);
        assertTrue(top.indexOf(second) < top.indexOf(first));
        assertTrue(bottom.indexOf(first) < bottom.indexOf(second));
        assertDoesNotThrow(() -> bank.print("unknown"));
        assertDoesNotThrow(() -> bank.print("unknown", 1970, 1));
    }

    private void comparatorsHandleEqualBalances() {
        Account account = new SavingsAccount("S-1");
        assertEquals(0, new AccountBalanceComparator().compare(account, account));
        assertEquals(0, new AccountInverseBalanceComparator().compare(account, account));
    }

    private String output(Runnable action) {
        PrintStream old = System.out;
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        try {
            System.setOut(new PrintStream(result));
            action.run();
            return result.toString();
        } finally {
            System.setOut(old);
        }
    }
}
