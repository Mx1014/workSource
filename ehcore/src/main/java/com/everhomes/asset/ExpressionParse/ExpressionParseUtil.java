package com.everhomes.asset.ExpressionParse;

import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

public class ExpressionParseUtil {

    public static  Map<String,Integer> parse(String Expression){
        Logger LOGGE = Logger.getLogger(ExpressionParseUtil.class);
        Map<String,Integer> result = new HashMap<>();
        if (Expression!=null&&Expression.length()>0){
            String params[] = Expression.split(",");
            for (String param:params ){
                try {
                    result.put(param.substring(1,param.indexOf(":")-1),Integer.valueOf(param.substring(param.indexOf(":")+1)));
                }catch (NumberFormatException e){
                    try {
                        result.put(param.substring(1,param.indexOf(":")-1),Integer.valueOf(param.substring(param.indexOf(":")+2,param.length()-1)));
                    }catch (NumberFormatException numberFormatException){
                        LOGGE.info("the expression format  is null");
                    }
                }
            }
        }
        return result;
    }
}
