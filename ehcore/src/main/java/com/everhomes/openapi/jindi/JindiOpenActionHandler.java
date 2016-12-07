package com.everhomes.openapi.jindi;

import org.springframework.stereotype.Component;

import com.everhomes.rest.openapi.jindi.JindiDataType;
import com.everhomes.rest.openapi.jindi.JindiFetchDataCommand;

@Component(JindiOpenHandler.JINDI_OPEN_HANDLER+JindiDataType.JindiDataTypeCode.ACTION_CODE)
public class JindiOpenActionHandler implements JindiOpenHandler {

	@Override
	public String fetchData(JindiFetchDataCommand cmd) {
		
		return null;
	}
	
}
