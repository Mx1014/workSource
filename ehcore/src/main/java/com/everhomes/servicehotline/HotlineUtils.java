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

}
