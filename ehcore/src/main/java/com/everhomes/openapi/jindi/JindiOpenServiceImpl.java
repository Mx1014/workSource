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
			JindiOpenHandler handler = getHandler(cmd.getDataType(), cmd.getActionType(), cmd.getSubActionType());
			return handler.fetchData(cmd);
		}
		return null;
	}
	
	private JindiOpenHandler getHandler(String dataType, String actionType, String subActionType) {
		if (dataType != null) {
			if (actionType == null) {
				actionType = "";
			}
			if (subActionType == null) {
				subActionType = "";
			}
			JindiOpenHandler handler = PlatformContext.getComponent(JindiOpenHandler.JINDI_OPEN_HANDLER
					+ dataType + actionType + subActionType);
			if (handler != null) {
				return handler;
			}
		}
		return null;
	}
}
