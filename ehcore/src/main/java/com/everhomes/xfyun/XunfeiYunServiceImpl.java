package com.everhomes.xfyun;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.everhomes.community.Community;
import com.everhomes.community.CommunityProvider;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.organization.OrganizationCommunity;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.portal.PlatformContextNoWarnning;
import com.everhomes.portal.PortalVersion;
import com.everhomes.portal.PortalVersionProvider;
import com.everhomes.rest.common.ServiceModuleConstants;
import com.everhomes.rest.general_approval.GeneralFormDataSourceType;
import com.everhomes.rest.launchpadbase.AppDTO;
import com.everhomes.rest.module.AccessControlType;
import com.everhomes.rest.module.ServiceModuleAppType;
import com.everhomes.rest.module.ServiceModuleLocationType;
import com.everhomes.rest.module.ServiceModuleSceneType;
import com.everhomes.rest.portal.ClientHandlerType;
import com.everhomes.rest.user.LoginToken;
import com.everhomes.rest.xfyun.AfterDealCommand;
import com.everhomes.rest.xfyun.AfterDealResponse;
import com.everhomes.rest.xfyun.QueryRoutersCommand;
import com.everhomes.rest.xfyun.QueryRoutersResponse;
import com.everhomes.rest.xfyun.RouterDTO;
import com.everhomes.rest.xfyun.RouterTypeEnum;
import com.everhomes.serviceModuleApp.ServiceModuleApp;
import com.everhomes.serviceModuleApp.ServiceModuleAppProvider;
import com.everhomes.serviceModuleApp.ServiceModuleAppService;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserService;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.StringHelper;
import com.everhomes.util.WebTokenGenerator;

@Component
public class XunfeiYunServiceImpl implements XunfeiYunService{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(XunfeiYunServiceImpl.class);
	
	@Autowired
	private ServiceModuleAppService serviceModuleAppService;
	
	@Autowired
	private OrganizationProvider organizationProvider;
	@Autowired
	private ConfigurationProvider configurationProvider;
	@Autowired
	private UserService userService;
	@Autowired
	private XfyunMatchProvider xfyunMatchProvider;
	@Autowired
	private CommunityProvider communityProvider;
	 
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
			appDto.setAccessControlType(AccessControlType.ALL.getCode());
		} else {
			appDto.setClientHandlerType(ClientHandlerType.INSIDE_URL.getCode());
			String homeUrl = configurationProvider.getValue("home.url", "http://core.zuolin.com");
			appDto.setRouter("zl://browser/i?url="+homeUrl + "/mobile/static/stay_tuned/index.html"); // 敬请期待
			appDto.setAccessControlType(AccessControlType.ALL.getCode());
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


	@Override
	public void afterDeal(HttpServletRequest req ,HttpServletResponse resp) {
		
		//判断是否用于校验token的请求
		if (isTestRequest(req)) {
			
			//设置返回信息
			addToBody(resp, "text/plain;charset=UTF-8", encryptToken(getAfterDealTestToken()));
			return;
		}
		
		String jsonStr  = getJSONText(req);
		if (null == jsonStr) {
			LOGGER.error("afterDeal jsonStr null:");
			return ;
		}
		LOGGER.info("afterDeal json:"+jsonStr);
		
		XunfeiYunAfterDealTool tool = new XunfeiYunAfterDealTool(jsonStr);
		if (!tool.isSuccess()) {
			LOGGER.error("afterDeal  not success:");
			return;
		}
		
		if (!tool.isVerified()) {
			LOGGER.error("tool not verified");
			return;
		}
		
		//是否登录
		boolean isUser = checkUserToken(tool.getLoginToken()); 
		if (!isUser) {
			LOGGER.error("afterDeal login error token:"+ tool.getLoginToken());
			return ;
		}
		
		AppDTO appDto = queryRouter(tool.getRouterQueryParams());
		if (null == appDto) {
			return ;
		}
		
		String routerString = appDto.toString();
		LOGGER.info("hit router:"+routerString);
		
		//设置返回信息
		addToBody(resp, "application/json;charset=UTF-8", routerString);
	}
	
	private String getAfterDealTestToken() {
		return configurationProvider.getValue(UserContext.getCurrentNamespaceId(), getAfterDealConfigPrefix()+"testToken", "");
	}

	private String getAfterDealConfigPrefix() {
		return "xfyun.tpp.";
	}
	

	private boolean isTestRequest(HttpServletRequest req) {
		
		String signature = req.getParameter("signature");
		if (null != signature) {
			return true;
		}
		
		return false;
	}


	private void addToBody(HttpServletResponse resp, String contentType, String content) {
		resp.setHeader("content-type", contentType);

		ServletOutputStream outputStream =  null;
		try {
			outputStream = resp.getOutputStream();
			outputStream.write(content.getBytes("utf-8"));
			outputStream.flush();
			outputStream.close();
		} catch (IOException e) {
			LOGGER.error("error wirter : e:"+e.getMessage());
		} 
	}
	
	private AppDTO queryRouter(RouterQueryParamCommand cmd) {

		if (!isRouterQueryParamValid(cmd)) {
			return null;
		}

		XfyunMatch match = xfyunMatchProvider.findMatch(cmd.getIntent().getVendor(), cmd.getIntent().getService(),
				cmd.getIntent().getFirstIntent());
		if (null == match) {
			return null;
		}

		if (1 == match.getType()) {
			//自定义配置
			return getSelfRouter(match);
		}
		
		//模块配置
		Community community = communityProvider.findCommunityById(cmd.getContext().getCommunityId());
		if (null == community) {
			return null;
		}
		
		List<AppDTO> dtos = getTargetApps(community.getNamespaceId(), cmd.getContext().getCommunityId(),
				match.getModuleId());
		if (CollectionUtils.isEmpty(dtos)) {
			return null;
		}

		for (AppDTO dto : dtos) {
			if (dto.getName().equals(cmd.getVerifyText())) {
				return dto;
			}
		}

		return dtos.get(0);
	}
	
	private String getRouterByMatch(XfyunMatch match) {
		String router = match.getDefaultRouter();
		if (null == router) {
			return null;
		}

		// 拼接clientHandlerType
		if (router.indexOf("?") < 0) {
			router = router + "?";
		} else {
			router = router + "&";
		}

		router += "clientHandlerType=" + (null == match.getClientHandlerType() ? ClientHandlerType.NATIVE.getCode()
				: match.getClientHandlerType());

		// 设置home.url
		int pos1 = router.indexOf("$");
		if (pos1 < 0) {
			return router;
		}

		int pos2 = router.indexOf("}");

		String homeUrl = configurationProvider.getValue("home.url", null);
		return router.substring(0, pos1) + homeUrl + router.substring(pos2 + 1);
	}
	
	private AppDTO getSelfRouter(XfyunMatch match) {
		AppDTO appDto = new AppDTO();
		appDto.setClientHandlerType(ClientHandlerType.NATIVE.getCode());
		appDto.setAccessControlType(AccessControlType.ALL.getCode());
		appDto.setRouter(getRouterByMatch(match));

		if (null != match.getClientHandlerType()) {
			appDto.setClientHandlerType(match.getClientHandlerType());
		}
		
		if (null != match.getAccessControlType()) {
			appDto.setAccessControlType(match.getAccessControlType());
		}
		
		return appDto;
	}


	private boolean isRouterQueryParamValid(RouterQueryParamCommand cmd) {
		if (null == cmd 
				|| null == cmd.getContext()
				|| null == cmd.getContext().getCommunityId()
				|| null == cmd.getIntent()) {
			
			return false;
		}
		
		return true;
	}


	private boolean checkUserToken(String loginTokenString) {
		LoginToken token = WebTokenGenerator.getInstance().fromWebToken(loginTokenString, LoginToken.class);
        if (!userService.isValid(token)) {
        	return false;
        }
        return true;
	}


	public String getJSONText(HttpServletRequest request) {
		StringBuilder sb = new StringBuilder();

		try { 
			// 获取输入流
			BufferedReader streamReader = new BufferedReader(new InputStreamReader(request.getInputStream(), "UTF-8"));

			String line = null;
			while ((line = streamReader.readLine()) != null) {
				sb.append(line);
			}
		} catch (Exception e) {
			LOGGER.error("can't get json text from request");
			return null;
		}
		
		return sb.toString();
	}
	
	private  String encryptToken(String src) {
		MessageDigest md;
		byte[] b = new byte[1];;
		try {
			md = MessageDigest.getInstance("SHA-1");
			b = md.digest(src.getBytes("utf-8"));
		} catch (NoSuchAlgorithmException | UnsupportedEncodingException e) { 
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return byte2HexStr(b);
	}
	
    /** 
     * 字节数组转化为大写16进制字符串 
     */
   
    private static String byte2HexStr(byte[] b) {  
        StringBuilder sb = new StringBuilder();  
        for (int i = 0; i < b.length; i++) {  
            String s = Integer.toHexString(b[i] & 0xFF);  
            if (s.length() == 1) {  
                sb.append("0");  
            }  
            sb.append(s.toLowerCase());  
        }  
        return sb.toString();  

    }
	
}
