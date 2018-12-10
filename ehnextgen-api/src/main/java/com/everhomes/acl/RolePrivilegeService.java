// @formatter:off
package com.everhomes.acl;

import com.everhomes.module.ServiceModulePrivilegeType;
import com.everhomes.rest.acl.*;
import com.everhomes.rest.acl.admin.*;
import com.everhomes.rest.address.CommunityDTO;
import com.everhomes.rest.community.CommunityFetchType;
import com.everhomes.rest.customer.createSuperAdminCommand;
import com.everhomes.rest.module.ListServiceModuleAppsAdministratorResponse;
import com.everhomes.rest.organization.ListOrganizationAdministratorCommand;
import com.everhomes.rest.organization.ListOrganizationMemberCommandResponse;
import com.everhomes.rest.organization.OrganizationContactDTO;
import com.everhomes.rest.user.admin.ImportDataResponse;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface RolePrivilegeService {
	
	/**
	 * 获取用户权限的拥有的菜单
	 * @return
	 */
	ListWebMenuResponse listWebMenu(ListWebMenuCommand cmd);
	
	/**
	 * 获取用户所有模块权限
	 * @return
	 */
	List<ListWebMenuPrivilegeDTO> listWebMenuPrivilege(ListWebMenuPrivilegeCommand cmd);
	
	/**
     * 获取用户的权限列表
     * @param module
     * @param organizationId
     * @param userId
     * @return
     */
	List<Long> getUserPrivileges(String module ,Long organizationId, Long userId);
	
	/**
	 * 创建角色权限
	 * @param cmd
	 */
	void createRolePrivileges(CreateRolePrivilegesCommand cmd);
	
	/**
	 * 修改角色权限
	 * @param cmd
	 */
	void updateRolePrivileges(UpdateRolePrivilegesCommand cmd);
	
	/**
	 * 删除角色权限
	 * @param cmd
	 */
	void deleteRolePrivileges(DeleteRolePrivilegesCommand cmd);
	
	/**
	 * 获取角色权限
	 * @param cmd
	 * @return
	 */
	List<ListWebMenuPrivilegeDTO> qryRolePrivileges(QryRolePrivilegesCommand cmd);
	
	/**
	 * 获取角色列表
	 * @param cmd
	 * @return
	 */
	ListRolesResponse listRoles(ListRolesCommand cmd);
	
	/**
	 * 判断是否是系统管理员
	 * @param organizationId
	 * @return
	 */
	boolean checkAdministrators(Long organizationId);

	GetPrivilegeByRoleIdResponse getPrivilegeByRoleId(ListPrivilegesByRoleIdCommand cmd);
	
	/**
	 * 校验是否有权限
	 * @param ownerType
	 * @param ownerId
	 * @param privilegeId
	 * @return
	 */
	boolean checkAuthority(String ownerType, Long ownerId, Long privilegeId);
	
	/**
	 * 创建超级管理员
	 * @param cmd
	 */
	OrganizationContactDTO createOrganizationSuperAdmin(CreateOrganizationAdminCommand cmd);
	
	/**
	 * 创建普通管理员
	 * @param cmd
	 */
	void createOrganizationOrdinaryAdmin(CreateOrganizationAdminCommand cmd);
	
	/**
	 * 修改超级管理员
	 * @param cmd
	 */
	void updateOrganizationSuperAdmin(UpdateOrganizationAdminCommand cmd);
	
	/**
	 * 修改普通管理员
	 * @param cmd
	 */
	void updateOrganizationOrdinaryAdmin(UpdateOrganizationAdminCommand cmd);
	
	/**
	 * 查询管理员列表
	 * @param cmd
	 * @return
	 */
	ListOrganizationMemberCommandResponse listOrganizationAdministrators(ListOrganizationAdministratorCommand cmd);
	
	/**
	 * 删除角色人员
	 * @param cmd
	 */
	void deleteAclRoleAssignment(DeleteAclRoleAssignmentCommand cmd);
	
	/**
	 * 添加角色人员
	 * @param cmd
	 */
	void addAclRoleAssignment(AddAclRoleAssignmentCommand cmd);
	
	/**
	 * 添加人员多个角色
	 * @param cmd
	 */
	void batchAddTargetRoles(BatchAddTargetRoleCommand cmd);
	
	/**
	 * 导入
	 * @param files
	 * @return
	 */
	ImportDataResponse importRoleAssignmentPersonnelXls(ExcelRoleExcelRoleAssignmentPersonnelCommand cmd, MultipartFile[] files);
	
	/**
	 * 导出
	 * @param cmd
	 * @param response
	 */
	void exportRoleAssignmentPersonnelXls(ExcelRoleExcelRoleAssignmentPersonnelCommand cmd, HttpServletResponse response);

	/**
	 * 创建公司管理员
	 * @param cmd
     */
	OrganizationContactDTO createOrganizationAdmin(CreateOrganizationAdminCommand cmd);

//	/**
//	 * 创建业务模块管理员
//	 * @param cmd
//     */
//	@Deprecated
//	void createServiceModuleAdministrators(CreateServiceModuleAdministratorsCommand cmd);


	/**
	 * 业务模块管理员列表
	 * @param cmd
	 * @return
	 */
	List<ServiceModuleAuthorizationsDTO> listServiceModuleAdministrators(ListServiceModuleAdministratorsCommand cmd);

	/**
	 * 业务模块管理员列表(应用)
	 * @param cmd
	 * @return
	 */
	ListServiceModuleAppsAdministratorResponse listServiceModuleAppsAdministrators(ListServiceModuleAdministratorsCommand cmd);

	/**
	 * 超级管理员列表
	 * @param cmd
	 * @return
     */
	List<OrganizationContactDTO> listOrganizationSuperAdministrators(ListServiceModuleAdministratorsCommand cmd);

	/**
	 * 公司管理员列表
	 * @param cmd
	 * @return
     */
	List<OrganizationContactDTO> listOrganizationAdministrators(ListServiceModuleAdministratorsCommand cmd);

	/**
	 * 删除超级管理员
	 * @param cmd
     */
	void deleteOrganizationSuperAdministrators(DeleteOrganizationAdminCommand cmd);

	/**
	 * 删除公司管理员
	 * @param cmd
	 */
	void deleteOrganizationAdministrators(DeleteOrganizationAdminCommand cmd);

	void deleteOrganizationAdministratorsForOnes(DeleteOrganizationAdminCommand cmd);

	/**
	 * 删除业务模块管理员
	 * @param cmd
     */
	void deleteServiceModuleAdministrators(DeleteServiceModuleAdministratorsCommand cmd);

	/**
	 * 删除业务模块应用管理员
	 * @param cmd
	 */
	void deleteServiceModuleAppsAdministrators(DeleteServiceModuleAdministratorsCommand cmd);

	/**
	 * 业务模块授权
	 * @param cmd
     */
	void authorizationServiceModule(AuthorizationServiceModuleCommand cmd);

	/**
	 * 机构业务模块授权列表
	 * @param cmd
	 * @return
     */
	List<AuthorizationServiceModuleDTO> listAuthorizationServiceModules(ListAuthorizationServiceModuleCommand cmd);

	/**
	 * 机构人员业务模块授权列表
	 * @param cmd
	 * @return
     */
	List<AuthorizationServiceModuleMembersDTO> listAuthorizationServiceModuleMembers(ListAuthorizationServiceModuleCommand cmd);


	/**
	 * 删除授权的业务模块
 	 * @param cmd
     */
	void deleteAuthorizationServiceModule(DeleteAuthorizationServiceModuleCommand cmd);

	/**
	 * 获取用户全部角色
	 * @param organizationId
	 * @param userId
     * @return
     */
	List<RoleAssignment> getUserAllOrgRoles(Long organizationId, Long userId);

	/**
     * 获取用户的权限列表
     * @param communityId
     * @param userId
     * @return
     */
    List<Long> getUserCommunityPrivileges(Long communityId, Long userId);

	/**
	 * 获取项目
	 * @param cmd
	 * @return
     */
	List<ProjectDTO> listUserRelatedProjectByMenuId(ListUserRelatedProjectByMenuIdCommand cmd);

	/**
	 *
	 * @param ownerType
	 * @param ownerId
	 * @param targetType
	 * @param targetId
	 * @param scope
     * @param privilegeId
     */
	void assignmentPrivileges(String ownerType, Long ownerId,String targetType, Long targetId, String scope,  Long privilegeId);

	/**
	 * 分配权限
	 * @param ownerType
	 * @param ownerId
	 * @param targetType
	 * @param targetId
	 * @param scope
     * @param privilegeIds
     */
	void assignmentPrivileges(String ownerType, Long ownerId,String targetType, Long targetId, String scope,  List<Long> privilegeIds, String tag);

	/**
	 * 分配模块权限
	 * @param ownerType
	 * @param ownerId
	 * @param targetType
	 * @param targetId
	 * @param scope
	 * @param moduleId
     * @param privilegeType
     */
	void assignmentPrivileges(String ownerType, Long ownerId,String targetType, Long targetId, String scope, Long moduleId, ServiceModulePrivilegeType privilegeType, String tag);

	void assignmentPrivileges(String ownerType, Long ownerId,String targetType, Long targetId, String scope,  List<Long> privilegeIds);

	/**
	 *
	 * @param resourceType
	 * @param resourceId
	 * @param targetType
	 * @param targetId
	 * @param moduleId
     * @param type
     */
	void deleteAcls(String resourceType, Long resourceId, String targetType, Long targetId, Long moduleId, ServiceModulePrivilegeType type);

	/**
	 *
	 * @param resourceType
	 * @param resourceId
	 * @param targetType
	 * @param targetId
	 * @param moduleId
	 * @param privilegeIds
     * @param type
     */
	void deleteAcls(String resourceType, Long resourceId, String targetType, Long targetId, Long moduleId, List<Long> privilegeIds, ServiceModulePrivilegeType type);

	/**
	 *
	 * @param resourceType
	 * @param resourceId
	 * @param targetType
	 * @param targetId
     * @param privilegeIds
     */
	void deleteAcls(String resourceType, Long resourceId, String targetType, Long targetId, List<Long> privilegeIds);

	/**
	 *
	 * @param resourceType
	 * @param resourceId
	 * @param targetType
	 * @param targetId
     */
	void deleteAcls(String resourceType, Long resourceId, String targetType, Long targetId);

	/**
	 *
	 * @param resourceType
	 * @param resourceId
	 * @param targetType
	 * @param targetId
	 * @param moduleIds
     * @param type
     */
	void deleteAcls(String resourceType, Long resourceId, String targetType, Long targetId, List<Long> moduleIds, ServiceModulePrivilegeType type);

	OrganizationContactDTO createOrganizationAdmin(CreateOrganizationAdminCommand cmd, Integer namespaceId);

	/**
	 * 获取权限列表
	 * @param ownerType
	 * @param ownerId
	 * @param targetType
	 * @param targetId
     * @return
     */
	List<Privilege> listPrivilegesByTarget(String ownerType, Long ownerId, String targetType, Long targetId, String scope);

	List<CommunityDTO> listUserRelatedProjectByModuleId(ListUserRelatedProjectByModuleIdCommand cmd);

	List<Long> listUserRelatedPrivilegeByModuleId(ListUserRelatedPrivilegeByModuleIdCommand cmd);

	List<Long> listUserPrivilegeByModuleId(Integer namespaceId, String ownerType, Long ownerId, Long organizationId, Long userId, Long moduleId);

	List<Long> getPrivilegeIdsByRoleId(ListPrivilegesByRoleIdCommand cmd);

	void createRole(CreateRoleCommand cmd);

	void updateRole(UpdateRoleCommand cmd);
	/**
	 * 获取项目分类的树状数据结构
	 * @param namespaceId
	 * @param projects
     * @return
     */
	List<ProjectDTO> getTreeProjectCategories(Integer namespaceId, List<ProjectDTO> projects);

	List<ProjectDTO> getTreeProjectCategories(Integer namespaceId, List<ProjectDTO> projects, CommunityFetchType fetchType);

	List<RoleAuthorizationsDTO> listRoleAdministrators(ListRoleAdministratorsCommand cmd);

	void deleteRoleAdministrators(DeleteRoleAdministratorsCommand cmd);

	void createRoleAdministrators(CreateRoleAdministratorsCommand cmd);

	void updateRoleAdministrators(CreateRoleAdministratorsCommand cmd);

	RoleAuthorizationsDTO checkRoleAdministrators(CheckRoleAdministratorsCommand cmd);

	AclPrivilegeInfoResponse getPrivilegeInfosByRoleId(
			ListPrivilegesByRoleIdCommand cmd);

//	@Deprecated
//	void updateServiceModuleAdministrators(UpdateServiceModuleAdministratorsCommand cmd);

	void createAuthorizationRelation(CreateAuthorizationRelationCommand cmd);

	ListAuthorizationRelationsResponse listAuthorizationRelations(ListAuthorizationRelationsCommand cmd);

	void updateAuthorizationRelation(UpdateAuthorizationRelationCommand cmd);

	void deleteAuthorizationRelation(DeleteAuthorizationRelationCommand cmd);

	List<ServiceModuleDTO> listServiceModulesByTarget(ListServiceModulesByTargetCommand cmd);

	void assignmentAclRole(String ownerType, Long ownerId, String targetType, Long targetId, Integer namespaceId, Long creatorUid, Long roleId);

	//	批量增加管理员 added by R 20170912.
	void createOrganizationSuperAdmins(CreateOrganizationAdminsCommand cmd);
	//	移交管理员权限 added by R 20170912.
    void transferOrganizationSuperAdmin(TransferOrganizationSuperAdminCommand cmd);

    // 新版设置模块管理员 added by lei.lv 20171101
	void resetServiceModuleAdministrators(ResetServiceModuleAdministratorsCommand cmd);

	// 获取所有应用管理员的targetIds
	List<Long> listServiceModuleAppsAdministratorTargetIds(ListServiceModuleAdministratorsCommand cmd);

//TODO  冲突，先注释
	List<ProjectDTO> handleTreeProject(List<ProjectDTO> projects);

	OrganizationContactDTO listOrganizationTopAdministrator(ListServiceModuleAdministratorsCommand cmd);

	GetAdministratorInfosByUserIdResponse getAdministratorInfosByUserId(GetAdministratorInfosByUserIdCommand cmd);

	ListOrganizationContectDTOResponse listOrganizationSystemAdministrators(ListServiceModuleAdministratorsCommand cmd);

	GetPersonelInfoByTokenResponse getPersonelInfoByToken(GetPersonelInfoByTokenCommand cmd);

	GetAdministratorInfosByUserIdResponse updateTopAdminstrator(CreateOrganizationAdminCommand cmd);

	String findTopAdminByOrgId(FindTopAdminByOrgIdCommand cmd);

	boolean checkIsSystemOrAppAdmin(Long orgId, Long userId);

	void updateSuperAdmin(createSuperAdminCommand cmd);

	OrganizationContactDTO createOrganizationAdmin(Long organizationId, String contactName,
												   String contactToken,
												   Long adminPrivilegeId,
												   Long roleId ,boolean notSendMsgFlag ,boolean realSuperAdmin);


}
