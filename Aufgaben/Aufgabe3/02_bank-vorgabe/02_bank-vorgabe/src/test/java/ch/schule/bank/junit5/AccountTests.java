package ch.schule.bank.junit5;

import ch.schule.Account;
import ch.schule.Booking;
import ch.schule.SavingsAccount;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AccountTests {
    @Test
    void testAccount() {
        Account account = new SavingsAccount("S-1000");

        assertEquals("S-1000", account.getId());
        assertEquals(0, account.getBalance());

        assertTrue(account.deposit(1, 1000));
        assertEquals(1000, account.getBalance());

        assertTrue(account.withdraw(2, 500));
        assertEquals(500, account.getBalance());

        assertFalse(account.deposit(1, 100));
        assertFalse(account.deposit(3, -100));
        assertFalse(account.withdraw(3, -100));

        Booking booking = new Booking(3, 100);
        account.setBooking(booking);
        assertSame(booking, account.getBooking());

        account.print();
        account.print(1970, 1);
    }
}
