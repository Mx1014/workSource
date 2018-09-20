package com.everhomes.aclink;

import com.everhomes.module.RouterListener;
import com.everhomes.module.RouterPath;
import com.everhomes.rest.module.RouterInfo;

public class AclinkClientRouterListener implements RouterListener {

    @Override
    public Long getModuleId() {
        return 40100L;
    }
    
    @RouterPath(path = "/index")
    public RouterInfo getIndexRouterInfo(String queryJson){
        RouterInfo routerInfo = new RouterInfo();
        routerInfo.setPath("/index");
        routerInfo.setQuery("isSupportQR=1&isSupportSmart=1");
        return routerInfo;
    }

}
