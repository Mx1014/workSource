package com.everhomes.acl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;






































import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;
import org.springframework.util.StringUtils;





































import org.springframework.web.multipart.MultipartFile;

import com.everhomes.db.DbProvider;
import com.everhomes.entity.EntityType;
import com.everhomes.organization.Organization;
import com.everhomes.organization.OrganizationMember;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.organization.OrganizationRoleMap;
import com.everhomes.organization.OrganizationRoleMapProvider;
import com.everhomes.organization.OrganizationService;
import com.everhomes.organization.OrganizationServiceImpl;
import com.everhomes.rest.acl.RoleConstants;
import com.everhomes.rest.acl.WebMenuDTO;
import com.everhomes.rest.acl.WebMenuPrivilegeDTO;
import com.everhomes.rest.acl.WebMenuPrivilegeShowFlag;
import com.everhomes.rest.acl.WebMenuScopeApplyPolicy;
import com.everhomes.rest.acl.WebMenuType;
import com.everhomes.rest.acl.admin.AddAclRoleAssignmentCommand;
import com.everhomes.rest.acl.admin.CreateOrganizationAdminCommand;
import com.everhomes.rest.acl.admin.CreateRolePrivilegeCommand;
import com.everhomes.rest.acl.admin.DeleteAclRoleAssignmentCommand;
import com.everhomes.rest.acl.admin.DeleteOrganizationAdminCommand;
import com.everhomes.rest.acl.admin.DeleteRolePrivilegeCommand;
import com.everhomes.rest.acl.admin.ExcelRoleExcelRoleAssignmentPersonnelCommand;
import com.everhomes.rest.acl.admin.ListAclRolesCommand;
import com.everhomes.rest.acl.admin.ListWebMenuCommand;
import com.everhomes.rest.acl.admin.ListWebMenuPrivilegeCommand;
import com.everhomes.rest.acl.admin.ListWebMenuPrivilegeDTO;
import com.everhomes.rest.acl.admin.ListWebMenuResponse;
import com.everhomes.rest.acl.admin.QryRolePrivilegesCommand;
import com.everhomes.rest.acl.admin.RoleDTO;
import com.everhomes.rest.acl.admin.BatchAddTargetRoleCommand;
import com.everhomes.rest.acl.admin.UpdateOrganizationAdminCommand;
import com.everhomes.rest.acl.admin.UpdateRolePrivilegeCommand;
import com.everhomes.rest.app.AppConstants;
import com.everhomes.rest.organization.CreateOrganizationAccountCommand;
import com.everhomes.rest.organization.ListOrganizationAdministratorCommand;
import com.everhomes.rest.organization.ListOrganizationMemberCommandResponse;
import com.everhomes.rest.organization.ListOrganizationPersonnelByRoleIdsCommand;
import com.everhomes.rest.organization.OrganizationGroupType;
import com.everhomes.rest.organization.OrganizationMemberDTO;
import com.everhomes.rest.organization.OrganizationServiceErrorCode;
import com.everhomes.rest.organization.OrganizationType;
import com.everhomes.rest.organization.SetAclRoleAssignmentCommand;
import com.everhomes.rest.user.admin.ImportDataResponse;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.StringHelper;

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
	
	@Autowired
	private OrganizationService organizationService;
	
	
	@Override
	public ListWebMenuResponse listWebMenu(ListWebMenuCommand cmd) {
		User user = UserContext.current().getUser();
		
		
		Integer namespaceId = UserContext.getCurrentNamespaceId();
		
		ListWebMenuResponse res = new ListWebMenuResponse();
		
		//获取用户在机构范围内的所有权限
		List<Long> privilegeIds = this.getUserPrivileges(null, cmd.getOrganizationId(), user.getId());
		
		if(null == privilegeIds){
			res.setMenus(new ArrayList<WebMenuDTO>());
			return res;
		}
		//根据权限获取所有菜单
		List<WebMenuPrivilege> webMenuPrivileges = webMenuPrivilegeProvider.listWebMenuByPrivilegeIds(privilegeIds, WebMenuPrivilegeShowFlag.MENU_SHOW);
		
		List<Long> menuIds = new ArrayList<Long>();
		for (WebMenuPrivilege webMenuPrivilege : webMenuPrivileges) {
			menuIds.add(webMenuPrivilege.getMenuId());
		}
		
		List<WebMenu> menus = webMenuPrivilegeProvider.listWebMenuByMenuIds(this.getAllMenuIds(menuIds));
		
		//根据机构获取全部要屏蔽的菜单
		List<WebMenuScope> orgWebMenuScopes = webMenuPrivilegeProvider.listWebMenuScopeByOwnerId(EntityType.ORGANIZATIONS.getCode(), Long.valueOf(cmd.getOrganizationId()));
		
		//根据域获取全部要屏蔽的菜单
		List<WebMenuScope> webMenuScopes = webMenuPrivilegeProvider.listWebMenuScopeByOwnerId(EntityType.NAMESPACE.getCode(), Long.valueOf(namespaceId));
		
		//去掉机构要屏蔽的菜单
		if(null != orgWebMenuScopes && orgWebMenuScopes.size() > 0){
			menus = this.handleMenus(menus, orgWebMenuScopes);
		}
		
		//去掉域要屏蔽的菜单
		if(null != webMenuScopes && webMenuScopes.size() > 0)
			menus = this.handleMenus(menus, webMenuScopes);
		
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
	public List<ListWebMenuPrivilegeDTO> listWebMenuPrivilege(ListWebMenuPrivilegeCommand cmd) {
		User user = UserContext.current().getUser();
		Integer namespaceId = UserContext.getCurrentNamespaceId();
		List<Long> privilegeIds = this.getUserPrivileges(null, cmd.getOrganizationId(), user.getId());
		List<WebMenuPrivilege> webMenuPrivileges = webMenuPrivilegeProvider.listWebMenuByPrivilegeIds(privilegeIds, null);
		
		//根据机构获取全部要屏蔽的菜单权限
		List<WebMenuScope> orgWebMenuScopes = webMenuPrivilegeProvider.listWebMenuScopeByOwnerId(EntityType.ORGANIZATIONS.getCode(), Long.valueOf(cmd.getOrganizationId()));
		
		//屏蔽域下面的菜单权限
		List<WebMenuScope> webMenuScopes = webMenuPrivilegeProvider.listWebMenuScopeByOwnerId(EntityType.NAMESPACE.getCode(), Long.valueOf(namespaceId));
		
		if(null != webMenuScopes){
			webMenuScopes.addAll(orgWebMenuScopes);
		}else{
			webMenuScopes = orgWebMenuScopes;
		}
		
		return this.getListWebMenuPrivilege(webMenuPrivileges, webMenuScopes);
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
			aclProvider.updateRole(role);
			
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
			
			aclProvider.deleteRole(cmd.getRoleId());
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
		
		List<WebMenuPrivilege> webMenuPrivileges = webMenuPrivilegeProvider.listWebMenuByPrivilegeIds(privilegeIds, null);
		
		return this.getListWebMenuPrivilege(webMenuPrivileges, null);
	}
	
	
	
	
	@Override
	public List<RoleDTO> listAclRoleByOrganizationId(ListAclRolesCommand cmd) {
		
		Organization org = organizationProvider.findOrganizationById(cmd.getOrganizationId());
		
		Integer namespaceId = UserContext.getCurrentNamespaceId();
		
		List<RoleDTO> dtos = new ArrayList<RoleDTO>();
		if(null == org){
			return dtos;
		}
		
//		List<String> groupTypes = new ArrayList<String>();
//		groupTypes.add(OrganizationGroupType.ENTERPRISE.getCode());
//		groupTypes.add(OrganizationGroupType.GROUP.getCode());
	
//		List<Organization> orgs = organizationProvider.listOrganizationByGroupTypes(org.getPath() + "/%", groupTypes);
		
//		List<Long> ownerIds = new ArrayList<Long>();
//		ownerIds.add(org.getId());
//		if(null != orgs && 0 != orgs.size()){
//			for (Organization organization : orgs) {
//				ownerIds.add(organization.getId());
//			}
//		}
		
		List<Role> roles = aclProvider.getRolesByOwner(namespaceId, cmd.getAppId(), EntityType.ORGANIZATIONS.getCode(), cmd.getOrganizationId());
		
		dtos = roles.stream().map(r->{
			return ConvertHelper.convert(r, RoleDTO.class);
		}).collect(Collectors.toList());
		
		return dtos;
	}
	
	@Override
	public void createOrganizationSuperAdmin(CreateOrganizationAdminCommand cmd){
		Organization org = organizationProvider.findOrganizationById(cmd.getOrganizationId());
		Long roleId = RoleConstants.PM_SUPER_ADMIN;
		if(OrganizationType.fromCode(org.getOrganizationType()) == OrganizationType.ENTERPRISE){
			roleId = RoleConstants.ENTERPRISE_SUPER_ADMIN;
		}
		
		CreateOrganizationAccountCommand command = new CreateOrganizationAccountCommand();
		command.setOrganizationId(org.getId());
		command.setAccountName(cmd.getContactName());
		command.setAccountPhone(cmd.getContactToken());
		organizationService.createOrganizationAccount(command, roleId);
	}
	
	@Override
	public void createOrganizationOrdinaryAdmin(CreateOrganizationAdminCommand cmd){
		Organization org = organizationProvider.findOrganizationById(cmd.getOrganizationId());
		
		Long roleId = RoleConstants.PM_ORDINARY_ADMIN;
		if(OrganizationType.fromCode(org.getOrganizationType()) == OrganizationType.ENTERPRISE){
			roleId = RoleConstants.ENTERPRISE_ORDINARY_ADMIN;
		}
		
		CreateOrganizationAccountCommand command = new CreateOrganizationAccountCommand();
		command.setOrganizationId(org.getId());
		command.setAccountName(cmd.getContactName());
		command.setAccountPhone(cmd.getContactToken());
		organizationService.createOrganizationAccount(command, roleId);
	}
	
	@Override
	public void updateOrganizationOrdinaryAdmin(
			UpdateOrganizationAdminCommand cmd) {
		
		Organization org = organizationProvider.findOrganizationById(cmd.getOrganizationId());
		
		Long ordinaryAdminRoleId = RoleConstants.PM_ORDINARY_ADMIN;
		Long superAdminRoleId = RoleConstants.PM_SUPER_ADMIN;
		if(OrganizationType.fromCode(org.getOrganizationType()) == OrganizationType.ENTERPRISE){
			ordinaryAdminRoleId = RoleConstants.ENTERPRISE_ORDINARY_ADMIN;
			superAdminRoleId = RoleConstants.ENTERPRISE_SUPER_ADMIN;
		}
		
		OrganizationMember member = organizationProvider.findOrganizationMemberByOrgIdAndUId(cmd.getUserId(), cmd.getOrganizationId());
		if(null != member){
			member.setContactName(cmd.getContactName());
			member.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
			organizationProvider.updateOrganizationMember(member);
		}
		
		List<RoleAssignment> roleAssignments = aclProvider.getRoleAssignmentByResourceAndTarget(EntityType.ORGANIZATIONS.getCode(), cmd.getOrganizationId(), EntityType.USER.getCode(), cmd.getUserId());
		
		boolean isAdminFlag = false;
		
		for (RoleAssignment roleAssignment : roleAssignments) {
			if(roleAssignment.getRoleId().equals(superAdminRoleId)){
				aclProvider.deleteRoleAssignment(roleAssignment.getId());
			}else if(roleAssignment.getRoleId().equals(ordinaryAdminRoleId)){
				isAdminFlag = true;
			}
		}
		
		if(!isAdminFlag){
			SetAclRoleAssignmentCommand command = new SetAclRoleAssignmentCommand();
			command.setOrganizationId(cmd.getOrganizationId());
			command.setRoleId(ordinaryAdminRoleId);
			command.setTargetId(cmd.getUserId());
			organizationService.setAclRoleAssignmentRole(command, EntityType.USER);
		}
		
	}
	
	@Override
	public void updateOrganizationSuperAdmin(UpdateOrganizationAdminCommand cmd) {
		Organization org = organizationProvider.findOrganizationById(cmd.getOrganizationId());
		
		Long ordinaryAdminRoleId = RoleConstants.PM_ORDINARY_ADMIN;
		Long superAdminRoleId = RoleConstants.PM_SUPER_ADMIN;
		if(OrganizationType.fromCode(org.getOrganizationType()) == OrganizationType.ENTERPRISE){
			ordinaryAdminRoleId = RoleConstants.ENTERPRISE_ORDINARY_ADMIN;
			superAdminRoleId = RoleConstants.ENTERPRISE_SUPER_ADMIN;
		}
		
		OrganizationMember member = organizationProvider.findOrganizationMemberByOrgIdAndUId(cmd.getUserId(), cmd.getOrganizationId());
		if(null != member){
			member.setContactName(cmd.getContactName());
			member.setUpdateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
			organizationProvider.updateOrganizationMember(member);
		}
		
		List<RoleAssignment> roleAssignments = aclProvider.getRoleAssignmentByResourceAndTarget(EntityType.ORGANIZATIONS.getCode(), cmd.getOrganizationId(), EntityType.USER.getCode(), cmd.getUserId());
		
		boolean isAdminFlag = false;
		
		for (RoleAssignment roleAssignment : roleAssignments) {
			if(roleAssignment.getRoleId().equals(ordinaryAdminRoleId)){
				aclProvider.deleteRoleAssignment(roleAssignment.getId());
			}else if(roleAssignment.getRoleId().equals(superAdminRoleId)){
				isAdminFlag = true;
			}
		}
		
		if(!isAdminFlag){
			SetAclRoleAssignmentCommand command = new SetAclRoleAssignmentCommand();
			command.setOrganizationId(cmd.getOrganizationId());
			command.setRoleId(superAdminRoleId);
			command.setTargetId(cmd.getUserId());
			organizationService.setAclRoleAssignmentRole(command, EntityType.USER);
		}
	}
	
	@Override
	public void deleteOrganizationAdmin(DeleteOrganizationAdminCommand cmd) {
		Organization org = organizationProvider.findOrganizationById(cmd.getOrganizationId());
		
		List<Long> roles = new ArrayList<Long>();
		if(OrganizationType.fromCode(org.getOrganizationType()) == OrganizationType.ENTERPRISE){
			roles.add(RoleConstants.ENTERPRISE_ORDINARY_ADMIN);
			roles.add(RoleConstants.ENTERPRISE_SUPER_ADMIN);
		}else{
			roles.add(RoleConstants.PM_ORDINARY_ADMIN);
			roles.add(RoleConstants.PM_SUPER_ADMIN);
		}
		
		ListOrganizationPersonnelByRoleIdsCommand command = new ListOrganizationPersonnelByRoleIdsCommand();
		command.setOrganizationId(cmd.getOrganizationId());
		command.setRoleIds(roles);
		
		ListOrganizationMemberCommandResponse res =  organizationService.listOrganizationPersonnelsByRoleIds(command);
		
		if(1 == res.getMembers().size()){
			LOGGER.error("Keep at least one administrator, organizationId = {}", cmd.getOrganizationId());
			throw RuntimeErrorException.errorWith(OrganizationServiceErrorCode.SCOPE, OrganizationServiceErrorCode.ERROR_CONNOT_DELETE_ADMIN,
					"Keep at least one administrator.");
		}
		
		List<RoleAssignment> roleAssignments = aclProvider.getRoleAssignmentByResourceAndTarget(EntityType.ORGANIZATIONS.getCode(), cmd.getOrganizationId(), EntityType.USER.getCode(), cmd.getUserId());
		
		/**
		 * 只删除admin这个角色权限
		 */
		for (RoleAssignment roleAssignment : roleAssignments) {
			if(roles.contains(roleAssignment.getRoleId())){
				aclProvider.deleteRoleAssignment(roleAssignment.getId());
			}
		}
		
	}
	
	
	@Override
	public ListOrganizationMemberCommandResponse listOrganizationAdministrators(ListOrganizationAdministratorCommand cmd){
		Organization org = organizationProvider.findOrganizationById(cmd.getOrganizationId());
		List<Long> roles = new ArrayList<Long>();
		if(OrganizationType.fromCode(org.getOrganizationType()) == OrganizationType.ENTERPRISE){
			roles.add(RoleConstants.ENTERPRISE_ORDINARY_ADMIN);
			roles.add(RoleConstants.ENTERPRISE_SUPER_ADMIN);
		}else{
			roles.add(RoleConstants.PM_ORDINARY_ADMIN);
			roles.add(RoleConstants.PM_SUPER_ADMIN);
		}
		
		cmd.setRoleIds(roles);
		
		return organizationService.listOrganizationPersonnelsByRoleIds(ConvertHelper.convert(cmd, ListOrganizationPersonnelByRoleIdsCommand.class));
	}
	
	@Override
	public void deleteAclRoleAssignment(DeleteAclRoleAssignmentCommand cmd) {
		if(null == EntityType.fromCode(cmd.getTargetType()) || null == cmd.getRoleId()){
			LOGGER.error("delete acl role assignment error, cmd = {}", cmd);
			throw RuntimeErrorException.errorWith(OrganizationServiceErrorCode.SCOPE, OrganizationServiceErrorCode.ERROR_INVALID_PARAMETER,
					"delete acl role assignment error.");
		}
		
		List<RoleAssignment> roleAssignments = aclProvider.getRoleAssignmentByResourceAndTarget(EntityType.ORGANIZATIONS.getCode(), cmd.getOrganizationId(), cmd.getTargetType(), cmd.getTargetId());
		
		if(null != roleAssignments && 0 < roleAssignments.size()){
			for (RoleAssignment assignment : roleAssignments) {
				if(assignment.getRoleId().equals(cmd.getRoleId())){
					aclProvider.deleteRoleAssignment(assignment.getId());
				}
			}
		}
	}
	
	@Override
	public void addAclRoleAssignment(AddAclRoleAssignmentCommand cmd) {
		if(null == EntityType.fromCode(cmd.getTargetType()) || null == cmd.getRoleId()){
			LOGGER.error("add acl role assignment error, cmd = {}", cmd);
			throw RuntimeErrorException.errorWith(OrganizationServiceErrorCode.SCOPE, OrganizationServiceErrorCode.ERROR_INVALID_PARAMETER,
					"add acl role assignment error.");
		}
		
		RoleAssignment roleAssignment = new RoleAssignment();
		List<RoleAssignment> roleAssignments = aclProvider.getRoleAssignmentByResourceAndTarget(EntityType.ORGANIZATIONS.getCode(), cmd.getOrganizationId(), cmd.getTargetType(), cmd.getTargetId());
		
		if(null != roleAssignments && 0 < roleAssignments.size()){
			for (RoleAssignment assignment : roleAssignments) {
				if(assignment.getRoleId().equals(cmd.getRoleId())){
					LOGGER.error("role personnel already exist, cmd = {}", cmd);
					throw RuntimeErrorException.errorWith(OrganizationServiceErrorCode.SCOPE, OrganizationServiceErrorCode.ERROR_ASSIGNMENT_EXISTS,
							"role personnel already exist.");
				}
			}
		}
		
		dbProvider.execute((TransactionStatus status) -> {
			roleAssignment.setRoleId(cmd.getRoleId());
			roleAssignment.setOwnerType(EntityType.ORGANIZATIONS.getCode());
			roleAssignment.setOwnerId(cmd.getOrganizationId());
			roleAssignment.setTargetType(cmd.getTargetType());
			roleAssignment.setTargetId(cmd.getTargetId());
			roleAssignment.setCreatorUid(UserContext.current().getUser().getId());
			aclProvider.createRoleAssignment(roleAssignment);
			return null;
		});
	}
	
	
	@Override
	public void batchAddTargetRoles(BatchAddTargetRoleCommand cmd) {
		if(null == EntityType.fromCode(cmd.getTargetType()) || null == cmd.getRoleIds() || 0 == cmd.getRoleIds().size()){
			LOGGER.error("set target role error, cmd = {}", cmd);
			throw RuntimeErrorException.errorWith(OrganizationServiceErrorCode.SCOPE, OrganizationServiceErrorCode.ERROR_INVALID_PARAMETER,
					"set target role error.");
		}
		
		RoleAssignment roleAssignment = new RoleAssignment();
		List<RoleAssignment> roleAssignments = aclProvider.getRoleAssignmentByResourceAndTarget(EntityType.ORGANIZATIONS.getCode(), cmd.getOrganizationId(), cmd.getTargetType(), cmd.getTargetId());
		
		List<Long> roleIds = cmd.getRoleIds();
		dbProvider.execute((TransactionStatus status) -> {

			for (Long roleId : roleIds) {
				boolean flag = true;
				if(null != roleAssignments && 0 < roleAssignments.size()){
					for (RoleAssignment assignment : roleAssignments) {
						if(assignment.getRoleId().equals(roleId)){
							flag = false;
							break;
						}
					}
				}
				
				if(flag){
					roleAssignment.setRoleId(roleId);
					roleAssignment.setOwnerType(EntityType.ORGANIZATIONS.getCode());
					roleAssignment.setOwnerId(cmd.getOrganizationId());
					roleAssignment.setTargetType(cmd.getTargetType());
					roleAssignment.setTargetId(cmd.getTargetId());
					roleAssignment.setCreatorUid(UserContext.current().getUser().getId());
					aclProvider.createRoleAssignment(roleAssignment);
				}
				
			}
			
			return null;
		});
	}
	
	
	 /**
     * 获取用户的权限列表
     * @param module
     * @param organizationId
     * @param userId
     * @return
     */
    public List<Long> getUserPrivileges(String module ,Long organizationId, Long userId){
    	
    	List<RoleAssignment> userRoles = this.getUserAllOrgRoles(organizationId, userId);
    	
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
    		
    		List<Long> privilegeIds = new ArrayList<Long>();
    		for (Long roleId : roleIds) {
    			List<Acl> acls = null;
    			if(RoleConstants.PLATFORM_PM_ROLES.contains(roleId) || RoleConstants.PLATFORM_ENTERPRISE_ROLES.contains(roleId)){
    				acls = aclProvider.getResourceAclByRole(EntityType.ORGANIZATIONS.getCode(), null, roleId);
    			}else{
    				Role role = aclProvider.getRoleById(roleId);
    				if(null != role){
    					acls = aclProvider.getResourceAclByRole(role.getOwnerType(), role.getOwnerId(), roleId);
    				}
    				LOGGER.debug("user["+userId+"], role = " + StringHelper.toJsonString(role));
    			}
    			for (Acl acl : acls) {
    				privilegeIds.add(acl.getPrivilegeId());
				}
    			
			}
    		
    		for (Privilege privilege : s) {
				if(privilegeIds.contains(privilege.getId())){
					privileges.add(privilege.getId());
				}
			}
    		
    	}else{
    		List<Long> privilegeIds = new ArrayList<Long>();
    		for (Long roleId : roleIds) {
    			List<Acl> acls = null;
    			if(RoleConstants.PLATFORM_PM_ROLES.contains(roleId) || RoleConstants.PLATFORM_ENTERPRISE_ROLES.contains(roleId)){
    				acls = aclProvider.getResourceAclByRole(EntityType.ORGANIZATIONS.getCode(), null, roleId);
    			}else{
    				Role role = aclProvider.getRoleById(roleId);
    				if(null != role){
    					acls = aclProvider.getResourceAclByRole(role.getOwnerType(), role.getOwnerId(), roleId);
    				}
    				LOGGER.debug("user["+userId+"], role = " + StringHelper.toJsonString(role));
    			}
    			for (Acl acl : acls) {
    				privilegeIds.add(acl.getPrivilegeId());
				}
    			
			}
    		privileges = privilegeIds;
    	}
    	
    	return privileges;
    }
    
    
    @Override
    public boolean checkAdministrators(Long organizationId) {
    	User user = UserContext.current().getUser();
    	
    	Organization org = organizationProvider.findOrganizationById(organizationId);
    	
    	if(null == org){
    		return false;
    	}
    	
    	List<RoleAssignment> userRoles = this.getUserRoles(organizationId, user.getId());
    	
    	List<Long> roleIds = new ArrayList<Long>();
    	for (RoleAssignment role : userRoles) {
    		roleIds.add(role.getRoleId());
		}
    	
    	if(OrganizationType.fromCode(org.getOrganizationType()) == OrganizationType.ENTERPRISE){
    		if(roleIds.contains(RoleConstants.ENTERPRISE_SUPER_ADMIN) || roleIds.contains(RoleConstants.ENTERPRISE_ORDINARY_ADMIN)){
    			return true;
    		}
    	}else{
    		if(roleIds.contains(RoleConstants.PM_SUPER_ADMIN) || roleIds.contains(RoleConstants.PM_ORDINARY_ADMIN)){
    			return true;
    		}
    	}
    	
    	return false;
    }
    
    @Override
    public boolean checkAuthority(String ownerType, Long ownerId, Long privilegeId){
    	User user = UserContext.current().getUser();
    	
    	List<Long> privileges = this.getUserPrivileges(null, ownerId, user.getId());
    	
    	if(!privileges.contains(privilegeId)){
    		this.returnNoPrivileged(privileges, user);
    	}
    	
    	return true;
    }
    
    @Override
    public void exportRoleAssignmentPersonnelXls(
    		ExcelRoleExcelRoleAssignmentPersonnelCommand cmd,
    		HttpServletResponse response) {
    	
    	Long organizationId = cmd.getOrganizationId();
    	
    	Long roleId = cmd.getRoleId();
    	List<Long> roleIds = new ArrayList<Long>();
    	ListOrganizationPersonnelByRoleIdsCommand command = new ListOrganizationPersonnelByRoleIdsCommand();
    	command.setKeywords(cmd.getKeywords());
    	command.setOrganizationId(organizationId);
    	roleIds.add(roleId);
    	command.setRoleIds(roleIds);
    	ListOrganizationMemberCommandResponse res = organizationService.listOrganizationPersonnelsByRoleIds(command);
    	List<OrganizationMemberDTO> members = res.getMembers();
    	
    }
    
    @Override
    public ImportDataResponse importRoleAssignmentPersonnelXls(
    		ExcelRoleExcelRoleAssignmentPersonnelCommand cmd,
    		MultipartFile[] files) {
    	
    	
    	return null;
    }
    
    private List<RoleAssignment> getUserAllOrgRoles(Long organizationId, Long userId){
    	List<String> groupTypes = new ArrayList<String>();
    	groupTypes.add(OrganizationGroupType.ENTERPRISE.getCode());
    	groupTypes.add(OrganizationGroupType.GROUP.getCode());
    	
    	Organization org = organizationProvider.findOrganizationById(organizationId);
    	
    	String path = org.getPath();
    	
    	String[] orgIds = path.split("/");
    	
    	List<RoleAssignment> userRoles = null;
    	for (String orgId : orgIds) {
    		
    		if(StringUtils.isEmpty(orgId)){
    			continue;
    		}
    		
    		if(null == userRoles){
    			userRoles = this.getUserRoles(Long.parseLong(orgId), userId);
    		}else{
    			userRoles.addAll(this.getUserRoles(Long.parseLong(orgId), userId));
    		}
		}
    	
    	return userRoles;
    }
    
    private List<RoleAssignment> getUserRoles(Long organizationId, Long userId){
    	
    	Organization org = organizationProvider.findOrganizationById(organizationId);
    	
    	List<RoleAssignment> userRoles = aclProvider.getRoleAssignmentByResourceAndTarget(EntityType.ORGANIZATIONS.getCode(), organizationId, EntityType.USER.getCode(), userId);
    	
    	LOGGER.debug("organization [ " + organizationId + " ],user[" + userId +  "] roles = " + StringHelper.toJsonString(userRoles));
    	
    	if(null == org){
    		return new ArrayList<RoleAssignment>();
    	}
    	
    	Long childrenOrgId = organizationId;
    	if(OrganizationGroupType.fromCode(org.getGroupType()) == OrganizationGroupType.ENTERPRISE){
    		OrganizationMember member = organizationProvider.findOrganizationMemberByOrgIdAndUId(userId, org.getId());
    		if(null != member && null != member.getGroupId() && 0 != member.getGroupId()){
    			childrenOrgId = member.getGroupId();
    		}
    	}else if(OrganizationGroupType.fromCode(org.getGroupType()) == OrganizationGroupType.GROUP){
    		childrenOrgId = org.getId();
    		organizationId = org.getDirectlyEnterpriseId();
    	}
    	
    	List<RoleAssignment> userOrgRoles = aclProvider.getRoleAssignmentByResourceAndTarget(EntityType.ORGANIZATIONS.getCode(), organizationId, EntityType.ORGANIZATIONS.getCode(), childrenOrgId);
    	
    	LOGGER.debug("organization [ " + organizationId + " ],user[" + userId +  "] organization roles = " + StringHelper.toJsonString(userOrgRoles));
    	
    	userRoles.addAll(userOrgRoles);
    	
    	return userRoles;
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
	private List<ListWebMenuPrivilegeDTO> getListWebMenuPrivilege(List<WebMenuPrivilege> webMenuPrivileges, List<WebMenuScope> webMenuScopes){
		
		List<ListWebMenuPrivilegeDTO> dtos = new ArrayList<ListWebMenuPrivilegeDTO>();
		
		Map<Long, List<WebMenuPrivilegeDTO>> dtosMap = new LinkedHashMap<Long, List<WebMenuPrivilegeDTO>>();
		
		
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
		
		if(null != webMenuScopes){
			for (WebMenuScope webMenuScope : webMenuScopes) {
				if(WebMenuScopeApplyPolicy.fromCode(webMenuScope.getApplyPolicy()) == WebMenuScopeApplyPolicy.DELETE){
					dtosMap.remove(webMenuScope.getMenuId());
				}
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
	
	/**
	 * 处理菜单
	 * @param menus
	 * @param webMenuScopes
	 * @return
	 */
	private List<WebMenu> handleMenus(List<WebMenu> menus, List<WebMenuScope> webMenuScopes){
		Map<Long, WebMenu> menuMap = new LinkedHashMap<Long, WebMenu>();
    	for (WebMenu webMenu : menus) {
    		menuMap.put(webMenu.getId(), webMenu);
		}
    	
    	for (WebMenuScope webMenuScope : webMenuScopes) {
    		WebMenu webMenu = menuMap.get(webMenuScope.getMenuId());
			if(null != webMenu && WebMenuScopeApplyPolicy.fromCode(webMenuScope.getApplyPolicy()) == WebMenuScopeApplyPolicy.DELETE){
				menuMap.remove(webMenuScope.getMenuId());
			}else if(WebMenuScopeApplyPolicy.fromCode(webMenuScope.getApplyPolicy()) == WebMenuScopeApplyPolicy.OVERRIDE){
				//override menu
				webMenu.setName(webMenuScope.getMenuName());
			}else if(WebMenuScopeApplyPolicy.fromCode(webMenuScope.getApplyPolicy()) == WebMenuScopeApplyPolicy.REVERT){
				
			}
		}
    	
    	menus = new ArrayList<WebMenu>();
    	for (Long  key : menuMap.keySet()) {
    		menus.add(menuMap.get(key));
		}
    	return menus;
	}
	

	/**
     * 抛出无权限 
     */
    private void returnNoPrivileged(List<Long> privileges, User user){
    	LOGGER.error("non-privileged, privileges="+privileges + ", userId=" + user.getId());
		throw RuntimeErrorException.errorWith(OrganizationServiceErrorCode.SCOPE, OrganizationServiceErrorCode.ERROR_NO_PRIVILEGED,
				"non-privileged.");
    }
    
    public static void main(String[] args) {
	}
}
