//@formatter:off
package com.everhomes.asset;

import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by Wentian Wang on 2017/9/12.
 */

public class assetUtils {
    public static final String convertStringList2CommaSeparation(List<String> list){
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < list.size(); i++){
            if(i == list.size()-1){
                sb.append(list.get(i));
                break;
            }
            sb.append(list.get(i)+",");
        }
        return sb.toString();
    }
    public static final List<String> convertCommaSeparation2StringList(String s){
        List<String> list = new ArrayList<>();
        if(StringUtils.isEmpty(s)){
            return list;
        }else{
            return Arrays.asList(s.split(","));
        }
    }
}
