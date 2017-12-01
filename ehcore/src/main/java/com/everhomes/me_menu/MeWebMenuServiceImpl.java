package com.everhomes.me_menu;

import com.everhomes.acl.AuthorizationProvider;
import com.everhomes.acl.WebMenu;
import com.everhomes.acl.WebMenuPrivilegeProvider;
import com.everhomes.acl.WebMenuScope;
import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.db.AccessSpec;
import com.everhomes.db.DbProvider;
import com.everhomes.domain.Domain;
import com.everhomes.entity.EntityType;
import com.everhomes.menu.Target;
import com.everhomes.menu.WebMenuService;
import com.everhomes.module.ServiceModuleService;
import com.everhomes.organization.Organization;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.organization.OrganizationService;
import com.everhomes.rest.acl.WebMenuDTO;
import com.everhomes.rest.acl.WebMenuScopeApplyPolicy;
import com.everhomes.rest.acl.WebMenuType;
import com.everhomes.rest.acl.admin.ListWebMenuResponse;
import com.everhomes.rest.me_menu.ListMeWebMenusCommand;
import com.everhomes.rest.me_menu.ListMeWebMenusResponse;
import com.everhomes.rest.me_menu.MeWebMenuDTO;
import com.everhomes.rest.menu.ListUserRelatedWebMenusCommand;
import com.everhomes.rest.menu.WebMenuCategory;
import com.everhomes.rest.organization.OrganizationType;
import com.everhomes.server.schema.Tables;
import com.everhomes.server.schema.tables.daos.EhMeWebMenusDao;
import com.everhomes.server.schema.tables.pojos.EhMeWebMenus;
import com.everhomes.server.schema.tables.records.EhMeWebMenusRecord;
import com.everhomes.user.UserContext;
import com.everhomes.user.admin.SystemUserPrivilegeMgr;
import com.everhomes.util.ConvertHelper;
import org.jooq.DSLContext;
import org.jooq.SelectQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class MeWebMenuServiceImpl implements MeWebMenuService {

	private static final Logger LOGGER = LoggerFactory.getLogger(MeWebMenuServiceImpl.class);

	@Autowired
	private MeWebMenuProvider meWebMenuProvider;

	@Override
	public ListMeWebMenusResponse listMeWebMenus(ListMeWebMenusCommand cmd){
		ListMeWebMenusResponse response = new ListMeWebMenusResponse();
		List<MeWebMenu> meWebMenus = meWebMenuProvider.listMeWebMenus(cmd.getNamespaceId());
		//找不到就找默认0空间的
		if(meWebMenus == null || meWebMenus.size() == 0){
			meWebMenus = meWebMenuProvider.listMeWebMenus(0);
		}
		List<MeWebMenuDTO> dtos = new ArrayList<>();
		if(meWebMenus != null){
			dtos = meWebMenus.stream()
					.map(r -> ConvertHelper.convert(r, MeWebMenuDTO.class))
					.collect(Collectors.toList());
		}

		response.setDtos(dtos);
		return  response;
	}

}
