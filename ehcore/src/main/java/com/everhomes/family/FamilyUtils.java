package com.everhomes.family;

import org.apache.commons.lang.StringUtils;

public class FamilyUtils {
    
    public static String joinDisplayName(String cityName, String communityName ,String buildingName, String apartName){
       
        StringBuilder strBuilder = new StringBuilder();
        
        if (!StringUtils.isEmpty(cityName)){
           strBuilder.append(cityName);
       }

       if (!StringUtils.isEmpty(communityName)){
          strBuilder.append(communityName);
       }
       
        if (!StringUtils.isEmpty(buildingName)){
            strBuilder.append(buildingName);
        }

        if (!StringUtils.isEmpty(apartName)){
            strBuilder.append("-");
            strBuilder.append(apartName);
        }
        return strBuilder.toString();
    }

}
