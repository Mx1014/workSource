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

    /**
     * 给定一个单精度的数字，获得整数部分(index : 0)和单精度的小数部分(index : 1)
     */
    public static Object[] getIntegerAndFloatPartFromFloat(Float separationTime) {
        Object[] objs = new Object[2];
        String s = separationTime.toString();
        objs[0] = Integer.parseInt(s.substring(0, s.indexOf(".")));
        objs[1] = Float.parseFloat("0" + s.substring(s.indexOf(".")));
        return objs;
    }

    /**
     * 判断一个string是否含有数字
     */
    public final boolean hasDigit(String iden) {
        char[] chars = iden.toCharArray();
        for(char c : chars){
            if(Character.isDigit(c)){
                return true;
            }
        }
        return false;
    }
}
