package com.everhomes.openapi.jindi;

import org.springframework.stereotype.Component;

import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.rest.openapi.jindi.JindiDataType;
import com.everhomes.rest.openapi.jindi.JindiFetchDataCommand;

@Component
public class JindiOpenServiceImpl implements JindiOpenService {

	@Override
	public String fetchData(JindiFetchDataCommand cmd) {
		if (JindiDataType.fromCode(cmd.getDataType()) != null) {
			JindiOpenHandler handler = getHandler(cmd.getDataType());
			return handler.fetchData(cmd);
		}
		return null;
	}
	
	private JindiOpenHandler getHandler(String dataType) {
		if (dataType != null) {
			JindiOpenHandler handler = PlatformContext.getComponent(JindiOpenHandler.JINDI_OPEN_HANDLER
					+ dataType);
			if (handler != null) {
				return handler;
			}
		}
		return null;
	}
}
