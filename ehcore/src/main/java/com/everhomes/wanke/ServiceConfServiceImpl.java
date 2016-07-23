// @formatter:off
package com.everhomes.wanke;

import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.rest.wanke.CommunityServiceDTO;
import com.everhomes.rest.wanke.ListCommunityCommand;
import com.everhomes.rest.wanke.ListCommunityResponse;
import com.everhomes.rest.wanke.ListCommunityServiceCommand;
import com.everhomes.rest.wanke.ListCommunityServiceResponse;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.wanke.CommunityService;
import com.everhomes.wanke.ServiceConfProvider;
import com.everhomes.wanke.ServiceConfService;
import com.everhomes.wanke.ServiceConfVendorHandler;

@Component
public class ServiceConfServiceImpl implements ServiceConfService {
	private static final Logger LOGGER = LoggerFactory.getLogger(ServiceConfServiceImpl.class);
	
	@Autowired
	private ServiceConfProvider serviceConfProvider;
	@Autowired
	private ConfigurationProvider configProvider;
	
	@Override
	public ListCommunityServiceResponse listCommunityServices(ListCommunityServiceCommand cmd){
		checkParameter(cmd.getOwnerId(), cmd.getOwnerType());
		if(cmd.getScopeCode() == null ) {
        	LOGGER.error("ScopeCode cannot be null.");
    		throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
    				"ScopeCode cannot be null.");
        }
		if(cmd.getScopeId() == null ) {
        	LOGGER.error("ScopeId cannot be null.");
    		throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
    				"ScopeId cannot be null.");
        }
		ListCommunityServiceResponse response = new ListCommunityServiceResponse();
		UserContext context = UserContext.current();
		Integer namespaceId = UserContext.getCurrentNamespaceId();
		Integer pageSize = PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize());

		List<CommunityService> list = serviceConfProvider.listCommunityServices(namespaceId, cmd.getOwnerId(),
				cmd.getOwnerType(), cmd.getScopeCode(), cmd.getScopeId(), pageSize, cmd.getPageAnchor());
		
		response.setRequests(list.stream().map(r -> ConvertHelper.convert(r, CommunityServiceDTO.class))
				.collect(Collectors.toList()));
		if(list.size() > 0){
			if(pageSize != null && list.size() != pageSize){
        		response.setNextPageAnchor(null);
        	}else{
        		response.setNextPageAnchor(list.get(list.size()-1).getId());
        	}
        }
		return response;
	}
	
	@Override
	public ListCommunityResponse loginAndGetCommunities(ListCommunityCommand cmd, HttpServletRequest req, HttpServletResponse resp) {
		cmd.setType("wanke");
		checkListCommunityCommand(cmd.getToken(),cmd.getType());
		ServiceConfVendorHandler handler = getServiceConfVendorHandler(cmd.getType());
		ListCommunityResponse response = handler.loginAndGetCommunities(cmd, req, resp);
		return response;
	}
	
	private ServiceConfVendorHandler getServiceConfVendorHandler(String vendorName) {
		ServiceConfVendorHandler handler = null;
        
        if(vendorName != null && vendorName.length() > 0) {
            String handlerPrefix = ServiceConfVendorHandler.SERVICECONF_VENDOR_PREFIX;
            handler = PlatformContext.getComponent(handlerPrefix + vendorName);
        }
        return handler;
    }
	
	private void checkParameter(Long ownerId, String ownerType) {
		if(ownerId == null ) {
        	LOGGER.error("OwnerId cannot be null.");
    		throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
    				"OwnerId cannot be null.");
        }
    	
    	if(StringUtils.isBlank(ownerType)) {
        	LOGGER.error("OwnerType cannot be null.");
    		throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
    				"OwnerType cannot be null.");
        }
	}
	
	private void checkListCommunityCommand(String token, String type/*, String identifierToken, String organizationToken, String vendor*/) {
		if(StringUtils.isBlank(token)) {
        	LOGGER.error("Token cannot be null.");
    		throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
    				"Token cannot be null.");
        }
    	if(StringUtils.isBlank(type)) {
        	LOGGER.error("Type cannot be null.");
    		throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
    				"Type cannot be null.");
        }
//    	if(StringUtils.isBlank(organizationToken)) {
//        	LOGGER.error("OrganizationToken cannot be null.");
//    		throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
//    				"OrganizationToken cannot be null.");
//        }
//    	if(StringUtils.isBlank(vendor)) {
//        	LOGGER.error("OrganizationId cannot be null.");
//    		throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
//    				"OrganizationId cannot be null.");
//        }
	}
}
