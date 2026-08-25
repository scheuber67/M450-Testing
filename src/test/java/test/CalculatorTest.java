package test;

import main.Calculator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CalculatorTest {
    private Calculator calculator;

    @BeforeEach
    void setUp() {
        calculator = new Calculator();
    }

    @Test
    void addTwoPositiveNumbers() {
        assertEquals(8, calculator.add(5, 3));
    }

    @Test
    void addNegativeNumber() {
        assertEquals(2, calculator.add(5, -3));
    }

    @Test
    void subtractTwoNumbers() {
        assertEquals(2, calculator.subtract(5, 3));
    }

    @Test
    void multiplyTwoNumbers() {
        assertEquals(15, calculator.multiply(5, 3));
    }

    @Test
    void multiplyByZero() {
        assertEquals(0, calculator.multiply(5, 0));
    }

    @Test
    void divideTwoNumbers() {
        assertEquals(2.5, calculator.divide(5, 2));
    }
}
