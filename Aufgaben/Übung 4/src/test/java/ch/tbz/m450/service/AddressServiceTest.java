package ch.tbz.m450.service;

import ch.tbz.m450.repository.Address;
import ch.tbz.m450.repository.AddressRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AddressServiceTest {
    @Mock
    private AddressRepository addressRepository;

    @InjectMocks
    private AddressService addressService;

    private Address annaMuster;
    private Address claudiaBerger;

    @BeforeEach
    void setUp() {
        annaMuster = new Address(2, "Anna", "Muster", "079 222 22 22", new Date(2_000L));
        claudiaBerger = new Address(1, "Claudia", "Berger", "079 111 11 11", new Date(1_000L));
    }

    @Test
    void saveDelegatesToRepository() {
        when(addressRepository.save(annaMuster)).thenReturn(annaMuster);

        Address savedAddress = addressService.save(annaMuster);

        assertEquals(annaMuster, savedAddress);
        verify(addressRepository).save(annaMuster);
    }

    @Test
    void getAllReturnsAddressesSortedByDefaultComparator() {
        when(addressRepository.findAll()).thenReturn(List.of(annaMuster, claudiaBerger));

        List<Address> addresses = addressService.getAll();

        assertEquals(List.of(claudiaBerger, annaMuster), addresses);
        verify(addressRepository).findAll();
    }

    @Test
    void getAddressReturnsRepositoryResult() {
        when(addressRepository.findById(1)).thenReturn(Optional.of(claudiaBerger));

        Optional<Address> address = addressService.getAddress(1);

        assertTrue(address.isPresent());
        assertEquals(claudiaBerger, address.get());
        verify(addressRepository).findById(1);
    }

    @Test
    void getAddressReturnsEmptyWhenRepositoryDoesNotFindAddress() {
        when(addressRepository.findById(99)).thenReturn(Optional.empty());

        Optional<Address> address = addressService.getAddress(99);

        assertTrue(address.isEmpty());
        verify(addressRepository).findById(99);
    }
}
