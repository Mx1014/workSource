package com.everhomes.openapi.jindi;

import com.everhomes.rest.openapi.jindi.JindiFetchDataCommand;

public interface JindiOpenService {

	String fetchData(JindiFetchDataCommand cmd);
	
}
