package ch.schule.bank.junit5;

import ch.schule.BankUtils;
import ch.schule.Booking;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

class BookingTests {
    @Test
    void initialization() {
        Booking booking = new Booking(0, 100000);
        assertEquals(0, booking.getDate());
        assertEquals(100000, booking.getAmount());
    }

    @Test
    void formattingAndPrint() {
        Booking booking = new Booking(0, 100000);
        PrintStream old = System.out;
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        try {
            System.setOut(new PrintStream(result));
            booking.print(200000);
        } finally {
            System.setOut(old);
        }

        assertTrue(result.toString().contains("01.01.1970"));
        assertEquals("01.02.1971", BankUtils.formatBankDate(390));
        assertEquals("      1.00", BankUtils.formatAmount(100000));
    }
}
