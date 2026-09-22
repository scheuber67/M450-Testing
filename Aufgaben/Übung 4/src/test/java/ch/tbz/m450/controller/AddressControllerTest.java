package ch.tbz.m450.controller;

import ch.tbz.m450.repository.Address;
import ch.tbz.m450.service.AddressService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AddressControllerTest {
    @Mock
    private AddressService addressService;

    private AddressController addressController;
    private Address address;

    @BeforeEach
    void setUp() {
        addressController = new AddressController(addressService);
        address = new Address(1, "Anna", "Muster", "079 111 22 33", new Date(1_000L));
    }

    @Test
    void createAddressReturnsCreatedAddress() {
        when(addressService.save(address)).thenReturn(address);

        ResponseEntity<Address> response = addressController.createAddress(address);

        assertEquals(201, response.getStatusCode().value());
        assertEquals(address, response.getBody());
        verify(addressService).save(address);
    }

    @Test
    void getAddressesReturnsAllAddresses() {
        when(addressService.getAll()).thenReturn(List.of(address));

        ResponseEntity<List<Address>> response = addressController.getAddresses();

        assertEquals(200, response.getStatusCode().value());
        assertEquals(List.of(address), response.getBody());
        verify(addressService).getAll();
    }

    @Test
    void getAddressReturnsAddressWhenFound() {
        when(addressService.getAddress(1)).thenReturn(Optional.of(address));

        ResponseEntity<Address> response = addressController.getAddress(1);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(address, response.getBody());
        verify(addressService).getAddress(1);
    }

    @Test
    void getAddressReturnsNotFoundWhenMissing() {
        when(addressService.getAddress(99)).thenReturn(Optional.empty());

        ResponseEntity<Address> response = addressController.getAddress(99);

        assertEquals(404, response.getStatusCode().value());
        assertNull(response.getBody());
        verify(addressService).getAddress(99);
    }
}
