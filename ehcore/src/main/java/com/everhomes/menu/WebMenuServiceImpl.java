package com.everhomes.menu;

import com.everhomes.acl.*;
import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.db.DbProvider;
import com.everhomes.domain.Domain;
import com.everhomes.domain.DomainService;
import com.everhomes.entity.EntityType;
import com.everhomes.module.*;
import com.everhomes.namespace.NamespacesService;
import com.everhomes.organization.Organization;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.organization.OrganizationService;
import com.everhomes.portal.PortalService;
import com.everhomes.portal.PortalVersion;
import com.everhomes.portal.PortalVersionProvider;
import com.everhomes.rest.acl.ProjectDTO;
import com.everhomes.rest.acl.WebMenuDTO;
import com.everhomes.rest.acl.WebMenuScopeApplyPolicy;
import com.everhomes.rest.acl.WebMenuSelectedFlag;
import com.everhomes.rest.common.TrueOrFalseFlag;
import com.everhomes.rest.module.*;
import com.everhomes.rest.portal.ServiceModuleAppDTO;
import com.everhomes.server.schema.tables.pojos.EhServiceModules;
import com.everhomes.rest.acl.WebMenuType;
import com.everhomes.rest.acl.admin.ListWebMenuResponse;
import com.everhomes.rest.community.CommunityFetchType;
import com.everhomes.rest.menu.*;
import com.everhomes.rest.oauth2.ModuleManagementType;
import com.everhomes.rest.organization.OrganizationStatus;
import com.everhomes.rest.servicemoduleapp.OrganizationAppStatus;
import com.everhomes.serviceModuleApp.*;
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

	@Autowired
	private ServiceModuleAppAuthorizationService serviceModuleAppAuthorizationService;

	@Autowired
	private ServiceModuleAppProvider serviceModuleAppProvider;

	@Autowired
	private OrganizationAppProvider organizationAppProvider;

	@Autowired
	private AppCategoryProvider appCategoryProvider;

	@Autowired
	private ServiceModuleEntryProvider serviceModuleEntryProvider;

	@Autowired
	private PortalVersionProvider portalVersionProvider;
	
	@Autowired
	private NamespacesService namespacesService ;

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
				return listWebMenus(userId, menu, categories, currentOrgId);
			}

		}else /*if(EntityType.fromCode(UserContext.getCurrentSceneType()) == EntityType.ZUOLIN_ADMIN) */{
			 return listZuolinAdminWebMenu(userId, menu, categories);
		}
		return null;
	}

	private List<WebMenuDTO> listWebMenus(Long userId, WebMenu menu, List<String> categories, Long organizationId){

		//拆成两个独立的方法
		//前面一个是获取这个用户在这个公司有权限的应用id，这是权限的锅
		//后面一个是根据应用id获取菜单的，这个是菜单的锅
		List<Long> appOriginIds = listUserAppIds(userId, menu, organizationId);

		// 根据应用id拿菜单
		List<WebMenuDTO> webMenuDTOS = listWebMenuByApp(WebMenuType.PARK.getCode(), appOriginIds);

		return webMenuDTOS;
	}

	@Override
	public List<AppCategoryDTO> listUserAppCategory(ListUserAppCategoryCommand cmd){
		//拆成两个独立的方法
		//前面一个是获取这个用户在这个公司有权限的应用id，这是权限的锅
		//后面一个是根据应用id获取菜单的，这个是菜单的锅
		List<Long> managementAppOriginIds = listUserAppIds(UserContext.currentUserId(), null, cmd.getOrganizationId());

		List<Long> clientAppOriginIds = listClientAppIds(UserContext.getCurrentNamespaceId(), cmd.getOrganizationId());


		List<AppCategoryDTO> dtos = listCategoryByAppIds(managementAppOriginIds, clientAppOriginIds);

		return dtos;
	}


	private List<Long> listUserAppIds(Long userId, WebMenu menu, Long organizationId){

		SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
		String path = null;
		if(null != menu){
			path = menu.getPath() + "/%";
		}
		List<Target> targets = new ArrayList<>();
		targets.add(new Target(EntityType.USER.getCode(), userId));

		//getVersion
		PortalVersion version = this.portalService.findReleaseVersion(UserContext.getCurrentNamespaceId());
		Long versionId = version != null ? version.getId() : null;


		List<Long> appOriginIds = new ArrayList<>();

		// 公司拥有所有权的园区集合
		List<Long> authCommunityIds = serviceModuleAppAuthorizationService.listCommunityRelationOfOrgId(UserContext.getCurrentNamespaceId(), organizationId).stream().map(r->r.getProjectId()).collect(Collectors.toList());
		// 公司分配过园区的应用
		List<Long> authAppIds = serviceModuleAppAuthorizationService.listCommunityAppIdOfOrgId(UserContext.getCurrentNamespaceId(), organizationId);
		List<ServiceModuleApp> communityApps = serviceModuleAppService.listServiceModuleApp(UserContext.getCurrentNamespaceId(), versionId, null, null, null, ModuleManagementType.COMMUNITY_CONTROL.getCode());

		//OA类型的要查询安装的应用
		//List<ServiceModuleApp> orgApps = serviceModuleAppService.listServiceModuleApp(UserContext.getCurrentNamespaceId(), versionId, null, null, null, ModuleManagementType.ORG_CONTROL.getCode());
		List<ServiceModuleApp> orgApps = serviceModuleAppProvider.listInstallServiceModuleApps(UserContext.getCurrentNamespaceId(), versionId, organizationId, null, ServiceModuleAppType.OA.getCode(), null, OrganizationAppStatus.ENABLE.getCode(), null);

		List<ServiceModuleApp> unlimitApps = serviceModuleAppService.listServiceModuleApp(UserContext.getCurrentNamespaceId(), versionId, null, null, null, ModuleManagementType.UNLIMIT_CONTROL.getCode());

		Integer namespaceId = UserContext.getCurrentNamespaceId();
		//TODO do better here 全部appOriginIds
		List<ServiceModuleApp> allApps = this.serviceModuleAppService.listReleaseServiceModuleApps(namespaceId);
		List<ServiceModuleApp> oaApps = new ArrayList<ServiceModuleApp>();//
		for(ServiceModuleApp app: allApps){
			if(ServiceModuleAppType.fromCode(app.getAppType()) == ServiceModuleAppType.OA ){
				oaApps.add(app);
			}
		}
		if(!namespacesService.isStdNamespace(namespaceId)) {
			//非标准版，则使用所有的模块定义的 oa 应用。
			orgApps = oaApps;
		}
		
		//填充OA和无限制的应用id
		orgApps.stream().map(r->authAppIds.add(r.getOriginId())).collect(Collectors.toList());
		unlimitApps.stream().map(r->authAppIds.add(r.getOriginId())).collect(Collectors.toList());

		// 超级管理员拿所有菜单
		if(resolver.checkSuperAdmin(userId, organizationId) || null != path) {
			//管理公司的超管是有authCommunityIds的，普通公司没有，普通公司只拿OA应用
			if(authCommunityIds != null && authCommunityIds.size() > 0 ){
				appOriginIds = allApps.stream().map(r->r.getOriginId()).collect(Collectors.toList());
			}else {
				appOriginIds = orgApps.stream().map(r->r.getOriginId()).collect(Collectors.toList());
			}

		}else {


			List<Long> appIds = new ArrayList<>();

			// 获取人员和人员所有机构所赋予的应用模块权限(应用管理员 + 权限细化) 应用管理员拥有应用对应的菜单
			List<Long> orgIds = organizationService.getIncludeOrganizationIdsByUserId(userId, organizationId);
			for (Long orgId : orgIds) {
				targets.add(new Target(EntityType.ORGANIZATIONS.getCode(), orgId));
			}


			// 园区控制的应用和非园区控制的应用分开查询
			List<String> types = new ArrayList<>();
			types.add(ModuleManagementType.ORG_CONTROL.getCode());
			types.add(ModuleManagementType.UNLIMIT_CONTROL.getCode());
			List<Tuple<Long, String>> appTuples = authorizationProvider.getAuthorizationAppModuleIdsByTargetWithTypes(targets, types);


			types.clear();
			//todo: 这里感觉有问题，有想不出来，秋敢哥解答
			types.add(ModuleManagementType.COMMUNITY_CONTROL.getCode());
			appTuples.addAll(authorizationProvider.getAuthorizationAppModuleIdsByTargetWithTypesAndConfigIds(targets,types, authCommunityIds));

			final List<ServiceModuleApp> finalOrgApps = orgApps; 
			appTuples.stream().map(r -> {
				if (Long.valueOf(r.first()) == 0L) {
					List<ServiceModuleApp> appDtos = null;
					switch (ModuleManagementType.fromCode(r.second())) {
						case COMMUNITY_CONTROL:
							appDtos = communityApps;
							break;
						case ORG_CONTROL:
							appDtos = finalOrgApps;
							break;
						case UNLIMIT_CONTROL:
							appDtos = unlimitApps;
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

		//TODO 1.对authAppIds遍历的时候调用listUserRelatedProjectByModuleId涉及权限有点慢，
		//TODO 2.然后发现这段代码只对标准版有用（原因见下TODO 3），现在用标准版域空间框起来。

		if(namespacesService.isStdNamespace(UserContext.getCurrentNamespaceId())){
			//这里需要优化，需要缓存
			List<Long> authAppIdsWithoutZeroProjects = new ArrayList<>();
			Map<Long, ServiceModuleApp> communityAppMap = new HashMap<Long, ServiceModuleApp>();
			for(ServiceModuleApp r: allApps) {
				if(r.getAppType() != null && r.getAppType().equals(ServiceModuleAppType.COMMUNITY.getCode())) {
					communityAppMap.put(r.getOriginId(), r);
				}
			}
			for(Long authAppId : authAppIds) {
				ServiceModuleApp app = communityAppMap.get(authAppId);
				if(app != null && app.getAppType().equals(ServiceModuleAppType.COMMUNITY.getCode())) {

					//TODO 4.这段代码有点坑，特别慢。感觉他想要查询的是这个公司在这个应用下有权限的项目。换成了TODO 5的代码。权限有问题的话找敢哥。
//					//如果是园区 APP，并且项目数量大于 0 值，则返回此应用的菜单。
//					ListUserRelatedProjectByModuleCommand relatedProjectCmd = new ListUserRelatedProjectByModuleCommand();
//					relatedProjectCmd.setAppId(authAppId);
//					relatedProjectCmd.setCommunityFetchType(CommunityFetchType.ONLY_COMMUNITY.getCode());
//					relatedProjectCmd.setOrganizationId(organizationId);
//					List<ProjectDTO> projects = serviceModuleService.listUserRelatedProjectByModuleId(relatedProjectCmd);
//					if(projects != null && projects.size() > 0) {
//						authAppIdsWithoutZeroProjects.add(authAppId);
//					}

					//TODO 5.直接用标准版授权的代码，因为外面已经被标准框起来了。
					List<ServiceModuleAppAuthorization> authorizations = serviceModuleAppAuthorizationService.listCommunityRelationOfOrgIdAndAppId(UserContext.getCurrentNamespaceId(), organizationId, authAppId);
					if(authorizations != null && authorizations.size() > 0) {
						authAppIdsWithoutZeroProjects.add(authAppId);
					}

				} else {

					//因为授权的应用authAppIds里包含了禁用的oa应用，此处要过滤掉
					OrganizationApp organizationApp = organizationAppProvider.findOrganizationAppsByOriginIdAndOrgId(authAppId, organizationId);
					if(organizationApp == null || OrganizationStatus.fromCode(organizationApp.getStatus()) != OrganizationStatus.ACTIVE){
						continue;
					}

					authAppIdsWithoutZeroProjects.add(authAppId);
				}
			}

			//TODO 3.下面这句话原来是用标准版域空间框起来，因此推理上面整段代码都可以用标准版域空间框起来。
			// 取下交集
			appOriginIds.retainAll(authAppIdsWithoutZeroProjects);
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

		return appOriginIds;
	}



	//获取在pc工作台有用户入口的应用id，这些应用的菜单不需要授权
	private List<Long> listClientAppIds(Integer namespaceId, Long organizationId){


		//1、查询已安装的应用
		PortalVersion releaseVersion = portalVersionProvider.findReleaseVersion(namespaceId);
		List<ServiceModuleApp> apps;
		if(namespacesService.isStdNamespace(namespaceId)){
			apps = serviceModuleAppProvider.listServiceModuleAppsByOrganizationId(releaseVersion.getId(), null, null, organizationId, TrueOrFalseFlag.TRUE.getCode(), null, null, 10000);
		}else {
			apps = serviceModuleAppProvider.listServiceModuleApp(namespaceId, releaseVersion.getId(), null);
		}

		if(apps == null || apps.size()  == 0){
			return null;
		}

		//2、过滤有pc工作台用户端的应用

		//一次查询减轻数据库压力
		Set<Long> moduleIds = new HashSet<>();
		for(ServiceModuleApp app: apps){
			if(app.getModuleId() != null && app.getModuleId() != 0){
				moduleIds.add(app.getModuleId());
			}
		}
		List<ServiceModuleEntry> entries = serviceModuleEntryProvider.listServiceModuleEntries(new ArrayList<>(moduleIds), ServiceModuleLocationType.PC_WORKPLATFORM.getCode(), ServiceModuleSceneType.CLIENT.getCode());
		if(entries == null || entries.size()  == 0){
			return null;
		}

		List<Long> clientAppIds = new ArrayList<>();
		for(ServiceModuleApp app: apps){
			for (ServiceModuleEntry entry: entries){
				if(entry.getModuleId().equals(app.getModuleId())){
					clientAppIds.add(app.getOriginId());
					break;
				}
			}

		}

		return clientAppIds;
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

		List<WebMenu> webMenus = listWebMenuByAppOriginIds(namespaceId, webMenuType, appOriginIds, ServiceModuleSceneType.MANAGEMENT.getCode());

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


	private List<AppCategoryDTO> listCategoryByAppIds(List<Long> managementAppIds, List<Long> clientAppIds){


		Integer namespaceId = UserContext.getCurrentNamespaceId();
		List<WebMenu> webMenus = listWebMenuByAppOriginIds(namespaceId, WebMenuType.PARK.getCode(), managementAppIds, ServiceModuleSceneType.MANAGEMENT.getCode());


		List<WebMenu> clientWebMenus = listWebMenuByAppOriginIds(namespaceId, WebMenuType.PARK.getCode(), clientAppIds, ServiceModuleSceneType.CLIENT.getCode());

		if(webMenus.size() == 0 && clientWebMenus.size() == 0){
			return null;
		}

		webMenus.addAll(clientWebMenus);

		ListAppCategoryCommand cmd = new ListAppCategoryCommand();
		cmd.setLocationType(ServiceModuleLocationType.PC_WORKPLATFORM.getCode());
		cmd.setParentId(0L);

		ListAppCategoryResponse categoryResponse = serviceModuleService.listAppCategory(cmd);
		if(categoryResponse == null && categoryResponse.getDtos() == null && categoryResponse.getDtos().size() == 0){
			return null;
		}

		List<AppCategoryDTO> dtos = categoryResponse.getDtos();

		for (AppCategoryDTO dto: dtos){
			setMenuToAppCategoryDto(dto, webMenus);
		}


		//清除空目录
		if(dtos != null && dtos.size() > 0){
			dtos = dtos.stream().filter((dto) -> {
				boolean removeFlag = removeEmpty(dto);
				return !removeFlag;
			}).collect(Collectors.toList());
		}

		return dtos;

	}


	private boolean removeEmpty(AppCategoryDTO dto){

		if(dto == null){
			return false;
		}


		//1、有下级目录，先遍历下级目录。
		if(dto.getDtos() != null && dto.getDtos().size() > 0){
			List<AppCategoryDTO> subDtos = dto.getDtos().stream().filter((subDto) -> {
				boolean removeFlag = removeEmpty(subDto);
				return !removeFlag;
			}).collect(Collectors.toList());

			dto.setDtos(subDtos);
		}

		//2、处理完下级目录之后，再看看是否剩余非空目录
		if(dto.getDtos() != null && dto.getDtos().size() > 0){
			return false;
		}


		//3、有没有菜单
		if(dto.getMenuDtos() != null && dto.getMenuDtos().size() > 0){
			return false;
		}


		//4、没有非空目录，也没有菜单，要删除。
		return true;
	}


	private void setMenuToAppCategoryDto(AppCategoryDTO dto, List<WebMenu> menus){

		//结束遍历
		if(dto == null || menus == null || menus.size() == 0){
			return;
		}

		//非叶子节点
		if(TrueOrFalseFlag.fromCode(dto.getLeafFlag()) != TrueOrFalseFlag.TRUE && dto.getDtos() != null){
			for (AppCategoryDTO subDto: dto.getDtos()){
				setMenuToAppCategoryDto(subDto, menus);
			}

			return;
		}

		//叶子节点
		List<ServiceModuleEntry> entries = serviceModuleEntryProvider.listServiceModuleEntries(null, dto.getId(), null, null, null);

		if (entries == null || entries.size() == 0){
			return;
		}

		List<WebMenuDTO> menuDtos = new ArrayList<>();

		for (ServiceModuleEntry entry: entries){
			for (WebMenu menu : menus) {
				//模块和场景都一致
				if(entry.getModuleId().equals(menu.getModuleId()) && entry.getSceneType().equals(menu.getSceneType())){
					WebMenuDTO menuDto = ConvertHelper.convert(menu, WebMenuDTO.class);
					ServiceModuleAppDTO appDTO = ConvertHelper.convert(menu.getAppConfig(), ServiceModuleAppDTO.class);
					menuDto.setAppConfig(appDTO);
					menuDtos.add(menuDto);
				}
			}
		}

		dto.setMenuDtos(menuDtos);
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
		List<WebMenuDTO> tree = getTree(0L, WebMenuType.PARK.getCode(), webMenuScopes, ServiceModuleSceneType.MANAGEMENT.getCode());

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
	public List<WebMenuDTO> getTree(Long parentId, String type, List<WebMenuScope> scopes, Byte sceneType){
		List<WebMenuDTO> dtos = new ArrayList<>();
		//获取当前层级的菜单
		List<WebMenu> webMenus = webMenuProvider.listWebMenus(parentId, type, sceneType);
		if(webMenus != null && webMenus.size() > 0){
			for (WebMenu m: webMenus){

				WebMenuDTO dto = ConvertHelper.convert(m, WebMenuDTO.class);

				//在菜单中加入配置状态
				popuScopeToMenuDto(dto, scopes);

					//获取子菜单
				List<WebMenuDTO> leafDtos = getTree(dto.getId(), type, scopes, sceneType);
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


	private List<WebMenu> listWebMenuByAppOriginIds(Integer namespaceId, String webMenuType,  List<Long> appOrginIds, Byte sceneType){

		List<WebMenu> menus = new ArrayList<>();

		if(appOrginIds == null || appOrginIds.size() == 0){
			return menus;
		}

		List<ServiceModuleApp> serviceModuleApps = serviceModuleAppService.listReleaseServiceModuleAppsByOriginIds(namespaceId, appOrginIds);

		if(serviceModuleApps == null || serviceModuleApps.size() == 0){
			return null;
		}

		List<WebMenu> allMenus = webMenuProvider.listWebMenus(null, webMenuType, sceneType);

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
