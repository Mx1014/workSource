package com.everhomes.contract.template;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Steve
 * @version 1.0
 * @description 
 * @date 2018/12/3
 */
public class GetKeywordsUtils {

	/**
	 * 该方法可以提取出一段文本中，以特定开始标记、结束标记包裹的关键字
	 * 返回所有符合条件的关键字列表，关键字带着开始、结束标记
	 * 注意：开始标记和结束标记不能相同
	 */
    public static List<String> getKeywordsWithPattern(String text,String startParten,String endParten){
        List<String> results = new ArrayList<>();

        char[] startPartenArray = startParten.toCharArray();
        char[] endPartenArray = endParten.toCharArray();
        char[] textArray = text.toCharArray();

        boolean beginFlag = false;
        StringBuffer tempSB = new StringBuffer();
        
        for (int i = 0; i < textArray.length; i++) {
            //判断是否开始匹配
            if (textArray[i] == startPartenArray[0]){
                if (checkPattern(text, startParten, i)){
                    beginFlag = true;
                }
            }
            //判断是否结束匹配
            if (textArray[i] == endPartenArray[0]){
                if (checkPattern(text, endParten, i)){
                    beginFlag = false;
                    tempSB.append(endParten);
                    results.add(tempSB.toString());
                    tempSB.setLength(0);
                    continue;
                }
            }
            if (beginFlag){
                tempSB.append(textArray[i]);
            }
        }
        return results;
    }

    /**
	 * 该方法可以提取出一段文本中，以特定开始标记、结束标记包裹的关键字
	 * 返回所有符合条件的关键字列表，关键字不带开始、结束标记
	 * 注意：开始标记和结束标记不能相同
	 */
    public static List<String> getKeywordsWithoutPattern(String text,String startParten,String endParten){
        List<String> results = new ArrayList<>();

        char[] startPartenArray = startParten.toCharArray();
        char[] endPartenArray = endParten.toCharArray();
        char[] textArray = text.toCharArray();

        boolean beginFlag = false;
        StringBuffer tempSB = new StringBuffer();

        for (int i = 0; i < textArray.length; i++) {
            //判断是否开始匹配
            if (textArray[i] == startPartenArray[0]){
                if (checkPattern(text, startParten, i)){
                    beginFlag = true;
                }
            }
            //判断是否结束匹配
            if (textArray[i] == endPartenArray[0]){
                if (checkPattern(text, endParten, i)){
                    beginFlag = false;
                    results.add(tempSB.substring(startParten.length()));
                    tempSB.setLength(0);
                    continue;
                }
            }
            if (beginFlag){
                tempSB.append(textArray[i]);
            }
        }
        return results;
    }

    private static boolean checkPattern(String text,String pattern,int startIndex){
    	char[] patternArray = pattern.toCharArray();
    	char[] textArray = text.toCharArray();
    	
    	int count = 0;
    	for (int j = 0; j < patternArray.length; j++) {
            if (textArray[startIndex+j] == patternArray[j]){
            	count++;
            }else {
            	return false;
            }
        }
    	if (count == pattern.length())
    		return true;
    	return false;
    }
}
