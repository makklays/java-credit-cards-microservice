package com.techmatrix18.sandbox;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the Calculator class.
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 24.06.2026
 */
class Calculator {
    public int add(int a, int b) {
        return a + b;
    }
}

// @ExtendWith(MockitoExtension.class)
class CalculatorTest {

    private Calculator calculator;

    @BeforeEach
    void setUp() {
        calculator = new Calculator();
    }

    @Test
    @DisplayName("Проверка успешного сложения двух чисел")
    void testAddition() {
        int result = calculator.add(2, 3);
        assertEquals(5, result, "2 + 3 должно быть равно 5"); // Проверка утверждения
    }
}

