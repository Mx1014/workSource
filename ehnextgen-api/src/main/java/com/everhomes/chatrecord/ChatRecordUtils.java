package com.everhomes.chatrecord;

import org.apache.commons.lang.StringUtils;

public class ChatRecordUtils {
	
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
