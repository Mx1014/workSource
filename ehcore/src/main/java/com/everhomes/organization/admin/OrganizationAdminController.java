package com.everhomes.organization.admin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import com.everhomes.rest.acl.PrivilegeConstants;
import com.everhomes.rest.common.ImportFileResponse;
import com.everhomes.rest.common.ServiceModuleConstants;
import com.everhomes.rest.community.admin.OperateType;
import com.everhomes.rest.launchpad.ActionType;
import com.everhomes.rest.organization.*;

import com.everhomes.user.UserPrivilegeMgr;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestReturn;
import com.everhomes.entity.EntityType;
import com.everhomes.organization.OrganizationService;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.acl.PrivilegeConstants;
import com.everhomes.rest.acl.RoleConstants;
import com.everhomes.rest.acl.admin.AclRoleAssignmentsDTO;
import com.everhomes.rest.common.ImportFileResponse;
import com.everhomes.rest.common.ServiceModuleConstants;
import com.everhomes.rest.enterprise.*;
import com.everhomes.rest.forum.ListPostCommandResponse;
import com.everhomes.rest.launchpad.ActionType;
import com.everhomes.rest.organization.*;
import com.everhomes.rest.organization.CreateOrganizationOwnerCommand;
import com.everhomes.rest.organization.DeleteOrganizationOwnerCommand;
import com.everhomes.rest.organization.pm.*;
import com.everhomes.rest.user.UserServiceErrorCode;
import com.everhomes.rest.user.UserTokenCommand;
import com.everhomes.rest.user.UserTokenCommandResponse;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserPrivilegeMgr;
import com.everhomes.user.admin.SystemUserPrivilegeMgr;
import com.everhomes.util.RequireAuthentication;
import com.everhomes.util.RuntimeErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin/org")
public class OrganizationAdminController extends ControllerBase {
    private static final Logger LOGGER = LoggerFactory.getLogger(OrganizationAdminController.class);

    @Autowired
    OrganizationService organizationService;

    @Autowired
    UserPrivilegeMgr userPrivilegeMgr;

    /**
     * <b>URL: /admin/org/createOrganization</b>
     * <p>创建政府机构</p>
     */
    @RequestMapping("createOrganization")
    @RestReturn(value = String.class)
    public RestResponse createOrganizationByAdmin(@Valid CreateOrganizationByAdminCommand cmd) {
        SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
        resolver.checkUserPrivilege(UserContext.current().getUser().getId(), 0);

        organizationService.createOrganizationByAdmin(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /admin/org/createOrganizationMember</b>
     * <p>创建政府机构成员</p>
     */
    @RequestMapping("createOrganizationMember")
    @RestReturn(value = String.class)
    public RestResponse createOrganizationMember(@Valid CreateOrganizationMemberCommand cmd) {
        SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
        //resolver.checkUserPrivilege(UserContext.current().getUser().getId(), 0);
        organizationService.createOrganizationMember(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /admin/org/findUserByIndentifier</b>
     * <p>根据用户token查询用户信息</p>
     */
    @RequestMapping("findUserByIndentifier")
    @RestReturn(value = UserTokenCommandResponse.class)
    public RestResponse findUserByIndentifier(@Valid UserTokenCommand cmd) {
        SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
        resolver.checkUserPrivilege(UserContext.current().getUser().getId(), 0);

        UserTokenCommandResponse commandResponse = organizationService.findUserByIndentifier(cmd);
        RestResponse response = new RestResponse(commandResponse);

        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /admin/org/createOrganizationCommunity</b>
     * <p>创建政府机构对应的小区列表</p>
     *
     * @return 添加的结果
     */
    @RequestMapping("createOrganizationCommunity")
    @RestReturn(value = String.class)
    public RestResponse createOrganizationCommunity(@Valid CreateOrganizationCommunityCommand cmd) {
        SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
        resolver.checkUserPrivilege(UserContext.current().getUser().getId(), 0);

        organizationService.createOrganizationCommunity(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /admin/org/createPropertyOrganization</b>
     * <p>创建小区的物业机构</p>
     *
     * @return 添加的结果
     */
    @RequestMapping("createPropertyOrganization")
    @RestReturn(value = String.class)
    public RestResponse createPropertyOrganization(@Valid CreatePropertyOrganizationCommand cmd) {
        SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
        resolver.checkUserPrivilege(UserContext.current().getUser().getId(), 0);

        organizationService.createPropertyOrganization(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /admin/org/listOrganizations</b>
     * <p>列出政府机构表（）</p>
     */
    @RequestMapping("listOrganizations")
    @RestReturn(value = ListOrganizationsCommandResponse.class)
    public RestResponse listOrganizations(@Valid ListOrganizationsCommand cmd) {
        SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
        resolver.checkUserPrivilege(UserContext.current().getUser().getId(), 0);

        ListOrganizationsCommandResponse commandResponse = organizationService.listOrganizations(cmd);
        RestResponse response = new RestResponse(commandResponse);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /admin/org/listOrgMembers</b>
     * <p>查询政府机构成员列表</p>
     */
    @RequestMapping("listOrgMembers")
    @RestReturn(value = ListOrganizationMemberCommandResponse.class)
    public RestResponse listOrgMembers(@Valid ListOrganizationMemberCommand cmd) {
        SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
        //resolver.checkUserPrivilege(UserContext.current().getUser().getId(), 0);

        ListOrganizationMemberCommandResponse commandResponse = organizationService.listOrgMembers(cmd);
        RestResponse response = new RestResponse(commandResponse);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /admin/org/deleteOrganizationMember</b>
     * <p>删除政府机构成员</p>
     */
    @RequestMapping("deleteOrganizationMember")
    @RestReturn(value = String.class)
    public RestResponse deleteOrganizationMember(@Valid DeleteOrganizationIdCommand cmd) {
        SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
        resolver.checkUserPrivilege(UserContext.current().getUser().getId(), 0);

        organizationService.deleteOrganizationMember(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }


    @RequestMapping("addOrgContact")
    @RestReturn(value = String.class)
    public RestResponse addOrgContact(@Valid CreateOrganizationContactCommand cmd) {
        organizationService.createOrgContact(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    @RequestMapping("addOrgAddress")
    @RestReturn(value = String.class)
    public RestResponse addOrgAddress(@Valid AddOrgAddressCommand cmd) {
        organizationService.addOrgAddress(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /org/importOrganization</b>
     * <p>导入机构信息</p>
     */
    @RequestMapping("importOrganization")
    @RestReturn(value = String.class)
    public RestResponse importOrganization(@RequestParam(value = "attachment") MultipartFile[] files) {
        this.organizationService.importOrganization(files);
        RestResponse response = new RestResponse("服务器正异步处理数据。请耐心等待。不能重复上传。");
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /org/importOrgPost</b>
     * <p>导入机构公告，报修等帖</p>
     */
    @RequestMapping("importOrgPost")
    @RestReturn(value = String.class)
    public RestResponse importOrgPost(@RequestParam(value = "attachment") MultipartFile[] files) {
        this.organizationService.importOrgPost(files);
        RestResponse response = new RestResponse("服务器正异步处理数据。请耐心等待。不能重复上传。");
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /admin/org/addPmBuilding</b>
     * <p>增加物业管辖小区楼栋</p>
     */
    @RequestMapping("addPmBuilding")
    @RestReturn(value = String.class)
    public RestResponse addPmBuilding(AddPmBuildingCommand cmd) {

        SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
        //resolver.checkUserPrivilege(UserContext.current().getUser().getId(), 0);

        this.organizationService.addPmBuilding(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /admin/org/deletePmCommunity</b>
     * <p>删除物业管辖小区</p>
     */
    @RequestMapping("deletePmCommunity")
    @RestReturn(value = String.class)
    public RestResponse deletePmCommunity(DeletePmCommunityCommand cmd) {

        SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
//        resolver.checkUserPrivilege(UserContext.current().getUser().getId(), 0);

        this.organizationService.deletePmCommunity(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /org/listPmBuildings</b>
     * <p>查询物业管理楼栋列表</p>
     */
    @RequestMapping("listPmBuildings")
    @RestReturn(value = PmBuildingDTO.class, collection = true)
    public RestResponse listPmBuildings(ListPmBuildingCommand cmd) {

        SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
        // resolver.checkUserPrivilege(UserContext.current().getUser().getId(), 0);

        List<PmBuildingDTO> list = this.organizationService.listPmBuildings(cmd);
        RestResponse response = new RestResponse(list);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /org/listUnassignedBuilding</b>
     * <p>查询物业管理小区内未管理到的楼栋列表</p>
     */
    @RequestMapping("listUnassignedBuilding")
    @RestReturn(value = UnassignedBuildingDTO.class, collection = true)
    public RestResponse listUnassignedBuilding(ListPmBuildingCommand cmd) {

        SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
        //resolver.checkUserPrivilege(UserContext.current().getUser().getId(), 0);

        List<UnassignedBuildingDTO> list = this.organizationService.listUnassignedBuilding(cmd);
        RestResponse response = new RestResponse(list);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /org/listPmManagements</b>
     * <p>查询物业管理列表</p>
     */

    @RequestMapping("listPmManagements")
    @RestReturn(value = PmManagementsResponse.class)
    public RestResponse listPmManagements(ListPmManagementsCommand cmd) {

        SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
        // resolver.checkUserPrivilege(UserContext.current().getUser().getId(), 0);

        PmManagementsResponse list = this.organizationService.listPmManagements(cmd);
        RestResponse response = new RestResponse(list);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /admin/org/listPmManagementComunites</b>
     * <p>查询物业管理小区列表</p>
     */

    @RequestMapping("listPmManagementComunites")
    @RestReturn(value = PmManagementCommunityDTO.class, collection = true)
    public RestResponse listPmManagementComunites(ListPmManagementComunitesCommand cmd) {

        SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
        // resolver.checkUserPrivilege(UserContext.current().getUser().getId(), 0);

        List<PmManagementCommunityDTO> dtos = this.organizationService.listPmManagementComunites(cmd);
        RestResponse response = new RestResponse(dtos);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /admin/org/createDepartment</b>
     * <p>创建部门</p>
     */
    @RequestMapping("createDepartment")
    @RestReturn(value = String.class)
    public RestResponse createDepartment(CreateDepartmentCommand cmd) {

        SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
        //resolver.checkUserPrivilege(UserContext.current().getUser().getId(), 0);

        organizationService.createDepartment(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /admin/org/getUserResourcePrivilege</b>
     * <p>获取权限资源</p>
     */
    @RequestMapping("getUserResourcePrivilege")
    @RestReturn(value = Long.class, collection = true)
    public RestResponse getUserResourcePrivilege(@Valid GetUserResourcePrivilege cmd) {
        List<Long> res = organizationService.getUserResourcePrivilege(UserContext.current().getUser().getId(), cmd.getOrganizationId());
        RestResponse response = new RestResponse(res);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }


    /**
     * <b>URL: /admin/org/createEnterprise</b>
     * <p>创建企业</p>
     */
    @RequestMapping("createEnterprise")
    @RestReturn(value = String.class)
    public RestResponse createEnterprise(@Valid CreateEnterpriseCommand cmd) {
        cmd.setCheckPrivilege(true);
        organizationService.createEnterprise(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /admin/org/createStandardEnterprise</b>
     * <p>创建企业（标准版）</p>
     */
    @RequestMapping("createStandardEnterprise")
    @RestReturn(value = OrganizationDTO.class)
    public RestResponse createStandardEnterprise(@Valid CreateEnterpriseStandardCommand cmd) {
//        cmd.setCheckPrivilege(true);
        OrganizationDTO organizationDTO = organizationService.createStandardEnterprise(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /admin/org/createSettledEnterprise</b>
     * <p>添加入驻企业（标准版）</p>
     * @param cmd
     * @return
     */
    @RequestMapping("createSettledEnterprise")
    @RestReturn(value = String.class)
    public RestResponse createSettledEnterprise(CreateSettledEnterpriseCommand cmd){
        organizationService.createSettledEnterprise(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /admin/org/updateEnterprise</b>
     * <p>修改企业</p>
     */
    @RequestMapping("updateEnterprise")
    @RestReturn(value = String.class)
    public RestResponse updateEnterprise(@Valid UpdateEnterpriseCommand cmd) {
        organizationService.updateEnterprise(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /admin/org/deleteEnterpriseById</b>
     * <p>删除</p>
     */
    @RequestMapping("deleteEnterpriseById")
    @RestReturn(value = String.class)
    public RestResponse deleteEnterpriseById(@Valid DeleteOrganizationIdCommand cmd) {
        organizationService.deleteEnterpriseById(cmd, true);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }


    /**
     * <b>URL: /admin/org/listDepartments</b>
     * <p>列出机构部门表（）</p>
     */
    @RequestMapping("listDepartments")
    @RestReturn(value = ListDepartmentsCommandResponse.class)
    public RestResponse listDepartments(@Valid ListDepartmentsCommand cmd) {
        SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
        //resolver.checkUserPrivilege(UserContext.current().getUser().getId(), 0);
        List<String> groupTypes = new ArrayList<String>();
        groupTypes.add(OrganizationGroupType.DEPARTMENT.getCode());
        RestResponse response = new RestResponse(organizationService.listAllChildrenOrganizations(cmd.getParentId(), groupTypes));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /admin/org/listAllChildrenOrganizations</b>
     * <p>获取层级菜单</p>
     */
    @RequestMapping("listAllChildrenOrganizations")
    @RestReturn(value = OrganizationMenuResponse.class)
    public RestResponse listAllChildrenOrganizations(@Valid ListAllChildrenOrganizationsCommand cmd) {
        RestResponse response = new RestResponse(organizationService.listAllChildrenOrganizationMenus(cmd.getId(), cmd.getGroupTypes(), cmd.getNaviFlag()));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /admin/org/listChildrenOrganizations</b>
     * <p>子机构列表</p>
     */
    @RequestMapping("listChildrenOrganizations")
    @RestReturn(value = ListOrganizationsCommandResponse.class)
    public RestResponse listChildrenOrganizations(@Valid ListAllChildrenOrganizationsCommand cmd) {
        RestResponse response = new RestResponse(organizationService.listChildrenOrganizations(cmd.getId(), cmd.getGroupTypes(), cmd.getKeywords()));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /admin/org/createChildrenEnterprise</b>
     * <p>创建子公司</p>
     */
    @RequestMapping("createChildrenEnterprise")
    @RestReturn(value = String.class, collection = true)
    public RestResponse createChildrenEnterprise(@Valid CreateOrganizationCommand cmd) {
        cmd.setGroupType(OrganizationGroupType.ENTERPRISE.getCode());
        organizationService.createChildrenOrganization(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /admin/org/createChildrenGroup</b>
     * <p>创建群组</p>
     */
    @RequestMapping("createChildrenGroup")
    @RestReturn(value = String.class, collection = true)
    public RestResponse createChildrenGroup(@Valid CreateOrganizationCommand cmd) {
        cmd.setGroupType(OrganizationGroupType.GROUP.getCode());
        organizationService.createChildrenOrganization(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /admin/org/createChildrenOrganization</b>
     * <p>创建子机构</p>
     */
    @RequestMapping("createChildrenOrganization")
    @RestReturn(value = OrganizationDTO.class)
    public RestResponse createChildrenOrganization(@Valid CreateOrganizationCommand cmd) {
        RestResponse response = new RestResponse(organizationService.createChildrenOrganization(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /admin/org/createChildrenDepartment</b>
     * <p>创建部门</p>
     */
    @RequestMapping("createChildrenDepartment")
    @RestReturn(value = String.class, collection = true)
    public RestResponse createChildrenDepartment(@Valid CreateOrganizationCommand cmd) {
        cmd.setGroupType(OrganizationGroupType.DEPARTMENT.getCode());
        organizationService.createChildrenOrganization(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /admin/org/updateOrganization</b>
     * <p>修改机构名称</p>
     */
    @RequestMapping("updateOrganization")
    @RestReturn(value = String.class, collection = true)
    public RestResponse updateOrganization(@Valid UpdateOrganizationsCommand cmd) {
        organizationService.updateChildrenOrganization(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /admin/org/deleteOrganizationById</b>
     * <p>删除机构</p>
     *
     * @return 删除的结果
     */
    @RequestMapping("deleteOrganizationById")
    @RestReturn(value = String.class)
    public RestResponse deleteOrganizationById(@Valid DeleteOrganizationIdCommand cmd) {
//        SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
        RestResponse response = new RestResponse(organizationService.deleteOrganization(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }


    /**
     * <b>URL: /admin/org/setOrganizationRole</b>
     * <p>设置机构role</p>
     */
    @RequestMapping("setOrganizationRole")
    @RestReturn(value = String.class, collection = true)
    public RestResponse setOrganizationRole(@Valid SetAclRoleAssignmentCommand cmd) {
        organizationService.setAclRoleAssignmentRole(cmd, EntityType.ORGANIZATIONS);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /admin/org/setOrganizationPersonnelRole</b>
     * <p>设置机构人员role</p>
     */
    @RequestMapping("setOrganizationPersonnelRole")
    @RestReturn(value = String.class, collection = true)
    public RestResponse setOrganizationPersonnelRole(@Valid SetAclRoleAssignmentCommand cmd) {
        organizationService.setAclRoleAssignmentRole(cmd, EntityType.USER);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /admin/org/approveForEnterpriseContact</b>
     * <p>审批通过认证申请</p>
     *
     * @return {@link String}
     */
    @RequestMapping("approveForEnterpriseContact")
    @RestReturn(value = String.class)
    public RestResponse approveForEnterpriseContact(@Valid ApproveContactCommand cmd) {
        cmd.setOperateType(OperateType.MANUAL.getCode());
        this.organizationService.approveForEnterpriseContact(cmd);
        RestResponse res = new RestResponse();
        res.setErrorCode(ErrorCodes.SUCCESS);
        res.setErrorDescription("OK");

        return res;
    }

    /**
     * <b>URL: /admin/org/verifyEnterpriseContact</b>
     * <p>通过点击邮箱认证通过认证申请</p>
     * @return {@link String}
     */
    @RequestMapping("verifyEnterpriseContact")
    @RestReturn(value=String.class)
    @RequireAuthentication(false)
    public RestResponse verifyEnterpriseContact(@Valid VerifyEnterpriseContactCommand cmd) {
//    	this.organizationService.verifyEnterpriseContact(cmd);
    	 RestResponse res = new RestResponse();
         res.setErrorCode(ErrorCodes.SUCCESS);
         res.setErrorDescription("OK");
         
         return res;
    }
    
    /**
     * <b>URL: /admin/org/batchApproveForEnterpriseContact</b>
     * <p>批量审批通过认证申请</p>
     *
     * @return {@link String}
     */
    @RequestMapping("batchApproveForEnterpriseContact")
    @RestReturn(value = String.class)
    public RestResponse batchApproveForEnterpriseContact(@Valid BatchApproveContactCommand cmd) {
        this.organizationService.batchApproveForEnterpriseContact(cmd);
        RestResponse res = new RestResponse();
        res.setErrorCode(ErrorCodes.SUCCESS);
        res.setErrorDescription("OK");

        return res;
    }

    /**
     * <b>URL: /admin/org/rejectForEnterpriseContact</b>
     * <p>审批拒绝认证申请</p>
     *
     * @return {@link String}
     */
    @RequestMapping("rejectForEnterpriseContact")
    @RestReturn(value = String.class)
    public RestResponse rejectForEnterpriseContact(@Valid RejectContactCommand cmd) {
        this.organizationService.rejectForEnterpriseContact(cmd);
        RestResponse res = new RestResponse();
        res.setErrorCode(ErrorCodes.SUCCESS);
        res.setErrorDescription("OK");

        return res;
    }


    /**
     * <b>URL: /admin/org/batchRejectForEnterpriseContact</b>
     * <p>批量审批拒绝认证申请</p>
     *
     * @return {@link String}
     */
    @RequestMapping("batchRejectForEnterpriseContact")
    @RestReturn(value = String.class)
    public RestResponse batchRejectForEnterpriseContact(@Valid BatchRejectContactCommand cmd) {
        this.organizationService.batchRejectForEnterpriseContact(cmd);
        RestResponse res = new RestResponse();
        res.setErrorCode(ErrorCodes.SUCCESS);
        res.setErrorDescription("OK");

        return res;
    }

    /**
     * <b>URL: /admin/org/updatePersonnelsToDepartment</b>
     * <p>批量调整员工到部门</p>
     */
    @RequestMapping("updatePersonnelsToDepartment")
    @RestReturn(value = String.class, collection = true)
    public RestResponse updatePersonnelsToDepartment(@Valid UpdatePersonnelsToDepartment cmd) {
        organizationService.updatePersonnelsToDepartment(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /admin/org/addPersonnelsToGroup</b>
     * <p>批量添加的组</p>
     */
    @RequestMapping("addPersonnelsToGroup")
    @RestReturn(value = String.class, collection = true)
    public RestResponse addPersonnelsToGroup(@Valid AddPersonnelsToGroup cmd) {
        organizationService.addPersonnelsToGroup(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /admin/org/listPersonnelNotJoinGroups</b>
     * <p>查询未加入组的人员</p>
     */
    @Deprecated
    @RequestMapping("listPersonnelNotJoinGroups")
    @RestReturn(value = ListOrganizationMemberCommandResponse.class)
    public RestResponse listPersonnelNotJoinGroups(@Valid ListPersonnelNotJoinGroupCommand cmd) {
        RestResponse response = new RestResponse(organizationService.listPersonnelNotJoinGroups(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /admin/org/listOrganizationPersonnels</b>
     * <p>通讯录列表</p>
     */
    @RequestMapping("listOrganizationPersonnels")
    @RestReturn(value = ListOrganizationMemberCommandResponse.class)
    public RestResponse listOrganizationPersonnels(@Valid ListOrganizationContactCommand cmd) {
        ListOrganizationMemberCommandResponse res = organizationService.listOrganizationPersonnelsWithDownStream(cmd);
        RestResponse response = new RestResponse(res);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /admin/org/listOrgAuthPersonnels</b>
     * <p>认证通讯录列表</p>
     */
    @RequestMapping("listOrgAuthPersonnels")
    @RestReturn(value = ListOrganizationMemberCommandResponse.class)
    public RestResponse listOrgAuthPersonnels(@Valid ListOrganizationContactCommand cmd) {
        ListOrganizationMemberCommandResponse res = organizationService.listOrgAuthPersonnels(cmd);
        RestResponse response = new RestResponse(res);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /admin/org/listAllOrganizationPersonnels</b>
     * <p>查询园区全部的员工（）</p>
     */
    @RequestMapping("listAllOrganizationPersonnels")
    @RestReturn(value = ListOrganizationMemberCommandResponse.class)
    public RestResponse listAllOrganizationPersonnels(@Valid ListOrganizationMemberCommand cmd) {
        SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
        // resolver.checkUserPrivilege(UserContext.current().getUser().getId(), 0);

        ListOrganizationMemberCommandResponse commandResponse = organizationService.listParentOrganizationPersonnels(cmd);
        RestResponse response = new RestResponse(commandResponse);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

//    /**
//     * <b>URL: /admin/org/createOrganizationPersonnel</b>
//     * <p>添加机构成员</p>
//     */
//    @RequestMapping("createOrganizationPersonnel")
//    @RestReturn(value = String.class)
//    public RestResponse createOrganizationPersonnel(@Valid CreateOrganizationMemberCommand cmd) {
//        OrganizationMemberDTO dto = organizationService.createOrganizationPersonnel(cmd);
//        RestResponse response = new RestResponse(dto);
//        response.setErrorCode(ErrorCodes.SUCCESS);
//        response.setErrorDescription("OK");
//        return response;
//    }

    /**
     * <b>URL: /admin/org/updateOrganizationPersonnel</b>
     * <p>修改机构成员</p>
     */
    @RequestMapping("updateOrganizationPersonnel")
    @RestReturn(value = String.class)
    public RestResponse updateOrganizationPersonnel(@Valid UpdateOrganizationMemberCommand cmd) {
        organizationService.updateOrganizationPersonnel(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /admin/org/deleteOrganizationPersonnel</b>
     * <p>删除机构成员</p>
     */
    @RequestMapping("deleteOrganizationPersonnel")
    @RestReturn(value = String.class)
    public RestResponse deleteOrganizationPersonnel(@Valid DeleteOrganizationIdCommand cmd) {
        organizationService.deleteOrganizationMember(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /admin/org/verifyPersonnelByPhone</b>
     * <p>判断成员是否已经存在</p>
     */
    @RequestMapping("verifyPersonnelByPhone")
    @RestReturn(value = OrganizationMemberDTO.class)
    public RestResponse verifyPersonnelByPhone(@Valid VerifyPersonnelByPhoneCommand cmd) {
        RestResponse response = new RestResponse(organizationService.verifyPersonnelByPhone(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }


    /**
     * <b>URL: /admin/org/createOrganizationAccount</b>
     * <p>开通账号</p>
     */
    @RequestMapping("createOrganizationAccount")
    @RestReturn(value = String.class)
    public RestResponse createOrganizationAccount(@Valid CreateOrganizationAccountCommand cmd) {
        organizationService.createOrganizationAccount(cmd, RoleConstants.ENTERPRISE_SUPER_ADMIN);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }


    /**
     * <b>URL: /admin/org/listEnterpriseByCommunityId</b>
     * <p>后台管理 企业列表 和对于的管理员信息</p>
     */
    @RequestMapping("listEnterpriseByCommunityId")
    @RestReturn(value = OrganizationDetailDTO.class, collection = true)
    public RestResponse listEnterpriseByCommunityId(@Valid ListEnterprisesCommand cmd) {
//        cmd.setQryAdminRoleFlag(false);
        userPrivilegeMgr.checkUserPrivilege(UserContext.currentUserId(), cmd.getManageOrganizationId(), PrivilegeConstants.ORGANIZATION_LIST, ServiceModuleConstants.ORGANIZATION_MODULE, ActionType.OFFICIAL_URL.getCode(), null, cmd.getManageOrganizationId(), cmd.getCommunityId());
        ListEnterprisesCommandResponse res = organizationService.listEnterprises(cmd);
        RestResponse response = new RestResponse(res);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /admin/org/checkSetAdminPrivilege</b>
     * <p>后台管理 企业列表 校验是否有设置管理员的权限</p>
     */
    @RequestMapping("checkSetAdminPrivilege")
    @RestReturn(value = String.class)
    public RestResponse checkSetAdminPrivilege(@Valid CheckSetAdminPrivilegeCommand cmd) {
        userPrivilegeMgr.checkUserPrivilege(UserContext.currentUserId(), cmd.getManageOrganizationId(), PrivilegeConstants.ORGANIZATION_SET_ADMIN, ServiceModuleConstants.ORGANIZATION_MODULE, ActionType.OFFICIAL_URL.getCode(), null, cmd.getManageOrganizationId(), cmd.getCommunityId());
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /admin/org/exportEnterpriseByCommunityId</b>
     * <p>后台管理 企业列表 和对于的管理员信息</p>
     */
    @RequestMapping("exportEnterpriseByCommunityId")
    @RestReturn(value = String.class)
    public RestResponse exportEnterpriseByCommunityId(@Valid ListEnterprisesCommand cmd, HttpServletResponse httpServletResponse) {
    	cmd.setQryAdminRoleFlag(false);
    	organizationService.exportEnterprises(cmd, httpServletResponse);
    	RestResponse response = new RestResponse();
    	response.setErrorCode(ErrorCodes.SUCCESS);
    	response.setErrorDescription("OK");
    	return response;
    }
    
    /**
     * <b>URL: /admin/org/listAclRoleByUserId</b>
     * <p>获取角色列表</p>
     */
    @RequestMapping("listAclRoleByUserId")
    @RestReturn(value = OrganizationDetailDTO.class, collection = true)
    public RestResponse listAclRoleByUserId(@Valid ListAclRoleByUserIdCommand cmd) {
        List<AclRoleAssignmentsDTO> dtos = organizationService.listAclRoleByUserId(cmd);
        RestResponse response = new RestResponse(dtos);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /admin/org/importEnterpriseData</b>
     * <p>导入企业信息</p>
     */
    @RequestMapping("importEnterpriseData")
    @RestReturn(value = ImportFileTaskDTO.class)
    public RestResponse importEnterpriseData(@Valid ImportEnterpriseDataCommand cmd, @RequestParam(value = "attachment") MultipartFile[] files) {
        User manaUser = UserContext.current().getUser();
        Long userId = manaUser.getId();
        if (null == files || null == files[0]) {
            LOGGER.error("files is null。userId=" + userId);
            throw RuntimeErrorException.errorWith(UserServiceErrorCode.SCOPE, UserServiceErrorCode.ERROR_INVALID_PARAMS,
                    "files is null");
        }
        RestResponse response = new RestResponse(organizationService.importEnterpriseData(cmd, files[0], userId));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
//    
//    /**
//     * <b>URL: /admin/org/importEnterpriseData</b>
//     * <p>导入企业信息</p>
//     */
//    @RequestMapping("importEnterpriseData")
//    @RestReturn(value = ImportFileResponse.class)
//    public RestResponse importEnterpriseData(@Valid ImportEnterpriseDataCommand cmd, @RequestParam(value = "attachment") MultipartFile[] files) {
//    	User manaUser = UserContext.current().getUser();
//    	Long userId = manaUser.getId();
//    	if (null == files || null == files[0]) {
//    		LOGGER.error("files is null。userId=" + userId);
//    		throw RuntimeErrorException.errorWith(UserServiceErrorCode.SCOPE, UserServiceErrorCode.ERROR_INVALID_PARAMS,
//    				"files is null");
//    	}
//    	ImportFileResponse<ImportEnterpriseDataDTO> importFileResponse = this.organizationService.importEnterpriseData(files[0], userId, cmd);
//    	RestResponse response = new RestResponse(importFileResponse);
//    	response.setErrorCode(ErrorCodes.SUCCESS);
//    	response.setErrorDescription("OK");
//    	return response;
//    }

    /**
     * <b>URL: /admin/org/importOrganizationPersonnelData</b>
     * <p>导入机构成员信息</p>
     */
    @RequestMapping("importOrganizationPersonnelData")
    @RestReturn(value = ImportFileTaskDTO.class)
    public RestResponse importOrganizationPersonnelData(@Valid ImportOrganizationPersonnelDataCommand cmd, @RequestParam(value = "attachment") MultipartFile[] files) {

        User manaUser = UserContext.current().getUser();
        Long userId = manaUser.getId();
        if (null == files || null == files[0]) {
            LOGGER.error("files is null, userId=" + userId);
            throw RuntimeErrorException.errorWith(UserServiceErrorCode.SCOPE, UserServiceErrorCode.ERROR_INVALID_PARAMS,
                    "files is null");
        }
        RestResponse response = new RestResponse(organizationService.importOrganizationPersonnelData(files[0], userId, cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /admin/org/listMyTaskTopics</b>
     * <p>我的任务</p>
     */
    @RequestMapping("listMyTaskTopics")
    @RestReturn(value = ListPostCommandResponse.class)
    public RestResponse listMyTaskTopics(@Valid ListTopicsByTypeCommand cmd) {
        ListPostCommandResponse res = organizationService.listMyTaskTopics(cmd);
        RestResponse response = new RestResponse(res);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /admin/org/listAllTaskTopics</b>
     * <p>全部任务</p>
     */
    @RequestMapping("listAllTaskTopics")
    @RestReturn(value = ListPostCommandResponse.class)
    public RestResponse listAllTaskTopics(@Valid ListTopicsByTypeCommand cmd) {
        ListPostCommandResponse res = organizationService.listAllTaskTopics(cmd);
        RestResponse response = new RestResponse(res);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /admin/org/acceptTask</b>
     * <p>接受任务</p>
     */
    @RequestMapping("acceptTask")
    @RestReturn(value = String.class)
    public RestResponse acceptTask(@Valid ProcessOrganizationTaskCommand cmd) {
        organizationService.acceptTask(cmd);
        RestResponse res = new RestResponse();
        res.setErrorCode(ErrorCodes.SUCCESS);
        res.setErrorDescription("OK");

        return res;
    }

    /**
     * <b>URL: /admin/org/refuseTask</b>
     * <p>拒绝任务</p>
     */
    @RequestMapping("refuseTask")
    @RestReturn(value = String.class)
    public RestResponse refuseTask(@Valid ProcessOrganizationTaskCommand cmd) {
        organizationService.refuseTask(cmd);
        RestResponse res = new RestResponse();
        res.setErrorCode(ErrorCodes.SUCCESS);
        res.setErrorDescription("OK");

        return res;
    }

    /**
     * <b>URL: /admin/org/grabTask</b>
     * <p>抢单</p>
     */
    @RequestMapping("grabTask")
    @RestReturn(value = String.class)
    public RestResponse grabTask(@Valid ProcessOrganizationTaskCommand cmd) {
        organizationService.grabTask(cmd);
        RestResponse res = new RestResponse();
        res.setErrorCode(ErrorCodes.SUCCESS);
        res.setErrorDescription("OK");

        return res;
    }

    /**
     * <b>URL: /admin/org/processingTask</b>
     * <p>处理</p>
     */
    @RequestMapping("processingTask")
    @RestReturn(value = String.class)
    public RestResponse processingTask(@Valid ProcessOrganizationTaskCommand cmd) {
        RestResponse res = new RestResponse();
        organizationService.processingTask(cmd);
        res.setErrorCode(ErrorCodes.SUCCESS);
        res.setErrorDescription("OK");

        return res;
    }

    /**
     * <b>URL: /admin/org/deleteOrganizationOwner</b>
     * <p>删除用户门牌</p>
     */
    @RequestMapping("deleteOrganizationOwner")
    @RestReturn(value = String.class)
    public RestResponse deleteOrganizationOwner(@Valid DeleteOrganizationOwnerCommand cmd) {
        organizationService.deleteOrganizationOwner(cmd);
        RestResponse res = new RestResponse();
        res.setErrorCode(ErrorCodes.SUCCESS);
        res.setErrorDescription("OK");

        return res;
    }

    /**
     * <b>URL: /admin/org/createOrganizationOwner</b>
     * <p>创建用户门牌</p>
     */
    @RequestMapping("createOrganizationOwner")
    @RestReturn(value = String.class)
    public RestResponse createOrganizationOwner(@Valid CreateOrganizationOwnerCommand cmd) {
        organizationService.createOrganizationOwner(cmd);
        RestResponse res = new RestResponse();
        res.setErrorCode(ErrorCodes.SUCCESS);
        res.setErrorDescription("OK");

        return res;
    }

    /**
     * <b>URL: /admin/org/importOwnerData</b>
     * <p>导入业主信息</p>
     */
    @RequestMapping("importOwnerData")
    @RestReturn(value = String.class)
    public RestResponse importOwnerData(@Valid ImportOwnerDataCommand cmd, @RequestParam(value = "attachment") MultipartFile[] files) {
        if (null == files || null == files[0]) {
            LOGGER.error("files is null。");
            throw RuntimeErrorException.errorWith(UserServiceErrorCode.SCOPE, UserServiceErrorCode.ERROR_INVALID_PARAMS,
                    "files is null");
        }
        RestResponse response = new RestResponse(organizationService.importOwnerData(files[0], cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /admin/org/listOrganizationByName</b>
     * <p>导入业主信息</p>
     */
    @RequestMapping("listOrganizationByName")
    @RestReturn(value = ListOrganizationsByNameResponse.class)
    public RestResponse listOrganizationByName(@Valid ListOrganizationsByNameCommand cmd) {
        RestResponse response = new RestResponse(organizationService.listOrganizationByName(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /admin/org/addNewOrganization</b>
     * <p>新增公司</p>
     */
    @RequestMapping("addNewOrganization")
    @RestReturn(value = String.class)
    public RestResponse addNewOrganization(AddNewOrganizationInZuolinCommand cmd) {
        organizationService.addNewOrganizationInZuolin(cmd);
        RestResponse res = new RestResponse();
        res.setErrorCode(ErrorCodes.SUCCESS);
        res.setErrorDescription("OK");

        return res;
    }

    /**
     * <b>URL: /admin/org/exportOrganizationPersonnelXls</b>
     * <p>导出通讯录人员</p>
     */
    @RequestMapping("exportOrganizationPersonnelXls")
    @RestReturn(value = String.class)
    public RestResponse exportRoleAssignmentPersonnelXls(@Valid ExcelOrganizationPersonnelCommand cmd, HttpServletResponse httpResponse) {
        organizationService.exportRoleAssignmentPersonnelXls(cmd, httpResponse);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /admin/org/deleteOrganizationPersonnelByContactToken</b>
     * <p>删除机构成员包括子部门</p>
     */
    @RequestMapping("deleteOrganizationPersonnelByContactToken")
    @RestReturn(value = String.class)
    public RestResponse deleteOrganizationPersonnelByContactToken(@Valid DeleteOrganizationPersonnelByContactTokenCommand cmd) {
        organizationService.deleteOrganizationPersonnelByContactToken(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /admin/org/addOrganizationPersonnel</b>
     * <p>添加成员到多部门多岗位</p>
     */
    @RequestMapping("addOrganizationPersonnel")
    @RestReturn(value = OrganizationDTO.class)
    public RestResponse addOrganizationPersonnel(@Valid AddOrganizationPersonnelCommand cmd) {
        RestResponse response = new RestResponse(organizationService.addOrganizationPersonnel(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /admin/org/getMemberTopDepartment</b>
     * <p>获取人员当前部门的顶级部门</p>
     */
    @RequestMapping("getMemberTopDepartment")
    @RestReturn(value = OrganizationDTO.class)
    public RestResponse getMemberTopDepartment(@Valid GetMemberTopDepartmentCommand cmd) {

//        OrganizationGroupType groupType = OrganizationGroupType.fromCode(cmd.getGroupType());
        List<String> groupTypes = new ArrayList<String>();
		groupTypes.add(cmd.getGroupType()); 
        RestResponse response = new RestResponse(organizationService.getMemberTopDepartment(groupTypes, cmd.getContactToken(), cmd.getOrganizationId()));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /admin/org/listAllChildOrganizationPersonnel</b>
     * <p>查询机构下面全部子机构的所有人员</p>
     */
    @RequestMapping("listAllChildOrganizationPersonnel")
    @RestReturn(value = OrganizationDTO.class)
    public RestResponse listAllChildOrganizationPersonnel(@Valid ListAllChildOrganizationPersonnelCommand cmd) {
        RestResponse response = new RestResponse(organizationService.listAllChildOrganizationPersonnel(cmd.getOrganizationId(), cmd.getGroupTypes(), cmd.getContactName()));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /admin/org/getImportFileResult</b>
     * <p>查询导入的文件结果</p>
     */
    @RequestMapping("getImportFileResult")
    @RestReturn(value = ImportFileResponse.class)
    public RestResponse getImportFileResult(@Valid GetImportFileResultCommand cmd) {
        RestResponse response = new RestResponse(organizationService.getImportFileResult(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /admin/org/exportImportFileFailResultXls</b>
     * <p>导出错误原因</p>
     */
    @RequestMapping("exportImportFileFailResultXls")
    @RestReturn(value = String.class)
    public RestResponse exportImportFileFailResultXls(@Valid GetImportFileResultCommand cmd, HttpServletResponse httpResponse) {
        organizationService.exportImportFileFailResultXls(cmd, httpResponse);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /admin/org/checkIfLastOnNode</b>
     * <p></p>
     */
    @RequestMapping("checkIfLastOnNode")
    @RestReturn(value = String.class)
    public RestResponse checkIfLastOnNode(@Valid DeleteOrganizationPersonnelByContactTokenCommand cmd, HttpServletResponse httpResponse) {
        Map map = new HashMap<>();
        map.put("isLastOne", organizationService.checkIfLastOnNode(cmd));
        RestResponse response = new RestResponse(map);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
}
