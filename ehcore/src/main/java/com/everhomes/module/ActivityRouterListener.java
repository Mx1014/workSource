package com.everhomes.module;

import com.alibaba.fastjson.JSON;
import com.everhomes.rest.module.RouterInfo;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Component
public class ActivityRouterListener implements RouterListener {

    @Override
    public List<RouterInfo> listRouterInfos(){

        List<RouterInfo> routerInfos = new ArrayList<>();

        RouterInfo index = new RouterInfo();
        index.setName(INDEX_NAME);
        index.setPath("list-nearby");
        routerInfos.add(index);

        RouterInfo detail = new RouterInfo();
        detail.setName("detail");
        detail.setPath("d");
        routerInfos.add(index);

        return routerInfos;
    }

    @Override
    public Long getModuleId(){
        return 10600L;
    }

    @Override
    public void setQueryString(RouterInfo routerInfo, String jsonStr){

        Map<String, Object> parse = (Map) JSON.parse(jsonStr);

        if(parse.size() == 0){
            return;
        }
        StringBuffer queryBuffer = new StringBuffer();

        for (Map.Entry entry: parse.entrySet()){
            if(entry.getKey().equals("categoryDTOList")){
                continue;
            }
            queryBuffer.append(entry.getKey());
            queryBuffer.append("=");
            queryBuffer.append(entry.getValue());
            queryBuffer.append("&");
        }

        String queryString = queryBuffer.substring(0, queryBuffer.length() - 1);

        routerInfo.setQuery(queryString);
    }

}
