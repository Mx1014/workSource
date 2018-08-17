package com.everhomes.util;

/**
 * @author huangmingbo
 *
 */
public class NumberUtils {
	
	/**
	 * 判断该字符串是否全为数字
	 * @param keyword
	 * @return
	 */
	public static boolean isNumber(String inputStr) {
		
		if (null == inputStr) {
			return false;
		}
		
		for (int i = 0; i < inputStr.length(); i++) {
            if (!Character.isDigit(inputStr.charAt(i))) {
                return false;
            }
        }
		
        return true;
	}
}
