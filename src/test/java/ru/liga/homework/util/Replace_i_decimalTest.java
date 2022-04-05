package ru.liga.homework.util;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class Replace_i_decimalTest {
    private static final char I_DECIMAL = 0x456;

    public final ConvertTextToPreRevolution convertTextToPreRevolution = new ConvertTextToPreRevolution();

    @Test
    void not_replace_i_decimal_1() {
        String str = convertTextToPreRevolution.replace_i_decimal("пийн");
        assertTrue(str.contains(String.valueOf(I_DECIMAL)));
    }

    @Test
    void replace_i_decimal_2() {
        String str = convertTextToPreRevolution.replace_i_decimal("пппивввв [и] о");
        assertFalse(str.contains(String.valueOf(I_DECIMAL)));
    }

    @Test
    void replace_all_i_decimal_3() {
        String str = convertTextToPreRevolution.replace_i_decimal("Для понимания объяснийм «на пальцах». Если соглиасная и й буиква иок относи1тся к парныийм. ииНапример");

        long count = Arrays
                .stream(str.split("[\\s,.:!?]+"))
                .filter(s -> s.contains(String.valueOf(I_DECIMAL)))
                .count();
        assertEquals(6, count);
    }
}