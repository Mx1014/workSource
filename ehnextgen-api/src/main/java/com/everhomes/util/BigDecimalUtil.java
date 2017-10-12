//@formatter:off
package com.everhomes.util;

import java.math.BigDecimal;

/**
 * Created by Wentian Wang on 2017/9/28.
 */

public class BigDecimalUtil {
    public static BigDecimal convert(Double value, int scale, int roundingMode) {
        return value == null ?BigDecimal.ZERO : BigDecimal.valueOf(value).setScale(scale, roundingMode);
    }

    public static BigDecimal convert(Integer value) {
        return value == null ?BigDecimal.ZERO : BigDecimal.valueOf(value);
    }

    public static BigDecimal convert(Double value) {
        return value == null ?BigDecimal.ZERO : BigDecimal.valueOf(value);
    }

    public static BigDecimal convert(BigDecimal value) {
        return value == null ?BigDecimal.ZERO : value;
    }

    //ROUND_HALF_UP
    public static BigDecimal convertByDefault(Double value) {
        return value == null ?BigDecimal.ZERO : BigDecimal.valueOf(value).setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    public static BigDecimal convertByDefault(BigDecimal value) {
        return value.setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    public static BigDecimal convertByDefault(Integer value) {
        return value == null ?BigDecimal.ZERO : BigDecimal.valueOf(value).setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    //ROUND_HALF_DOWN
    public static BigDecimal convertForRoundHalfDown(Double value) {
        return value == null ?BigDecimal.ZERO : BigDecimal.valueOf(value).setScale(2, BigDecimal.ROUND_HALF_DOWN);
    }

    public static BigDecimal convertForRoundHalfDown(BigDecimal value) {
        return value.setScale(2, BigDecimal.ROUND_HALF_DOWN);
    }

    public static BigDecimal convertForRoundHalfDown(Integer value) {
        return value == null ?BigDecimal.ZERO : BigDecimal.valueOf(value).setScale(2, BigDecimal.ROUND_HALF_DOWN);
    }
}
