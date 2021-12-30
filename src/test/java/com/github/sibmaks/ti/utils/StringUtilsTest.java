package com.github.sibmaks.ti.utils;

import com.github.sibmaks.ti.utils.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author drobyshev-ma
 * Created at 19-08-2021
 */
class StringUtilsTest {

    @Test
    public void lowerCase1stChar() {
        Assertions.assertEquals("sOME_STRING", StringUtils.toLowerCase1st("SOME_STRING"));
    }

    @Test
    public void lowerCase1stCharNull() {
        assertNull(StringUtils.toLowerCase1st(null));
    }

    @Test
    public void lowerCase1stCharEmpty() {
        assertEquals("", StringUtils.toLowerCase1st(""));
    }
}