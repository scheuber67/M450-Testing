package ch.schule.bank.junit5;

import ch.schule.Bank;
import ch.schule.Account;
import ch.schule.SavingsAccount;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BankTests {
    @Test
    void testBank() {
        Bank bank = new Bank();
        String id = bank.createSavingsAccount();

        assertEquals("S-1000", id);
        assertTrue(bank.deposit(id, 1, 1000));
        assertTrue(bank.withdraw(id, 2, 400));
        assertEquals(600, bank.getBalance(id));

        String youthId = bank.createPromoYouthSavingsAccount();
        String salaryId = bank.createSalaryAccount(-1000);
        assertNotNull(youthId);
        assertNotNull(salaryId);
        assertNull(bank.createSalaryAccount(1000));

        assertFalse(bank.deposit("falsch", 1, 100));
        assertFalse(bank.withdraw("falsch", 1, 100));
        assertEquals(0, bank.getBalance("falsch"));
        assertEquals(-600, bank.getBalance());

        Account account = new SavingsAccount("S-2000");
        bank.setAccount(account);
        assertSame(account, bank.getAccount());

        bank.print(id);
        bank.print(id, 1970, 1);
        bank.print("falsch");
        bank.print("falsch", 1970, 1);
        bank.printTop5();
        bank.printBottom5();
    }
}
