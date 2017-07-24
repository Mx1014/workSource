package com.everhomes.acl;

import com.everhomes.community.Community;
import com.everhomes.community.CommunityProvider;
import com.everhomes.community.ResourceCategory;
import com.everhomes.community.ResourceCategoryAssignment;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.db.DbProvider;
import com.everhomes.db.QueryBuilder;
import com.everhomes.entity.EntityType;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.module.*;
import com.everhomes.organization.*;
import com.everhomes.payment.util.DownloadUtil;
import com.everhomes.rest.acl.*;
import com.everhomes.rest.acl.admin.*;
import com.everhomes.rest.address.CommunityDTO;
import com.everhomes.rest.app.AppConstants;
import com.everhomes.rest.common.AllFlagType;
import com.everhomes.rest.community.ResourceCategoryType;
import com.everhomes.rest.module.AssignmentTarget;
import com.everhomes.rest.module.Project;
import com.everhomes.rest.organization.*;
import com.everhomes.rest.organization.pm.PmMemberTargetType;
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
import org.apache.xmlbeans.UserType;
import org.jooq.Condition;
import org.jooq.Record;
import org.jooq.SelectQuery;
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
	private PrivilegeProvider privilegeProvider;

	@Autowired
	private AuthorizationProvider authorizationProvider;

	@Autowired
	private WebMenuPrivilegeProvider webMenuPrivilegeProvider;

	@Autowired
	private OrganizationProvider organizationProvider;

	@Autowired
	private OrganizationService organizationService;

	@Autowired
	private UserProvider userProvider;

	@Autowired
	private AclProvider aclProvider;

	@Autowired
	private ServiceModuleProvider serviceModuleProvider;

	@Autowired
	private CommunityProvider communityProvider;

	@Autowired
	private ConfigurationProvider configProvider;

	@Autowired
	private ServiceModuleService serviceModuleService;

	@Autowired
	private AclPrivilegeProvider aclPrivilegeProvider;


	@Override
	public ListWebMenuResponse listWebMenu(ListWebMenuCommand cmd) {
		User user = UserContext.current().getUser();

		Integer namespaceId = UserContext.getCurrentNamespaceId(cmd.getNamespaceId());

		ListWebMenuResponse res = new ListWebMenuResponse();

		//获取用户在机构范围内的所有权限
		List<Long> privilegeIds = new ArrayList<>();

		List<Long> ids = this.getUserPrivileges(null, cmd.getOrganizationId(), user.getId());
		LOGGER.info("Get user privilegeIds={}", StringHelper.toJsonString(ids));

		if(null != ids){
			privilegeIds.addAll(ids);
		}
		ids = this.getAllResourcePrivilegeIds(cmd.getOrganizationId(), user.getId());
		LOGGER.info("Get All resource privilegeIds={}", StringHelper.toJsonString(ids));

		if(null != ids){
			privilegeIds.addAll(ids);
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
	public void createRole(CreateRoleCommand cmd) {
		Integer namespaceId = UserContext.getCurrentNamespaceId();
		User user = UserContext.current().getUser();
		//创建角色
		Role role = new Role();
		role.setAppId(AppConstants.APPID_PARK_ADMIN);
		role.setName(cmd.getRoleName());
		role.setDescription(cmd.getDescription());
		role.setNamespaceId(namespaceId);
		role.setOwnerType(cmd.getOwnerType());
		role.setOwnerId(cmd.getOwnerId());
		role.setCreatorUid(user.getId());
		role.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
		aclProvider.createRole(role);
	}

	@Override
	public void updateRole(UpdateRoleCommand cmd) {

		//修改角色信息
		Role role = checkRole(cmd.getRoleId());
		role.setName(cmd.getRoleName());
		role.setDescription(cmd.getDescription());
		aclProvider.updateRole(role);

	}

	@Override
	public void createRolePrivileges(CreateRolePrivilegesCommand cmd) {
		User user = UserContext.current().getUser();
		Integer namespaceId = UserContext.getCurrentNamespaceId();
		dbProvider.execute((TransactionStatus status) -> {
			Timestamp time = new Timestamp(DateHelper.currentGMTTime().getTime());

			//创建角色
			Role role = new Role();
			role.setAppId(AppConstants.APPID_PARK_ADMIN);
			role.setName(cmd.getRoleName());
			role.setDescription(cmd.getDescription());
			role.setNamespaceId(namespaceId);
			role.setOwnerType(cmd.getOwnerType());
			role.setOwnerId(cmd.getOwnerId());
			aclProvider.createRole(role);

			//添加角色和权限的关系
			List<Long> privilegeIds = cmd.getPrivilegeIds();
			if(null != privilegeIds && 0 != privilegeIds.size()){
				Acl acl = new Acl();
				acl.setGrantType((byte) 1);
				acl.setOwnerType(cmd.getOwnerType());
				acl.setOwnerId(cmd.getOwnerId());
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
	public void updateRolePrivileges(UpdateRolePrivilegesCommand cmd) {

		checkRole(cmd.getRoleId());

		User user = UserContext.current().getUser();
		dbProvider.execute((TransactionStatus status) -> {
			Timestamp time = new Timestamp(DateHelper.currentGMTTime().getTime());
//			//修改角色信息
//			Role role = aclProvider.getRoleById(cmd.getRoleId());
//			role.setName(cmd.getRoleName());
//			role.setDescription(cmd.getDescription());
//			aclProvider.updateRole(role);

			//删除角色的权限
			deleteAcls(cmd.getOwnerType(), cmd.getOwnerId(), EntityType.ROLE.getCode(), cmd.getRoleId());

			//重新添加角色和权限的关系
			List<Long> privilegeIds = cmd.getPrivilegeIds();
			if(null != privilegeIds && 0 != privilegeIds.size()){
				Acl acl = new Acl();
				acl.setGrantType((byte) 1);
				acl.setOwnerType(cmd.getOwnerType());
				acl.setOwnerId(cmd.getOwnerId());
				acl.setOrderSeq(0);
				acl.setRoleType(EntityType.ROLE.getCode());
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
	public void deleteRolePrivileges(DeleteRolePrivilegesCommand cmd) {

		checkRole(cmd.getRoleId());

		dbProvider.execute((TransactionStatus status) -> {
			//删除角色
			aclProvider.deleteRole(cmd.getRoleId());
			//删除角色权限
			deleteAcls(cmd.getOwnerType(), cmd.getOwnerId(), EntityType.ROLE.getCode(), cmd.getRoleId());
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

	private Role checkRole(Long roleId){
		Role role = aclProvider.getRoleById(roleId);
		if(null == role){
			LOGGER.error("Role Non-existent., roleId = {}", roleId);
			throw RuntimeErrorException.errorWith(OrganizationServiceErrorCode.SCOPE, OrganizationServiceErrorCode.ERROR_INVALID_PARAMETER,
					"Role Non-existent.");
		}

		return role;
	}

	@Override
	public List<RoleDTO> listRoles(ListRolesCommand cmd) {
		Integer namespaceId = UserContext.getCurrentNamespaceId(cmd.getNamespaceId());
		List<Role> roles = privilegeProvider.getRolesByOwnerAndKeywords(namespaceId, AppConstants.APPID_PARK_ADMIN, cmd.getOwnerType(), cmd.getOwnerId(), cmd.getKeywords());

		return roles.stream().map(r->{
			RoleDTO role = ConvertHelper.convert(r, RoleDTO.class);
			if(r.getCreateTime() != null) {
				role.setCreateTime(r.getCreateTime().getTime());
			}
			if(r.getCreatorUid() != null) {
				User user = userProvider.findUserById(r.getCreatorUid());
				if(null != user){
					role.setCreatorUName(user.getNickName());
				}
			}
			return ConvertHelper.convert(r, RoleDTO.class);
		}).collect(Collectors.toList());
	}

	@Override
	public List<Long> getPrivilegeIdsByRoleId(ListPrivilegesByRoleIdCommand cmd) {

		checkRole(cmd.getRoleId());

		List<Acl> acls = aclProvider.getResourceAclByRole(cmd.getOwnerType(), cmd.getOwnerId(), new AclRoleDescriptor(EntityType.ROLE.getCode(), cmd.getRoleId()));

		return acls.stream().map(r->{
			return r.getPrivilegeId();
		}).collect(Collectors.toList());
	}

	//added by janson
	@Override
	public AclPrivilegeInfoResponse getPrivilegeInfosByRoleId(ListPrivilegesByRoleIdCommand cmd) {
		checkRole(cmd.getRoleId());

		List<Acl> acls = aclProvider.getResourceAclByRole(cmd.getOwnerType(), cmd.getOwnerId(), new AclRoleDescriptor(EntityType.ROLE.getCode(), cmd.getRoleId()));
		AclPrivilegeInfoResponse resp = new AclPrivilegeInfoResponse();

		List<AclPrivilegeInfo> infos = acls.stream().map(r->{
			AclPrivilegeInfo info = new AclPrivilegeInfo();
			info.setPrivilegeId(r.getPrivilegeId());
			info.setRoleId(r.getRoleId());
//			AclPrivilege privilege = aclPrivilegeProvider.getAclPrivilegeById(r.getPrivilegeId());
//			if(privilege != null) {
//				info.setPrivilegeName(privilege.getName());
//			}
			List<ServiceModulePrivilege> mps = serviceModuleProvider.listServiceModulePrivilegesByPrivilegeId(r.getPrivilegeId(), null);
			if(mps != null) {
				List<ServiceModulePrivilegeDTO> mpDTOS = mps.stream().map(rr->{
					return ConvertHelper.convert(rr, ServiceModulePrivilegeDTO.class);
				}).collect(Collectors.toList());

				info.setModulePrivileges(mpDTOS);
			}
			return info;
		}).collect(Collectors.toList());

		resp.setPrivileges(infos);
		return resp;
	}

	@Override
	public void createOrganizationSuperAdmin(CreateOrganizationAdminCommand cmd){

		Integer namespaceId = UserContext.getCurrentNamespaceId();
		User user = UserContext.current().getUser();

		Organization org = organizationProvider.findOrganizationById(cmd.getOrganizationId());
		Long roleId = RoleConstants.PM_SUPER_ADMIN;

		CreateOrganizationAccountCommand command = new CreateOrganizationAccountCommand();
		command.setOrganizationId(org.getId());
		command.setAccountName(cmd.getContactName());
		command.setAccountPhone(cmd.getContactToken());
		organizationService.createOrganizationAccount(command, roleId);

		UserIdentifier userIdentifier = this.userProvider.findClaimedIdentifierByToken(namespaceId, cmd.getContactToken());


		/**
		 * 分配权限
		 */
		this.assignmentPrivileges(EntityType.ORGANIZATIONS.getCode(),org.getId(),EntityType.USER.getCode(),userIdentifier.getOwnerUid(),"admin",PrivilegeConstants.ORGANIZATION_SUPER_ADMIN);

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

	@Override
	public List<Long> listUserRelatedPrivilegeByModuleId(ListUserRelatedPrivilegeByModuleIdCommand cmd){
		return listUserPrivilegeByModuleId(cmd.getOwnerType(), cmd.getOwnerId(), cmd.getOrganizationId(), UserContext.current().getUser().getId(), cmd.getModuleId());
	}


	@Override
	public List<Long> listUserPrivilegeByModuleId(String ownerType, Long ownerId, Long organizationId, Long userId, Long moduleId){
		List<Long> privilegeIds = new ArrayList<>();
		if(null != organizationId){
			privilegeIds.addAll(getUserPrivileges(null ,organizationId, userId));

			// 用户在当前机构自身权限
			privilegeIds.addAll(this.getResourceAclPrivilegeIds(EntityType.ORGANIZATIONS.getCode(), organizationId, EntityType.USER.getCode(), userId));

		}

		//如果ownerType和ownerId没传，则默认返回用户所有的项目模块权限 add by sfyan 20170329
		if(null == EntityType.fromCode(ownerType) || null == ownerId){
			privilegeIds.addAll(getUserModulePrivilegeByAllProject(EntityType.USER.getCode(), userId, moduleId));
		}else{
			privilegeIds.addAll(getResourceAclPrivilegeIds(ownerType, ownerId, EntityType.USER.getCode(), userId));

		}

		//如果个人没有权限，就去找他的所属机构节点的权限 add by sfyan 20170329
		if(null == privilegeIds || privilegeIds.size() == 0){
			if(null != organizationId){
				List<String> groupTypes = new ArrayList<>();
				groupTypes.add(OrganizationGroupType.DEPARTMENT.getCode());
				groupTypes.add(OrganizationGroupType.GROUP.getCode());
				List<OrganizationDTO> organizations = organizationService.getOrganizationMemberGroups(groupTypes, userId, organizationId);

				//如果ownerType和ownerId没传，则默认返回机构所有的项目模块权限 add by sfyan 20170329
				if(null == EntityType.fromCode(ownerType) || null == ownerId){
					for (OrganizationDTO organization:organizations) {
						privilegeIds.addAll(getUserModulePrivilegeByAllProject(EntityType.ORGANIZATIONS.getCode(), organization.getId(), moduleId));
					}
					//所属根节点的权限也要加进去 add by sfyan 20170329
					privilegeIds.addAll(getUserModulePrivilegeByAllProject(EntityType.ORGANIZATIONS.getCode(), organizationId, moduleId));

				}else{
					for (OrganizationDTO organization:organizations) {
						privilegeIds.addAll(this.getResourceAclPrivilegeIds(ownerType, ownerId, EntityType.ORGANIZATIONS.getCode(), organization.getId()));
					}
				}

			}
		}


		List<Long> pIds = new ArrayList<>();

		//获取的权限当中有模块的超管权限，就把模块下面所有的权限都返回 add by sfyan 20170329
		List<ServiceModulePrivilege> moduleSuperPrivileges = serviceModuleProvider.listServiceModulePrivileges(moduleId, ServiceModulePrivilegeType.SUPER);
		for (ServiceModulePrivilege moduleSuperPrivilege:  moduleSuperPrivileges) {
			if(privilegeIds.contains(moduleSuperPrivilege.getPrivilegeId())){
				List<ServiceModulePrivilege> modulePrivileges = serviceModuleProvider.listServiceModulePrivileges(moduleId, null);
				for (ServiceModulePrivilege modulePrivilege: modulePrivileges) {
					pIds.add(modulePrivilege.getPrivilegeId());
				}
				return pIds;
			}
		}

		//从拥有的权限里面筛选出模块权限返回  add by sfyan 20170329
		List<ServiceModulePrivilege> modulePrivileges = serviceModuleProvider.listServiceModulePrivileges(moduleId, ServiceModulePrivilegeType.ORDINARY);
		for (ServiceModulePrivilege modulePrivilege:  modulePrivileges) {
			if(privilegeIds.contains(modulePrivilege.getPrivilegeId())){
				pIds.add(modulePrivilege.getPrivilegeId());
			}
		}

		return pIds;
	}

	private List<Long> getUserModulePrivilegeByAllProject(String targetType, Long targetId, Long moduleId){
		List<Long> privilegeIds = new ArrayList<>();
		List<Long> moduleIds = new ArrayList<>();
		moduleIds.add(moduleId);
		List<ServiceModuleAssignment> assignments = serviceModuleProvider.listResourceAssignments(targetType, targetId, null, moduleIds);
		for (ServiceModuleAssignment assignment: assignments) {
			if(EntityType.COMMUNITY == EntityType.fromCode(assignment.getOwnerType())){
				privilegeIds.addAll(getResourceAclPrivilegeIds(assignment.getOwnerType(), assignment.getOwnerId(), targetType, targetId));
			}
		}
		return privilegeIds;
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
			if(RoleConstants.BLACKLIST != role.getRoleId()){
				roleIds.add(role.getRoleId());
			}
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
		LOGGER.info("Get ORGANIZATIONS ownerId={}, roleId={}, privilegeIds={}", organizationId, userId, StringHelper.toJsonString(ids));

		if(null != ids){
			privilegeIds.addAll(ids);
		}
		for (CommunityDTO communityDTO:communityDTOs) {
			List<Long> pIds = this.getResourceAclPrivilegeIds(EntityType.COMMUNITY.getCode(), communityDTO.getId(), EntityType.USER.getCode(), userId);
			LOGGER.info("Get COMMUNITY ownerId={}, roleId={}, privilegeIds={}", communityDTO.getId(), userId, StringHelper.toJsonString(ids));

			if(null != pIds){
				privilegeIds.addAll(pIds);
			}
			if(pIds.size() == 0){
				for (OrganizationDTO dto: organizationDTOs) {
					ids = this.getResourceAclPrivilegeIds(EntityType.COMMUNITY.getCode(), communityDTO.getId(), EntityType.ORGANIZATIONS.getCode(), dto.getId());
					LOGGER.info("Get ORGANIZATIONS ownerId={}, roleId={}, privilegeIds={}", communityDTO.getId(), dto.getId(), StringHelper.toJsonString(ids));

					if(null != ids){
						privilegeIds.addAll(ids);
					}
				}
				ids = this.getResourceAclPrivilegeIds(EntityType.COMMUNITY.getCode(), communityDTO.getId(), EntityType.ORGANIZATIONS.getCode(), organizationId);
				LOGGER.info("Get ORGANIZATIONS ownerId={}, roleId={}, privilegeIds={}", communityDTO.getId(), organizationId, StringHelper.toJsonString(ids));

				if(null != ids){
					privilegeIds.addAll(ids);
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
				dto.setEmployeeNo(r.getA());
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
		if(null == org){
			LOGGER.debug("organization is null. organizationId = {}", organizationId);
			return new ArrayList<>();
		}
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
		namespaceId = UserContext.getCurrentNamespaceId();
		Organization org = organizationProvider.findOrganizationById(cmd.getOrganizationId());
		Long roleId = RoleConstants.ENTERPRISE_SUPER_ADMIN;

		CreateOrganizationAccountCommand command = new CreateOrganizationAccountCommand();
		command.setOrganizationId(org.getId());
		command.setAccountName(cmd.getContactName());
		command.setAccountPhone(cmd.getContactToken());
		//创建管理员不再返回member
		organizationService.createOrganizationAccount(command, roleId, namespaceId);


		UserIdentifier userIdentifier = userProvider.findClaimedIdentifierByToken(namespaceId, cmd.getContactToken());


		/**
		 * 分配权限
		 */
		this.assignmentPrivileges(EntityType.ORGANIZATIONS.getCode(),org.getId(),EntityType.USER.getCode(),userIdentifier.getOwnerUid(),"admin",PrivilegeConstants.ORGANIZATION_ADMIN);
	}

	@Override
	public void createOrganizationAdmin(CreateOrganizationAdminCommand cmd){
		createOrganizationAdmin(cmd, null);
	}

	@Override
	public void assignmentPrivileges(String ownerType, Long ownerId,String targetType, Long targetId, String scope,  Long privilegeId){
		List<Long> privilegeIds = new ArrayList<>();
		privilegeIds.add(privilegeId);
		assignmentPrivileges(ownerType, ownerId, targetType, targetId, scope, privilegeIds, null);
	}

	@Override
	public void assignmentPrivileges(String ownerType, Long ownerId,String targetType, Long targetId, String scope,  List<Long> privilegeIds){
		assignmentPrivileges(ownerType, ownerId, targetType, targetId, scope, privilegeIds, null);

	}

	@Override
	public void assignmentPrivileges(String ownerType, Long ownerId,String targetType, Long targetId, String scope,  List<Long> privilegeIds, String tag){
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
				acl.setScope(scope);
				acl.setCommentTag1(tag);
				acl.setNamespaceId(UserContext.getCurrentNamespaceId());
				aclProvider.createAcl(acl);
			}
		}else{
			LOGGER.debug("assignment privileges is null");
		}
	}

	private void assignmentModulePrivileges(String ownerType, Long ownerId,String targetType, Long targetId, String scope, List<Long> moduleIds, ServiceModulePrivilegeType privilegeType){
		assignmentModulePrivileges(ownerType, ownerId, targetType, targetId, scope, moduleIds, privilegeType, null);
	}
	private void assignmentModulePrivileges(String ownerType, Long ownerId,String targetType, Long targetId, String scope, List<Long> moduleIds, ServiceModulePrivilegeType privilegeType, String tag){
		List<ServiceModulePrivilege> serviceModulePrivileges = serviceModuleProvider.listServiceModulePrivileges(moduleIds, privilegeType);
		List<Long> privilegeIds = new ArrayList<>();
		for (ServiceModulePrivilege serviceModulePrivilege: serviceModulePrivileges) {
			privilegeIds.add(serviceModulePrivilege.getPrivilegeId());
		}

		this.assignmentPrivileges(ownerType, ownerId, targetType, targetId, scope, privilegeIds, tag);
	}

	public void assignmentPrivileges(String ownerType, Long ownerId,String targetType, Long targetId, String scope, Long moduleId, ServiceModulePrivilegeType privilegeType){
		this.assignmentPrivileges(ownerType, ownerId, targetType, targetId, scope, moduleId, privilegeType, null);
	}

	public void assignmentPrivileges(String ownerType, Long ownerId,String targetType, Long targetId, String scope, Long moduleId, ServiceModulePrivilegeType privilegeType, String tag){
		List<Long> moduleIds = new ArrayList<>();

		if(0L == moduleId){
			ListServiceModulesCommand command = new ListServiceModulesCommand();
			List<ServiceModuleDTO> modules = serviceModuleService.listServiceModules(command);
			for (ServiceModuleDTO module: modules) {
				moduleIds.add(module.getId());
			}
		}else{
			moduleIds.add(moduleId);
		}
		this.assignmentModulePrivileges(ownerType, ownerId, targetType, targetId, scope, moduleIds, privilegeType, tag);
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
		return this.getRoleMembers(cmd.getOrganizationId(), RoleConstants.PM_SUPER_ADMIN);
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
	private List<OrganizationContactDTO> getRoleMembers(Long organizationId, Long roleId){
		List<OrganizationContactDTO> dtos = new ArrayList<>();
		List<RoleAssignment> roleAssignments = aclProvider.getRoleAssignmentByResource(EntityType.ORGANIZATIONS.getCode(), organizationId);
		if(null != roleAssignments){
			for (RoleAssignment roleassignment: roleAssignments) {
				if(EntityType.fromCode(roleassignment.getTargetType()) == EntityType.USER && roleassignment.getRoleId().equals(roleId)){
					OrganizationContactDTO dto = new OrganizationContactDTO();
					UserIdentifier userIdentifier = userProvider.findClaimedIdentifierByOwnerAndType(roleassignment.getTargetId(), IdentifierType.MOBILE.getCode());
					User user = userProvider.findUserById(roleassignment.getTargetId());
					OrganizationMember member = organizationProvider.findOrganizationMemberByOrgIdAndUId(roleassignment.getTargetId(), organizationId);
					if(user != null){
						dto.setId(user.getId());
						dto.setNickName(user.getNickName());
						dto.setGender(user.getGender());
						dto.setTargetId(user.getId());
					}
					if(userIdentifier != null){
						dto.setContactToken(userIdentifier.getIdentifierToken());
					}
					if(member != null){
						dto.setContactName(member.getContactName());
						dto.setContactToken(member.getContactToken());
						dto.setTargetType(PmMemberTargetType.USER.getCode());
					}
					dtos.add(dto);
				}
			}
		}
		if(dtos.size() > 0)
			return dtos;
		return null;
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
						User user = this.userProvider.findUserById(member.getTargetId());
						if(user != null){
							member.setNickName(user.getNickName());
						}
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
		deleteAcls(EntityType.ORGANIZATIONS.getCode(), cmd.getOrganizationId(), EntityType.USER.getCode(), cmd.getUserId(), privilegeIds);


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
		deleteAcls(EntityType.ORGANIZATIONS.getCode(), cmd.getOrganizationId(), EntityType.USER.getCode(), cmd.getUserId(), privilegeIds);

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
				ListServiceModulesCommand command = new ListServiceModulesCommand();
				command.setOwnerType(EntityType.ORGANIZATIONS.getCode());
				command.setOwnerId(cmd.getOrganizationId());
				List<ServiceModuleDTO> modules = serviceModuleService.listServiceModules(command);
				List<Long> moduleIds = new ArrayList<Long>();
				for (ServiceModuleDTO module: modules) {
					moduleIds.add(module.getId());
				}

				// 删除范围的权限
				deleteAcls(authorizationServiceModule.getResourceType(), authorizationServiceModule.getResourceId(),cmd.getTargetType(),cmd.getTargetId(), moduleIds, null);

				//业务模块授权
				if(0 == authorizationServiceModule.getAllModuleFlag()){
					if(null != authorizationServiceModule.getAssignments()){
						for (ModuleAssignment moduleAssignment: authorizationServiceModule.getAssignments()) {
							ServiceModuleAssignment assignment = ConvertHelper.convert(moduleAssignment, ServiceModuleAssignment.class);
							assignment.setOrganizationId(cmd.getOrganizationId());
							assignment.setNamespaceId(namespaceId);
							assignment.setTargetType(cmd.getTargetType());
							assignment.setTargetId(cmd.getTargetId());
							assignment.setOwnerType(authorizationServiceModule.getResourceType());
							assignment.setOwnerId(authorizationServiceModule.getResourceId());
							assignment.setCreateUid(user.getId());
							serviceModuleProvider.createServiceModuleAssignment(assignment);

//							if(EntityType.fromCode(authorizationServiceModule.getResourceType()) == EntityType.RESOURCE_CATEGORY){
//								List<ResourceCategoryAssignment> buildingAssignments = communityProvider.listResourceCategoryAssignment(authorizationServiceModule.getResourceId(), namespaceId);
//								for (ResourceCategoryAssignment buildingAssignment: buildingAssignments) {
//									if(ServiceModuleAssignmentType.fromCode(moduleAssignment.getAssignmentType()) == ServiceModuleAssignmentType.PORTION
//											&& null != moduleAssignment.getPrivilegeIds() && moduleAssignment.getPrivilegeIds().size() > 0)
//										this.assignmentPrivileges(buildingAssignment.getResourceType(),buildingAssignment.getResourceId(),assignment.getTargetType(),assignment.getTargetId(),"M" + assignment.getModuleId() + "." + authorizationServiceModule.getResourceType() + authorizationServiceModule.getResourceId(), moduleAssignment.getPrivilegeIds());
//									else
//										this.assignmentPrivileges(buildingAssignment.getResourceType(),buildingAssignment.getResourceId(),assignment.getTargetType(),assignment.getTargetId(),"M" + assignment.getModuleId() + "." + authorizationServiceModule.getResourceType() + authorizationServiceModule.getResourceId(), assignment.getModuleId(),ServiceModulePrivilegeType.SUPER);
//
//								}
//
//							}else{
							if(ServiceModuleAssignmentType.fromCode(moduleAssignment.getAssignmentType()) == ServiceModuleAssignmentType.PORTION
									&& null != moduleAssignment.getPrivilegeIds() && moduleAssignment.getPrivilegeIds().size() > 0)
								this.assignmentPrivileges(assignment.getOwnerType(),assignment.getOwnerId(),assignment.getTargetType(),assignment.getTargetId(),assignment.getOwnerType() +  assignment.getOwnerId() + ".M" + assignment.getModuleId(), moduleAssignment.getPrivilegeIds());
							else
								this.assignmentPrivileges(assignment.getOwnerType(),assignment.getOwnerId(),assignment.getTargetType(),assignment.getTargetId(),assignment.getOwnerType() +  assignment.getOwnerId() + ".M" + assignment.getModuleId(), assignment.getModuleId(),ServiceModulePrivilegeType.SUPER);

//							}
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
//					if(EntityType.fromCode(authorizationServiceModule.getResourceType()) == EntityType.RESOURCE_CATEGORY){
//						List<ResourceCategoryAssignment> buildingAssignments = communityProvider.listResourceCategoryAssignment(authorizationServiceModule.getResourceId(), namespaceId);
//						for (ResourceCategoryAssignment buildingAssignment: buildingAssignments) {
//							this.assignmentPrivileges(buildingAssignment.getResourceType(),buildingAssignment.getResourceId(),assignment.getTargetType(),assignment.getTargetId(),"M" + assignment.getModuleId() + "." + authorizationServiceModule.getResourceType() + authorizationServiceModule.getResourceId(), moduleIds,ServiceModulePrivilegeType.SUPER);
//						}
//					}else{
					this.assignmentModulePrivileges(assignment.getOwnerType(),assignment.getOwnerId(),assignment.getTargetType(),assignment.getTargetId(),assignment.getOwnerType() +  assignment.getOwnerId() + ".M" + assignment.getModuleId(), moduleIds,ServiceModulePrivilegeType.SUPER);
//					}
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
		List<ServiceModuleAssignment> assignments = serviceModuleProvider.listServiceModuleAssignments(condition);
		String key = "";
		AuthorizationServiceModuleDTO dto = null;
		for (ServiceModuleAssignment assignment: assignments) {
			if(key.equals(assignment.getOwnerType() + assignment.getOwnerId())){
				dto.getAssignments().add(convertServiceModuleAssignmentDTO(assignment));
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
				dto.setAssignments(new ArrayList<>());
				if(0L == assignment.getModuleId()){
					dto.setAllModuleFlag((byte)1);
				}else{
					dto.getAssignments().add(convertServiceModuleAssignmentDTO(assignment));
				}
				dtos.add(dto);
				key = assignment.getOwnerType() + assignment.getOwnerId(); // 拼装Key
			}
		}
		return dtos;
	}

	private ServiceModuleAssignmentDTO convertServiceModuleAssignmentDTO(ServiceModuleAssignment assignment){
		ServiceModule serviceModule = serviceModuleProvider.findServiceModuleById(assignment.getModuleId());
		ServiceModuleAssignmentDTO assignmentDTO = ConvertHelper.convert(assignment, ServiceModuleAssignmentDTO.class);
		assignmentDTO.setModuleId(assignment.getModuleId());
		if(null != serviceModule)
			assignmentDTO.setModuleName(serviceModule.getName());

		if(ServiceModuleAssignmentType.PORTION == ServiceModuleAssignmentType.fromCode(assignment.getAssignmentType())){
			List<Acl> acls = aclProvider.getAcl(new QueryBuilder() {
				@Override
				public SelectQuery<? extends Record> buildCondition(SelectQuery<? extends Record> selectQuery) {
//					if(EntityType.fromCode(assignment.getOwnerType()) == EntityType.RESOURCE_CATEGORY){
//						selectQuery.addConditions(com.everhomes.schema.Tables.EH_ACLS.SCOPE.like("%.M" + assignment.getModuleId() + "." + assignment.getOwnerType() + assignment.getOwnerId()).or(com.everhomes.schema.Tables.EH_ACLS.SCOPE.like(assignment.getTargetType() + assignment.getTargetId() + ".M0" + "%")));
//					}else{
					selectQuery.addConditions(com.everhomes.schema.Tables.EH_ACLS.SCOPE.like(assignment.getOwnerType() + assignment.getOwnerId() + ".M" + assignment.getModuleId() + "%").or(com.everhomes.schema.Tables.EH_ACLS.SCOPE.like(assignment.getOwnerType() + assignment.getOwnerId() + ".M0" + "%")));
					selectQuery.addConditions(com.everhomes.schema.Tables.EH_ACLS.ROLE_TYPE.eq(assignment.getTargetType()));
					selectQuery.addConditions(com.everhomes.schema.Tables.EH_ACLS.ROLE_ID.eq(assignment.getTargetId()));
//					}
					return null;
				}
			});

			List<PrivilegeDTO> ps = new ArrayList<>();
			List<Long> pIds = new ArrayList<>();
			for (Acl acl: acls) {
				if(!pIds.contains(acl.getPrivilegeId())){
					Privilege p = aclProvider.getPrivilegeById(acl.getPrivilegeId());
					PrivilegeDTO pDTO = new PrivilegeDTO();
					pDTO.setPrivilegeId(acl.getPrivilegeId());
					if(null != p){
						pDTO.setPrivilegeName(p.getName());
					}
					ps.add(pDTO);
					pIds.add(acl.getPrivilegeId());
				}

			}
			assignmentDTO.setPrivileges(ps);
		}

		return assignmentDTO;
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
							List<ServiceModuleAssignmentDTO> serviceModuleAssignmentDTO = new ArrayList<>();
							for (ServiceModuleAssignment userAssignment:userAssignments) {
								if(0L == userAssignment.getModuleId()){
									authorizationDTO.setAllModuleFlag((byte)1); // 加入moduleId是0 代表全部业务
									break;
								}else{
									authorizationDTO.setAllModuleFlag((byte)0);
								}
								serviceModuleAssignmentDTO.add(convertServiceModuleAssignmentDTO(userAssignment));
							}
							authorizationDTO.setAssignments(serviceModuleAssignmentDTO);
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
			ListServiceModulesCommand command = new ListServiceModulesCommand();
			command.setOwnerType(EntityType.ORGANIZATIONS.getCode());
			command.setOwnerId(cmd.getOrganizationId());
			List<ServiceModuleDTO> modules = serviceModuleService.listServiceModules(command);
			List<Long> moduleIds = new ArrayList<Long>();
			for (ServiceModuleDTO module: modules) {
				moduleIds.add(module.getId());
			}
			deleteAcls(cmd.getResourceType(), cmd.getResourceId(),EntityType.ORGANIZATIONS.getCode(),cmd.getOrganizationId(), moduleIds, null);

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
					deleteAcls(cmd.getResourceType(), cmd.getResourceId(),EntityType.ORGANIZATIONS.getCode(),cmd.getOrganizationId(), moduleIds, null);				}
			}
			return null;
		});

	}

	@Override
	public List<CommunityDTO> listUserRelatedProjectByModuleId(ListUserRelatedProjectByModuleIdCommand cmd) {

		Long startTime1 = System.currentTimeMillis();
		User user = UserContext.current().getUser();

		List<CommunityDTO> communitydtos = new ArrayList<>();

		Long startTime2 = System.currentTimeMillis();
		// 用户的角色以及用户所在部门角色的所有权限
		List<Long> privilegeIds = this.getUserPrivileges(null, cmd.getOrganizationId(), user.getId());

		// 用户在当前机构自身权限
		privilegeIds.addAll(this.getResourceAclPrivilegeIds(EntityType.ORGANIZATIONS.getCode(), cmd.getOrganizationId(), EntityType.USER.getCode(), user.getId()));

		Long endTime2 = System.currentTimeMillis();
		List<ServiceModulePrivilege> serviceModulePrivileges = serviceModuleProvider.listServiceModulePrivileges(cmd.getModuleId(), ServiceModulePrivilegeType.SUPER);

		if(privilegeIds.contains(serviceModulePrivileges.get(0).getPrivilegeId())) {
			communitydtos = organizationService.listAllChildrenOrganizationCoummunities(cmd.getOrganizationId());
			return communitydtos;
		}

		List<Long> moduleIds = new ArrayList<>();
		moduleIds.add(0L);
		moduleIds.add(cmd.getModuleId());

		// 获取个人的业务模块下的项目
		List<ServiceModuleAssignment> serviceModuleAssignments = serviceModuleProvider.listResourceAssignments(EntityType.USER.getCode(), user.getId(), cmd.getOrganizationId(), moduleIds);

		List<OrganizationDTO> orgDTOs = new ArrayList<>();

		// 没有，则获取个人所在公司节点的业务模块下的项目

		Long startTime3 = System.currentTimeMillis();
		if(serviceModuleAssignments.size() == 0){
			List<String> groupTypes = new ArrayList<>();
			groupTypes.add(OrganizationGroupType.ENTERPRISE.getCode());
			groupTypes.add(OrganizationGroupType.DEPARTMENT.getCode());
			groupTypes.add(OrganizationGroupType.GROUP.getCode());
			orgDTOs.addAll(organizationService.getOrganizationMemberGroups(groupTypes, user.getId(), cmd.getOrganizationId()));
			List<Long> targetIds = new ArrayList<>();
			targetIds.add(cmd.getOrganizationId());
			for (OrganizationDTO orgDTO: orgDTOs) {
				targetIds.add(orgDTO.getId());
			}
			if(targetIds.size() > 0){
				serviceModuleAssignments = serviceModuleProvider.listResourceAssignments(EntityType.ORGANIZATIONS.getCode(), targetIds, null, moduleIds);
			}
		}
		Long endTime3 = System.currentTimeMillis();
		for (ServiceModuleAssignment serviceModuleAssignment: serviceModuleAssignments) {
			if(EntityType.fromCode(serviceModuleAssignment.getOwnerType()) == EntityType.COMMUNITY){
				Community community = communityProvider.findCommunityById(serviceModuleAssignment.getOwnerId());
				if(null != community){
					communitydtos.add(ConvertHelper.convert(community, CommunityDTO.class));
				}
			}
		}
		Long endTime1 = System.currentTimeMillis();
		if(LOGGER.isInfoEnabled()){
			LOGGER.debug("TrackUserRelatedCost:listUserRelatedProjectByModuleId: get privileges elapse:{}, get organization elapse:{}, total elapse:{}", endTime2 - startTime2, endTime3 - startTime3, endTime1 - startTime1);
		}
		return communitydtos;
	}

	@Override
	public List<ProjectDTO> listUserRelatedProjectByMenuId(ListUserRelatedProjectByMenuIdCommand cmd) {

		Long startTime1 = System.currentTimeMillis();
		User user = UserContext.current().getUser();
		Integer namespaceId = UserContext.getCurrentNamespaceId();
		List<WebMenuPrivilege> webMenuPrivileges = webMenuPrivilegeProvider.listWebMenuPrivilegeByMenuId(cmd.getMenuId());
		Long startTime2 = System.currentTimeMillis();
		// 用户的角色以及用户所在部门角色的所有权限
		List<Long> privilegeIds = this.getUserPrivileges(null, cmd.getOrganizationId(), user.getId());

		// 用户在当前机构自身权限
		privilegeIds.addAll(this.getResourceAclPrivilegeIds(EntityType.ORGANIZATIONS.getCode(), cmd.getOrganizationId(), EntityType.USER.getCode(), user.getId()));
		Long endTime2 = System.currentTimeMillis();
		List<CommunityDTO> communitydtos = organizationService.listAllChildrenOrganizationCoummunities(cmd.getOrganizationId());

		List<ProjectDTO> projectDTOs = new ArrayList<>();

		Long startTime3 = System.currentTimeMillis();
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
		Long endTime3 = System.currentTimeMillis();
		if(0 == projectDTOs.size()){
			List<Long> moduleIds = new ArrayList<>();
			moduleIds.add(0L);
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
				List<String> groupTypes = new ArrayList<>();
				groupTypes.add(OrganizationGroupType.ENTERPRISE.getCode());
				groupTypes.add(OrganizationGroupType.DEPARTMENT.getCode());
				groupTypes.add(OrganizationGroupType.GROUP.getCode());
				orgDTOs.addAll(organizationService.getOrganizationMemberGroups(groupTypes, user.getId(), cmd.getOrganizationId()));
				List<Long> targetIds = new ArrayList<>();
				targetIds.add(cmd.getOrganizationId());
				for (OrganizationDTO orgDTO: orgDTOs) {
					targetIds.add(orgDTO.getId());
				}
				if(targetIds.size() > 0){
					serviceModuleAssignments = serviceModuleProvider.listResourceAssignments(EntityType.ORGANIZATIONS.getCode(), targetIds, null, moduleIds);
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
		Long endTime1 = System.currentTimeMillis();
		if(LOGGER.isInfoEnabled()){
			LOGGER.debug("Track: listUserRelatedProjectByMenuId: get privileges:{}, webMenuPrivilege elapse:{}, get organization elapse:{}, total elapse:{}", endTime2 - startTime2, endTime3 - startTime3, endTime1 - startTime3, endTime1 - startTime1);
		}
		return getTreeProjectCategories(namespaceId, projectDTOs);
	}

	@Override
	public List<ProjectDTO> getTreeProjectCategories(Integer namespaceId, List<ProjectDTO> projects){
		Long startTime = System.currentTimeMillis();
		List<ProjectDTO> entityts = new ArrayList<>();
		List<Long> categoryIds = new ArrayList<>();
		List<Long> projectIds = new ArrayList<>();

		if(0 != projects.size()){
			for (ProjectDTO project: projects) {
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

				projectIds.add(project.getProjectId());
			}
		}
		List<ProjectDTO> projectTrees = new ArrayList<>();
		if(0 != categoryIds.size()){
			List<ProjectDTO> temp = communityProvider.listResourceCategory(null, null, categoryIds, ResourceCategoryType.CATEGORY.getCode())
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
					projectTrees.add(project);
				}
			}
			setResourceDTOs(projectTrees, projectIds, namespaceId);
		}
		projectTrees.addAll(entityts);

		Long endTime = System.currentTimeMillis();
		if(LOGGER.isInfoEnabled()){
			LOGGER.debug("Track: getProjectCategoryTree: get project category tree:{}", endTime - startTime);
		}

		return projectTrees;
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

	private void setResourceDTOs(List<ProjectDTO> list, List<Long> projectIds, Integer namespaceId){
		if(null != list) {
			for(ProjectDTO project: list) {
				List<ResourceCategoryAssignment> resourceCategoryAssignments = communityProvider.listResourceCategoryAssignment(project.getProjectId(), namespaceId, EntityType.COMMUNITY.getCode(), projectIds);
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
				setResourceDTOs(project.getProjects(), projectIds, namespaceId);
				if(null == project.getProjects()){
					project.setProjects(projects);
				}else{
					project.getProjects().addAll(projects);
				}

			}
		}

	}

	@Override
	public List<Privilege> listPrivilegesByTarget(String ownerType, Long ownerId, String targetType, Long targetId, String scope){
		List<Privilege> privileges = new ArrayList<>();
		AclRoleDescriptor descriptor = new AclRoleDescriptor(targetType, targetId);
		List<Acl> acls = aclProvider.getResourceAclByRole(ownerType,ownerId, descriptor);
		for (Acl acl: acls) {
			if(!StringUtils.isEmpty(scope)){
				if(acl.getScope().equals(scope)){
					Privilege privilege = aclProvider.getPrivilegeById(acl.getPrivilegeId());
					privileges.add(privilege);
				}
			}else{
				Privilege privilege = aclProvider.getPrivilegeById(acl.getPrivilegeId());
				privileges.add(privilege);
			}

		}
		return privileges;
	}


	/**
	 * 删除权限
	 * @param resourceType
	 * @param resourceId
	 * @param targetType
	 * @param targetId
	 */
	private void deleteAcls(String resourceType, Long resourceId, String targetType, Long targetId, List<Long> moduleIds, List<Long> privilegeIds, ServiceModulePrivilegeType type){
		if(null != moduleIds && moduleIds.size() > 0){
			List<ServiceModulePrivilege> privileges = serviceModuleProvider.listServiceModulePrivileges(moduleIds, type);
			if(null == privilegeIds){
				privilegeIds = new ArrayList<>();
			}
			for (ServiceModulePrivilege privilege: privileges) {
				privilegeIds.add(privilege.getPrivilegeId());
			}
		}
		AclRoleDescriptor descriptor = new AclRoleDescriptor(targetType, targetId);
		List<Acl> acls = aclProvider.getResourceAclByRole(resourceType, resourceId, descriptor);
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

	@Override
	public void deleteAcls(String resourceType, Long resourceId, String targetType, Long targetId, Long moduleId, List<Long> privilegeIds, ServiceModulePrivilegeType type){
		List<Long> moduleIds = new ArrayList<>();
		moduleIds.add(moduleId);
		this.deleteAcls(resourceType, resourceId, targetType, targetId, moduleIds, privilegeIds, type);
	}

	@Override
	public void deleteAcls(String resourceType, Long resourceId, String targetType, Long targetId, List<Long> moduleIds, ServiceModulePrivilegeType type){
		this.deleteAcls(resourceType, resourceId, targetType, targetId, moduleIds, null, type);
	}

	@Override
	public void deleteAcls(String resourceType, Long resourceId, String targetType, Long targetId){
		deleteAcls(resourceType, resourceId, targetType, targetId, new ArrayList<Long>(), null);
	}

	@Override
	public void deleteAcls(String resourceType, Long resourceId, String targetType, Long targetId, List<Long> privilegeIds){
		deleteAcls(resourceType, resourceId, targetType, targetId, new ArrayList<Long>(), privilegeIds, null);
	}

	@Override
	public void deleteAcls(String resourceType, Long resourceId, String targetType, Long targetId, Long moduleId, ServiceModulePrivilegeType type){
		this.deleteAcls(resourceType, resourceId, targetType, targetId, moduleId, null, type);
	}



	/**
	 * 抛出无权限
	 */
	private void returnNoPrivileged(List<Long> privileges, User user){
		LOGGER.error("non-privileged, privileges="+privileges + ", userId=" + user.getId());
		throw RuntimeErrorException.errorWith(OrganizationServiceErrorCode.SCOPE, OrganizationServiceErrorCode.ERROR_NO_PRIVILEGED,
				"non-privileged.");
	}

	@Override
	public List<RoleAuthorizationsDTO> listRoleAdministrators(ListRoleAdministratorsCommand cmd) {
		List<Authorization> authorizations = authorizationProvider.listManageAuthorizations(cmd.getOwnerType(), cmd.getOwnerId(), EntityType.ROLE.getCode(), cmd.getRoleId());
		return authorizations.stream().map((r) ->{
			RoleAuthorizationsDTO dto = ConvertHelper.convert(r, RoleAuthorizationsDTO.class);
			if(EntityType.USER == EntityType.fromCode(r.getTargetType())){
				UserIdentifier userIdentifier = userProvider.findClaimedIdentifierByOwnerAndType(r.getTargetId(), IdentifierType.MOBILE.getCode());
				if(null != userIdentifier){
					dto.setIdentifierToken(userIdentifier.getIdentifierToken());
				}
				User user = userProvider.findUserById(r.getTargetId());
				if(null != user){
					dto.setNikeName(user.getNickName());
				}
			}
			List<Role> roles = getRoleManageByTarget(r.getOwnerType(), r.getOwnerId(), r.getTargetType(), r.getTargetId(), EntityType.ROLE.getCode(), null);
			dto.setRoles(roles.stream().map((n) ->{
				return ConvertHelper.convert(n, RoleDTO.class);
			}).collect(Collectors.toList()));
			return dto;
		}).collect(Collectors.toList());
	}

	@Override
	public void updateRoleAdministrators(CreateRoleAdministratorsCommand cmd) {
		checkOwner(cmd.getOwnerType(), cmd.getOwnerId());

		checkTarget(cmd.getTargetType(), cmd.getTargetId());

		List<Role> roles = getRoleManageByTarget(cmd.getOwnerType(), cmd.getOwnerId(), cmd.getTargetType(), cmd.getTargetId(), EntityType.ROLE.getCode(), null);

		if(null == roles){
			LOGGER.error("This user has not been added to the administrator list.");
			throw RuntimeErrorException.errorWith(PrivilegeServiceErrorCode.SCOPE, PrivilegeServiceErrorCode.ERROR_ADMINISTRATORS_LIST_NONEXISTS,
					"This user has not been added to the administrator list.");
		}

		DeleteRoleAdministratorsCommand deleteCmd = ConvertHelper.convert(cmd, DeleteRoleAdministratorsCommand.class);
		dbProvider.execute((TransactionStatus status) -> {
			deleteRoleAdministrators(deleteCmd);
			createRoleAdministrators(cmd);
			return null;
		});
	}

	@Override
	public void createRoleAdministrators(CreateRoleAdministratorsCommand cmd) {

		checkOwner(cmd.getOwnerType(), cmd.getOwnerId());

		checkTarget(cmd.getTargetType(), cmd.getTargetId());

		List<Role> roles = getRoleManageByTarget(cmd.getOwnerType(), cmd.getOwnerId(), cmd.getTargetType(), cmd.getTargetId(), EntityType.ROLE.getCode(), null);

		if(null != roles){
			LOGGER.error("This user has been added to the administrator list.");
			throw RuntimeErrorException.errorWith(PrivilegeServiceErrorCode.SCOPE, PrivilegeServiceErrorCode.ERROR_ADMINISTRATORS_LIST_EXISTS,
					"This user has been added to the administrator list.");
		}

		if(null == cmd.getRoleIds() || cmd.getRoleIds().size() == 0){
			LOGGER.error("roleIds is null");
			throw RuntimeErrorException.errorWith(PrivilegeServiceErrorCode.SCOPE, PrivilegeServiceErrorCode.ERROR_INVALID_PARAMETER,
					"roleIds is null.");
		}

		User user = UserContext.current().getUser();
		Integer namespaceId = UserContext.getCurrentNamespaceId();
		Authorization authorization = ConvertHelper.convert(cmd, Authorization.class);
		authorization.setAuthType(EntityType.ROLE.getCode());
		authorization.setIdentityType(IdentityType.MANAGE.getCode());
		authorization.setNamespaceId(namespaceId);
		authorization.setAllFlag(AllFlagType.NO.getCode());
		authorization.setCreatorUid(user.getId());
		authorization.setOperatorUid(user.getId());
		authorization.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));

		dbProvider.execute((TransactionStatus status) -> {
			for (Long roleId: cmd.getRoleIds()) {
				authorization.setAuthId(roleId);
				authorizationProvider.createAuthorization(authorization);
				assignmentAclRole(cmd.getOwnerType(), cmd.getOwnerId(), cmd.getTargetType(), cmd.getTargetId(), namespaceId, user.getId(), roleId);
			}
			return null;
		});

	}

	@Override
	public void deleteRoleAdministrators(DeleteRoleAdministratorsCommand cmd) {

		checkOwner(cmd.getOwnerType(), cmd.getOwnerId());

		checkTarget(cmd.getTargetType(), cmd.getTargetId());

		List<Role> roles = getRoleManageByTarget(cmd.getOwnerType(), cmd.getOwnerId(), cmd.getTargetType(), cmd.getTargetId(), EntityType.ROLE.getCode(), null);

		if(null == roles){
			LOGGER.error("This user has not been added to the administrator list.");
			throw RuntimeErrorException.errorWith(PrivilegeServiceErrorCode.SCOPE, PrivilegeServiceErrorCode.ERROR_ADMINISTRATORS_LIST_NONEXISTS,
					"This user has not been added to the administrator list.");
		}

		List<Authorization> authorizations = authorizationProvider.listManageAuthorizationsByTarget(cmd.getOwnerType(), cmd.getOwnerId(), cmd.getTargetType(), cmd.getTargetId(), EntityType.ROLE.getCode(), null);
		List<Long> roleIds = new ArrayList<>();
		dbProvider.execute((TransactionStatus status) -> {
			for (Authorization authorization: authorizations) {
				roleIds.add(authorization.getAuthId());
				authorizationProvider.deleteAuthorizationById(authorization.getId());
			}

			List<RoleAssignment> roleAssignments = aclProvider.getRoleAssignmentByResourceAndTarget(cmd.getOwnerType(), cmd.getOwnerId(), cmd.getTargetType(), cmd.getTargetId());
			for (RoleAssignment roleAssignment: roleAssignments) {
				if(roleIds.contains(roleAssignment.getRoleId())){
					aclProvider.deleteRoleAssignment(roleAssignment.getId());
				}
			}
			return null;
		});
	}

	@Override
	public RoleAuthorizationsDTO checkRoleAdministrators(CheckRoleAdministratorsCommand cmd) {

		checkOwner(cmd.getOwnerType(), cmd.getOwnerId());

		Integer namespaceId = UserContext.getCurrentNamespaceId(cmd.getNamespaceId());
		UserIdentifier userIdentifier = userProvider.findClaimedIdentifierByToken(namespaceId, cmd.getIdentifierToken());
		RoleAuthorizationsDTO dto = new RoleAuthorizationsDTO();
		dto.setTargetType(EntityType.USER.getCode());
		if(null != userIdentifier){
			dto.setTargetId(userIdentifier.getOwnerUid());
			dto.setIdentifierToken(userIdentifier.getIdentifierToken());
			User user = userProvider.findUserById(userIdentifier.getOwnerUid());
			if(null != user){
				dto.setNikeName(user.getNickName());
			}

			List<Role> roles = getRoleManageByTarget(cmd.getOwnerType(), cmd.getOwnerId(), dto.getTargetType(), dto.getTargetId(), EntityType.ROLE.getCode(), null);

			if(roles.size() > 0){
				dto.setRoles(roles.stream().map((r) ->{
					return ConvertHelper.convert(r, RoleDTO.class);
				}).collect(Collectors.toList()));
			}
		}
		return dto;
	}

	@Override
	public List<ServiceModuleAuthorizationsDTO> listServiceModuleAdministrators(ListServiceModuleAdministratorsCommand cmd) {
		List<Authorization> authorizations = authorizationProvider.listManageAuthorizations(cmd.getOwnerType(), cmd.getOwnerId(), EntityType.SERVICE_MODULE.getCode(), cmd.getModuleId());
		return authorizations.stream().map((r) ->{
			ServiceModuleAuthorizationsDTO dto = ConvertHelper.convert(r, ServiceModuleAuthorizationsDTO.class);

			processServiceModuleAuthorization(dto);

			List<ServiceModule> serviceModules = getServiceModuleManageByTarget(r.getOwnerType(), r.getOwnerId(), r.getTargetType(), r.getTargetId(), r.getAuthType(), null);

			if(null != serviceModules){
				if(serviceModules.size() == 0){
					dto.setAllFlag(AllFlagType.YES.getCode());
				}else{
					dto.setAllFlag(AllFlagType.NO.getCode());
					dto.setModules(serviceModules.stream().map((m) ->{
						return ConvertHelper.convert(m, ServiceModuleDTO.class);
					}).collect(Collectors.toList()));
				}
			}
			return dto;
		}).collect(Collectors.toList());
	}

	@Override
	public void createServiceModuleAdministrators(CreateServiceModuleAdministratorsCommand cmd){

		checkOwner(cmd.getOwnerType(), cmd.getOwnerId());

		checkTarget(cmd.getTargetType(), cmd.getTargetId());

		List<ServiceModule> modules = getServiceModuleManageByTarget(cmd.getOwnerType(), cmd.getOwnerId(), cmd.getTargetType(), cmd.getTargetId(), EntityType.SERVICE_MODULE.getCode(), null);

		if(null != modules){
			LOGGER.error("This user has been added to the administrator list.");
			throw RuntimeErrorException.errorWith(PrivilegeServiceErrorCode.SCOPE, PrivilegeServiceErrorCode.ERROR_ADMINISTRATORS_LIST_EXISTS,
					"This user has been added to the administrator list.");
		}

		if(null == AllFlagType.fromCode(cmd.getAllFlag())){
			LOGGER.error("params allFlag is null");
			throw RuntimeErrorException.errorWith(PrivilegeServiceErrorCode.SCOPE, PrivilegeServiceErrorCode.ERROR_INVALID_PARAMETER,
					"params allFlag is null.");
		}

		if(AllFlagType.NO == AllFlagType.fromCode(cmd.getAllFlag()) && (null == cmd.getModuleIds() || cmd.getModuleIds().size() == 0)){
			LOGGER.error("params moduleIds is null");
			throw RuntimeErrorException.errorWith(PrivilegeServiceErrorCode.SCOPE, PrivilegeServiceErrorCode.ERROR_INVALID_PARAMETER,
					"params moduleIds is null.");
		}

		User user = UserContext.current().getUser();
		Integer namespaceId = UserContext.getCurrentNamespaceId();
		Authorization authorization = ConvertHelper.convert(cmd, Authorization.class);
		authorization.setAuthType(EntityType.SERVICE_MODULE.getCode());
		authorization.setIdentityType(IdentityType.MANAGE.getCode());
		authorization.setNamespaceId(namespaceId);
		authorization.setCreatorUid(user.getId());
		authorization.setOperatorUid(user.getId());
		authorization.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));

		dbProvider.execute((TransactionStatus status) -> {

			if(AllFlagType.fromCode(authorization.getAllFlag()) == AllFlagType.YES){
				authorization.setAuthId(0L);
				authorizationProvider.createAuthorization(authorization);
				//给对象分配全部模块管理员的权限
				assignmentPrivileges(authorization.getOwnerType(), authorization.getOwnerId(), authorization.getTargetType(),authorization.getTargetId(), authorization.getAuthType() + authorization.getAuthId(), PrivilegeConstants.ALL_SERVICE_MODULE);
			}else{
				for (Long moduleId: cmd.getModuleIds()) {
					authorization.setAuthId(moduleId);
					authorizationProvider.createAuthorization(authorization);
					//给对象分配每个模块管理员的权限
					assignmentPrivileges(authorization.getOwnerType(), authorization.getOwnerId(), authorization.getTargetType(),authorization.getTargetId(), authorization.getAuthType() + authorization.getAuthId(), authorization.getAuthId(), ServiceModulePrivilegeType.SUPER);
				}
			}
			return null;
		});
	}

	@Override
	public void updateServiceModuleAdministrators(UpdateServiceModuleAdministratorsCommand cmd){

		checkOwner(cmd.getOwnerType(), cmd.getOwnerId());

		checkTarget(cmd.getTargetType(), cmd.getTargetId());

		List<ServiceModule> modules = getServiceModuleManageByTarget(cmd.getOwnerType(), cmd.getOwnerId(), cmd.getTargetType(), cmd.getTargetId(), EntityType.SERVICE_MODULE.getCode(), null);

		if(null == modules){
			LOGGER.error("This user has not been added to the administrator list.");
			throw RuntimeErrorException.errorWith(PrivilegeServiceErrorCode.SCOPE, PrivilegeServiceErrorCode.ERROR_ADMINISTRATORS_LIST_NONEXISTS,
					"This user has not been added to the administrator list.");
		}

		if(null == AllFlagType.fromCode(cmd.getAllFlag())){
			LOGGER.error("params allFlag is null");
			throw RuntimeErrorException.errorWith(PrivilegeServiceErrorCode.SCOPE, PrivilegeServiceErrorCode.ERROR_INVALID_PARAMETER,
					"params allFlag is null.");
		}

		if(AllFlagType.NO == AllFlagType.fromCode(cmd.getAllFlag()) && (null == cmd.getModuleIds() || cmd.getModuleIds().size() == 0)){
			LOGGER.error("params moduleIds is null");
			throw RuntimeErrorException.errorWith(PrivilegeServiceErrorCode.SCOPE, PrivilegeServiceErrorCode.ERROR_INVALID_PARAMETER,
					"params moduleIds is null.");
		}

		checkTarget(cmd.getTargetType(), cmd.getTargetId());
		dbProvider.execute((TransactionStatus status) -> {
			deleteServiceModuleAdministrators(ConvertHelper.convert(cmd, DeleteServiceModuleAdministratorsCommand.class));
			createServiceModuleAdministrators(ConvertHelper.convert(cmd, CreateServiceModuleAdministratorsCommand.class));
			return null;
		});
	}

	@Override
	public void deleteServiceModuleAdministrators(DeleteServiceModuleAdministratorsCommand cmd){

		checkOwner(cmd.getOwnerType(), cmd.getOwnerId());

		checkTarget(cmd.getTargetType(), cmd.getTargetId());

		List<ServiceModule> modules = getServiceModuleManageByTarget(cmd.getOwnerType(), cmd.getOwnerId(), cmd.getTargetType(), cmd.getTargetId(), EntityType.SERVICE_MODULE.getCode(), null);

		if(null == modules){
			LOGGER.error("This user has not been added to the administrator list.");
			throw RuntimeErrorException.errorWith(PrivilegeServiceErrorCode.SCOPE, PrivilegeServiceErrorCode.ERROR_ADMINISTRATORS_LIST_NONEXISTS,
					"This user has not been added to the administrator list.");
		}

		List<Authorization> authorizations = authorizationProvider.listManageAuthorizationsByTarget(cmd.getOwnerType(), cmd.getOwnerId(), cmd.getTargetType(), cmd.getTargetId(), EntityType.SERVICE_MODULE.getCode(), null);
		List<Long> moduleIds = new ArrayList<>();
		dbProvider.execute((TransactionStatus status) -> {
			for (Authorization authorization: authorizations) {
				authorizationProvider.deleteAuthorizationById(authorization.getId());

				//删除管理员拥有全部模块的管理员权限
				if(AllFlagType.fromCode(authorization.getAllFlag()) == AllFlagType.YES){
					List<Long> privilegeIds = new ArrayList<>();
					privilegeIds.add(PrivilegeConstants.ALL_SERVICE_MODULE);
					deleteAcls(cmd.getOwnerType(), cmd.getOwnerId(), cmd.getTargetType(), cmd.getTargetId(), privilegeIds);

				}else{
					moduleIds.add(authorization.getAuthId());
				}
			}
			//删除管理员拥有部分模块的管理员权限
			if(moduleIds.size() > 0)
				deleteAcls(cmd.getOwnerType(), cmd.getOwnerId(), cmd.getTargetType(), cmd.getTargetId(), moduleIds, ServiceModulePrivilegeType.SUPER);
			return null;
		});
	}

	@Override
	public ListAuthorizationRelationsResponse listAuthorizationRelations(ListAuthorizationRelationsCommand cmd) {

		CrossShardListingLocator locator = new CrossShardListingLocator();

		locator.setAnchor(cmd.getPageAnchor());

		int pageSize = PaginationConfigHelper.getPageSize(configProvider, cmd.getPageSize());


		List<AuthorizationRelation> authorizationRelations = authorizationProvider.listAuthorizationRelations(locator, pageSize, cmd.getOwnerType(), cmd.getOwnerId(), cmd.getModuleId());

		List<AuthorizationRelationDTO> dtos =  authorizationRelations.stream().map((r) ->{

			String projectJson = r.getProjectJson();
			String targetJson = r.getTargetJson();
			String privilegeJson = r.getPrivilegeJson();
			AuthorizationRelationDTO authorizationRelation = ConvertHelper.convert(r, AuthorizationRelationDTO.class);

			if(AllFlagType.NO == AllFlagType.fromCode(r.getAllProjectFlag())){
				Project[] projectArr = (Project[])StringHelper.fromJsonString(projectJson, Project[].class);
				List<Project> projects = Arrays.asList(projectArr);
				authorizationRelation.setProjects(projects.stream().map((p) ->{
					if(EntityType.COMMUNITY == EntityType.fromCode(p.getProjectType())){
						Community community = communityProvider.findCommunityById(p.getProjectId());
						if(null != community){
							p.setProjectName(community.getName());
						}else{
							LOGGER.error("Unable to find the community. communityId = {}", p.getProjectId());
						}

					}else if(EntityType.CHILD_PROJECT == EntityType.fromCode(p.getProjectType())){
						ResourceCategory resourceCategory = communityProvider.findResourceCategoryById(p.getProjectId());
						if(null != resourceCategory){
							p.setProjectName(resourceCategory.getName());
						}else{
							LOGGER.error("Unable to find the resourceCategory. resourceCategoryId = {}", p.getProjectId());
						}
					}else{
						LOGGER.error("Unable to find the projectType. targetType = {}", p.getProjectType());
					}
					return p;
				}).collect(Collectors.toList()));
			}

			AssignmentTarget[] targetArr = (AssignmentTarget[])StringHelper.fromJsonString(targetJson, AssignmentTarget[].class);
			List<AssignmentTarget> targets = Arrays.asList(targetArr);
			authorizationRelation.setTargets(targets.stream().map((p) ->{
				if(EntityType.USER == EntityType.fromCode(p.getTargetType())){
					User user = userProvider.findUserById(p.getTargetId());
					if(null != user){
						p.setTargetName(user.getNickName());
					}else{
						LOGGER.error("Unable to find the user. userId = {}", p.getTargetId());
					}

				}else if(EntityType.ORGANIZATIONS == EntityType.fromCode(p.getTargetType())){
					Organization organization = organizationProvider.findOrganizationById(p.getTargetId());
					if(null != organization){
						p.setTargetName(organization.getName());
					}else{
						LOGGER.error("Unable to find the orgnaization. orgnaizationId = {}", p.getTargetId());
					}
				}else{
					LOGGER.error("Unable to find the targetType. targetType = {}", p.getTargetId());
				}
				return p;
			}).collect(Collectors.toList()));

			//非全部的时候 查出所有的权限
			if(AllFlagType.NO == AllFlagType.fromCode(r.getAllFlag())){
				Long[] privilegeIdArr = (Long[])StringHelper.fromJsonString(privilegeJson, Long[].class);
				List<Long> privilegeIds = Arrays.asList(privilegeIdArr);
				authorizationRelation.setPrivileges(privilegeIds.stream().map((i) ->{
					ServiceModulePrivilege privilege = serviceModuleProvider.getServiceModulePrivilegesByModuleIdAndPrivilegeId(r.getModuleId(), i);
					PrivilegeDTO privileges = new PrivilegeDTO();
					privileges.setPrivilegeId(i);
					if(null != privilege){
						privileges.setPrivilegeName(privilege.getRemark());
					}else{
						LOGGER.error("Unable to find the serviceModulePrivilege. moduleId = {}, privilegeId = {}", r.getModuleId(), i);
					}
					return privileges;
				}).collect(Collectors.toList()));
			}
			return authorizationRelation;
		}).collect(Collectors.toList());

		ListAuthorizationRelationsResponse response = new ListAuthorizationRelationsResponse();
		response.setNextPageAnchor(locator.getAnchor());
		response.setDtos(dtos);

		return response;
	}


	@Override
	public void createAuthorizationRelation(CreateAuthorizationRelationCommand cmd) {
		checkOwner(cmd.getOwnerType(),cmd.getOwnerId());

		if(null == cmd.getTargets() || cmd.getTargets().size() == 0){
			LOGGER.error("params targets is null");
			throw RuntimeErrorException.errorWith(PrivilegeServiceErrorCode.SCOPE, PrivilegeServiceErrorCode.ERROR_INVALID_PARAMETER,
					"params targets is null.");
		}

		if(null == AllFlagType.fromCode(cmd.getAllProjectFlag())){
			LOGGER.error("params allProjectFlag is null");
			throw RuntimeErrorException.errorWith(PrivilegeServiceErrorCode.SCOPE, PrivilegeServiceErrorCode.ERROR_INVALID_PARAMETER,
					"params allProjectFlag is null.");
		}

		if(AllFlagType.NO == AllFlagType.fromCode(cmd.getAllProjectFlag()) && (null == cmd.getProjects() || cmd.getProjects().size() == 0)){
			LOGGER.error("params projects is null");
			throw RuntimeErrorException.errorWith(PrivilegeServiceErrorCode.SCOPE, PrivilegeServiceErrorCode.ERROR_INVALID_PARAMETER,
					"params projects is null.");
		}

		if(null == AllFlagType.fromCode(cmd.getAllFlag())){
			LOGGER.error("params allFlag is null");
			throw RuntimeErrorException.errorWith(PrivilegeServiceErrorCode.SCOPE, PrivilegeServiceErrorCode.ERROR_INVALID_PARAMETER,
					"params allFlag is null.");
		}

		if(AllFlagType.NO == AllFlagType.fromCode(cmd.getAllFlag()) && (null == cmd.getPrivilegeIds() || cmd.getPrivilegeIds().size() == 0)){
			LOGGER.error("params privilegeIds is null");
			throw RuntimeErrorException.errorWith(PrivilegeServiceErrorCode.SCOPE, PrivilegeServiceErrorCode.ERROR_INVALID_PARAMETER,
					"params privilegeIds is null.");
		}

		User user = UserContext.current().getUser();
		Integer namespaceId = UserContext.getCurrentNamespaceId();
		dbProvider.execute((TransactionStatus status) -> {
			AuthorizationRelation authorizationRelation = ConvertHelper.convert(cmd, AuthorizationRelation.class);
			authorizationRelation.setCreatorUid(user.getId());
			authorizationRelation.setOperatorUid(user.getId());
			authorizationRelation.setNamespaceId(namespaceId);
			if(AllFlagType.NO == AllFlagType.fromCode(cmd.getAllProjectFlag())){
				authorizationRelation.setProjectJson(StringHelper.toJsonString(cmd.getProjects()));
			}
			authorizationRelation.setTargetJson(StringHelper.toJsonString(cmd.getTargets()));
			if(AllFlagType.NO == AllFlagType.fromCode(cmd.getAllFlag())){
				authorizationRelation.setPrivilegeJson(StringHelper.toJsonString(cmd.getPrivilegeIds()));
			}

			//创建授权关系记录
			authorizationProvider.createAuthorizationRelation(authorizationRelation);

			//创建授权信息和权限
			createAuthorizationsOrAclsByRelation(user, authorizationRelation, cmd.getTargets(), cmd.getProjects(), cmd.getPrivilegeIds());

			return null;
		});

	}

	@Override
	public void updateAuthorizationRelation(UpdateAuthorizationRelationCommand cmd) {

		checkOwner(cmd.getOwnerType(),cmd.getOwnerId());

		if(null == cmd.getId()){
			LOGGER.error("params id is null");
			throw RuntimeErrorException.errorWith(PrivilegeServiceErrorCode.SCOPE, PrivilegeServiceErrorCode.ERROR_INVALID_PARAMETER,
					"params id is null.");
		}

		if(null == cmd.getTargets() || cmd.getTargets().size() == 0){
			LOGGER.error("params targets is null");
			throw RuntimeErrorException.errorWith(PrivilegeServiceErrorCode.SCOPE, PrivilegeServiceErrorCode.ERROR_INVALID_PARAMETER,
					"params targets is null.");
		}

		if(null == AllFlagType.fromCode(cmd.getAllProjectFlag())){
			LOGGER.error("params allProjectFlag is null");
			throw RuntimeErrorException.errorWith(PrivilegeServiceErrorCode.SCOPE, PrivilegeServiceErrorCode.ERROR_INVALID_PARAMETER,
					"params allProjectFlag is null.");
		}

		if(AllFlagType.NO == AllFlagType.fromCode(cmd.getAllProjectFlag()) && (null == cmd.getProjects() || cmd.getProjects().size() == 0)){
			LOGGER.error("params projects is null");
			throw RuntimeErrorException.errorWith(PrivilegeServiceErrorCode.SCOPE, PrivilegeServiceErrorCode.ERROR_INVALID_PARAMETER,
					"params projects is null.");
		}

		if(null == AllFlagType.fromCode(cmd.getAllFlag())){
			LOGGER.error("params allFlag is null");
			throw RuntimeErrorException.errorWith(PrivilegeServiceErrorCode.SCOPE, PrivilegeServiceErrorCode.ERROR_INVALID_PARAMETER,
					"params allFlag is null.");
		}

		if(AllFlagType.NO == AllFlagType.fromCode(cmd.getAllFlag()) && (null == cmd.getPrivilegeIds() || cmd.getPrivilegeIds().size() == 0)){
			LOGGER.error("params privilegeIds is null");
			throw RuntimeErrorException.errorWith(PrivilegeServiceErrorCode.SCOPE, PrivilegeServiceErrorCode.ERROR_INVALID_PARAMETER,
					"params privilegeIds is null.");
		}

		User user = UserContext.current().getUser();

		AuthorizationRelation authorizationRelation = checkAuthorizationRelation(cmd.getId());

		dbProvider.execute((TransactionStatus status) -> {

			//根据关系删除授权的记录以及关系权限
			deleteAuthorizationsOrAclsByRelation(authorizationRelation);
			authorizationRelation.setOperatorUid(user.getId());
			authorizationRelation.setAllFlag(cmd.getAllFlag());
			authorizationRelation.setAllProjectFlag(cmd.getAllProjectFlag());
			if(AllFlagType.NO == AllFlagType.fromCode(cmd.getAllProjectFlag())){
				authorizationRelation.setProjectJson(StringHelper.toJsonString(cmd.getProjects()));
			}
			authorizationRelation.setTargetJson(StringHelper.toJsonString(cmd.getTargets()));
			if(AllFlagType.NO == AllFlagType.fromCode(cmd.getAllFlag())){
				authorizationRelation.setPrivilegeJson(StringHelper.toJsonString(cmd.getPrivilegeIds()));
			}

			//修改授权关系
			authorizationProvider.updateAuthorizationRelation(authorizationRelation);

			//授权的记录以及关系权限
			createAuthorizationsOrAclsByRelation(user, authorizationRelation, cmd.getTargets(), cmd.getProjects(), cmd.getPrivilegeIds());
			return null;
		});
	}

	private void deleteAuthorizations(String scope){
		List<Authorization> authorizations = authorizationProvider.listAuthorizationsByScope(scope);
		for (Authorization authorization: authorizations) {
			authorizationProvider.deleteAuthorizationById(authorization.getId());
		}
	}

	@Override
	public void deleteAuthorizationRelation(DeleteAuthorizationRelationCommand cmd) {
		checkOwner(cmd.getOwnerType(), cmd.getOwnerId());

		if(null == cmd.getId()){
			LOGGER.error("params id is null");
			throw RuntimeErrorException.errorWith(PrivilegeServiceErrorCode.SCOPE, PrivilegeServiceErrorCode.ERROR_INVALID_PARAMETER,
					"params id is null.");
		}

		AuthorizationRelation authorizationRelation = checkAuthorizationRelation(cmd.getId());

		dbProvider.execute((TransactionStatus status) -> {
			authorizationProvider.deleteAuthorizationRelationById(cmd.getId());
			deleteAuthorizationsOrAclsByRelation(authorizationRelation);
			return null;
		});

	}

	private void createAuthorizationsOrAclsByRelation(User user, AuthorizationRelation authorizationRelation, List<AssignmentTarget> targets, List<Project> projects, List<Long> privilegeIds){
		List<Authorization> authorizations = new ArrayList<>();
		String tag = EntityType.AUTHORIZATION_RELATION.getCode() + "." + authorizationRelation.getId();
		for (AssignmentTarget target: targets) {
			checkTarget(target.getTargetType(), target.getTargetId());
			if (AllFlagType.NO == AllFlagType.fromCode(authorizationRelation.getAllProjectFlag())) {
				for (Project project : projects) {
					//授权
					assignmentAcls(project.getProjectType(), project.getProjectId(), target.getTargetType(), target.getTargetId(), authorizationRelation.getAllFlag(), authorizationRelation.getModuleId(), privilegeIds, false, tag);

					Authorization authorization = new Authorization();
					authorization.setOwnerType(project.getProjectType());
					authorization.setOwnerId(project.getProjectId());
					authorization.setTargetType(target.getTargetType());
					authorization.setTargetId(target.getTargetId());
					authorization.setAllFlag(authorizationRelation.getAllFlag());
					authorization.setAuthType(EntityType.SERVICE_MODULE.getCode());
					authorization.setIdentityType(IdentityType.ORDINARY.getCode());
					authorization.setNamespaceId(authorizationRelation.getNamespaceId());
					authorization.setCreatorUid(user.getId());
					authorization.setOperatorUid(user.getId());
					authorization.setAuthId(authorizationRelation.getModuleId());
					authorization.setScope(tag);
					authorizations.add(authorization);

				}
			} else {
				//给对象授权
				assignmentAcls(EntityType.ALL.getCode(), 0L, target.getTargetType(), target.getTargetId(), authorizationRelation.getAllFlag(), authorizationRelation.getModuleId(), privilegeIds, false, tag);

				Authorization authorization = new Authorization();
				authorization.setOwnerType(EntityType.ALL.getCode());
				authorization.setOwnerId(0L);
				authorization.setTargetType(target.getTargetType());
				authorization.setTargetId(target.getTargetId());
				authorization.setAllFlag(authorizationRelation.getAllFlag());
				authorization.setAuthType(EntityType.SERVICE_MODULE.getCode());
				authorization.setAuthId(authorizationRelation.getModuleId());
				authorization.setIdentityType(IdentityType.ORDINARY.getCode());
				authorization.setNamespaceId(authorizationRelation.getNamespaceId());
				authorization.setCreatorUid(user.getId());
				authorization.setOperatorUid(user.getId());
				authorization.setScope(tag);
				authorizations.add(authorization);
			}
		}

		//创建授权模块数据
		authorizationProvider.createAuthorizations(authorizations);
	}

	/**
	 * 根据授权关系删除授权记录，以及权限
	 * @param authorizationRelation
	 */
	private void deleteAuthorizationsOrAclsByRelation(AuthorizationRelation authorizationRelation){

		String tag = EntityType.AUTHORIZATION_RELATION.getCode() + "." + authorizationRelation.getId();

		//删除权限
		privilegeProvider.deleteAclsByTag(tag);

		//根据关系删除授权的记录
		deleteAuthorizations(tag);

	}

	@Override
	public List<ServiceModuleDTO> listServiceModulesByTarget(ListServiceModulesByTargetCommand cmd){
		List<ServiceModule> serviceModules = getServiceModuleManageByTarget(cmd.getOwnerType(), cmd.getOwnerId(), cmd.getTargetType(), cmd.getTargetId(), EntityType.SERVICE_MODULE.getCode(), null);

		List<Long> parentIds = new ArrayList<>();
		for (ServiceModule serviceModule: serviceModules) {
			if(!parentIds.contains(serviceModule.getParentId())){
				parentIds.add(serviceModule.getParentId());
			}
		}

		List<ServiceModule> parentModules = serviceModuleProvider.listServiceModule(parentIds);
		List<ServiceModuleDTO> dtos = new ArrayList<>();
		for (ServiceModule parentModule: parentModules) {
			ServiceModuleDTO dto = ConvertHelper.convert(parentModule, ServiceModuleDTO.class);
			dto.setServiceModules(new ArrayList<>());
			for (ServiceModule serviceModule: serviceModules) {
				if(parentModule.getId().equals(serviceModule.getParentId())){
					dto.getServiceModules().add(ConvertHelper.convert(serviceModule, ServiceModuleDTO.class));
				}
			}
			dtos.add(dto);
		}

		return dtos;
	}


	private void assignmentAcls(String ownerType, Long ownerId, String targetType, Long targetId, Byte allFlag, Long moduleId, List<Long> privilegeIds ,boolean isDelete, String tag){
		if(isDelete){
			//删除人员拥有的模块全部权限
			if(AllFlagType.fromCode(allFlag) == AllFlagType.YES){
				deleteAcls(ownerType, ownerId, targetType, targetId, moduleId, ServiceModulePrivilegeType.ORDINARY_ALL);

				//删除人员拥有的模块部分权限
			}else{
				deleteAcls(ownerType, ownerId, targetType, targetId, moduleId, ServiceModulePrivilegeType.ORDINARY);
			}
		}

		if(AllFlagType.fromCode(allFlag) == AllFlagType.YES){
			//给对象分配模块的全部权限
			assignmentPrivileges(ownerType, ownerId, targetType, targetId, EntityType.SERVICE_MODULE.getCode() + moduleId, moduleId, ServiceModulePrivilegeType.ORDINARY_ALL, tag);
		}else{
			assignmentPrivileges(ownerType, ownerId, targetType, targetId, EntityType.SERVICE_MODULE.getCode() + moduleId, privilegeIds, tag);
		}
	}

	private void assignmentAclRole(String ownerType, Long ownerId, String targetType, Long targetId, Integer namespaceId, Long creatorUid, Long roleId){
		RoleAssignment roleAssignment = new RoleAssignment();
		roleAssignment.setOwnerType(ownerType);
		roleAssignment.setOwnerId(ownerId);
		roleAssignment.setTargetType(targetType);
		roleAssignment.setTargetId(targetId);
		roleAssignment.setCreatorUid(creatorUid);
		roleAssignment.setNamespaceId(namespaceId);
		roleAssignment.setRoleId(roleId);
		aclProvider.createRoleAssignment(roleAssignment);
	}

	private List<Role> getRoleManageByTarget(String ownerType, Long ownerId, String targetType, Long targetId, String authType, Long authId){
		List<Authorization> authorizations =  authorizationProvider.listManageAuthorizationsByTarget(ownerType, ownerId, targetType, targetId , authType, authId);
		List<Role> roles = new ArrayList<>();
		if(null == authorizations){
			return null;
		}
		for (Authorization authorization: authorizations) {
			Role role = aclProvider.getRoleById(authorization.getAuthId());
			if(null != role){
				roles.add(role);
			}
		}
		return roles;
	}

	private List<Privilege> getPrivilegeOrdinaryByTarget(String ownerType, Long ownerId, String targetType, Long targetId, String authType, Long authId){
		List<Authorization> authorizations =  authorizationProvider.listOrdinaryAuthorizationsByTarget(ownerType, ownerId, targetType, targetId ,authType, authId);
		List<Privilege> privileges = new ArrayList<>();
		if(authorizations.size() == 0){
			return null;
		}
		Authorization authorization = authorizations.get(0);

		//全部则不查询具体权限
		if(AllFlagType.fromCode(authorization.getAllFlag()) == AllFlagType.YES){
			return privileges;
		}else{
			List<Acl> acls = privilegeProvider.listAclsByModuleId(authorization.getOwnerType(), authorization.getOwnerId(), authorization.getTargetType(), authorization.getTargetId(), authorization.getAuthId());
			return acls.stream().map((p) ->{
				Privilege privilege = new Privilege();
				privilege.setId(p.getPrivilegeId());
				ServiceModulePrivilege modulePrivilege = serviceModuleProvider.getServiceModulePrivilegesByModuleIdAndPrivilegeId(authorization.getAuthId(), p.getPrivilegeId());
				if(null != privilege)
					privilege.setName(modulePrivilege.getRemark());
				return privilege;
			}).collect(Collectors.toList());
		}
	}

	private List<ServiceModule> getServiceModuleManageByTarget(String ownerType, Long ownerId, String targetType, Long targetId, String authType, Long authId){
		List<Authorization> authorizations =  authorizationProvider.listManageAuthorizationsByTarget(ownerType, ownerId, targetType, targetId ,authType, authId);
		List<ServiceModule> serviceModules = new ArrayList<>();

		if(authorizations.size() == 0){
			return null;
		}

		for (Authorization authorization: authorizations) {
			if(AllFlagType.fromCode(authorization.getAllFlag()) == AllFlagType.YES){
				return serviceModules;
			}

			ServiceModule serviceModule = serviceModuleProvider.findServiceModuleById(authorization.getAuthId());
			if(null != serviceModule){
				serviceModules.add(serviceModule);
			}
		}
		return serviceModules;
	}

	private void processServiceModuleAuthorization(ServiceModuleAuthorizationsDTO dto){
		if(EntityType.USER == EntityType.fromCode(dto.getTargetType())){
			OrganizationMember member = null;
			if(EntityType.fromCode(dto.getOwnerType()) == EntityType.ORGANIZATIONS){
				member = organizationProvider.findOrganizationMemberByOrgIdAndUId(dto.getTargetId(), dto.getOwnerId());
			}

			//设置昵称
			UserIdentifier userIdentifier = userProvider.findClaimedIdentifierByOwnerAndType(dto.getTargetId(), IdentifierType.MOBILE.getCode());
			if(null != userIdentifier){
				dto.setIdentifierToken(userIdentifier.getIdentifierToken());
			}
			User user = userProvider.findUserById(dto.getTargetId());
			if(null != user){
				dto.setNickName(user.getNickName());
			}

			//设置姓名
			if(null != member && member.getStatus().equals(OrganizationMemberStatus.ACTIVE.getCode())){//如果是该公司的成员
				dto.setIdentifierToken(member.getContactToken());
				dto.setTargetName(member.getContactName());
			}
		}else if(EntityType.ORGANIZATIONS == EntityType.fromCode(dto.getTargetType())){
			Organization organization = organizationProvider.findOrganizationById(dto.getTargetId());
			if(null != organization)
				dto.setTargetName(organization.getName());
		}
	}

	private void checkTarget(String targetType, Long targetId){
		if(null == EntityType.fromCode(targetType)){
			LOGGER.error("params targetType is null");
			throw RuntimeErrorException.errorWith(PrivilegeServiceErrorCode.SCOPE, PrivilegeServiceErrorCode.ERROR_INVALID_PARAMETER,
					"params targetType is null.");
		}

		if(null == targetId){
			LOGGER.error("params targetId is null");
			throw RuntimeErrorException.errorWith(PrivilegeServiceErrorCode.SCOPE, PrivilegeServiceErrorCode.ERROR_INVALID_PARAMETER,
					"params targetId is null.");
		}

		if(EntityType.USER == EntityType.fromCode(targetType)){
			checkUser(targetId);
		}else if(EntityType.ORGANIZATIONS == EntityType.fromCode(targetType)){
			checkOrganization(targetId);
		}
	}

	private User checkUser(Long userId){
		User user = userProvider.findUserById(userId);
		if(null == user){
			LOGGER.error("Unable to find the user. user = {}", userId);
			throw RuntimeErrorException.errorWith(PrivilegeServiceErrorCode.SCOPE, PrivilegeServiceErrorCode.ERROR_INVALID_PARAMETER,
					"user non-existent.");
		}
		return user;
	}

	private Organization checkOrganization(Long organizationId) {
		Organization org = organizationProvider.findOrganizationById(organizationId);
		if(org == null){
			LOGGER.error("Unable to find the organization.organizationId = {}",  organizationId);
			throw RuntimeErrorException.errorWith(PrivilegeServiceErrorCode.SCOPE, PrivilegeServiceErrorCode.ERROR_INVALID_PARAMETER,
					"Unable to find the organization.");
		}
		return org;
	}

	private void checkOwner(String ownerType, Long ownerId){
		if(null == EntityType.fromCode(ownerType)){
			LOGGER.error("params ownerType error.");
			throw RuntimeErrorException.errorWith(PrivilegeServiceErrorCode.SCOPE, PrivilegeServiceErrorCode.ERROR_INVALID_PARAMETER,
					"params ownerType error.");
		}
	}

	private AuthorizationRelation checkAuthorizationRelation(Long id){
		AuthorizationRelation authorizationRelation = authorizationProvider.findAuthorizationRelationById(id);
		if(null == authorizationRelation){
			LOGGER.error("Unable to find the authorizationRelation. id = {}", id);
			throw RuntimeErrorException.errorWith(PrivilegeServiceErrorCode.SCOPE, PrivilegeServiceErrorCode.ERROR_INVALID_PARAMETER,
					"Unable to find the authorizationRelation.");
		}

		return authorizationRelation;
	}

	public static void main(String[] args) {
//		System.out.println(GeoHashUtils.encode(41.843665, 123.455102));
		System.out.println("2015/11/11".replaceAll("/", "-"));
//		System.out.println(new Timestamp(DateUtil.parseDate("2015-11-11 02:30:00").getTime()));

//		for (int i = 1; i < 13; i++ ) {
//			System.out.println("INSERT INTO `eh_buildings` (`id`, `community_id`, `name`, `alias_name`, `manager_uid`, `contact`, `address`, `area_size`, `longitude`, `latitude`, `geohash`, `description`, `poster_uri`, `status`, `operator_uid`, `operate_time`, `creator_uid`, `create_time`, `delete_time`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `namespace_id`) VALUES((@building_id := @building_id + 1), @community_id, '伊湾尊府" + i + "号楼', '" + i + "号楼', 0, '0755-82738680', '浑南区朗日街19-" + i + "号楼', NULL, 41.843665, 123.455102, 'wxry133m02s0', '', NULL, 2, 1, UTC_TIMESTAMP(), 1, UTC_TIMESTAMP(), NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 999993);");
//		}
	}
}
