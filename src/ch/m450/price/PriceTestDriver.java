package ch.m450.price;

/** Einfacher Testtreiber ohne Unit-Test-Framework. */
public final class PriceTestDriver {

    private static final double TOLERANCE = 0.000_001;
    private static int testsRun;
    private static int testsPassed;

    private PriceTestDriver() {
    }

    public static void main(String[] args) {
        boolean testOk = testCalculatePrice();

        System.out.printf("%nResultat: %d von %d Tests bestanden.%n", testsPassed, testsRun);
        if (!testOk) {
            System.exit(1);
        }
    }

    public static boolean testCalculatePrice() {
        boolean testOk = true;

        testOk &= check("weniger als 3 Extras", 34_500.0,
                PriceCalculator.calculatePrice(30_000.0, 1_000.0, 5_000.0, 2, 5.0));

        testOk &= check("genau 3 Extras: 10 %", 34_000.0,
                PriceCalculator.calculatePrice(30_000.0, 1_000.0, 5_000.0, 3, 5.0));

        testOk &= check("genau 5 Extras: 15 %", 33_750.0,
                PriceCalculator.calculatePrice(30_000.0, 1_000.0, 5_000.0, 5, 5.0));

        testOk &= check("Rabatte bleiben getrennt", 17_000.0,
                PriceCalculator.calculatePrice(10_000.0, 0.0, 10_000.0, 3, 20.0));

        testOk &= check("Nullwerte", 0.0,
                PriceCalculator.calculatePrice(0.0, 0.0, 0.0, 0, 0.0));

        return testOk;
    }

    private static boolean check(String description, double expected, double actual) {
        testsRun++;
        boolean passed = Math.abs(expected - actual) <= TOLERANCE;
        if (passed) {
            testsPassed++;
        }

        System.out.printf("[%s] %-30s erwartet: %.2f, erhalten: %.2f%n",
                passed ? "OK" : "FEHLER", description, expected, actual);
        return passed;
    }
}
