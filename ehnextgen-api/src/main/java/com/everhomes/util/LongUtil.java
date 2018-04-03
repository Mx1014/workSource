//@formatter:off
package com.everhomes.util;

import org.apache.commons.lang.StringUtils;

/**
 * Created by Wentian Wang on 2017/9/28.
 */

public class LongUtil {

    public static long convert(Long value) {
        return value == null ? 0:value.longValue();
    }

    public static Long convert(String value) {
        if(StringUtils.isBlank(value)) {
            return null;
        }
        return Long.valueOf(value);
    }

    public static String convertToString(Long value) {
        return value == null ? null:value.toString();
    }

}
