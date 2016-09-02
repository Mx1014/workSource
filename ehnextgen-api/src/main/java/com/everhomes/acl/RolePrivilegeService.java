// @formatter:off
package com.everhomes.acl;

import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.web.multipart.MultipartFile;

import com.everhomes.rest.acl.admin.AddAclRoleAssignmentCommand;
import com.everhomes.rest.acl.admin.BatchAddTargetRoleCommand;
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
import com.everhomes.rest.acl.admin.UpdateOrganizationAdminCommand;
import com.everhomes.rest.acl.admin.UpdateRolePrivilegeCommand;
import com.everhomes.rest.organization.ListOrganizationAdministratorCommand;
import com.everhomes.rest.organization.ListOrganizationMemberCommandResponse;
import com.everhomes.rest.user.admin.ImportDataResponse;

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
	void createRolePrivilege(CreateRolePrivilegeCommand cmd);
	
	/**
	 * 修改角色权限
	 * @param cmd
	 */
	void updateRolePrivilege(UpdateRolePrivilegeCommand cmd);
	
	/**
	 * 删除角色权限
	 * @param cmd
	 */
	void deleteRolePrivilege(DeleteRolePrivilegeCommand cmd);
	
	/**
	 * 获取角色权限
	 * @param cmd
	 * @return
	 */
	List<ListWebMenuPrivilegeDTO> qryRolePrivileges(QryRolePrivilegesCommand cmd);
	
	/**
	 * 根据机构获取角色列表
	 * @param cmd
	 * @return
	 */
	List<RoleDTO> listAclRoleByOrganizationId(ListAclRolesCommand cmd);
	
	/**
	 * 判断是否是系统管理员
	 * @param organizationId
	 * @return
	 */
	boolean checkAdministrators(Long organizationId);
	
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
	void createOrganizationSuperAdmin(CreateOrganizationAdminCommand cmd);
	
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
	 * 删除管理员
	 * @param cmd
	 */
	void deleteOrganizationAdmin(DeleteOrganizationAdminCommand cmd);
	
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
	
}
