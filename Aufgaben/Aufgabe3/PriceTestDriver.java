public class PriceTestDriver {
    public static void main(String[] args) {
        if (testCalculatePrice()) {
            System.out.println("Alle Tests bestanden");
        } else {
            System.out.println("Ein Test ist fehlgeschlagen");
        }
    }

    static boolean testCalculatePrice() {
        double price;
        boolean testOk = true;

        price = PriceCalculator.calculatePrice(30000, 1000, 5000, 2, 5);
        if (price != 34500) {
            testOk = false;
        }

        price = PriceCalculator.calculatePrice(30000, 1000, 5000, 3, 5);
        if (price != 34000) {
            testOk = false;
        }

        price = PriceCalculator.calculatePrice(30000, 1000, 5000, 5, 5);
        if (price != 33750) {
            testOk = false;
        }

        return testOk;
    }
}
