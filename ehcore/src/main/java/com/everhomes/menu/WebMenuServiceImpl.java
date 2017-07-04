package com.everhomes.menu;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.everhomes.acl.AuthorizationProvider;
import com.everhomes.acl.WebMenuScope;
import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.domain.Domain;
import com.everhomes.entity.EntityType;
import com.everhomes.module.ServiceModuleService;
import com.everhomes.organization.Organization;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.organization.OrganizationService;
import com.everhomes.rest.acl.WebMenuScopeApplyPolicy;
import com.everhomes.rest.common.PortalType;
import com.everhomes.rest.menu.ListUserRelatedWebMenusCommand;
import com.everhomes.rest.menu.WebMenuCategory;
import com.everhomes.rest.organization.OrganizationType;
import com.everhomes.rest.user.UserServiceErrorCode;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.user.admin.SystemUserPrivilegeMgr;
import com.everhomes.util.RuntimeErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.acl.WebMenu;
import com.everhomes.acl.WebMenuPrivilegeProvider;
import com.everhomes.rest.acl.WebMenuDTO;
import com.everhomes.rest.acl.WebMenuType;
import com.everhomes.rest.acl.admin.ListWebMenuResponse;
import com.everhomes.util.ConvertHelper;
import org.springframework.util.StringUtils;

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
		Domain domain = UserContext.current().getDomain();
		Long currentOrgId = cmd.getCurrentOrgId();
		if(null == domain){
			LOGGER.error("domain not configured, userId = {}", userId);
			domain = new Domain();
			domain.setPortalType(PortalType.PM.getCode());
//			throw RuntimeErrorException.errorWith(UserServiceErrorCode.SCOPE, UserServiceErrorCode.DOMAIN_NOT_CONFIGURED,
//					"domain not configured");
		}
		if(null == cmd.getCurrentOrgId()){
			currentOrgId = domain.getPortalId();
		}
		if(PortalType.fromCode(domain.getPortalType()) == PortalType.PM || PortalType.fromCode(domain.getPortalType()) == PortalType.ENTERPRISE){
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
		List<WebMenu> menus = null;
		String path = null;
		if(null != menu){
			path = menu.getPath() + "/%";
		}
		List<Target> targets = new ArrayList<>();
		targets.add(new Target(EntityType.USER.getCode(), userId));

		//物业超级管理员拿所有菜单
		if(resolver.checkSuperAdmin(userId, organizationId) || null != path){
			menus = webMenuProvider.listWebMenuByType(WebMenuType.PARK.getCode(), categories, path, null);
			if(null != menu && menus.size() > 0){
				menus.add(menu);
			}
		}else{
			//获取人员的所有相关机构
			List<Long> orgIds = organizationService.getIncludeOrganizationIdsByUserId(userId, organizationId);
			for (Long orgId: orgIds) {
				targets.add(new Target(EntityType.ORGANIZATIONS.getCode(), orgId));
			}
			//获取人员和人员所有机构所赋予的权限模块
			List<Long> moduleIds = authorizationProvider.getAuthorizationModuleIdsByTarget(targets);
			if(null != moduleIds && moduleIds.size() > 0)
				if(moduleIds.contains(0L)){
					moduleIds = serviceModuleService.filterByScopes(UserContext.getCurrentNamespaceId(), null, null).stream().map(r ->{
						return r.getId();
					}).collect(Collectors.toList());
				}
				menus = webMenuProvider.listWebMenuByType(WebMenuType.PARK.getCode(), categories, null, moduleIds);

			//拼上菜单的所有父级菜单
			menus = appendParentMenus(menus);

		}

		if(null == menus || menus.size() == 0){
			if(null != menu)
				menus.add(menu);
			else
				return new ArrayList<>();
		}
		menus = filterMenus(menus, organizationId);
		return processWebMenus(menus.stream().map(r->{
			return ConvertHelper.convert(r, WebMenuDTO.class);
		}).collect(Collectors.toList()), ConvertHelper.convert(menu, WebMenuDTO.class)).getDtos();
	}

	private List<WebMenuDTO> listEnterpriseWebMenu(Long userId, WebMenu menu, List<String> categories, Long organizationId){
		SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
		List<WebMenu> menus = null;
		String path = null;
		if(null != menu){
			path = menu.getPath() + "/%";
		}
		if(resolver.checkOrganizationAdmin(userId, organizationId)){
			menus = webMenuProvider.listWebMenuByType(WebMenuType.ORGANIZATION.getCode(), categories, path, null);
		}
		if(null == menus || menus.size() == 0){
			if(null != menu)
				menus.add(menu);
			else
				return new ArrayList<>();
		}
		menus = filterMenus(menus, organizationId);
		return processWebMenus(menus.stream().map(r->{
			return ConvertHelper.convert(r, WebMenuDTO.class);
		}).collect(Collectors.toList()), ConvertHelper.convert(menu, WebMenuDTO.class)).getDtos();
	}

	private List<WebMenuDTO> listZuolinAdminWebMenu(Long userId, WebMenu menu, List<String> categories) {
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
	 * @param filterMap
     * @return
     */
	private List<WebMenu> filterMenus(List<WebMenu> menus, Map<Long, WebMenuScope> filterMap){
		List<WebMenu> filterMenus = new ArrayList<>();
		for (WebMenu menu: menus) {
			WebMenuScope scope = filterMap.get(menu.getId());
			if(null != scope){
				if(WebMenuScopeApplyPolicy.fromCode(scope.getApplyPolicy()) == WebMenuScopeApplyPolicy.OVERRIDE){
					//override menu
					menu.setName(scope.getMenuName());
					filterMenus.add(menu);
				}else if(WebMenuScopeApplyPolicy.fromCode(scope.getApplyPolicy()) == WebMenuScopeApplyPolicy.REVERT){
					filterMenus.add(menu);
				}

			}
		}
		filterMenus.sort((o1, o2) -> o1.getSortNum() - o2.getSortNum());
		return filterMenus;
	}

	/**
	 * 根据对应的机构过滤菜单
	 * @param menus
	 * @param organizationId
     * @return
     */
	private List<WebMenu> filterMenus(List<WebMenu> menus, Long organizationId){
		Map<Long, WebMenuScope> filterMap = webMenuProvider.getWebMenuScopeMapByOwnerId(EntityType.ORGANIZATIONS.getCode(), organizationId);
		if(filterMap.size() == 0 ){
			filterMap = webMenuProvider.getWebMenuScopeMapByOwnerId(EntityType.NAMESPACE.getCode(), Long.valueOf(UserContext.getCurrentNamespaceId()));
		}
		return filterMenus(menus, filterMap);
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
		dto.setDtos(dtos);
		
		return dto;
	}
}
