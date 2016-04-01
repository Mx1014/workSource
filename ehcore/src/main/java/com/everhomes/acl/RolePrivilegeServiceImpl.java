package com.everhomes.acl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;













import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;
import org.springframework.util.StringUtils;













import com.everhomes.db.DbProvider;
import com.everhomes.entity.EntityType;
import com.everhomes.organization.Organization;
import com.everhomes.organization.OrganizationMember;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.organization.OrganizationRoleMap;
import com.everhomes.organization.OrganizationRoleMapProvider;
import com.everhomes.organization.OrganizationServiceImpl;
import com.everhomes.rest.acl.RoleConstants;
import com.everhomes.rest.acl.WebMenuDTO;
import com.everhomes.rest.acl.WebMenuPrivilegeDTO;
import com.everhomes.rest.acl.WebMenuPrivilegeShowFlag;
import com.everhomes.rest.acl.WebMenuType;
import com.everhomes.rest.acl.admin.CreateRolePrivilegeCommand;
import com.everhomes.rest.acl.admin.DeleteRolePrivilegeCommand;
import com.everhomes.rest.acl.admin.ListAclRolesCommand;
import com.everhomes.rest.acl.admin.ListWebMenuCommand;
import com.everhomes.rest.acl.admin.ListWebMenuPrivilegeCommand;
import com.everhomes.rest.acl.admin.ListWebMenuPrivilegeDTO;
import com.everhomes.rest.acl.admin.ListWebMenuResponse;
import com.everhomes.rest.acl.admin.QryRolePrivilegesCommand;
import com.everhomes.rest.acl.admin.RoleDTO;
import com.everhomes.rest.acl.admin.UpdateRolePrivilegeCommand;
import com.everhomes.rest.app.AppConstants;
import com.everhomes.rest.organization.OrganizationGroupType;
import com.everhomes.rest.organization.OrganizationRoleMapStatus;
import com.everhomes.rest.organization.OrganizationType;
import com.everhomes.rest.organization.PrivateFlag;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;

@Component
public class RolePrivilegeServiceImpl implements RolePrivilegeService {

	private static final Logger LOGGER = LoggerFactory.getLogger(OrganizationServiceImpl.class);
	
	@Autowired
	private DbProvider dbProvider;
	
	@Autowired
	private AclProvider aclProvider;
	
	@Autowired
	private WebMenuPrivilegeProvider webMenuPrivilegeProvider;
	
	@Autowired
	private OrganizationRoleMapProvider organizationRoleMapProvider;
	
	@Autowired
	private OrganizationProvider organizationProvider;
	
	
	@Override
	public ListWebMenuResponse ListWebMenu(ListWebMenuCommand cmd) {
		User user = UserContext.current().getUser();
		ListWebMenuResponse res = new ListWebMenuResponse();
		
		
		List<Long> privilegeIds = this.getUserPrivileges(null, cmd.getOrganizationId(), user.getId());
		List<WebMenuPrivilege> webMenuPrivileges = webMenuPrivilegeProvider.ListWebMenuByPrivilegeIds(privilegeIds, WebMenuPrivilegeShowFlag.MENU_SHOW);
		if(null == webMenuPrivileges){
			res.setMenus(new ArrayList<WebMenuDTO>());
			return res;
		}
		List<Long> menuIds = new ArrayList<Long>();
		for (WebMenuPrivilege webMenuPrivilege : webMenuPrivileges) {
			menuIds.add(webMenuPrivilege.getMenuId());
		}
		
		List<WebMenu> menus = webMenuPrivilegeProvider.ListWebMenuByMenuIds(this.getAllMenuIds(menuIds));
		
		if(null == menus){
			res.setMenus(new ArrayList<WebMenuDTO>());
			return res;
		}
		
		List<WebMenuDTO> menuDtos =  menus.stream().map(r->{
			return ConvertHelper.convert(r, WebMenuDTO.class);
		}).collect(Collectors.toList());
		
		res.setMenus(this.getWebMenu(menuDtos, null).getDtos());
		
		return res;
	}

	@Override
	public List<ListWebMenuPrivilegeDTO> ListWebMenuPrivilege(ListWebMenuPrivilegeCommand cmd) {
		User user = UserContext.current().getUser();
		List<Long> privilegeIds = this.getUserPrivileges(null, cmd.getOrganizationId(), user.getId());
		List<WebMenuPrivilege> webMenuPrivileges = webMenuPrivilegeProvider.ListWebMenuByPrivilegeIds(privilegeIds, null);
		return this.getListWebMenuPrivilege(webMenuPrivileges);
	}
	
	@Override
	public void createRolePrivilege(CreateRolePrivilegeCommand cmd) {
		User user = UserContext.current().getUser();
		Integer namespaceId = UserContext.getCurrentNamespaceId();
		dbProvider.execute((TransactionStatus status) -> {
			Timestamp time = new Timestamp(DateHelper.currentGMTTime().getTime());
			Role role = new Role();
			role.setAppId(AppConstants.APPID_PARK_ADMIN);
			role.setName(cmd.getRoleName());
			role.setDescription(cmd.getDescription());
			role.setNamespaceId(namespaceId);
			role.setOwnerType(EntityType.ORGANIZATIONS.getCode());
			role.setOwnerId(cmd.getOrganizationId());
			aclProvider.createRole(role);
			
			List<Long> privilegeIds = cmd.getPrivilegeIds();
			if(null != privilegeIds && 0 != privilegeIds.size()){
				Acl acl = new Acl();
				acl.setGrantType((byte) 1);
				acl.setOwnerType(EntityType.ORGANIZATIONS.getCode());
				acl.setOwnerId(cmd.getOrganizationId());
				acl.setOrderSeq(0);
				acl.setRoleId(role.getId());
				acl.setCreatorUid(user.getId());
				acl.setCreateTime(time);
				for (Long privilegeId : privilegeIds) {
					acl.setPrivilegeId(privilegeId);
					aclProvider.createAcl(acl);
				}
			}
			
			return null;
		});
		
	}
	
	@Override
	public void updateRolePrivilege(UpdateRolePrivilegeCommand cmd) {
		User user = UserContext.current().getUser();
		dbProvider.execute((TransactionStatus status) -> {
			Timestamp time = new Timestamp(DateHelper.currentGMTTime().getTime());
			Role role = aclProvider.getRoleById(cmd.getRoleId());
			role.setName(cmd.getRoleName());
			role.setDescription(cmd.getDescription());
//			aclProvider.
			
			List<Acl> acls = aclProvider.getResourceAclByRole(EntityType.ORGANIZATIONS.getCode(), cmd.getOrganizationId(), cmd.getRoleId());
			for (Acl acl : acls) {
				aclProvider.deleteAcl(acl.getId());
			}
			List<Long> privilegeIds = cmd.getPrivilegeIds();
			if(null != privilegeIds && 0 != privilegeIds.size()){
				Acl acl = new Acl();
				acl.setGrantType((byte) 1);
				acl.setOwnerType(EntityType.ORGANIZATIONS.getCode());
				acl.setOwnerId(cmd.getOrganizationId());
				acl.setOrderSeq(0);
				acl.setRoleId(cmd.getRoleId());
				acl.setCreatorUid(user.getId());
				acl.setCreateTime(time);
				for (Long privilegeId : privilegeIds) {
					acl.setPrivilegeId(privilegeId);
					aclProvider.createAcl(acl);
				}
			}
			
			return null;
		});
	}
	
	@Override
	public void deleteRolePrivilege(DeleteRolePrivilegeCommand cmd) {
		dbProvider.execute((TransactionStatus status) -> {
			Role role = new Role();
			role.setId(cmd.getRoleId());
			aclProvider.deleteRole(role);
			List<Acl> acls = aclProvider.getResourceAclByRole(EntityType.ORGANIZATIONS.getCode(), cmd.getOrganizationId(), cmd.getRoleId());
			for (Acl acl : acls) {
				aclProvider.deleteAcl(acl.getId());
			}
			return null;
		});
	}
	
	@Override
	public List<ListWebMenuPrivilegeDTO> qryRolePrivileges(
			QryRolePrivilegesCommand cmd) {
		
		List<Long> privilegeIds = new ArrayList<Long>();
		
		List<Acl> acls = aclProvider.getResourceAclByRole(EntityType.ORGANIZATIONS.getCode(), cmd.getOrganizationId(), cmd.getRoleId());
		
		if(null == acls){
			return new ArrayList<ListWebMenuPrivilegeDTO>();
		}
		for (Acl acl : acls) {
			privilegeIds.add(acl.getPrivilegeId());
		}
		
		List<WebMenuPrivilege> webMenuPrivileges = webMenuPrivilegeProvider.ListWebMenuByPrivilegeIds(privilegeIds, null);
		
		return this.getListWebMenuPrivilege(webMenuPrivileges);
	}
	
	@Override
	public List<RoleDTO> listAclRoleByOrganizationIds(ListAclRolesCommand cmd) {
		
		Organization org = organizationProvider.findOrganizationById(cmd.getOrganizationId());
		
		List<RoleDTO> dtos = new ArrayList<RoleDTO>();
		if(null == org){
			return dtos;
		}
		
		List<String> groupTypes = new ArrayList<String>();
		groupTypes.add(OrganizationGroupType.ENTERPRISE.getCode());
		groupTypes.add(OrganizationGroupType.GROUP.getCode());
	
//		List<Organization> orgs = organizationProvider.listOrganizationByGroupTypes(org.getPath() + "/%", groupTypes);
		
		List<Long> ownerIds = new ArrayList<Long>();
		ownerIds.add(org.getId());
//		if(null != orgs && 0 != orgs.size()){
//			for (Organization organization : orgs) {
//				ownerIds.add(organization.getId());
//			}
//		}
		PrivateFlag privateFlag = PrivateFlag.PUBLIC;
		
		if(OrganizationType.fromCode(org.getOrganizationType()) == OrganizationType.ENTERPRISE){
			privateFlag = PrivateFlag.PRIVATE;
		}
		
		List<OrganizationRoleMap> organizationRoleMaps = organizationRoleMapProvider.listOrganizationRoleMapsByOwnerIds(ownerIds, EntityType.ORGANIZATIONS, privateFlag);
		
		if(null == organizationRoleMaps){
			return dtos;
		}
		
		for (OrganizationRoleMap organizationRoleMap : organizationRoleMaps) {
			Role role = aclProvider.getRoleById(organizationRoleMap.getRoleId());
			if(null != role){
				dtos.add(ConvertHelper.convert(role, RoleDTO.class));
			}
			
		}
		
		return dtos;
	}
	
	 /**
     * 获取用户的权限列表
     * @param module
     * @param organizationId
     * @param userId
     * @return
     */
    public List<Long> getUserPrivileges(String module ,Long organizationId, Long userId){
    	Organization org = organizationProvider.findOrganizationById(organizationId);
    	
    	List<RoleAssignment> userRoles = aclProvider.getRoleAssignmentByResourceAndTarget(EntityType.ORGANIZATIONS.getCode(), organizationId, EntityType.USER.getCode(), userId);
    	
    	if(null == org){
    		return new ArrayList<Long>();
    	}
    	
    	if(OrganizationGroupType.fromCode(org.getGroupType()) == OrganizationGroupType.ENTERPRISE){
    		OrganizationMember member = organizationProvider.findOrganizationMemberByOrgIdAndUId(userId, org.getId());
    		if(null != member && null != member.getGroupId() && 0 != member.getGroupId()){
    			organizationId = member.getGroupId();
    		}
    	}
    	
    	List<RoleAssignment> userOrgRoles = aclProvider.getRoleAssignmentByResourceAndTarget(EntityType.ORGANIZATIONS.getCode(), organizationId, EntityType.ORGANIZATIONS.getCode(), organizationId);
    	
    	userRoles.addAll(userOrgRoles);
    	List<Long> privileges = new ArrayList<Long>();
    	
    	List<Long> roleIds = new ArrayList<Long>();
    	for (RoleAssignment role : userRoles) {
    		roleIds.add(role.getRoleId());
		}
    	
    	if(!StringUtils.isEmpty(module)){
    		List<Privilege> s = aclProvider.getPrivilegesByTag(module); //aclProvider 调平台根据角色list+模块 获取权限list接口
    		if(null == s){
    			return privileges;
    		}
    		List<Long> privilegeIds = aclProvider.getRolesFromResourceAssignments(EntityType.ORGANIZATIONS.getCode(), organizationId, roleIds);
    		
    		for (Privilege privilege : s) {
				if(privilegeIds.contains(privilege.getId())){
					privileges.add(privilege.getId());
				}
			}
    		
    		return privileges;
    		
    	}else{
//    		for (Long roleId : roleIds) {
//    			List<Acl> acls = aclProvider.getResourceAclByRole(EntityType.ORGANIZATIONS.getCode(), organizationId, roleId);
//    			for (Acl acl : acls) {
//    				if(!privileges.contains(acl.getPrivilegeId())){
//    					privileges.add(acl.getPrivilegeId());
//    				}
//				}
//    			
//			}
    	}
    	
    	return aclProvider.getRolesFromResourceAssignments(EntityType.ORGANIZATIONS.getCode(), organizationId, roleIds);
    }
    
    /**
     * 获取菜单 map
     * @return
     */
    private Map<Long, WebMenu> getWebMenuMap(String type){
    	Map<Long, WebMenu> menuMap = new HashMap<Long, WebMenu>();
    	List<WebMenu> menus = webMenuPrivilegeProvider.listWebMenuByType(type);
    	for (WebMenu webMenu : menus) {
    		menuMap.put(webMenu.getId(), webMenu);
		}
    	return menuMap;
    }

    /**
     * 把全部父级的menuId添加进去
     * @param Ids
     * @return
     */
    private List<Long> getAllMenuIds(List<Long> Ids){
    	Map<Long, WebMenu> menuMap = this.getWebMenuMap(WebMenuType.PARK.getCode());
    	
    	List<Long> addIds = new ArrayList<Long>();
    	for (Long id : Ids) {
    		WebMenu menu= menuMap.get(id);
    		if(null != menu){
    			String[] idStrs = menu.getPath().split("/");
    			if(idStrs.length > 2){
    				for (String idStr : idStrs) {
						if(!StringUtils.isEmpty(idStr)){
							Long menuId = Long.valueOf(idStr);
							if(!addIds.contains(menuId)){
								addIds.add(menuId);
							}
						}
					}
    			}
    		}
		}
    	
    	addIds.addAll(Ids);
    	return addIds;
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
	
	
	/**
	 * 转换模块的权限集合
	 * @param webMenuPrivileges
	 * @return
	 */
	private List<ListWebMenuPrivilegeDTO> getListWebMenuPrivilege(List<WebMenuPrivilege> webMenuPrivileges){
		
		List<ListWebMenuPrivilegeDTO> dtos = new ArrayList<ListWebMenuPrivilegeDTO>();
		
		Map<Long, List<WebMenuPrivilegeDTO>> dtosMap = new HashMap<Long, List<WebMenuPrivilegeDTO>>();
		
		
		for (WebMenuPrivilege r : webMenuPrivileges) {
			WebMenuPrivilegeDTO webMenuPrivilegeDTO = ConvertHelper.convert(r, WebMenuPrivilegeDTO.class);
			if(null == dtosMap.get(r.getMenuId())){
				List<WebMenuPrivilegeDTO> webMenuPrivilegeDTOs= new ArrayList<WebMenuPrivilegeDTO>();
				webMenuPrivilegeDTOs.add(webMenuPrivilegeDTO);
				dtosMap.put(r.getMenuId(), webMenuPrivilegeDTOs);
			}else{
				dtosMap.get(r.getMenuId()).add(webMenuPrivilegeDTO);
			}
		}
		
		Map<Long, WebMenu> menuMap = this.getWebMenuMap(WebMenuType.PARK.getCode());
		
		for (Long menuId : dtosMap.keySet()) {
			ListWebMenuPrivilegeDTO dto = new ListWebMenuPrivilegeDTO();
			WebMenu menu = menuMap.get(menuId);
			dto.setModuleId(menu.getId());
			dto.setModuleName(menu.getName());
			dto.setDtos(dtosMap.get(menuId));
			dtos.add(dto);
		}
		
		return dtos;
	}

	
}
