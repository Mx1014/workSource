package com.everhomes.module;


import com.everhomes.rest.module.RouterInfo;

public interface RouterInfoService {

	RouterInfo getRouterInfo(Long moduleId, String path, String jsonStr);

	String getQueryInDefaultWay(String queryJson);
}
