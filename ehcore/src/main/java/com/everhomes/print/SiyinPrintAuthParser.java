//@formatter:off
package com.everhomes.print;

import com.everhomes.portal.PortalUrlParser;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;


@Component
public class SiyinPrintAuthParser implements PortalUrlParser {
	 private static Logger LOGGER = LoggerFactory.getLogger(SiyinPrintAuthParser.class);
	 
    @Override
    public Long getModuleId(Integer namespaceId, String actionData, Byte actionType, String itemLabel) {
    	LOGGER.info("SiyinPrintAuthParser actionData={},actionType={},namespaceId={}",actionData,actionType,namespaceId);
        if(actionType == 13 || actionType == 14){
        	LOGGER.info("actionData:"+actionData);
            Map<String,String> data= new Gson().fromJson(actionData,new TypeToken<HashMap<String,String>>(){}.getType());
            if(data!=null && data.size() > 0 && data.get("url")!=null){
                String wd = data.get("url");
                LOGGER.info("wd:"+wd);
//              if(wd.contains("/cloud-print/build/index.htm")){
                if(wd.contains("cloud-print")){
                    return 41400L;//云打印
                }
                if(wd.contains("park-introduction")){
//                if(wd.contains("/park-introduction/index.html")){
                	return 10200L;//园区介绍
                }
                if(wd.contains("park-news-web"))
//                	 if(wd.contains("/park-news-web/build/index.html"))
                {
                	return 10800L;//园区快讯
                }
                
                if(wd.contains("questionnaire-survey"))
//                	if(wd.contains("/questionnaire-survey/build/index.htm"))
                {
                	return 41700L;//问卷调查
                }
                if(wd.contains("station-booking"))
//                	if(wd.contains("/station-booking/index.html"))
                {
                	return 40200L;//工位预定
                }
                
                if(wd.contains("goods-move"))
//                	if(wd.contains("/goods-move/build/index.html"))
                {
                	return 49200L;//物品搬迁
                }
                
                if(wd.contains("deliver"))
//                	if(wd.contains("/deliver/dist/index.html"))
                {
                	return 40700L;//快递
                }
                
                if(wd.contains("metro_card"))
//                	if(wd.contains("/metro_card/index.html"))
                {
                	return 41200L;//一卡通？
                }
                
                if(wd.contains("property-repair-web"))
//                	if(wd.contains("/property-repair-web/build/index.html"))
                {
                	return 20100L;//物业报修
                }
            }
        }
        return null;
    }
    
    public static void main(String[] args) {
		System.out.println(new SiyinPrintAuthParser().getModuleId(null, "{\"url\":\"http://opv2-test.zuolin.com/cloud-print/build/index.html#/home#sign_suffix\"}", (byte)13, null));
	}
}
