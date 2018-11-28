// @formatter:off
package com.everhomes.module;

import com.alibaba.fastjson.JSON;
import com.everhomes.portal.PortalVersion;
import com.everhomes.portal.PortalVersionProvider;
import com.everhomes.user.UserContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

@Component
public class OrdinaryLinkRouterListener{
    private static final Logger LOGGER = LoggerFactory.getLogger(OrdinaryLinkRouterListener.class);


    public String getQueryString(String queryJson, Long appId){

        Map<String, Object> parse = (Map) JSON.parse(queryJson);

        if(parse.size() == 0){
            return null;
        }
        StringBuffer queryBuffer = new StringBuffer();

        for (Map.Entry entry: parse.entrySet()){
            if(entry.getKey() != null && entry.getKey().toString().toLowerCase().equals("url")){
                queryBuffer.append(entry.getKey());
                queryBuffer.append("=");
                queryBuffer.append(getUrl(entry.getValue().toString(),appId));
                queryBuffer.append("&");
                continue;
            }
            try {
                queryBuffer.append(entry.getKey());
                queryBuffer.append("=");
                queryBuffer.append(URLEncoder.encode(entry.getValue().toString(), "UTF-8"));
                queryBuffer.append("&");
            } catch (UnsupportedEncodingException e) {
                //给出错误提示信息
                LOGGER.error("OrdinaryLinkRouterListener getQueryString URLEncoder.encode fail, queryJson={} ", queryJson);
            }

        }

        if(queryBuffer.length() > 0 ){
            return queryBuffer.substring(0, queryBuffer.length() - 1);
        }

        return null;
    }

    private String getUrl(String url, Long appId) {
        if (!StringUtils.isEmpty(url)) {
            String returnUrl = "";
            if (url.indexOf("?") > 0) {
                String[] urls = url.split("[?]");
                returnUrl = urls[0] + "?appId=" + appId + "&" + urls[1];
                if (urls.length > 2) {
                    for (int i=2; i< urls.length; i++) {
                       returnUrl = returnUrl + "?" + urls[i];
                    }
                }
            }else {
                returnUrl = url + "?appId=" + appId;
            }
            return returnUrl;
        }
        return "";
    }
}
