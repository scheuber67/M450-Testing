package ch.tbz.m450.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class AddressTest {
    private Date registrationDate;
    private Address address;

    @BeforeEach
    void setUp() {
        registrationDate = new Date(1_700_000_000_000L);
        address = new Address(1, "Anna", "Muster", "079 111 22 33", registrationDate);
    }

    @Test
    void allArgsConstructorCreatesAddress() {
        assertEquals(1, address.getId());
        assertEquals("Anna", address.getFirstname());
        assertEquals("Muster", address.getLastname());
        assertEquals("079 111 22 33", address.getPhonenumber());
        assertEquals(registrationDate, address.getRegistrationDate());
    }

    @Test
    void noArgsConstructorAndSettersUpdateAddress() {
        Address emptyAddress = new Address();

        emptyAddress.setId(2);
        emptyAddress.setFirstname("Ben");
        emptyAddress.setLastname("Berger");
        emptyAddress.setPhonenumber("079 222 33 44");
        emptyAddress.setRegistrationDate(registrationDate);

        assertNotNull(emptyAddress);
        assertEquals(2, emptyAddress.getId());
        assertEquals("Ben", emptyAddress.getFirstname());
        assertEquals("Berger", emptyAddress.getLastname());
        assertEquals("079 222 33 44", emptyAddress.getPhonenumber());
        assertEquals(registrationDate, emptyAddress.getRegistrationDate());
    }
}
