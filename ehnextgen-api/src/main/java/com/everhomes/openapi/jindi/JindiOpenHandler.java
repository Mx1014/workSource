package com.everhomes.openapi.jindi;

import com.everhomes.rest.openapi.jindi.JindiFetchDataCommand;
import com.everhomes.rest.openapi.jindi.JindiOpenConstant;

public interface JindiOpenHandler {
	String JINDI_OPEN_HANDLER = "jindi_open_handler_";
	String fetchData(JindiFetchDataCommand cmd);
	
	default Integer getPageSize(Integer pageSize){
		if (pageSize == null) {
			return JindiOpenConstant.PAGE_SIZE;
		}
		return pageSize;
	}
}
