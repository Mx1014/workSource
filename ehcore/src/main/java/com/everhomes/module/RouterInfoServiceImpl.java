package com.everhomes.module;

import com.alibaba.fastjson.JSON;
import com.everhomes.rest.module.RouterInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.util.*;

@Service
public class RouterInfoServiceImpl implements RouterInfoService {

    private static final Logger logger = LoggerFactory.getLogger(RouterInfoServiceImpl.class);

    @Autowired(required = false)
    private List<RouterListener> routerListeners;



    @Override
    public RouterInfo getRouterInfo(Long moduleId, String path, String jsonStr) {

        //RouterInfo routerInfo = null;
        if (routerListeners == null) {
            return null;
        }

        String[] splits = path.split("/");
        String tempPath = "";
        //根据最长路径开始依次匹配
        for (int i = splits.length; i > 0; i--) {

            for (int j = 0; j < i; j++) {
                tempPath = "/" + splits[j];
            }

            for (RouterListener listener : routerListeners) {
                if (listener.getModuleId().equals(moduleId)) {
                    Method[] methods = listener.getClass().getMethods();
                    for (Method method : methods) {
                        if (checkRouterPathMethod(method, tempPath)) {
                            method.setAccessible(true);
                            try {
                                return  (RouterInfo) method.invoke(listener, jsonStr);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        }

        return null;
    }

    private boolean checkRouterPathMethod(Method method, String path){
        RouterPath declaredAnnotation = method.getDeclaredAnnotation(RouterPath.class);
        if(declaredAnnotation == null || !path.equals(declaredAnnotation.path())){
            return false;
        }
        int parameterCount = method.getParameterCount();

        if(parameterCount != 1){
            return false;
        }

        Class<?>[] parameterTypes = method.getParameterTypes();
        if(!parameterTypes[0].equals(String.class)){
            return false;
        }

        Class<?> returnType = method.getReturnType();
        if(returnType != RouterInfo.class){
            return false;
        }

        return true;
    }


    @Override
    public String getQueryInDefaultWay(String queryJson){

        if(queryJson == null){
            return null;
        }

        Map<String, Object> parse = (Map)JSON.parse(queryJson);

        if(parse == null || parse.size() == 0){
            return null;
        }
        StringBuffer queryBuffer = new StringBuffer();

        for (Map.Entry entry: parse.entrySet()){
            try {
                queryBuffer.append(entry.getKey());
                queryBuffer.append("=");
                queryBuffer.append(URLEncoder.encode(entry.getValue().toString(), "UTF-8"));
                queryBuffer.append("&");
            } catch (UnsupportedEncodingException e) {
                //给出错误提示信息
                logger.error("getQueryInDefaultWay URLEncoder.encode fail, queryJson={} ", queryJson);
            }

        }

        if(queryBuffer.length() > 0 ){
            return queryBuffer.substring(0, queryBuffer.length() - 1);
        }

        return null;
    }
}