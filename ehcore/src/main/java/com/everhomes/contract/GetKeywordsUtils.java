package com.everhomes.contract;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Steve
 * @version 1.0
 * @description com.test.demo06.getKeyWords
 * @date 2018/12/3
 */
public class GetKeywordsUtils {

    public List<String> getKeywordsWithPattern(String text,String startParten,String endParten){
        List<String> results = new ArrayList<>();

        char[] startPartenArray = startParten.toCharArray();
        char[] endPartenArray = endParten.toCharArray();
        char[] textArray = text.toCharArray();

        boolean beginFlag = false;
        StringBuffer tempSB = new StringBuffer();

        for (int i = 0; i < textArray.length; i++) {
            //判断是否开始匹配
            int countStart = 0;
            if (textArray[i] == startPartenArray[0]){
                for (int j = 0; j < startPartenArray.length; j++) {
                    if (textArray[i+j] == startPartenArray[j]){
                        countStart++;
                    }else {
                        break;
                    }
                }
                if (countStart == startParten.length()){
                    beginFlag = true;
                }
            }
            //判断是否结束匹配
            int countEnd = 0;
            if (textArray[i] == endPartenArray[0]){
                for (int j = 0; j < endPartenArray.length; j++) {
                    if (textArray[i+j] == endPartenArray[j]){
                        countEnd++;
                    }else {
                        break;
                    }
                }
                if (countEnd == endParten.length()){
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

    public List<String> getKeywordsWithoutPattern(String text,String startParten,String endParten){
        List<String> results = new ArrayList<>();

        char[] startPartenArray = startParten.toCharArray();
        char[] endPartenArray = endParten.toCharArray();
        char[] textArray = text.toCharArray();

        boolean beginFlag = false;
        StringBuffer tempSB = new StringBuffer();

        for (int i = 0; i < textArray.length; i++) {
            //判断是否开始匹配
            int countStart = 0;
            if (textArray[i] == startPartenArray[0]){
                for (int j = 0; j < startPartenArray.length; j++) {
                    if (textArray[i+j] == startPartenArray[j]){
                        countStart++;
                    }else {
                        break;
                    }
                }
                if (countStart == startParten.length()){
                    beginFlag = true;
                }
            }
            //判断是否结束匹配
            int countEnd = 0;
            if (textArray[i] == endPartenArray[0]){
                for (int j = 0; j < endPartenArray.length; j++) {
                    if (textArray[i+j] == endPartenArray[j]){
                        countEnd++;
                    }else {
                        break;
                    }
                }
                if (countEnd == endParten.length()){
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



}
