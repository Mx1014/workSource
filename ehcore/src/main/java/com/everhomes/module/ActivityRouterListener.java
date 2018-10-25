package com.everhomes.module;

import com.alibaba.fastjson.JSON;
import com.everhomes.rest.common.ActivityActionData;
import com.everhomes.rest.module.RouterInfo;
import com.everhomes.util.StringHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Component
public class ActivityRouterListener implements RouterListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(RouterInfoServiceImpl.class);

    @RouterPath(path = "/index")
    public RouterInfo getIndexRouterInfo(String queryJson){
        RouterInfo routerInfo = new RouterInfo();
        routerInfo.setPath("/index");
        String query = getQueryString(queryJson);
        routerInfo.setQuery(query);
        return routerInfo;
    }

    @RouterPath(path = "/detail")
    public RouterInfo getDetailRouterInfo(String queryJson){
        RouterInfo routerInfo = new RouterInfo();
        routerInfo.setPath("/detail");
        String query = getQueryString(queryJson);
        routerInfo.setQuery(query);
        return routerInfo;
    }

    @Override
    public Long getModuleId(){
        return 10600L;
    }


    @Override
    public String getQueryString(String queryJson){

        Map<String, Object> parse = (Map) JSON.parse(queryJson);

        if(parse.size() == 0){
            return null;
        }
        StringBuffer queryBuffer = new StringBuffer();

        for (Map.Entry entry: parse.entrySet()){
            if(entry.getKey().equals("categoryDTOList") || entry.getKey().equals("title")){
                continue;
            }
            try {
                queryBuffer.append(entry.getKey());
                queryBuffer.append("=");
                queryBuffer.append(URLEncoder.encode(entry.getValue().toString(), "UTF-8"));
                queryBuffer.append("&");
            } catch (UnsupportedEncodingException e) {
                //给出错误提示信息
                LOGGER.error("ActivityRouterListener getQueryString URLEncoder.encode fail, queryJson={} ", queryJson);
            }

        }

        if(queryBuffer.length() > 0 ){
            return queryBuffer.substring(0, queryBuffer.length() - 1);
        }

        return null;
    }

}
