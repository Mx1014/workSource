package com.everhomes.menu;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.everhomes.acl.AuthorizationProvider;
import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.entity.EntityType;
import com.everhomes.organization.Organization;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.organization.OrganizationService;
import com.everhomes.rest.menu.ListUserRelatedWebMenusCommand;
import com.everhomes.rest.menu.WebMenuCategory;
import com.everhomes.rest.organization.OrganizationType;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.user.admin.SystemUserPrivilegeMgr;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.everhomes.acl.WebMenu;
import com.everhomes.acl.WebMenuPrivilegeProvider;
import com.everhomes.rest.acl.WebMenuDTO;
import com.everhomes.rest.acl.WebMenuType;
import com.everhomes.rest.acl.admin.ListWebMenuResponse;
import com.everhomes.util.ConvertHelper;

@Component
public class WebMenuServiceImpl implements WebMenuService {

	@Autowired
	private WebMenuPrivilegeProvider webMenuProvider;

	@Autowired
	private OrganizationProvider organizationProvider;

	@Autowired
	private OrganizationService organizationService;

	@Autowired
	private AuthorizationProvider authorizationProvider;

	@Override
	public List<WebMenuDTO> listUserRelatedWebMenus(ListUserRelatedWebMenusCommand cmd){
		UserContext userContext = UserContext.current();
		User user = userContext.getUser();
		List<String> categories = new ArrayList<>();
		WebMenu menu = null;
		if(null == cmd.getMenuId()){
			categories.add(WebMenuCategory.CLASSIFY.getCode());
			categories.add(WebMenuCategory.MODULE.getCode());
		}else{
			menu = webMenuProvider.getWebMenuById(cmd.getMenuId());
			categories.add(WebMenuCategory.PAGE.getCode());
		}

		if(EntityType.fromCode(UserContext.getCurrentPortalType()) == EntityType.ORGANIZATIONS){
			Long organizationId = UserContext.getCurrentPortalId();
			Organization organization = organizationProvider.findOrganizationById(organizationId);
			if(null != organization){
				if(OrganizationType.fromCode(organization.getOrganizationType()) == OrganizationType.PM){
					listPmWebMenu(user.getId(), menu, categories, organizationId);
				}else{
					listEnterpriseWebMenu(user.getId(), menu, categories, organizationId);
				}
			}


		}else /*if(EntityType.fromCode(UserContext.getCurrentSceneType()) == EntityType.ZUOLIN_ADMIN) */{
			 return listZuolinAdminWebMenu(user.getId(), menu, categories);
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
		if(resolver.checkSuperAdmin(userId, organizationId) || null != path){
			menus = webMenuProvider.listWebMenuByType(WebMenuType.PARK.getCode(), categories, path, null);
		}else{
			List<Long> orgIds = organizationService.getIncludeOrganizationIdsByUserId(userId, organizationId);
			for (Long orgId: orgIds) {
				targets.add(new Target(EntityType.ORGANIZATIONS.getCode(), orgId));
			}
			List<Long> moduleIds = authorizationProvider.getAuthorizationModuleIdsByTarget(targets);
			if(null != moduleIds && moduleIds.size() > 0)
				menus = webMenuProvider.listWebMenuByType(WebMenuType.PARK.getCode(), categories, null, moduleIds);
		}

		if(null == menus || menus.size() == 0){
			return new ArrayList<>();
		}

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
			return new ArrayList<>();
		}
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
			return new ArrayList<>();
		}
		return processWebMenus(menus.stream().map(r->{
			return ConvertHelper.convert(r, WebMenuDTO.class);
		}).collect(Collectors.toList()), ConvertHelper.convert(menu, WebMenuDTO.class)).getDtos();
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
