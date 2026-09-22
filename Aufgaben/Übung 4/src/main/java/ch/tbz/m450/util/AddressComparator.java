package ch.tbz.m450.util;

import ch.tbz.m450.repository.Address;

import java.util.Comparator;
import java.util.Date;

public class AddressComparator implements Comparator<Address> {
    public enum SortBy {
        ID,
        FIRSTNAME,
        LASTNAME,
        PHONENUMBER,
        REGISTRATION_DATE
    }

    private static final Comparator<String> STRING_COMPARATOR =
            Comparator.nullsLast(String.CASE_INSENSITIVE_ORDER);
    private static final Comparator<Date> DATE_COMPARATOR =
            Comparator.nullsLast(Date::compareTo);

    private final SortBy sortBy;

    public AddressComparator() {
        this(null);
    }

    public AddressComparator(SortBy sortBy) {
        this.sortBy = sortBy;
    }

    @Override
    public int compare(Address a1, Address a2) {
        return Comparator.nullsLast(resolveComparator()).compare(a1, a2);
    }

    private Comparator<Address> resolveComparator() {
        if (sortBy == null) {
            return defaultComparator();
        }

        return switch (sortBy) {
            case ID -> Comparator.comparingInt(Address::getId);
            case FIRSTNAME -> Comparator.comparing(Address::getFirstname, STRING_COMPARATOR)
                    .thenComparing(defaultComparator());
            case LASTNAME -> Comparator.comparing(Address::getLastname, STRING_COMPARATOR)
                    .thenComparing(defaultComparator());
            case PHONENUMBER -> Comparator.comparing(Address::getPhonenumber, STRING_COMPARATOR)
                    .thenComparing(defaultComparator());
            case REGISTRATION_DATE -> Comparator.comparing(Address::getRegistrationDate, DATE_COMPARATOR)
                    .thenComparing(defaultComparator());
        };
    }

    private Comparator<Address> defaultComparator() {
        return Comparator.comparing(Address::getLastname, STRING_COMPARATOR)
                .thenComparing(Address::getFirstname, STRING_COMPARATOR)
                .thenComparingInt(Address::getId);
    }
}
