package com.everhomes.openapi;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.everhomes.configuration.ConfigConstants;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.configuration.FindServerInfoCommand;
import com.everhomes.rest.configuration.ServerInfoDTO;
import com.everhomes.user.UserContext;

@RestDoc(value="Base Info Controller", site="baseInfo")
@RestController
@RequestMapping("/openapi")
public class BaseInfoOpenController extends ControllerBase{
	
	@Autowired
	private ConfigurationProvider configurationProvider;
	
	@RequestMapping("findServerInfo")
	@RestReturn(ServerInfoDTO.class)
	public RestResponse findServerInfo(@Valid FindServerInfoCommand cmd) {
		cmd.setNamespaceId(UserContext.getCurrentNamespaceId(cmd.getNamespaceId()));
		
		ServerInfoDTO dto = new ServerInfoDTO();
		dto.setoAuthServer(configurationProvider.getValue(cmd.getNamespaceId(), ConfigConstants.OAUTH_SERVER, ""));
		dto.setPayServer(configurationProvider.getValue(cmd.getNamespaceId(), ConfigConstants.PAY_SERVER, ""));
		
		RestResponse result =  new RestResponse(dto);
		result.setErrorCode(ErrorCodes.SUCCESS);
		result.setErrorDescription("OK");
		return result;
	}
}
