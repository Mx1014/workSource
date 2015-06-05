package com.everhomes.launchpad;


public class LaunchPadUtils {
    
    public static String addParameterToLink(String link, String paramName, String paramValue)
    {
        if (link != null && paramName != null && paramValue != null)
        {
            StringBuilder strBuilder = new StringBuilder(link);
            boolean isFirstParam = false;
            if (strBuilder.indexOf("?") == -1){
                strBuilder.append("?");
                isFirstParam = true;
            }
            if (!isFirstParam){
                strBuilder.append("&");
            }
            strBuilder.append(paramName);
            strBuilder.append("=");
            strBuilder.append(paramValue);

            return strBuilder.toString();
        } else{
            return link;
        }
    }


}
