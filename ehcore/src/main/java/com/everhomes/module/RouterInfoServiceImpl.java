package com.everhomes.module;

import com.everhomes.rest.module.RouterInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class RouterInfoServiceImpl implements RouterService {

    @Autowired(required = false)
    private List<RouterListener> routerListeners;


    @Override
    public RouterInfo getRouterInfo(Long moduleId, String name, String jsonStr){

        if(routerListeners == null){
            return null;
        }

        for (RouterListener listener: routerListeners){

            if(listener.getModuleId().equals(moduleId)){
                RouterPath declaredAnnotation = listener.getClass().getDeclaredAnnotation(RouterPath.class);
                declaredAnnotation.path();

                for (RouterInfo routerInfo: listener.listRouterInfos()){
                    if(routerInfo.getName().equals(name)){
                        listener.setQueryString(routerInfo, jsonStr);
                        return routerInfo;
                    }
                }
            }
        }

        return null;
    }

}