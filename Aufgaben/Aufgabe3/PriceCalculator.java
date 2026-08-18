public class PriceCalculator {
    static double calculatePrice(
            double basePrice,
            double specialPrice,
            double extraPrice,
            int extras,
            double discount) {

        double addonDiscount;

        if (extras >= 5) {
            addonDiscount = 15.0;
        } else if (extras >= 3) {
            addonDiscount = 10.0;
        } else {
            addonDiscount = 0.0;
        }

        return basePrice / 100.0 * (100.0 - discount)
                + specialPrice
                + extraPrice / 100.0 * (100.0 - addonDiscount);
    }
}
