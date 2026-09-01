package ch.schule.bank.junit5;

import ch.schule.Account;
import ch.schule.Booking;
import ch.schule.SalaryAccount;
import ch.schule.SavingsAccount;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

class AccountTests {
    @Test
    void testAccount() {
        initialization();
        deposit();
        withdraw();
        transactionDate();
        bookingReference();
        printStatements();
    }

    private void initialization() {
        Account account = new SavingsAccount("S-1000");
        assertEquals("S-1000", account.getId());
        assertEquals(0, account.getBalance());
    }

    private void deposit() {
        Account account = new SavingsAccount("S-1000");
        assertTrue(account.deposit(10, 5000));
        assertFalse(account.deposit(11, -1));
        assertEquals(5000, account.getBalance());
    }

    private void withdraw() {
        Account account = new SalaryAccount("P-1000", -10000);
        account.deposit(10, 5000);
        assertTrue(account.withdraw(11, 2000));
        assertFalse(account.withdraw(12, -1));
        assertEquals(3000, account.getBalance());
    }

    private void transactionDate() {
        Account account = new SavingsAccount("S-1000");
        assertTrue(account.canTransact(10));
        account.deposit(10, 5000);
        assertFalse(account.deposit(9, 1000));
        assertFalse(account.withdraw(9, 1000));
    }

    private void bookingReference() {
        Account account = new SavingsAccount("S-1000");
        Booking booking = new Booking(10, 5000);
        account.setBooking(booking);
        assertSame(booking, account.getBooking());
    }

    private void printStatements() {
        Account account = new SavingsAccount("S-1000");
        account.deposit(0, 100000);
        account.deposit(30, 200000);

        String all = output(account::print);
        String month = output(() -> account.print(1970, 2));

        assertTrue(all.contains("S-1000"));
        assertTrue(month.contains("01.02.1970"));
        assertFalse(month.contains("01.01.1970"));
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
