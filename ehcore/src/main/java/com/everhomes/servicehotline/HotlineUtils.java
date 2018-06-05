package com.everhomes.servicehotline;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

import com.everhomes.community.Community;
import com.everhomes.rest.news.NewsServiceErrorCode;
import com.everhomes.rest.servicehotline.HotlineErrorCode;
import com.everhomes.util.RuntimeErrorException;

public class HotlineUtils {
    
    // 严格正则
    private static final String HOT_LINE_PHONE_REGEX =
            "^(?:13[0-9]|14[579]|15[0-3,5-9]|17[0135678]|18[0-9]|19[0-9])\\d{8}$";

    // 测试手机号正则
    private static final int PHONE_NUMBER_LENGTH =  11;
	
	
	/**
	 * 判断该字符串是否手机号
	 * @param keyword
	 * @return
	 */
	static boolean isPhoneNumber(String inputStr) {
		if (null == inputStr || inputStr.length() != PHONE_NUMBER_LENGTH) {
			return false;
		}
		
		Pattern pattern = Pattern.compile(HOT_LINE_PHONE_REGEX);
		Matcher matcher = pattern.matcher(inputStr);
		return matcher.matches();
	}
	
	/**
	 * 判断该字符串是否数字
	 * @param keyword
	 * @return
	 */
	static boolean isNumber(String inputStr) {
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
	
	
	/**
     * emoji表情替换
     * 所谓Emoji就是一种在Unicode位于\u1F601-\u1F64F区段的字符。
     * @param source 原字符串
     * @param slipStr emoji表情替换成的字符串                
     * @return 过滤后的字符串
     */
    public static String filterEmoji(String source,String slipStr) {
        if(StringUtils.isNotBlank(source)){
            return source.replaceAll("[\\ud800\\udc00-\\udbff\\udfff\\ud800-\\udfff]", slipStr);
        }else{
            return source;
        }
    }
}
