package com.everhomes.util;


import java.util.HashSet;
import java.util.Set;

public class StringTemplateUtil {

    public static Set<String> getTemplateKeys(String line){
        Set<String> result = new HashSet<>();
        if(line == null || line.trim().length() < 2){
            return result;

        }
        char[] chars = line.trim().toCharArray();

        StringBuffer sb = new StringBuffer();
        boolean open = false;
        for(int i=1; i<chars.length; i++){

            //置开始状态
            if(chars[i] == '{' && chars[i -1] == '$'){
               open = true;
               continue;
            }

            //置结束状态
            if(chars[i] == '}' && open){
               open = false;
               if(sb.length() > 0){
                   result.add(sb.toString());
               }
               continue;
            }

            if(open){
                sb.append(chars[i]);
            }

        }

        return result;
    }

}
