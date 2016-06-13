package com.everhomes.core;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.PropertyOverrideConfigurer;

public class DataSourcePropertyOverrideConfigurer extends PropertyOverrideConfigurer {
    public static final String KEY_DRIVER_CLASSNAME = "driverClassName";
    public static final String KEY_ENCRYPTED = "encrypted";
    public static final String KEY_USERNAME = "username";
    public static final String KEY_PASSWORD = "password";
    
    @Value("${db.driver:}")
    private String driverName;
    
    @Value("${db.master:}")
    private String dbUrl;
    
    //db.driver: com.mysql.jdbc.Driver
    //db.master: jdbc:mysql://ehcore:ehcore@db-master:3306/ehcore?characterEncoding=UTF-8&encrypted=0
    
    protected String convertProperty(String propertyName, String propertyValue) {
        if(KEY_DRIVER_CLASSNAME.equals(propertyName)) {
            return driverName;
        }
        
        Map<String, String> map = parse(dbUrl);
        if(KEY_USERNAME.equals(propertyName)) {
            return map.get(propertyName);
        }
        
        if(KEY_PASSWORD.equals(propertyName)) {
            String encryptedFlag = map.get(KEY_ENCRYPTED);
            if("1".equals(encryptedFlag)) {
                return map.get(propertyName);
            } else {
                return map.get(propertyName);
            }
        }
        
        return null;
    }
    
    private Map<String, String> parse(String url) {
        Map<String, String> map = new HashMap<String, String>();
        
        if(url == null || url.trim().length() == 0) {
            return map;
        } else {
            url = url.trim();
        }
        
        try {
            int slashPos = url.indexOf("//");
            if(slashPos != -1) {
                int atPos = url.indexOf("@");
                if(atPos != -1) {
                    String userInfo = url.substring(slashPos + 2, atPos);
                    if(userInfo != null) {
                        String[] segments = userInfo.split(":");
                        if(segments != null && segments.length >= 2) {
                            map.put(KEY_USERNAME, segments[0].trim());
                            map.put(KEY_PASSWORD, segments[1].trim());
                        }
                    }
                }
            }
            
            int encryptedKeyPos = url.indexOf(KEY_ENCRYPTED);
            int encryptedValuePos = encryptedKeyPos + KEY_ENCRYPTED.length() + 2;
            if(encryptedKeyPos != -1 && url.length() > encryptedValuePos) {
                String encryptedValue = url.substring(encryptedValuePos, encryptedValuePos + 1);
                map.put(KEY_ENCRYPTED, encryptedValue);
            }
        } catch (Exception e) {
            
        }
        
        return map;
    }
}
