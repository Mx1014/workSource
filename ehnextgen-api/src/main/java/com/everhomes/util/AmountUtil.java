//@formatter:off
package com.everhomes.util;

import java.math.BigDecimal;

/**
 * Created by Wentian Wang on 2017/9/28.
 */

public class AmountUtil {
    public static Long unitToCent(BigDecimal amount) {
        if(amount == null) {
            return null;
        }
        return amount.multiply(new BigDecimal(100)).longValue();
    }

    public static Long unitToCent(Double amount) {
        if(amount == null) {
            return null;
        }
        return BigDecimalUtil.convertByDefault(amount).multiply(new BigDecimal(100)).longValue();
    }

    public static BigDecimal centToUnit(Long amount) {
        if(amount == null) {
            return null;
        }
        return new BigDecimal(amount).divide(new BigDecimal(100));
    }

}
