package com.everhomes.yellowPage;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.organization.pm.pay.GsonUtil;
import com.everhomes.rest.user.FieldContentType;
import com.everhomes.rest.user.FieldType;
import com.everhomes.rest.user.IdentifierType;
import com.everhomes.rest.user.RequestFieldDTO;
import com.everhomes.user.CustomRequestConstants;
import com.everhomes.user.CustomRequestHandler;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserIdentifier;
import com.everhomes.user.UserProvider;

@Component(CustomRequestHandler.CUSTOM_REQUEST_OBJ_RESOLVER_PREFIX + CustomRequestConstants.SERVICE_ALLIANCE_REQUEST_CUSTOM)
public class ServiceAllianceCustomRequestHandler implements CustomRequestHandler {

	private static final Logger LOGGER=LoggerFactory.getLogger(ServiceAllianceCustomRequestHandler.class);
	
	@Autowired
	private UserProvider userProvider;
	
	@Autowired
	private YellowPageProvider yellowPageProvider;
			
	@Override
	public void addCustomRequest(String json) {
		LOGGER.info("ServiceAllianceCustomRequestHandler addCustomRequest json:" + json);
		
		ServiceAllianceRequests request = GsonUtil.fromJson(json, ServiceAllianceRequests.class);
		request.setNamespaceId(UserContext.getCurrentNamespaceId());
	  
		User user = UserContext.current().getUser();
		request.setCreatorUid(user.getId());
		request.setCreatorName(user.getNickName());
		UserIdentifier identifier = userProvider.findClaimedIdentifierByOwnerAndType(user.getId(), IdentifierType.MOBILE.getCode());
		if(identifier != null)
			request.setCreatorMobile(identifier.getIdentifierToken());
		  
		LOGGER.info("ServiceAllianceCustomRequestHandler addCustomRequest request:" + request);
		yellowPageProvider.createServiceAllianceRequests(request);
	}

	@Override
	public List<RequestFieldDTO> getCustomRequestInfo(Long id) {

		ServiceAllianceRequests request = yellowPageProvider.findServiceAllianceRequests(id);
		List<RequestFieldDTO> fieldList = new ArrayList<RequestFieldDTO>();
//		if(request != null) {
////			String json = request.toString();
////			json = json.replace("\"", "");
////			json = json.substring(1, json.length()-1);
//			LOGGER.info("ServiceAllianceCustomRequestHandler getCustomRequestInfo request:" + request);
//
//			RequestFieldDTO dto = new RequestFieldDTO();
//			dto.setFieldName(field[0]);
//			dto.setFieldValue(field[1]);
//			
//			fieldList.add(dto);
//				}
//			}
//		}
		
		return fieldList;
	}

}
