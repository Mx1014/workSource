package com.everhomes.menu;

import com.everhomes.acl.AuthorizationProvider;
import com.everhomes.acl.WebMenu;
import com.everhomes.acl.WebMenuPrivilegeProvider;
import com.everhomes.acl.WebMenuScope;
import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.db.DbProvider;
import com.everhomes.domain.Domain;
import com.everhomes.domain.DomainService;
import com.everhomes.entity.EntityType;
import com.everhomes.module.ServiceModule;
import com.everhomes.module.ServiceModuleProvider;
import com.everhomes.module.ServiceModuleService;
import com.everhomes.organization.Organization;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.organization.OrganizationService;
import com.everhomes.portal.PortalService;
import com.everhomes.portal.PortalVersion;
import com.everhomes.server.schema.tables.pojos.EhServiceModules;
import com.everhomes.serviceModuleApp.ServiceModuleApp;
import com.everhomes.rest.acl.*;
import com.everhomes.rest.acl.WebMenuType;
import com.everhomes.rest.acl.admin.ListWebMenuResponse;
import com.everhomes.rest.menu.*;
import com.everhomes.rest.oauth2.ModuleManagementType;
import com.everhomes.rest.organization.OrganizationType;
import com.everhomes.rest.portal.ServiceModuleAppDTO;
import com.everhomes.serviceModuleApp.ServiceModuleAppService;
import com.everhomes.user.UserContext;
import com.everhomes.user.admin.SystemUserPrivilegeMgr;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.Tuple;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class WebMenuServiceImpl implements WebMenuService {

	private static final Logger LOGGER = LoggerFactory.getLogger(WebMenuServiceImpl.class);

	@Autowired
	private WebMenuPrivilegeProvider webMenuProvider;

	@Autowired
	private OrganizationProvider organizationProvider;

	@Autowired
	private OrganizationService organizationService;

	@Autowired
	private AuthorizationProvider authorizationProvider;

	@Autowired
	private ServiceModuleService serviceModuleService;

	@Autowired
	private ServiceModuleAppService serviceModuleAppService;

	@Autowired
	private DbProvider dbProvider;

	@Autowired
	private ServiceModuleProvider serviceModuleProvider;

	@Autowired
	private WebMenuPrivilegeProvider webMenuPrivilegeProvider;

	@Autowired
	private PortalService portalService;

	@Autowired
	private DomainService domainService;

	@Override
	public List<WebMenuDTO> listUserRelatedWebMenus(ListUserRelatedWebMenusCommand cmd){
		Long userId = cmd.getUserId();
		if(null == userId)
			userId = UserContext.current().getUser().getId();

		List<String> categories = new ArrayList<>();
		WebMenu menu = null;
		if(null == cmd.getMenuId()){
			categories.add(WebMenuCategory.CLASSIFY.getCode());
			categories.add(WebMenuCategory.MODULE.getCode());
		}else{
			menu = webMenuProvider.getWebMenuById(cmd.getMenuId());
			categories.add(WebMenuCategory.PAGE.getCode());
		}

		Domain domain = null;

		if(UserContext.getCurrentNamespaceId() != null){
			domain = domainService.findDomainByNamespaceId(UserContext.getCurrentNamespaceId());
		}
		if(domain == null){
			domain = UserContext.current().getDomain();
		}

		if(null == domain){
			LOGGER.error("domain not configured, userId = {}", userId);
			domain = new Domain();
			domain.setPortalType(EntityType.ORGANIZATIONS.getCode());
//			throw RuntimeErrorException.errorWith(UserServiceErrorCode.SCOPE, UserServiceErrorCode.DOMAIN_NOT_CONFIGURED,
//					"domain not configured");
		}

		Long currentOrgId = cmd.getCurrentOrgId();
		if(currentOrgId == null){
			currentOrgId = domain.getPortalId();
		}
		if(EntityType.fromCode(domain.getPortalType()) == EntityType.ORGANIZATIONS){
			Organization organization = organizationProvider.findOrganizationById(currentOrgId);
			if(null != organization){
				if(OrganizationType.fromCode(organization.getOrganizationType()) == OrganizationType.PM){
					return listPmWebMenu(userId, menu, categories, currentOrgId);
				}else{
					return listEnterpriseWebMenu(userId, menu, categories, currentOrgId);
				}
			}

		}else /*if(EntityType.fromCode(UserContext.getCurrentSceneType()) == EntityType.ZUOLIN_ADMIN) */{
			 return listZuolinAdminWebMenu(userId, menu, categories);
		}
		return null;
	}

	private List<WebMenuDTO> listPmWebMenu(Long userId, WebMenu menu, List<String> categories, Long organizationId){
		SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
		List<WebMenu> menus = new ArrayList<>();
		List<WebMenu> menus_apps = null;
		List<WebMenu> menus_module = null;
		String path = null;
		if(null != menu){
			path = menu.getPath() + "/%";
		}
		List<Target> targets = new ArrayList<>();
		targets.add(new Target(EntityType.USER.getCode(), userId));

//		//物业超级管理员拿所有菜单
//		// todo 这里要按照域空间配置的模块去拿，下个版本改
//		if(resolver.checkSuperAdmin(userId, organizationId) || null != path){
//			menus = webMenuProvider.listWebMenuByType(WebMenuType.PARK.getCode(), categories, path, null);
//			if(null != menu && menus.size() > 0){
//				menus.add(menu);
//			}
//		}else{
//			//todo: 1--根据模块拿菜单
//			//获取人员和人员所有机构所赋予的权限模块(模块管理员) 模块管理员拥有所有模块下的菜单
//			List<Long> moduleIds = authorizationProvider.getAuthorizationModuleIdsByTarget(targets);
//			//获取这些模块对应的菜单
//			if(moduleIds.contains(0L)){
//				moduleIds = serviceModuleService.filterByScopes(UserContext.getCurrentNamespaceId(), null, null).stream().map(r ->{
//					return r.getId();
//				}).collect(Collectors.toList());
//			}
//			if(moduleIds != null && moduleIds.size() > 0)
//				menus_module = webMenuProvider. listWebMenuByType(WebMenuType.PARK.getCode(), categories, null, moduleIds);
//
//
//			//todo: 2--根据应用拿菜单
//			//获取人员的所有相关机构(权限细化)
//			List<Long> orgIds = organizationService.getIncludeOrganizationIdsByUserId(userId, organizationId);
//			for (Long orgId: orgIds) {
//				targets.add(new Target(EntityType.ORGANIZATIONS.getCode(), orgId));
//			}
//
//			//获取人员和人员所有机构所赋予的应用模块权限(应用管理员 + 权限细化) 应用管理员拥有应用对应的菜单
//			List<Tuple<Long,String>> appTuples = authorizationProvider.getAuthorizationAppModuleIdsByTarget(targets);
//			List<Long> appIds = new ArrayList<>();
//			appTuples.stream().map(r->{
//				if(Long.valueOf(r.first()) == 0L){
//					List<ServiceModuleAppDTO> appDtos = null;
//					switch (ModuleManagementType.fromCode(r.second())){
//						case COMMUNITY_CONTROL:
//							appDtos =serviceModuleProvider.listReflectionServiceModuleApp(UserContext.getCurrentNamespaceId(), null, null, null, null, ModuleManagementType.COMMUNITY_CONTROL.getCode());
//							break;
//						case ORG_CONTROL:
//							appDtos = serviceModuleProvider.listReflectionServiceModuleApp(UserContext.getCurrentNamespaceId(), null, null, null, null, ModuleManagementType.ORG_CONTROL.getCode());
//							break;
//						case UNLIMIT_CONTROL:
//							 appDtos = serviceModuleProvider.listReflectionServiceModuleApp(UserContext.getCurrentNamespaceId(), null, null, null, null, ModuleManagementType.UNLIMIT_CONTROL.getCode());
//							break;
//					}
//					if(appDtos != null && appDtos.size() > 0){
//						List<Long> moduleIds_namespace = serviceModuleService.filterByScopes(UserContext.getCurrentNamespaceId(), null, null).stream().map(module ->{
//							return module.getId();
//						}).collect(Collectors.toList());
//						appIds.addAll(appDtos.stream().filter(a-> moduleIds_namespace.contains(a.getModuleId())).map(a-> a.getId()).collect(Collectors.toList()));
//					}
//				}else {
//					appIds.add(r.first());
//				}
//				return null;
//			}).collect(Collectors.toList());
//
//			//根据应用拿菜单
//			List<ServiceModuleAppDTO> dtos = serviceModuleProvider.listReflectionServiceModuleAppByActiveAppIds(UserContext.getCurrentNamespaceId(), appIds);
//			List<WebMenu> menus_app_iter = new ArrayList<>();
//			if (dtos != null) {
//				for (ServiceModuleAppDTO dto : dtos) {
//					Long menuId = dto.getMenuId();
//					if(menuId != null && menuId != 0L){
//						List<Long> menuIdSignle = new ArrayList<>();
//						menuIdSignle.add(menuId);
//						List<WebMenu> menuSignle = webMenuPrivilegeProvider.listWebMenuByMenuIds(menuIdSignle);
//						if(menuSignle != null){
//							List<WebMenu> menuList = webMenuProvider.listWebMenusByPath(menuSignle.get(0).getPath(), null);
//							menuList = menuList.stream().map(r->{
//								r.setAppId(dto.getId());
//								return r;
//							}).collect(Collectors.toList());
//							menus_app_iter.addAll(menuList);
//						}
//					}
//				}
//				menus_apps = menus_app_iter;
//			}
//
//			if(menus_module != null)
//				menus.addAll(menus_module);
//			if(menus_apps != null)
//				menus.addAll(menus_apps);
//
////
////			if(null != moduleIds && moduleIds.size() > 0){
////				if (moduleIds_app != null && moduleIds_app.size() > 0){
////					moduleIds.addAll(moduleIds_app);
////				}
////
////				//获取这些模块对应的菜单
////				if(moduleIds.contains(0L)){
////					moduleIds = serviceModuleService.filterByScopes(UserContext.getCurrentNamespaceId(), null, null).stream().map(r ->{
////						return r.getId();
////					}).collect(Collectors.toList());
////				}
////
////				menus = webMenuProvider. listWebMenuByType(WebMenuType.PARK.getCode(), categories, null, moduleIds);
////			}
//
//			//拼上菜单的所有父级菜单
//			if(menus != null)
//				menus = appendParentMenus(menus);
//
//		}
//
//		if(null == menus || menus.size() == 0){
//			if(null != menu)
//				menus.add(menu);
//			else
//				return new ArrayList<>();
//		}
//		menus = filterMenus(menus, organizationId);
//		List<Long> pathToArray = null;
//		//获取本域空间的所有appId
//		Map<Long, ServiceModuleApp> appMap = serviceModuleProvider.listReflectionAcitveAppIdByNamespaceId(UserContext.getCurrentNamespaceId());
//		List<WebMenuDTO> dtos = new ArrayList<>();
//		Set<Long> appMapKeus = appMap.keySet();
//		for (WebMenu webMenu : menus) {
//			pathToArray = Arrays.stream(webMenu.getPath().split("/")).map(r->{
//				if(!StringUtils.isEmpty(r))
//					return Long.valueOf(r);
//				return null;
//			}).filter(r-> r != null).collect(Collectors.toList());
//			pathToArray.retainAll(appMapKeus);
//			//存在对应的appId
//			if(!pathToArray.isEmpty()){
//				webMenu.setAppId(appMap.get(pathToArray.get(0)).getId());
//			}
//			dtos.add(ConvertHelper.convert(webMenu, WebMenuDTO.class));
//		}

		//return processWebMenus(dtos, ConvertHelper.convert(menu, WebMenuDTO.class)).getDtos();

		//TODO 前面的代码需要提供当前用户有权限的appOriginIds，其他的所有逻辑都不要了

		List<Long> appOriginIds = null;


		//物业超级管理员拿所有菜单
		if(resolver.checkSuperAdmin(userId, organizationId) || null != path) {
			//全部appOriginIds
			List<ServiceModuleApp> allApps = this.serviceModuleAppService.listReleaseServiceModuleApps(UserContext.getCurrentNamespaceId());
			appOriginIds = allApps.stream().map(r->r.getOriginId()).collect(Collectors.toList());
		}else {

			List<Long> appIds = new ArrayList<>();

			// 1、根据模块拿菜单
			//获取人员和人员所有机构所赋予的权限模块(模块管理员)
			List<Long> moduleIds = authorizationProvider.getAuthorizationModuleIdsByTarget(targets);
			if (moduleIds.contains(0L)) {
				moduleIds = serviceModuleAppService.listReleaseServiceModuleIdsByNamespace(UserContext.getCurrentNamespaceId());
//				moduleIds = serviceModuleService.filterByScopes(UserContext.getCurrentNamespaceId(), null, null).stream().map(r -> {
//					return r.getId();
//				}).collect(Collectors.toList());
			}
			if (moduleIds != null && moduleIds.size() > 0) {
				// 根据模块和域空间拿所有应用
				List<ServiceModuleApp> module_apps =this.serviceModuleAppService.listReleaseServiceModuleAppByModuleIds(UserContext.getCurrentNamespaceId(), moduleIds);
				if(module_apps != null && module_apps.size() > 0){
					appIds.addAll(module_apps.stream().map(r->r.getOriginId()).collect(Collectors.toList()));
				}
			}


			// 获取人员和人员所有机构所赋予的应用模块权限(应用管理员 + 权限细化) 应用管理员拥有应用对应的菜单
			List<Long> orgIds = organizationService.getIncludeOrganizationIdsByUserId(userId, organizationId);
			for (Long orgId : orgIds) {
				targets.add(new Target(EntityType.ORGANIZATIONS.getCode(), orgId));
			}

			List<Tuple<Long, String>> appTuples = authorizationProvider.getAuthorizationAppModuleIdsByTarget(targets);

			//getVersion
			PortalVersion version = this.portalService.findReleaseVersion(UserContext.getCurrentNamespaceId());

			Long versionId = version != null ? version.getId() : null;

			appTuples.stream().map(r -> {
				if (Long.valueOf(r.first()) == 0L) {
					List<ServiceModuleApp> appDtos = null;
					switch (ModuleManagementType.fromCode(r.second())) {
						case COMMUNITY_CONTROL:
							appDtos = serviceModuleAppService.listServiceModuleApp(UserContext.getCurrentNamespaceId(), versionId, null, null, null, ModuleManagementType.COMMUNITY_CONTROL.getCode());
							break;
						case ORG_CONTROL:
							appDtos = serviceModuleAppService.listServiceModuleApp(UserContext.getCurrentNamespaceId(), versionId, null, null, null, ModuleManagementType.ORG_CONTROL.getCode());
							break;
						case UNLIMIT_CONTROL:
							appDtos = serviceModuleAppService.listServiceModuleApp(UserContext.getCurrentNamespaceId(), versionId, null, null, null, ModuleManagementType.UNLIMIT_CONTROL.getCode());
							break;
					}
					if (appDtos != null && appDtos.size() > 0) {
						appIds.addAll(appDtos.stream().map(dto->dto.getOriginId()).collect(Collectors.toList()));
					}
				} else {
					appIds.add(r.first());
				}
				return null;
			}).collect(Collectors.toList());




			appOriginIds = appIds;
		}

		LOGGER.debug("Method listPmWebMenu's appOriginIds:" + appOriginIds.toString());


		//查询默认的应用
		List<Long> defaultMenuOriginId = getNoAuthOriginId(UserContext.getCurrentNamespaceId());
		if(defaultMenuOriginId != null){
			appOriginIds.addAll(defaultMenuOriginId);
		}

		//去重
		Set<Long> appOriginIdSet = new HashSet<>();
		appOriginIdSet.addAll(appOriginIds);
		appOriginIds.clear();
		appOriginIds.addAll(appOriginIdSet);

		// 根据应用id拿菜单
		List<WebMenuDTO> webMenuDTOS = listWebMenuByApp(WebMenuType.PARK.getCode(), appOriginIds);


		return webMenuDTOS;

	}

	private List<Long> getNoAuthOriginId(Integer namespaceId){

		List<ServiceModule> serviceModules = serviceModuleProvider.listServiceModulesByMenuAuthFlag((byte)0);
		if(serviceModules == null || serviceModules.size() == 0){
			return null;
		}
		List<Long> moduleIds = serviceModules.stream().map(ServiceModule::getId).collect(Collectors.toList());
		List<ServiceModuleApp> apps = serviceModuleAppService.listReleaseServiceModuleAppByModuleIds(namespaceId, moduleIds);

		if(apps == null || apps.size() == 0){
			return  null;
		}

		return apps.stream().map(r -> r.getOriginId()).collect(Collectors.toList());
	}


	private List<WebMenuDTO> listWebMenuByApp(String webMenuType, List<Long> appOriginIds){

		if(appOriginIds == null || appOriginIds.size() == 0 ){
			return null;
		}

		Integer namespaceId = UserContext.getCurrentNamespaceId();

		List<WebMenu> webMenus = listWebMenuByAppOriginIds(namespaceId, webMenuType, appOriginIds);

		if(webMenus == null || webMenus.size() == 0){
			return null;
		}

		List<WebMenu> parentMenus = listParentMenus(webMenus);
		webMenus.addAll(parentMenus);

		List<WebMenuDTO> webMenuDtos  = new ArrayList<>();
		for (WebMenu webMenu : webMenus) {
			WebMenuDTO dto = ConvertHelper.convert(webMenu, WebMenuDTO.class);
			ServiceModuleAppDTO appDTO = ConvertHelper.convert(webMenu.getAppConfig(), ServiceModuleAppDTO.class);
			dto.setAppConfig(appDTO);
			webMenuDtos.add(dto);
		}

		WebMenuDTO treeDto = processWebMenus(webMenuDtos, null);


		List<WebMenuDTO> dtos = treeDto.getDtos();

		addOaDefaultMenu(dtos, webMenuType);

		return dtos;

	}



	//TODO 临时为"智谷汇"添加“企业办公”下的“协同办公”和“人力资源”资源菜单
	private void addOaDefaultMenu(List<WebMenuDTO> dtos, String webMenuType){

		if(WebMenuType.fromCode(webMenuType) != WebMenuType.PARK || UserContext.getCurrentNamespaceId() == null || (UserContext.getCurrentNamespaceId() != 999945 && UserContext.getCurrentNamespaceId() != 1 && UserContext.getCurrentNamespaceId() != 999946 && UserContext.getCurrentNamespaceId() != 11)) {

			return;

		}

		// 1. 编辑需要添加的菜单

		WebMenuDTO dto1 = new WebMenuDTO();
		dto1.setName("协同办公");

		List<WebMenuDTO> dtos1 = new ArrayList<>();

		WebMenuDTO dto11 = new WebMenuDTO();
		dto11.setName("审批");
		dto11.setDataType("approval-work");
		dtos1.add(dto11);

		WebMenuDTO dto12 = new WebMenuDTO();
		dto12.setName("工作汇报");
		dto12.setDataType("working-conference");
		dtos1.add(dto12);

		WebMenuDTO dto13 = new WebMenuDTO();
		dto13.setName("文档");
		dto13.setDataType("document-work");
		dtos1.add(dto13);

		WebMenuDTO dto14 = new WebMenuDTO();
		dto14.setName("企业公告");
		dto14.setDataType("notice-work");
		dtos1.add(dto14);

		WebMenuDTO dto15 = new WebMenuDTO();
		dto15.setName("日程提醒");
		dto15.setDataType("time-schedule");
		dtos1.add(dto15);

		WebMenuDTO dto16 = new WebMenuDTO();
		dto16.setName("会议预订");
		dto16.setDataType("meeting-work");
		dtos1.add(dto16);

		dto1.setDtos(dtos1);


		WebMenuDTO dto2 = new WebMenuDTO();
		dto2.setName("人力资源");
		List<WebMenuDTO> dtos2 = new ArrayList<>();

		WebMenuDTO dto21 = new WebMenuDTO();
		dto21.setName("通讯录");
		dto21.setDataType("address-book");
		dtos2.add(dto21);

		dto2.setDtos(dtos2);



		List<WebMenuDTO> defaultDtos = new ArrayList<>();
		defaultDtos.add(dto1);
		defaultDtos.add(dto2);


		// 2. 加到原有菜单中
		if(dtos == null){
			dtos = new ArrayList<>();
		}

		boolean isExists = false;

		// 原来已经存在企业办公
		for (WebMenuDTO dto: dtos){
			if(dto.getId().longValue() == 40000010){
				dto.getDtos().addAll(0, defaultDtos);
				isExists = true;
			}
		}

		// 原来不存在企业办公
		if(!isExists){
			WebMenuDTO dto = new WebMenuDTO();
			dto.setName("企业办公");
			dto.setId(40000010L);
			dto.setDtos(defaultDtos);

			dtos.add(0, dto);
		}

	}

	private List<WebMenuDTO> listEnterpriseWebMenu(Long userId, WebMenu menu, List<String> categories, Long organizationId){
		SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
		List<WebMenu> menus = null;
		String path = null;
		if(null != menu){
			path = menu.getPath() + "/%";
		}
		Boolean isAdmin = resolver.checkOrganizationAdmin(userId, organizationId);
		LOGGER.debug("listEnterpriseWebMenu: userId: {}, organizationId: {}, isAdmin: {}", userId, organizationId, isAdmin);
		if(isAdmin){
			menus = webMenuProvider.listWebMenuByType(WebMenuType.ORGANIZATION.getCode(), categories, path, null);
		}
		LOGGER.debug("listEnterpriseWebMenu menus: {}", menus);
		if(null == menus || menus.size() == 0){
			if(null != menu)
				menus.add(menu);
			else
				return new ArrayList<>();
		}
		menus = filterMenus(menus, organizationId);
		LOGGER.debug("listEnterpriseWebMenu after filter menus: {}", menus);
//		return processWebMenus(menus.stream().map(r->{
//			return ConvertHelper.convert(r, WebMenuDTO.class);
//		}).collect(Collectors.toList()), ConvertHelper.convert(menu, WebMenuDTO.class)).getDtos();


		//TODO 暂时普通公司是按照所有应用找菜单的
		List<Long> appOriginIds = null;
		if(appOriginIds == null || appOriginIds.size() == 0){
			List<ServiceModuleApp> serviceModuleApps = serviceModuleAppService.listReleaseServiceModuleApps(UserContext.getCurrentNamespaceId());
			if(serviceModuleApps == null || serviceModuleApps.size() == 0){
				return null;
			}

			appOriginIds = serviceModuleApps.stream().map(r -> r.getOriginId()).collect(Collectors.toList());
		}


		//TODO 前面的代码需要提供当前用户有权限的appOriginIds，其他的所有逻辑都不要了
		//全部appOriginIds   参考serviceModuleAppService.listReleaseServiceModuleApps

		List<WebMenuDTO> webMenuDTOS = listWebMenuByApp(WebMenuType.ORGANIZATION.getCode(), appOriginIds);
		return webMenuDTOS;

	}

	private List<WebMenuDTO> listZuolinAdminWebMenu(Long userId, WebMenu menu, List<String> categories) {

		//超级管理员才能获取左邻的菜单  add by yanjun 20171110
		SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
		resolver.checkUserPrivilege(UserContext.current().getUser().getId(), 0);

		String path = null;
		if(null != menu){
			path = menu.getPath() + "/%";
		}
		List<WebMenu> menus = webMenuProvider.listWebMenuByType(WebMenuType.ZUOLIN.getCode(), categories, path, null);
		if(null == menus || menus.size() == 0){
			if(null != menu)
				menus.add(menu);
			else
				return new ArrayList<>();
		}
		return processWebMenus(menus.stream().map(r->{
			return ConvertHelper.convert(r, WebMenuDTO.class);
		}).collect(Collectors.toList()), ConvertHelper.convert(menu, WebMenuDTO.class)).getDtos();
	}

	private List<WebMenu> appendParentMenus(List<WebMenu> menus){
		List<Long> menuIds = new ArrayList<>();
		for (WebMenu menu: menus) {
			String[] menuIdStrs = menu.getPath().split("/");
			for (String menuIdStr: menuIdStrs) {
				if(!StringUtils.isEmpty(menuIdStr)){
					Long menuId = Long.valueOf(menuIdStr);
					if(!menuIds.contains(menuId)){
						menuIds.add(menuId);
					}
				}
			}
		}
		return webMenuProvider.listWebMenuByMenuIds(menuIds);
	}



	@Override
	public ListWebMenuResponse listZuolinAdminWebMenu() {
		List<WebMenu> menus = webMenuProvider.listWebMenuByType(WebMenuType.ZUOLIN.getCode());
		
		List<WebMenuDTO> menuDtos =  menus.stream().map(r->{
			return ConvertHelper.convert(r, WebMenuDTO.class);
		}).collect(Collectors.toList());
		
		ListWebMenuResponse res = new ListWebMenuResponse();
		res.setMenus(this.processWebMenus(menuDtos, null).getDtos());
		
		return res;
	}

	/**
	 * 过滤菜单
	 * @param menus
	 * @param scopes
     * @return
     */
	private List<WebMenu> filterMenus(List<WebMenu> menus, List<WebMenuScope> scopes){
		List<WebMenu> filterMenus = new ArrayList<>();
//		for (WebMenu menu: menus) {
//			WebMenuScope scope = filterMap.get(menu.getId());
//			LOGGER.debug("listEnterpriseWebMenu filterMenus scope: {}", scope);
//			if(null != scope){
//				if(WebMenuScopeApplyPolicy.fromCode(scope.getApplyPolicy()) == WebMenuScopeApplyPolicy.OVERRIDE){
//					//override menu
//					menu.setName(scope.getMenuName());
//					filterMenus.add(menu);
//				}else if(WebMenuScopeApplyPolicy.fromCode(scope.getApplyPolicy()) == WebMenuScopeApplyPolicy.REVERT){
//					filterMenus.add(menu);
//				}
//				menu.setAppId(scope.getAppId());
//				menu.setConfigId(scope.getConfigId());
//
//			}
//		}

		if(scopes == null){
			return filterMenus;
		}

		for (WebMenuScope scope: scopes){
			for (WebMenu menu: menus){
				if(scope.getMenuId().equals(menu.getId())){
					WebMenu filterMenu = ConvertHelper.convert(menu, WebMenu.class);
					if(WebMenuScopeApplyPolicy.fromCode(scope.getApplyPolicy()) == WebMenuScopeApplyPolicy.OVERRIDE) {
						filterMenu.setName(scope.getMenuName());
					}
					filterMenu.setAppId(scope.getAppId());
					filterMenu.setConfigId(scope.getConfigId());
					filterMenus.add(filterMenu);
				}
			}
		}

		filterMenus.sort((o1, o2) -> o1.getSortNum() - o2.getSortNum());
		LOGGER.debug("listEnterpriseWebMenu filterMenus: {}", filterMenus);
		return filterMenus;
	}

	/**
	 * 根据对应的机构过滤菜单
	 * @param menus
	 * @param organizationId
     * @return
     */
	private List<WebMenu> filterMenus(List<WebMenu> menus, Long organizationId){
		List<WebMenuScope> scopes = webMenuProvider.getWebMenuScopeMapByOwnerId(EntityType.ORGANIZATIONS.getCode(), organizationId);
		if(scopes.size() == 0 ){
			scopes = webMenuProvider.getWebMenuScopeMapByOwnerId(EntityType.NAMESPACE.getCode(), Long.valueOf(UserContext.getCurrentNamespaceId()));
		}
		return filterMenus(menus, scopes);
	}
	
    /**
     * 转换成菜单
     * @param menuDtos
     * @param dto
     * @param dto
     * @return
     */
	private WebMenuDTO processWebMenus(List<WebMenuDTO> menuDtos, WebMenuDTO dto){
		
		List<WebMenuDTO> dtos = new ArrayList<WebMenuDTO>();
		
		if(null == dto){
			dto = new WebMenuDTO();
			dto.setId(0l);
		}
		
		for (WebMenuDTO webMenuDTO : menuDtos) {
			if(dto.getId().equals(webMenuDTO.getParentId())){
				WebMenuDTO menuDto = this.processWebMenus(menuDtos, webMenuDTO);
				dtos.add(menuDto);
			}
		}

		Collections.sort(dtos,new Comparator<WebMenuDTO>(){
			public int compare(WebMenuDTO arg0, WebMenuDTO arg1) {
				if(arg0.getSortNum() == null){
					arg0.setSortNum(0);
				}
				if (arg1.getSortNum() == null){
					arg1.setSortNum(0);
				}
				return arg0.getSortNum().compareTo(arg1.getSortNum());
			}
		});

		dto.setDtos(dtos);
		
		return dto;
	}

	@Override
	public GetTreeWebMenusByNamespaceResponse getTreeWebMenusByNamespace(GetTreeWebMenusByNamespaceCommand cmd) {

		//获取已经配置的菜单
		List<WebMenuScope> webMenuScopes = webMenuProvider.listWebMenuScopeByOwnerId(EntityType.NAMESPACE.getCode(), Long.valueOf(cmd.getNamespaceId()));

		//获取菜单的树状结构，并在菜单中加入上述的配置状态
		List<WebMenuDTO> tree = getTree(0L, WebMenuType.PARK.getCode(), webMenuScopes);

		GetTreeWebMenusByNamespaceResponse response = new GetTreeWebMenusByNamespaceResponse();
		response.setDtos(tree);
		return response;
	}


	/**
	 * 获取菜单的树状结构，并在菜单中加入上述的配置状态
	 * @param parentId
	 * @param type
	 * @param scopes
	 * @return
	 */
	public List<WebMenuDTO> getTree(Long parentId, String type, List<WebMenuScope> scopes){
		List<WebMenuDTO> dtos = new ArrayList<>();
		//获取当前层级的菜单
		List<WebMenu> webMenus = webMenuProvider.listWebMenus(parentId, type);
		if(webMenus != null && webMenus.size() > 0){
			for (WebMenu m: webMenus){

				WebMenuDTO dto = ConvertHelper.convert(m, WebMenuDTO.class);

				//在菜单中加入配置状态
				popuScopeToMenuDto(dto, scopes);

					//获取子菜单
				List<WebMenuDTO> leafDtos = getTree(dto.getId(), type, scopes);
				dto.setDtos(leafDtos);

				dtos.add(dto);
			}
		}
		return dtos;
	}

	/**
	 * 在菜单中加入配置状态
	 * @param dto
	 * @param scopes
	 */
	private void popuScopeToMenuDto(WebMenuDTO dto, List<WebMenuScope> scopes){
		for (WebMenuScope s: scopes){
			if(s.getMenuId().longValue() == dto.getId().longValue()){
				dto.setSelected(WebMenuSelectedFlag.YES.getCode());
				dto.setApplyPolicy(s.getApplyPolicy());
				if(WebMenuScopeApplyPolicy.fromCode(s.getApplyPolicy()) == WebMenuScopeApplyPolicy.OVERRIDE){
					dto.setName(s.getMenuName());
				}
			}
		}
	}

	/**
	 * 更新菜单
	 * @param cmd
	 */
	@Override
	public void updateMenuScopesByNamespace(UpdateMenuScopesByNamespaceCommand cmd){

		Gson gson = new GsonBuilder().disableHtmlEscaping().create();
		List<WebMenuDTO> webMenuDTOS = gson.fromJson(cmd.getJsonDtos(), new TypeToken<List<WebMenuDTO>>() {
		}.getType());

		//从树状结构中获取需要配置的菜单
		List<WebMenuScope> newScopes = fromTree(webMenuDTOS, cmd.getNamespaceId());

		//查询已经配置的菜单
		List<WebMenuScope> oldScopes = webMenuProvider.listWebMenuScopeByOwnerId(EntityType.NAMESPACE.getCode(), Long.valueOf(cmd.getNamespaceId()));
		List<Long> oldScopeIds = new ArrayList<>();

		//只删除park菜单的，上面更新的也是park菜单的  fix
		List<WebMenu> webMenus = webMenuProvider.listWebMenuByType(WebMenuType.PARK.getCode());
		if(oldScopes != null){
			for(WebMenuScope scope: oldScopes){
				for (WebMenu webMenu: webMenus){
					if(scope.getMenuId().longValue() == webMenu.getId()){
						oldScopeIds.add(scope.getId());
					}
				}
			}
		}

		//更新菜单
		dbProvider.execute(status -> {
			if(oldScopeIds.size() > 0){
				webMenuProvider.deleteWebMenuScopes(oldScopeIds);
			}
			webMenuProvider.createWebMenuScopes(newScopes);
			return true;
		});
	}

	/**
	 * 从树状结构中获取需要配置的菜单
	 * @param dtos
	 * @param namespaceId
	 * @return
	 */
	private List<WebMenuScope> fromTree(List<WebMenuDTO> dtos, Integer namespaceId){
		List<WebMenuScope> webMenuScopes = new ArrayList<>();
		if(dtos != null && dtos.size() > 0){
			for (WebMenuDTO dto: dtos){
				if(WebMenuSelectedFlag.fromCode(dto.getSelected()) == WebMenuSelectedFlag.YES){
					WebMenuScope scope = new WebMenuScope();

					//关于名字的配置
					scope.setApplyPolicy(dto.getApplyPolicy());
					if(WebMenuScopeApplyPolicy.fromCode(dto.getApplyPolicy()) == null){
						scope.setApplyPolicy(WebMenuScopeApplyPolicy.REVERT.getCode());
					}else if(WebMenuScopeApplyPolicy.fromCode(dto.getApplyPolicy()) == WebMenuScopeApplyPolicy.OVERRIDE){
						//如果覆盖，去查下是否真的覆盖了。
						WebMenu menu = webMenuProvider.getWebMenuById(dto.getId());
						if(menu.getName().equals(dto.getName())){
							//其实并没有修改
							scope.setApplyPolicy(WebMenuScopeApplyPolicy.REVERT.getCode());
							scope.setMenuName(null);
						}else {
							//真的修改了
							scope.setMenuName(dto.getName());
						}
					}

					scope.setMenuId(dto.getId());
					scope.setOwnerType(EntityType.NAMESPACE.getCode());
					scope.setOwnerId((long)namespaceId);
					webMenuScopes.add(scope);

					//获取子菜单
					List<WebMenuScope> leafScopes = fromTree(dto.getDtos(), namespaceId);
					if(leafScopes != null && leafScopes.size() > 0){
						webMenuScopes.addAll(leafScopes);
					}

				}
			}
		}

		return webMenuScopes;
	}


	private List<WebMenu> listWebMenuByAppOriginIds(Integer namespaceId, String webMenuType,  List<Long> appOrginIds){

		if(appOrginIds == null || appOrginIds.size() == 0){
			return null;
		}

		List<ServiceModuleApp> serviceModuleApps = serviceModuleAppService.listReleaseServiceModuleAppsByOriginIds(namespaceId, appOrginIds);

		if(serviceModuleApps == null || serviceModuleApps.size() == 0){
			return null;
		}

		List<WebMenu> allMenus = webMenuProvider.listWebMenus(null, webMenuType);

		List<WebMenu> menus = new ArrayList<>();
		for(ServiceModuleApp app: serviceModuleApps){
			for(WebMenu menu: allMenus){
				if(app.getModuleId() != null && menu.getModuleId() != null && app.getModuleId().longValue() == menu.getModuleId().longValue()){
					WebMenu selectMenu = ConvertHelper.convert(menu, WebMenu.class);
					selectMenu.setName(app.getName());
					selectMenu.setConfigId(app.getId());
					selectMenu.setAppId(app.getOriginId());
					selectMenu.setAppConfig(app);
					menus.add(selectMenu);
				}
			}
		}

		return menus;

	}

	/**
	 * 不在menus中的parent
	 * @param menus
	 * @return
	 */
	private List<WebMenu> listParentMenus(List<WebMenu> menus){

		Set<Long> parentIds = new HashSet<>();
		for (WebMenu menu: menus) {
			String[] menuIdStrs = menu.getPath().split("/");
			for (String menuIdStr: menuIdStrs) {
				if(!StringUtils.isEmpty(menuIdStr)){
					parentIds.add(Long.valueOf(menuIdStr));
				}
			}
		}

		//可能有一部分是和menus自己重复了，要删除掉
		Set<Long> menuIds = new HashSet<>();
		for(WebMenu menu: menus){
			menuIds.add(menu.getId());
		}

		parentIds.removeAll(menuIds);

		List<WebMenu> parentMenus = webMenuProvider.listWebMenuByMenuIds(new ArrayList<>(parentIds));

		renameMenuFor999930(parentMenus);

		return parentMenus;
	}



	public void renameMenuFor999930(List<WebMenu> menus){

		if(!UserContext.getCurrentNamespaceId().equals(999930) || menus == null){
			return;
		}

		for (WebMenu menu: menus){
			if(menu.getName() != null && menu.getName().contains("园区")){
				String replace = menu.getName().replace("园区", "写字楼");
				menu.setName(replace);
			}
		}

	}


	@Override
	public void refleshMenuByPortalVersion(Long versionId){

//		PortalVersionDTO portalVersionDTO = portalService.findPortalVersionById(versionId);
//		ListServiceModuleAppsCommand cmd = new ListServiceModuleAppsCommand();
//		cmd.setNamespaceId(portalVersionDTO.getNamespaceId());
//		cmd.setVersionId(versionId);
//		ListServiceModuleAppsResponse listServiceModuleAppsResponse = portalService.listServiceModuleApps(cmd);
//		List<ServiceModuleAppDTO> serviceModuleApps = listServiceModuleAppsResponse.getServiceModuleApps();
//
//		List<WebMenuScope> scopes = new ArrayList<>();
//
//		//应用生成的菜单
//		for(ServiceModuleAppDTO dto: serviceModuleApps){
//			if(dto.getModuleId() != null){
//				//门禁设置临时设置，门禁应用在后台配置成“大堂门径”和“公司门禁”，因为客户端只有actiontype对应41000
//				if(dto.getModuleId() == 41000){
//					populateMenuScopeByApplication(scopes, portalVersionDTO.getNamespaceId(), 41010L, dto.getName(), dto.getId());
//					populateMenuScopeByApplication(scopes, portalVersionDTO.getNamespaceId(), 41020L, dto.getName(), dto.getId());
//				}else {
//					populateMenuScopeByApplication(scopes, portalVersionDTO.getNamespaceId(), dto.getModuleId(), dto.getName(), dto.getId());
//				}
//			}
//		}
//
//		//配置域空间固定菜单
//		populateMenuScopeByNamespace(scopes, portalVersionDTO.getNamespaceId());
//
//		webMenuProvider.deleteMenuScopeByOwner(EntityType.NAMESPACE.getCode(), Long.valueOf(cmd.getNamespaceId()));
//		webMenuProvider.deleteMenuScopeByOwner(EntityType.ORGANIZATIONS.getCode(), Long.valueOf(cmd.getNamespaceId()));
//
//		webMenuProvider.createWebMenuScopes(scopes);

	}



//	/**
//	 * 根据应用配置菜单
//	 * @param scopes
//	 * @param namespaceId
//	 * @param moduleId
//	 * @param name
//	 * @param configId
//	 */
//	private void populateMenuScopeByApplication(List<WebMenuScope> scopes, Integer namespaceId, Long moduleId, String name, Long configId){
//
//		List<WebMenu> webMenus = new ArrayList<>();
//		List<WebMenu> parkMenus = webMenuProvider.listMenuByModuleIdAndType(moduleId, WebMenuType.PARK.getCode());
//		List<WebMenu> orgMenus = webMenuProvider.listMenuByModuleIdAndType(moduleId, WebMenuType.ORGANIZATION.getCode());
//		webMenus.addAll(parkMenus);
//		webMenus.addAll(orgMenus);
//		for (WebMenu webMenu: webMenus){
//			WebMenuScope scope = new WebMenuScope();
//			scope.setMenuId(webMenu.getId());
//			scope.setMenuName(name);
//			scope.setApplyPolicy(WebMenuScopeApplyPolicy.OVERRIDE.getCode());
//			scope.setOwnerType(EntityType.NAMESPACE.getCode());
//			scope.setOwnerId(namespaceId.longValue());
//			//scope.setAppId(dto.getOriginId());
//			scope.setConfigId(configId);
//			scopes.add(scope);
//		}
//	}
//
//	/**
//	 * 配置域空间固定菜单
//	 * @param scopes
//	 * @param namespaceId
//	 */
//	private void populateMenuScopeByNamespace(List<WebMenuScope> scopes, Integer namespaceId){
//
//		//固定生成的菜单
//		List<WebMenu> webMenus = new ArrayList<>();
//		List<WebMenu> parkMenus = webMenuProvider.listMenuByTypeAndConfigType(WebMenuType.PARK.getCode(), WebMenuConfigType.NAMESPACE.getCode());
//		List<WebMenu> orgMenus = webMenuProvider.listMenuByTypeAndConfigType(WebMenuType.ORGANIZATION.getCode(), WebMenuConfigType.NAMESPACE.getCode());
//		webMenus.addAll(parkMenus);
//		webMenus.addAll(orgMenus);
//		for (WebMenu webMenu: webMenus) {
//			WebMenuScope scope = new WebMenuScope();
//			scope.setMenuId(webMenu.getId());
//			scope.setApplyPolicy(WebMenuScopeApplyPolicy.REVERT.getCode());
//			scope.setOwnerType(EntityType.NAMESPACE.getCode());
//			scope.setOwnerId(namespaceId.longValue());
//			scopes.add(scope);
//		}
//	}


	@Override
	public ListMenuForPcEntryResponse listMenuForPcEntry(ListMenuForPcEntryCommand cmd) {


		//很无奈，这里的菜单是写死的

		List<Long> moduleIds = new ArrayList<>();

		WebMenuDTO introduces = new WebMenuDTO();
		introduces.setName("走进园区");
		introduces.setDtos(new ArrayList<>());

		moduleIds.clear();
		moduleIds.add(40500L);//园区介绍
		List<WebMenuDTO> services405001 = findWebMenuDtoByModuleIds(cmd.getNamespaceId(), moduleIds);
		if(services405001 != null && services405001.size() > 0){
			for(WebMenuDTO dto: services405001){
				if("园区介绍".equals(dto.getName())){
					introduces.getDtos().add(dto);
					break;
				}
			}

		}

		WebMenuDTO introduce2 = new WebMenuDTO();
		introduce2.setName("名企风采");
		introduce2.setDataType("enterprise-management");
		introduces.getDtos().add(introduce2);

		WebMenuDTO introduce3 = new WebMenuDTO();
		introduce3.setName("联系我们");
		introduce3.setDataType("service-online");
		introduces.getDtos().add(introduce3);



		WebMenuDTO trends = new WebMenuDTO();
		trends.setName("园区动态");
		List<WebMenuDTO> sbuTrends = new ArrayList<>();

		moduleIds.clear();
		moduleIds.add(10300L);//公告
		List<WebMenuDTO> sbuTrend103 = findWebMenuDtoByModuleIds(cmd.getNamespaceId(), moduleIds);
		if(sbuTrend103 != null && sbuTrend103.size() > 0){
			sbuTrends.addAll(sbuTrend103);
		}

		moduleIds.clear();
		moduleIds.add(10800L);//园区快讯
		List<WebMenuDTO> sbuTrend108 = findWebMenuDtoByModuleIds(cmd.getNamespaceId(), moduleIds);
		if(sbuTrend108 != null && sbuTrend108.size() > 0){
			sbuTrends.addAll(sbuTrend108);
		}

		moduleIds.clear();
		moduleIds.add(10600L);//活动
		List<WebMenuDTO> sbuTrend106 = findWebMenuDtoByModuleIds(cmd.getNamespaceId(), moduleIds);
		if(sbuTrend106 != null && sbuTrend106.size() > 0){
			sbuTrends.addAll(sbuTrend106);
		}

		trends.setDtos(sbuTrends);



		WebMenuDTO services = new WebMenuDTO();
		services.setName("园区服务");
		List<WebMenuDTO> sbuServices = new ArrayList<>();

		moduleIds.clear();
		moduleIds.add(40100L);//招租管理
		List<WebMenuDTO> services401 = findWebMenuDtoByModuleIds(cmd.getNamespaceId(), moduleIds);
		if(services401 != null && services401.size() > 0){
			sbuServices.addAll(services401);
		}


		moduleIds.clear();
		moduleIds.add(40500L);//服务联盟
		List<WebMenuDTO> services405 = findWebMenuDtoByModuleIds(cmd.getNamespaceId(), moduleIds);
		if(services405 != null && services405.size() > 0){
			for(WebMenuDTO dto: services405){
				if("园区介绍".equals(dto.getName())){
					continue;
				}
				sbuServices.add(dto);
			}

		}


		moduleIds.clear();
		moduleIds.add(20100L);//物业报修
		List<WebMenuDTO> services201 = findWebMenuDtoByModuleIds(cmd.getNamespaceId(), moduleIds);
		if(services201 != null && services201.size() > 0){
			sbuServices.addAll(services201);
		}

		services.setDtos(sbuServices);


		WebMenuDTO resources = new WebMenuDTO();
		resources.setName("预订中心");


		List<WebMenuDTO> sbuResources = new ArrayList<>();

		moduleIds.clear();
		moduleIds.add(40400L);//资源预定
		List<WebMenuDTO> resources404 = findWebMenuDtoByModuleIds(cmd.getNamespaceId(), moduleIds);
		if(resources404 != null && resources404.size() > 0){
			sbuResources.addAll(resources404);
		}

		moduleIds.clear();
		moduleIds.add(40200L);//工位预定
		List<WebMenuDTO> resources402 = findWebMenuDtoByModuleIds(cmd.getNamespaceId(), moduleIds);
		if(resources402 != null && resources402.size() > 0){
			sbuResources.addAll(resources402);
		}

		resources.setDtos(sbuResources);


		ListMenuForPcEntryResponse response = new ListMenuForPcEntryResponse();

		List<WebMenuDTO> dtos = new ArrayList<>();
		dtos.add(introduces);
		dtos.add(trends);
		dtos.add(services);
		dtos.add(resources);

		response.setDtos(dtos);
		return response;
	}

	/**
	 * 给listMenuForPcEntry用的，只有菜单，没有目录结构
	 * @param namespaceId
	 * @param moduleIds
	 * @return
	 */
	private List<WebMenuDTO> findWebMenuDtoByModuleIds(Integer namespaceId, List<Long> moduleIds){
		List<ServiceModuleApp> apps = serviceModuleAppService.listReleaseServiceModuleAppByModuleIds(namespaceId, moduleIds);

		List<WebMenuDTO> dtos = new ArrayList<>();
		if (apps != null) {
			for (ServiceModuleApp app: apps){

				List<WebMenu> webMenus = webMenuProvider.listMenuByModuleIdAndType(app.getModuleId(), WebMenuType.PARK.getCode());
				if(webMenus != null && webMenus.size() > 0){
					WebMenuDTO dto = ConvertHelper.convert(webMenus.get(0), WebMenuDTO.class);
					dto.setName(app.getName());
					dto.setConfigId(app.getId());
					dtos.add(dto);
				}
			}
		}

		return dtos;
	}

	@Override
	public ListMenuForPcEntryServicesResponse listMenuForPcEntryServices(ListMenuForPcEntryServicesCommand cmd) {


		//很无奈，这里的菜单是写死的

		ListMenuForPcEntryServicesResponse response = new ListMenuForPcEntryServicesResponse();

		List<WebMenuDTO> services = new ArrayList<>();

		List<Long> moduleIds = new ArrayList<>();
		moduleIds.clear();
		moduleIds.add(40100L);//招租管理
		List<WebMenuDTO> services401 = findWebMenuDtoByModuleIds(cmd.getNamespaceId(), moduleIds);
		if(services401 == null || services401.size() ==  0){
			WebMenuDTO dto = new WebMenuDTO();
			dto.setName("园区招商");
			services.add(dto);
		}else {
			services.add(services401.get(0));
		}

		moduleIds.clear();
		moduleIds.add(40500L);//服务联盟
		List<WebMenuDTO> services405 = findWebMenuDtoByModuleIds(cmd.getNamespaceId(), moduleIds);
		if(services405 == null || services405.size() == 0){
			WebMenuDTO dto4051 = new WebMenuDTO();
			dto4051.setName("服务联盟1");
			services.add(dto4051);

			WebMenuDTO dto4052 = new WebMenuDTO();
			dto4052.setName("服务联盟2");
			services.add(dto4052);
		}else if(services405.size() == 1){
			services.add(services405.get(0));

			WebMenuDTO dto4052 = new WebMenuDTO();
			dto4052.setName("服务联盟1");
			services.add(dto4052);
		}else {
			services.add(services405.get(0));
			services.add(services405.get(1));
		}


		moduleIds.clear();
		moduleIds.add(20100L);//物业报修
		List<WebMenuDTO> services201 = findWebMenuDtoByModuleIds(cmd.getNamespaceId(), moduleIds);
		if(services201 == null || services201.size() == 0){
			WebMenuDTO dto = new WebMenuDTO();
			dto.setName("物业服务");
			services.add(dto);
		}else {
			services.add(services201.get(0));
		}


		response.setDtos(services);

		return response;
	}
}
