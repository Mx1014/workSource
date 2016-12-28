package com.everhomes.acl;

import com.everhomes.community.Community;
import com.everhomes.community.CommunityProvider;
import com.everhomes.community.ResourceCategory;
import com.everhomes.community.ResourceCategoryAssignment;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.db.DbProvider;
import com.everhomes.entity.EntityType;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.module.*;
import com.everhomes.organization.*;
import com.everhomes.payment.util.DownloadUtil;
import com.everhomes.rest.acl.*;
import com.everhomes.rest.acl.admin.*;
import com.everhomes.rest.address.CommunityDTO;
import com.everhomes.rest.app.AppConstants;
import com.everhomes.rest.community.ResourceCategoryType;
import com.everhomes.rest.organization.*;
import com.everhomes.rest.user.IdentifierType;
import com.everhomes.rest.user.admin.ImportDataResponse;
import com.everhomes.server.schema.Tables;
import com.everhomes.settings.PaginationConfigHelper;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserIdentifier;
import com.everhomes.user.UserProvider;
import com.everhomes.util.*;
import com.everhomes.util.excel.RowResult;
import com.everhomes.util.excel.handler.PropMrgOwnerHandler;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jooq.Condition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

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
	private OrganizationProvider organizationProvider;
	
	@Autowired
	private OrganizationService organizationService;
	
	@Autowired
	private UserProvider userProvider;

	@Autowired
	private ServiceModuleProvider serviceModuleProvider;

	@Autowired
	private CommunityProvider communityProvider;

	@Autowired
	private ConfigurationProvider configProvider;

	
	@Override
	public ListWebMenuResponse listWebMenu(ListWebMenuCommand cmd) {
		User user = UserContext.current().getUser();

		Integer namespaceId = UserContext.getCurrentNamespaceId(cmd.getNamespaceId());
		
		ListWebMenuResponse res = new ListWebMenuResponse();
		
		//获取用户在机构范围内的所有权限
		List<Long> privilegeIds = new ArrayList<>();

		List<Long> ids = this.getUserPrivileges(null, cmd.getOrganizationId(), user.getId());
		if(null != ids){
			privilegeIds.addAll(ids);
		}

		ids = this.getAllResourcePrivilegeIds(cmd.getOrganizationId(), user.getId());
		if(null != ids){
			privilegeIds.addAll(ids);
		}

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

		//去掉机构要屏蔽的菜单
		if(null != orgWebMenuScopes && orgWebMenuScopes.size() > 0){
			menus = this.handleMenus(menus, orgWebMenuScopes);
		}else{
			//根据域获取全部要屏蔽的菜单
			List<WebMenuScope> webMenuScopes = webMenuPrivilegeProvider.listWebMenuScopeByOwnerId(EntityType.NAMESPACE.getCode(), Long.valueOf(namespaceId));
			//去掉域要屏蔽的菜单
			if(null != webMenuScopes && webMenuScopes.size() > 0)
				menus = this.handleMenus(menus, webMenuScopes);
		}

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
		

		if(null != orgWebMenuScopes && orgWebMenuScopes.size() > 0){
			return this.getListWebMenuPrivilege(webMenuPrivileges, orgWebMenuScopes);
		}

		//屏蔽域下面的菜单权限
		List<WebMenuScope> webMenuScopes = webMenuPrivilegeProvider.listWebMenuScopeByOwnerId(EntityType.NAMESPACE.getCode(), Long.valueOf(namespaceId));
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

		User user = UserContext.current().getUser();

		Organization org = organizationProvider.findOrganizationById(cmd.getOrganizationId());
		Long roleId = RoleConstants.PM_SUPER_ADMIN;

		CreateOrganizationAccountCommand command = new CreateOrganizationAccountCommand();
		command.setOrganizationId(org.getId());
		command.setAccountName(cmd.getContactName());
		command.setAccountPhone(cmd.getContactToken());
		OrganizationMember member = organizationService.createOrganizationAccount(command, roleId);


		List<Long> privilegeIds = new ArrayList<>();
		privilegeIds.add(PrivilegeConstants.ORGANIZATION_SUPER_ADMIN);
		/**
		 * 分配权限
		 */
		this.assignmentPrivileges(EntityType.ORGANIZATIONS.getCode(),org.getId(),EntityType.USER.getCode(),member.getTargetId(),"admin",privilegeIds);

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
			LOGGER.error("invalid parameter error, cmd = {}", cmd);
			throw RuntimeErrorException.errorWith(OrganizationServiceErrorCode.SCOPE, OrganizationServiceErrorCode.ERROR_INVALID_PARAMETER,
					"invalid parameter error.");
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

			for (RoleAssignment assignment : roleAssignments) {
				aclProvider.deleteRoleAssignment(assignment.getId());
			}
			for (Long roleId : roleIds) {
				roleAssignment.setRoleId(roleId);
				roleAssignment.setOwnerType(EntityType.ORGANIZATIONS.getCode());
				roleAssignment.setOwnerId(cmd.getOrganizationId());
				roleAssignment.setTargetType(cmd.getTargetType());
				roleAssignment.setTargetId(cmd.getTargetId());
				roleAssignment.setCreatorUid(UserContext.current().getUser().getId());
				aclProvider.createRoleAssignment(roleAssignment);				
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

		List<Privilege> s = null;
		if(!StringUtils.isEmpty(module)){
    		s = aclProvider.getPrivilegesByTag(module); //aclProvider 调平台根据角色list+模块 获取权限list接口
    		if(null == s){
    			return privileges;
    		}
    	}

		List<Long> privilegeIds = new ArrayList<>();
		for (Long roleId : roleIds) {
			if(RoleConstants.PLATFORM_PM_ROLES.contains(roleId) || RoleConstants.PLATFORM_ENTERPRISE_ROLES.contains(roleId)){
				List<Long> ids = this.getResourceAclPrivilegeIds(EntityType.ORGANIZATIONS.getCode(), null, EntityType.ROLE.getCode(), roleId);
				if(null != ids){
					privilegeIds.addAll(ids);
				}

			}else{
				Role role = aclProvider.getRoleById(roleId);
				if(null != role){
					List<Long> ids = this.getResourceAclPrivilegeIds(role.getOwnerType(), role.getOwnerId(), EntityType.ROLE.getCode(), roleId);
					if(null != ids){
						privilegeIds.addAll(ids);
					}
				}
				LOGGER.debug("user["+userId+"], role = " + StringHelper.toJsonString(role));
			}
		}
		if(!StringUtils.isEmpty(module)){
			for (Privilege privilege : s) {
				if(privilegeIds.contains(privilege.getId())){
					privileges.add(privilege.getId());
				}
			}
		}else{
			privileges.addAll(privilegeIds);
		}

    	return privileges;
    }

	private List<Long> getAllResourcePrivilegeIds(Long organizationId, Long userId){
		Organization organization = organizationProvider.findOrganizationById(organizationId);
		List<CommunityDTO> communityDTOs = organizationService.listAllChildrenOrganizationCoummunities(organizationId);
		List<OrganizationDTO> organizationDTOs = new ArrayList<>();
		UserIdentifier userIdentifier = userProvider.findClaimedIdentifierByOwnerAndType(userId, IdentifierType.MOBILE.getCode());
		List<Long> privilegeIds = new ArrayList<>();
		if(null != userIdentifier){
			List<OrganizationDTO> departments = organizationService.getOrganizationMemberGroups(OrganizationGroupType.DEPARTMENT, userIdentifier.getIdentifierToken(), organization.getPath());
			if(null != departments){
				organizationDTOs.addAll(departments);
			}
			List<OrganizationDTO> enterprises = organizationService.getOrganizationMemberGroups(OrganizationGroupType.ENTERPRISE, userIdentifier.getIdentifierToken(), organization.getPath());
			if(null != enterprises){
				organizationDTOs.addAll(enterprises);
			}
		}

		List<Long> ids = this.getResourceAclPrivilegeIds(EntityType.ORGANIZATIONS.getCode(), organizationId, EntityType.USER.getCode(), userId);
		if(null != ids){
			privilegeIds.addAll(ids);
		}
		for (CommunityDTO communityDTO:communityDTOs) {
			List<Long> pIds = this.getResourceAclPrivilegeIds(EntityType.COMMUNITY.getCode(), communityDTO.getId(), EntityType.USER.getCode(), userId);
			if(null != pIds){
				privilegeIds.addAll(pIds);
			}
			if(pIds.size() == 0){
				for (OrganizationDTO dto: organizationDTOs) {
					ids = this.getResourceAclPrivilegeIds(EntityType.COMMUNITY.getCode(), communityDTO.getId(), EntityType.ORGANIZATIONS.getCode(), dto.getId());
					if(null != ids){
						privilegeIds.addAll(ids);
					}
				}
			}
		}
		return privilegeIds;
	}

    private List<Long> getResourceAclPrivilegeIds(String ownerType, Long ownerId, String targetType, Long targetId){
		List<Long> privilegeIds = new ArrayList<>();
		AclRoleDescriptor descriptor = new AclRoleDescriptor(targetType, targetId);
		List<Acl> acls = aclProvider.getResourceAclByRole(ownerType,ownerId, descriptor);
		if(null != acls){
			for (Acl acl :acls) {
				privilegeIds.add(acl.getPrivilegeId());
			}
		}
		return privilegeIds;
	}


    /**
     * 获取用户的权限列表
     * @param communityId
     * @param userId
     * @return
     */
    public List<Long> getUserCommunityPrivileges(Long communityId, Long userId){
    	
    	List<RoleAssignment> userRoles = aclProvider.getRoleAssignmentByResourceAndTarget(EntityType.COMMUNITY.getCode(), communityId, EntityType.USER.getCode(), userId);

    	List<Long> privileges = new ArrayList<Long>();
    	
    	List<Long> roleIds = new ArrayList<Long>();
    	for (RoleAssignment role : userRoles) {
    		roleIds.add(role.getRoleId());
		}
    	
    	List<Long> privilegeIds = new ArrayList<Long>();
		for (Long roleId : roleIds) {
			List<Acl> acls = null;
			if(RoleConstants.PLATFORM_PM_ROLES.contains(roleId) || RoleConstants.PLATFORM_ENTERPRISE_ROLES.contains(roleId)){
				acls = aclProvider.getResourceAclByRole(EntityType.ORGANIZATIONS.getCode(), null, roleId);
			}else{
				Role role = aclProvider.getRoleById(roleId);
				if(null != role){
					acls = aclProvider.getResourceAclByRole(role.getOwnerType(), role.getOwnerId(), new AclRoleDescriptor(EntityType.ROLE.getCode(), roleId));
				}
				LOGGER.debug("user["+userId+"], role = " + StringHelper.toJsonString(role));
			}
			for (Acl acl : acls) {
				privilegeIds.add(acl.getPrivilegeId());
			}
			
		}
		privileges = privilegeIds;	
    	
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
	@Deprecated
    public boolean checkAuthority(String ownerType, Long ownerId, Long privilegeId){
    	User user = UserContext.current().getUser();

    	List<Long> privileges = this.getUserPrivileges(null, ownerId, user.getId());

    	if(!privileges.contains(privilegeId)){

    		this.returnNoPrivileged(privileges, user);
    	}

		List<Long> ids = this.getAllResourcePrivilegeIds(ownerId, user.getId());
		if(null != ids){
			privileges.addAll(ids);
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
    	ByteArrayOutputStream out = null;
    	XSSFWorkbook wb = organizationService.createXSSFWorkbook(members);
    	try {
			out = new ByteArrayOutputStream();
			wb.write(out);
		    DownloadUtil.download(out, response);
		} catch (Exception e) {
			LOGGER.error("export error, e = {}", e);
		} finally{
			try {
				wb.close();
				out.close();
			} catch (IOException e) {
				LOGGER.error("close error", e);
			}
		}
    }
    
    @Override
    public ImportDataResponse importRoleAssignmentPersonnelXls(
    		ExcelRoleExcelRoleAssignmentPersonnelCommand cmd,
    		MultipartFile[] files) {
    	ImportDataResponse result = new ImportDataResponse();
    	if(null == files || 0 == files.length){
    		LOGGER.error("file is empty, cmd = {}" + cmd);
    		throw RuntimeErrorException.errorWith(OrganizationServiceErrorCode.SCOPE, OrganizationServiceErrorCode.ERROR_FILE_IS_EMPTY,
    				"file is empty.");
    	}
    	
    	Organization organization = organizationProvider.findOrganizationById(cmd.getOrganizationId());
    	
    	if(null == organization){
    		LOGGER.error("organization non-existent, organization = {}" + cmd.getOrganizationId());
    		throw RuntimeErrorException.errorWith(OrganizationServiceErrorCode.SCOPE, OrganizationServiceErrorCode.ERROR_FILE_IS_EMPTY,
    				"file is empty.");
    	}
    	
    	//解析excel
		try {
			List excelList = PropMrgOwnerHandler.processorExcel(files[0].getInputStream());
			
			List<OrganizationMemberDTO> memberDTOs = this.convertMemberDTO(excelList);
			
			Long totalCount = 0l;
			
			Long failCount = 0l;
			
			List<String> logs = new ArrayList<String>();
			
			totalCount = (long)memberDTOs.size();
			
			for (OrganizationMemberDTO organizationMemberDTO : memberDTOs) {
				try {
					this.createMemberAndRoleAssignment(organizationMemberDTO, organization.getId(), cmd.getRoleId());
				} catch (Exception e) {
					logs.add(StringHelper.toJsonString(organizationMemberDTO) + "exception:" + e.getMessage());
				}
			}
			
			failCount = (long)logs.size();
			
			result.setFailCount(failCount);
			result.setTotalCount(totalCount);
			result.setLogs(logs);
		} catch (IOException e) {
			LOGGER.error("import data error , cmd = {}" + cmd);
		}
    	
    	return result;
    }
    
    private void createMemberAndRoleAssignment(OrganizationMemberDTO memberDTO, Long organizationId, Long roleId){
    	
    	OrganizationMember member = ConvertHelper.convert(memberDTO, OrganizationMember.class);
    	
    	User user = UserContext.current().getUser();
    	
    	Integer namespaceId = UserContext.getCurrentNamespaceId();
    	
    	if(StringUtils.isEmpty(memberDTO.getContactToken())
    			|| StringUtils.isEmpty(memberDTO.getContactName())
    			|| StringUtils.isEmpty(memberDTO.getGroupName())
    			|| StringUtils.isEmpty(memberDTO.getEmployeeNo())){
    		LOGGER.error("invalid parameter error , member = {}" + member);
    		throw RuntimeErrorException.errorWith(OrganizationServiceErrorCode.SCOPE, OrganizationServiceErrorCode.ERROR_INVALID_PARAMETER,
    				"invalid parameter error.");
    	}
    	
    	UserIdentifier userIdentifier = userProvider.findClaimedIdentifierByToken(namespaceId, memberDTO.getContactToken());
    	if(null == userIdentifier){
    		LOGGER.error("Mobile phone not registered , contactToken = {}" + memberDTO.getContactToken());
    		throw RuntimeErrorException.errorWith(OrganizationServiceErrorCode.SCOPE, OrganizationServiceErrorCode.ERROR_INVALID_PARAMETER,
    				"Mobile phone not registered.");
    	}
    	
    	member.setStatus(OrganizationMemberStatus.ACTIVE.getCode());
    	member.setMemberGroup(OrganizationMemberGroupType.MANAGER.getCode());
    	member.setContactType(IdentifierType.MOBILE.getCode());
    	member.setCreatorUid(user.getId());
    	member.setNamespaceId(namespaceId);
    	member.setGroupId(0l);
    	member.setTargetType(OrganizationMemberTargetType.USER.getCode());
    	member.setTargetId(userIdentifier.getOwnerUid());
    	
    	dbProvider.execute((TransactionStatus status) -> {
    		OrganizationMember organizationMember = organizationProvider.findOrganizationMemberByOrgIdAndToken(memberDTO.getContactToken(), organizationId);
        	
        	if(null == organizationMember){
        		member.setOrganizationId(organizationId);
        		organizationProvider.createOrganizationMember(member);
        	}
        	
        	Organization department = organizationProvider.findOrganizationByParentAndName(organizationId, memberDTO.getGroupName());
        	
        	if(null == department){
        		CreateOrganizationCommand command = new CreateOrganizationCommand();
        		command.setGroupType(OrganizationGroupType.DEPARTMENT.getCode());
        		command.setName(memberDTO.getGroupName());
        		command.setParentId(organizationId);
        		OrganizationDTO departmentDTO = organizationService.createChildrenOrganization(command);
        		member.setOrganizationId(departmentDTO.getId());
        		member.setGroupPath(departmentDTO.getPath());
        	}else{
            	organizationMember = organizationProvider.findOrganizationMemberByOrgIdAndToken(memberDTO.getContactToken(), department.getId());
            	if(null == organizationMember){
//            		LOGGER.error("phone number already exists. organizationId = {}, contactToken = {}", department.getId(), memberDTO.getContactToken());
//    				throw RuntimeErrorException.errorWith(OrganizationServiceErrorCode.SCOPE, OrganizationServiceErrorCode.ERROR_INVALID_PARAMETER, 
//    						"phone number already exists.");
            		member.setOrganizationId(department.getId());
            		member.setGroupPath(department.getPath());
                	organizationProvider.createOrganizationMember(member);
            	}

        	}
        	RoleAssignment roleAssignment = new RoleAssignment();
    		List<RoleAssignment> roleAssignments = aclProvider.getRoleAssignmentByResourceAndTarget(EntityType.ORGANIZATIONS.getCode(), organizationId, EntityType.USER.getCode(), member.getTargetId());
    		
    		if(null != roleAssignments && 0 < roleAssignments.size()){
    			for (RoleAssignment assignment : roleAssignments) {
    				if(assignment.getRoleId().equals(roleId)){
    	        		LOGGER.debug("role assignment already exists. roleId = {}, userId = {}, contactToken = {}", roleId, member.getTargetId(), member.getContactToken());
    					return null;
    				}
    			}
    		}
    		
    		roleAssignment.setRoleId(roleId);
    		roleAssignment.setOwnerType(EntityType.ORGANIZATIONS.getCode());
    		roleAssignment.setOwnerId(organizationId);
    		roleAssignment.setTargetType(EntityType.USER.getCode());
    		roleAssignment.setTargetId(member.getTargetId());
    		roleAssignment.setCreatorUid(user.getId());
    		aclProvider.createRoleAssignment(roleAssignment);
    		
    		return null;
    	});
    }
    
    private List<OrganizationMemberDTO> convertMemberDTO(List excelList){
    	List<OrganizationMemberDTO> result = new ArrayList<OrganizationMemberDTO>();
		boolean firstRow = true;
		for (Object o : excelList) {
			if(firstRow){
				firstRow = false;
				continue;
			}
			RowResult r = (RowResult)o;
			OrganizationMemberDTO dto = new OrganizationMemberDTO();
			
			if(!StringUtils.isEmpty(r.getA())){
				dto.setEmployeeNo(Long.valueOf(r.getA()));
			}
			dto.setGroupName(r.getB());
			dto.setContactName(r.getC());
			Byte gender = 0;
			if(!StringUtils.isEmpty(r.getD()) && r.getD().equals("男")){
				gender = 1;
			}else if(!StringUtils.isEmpty(r.getD()) && r.getD().equals("女")){
				gender = 2;
			}
			dto.setGender(gender);
			dto.setContactToken(r.getE());
			
			result.add(dto);
		}
		return result;
    }

	@Override
    public List<RoleAssignment> getUserAllOrgRoles(Long organizationId, Long userId){
    	Organization org = organizationProvider.findOrganizationById(organizationId);
    	String path = org.getPath();
    	String[] orgIds = path.split("/");
    	List<RoleAssignment> userRoles = null;
    	for (String orgId : orgIds) {
    		if(StringUtils.isEmpty(orgId)){
    			continue;
    		}
			Organization organization = organizationProvider.findOrganizationById(Long.parseLong(orgId));
			if(OrganizationGroupType.fromCode(organization.getGroupType()) == OrganizationGroupType.ENTERPRISE || OrganizationGroupType.fromCode(organization.getGroupType()) == OrganizationGroupType.DEPARTMENT || OrganizationGroupType.fromCode(organization.getGroupType()) == OrganizationGroupType.GROUP ){
				if(null == userRoles){
					userRoles = this.getUserRoles(Long.parseLong(orgId), userId);
				}else{
					userRoles.addAll(this.getUserRoles(Long.parseLong(orgId), userId));
				}
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

		Map<Long, WebMenu> menuMap = this.getWebMenuMap(WebMenuType.PARK.getCode());

		if(null != webMenuScopes && webMenuScopes.size() > 0){
			for (WebMenuScope webMenuScope : webMenuScopes) {
				WebMenu menu = menuMap.get(webMenuScope.getMenuId());
				if(null == menu){
					continue;
				}

				if(WebMenuScopeApplyPolicy.fromCode(webMenuScope.getApplyPolicy()) == WebMenuScopeApplyPolicy.REVERT ||
						WebMenuScopeApplyPolicy.fromCode(webMenuScope.getApplyPolicy()) == WebMenuScopeApplyPolicy.OVERRIDE){
					if(null != dtosMap.get(webMenuScope.getMenuId()) && dtosMap.get(webMenuScope.getMenuId()).size() > 0){
						ListWebMenuPrivilegeDTO dto = new ListWebMenuPrivilegeDTO();
						dto.setModuleId(menu.getId());
						dto.setModuleName(menu.getName());
						dto.setDtos(dtosMap.get(webMenuScope.getMenuId()));
						dtos.add(dto);
					}
				}
			}
		}else{
			for (Long menuId : dtosMap.keySet()) {
				ListWebMenuPrivilegeDTO dto = new ListWebMenuPrivilegeDTO();
				WebMenu menu = menuMap.get(menuId);
				if(null == menu){
					LOGGER.error("Menu not found menuId={}", menuId);
					continue;
				}
				dto.setModuleId(menu.getId());
				dto.setModuleName(menu.getName());
				dto.setDtos(dtosMap.get(menuId));
				dtos.add(dto);
			}
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

		menus = new ArrayList<WebMenu>();

    	for (WebMenuScope webMenuScope : webMenuScopes) {
    		WebMenu webMenu = menuMap.get(webMenuScope.getMenuId());

			if(null == webMenu){
				continue;
			}

			if(WebMenuScopeApplyPolicy.fromCode(webMenuScope.getApplyPolicy()) == WebMenuScopeApplyPolicy.OVERRIDE){
				//override menu
				webMenu.setName(webMenuScope.getMenuName());
				menus.add(webMenu);
			}else if(WebMenuScopeApplyPolicy.fromCode(webMenuScope.getApplyPolicy()) == WebMenuScopeApplyPolicy.REVERT){
				menus.add(webMenu);
			}
		}
		menus.sort((o1, o2) -> o1.getSortNum() - o2.getSortNum());
    	return menus;
	}


	@Override
	public void createOrganizationAdmin(CreateOrganizationAdminCommand cmd, Integer namespaceId){
		Organization org = organizationProvider.findOrganizationById(cmd.getOrganizationId());
		Long roleId = RoleConstants.ENTERPRISE_SUPER_ADMIN;

		CreateOrganizationAccountCommand command = new CreateOrganizationAccountCommand();
		command.setOrganizationId(org.getId());
		command.setAccountName(cmd.getContactName());
		command.setAccountPhone(cmd.getContactToken());
		OrganizationMember member = organizationService.createOrganizationAccount(command, roleId, namespaceId);

		List<Long> privilegeIds = new ArrayList<>();
		privilegeIds.add(PrivilegeConstants.ORGANIZATION_ADMIN);
		/**
		 * 分配权限
		 */
		this.assignmentPrivileges(EntityType.ORGANIZATIONS.getCode(),org.getId(),EntityType.USER.getCode(),member.getTargetId(),"admin",privilegeIds);
	}
	
	@Override
	public void createOrganizationAdmin(CreateOrganizationAdminCommand cmd){
		createOrganizationAdmin(cmd, null);
	}

	@Override
	public void createServiceModuleAdmin(CreateServiceModuleAdminCommand cmd){

		Organization org = organizationProvider.findOrganizationById(cmd.getOrganizationId());

		CreateOrganizationAccountCommand command = new CreateOrganizationAccountCommand();
		command.setOrganizationId(org.getId());
		command.setAccountName(cmd.getContactName());
		command.setAccountPhone(cmd.getContactToken());
		OrganizationMember member = organizationService.createOrganizationAccount(command, null);

		/**
		 * 分配权限
		 */
		assignmentPrivileges(EntityType.ORGANIZATIONS.getCode(),org.getId(),EntityType.USER.getCode(),member.getTargetId(), cmd.getModuleId().toString(),cmd.getModuleId(), ServiceModulePrivilegeType.SUPER);

		/**
		 * 分配模块
		 */
		ServiceModuleAssignment serviceModuleAssignment = new ServiceModuleAssignment();
		serviceModuleAssignment.setCreateUid(UserContext.current().getUser().getId());
		serviceModuleAssignment.setOwnerType(EntityType.ORGANIZATIONS.getCode());
		serviceModuleAssignment.setOwnerId(org.getId());
		serviceModuleAssignment.setNamespaceId(UserContext.getCurrentNamespaceId());
		serviceModuleAssignment.setOrganizationId(org.getId());
		serviceModuleAssignment.setTargetType(EntityType.USER.getCode());
		serviceModuleAssignment.setTargetId(member.getTargetId());
		serviceModuleAssignment.setModuleId(cmd.getModuleId());
		serviceModuleProvider.createServiceModuleAssignment(serviceModuleAssignment);

	}

	@Override
	public void assignmentPrivileges(String ownerType, Long ownerId,String targetType, Long targetId, String scope,  List<Long> privilegeIds){
		User user = UserContext.current().getUser();
		if(null != privilegeIds){
			for (Long privilegeId: privilegeIds) {
				Acl acl = new Acl();
				acl.setRoleId(targetId);
				acl.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
				acl.setCreatorUid(user.getId());
				acl.setOwnerType(ownerType);
				acl.setOwnerId(ownerId);
				acl.setPrivilegeId(privilegeId);
				acl.setRoleType(targetType);
				acl.setScope(ownerType + ownerId + "." + scope);
				aclProvider.createAcl(acl);
			}
		}else{
			LOGGER.debug("assignment privileges is null");
		}
	}

	public void assignmentPrivileges(String ownerType, Long ownerId,String targetType, Long targetId, String scope, Long moduleId, ServiceModulePrivilegeType privilegeType){

		List<ServiceModulePrivilege> serviceModulePrivileges = null;
		if(0L == moduleId){
			List<ServiceModuleScope> moduleScopes = serviceModuleProvider.listServiceModuleScopes(UserContext.getCurrentNamespaceId(), null, null, null);
			List<Long> moduleIds = new ArrayList<>();
			for (ServiceModuleScope moduleScope:moduleScopes) {
				moduleIds.add(moduleScope.getModuleId());
			}
			serviceModulePrivileges = serviceModuleProvider.listServiceModulePrivileges(moduleIds, privilegeType);
		}else{
			serviceModulePrivileges = serviceModuleProvider.listServiceModulePrivileges(moduleId, privilegeType);
		}


		List<Long> privilegeIds = new ArrayList<>();
		for (ServiceModulePrivilege serviceModulePrivilege: serviceModulePrivileges) {
			privilegeIds.add(serviceModulePrivilege.getPrivilegeId());
		}

		this.assignmentPrivileges(ownerType, ownerId, targetType, targetId, scope, privilegeIds);
	}


	@Override
	public List<OrganizationContactDTO> listServiceModuleAdministrators(ListServiceModuleAdministratorsCommand cmd) {

		List<OrganizationContactDTO> contactDTOs = new ArrayList<>();

		Condition condition = Tables.EH_SERVICE_MODULE_ASSIGNMENTS.OWNER_TYPE.eq(cmd.getOwnerType());
		condition = condition.and(Tables.EH_SERVICE_MODULE_ASSIGNMENTS.OWNER_ID.eq(cmd.getOwnerId()));
		condition = condition.and(Tables.EH_SERVICE_MODULE_ASSIGNMENTS.MODULE_ID.eq(cmd.getModuleId()));

		List<ServiceModuleAssignment> serviceModuleAssignments = serviceModuleProvider.listServiceModuleAssignments(condition, cmd.getOrganizationId());

		for (ServiceModuleAssignment serviceModuleAssignment: serviceModuleAssignments) {
			if(EntityType.USER ==EntityType.fromCode(serviceModuleAssignment.getTargetType())){
				OrganizationMember member = organizationProvider.findOrganizationMemberByOrgIdAndUId(serviceModuleAssignment.getTargetId(), cmd.getOrganizationId());
				if(null != member){
					OrganizationContactDTO contactDTO = ConvertHelper.convert(member,OrganizationContactDTO.class);
					contactDTOs.add(contactDTO);
				}
			}
		}
		
		return contactDTOs;
	}

	@Override
	public List<OrganizationContactDTO> listOrganizationSuperAdministrators(ListServiceModuleAdministratorsCommand cmd) {

//		List<Long> roles = new ArrayList<Long>();
//		roles.add(RoleConstants.PM_SUPER_ADMIN);
//		ListOrganizationPersonnelByRoleIdsCommand command = new ListOrganizationPersonnelByRoleIdsCommand();
//		command.setRoleIds(roles);
//		command.setOrganizationId(cmd.getOrganizationId());
//		ListOrganizationMemberCommandResponse res = organizationService.listOrganizationPersonnelsByRoleIds(command);
//
//		return res.getMembers().stream().map(r -> {
//				return ConvertHelper.convert(r, OrganizationContactDTO.class);
//		}).collect(Collectors.toList());
		List<OrganizationMember> members = this.getRoleMembers(cmd.getOrganizationId(), RoleConstants.PM_SUPER_ADMIN);
		return members.stream().map(r -> {
			return ConvertHelper.convert(r, OrganizationContactDTO.class);
		}).collect(Collectors.toList());
	}

	@Override
	public List<OrganizationContactDTO> listOrganizationAdministrators(ListServiceModuleAdministratorsCommand cmd) {
		List<OrganizationMember> members = this.getRoleMembers(cmd.getOrganizationId(), RoleConstants.ENTERPRISE_SUPER_ADMIN, cmd.getKeywords());
		return members.stream().map(r -> {
			return ConvertHelper.convert(r, OrganizationContactDTO.class);
		}).collect(Collectors.toList());
	}

	/**
	 * 获取角色人员
	 * @param organizationId
	 * @param roleId
     * @return
     */
	private List<OrganizationMember> getRoleMembers(Long organizationId, Long roleId){
		List<OrganizationMember> members = new ArrayList<>();
		List<RoleAssignment> roleAssignments = aclProvider.getRoleAssignmentByResource(EntityType.ORGANIZATIONS.getCode(), organizationId);
		if(null != roleAssignments){
			for (RoleAssignment roleassignment: roleAssignments) {
				if(EntityType.fromCode(roleassignment.getTargetType()) == EntityType.USER && roleassignment.getRoleId().equals(roleId)){
					OrganizationMember member = organizationProvider.findOrganizationMemberByOrgIdAndUId(roleassignment.getTargetId(), organizationId);
					if(null != member)members.add(member);
				}
			}
		}
		return members;
	}

	/**
	 * 获取角色人员, 可以根据关键字搜索的
     */
	private List<OrganizationMember> getRoleMembers(Long organizationId, Long roleId, String keywords){
		List<OrganizationMember> members = new ArrayList<>();
		List<RoleAssignment> roleAssignments = aclProvider.getRoleAssignmentByResource(EntityType.ORGANIZATIONS.getCode(), organizationId);
		if(null != roleAssignments){
			for (RoleAssignment roleassignment: roleAssignments) {
				if(EntityType.fromCode(roleassignment.getTargetType()) == EntityType.USER && roleassignment.getRoleId().equals(roleId)){
					OrganizationMember member = organizationProvider.findOrganizationMemberByOrgIdAndUId(roleassignment.getTargetId(), organizationId);
                    if (null != member) {
                        if (keywords != null && !(member.getContactName().contains(keywords) || member.getContactToken().contains(keywords))) {
                            continue;
                        }
                        members.add(member);
                    }
				}
			}
		}
		return members;
	}

	@Override
	public void deleteOrganizationSuperAdministrators(DeleteOrganizationAdminCommand cmd) {
		EntityType entityType = EntityType.fromCode(cmd.getOwnerType());
		if(null == entityType){
			LOGGER.error("params ownerType error, cmd="+ cmd.getOwnerType());
			throw RuntimeErrorException.errorWith(OrganizationServiceErrorCode.SCOPE, OrganizationServiceErrorCode.ERROR_INVALID_PARAMETER,
					"params ownerType error.");
		}

		List<RoleAssignment> roleAssignments = aclProvider.getRoleAssignmentByResourceAndTarget(EntityType.ORGANIZATIONS.getCode(), cmd.getOrganizationId(), EntityType.USER.getCode(), cmd.getUserId());

		/**
		 * 只删除admin这个角色权限
		 */
		for (RoleAssignment roleAssignment : roleAssignments) {
			if(roleAssignment.getRoleId() == RoleConstants.PM_SUPER_ADMIN){
				aclProvider.deleteRoleAssignment(roleAssignment.getId());
			}
		}

		/**
		 * 权限删除
		 */
		List<Long> privilegeIds = new ArrayList<>();
		privilegeIds.add(PrivilegeConstants.ORGANIZATION_SUPER_ADMIN);
		deleteAcls(EntityType.ORGANIZATIONS.getCode(), cmd.getOrganizationId(), EntityType.USER.getCode(), cmd.getUserId(), null, privilegeIds);


	}

	@Override
	public void deleteOrganizationAdministrators(DeleteOrganizationAdminCommand cmd) {
		EntityType entityType = EntityType.fromCode(cmd.getOwnerType());
		if(null == entityType){
			LOGGER.error("params ownerType error, cmd="+ cmd.getOwnerType());
			throw RuntimeErrorException.errorWith(OrganizationServiceErrorCode.SCOPE, OrganizationServiceErrorCode.ERROR_INVALID_PARAMETER,
					"params ownerType error.");
		}

		List<RoleAssignment> roleAssignments = aclProvider.getRoleAssignmentByResourceAndTarget(EntityType.ORGANIZATIONS.getCode(), cmd.getOrganizationId(), EntityType.USER.getCode(), cmd.getUserId());

		/**
		 * 只删除admin这个角色权限
		 */
		for (RoleAssignment roleAssignment : roleAssignments) {
			if(roleAssignment.getRoleId() == RoleConstants.ENTERPRISE_SUPER_ADMIN){
				aclProvider.deleteRoleAssignment(roleAssignment.getId());
			}
		}

		/**
		 * 权限删除
		 */
		List<Long> privilegeIds = new ArrayList<>();
		privilegeIds.add(PrivilegeConstants.ORGANIZATION_ADMIN);
		deleteAcls(EntityType.ORGANIZATIONS.getCode(), cmd.getOrganizationId(), EntityType.USER.getCode(), cmd.getUserId(), null, privilegeIds);

	}

	@Override
	public void deleteServiceModuleAdministrators(DeleteServiceModuleAdministratorsCommand cmd){
		EntityType entityType = EntityType.fromCode(cmd.getOwnerType());
		if(null == entityType){
			LOGGER.error("params ownerType error, cmd="+ cmd.getOwnerType());
			throw RuntimeErrorException.errorWith(OrganizationServiceErrorCode.SCOPE, OrganizationServiceErrorCode.ERROR_INVALID_PARAMETER,
					"params ownerType error.");
		}

		Condition condition = Tables.EH_SERVICE_MODULE_ASSIGNMENTS.OWNER_TYPE.eq(entityType.getCode());
		condition = condition.and(Tables.EH_SERVICE_MODULE_ASSIGNMENTS.OWNER_ID.eq(cmd.getOwnerId()));
		condition = condition.and(Tables.EH_SERVICE_MODULE_ASSIGNMENTS.MODULE_ID.eq(cmd.getModuleId()));
		condition = condition.and(Tables.EH_SERVICE_MODULE_ASSIGNMENTS.TARGET_TYPE.eq(EntityType.USER.getCode()));
		condition = condition.and(Tables.EH_SERVICE_MODULE_ASSIGNMENTS.TARGET_ID.eq(cmd.getUserId()));

		List<ServiceModuleAssignment> serviceModuleAssignments = serviceModuleProvider.listServiceModuleAssignments(condition, cmd.getOrganizationId());

		for (ServiceModuleAssignment serviceModuleAssignment: serviceModuleAssignments) {
			serviceModuleProvider.deleteServiceModuleAssignmentById(serviceModuleAssignment.getId());
		}

		/**
		 * 权限删除
		 */
		this.deleteAcls(entityType.getCode(), cmd.getOwnerId(), EntityType.USER.getCode(), cmd.getUserId(), cmd.getModuleId(), null);
	}

	@Override
	public void authorizationServiceModule(AuthorizationServiceModuleCommand cmd) {

		Integer namespaceId = UserContext.getCurrentNamespaceId();
		User user = UserContext.current().getUser();
		List<AuthorizationServiceModule> serviceModuleAuthorizations = cmd.getServiceModuleAuthorizations();

		dbProvider.execute((TransactionStatus status) -> {

			for (AuthorizationServiceModule authorizationServiceModule: serviceModuleAuthorizations) {

				List<ServiceModuleAssignment> assignments = serviceModuleProvider.listServiceModuleAssignmentsByTargetIdAndOwnerId(authorizationServiceModule.getResourceType(), authorizationServiceModule.getResourceId(),cmd.getTargetType(),cmd.getTargetId(), cmd.getOrganizationId());
				for (ServiceModuleAssignment assignment: assignments) {
					serviceModuleProvider.deleteServiceModuleAssignmentById(assignment.getId());
				}

				//业务模块授权
				if(0 == authorizationServiceModule.getAllModuleFlag()){
					if(null != authorizationServiceModule.getModuleIds()){
						for (Long moduleId: authorizationServiceModule.getModuleIds()) {
							ServiceModuleAssignment assignment = new ServiceModuleAssignment();
							assignment.setModuleId(moduleId);
							assignment.setOrganizationId(cmd.getOrganizationId());
							assignment.setNamespaceId(namespaceId);
							assignment.setTargetType(cmd.getTargetType());
							assignment.setTargetId(cmd.getTargetId());
							assignment.setOwnerType(authorizationServiceModule.getResourceType());
							assignment.setOwnerId(authorizationServiceModule.getResourceId());
							assignment.setCreateUid(user.getId());
							serviceModuleProvider.createServiceModuleAssignment(assignment);

							if(EntityType.fromCode(authorizationServiceModule.getResourceType()) == EntityType.RESOURCE_CATEGORY){
								List<ResourceCategoryAssignment> buildingAssignments = communityProvider.listResourceCategoryAssignment(authorizationServiceModule.getResourceId(), namespaceId);
								for (ResourceCategoryAssignment buildingAssignment: buildingAssignments) {
									this.assignmentPrivileges(buildingAssignment.getResourceType(),buildingAssignment.getResourceId(),assignment.getTargetType(),assignment.getTargetId(),"M" + moduleId, moduleId,ServiceModulePrivilegeType.SUPER);
								}
							}else{
								this.assignmentPrivileges(assignment.getOwnerType(),assignment.getOwnerId(),assignment.getTargetType(),assignment.getTargetId(),"M" + moduleId, moduleId,ServiceModulePrivilegeType.SUPER);
							}
							/**
							 * 业务模块权限授权
							 */
						}
					}
				}else{
					ServiceModuleAssignment assignment = new ServiceModuleAssignment();
					assignment.setModuleId(0L); //0 代表全部业务
					assignment.setOrganizationId(cmd.getOrganizationId());
					assignment.setNamespaceId(namespaceId);
					assignment.setTargetType(cmd.getTargetType());
					assignment.setTargetId(cmd.getTargetId());
					assignment.setOwnerType(authorizationServiceModule.getResourceType());
					assignment.setOwnerId(authorizationServiceModule.getResourceId());
					assignment.setCreateUid(user.getId());
					serviceModuleProvider.createServiceModuleAssignment(assignment);

					/**
					 * 业务模块权限授权
					 */
					if(EntityType.fromCode(authorizationServiceModule.getResourceType()) == EntityType.RESOURCE_CATEGORY){
						List<ResourceCategoryAssignment> buildingAssignments = communityProvider.listResourceCategoryAssignment(authorizationServiceModule.getResourceId(), namespaceId);
						for (ResourceCategoryAssignment buildingAssignment: buildingAssignments) {
							this.assignmentPrivileges(buildingAssignment.getResourceType(),buildingAssignment.getResourceId(),assignment.getTargetType(),assignment.getTargetId(),"M" + assignment.getModuleId(), assignment.getModuleId(),ServiceModulePrivilegeType.SUPER);
						}
					}else{
						this.assignmentPrivileges(assignment.getOwnerType(),assignment.getOwnerId(),assignment.getTargetType(),assignment.getTargetId(),"M" + assignment.getModuleId(), assignment.getModuleId(),ServiceModulePrivilegeType.SUPER);
					}
				}
			}

			return null;
		});

	}

	@Override
	public List<AuthorizationServiceModuleDTO> listAuthorizationServiceModules(ListAuthorizationServiceModuleCommand cmd) {
		List<AuthorizationServiceModuleDTO> dtos = new ArrayList<>();
		Condition condition = Tables.EH_SERVICE_MODULE_ASSIGNMENTS.TARGET_TYPE.eq(EntityType.ORGANIZATIONS.getCode());
		condition = condition.and(Tables.EH_SERVICE_MODULE_ASSIGNMENTS.TARGET_ID.eq(cmd.getOrganizationId()));
		List<ServiceModuleAssignment> assignments = serviceModuleProvider.listServiceModuleAssignments(condition, cmd.getOwnerId());
		String key = "";
		AuthorizationServiceModuleDTO dto = null;
		for (ServiceModuleAssignment assignment: assignments) {
			if(key.equals(assignment.getOwnerType() + assignment.getOwnerId())){
				ServiceModule serviceModule = serviceModuleProvider.findServiceModuleById(assignment.getModuleId());
				dto.getServiceModules().add(ConvertHelper.convert(serviceModule, ServiceModuleDTO.class));
			}else{
				dto = new AuthorizationServiceModuleDTO();
				dto.setAllModuleFlag((byte)0);

				dto.setResourceId(assignment.getOwnerId());
				dto.setResourceType(assignment.getOwnerType());
				if(EntityType.COMMUNITY == EntityType.fromCode(assignment.getOwnerType())){
					Community community = communityProvider.findCommunityById(dto.getResourceId());
					if(null == community){
						LOGGER.debug("community is null...");
					}else{
						dto.setResourceName(community.getName());
					}
				}else if(EntityType.RESOURCE_CATEGORY == EntityType.fromCode(assignment.getOwnerType())){
					ResourceCategory category = communityProvider.findResourceCategoryById(assignment.getOwnerId());
					if(null == category){
						LOGGER.debug("resource category is null...");
					}else{
						if(EntityType.COMMUNITY == EntityType.fromCode(category.getOwnerType())){
							Community community = communityProvider.findCommunityById(category.getOwnerId());
							if(null == community){
								LOGGER.debug("community is null...");
							}else{
								dto.setResourceName(community.getName() + "-" + category.getName());
							}
						}
					}
				}
				dto.setServiceModules(new ArrayList<>());
				if(0L == assignment.getModuleId()){
					dto.setAllModuleFlag((byte)1);
				}else{
					ServiceModule serviceModule = serviceModuleProvider.findServiceModuleById(assignment.getModuleId());
					dto.getServiceModules().add(ConvertHelper.convert(serviceModule, ServiceModuleDTO.class));
				}
				dtos.add(dto);
				key = assignment.getOwnerType() + assignment.getOwnerId(); // 拼装Key
			}
		}
		return dtos;
	}

	@Override
	public List<AuthorizationServiceModuleMembersDTO> listAuthorizationServiceModuleMembers(ListAuthorizationServiceModuleCommand cmd){
		List<AuthorizationServiceModuleMembersDTO> dtos = new ArrayList<>();
		List<ServiceModuleAssignment> resourceAssignments = serviceModuleProvider.listResourceAssignments(EntityType.ORGANIZATIONS.getCode(), cmd.getOrganizationId(), cmd.getOwnerId(), null);

		if(0 != resourceAssignments.size()){
			List<AuthorizationServiceModuleDTO> authorizationServiceModuleDTOs = this.listAuthorizationServiceModules(cmd);

			int pageSize = PaginationConfigHelper.getPageSize(configProvider, 100000);
			CrossShardListingLocator locator = new CrossShardListingLocator();
			Organization orgCommoand = new Organization();
			orgCommoand.setId(cmd.getOrganizationId());
			orgCommoand.setStatus(OrganizationMemberStatus.ACTIVE.getCode());
			List<OrganizationMember> organizationMembers = this.organizationProvider.listOrganizationPersonnels(null ,orgCommoand, null ,null , locator, pageSize);

			for (OrganizationMember member: organizationMembers) {
				if(OrganizationMemberTargetType.USER == OrganizationMemberTargetType.fromCode(member.getTargetType())){
					AuthorizationServiceModuleMembersDTO dto = ConvertHelper.convert(member, AuthorizationServiceModuleMembersDTO.class);
					List<AuthorizationServiceModuleDTO> authorizationDTOs = CopyUtils.deepCopyList(authorizationServiceModuleDTOs);
					for (AuthorizationServiceModuleDTO authorizationDTO: authorizationDTOs) {
						List<ServiceModuleAssignment> userAssignments = serviceModuleProvider.listServiceModuleAssignmentsByTargetIdAndOwnerId(authorizationDTO.getResourceType(), authorizationDTO.getResourceId(),EntityType.USER.getCode(), member.getTargetId(), cmd.getOwnerId());

						//如果个人有自己的分配业务模块则取自己的
						if(0 != userAssignments.size()){
							List<ServiceModuleDTO> serviceModules = new ArrayList<>();
							for (ServiceModuleAssignment userAssignment:userAssignments) {
								if(0L == userAssignment.getModuleId()){
									authorizationDTO.setAllModuleFlag((byte)1); // 加入moduleId是0 代表全部业务
									break;
								}else{
									authorizationDTO.setAllModuleFlag((byte)0);
								}
								ServiceModule serviceModule = serviceModuleProvider.findServiceModuleById(userAssignment.getModuleId());
								serviceModules.add(ConvertHelper.convert(serviceModule, ServiceModuleDTO.class));
							}
							authorizationDTO.setServiceModules(serviceModules);
						}
					}
					dto.setAuthorizationServiceModules(authorizationDTOs);
					dtos.add(dto);
				}

			}

		}
		return dtos;
	}

	@Override
	public void deleteAuthorizationServiceModule(DeleteAuthorizationServiceModuleCommand cmd) {
		EntityType entityType = EntityType.fromCode(cmd.getOwnerType());
		if(null == entityType){
			LOGGER.error("params ownerType error, cmd="+ cmd.getOwnerType());
			throw RuntimeErrorException.errorWith(OrganizationServiceErrorCode.SCOPE, OrganizationServiceErrorCode.ERROR_INVALID_PARAMETER,
					"params ownerType error.");
		}

		dbProvider.execute((TransactionStatus status) -> {
			//删除部门的授权
			List<ServiceModuleAssignment> serviceModuleAssignments = serviceModuleProvider.listServiceModuleAssignmentsByTargetIdAndOwnerId(cmd.getResourceType(),cmd.getResourceId(),EntityType.ORGANIZATIONS.getCode(),cmd.getOrganizationId(),cmd.getOwnerId());
			//业务模块删除
			for (ServiceModuleAssignment serviceModuleAssignment:serviceModuleAssignments ) {
				serviceModuleProvider.deleteServiceModuleAssignmentById(serviceModuleAssignment.getId());
			}

			/**
			 * 权限删除
			 */
			this.deleteAcls(cmd.getResourceType(), cmd.getResourceId(), EntityType.ORGANIZATIONS.getCode(),cmd.getOrganizationId());

			int pageSize = PaginationConfigHelper.getPageSize(configProvider, 100000);
			CrossShardListingLocator locator = new CrossShardListingLocator();
			Organization orgCommoand = new Organization();
			orgCommoand.setId(cmd.getOrganizationId());
			orgCommoand.setStatus(OrganizationMemberStatus.ACTIVE.getCode());
			List<OrganizationMember> organizationMembers = this.organizationProvider.listOrganizationPersonnels(null ,orgCommoand, null ,null , locator, pageSize);

			//删除部门下所有人员的授权
			for (OrganizationMember member:organizationMembers) {
				if(OrganizationMemberTargetType.USER == OrganizationMemberTargetType.fromCode(member.getTargetType())){
					List<ServiceModuleAssignment> memberAssignments = serviceModuleProvider.listServiceModuleAssignmentsByTargetIdAndOwnerId(cmd.getResourceType(),cmd.getResourceId(),EntityType.USER.getCode(),member.getTargetId(),cmd.getOwnerId());
					for (ServiceModuleAssignment assignment:memberAssignments) {
						serviceModuleProvider.deleteServiceModuleAssignmentById(assignment.getId());
					}

					/**
					 * 权限删除
					 */
					this.deleteAcls(cmd.getResourceType(), cmd.getResourceId(), EntityType.USER.getCode(),member.getTargetId());
				}
			}
			return null;
		});

	}

	@Override
	public List<ProjectDTO> listUserRelatedProjectByMenuId(ListUserRelatedProjectByMenuIdCommand cmd) {
		User user = UserContext.current().getUser();
		Integer namespaceId = UserContext.getCurrentNamespaceId();
		List<WebMenuPrivilege> webMenuPrivileges = webMenuPrivilegeProvider.listWebMenuPrivilegeByMenuId(cmd.getMenuId());

		// 用户的角色以及用户所在部门角色的所有权限
		List<Long> privilegeIds = this.getUserPrivileges(null, cmd.getOrganizationId(), user.getId());

		// 用户在当前机构自身权限
		privilegeIds.addAll(this.getResourceAclPrivilegeIds(EntityType.ORGANIZATIONS.getCode(), cmd.getOrganizationId(), EntityType.USER.getCode(), user.getId()));

		List<CommunityDTO> communitydtos = organizationService.listAllChildrenOrganizationCoummunities(cmd.getOrganizationId());

		List<ProjectDTO> projectDTOs = new ArrayList<>();
		for (WebMenuPrivilege webMenuPrivilege:webMenuPrivileges) {
			// 用户有此菜单的权限，则获取全部的园区项目
			if(privilegeIds.contains(webMenuPrivilege.getPrivilegeId())){
				for (CommunityDTO community: communitydtos) {
					ProjectDTO dto = new ProjectDTO();
					dto.setProjectId(community.getId());
					dto.setProjectName(community.getName());
					dto.setProjectType(EntityType.COMMUNITY.getCode());
					projectDTOs.add(dto);
				}
				break;
			}
		}

		if(0 != communitydtos.size() && 0 == projectDTOs.size()){
			List<Long> moduleIds = new ArrayList<>();
			for (WebMenuPrivilege webMenuPrivilege: webMenuPrivileges) {
				List<ServiceModulePrivilege> modulePrivileges = serviceModuleProvider.listServiceModulePrivilegesByPrivilegeId(webMenuPrivilege.getPrivilegeId(), null);
				for (ServiceModulePrivilege modulePrivilege: modulePrivileges) {
					moduleIds.add(modulePrivilege.getModuleId());
				}
			}

			// 获取个人的业务模块下的项目
			List<ServiceModuleAssignment> serviceModuleAssignments = serviceModuleProvider.listResourceAssignments(EntityType.USER.getCode(), user.getId(), cmd.getOrganizationId(), moduleIds);

			List<OrganizationDTO> orgDTOs = new ArrayList<>();

			// 没有，则获取个人所在公司节点的业务模块下的项目
			if(serviceModuleAssignments.size() == 0){
				orgDTOs.addAll(organizationService.getOrganizationMemberGroups(OrganizationGroupType.ENTERPRISE, user.getId(), cmd.getOrganizationId()));
				orgDTOs.addAll(organizationService.getOrganizationMemberGroups(OrganizationGroupType.DEPARTMENT, user.getId(), cmd.getOrganizationId()));
				orgDTOs.addAll(organizationService.getOrganizationMemberGroups(OrganizationGroupType.GROUP, user.getId(), cmd.getOrganizationId()));
				List<Long> targetIds = new ArrayList<>();
				for (OrganizationDTO orgDTO: orgDTOs) {
					targetIds.add(orgDTO.getId());
				}
				if(targetIds.size() > 0){
					serviceModuleAssignments = serviceModuleProvider.listResourceAssignments(EntityType.ORGANIZATIONS.getCode(), targetIds, cmd.getOrganizationId(), moduleIds);
				}
			}

			for (ServiceModuleAssignment serviceModuleAssignment: serviceModuleAssignments) {
				if(EntityType.fromCode(serviceModuleAssignment.getOwnerType()) == EntityType.COMMUNITY){
					ProjectDTO dto = new ProjectDTO();
					dto.setProjectId(serviceModuleAssignment.getOwnerId());
					dto.setProjectType(EntityType.COMMUNITY.getCode());
					Community community = communityProvider.findCommunityById(serviceModuleAssignment.getOwnerId());
					if(null != community){
						dto.setProjectName(community.getName());
					}
					projectDTOs.add(dto);
				}
			}
		}

		List<ProjectDTO> entityts = new ArrayList<>();
		List<Long> categoryIds = new ArrayList<>();
		if(0 != projectDTOs.size()){
			for (ProjectDTO project: projectDTOs) {
				ResourceCategoryAssignment categoryAssignment = communityProvider.findResourceCategoryAssignment(project.getProjectId(), project.getProjectType(),namespaceId);

				if(null != categoryAssignment){
					ResourceCategory category = communityProvider.findResourceCategoryById(categoryAssignment.getResourceCategryId());
					if(null != category && !StringUtils.isEmpty(category.getPath())){
						String[] idStrs = category.getPath().split("/");
						for (String idStr:idStrs) {
							if(!StringUtils.isEmpty(idStr) && !categoryIds.contains(Long.valueOf(idStr)))
								categoryIds.add(Long.valueOf(idStr));
						}
					}
				}else{
					entityts.add(project);
				}
			}
		}

		List<ProjectDTO> projects = new ArrayList<>();
		if(0 != categoryIds.size()){
			List<ProjectDTO> temp = communityProvider.listResourceCategory(cmd.getOwnerId(), cmd.getOwnerType(), categoryIds, ResourceCategoryType.CATEGORY.getCode())
					.stream().map(r -> {
						ProjectDTO dto = ConvertHelper.convert(r, ProjectDTO.class);
						dto.setProjectType(EntityType.RESOURCE_CATEGORY.getCode());
						dto.setProjectName(r.getName());
						dto.setProjectId(r.getId());
						return dto;
					}).collect(Collectors.toList());

			for(ProjectDTO project: temp) {
				getChildCategories(temp, project);
				if(project.getParentId() == 0L) {
					projects.add(project);
				}
			}
			setResourceDTOs(projects, namespaceId);
		}
		projects.addAll(entityts);
		return projects;
	}


	private ProjectDTO getChildCategories(List<ProjectDTO> list, ProjectDTO dto){

		List<ProjectDTO> childrens = new ArrayList<>();

		for (ProjectDTO rrojectDTO : list) {
			if(dto.getProjectId().equals(rrojectDTO.getParentId())){
				childrens.add(getChildCategories(list, rrojectDTO));
			}
		}
		dto.setProjects(childrens);

		return dto;
	}

	private void setResourceDTOs(List<ProjectDTO> list, Integer namespaceId){
		if(null != list) {
			for(ProjectDTO project: list) {
				List<ResourceCategoryAssignment> resourceCategoryAssignments = communityProvider.listResourceCategoryAssignment(project.getProjectId(), namespaceId);
				List<ProjectDTO> projects = resourceCategoryAssignments.stream().map(r -> {
					ProjectDTO dto = ConvertHelper.convert(r, ProjectDTO.class);
					dto.setProjectId(r.getResourceId());
					if(EntityType.COMMUNITY == EntityType.fromCode(r.getResourceType())){
						Community community = communityProvider.findCommunityById(r.getResourceId());
						if(community != null){
							dto.setProjectName(community.getName());
						}
					}
					dto.setProjectType(r.getResourceType());
					dto.setParentId(r.getResourceCategryId());
					return dto;
				}).collect(Collectors.toList());
				setResourceDTOs(project.getProjects(), namespaceId);
				if(null == project.getProjects()){
					project.setProjects(projects);
				}else{
					project.getProjects().addAll(projects);
				}

			}
		}

	}

	/**
	 * 删除权限
	 * @param resourceType
	 * @param resourceId
	 * @param targetType
	 * @param targetId
     */
	@Override
	public void deleteAcls(String resourceType, Long resourceId, String targetType, Long targetId, Long moduleId, List<Long> privilegeIds){
		if(null != moduleId){
			List<ServiceModulePrivilege> privileges = serviceModuleProvider.listServiceModulePrivileges(moduleId, ServiceModulePrivilegeType.SUPER);
			if(null == privilegeIds){
				privilegeIds = new ArrayList<>();
			}
			for (ServiceModulePrivilege privilege: privileges) {
				privilegeIds.add(privilege.getPrivilegeId());
			}
		}
		AclRoleDescriptor descriptor = new AclRoleDescriptor(targetType, targetId);
		List<Acl> acls = aclProvider.getResourceAclByRole(resourceType,resourceId, descriptor);
		if(null != acls){
			for (Acl acl :acls) {
				if(null == privilegeIds){
					aclProvider.deleteAcl(acl.getId());
				}else{
					if(privilegeIds.contains(acl.getPrivilegeId())){
						aclProvider.deleteAcl(acl.getId());
					}
				}

			}
		}
	}


	private void deleteAcls(String resourceType, Long resourceId, String targetType, Long targetId){
		deleteAcls(resourceType, resourceId, targetType, targetId, null, null);
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
		List<Long> privilegeIds = new ArrayList<>();
		privilegeIds.addAll(null);
	}
}
