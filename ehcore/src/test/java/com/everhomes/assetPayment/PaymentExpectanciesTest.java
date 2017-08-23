//@formatter:off
package com.everhomes.assetPayment;

import com.everhomes.asset.AssetService;
import com.everhomes.asset.AssetServiceImpl;
import com.everhomes.rest.asset.*;
import com.everhomes.util.CalculatorUtil;
import com.google.gson.Gson;
import org.junit.Test;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Wentian Wang on 2017/8/22.
 */

public class PaymentExpectanciesTest {

    @Test
    public void fun(){
//        Script
        List<VariableIdAndValue> list = new ArrayList<>();
        VariableIdAndValue v1 = new VariableIdAndValue();
        v1.setVariableId("1");
        v1.setVariableValue("80");
        list.add(v1);
        VariableIdAndValue v2 = new VariableIdAndValue();
        v2.setVariableId("2");
        v2.setVariableValue("2");
        list.add(v2);
        VariableIdAndValue v3 = new VariableIdAndValue();
        v3.setVariableId("3");
        v3.setVariableValue("60");
        list.add(v3);
        String formula = "(1*2-3)";
        BigDecimal bigDecimal = calculateFee1(list, formula, 1);
        System.out.println(bigDecimal);

    }

    private BigDecimal calculateFee1(List<VariableIdAndValue> variableIdAndValueList, String formula, float duration) {
        Gson gson = new Gson();
        HashMap<String,BigDecimal> map = new HashMap();
        for(int i = 0; i < variableIdAndValueList.size(); i++){
            VariableIdAndValue variableIdAndValue = variableIdAndValueList.get(i);
            map.put((String)variableIdAndValue.getVariableId(),new BigDecimal(Float.parseFloat((String)variableIdAndValue.getVariableValue())));
        }
        char[] chars = formula.toCharArray();
        List<Character> ch = new ArrayList<>();
        for(int i = 0; i < chars.length; i++){
            ch.add(chars[i]);
        }
        List<Character> operators = new ArrayList<>();
        operators.add('*');
        operators.add('/');
        operators.add('+');
        operators.add('-');
        int begin = 0;
        int end = 0;
        outter:
        while(true){
            if(ch.get(end)=='('){

            }
            if(end == ch.size()-1){
                //最后的置换
                List<Character> characters = ch.subList(begin, end + 1);
                String variableId = "";
                for(int i = 0; i< characters.size();i++){
                    variableId += characters.get(i);
                }
                if(!map.containsKey(variableId)){
                    throw new RuntimeException("公式解析失败");
                }
                BigDecimal varibleValue = map.get(variableId);
                int value = varibleValue.intValue();
                String replaced = String.valueOf(value);
                char[] replacedChars = replaced.toCharArray();
                List<Character> target = new ArrayList<>();
                int len = replaced.length();
                for(int i = 0; i< replacedChars.length; i++){
                    target.add(replacedChars[i]);
                }
                int originaLen = end -begin;
                if(begin == end){
                    originaLen = 1;
                }
                ch.addAll(begin,target);
                int deleteIndex = begin+target.size();;
                for(int i = 0; i < originaLen; i++){
                    ch.remove(deleteIndex);
                }
                break outter;
            }
            char c = ch.get(end);
            if(operators.contains(c)){
                //替换 begin和end之间的数值，置begin为 begin+len(new)+1，end = begin--》continue来跳过这个运算符
                List<Character> characters = ch.subList(begin, end);
                String variableId = "";
                for(int i = 0; i< characters.size();i++){
                    variableId += characters.get(i);
                }

                if(!map.containsKey(variableId)){
                    throw new RuntimeException("公式解析失败");
                }
                BigDecimal varibleValue = map.get(variableId);
                int value = varibleValue.intValue();
                String replaced = String.valueOf(value);
                char[] replacedChars = replaced.toCharArray();
                List<Character> target = new ArrayList<>();
                int len = replaced.length();
                for(int i = 0; i< replacedChars.length; i++){
                    target.add(replacedChars[i]);
                }
                int originaLen = end -begin;
                ch.addAll(begin,target);
                int deleteIndex = begin+target.size();;
                for(int i = 0; i < originaLen; i++){
                    ch.remove(deleteIndex);
                }
                begin = begin+len+1;
                end = begin;
                continue;
            }
            end++;
        }
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < ch.size(); i++){
            String s = ch.get(i).toString();
            sb.append(s);
        }
        BigDecimal response = CalculatorUtil.arithmetic(sb.toString());
        response.setScale(2);
        return response;
    }

}
