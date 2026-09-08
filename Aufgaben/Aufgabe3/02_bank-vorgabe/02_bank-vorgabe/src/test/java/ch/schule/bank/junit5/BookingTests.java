package ch.schule.bank.junit5;

import ch.schule.Booking;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BookingTests {
    @Test
    void storesAmount() {
        Booking booking = new Booking(10, 5000);

        assertEquals(5000, booking.getAmount());
    }
}
