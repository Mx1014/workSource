package com.everhomes.menu;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.entity.EntityType;
import com.everhomes.organization.Organization;
import com.everhomes.organization.OrganizationProvider;
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

		if(EntityType.fromCode(userContext.getCurrentSceneType()) == EntityType.ORGANIZATIONS){
			Long organizationId = UserContext.getCurrentSceneId();
			Organization organization = organizationProvider.findOrganizationById(organizationId);
			if(null != organization){
				if(OrganizationType.fromCode(organization.getOrganizationType()) == OrganizationType.PM){
				}else{
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
		if(resolver.checkSuperAdmin(userId, organizationId)){
			menus = webMenuProvider.listWebMenuByType(WebMenuType.PARK.getCode(), categories, menu.getPath() + "/%", null);
		}else{

		}
		return null;
	}

	private List<WebMenuDTO> listZuolinAdminWebMenu(Long userId, WebMenu menu, List<String> categories) {
		String path = null;
		if(null != menu){
			path = menu.getPath() + "/%";
		}
		List<WebMenu> menus = webMenuProvider.listWebMenuByType(WebMenuType.ZUOLIN.getCode(), categories, path, null);
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
