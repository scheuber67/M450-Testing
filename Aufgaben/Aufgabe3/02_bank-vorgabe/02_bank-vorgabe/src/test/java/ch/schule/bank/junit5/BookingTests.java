package ch.schule.bank.junit5;

import ch.schule.Booking;
import ch.schule.BankUtils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BookingTests {
    @Test
    void testBooking() {
        Booking booking = new Booking(10, 5000);

        assertEquals(10, booking.getDate());
        assertEquals(5000, booking.getAmount());

        booking.print(1000);
        assertEquals("01.01.1970", BankUtils.formatBankDate(0));
        assertEquals("      1.00", BankUtils.formatAmount(100000));
    }
}
