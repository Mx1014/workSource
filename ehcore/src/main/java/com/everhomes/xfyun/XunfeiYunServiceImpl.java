package com.everhomes.xfyun;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.organization.OrganizationCommunity;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.portal.PlatformContextNoWarnning;
import com.everhomes.portal.PortalVersion;
import com.everhomes.portal.PortalVersionProvider;
import com.everhomes.rest.common.ServiceModuleConstants;
import com.everhomes.rest.general_approval.GeneralFormDataSourceType;
import com.everhomes.rest.launchpadbase.AppDTO;
import com.everhomes.rest.module.ServiceModuleAppType;
import com.everhomes.rest.module.ServiceModuleLocationType;
import com.everhomes.rest.module.ServiceModuleSceneType;
import com.everhomes.rest.portal.ClientHandlerType;
import com.everhomes.rest.xfyun.QueryRoutersCommand;
import com.everhomes.rest.xfyun.QueryRoutersResponse;
import com.everhomes.rest.xfyun.RouterDTO;
import com.everhomes.rest.xfyun.RouterTypeEnum;
import com.everhomes.serviceModuleApp.ServiceModuleApp;
import com.everhomes.serviceModuleApp.ServiceModuleAppProvider;
import com.everhomes.serviceModuleApp.ServiceModuleAppService;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.StringHelper;

@Component
public class XunfeiYunServiceImpl implements XunfeiYunService{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(XunfeiYunServiceImpl.class);
	
	@Autowired
	ServiceModuleAppProvider serviceModuleAppProvider;
	@Autowired
	ServiceModuleAppService serviceModuleAppService;
	
	@Autowired
	OrganizationProvider organizationProvider;
	@Autowired
	PortalVersionProvider portalVersionProvider;
	@Autowired
	ConfigurationProvider configurationProvider;
	 
    private static Map<Long, RouterTypeEnum> APP_ROUTER_TYPE_ENUM_MAP = new HashMap<>(20);
    private static Map<Integer, RouterTypeEnum> SELF_ROUTER_TYPE_ENUM_MAP = new HashMap<>(10);	
    
	//初始化
    public XunfeiYunServiceImpl() {
        for (RouterTypeEnum value : RouterTypeEnum.values()) {
        	
        	if (value.getCode() < RouterTypeEnum.MIN_SELF_ROUTER_CODE) {
        		APP_ROUTER_TYPE_ENUM_MAP.put(value.getModuleId(), value);
        		continue;
        	}
        	
        	SELF_ROUTER_TYPE_ENUM_MAP.put(value.getCode(), value);
        }
    }
	
	
	@Override
	public QueryRoutersResponse queryRouters(QueryRoutersCommand cmd) {

		// 业务跳转
		QueryRoutersResponse resp = new QueryRoutersResponse();
		List<AppDTO> appDtos = getTargetApps(UserContext.getCurrentNamespaceId(), cmd.getContext().getCommunityId(),
				null);

		List<RouterDTO> routerDtos = new ArrayList<>();
		buildModuleRouters(routerDtos, appDtos);
		buildExtraRouters(routerDtos);
		resp.setRouterDtos(routerDtos);
		return resp;
	}
	
	private void buildExtraRouters(List<RouterDTO> routerDtos) {
		for (RouterTypeEnum typeEnum : SELF_ROUTER_TYPE_ENUM_MAP.values()) {
			buildSelfRouter(routerDtos, typeEnum);
		}
	}


	private void buildSelfRouter(List<RouterDTO> routerDtos, RouterTypeEnum typeEnum) {

		AppDTO appDto = new AppDTO();

		if (RouterTypeEnum.MY_APPLY.getCode().equals(typeEnum.getCode())) {
			appDto.setClientHandlerType(ClientHandlerType.NATIVE.getCode());
			appDto.setRouter("zl://workflow/tasks");
		} else {
			appDto.setClientHandlerType(ClientHandlerType.INSIDE_URL.getCode());
			String homeUrl = configurationProvider.getValue("home.url", "http://core.zuolin.com");
			appDto.setRouter(homeUrl + "/mobile/static/stay_tuned/index.html"); // 敬请期待
		}
		
		RouterDTO rDto = new RouterDTO();
		rDto.setRouterType(typeEnum.getCode());
		rDto.setAppDto(appDto);
		routerDtos.add(rDto);
	}


	private void buildModuleRouters(List<RouterDTO> routerDtos, List<AppDTO> appDtos) {
		if (CollectionUtils.isEmpty(appDtos)) {
			return;
		}

		for (AppDTO dto : appDtos) {
			RouterTypeEnum typeEnum = APP_ROUTER_TYPE_ENUM_MAP.get(dto.getModuleId());
			if (null != typeEnum) {
				RouterDTO routerDto = new RouterDTO();
				routerDto.setRouterType(typeEnum.getCode());
				routerDto.setAppDto(dto);
				routerDtos.add(routerDto);
			}
		}

	}

	
	private List<AppDTO> getTargetApps(Integer namespaceId, Long communityId, Long moduleId) {
		OrganizationCommunity organizationProperty = organizationProvider.findOrganizationProperty(communityId);
		if (null == organizationProperty) {
			return new ArrayList<>(1);
		}

		Long orgId = organizationProperty.getOrganizationId();
		Byte sceneType = ServiceModuleSceneType.CLIENT.getCode();
		List<ServiceModuleApp> apps = serviceModuleAppService.listReleaseServiceModuleApp(namespaceId, moduleId, null,
				null, null);
		return serviceModuleAppService.toAppDtos(communityId, orgId, sceneType, apps);
	}
	
}
