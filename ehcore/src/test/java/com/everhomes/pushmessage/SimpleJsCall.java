package com.everhomes.pushmessage;

import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class SimpleJsCall {
    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleJsCall.class);
    
    public void call(Map map){
        JSONObject json = new JSONObject(map); // convert map to an json Object
        LOGGER.info("json:" + json.toJSONString());
    }
    
    public void jsonField(Map map, String field) {
        // try {
        //     // Object o = PropertyUtils.getProperty(map, field);
        //     // LOGGER.info("o=" + o);
        // } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
        //     // TODO Auto-generated catch block
        //     e.printStackTrace();
        // }
    }
}
