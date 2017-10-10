//@formatter:off
package com.everhomes.util;

import org.apache.commons.lang.StringUtils;

/**
 * Created by Wentian Wang on 2017/9/28.
 */

public class IntegerUtil {

    public static int convert(Integer value) {
        return value == null ? 0 :value.intValue();
    }

    public static int convert(Integer value, Integer defaultValue) {
        return value == null ? defaultValue :value.intValue();
    }

    public static Integer convert(String value) {
        if(StringUtils.isBlank(value)) {
            return null;
        }
        return Integer.valueOf(value);
    }

}
