/**
 * 
 */
package com.everhomes.news;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * news工具类
 * @author 黄明波
 *
 */
public class NewsUtils {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(NewsUtils.class);

	/**   
	* @Function: NewsUtils.java
	* @Description: 从html中提取纯文本
	* 如果提取出错则返回空字符串
	*
	* @version: v1.0.0
	* @author:	 黄明波
	* @date: 2018年4月24日 下午12:55:35 
	*
	*/
	public static String Html2Text(String inputString) {

		if (StringUtils.isBlank(inputString)) {
			return "";
		}

		String htmlStr = inputString; // 含html标签的字符串
		Pattern scriptPattern;
		Matcher scriptMatcher;
		Pattern stylePattern;
		Matcher styleMatcher;
		Pattern htmlPattern;
		Matcher htmlMatcher;
		
		try {
			// 定义script的正则表达式{或<script[^>]*?>[\\s\\S]*?<\\/script>
			String scriptRegEx = "<[\\s]*?script[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?script[\\s]*?>"; 
			
			// 定义style的正则表达式{或<style[^>]*?>[\\s\\S]*?<\\/style>
			String styleRegEx = "<[\\s]*?style[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?style[\\s]*?>"; 
			
			// 定义HTML标签的正则表达式
			String htmlRegEx = "<[^>]+>"; 
			
			// 过滤script标签
			scriptPattern = Pattern.compile(scriptRegEx, Pattern.CASE_INSENSITIVE);
			scriptMatcher = scriptPattern.matcher(htmlStr);
			htmlStr = scriptMatcher.replaceAll(""); 
			
			// 过滤style标签
			stylePattern = Pattern.compile(styleRegEx, Pattern.CASE_INSENSITIVE);
			styleMatcher = stylePattern.matcher(htmlStr);
			htmlStr = styleMatcher.replaceAll(""); 
			
			// 过滤html标签
			htmlPattern = Pattern.compile(htmlRegEx, Pattern.CASE_INSENSITIVE);
			htmlMatcher = htmlPattern.matcher(htmlStr);
			htmlStr = htmlMatcher.replaceAll(""); 
			
		} catch (Exception e) {
			LOGGER.info("Invalid html text, inputString=" + inputString);
			return "";
		}
		
		// 剔除空格行
		htmlStr = htmlStr.replaceAll("[ ]+", " ");
		htmlStr = htmlStr.replaceAll("(?m)^\\s*$(\\n|\\r\\n)", "");
		return htmlStr;
	}

}
