package ch.tbz.m450.util;

import ch.tbz.m450.repository.Address;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static ch.tbz.m450.util.AddressComparator.SortBy.FIRSTNAME;
import static ch.tbz.m450.util.AddressComparator.SortBy.ID;
import static ch.tbz.m450.util.AddressComparator.SortBy.LASTNAME;
import static ch.tbz.m450.util.AddressComparator.SortBy.PHONENUMBER;
import static ch.tbz.m450.util.AddressComparator.SortBy.REGISTRATION_DATE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AddressComparatorTest {
    private Address annaMuster;
    private Address benMuster;
    private Address claudiaBerger;

    @BeforeEach
    void setUp() {
        annaMuster = new Address(3, "Anna", "Muster", "079 333 33 33", new Date(3_000L));
        benMuster = new Address(2, "Ben", "Muster", "079 222 22 22", new Date(2_000L));
        claudiaBerger = new Address(1, "Claudia", "Berger", "079 111 11 11", new Date(1_000L));
    }

    @Test
    void defaultComparatorSortsByLastnameFirstnameAndId() {
        List<Address> addresses = new ArrayList<>(List.of(annaMuster, benMuster, claudiaBerger));

        addresses.sort(new AddressComparator());

        assertEquals(List.of(claudiaBerger, annaMuster, benMuster), addresses);
    }

    @Test
    void comparatorSortsById() {
        List<Address> addresses = new ArrayList<>(List.of(annaMuster, benMuster, claudiaBerger));

        addresses.sort(new AddressComparator(ID));

        assertEquals(List.of(claudiaBerger, benMuster, annaMuster), addresses);
    }

    @Test
    void comparatorSortsByFirstname() {
        List<Address> addresses = new ArrayList<>(List.of(benMuster, claudiaBerger, annaMuster));

        addresses.sort(new AddressComparator(FIRSTNAME));

        assertEquals(List.of(annaMuster, benMuster, claudiaBerger), addresses);
    }

    @Test
    void comparatorSortsByLastname() {
        List<Address> addresses = new ArrayList<>(List.of(benMuster, annaMuster, claudiaBerger));

        addresses.sort(new AddressComparator(LASTNAME));

        assertEquals(List.of(claudiaBerger, annaMuster, benMuster), addresses);
    }

    @Test
    void comparatorSortsByPhonenumber() {
        List<Address> addresses = new ArrayList<>(List.of(annaMuster, benMuster, claudiaBerger));

        addresses.sort(new AddressComparator(PHONENUMBER));

        assertEquals(List.of(claudiaBerger, benMuster, annaMuster), addresses);
    }

    @Test
    void comparatorSortsByRegistrationDate() {
        List<Address> addresses = new ArrayList<>(List.of(annaMuster, benMuster, claudiaBerger));

        addresses.sort(new AddressComparator(REGISTRATION_DATE));

        assertEquals(List.of(claudiaBerger, benMuster, annaMuster), addresses);
    }

    @Test
    void comparatorHandlesNullAddressesAndAttributes() {
        Address incomplete = new Address(4, null, null, null, null);
        List<Address> addresses = new ArrayList<>(List.of(incomplete, claudiaBerger));
        addresses.add(null);

        addresses.sort(new AddressComparator());

        assertEquals(claudiaBerger, addresses.get(0));
        assertEquals(incomplete, addresses.get(1));
        assertTrue(addresses.get(2) == null);
    }
}
