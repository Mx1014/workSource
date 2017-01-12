package com.everhomes.openapi.jindi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.configuration.ConfigConstants;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.rest.openapi.jindi.JindiDataType;
import com.everhomes.rest.openapi.jindi.JindiFetchDataCommand;
import com.everhomes.util.RuntimeErrorException;

@Component
public class JindiOpenServiceImpl implements JindiOpenService {
	
	@Autowired
	private ConfigurationProvider configurationProvider;
	
	@Override
	public String fetchData(JindiFetchDataCommand cmd) {
		checkNamespace(cmd.getNamespaceId());
		if (JindiDataType.fromCode(cmd.getDataType()) != null) {
			JindiOpenHandler handler = getHandler(cmd.getDataType(), cmd.getActionType(), cmd.getSubActionType());
			return handler.fetchData(cmd);
		}
		return null;
	}
	
	private void checkNamespace(Integer namespaceId) {
		if (namespaceId != null) {
			String jindiSyncNamespace = configurationProvider.getValue(ConfigConstants.JINDI_SYNC_NAMESPACE, "999989,999991");
			if (jindiSyncNamespace.contains(",")) {
				String[] arr = jindiSyncNamespace.split(",");
				for (String str : arr) {
					if (str.equals(String.valueOf(namespaceId))) {
						return;
					}
				}
			}else {
				if (jindiSyncNamespace.equals(String.valueOf(namespaceId))) {
					return;
				}
			}
		}
		throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
				"namespace error: namespaceId="+namespaceId);
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
