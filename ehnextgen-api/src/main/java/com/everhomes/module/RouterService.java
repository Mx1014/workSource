package com.everhomes.module;


import com.everhomes.rest.module.RouterInfo;

public interface RouterService {

	RouterInfo getRouterInfo(Long moduleId, String name, String jsonStr);
}
