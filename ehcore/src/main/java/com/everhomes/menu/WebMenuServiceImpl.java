package com.everhomes.menu;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
	WebMenuPrivilegeProvider webMenuProvider;
	
	@Override
	public ListWebMenuResponse listZuolinAdminWebMenu() {
		List<WebMenu> menus = webMenuProvider.listWebMenuByType(WebMenuType.ZUOLIN.getCode());
		
		List<WebMenuDTO> menuDtos =  menus.stream().map(r->{
			return ConvertHelper.convert(r, WebMenuDTO.class);
		}).collect(Collectors.toList());
		
		ListWebMenuResponse res = new ListWebMenuResponse();
		res.setMenus(this.getWebMenu(menuDtos, null).getDtos());
		
		return res;
	}
	
    /**
     * 转换成菜单
     * @param menuDtos
     * @param dto
     * @param dto
     * @return
     */
	private WebMenuDTO getWebMenu(List<WebMenuDTO> menuDtos, WebMenuDTO dto){
		
		List<WebMenuDTO> dtos = new ArrayList<WebMenuDTO>();
		
		if(null == dto){
			dto = new WebMenuDTO();
			dto.setId(0l);
		}
		
		for (WebMenuDTO webMenuDTO : menuDtos) {
			if(dto.getId().equals(webMenuDTO.getParentId())){
				WebMenuDTO menuDto = this.getWebMenu(menuDtos, webMenuDTO);
				dtos.add(menuDto);
			}
		}
		dto.setDtos(dtos);
		
		return dto;
	}
}
