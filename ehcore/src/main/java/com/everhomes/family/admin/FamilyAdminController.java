package com.everhomes.family.admin;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.everhomes.acl.Role;
import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.family.FamilyService;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.family.ApproveMemberCommand;
import com.everhomes.rest.family.BatchApproveMemberCommand;
import com.everhomes.rest.family.BatchRejectMemberCommand;
import com.everhomes.rest.family.ListAllFamilyMembersCommandResponse;
import com.everhomes.rest.family.ListWaitApproveFamilyCommandResponse;
import com.everhomes.rest.family.RejectMemberCommand;
import com.everhomes.rest.family.admin.ListAllFamilyMembersAdminCommand;
import com.everhomes.rest.family.admin.ListWaitApproveFamilyAdminCommand;
import com.everhomes.user.UserContext;
import com.everhomes.user.admin.SystemUserPrivilegeMgr;

@RestDoc(value="Family admin controller", site="core")
@RestController
@RequestMapping("/admin/family")
public class FamilyAdminController extends ControllerBase {
    
	private static final Logger LOGGER = LoggerFactory.getLogger(FamilyAdminController.class);

	@Autowired
    private FamilyService familyService;
	
    /**
     * <b>URL: /admin/family/listWaitApproveAddress</b>
     * <p>查询等待审核的地址列表</p>
     */
    @RequestMapping("listWaitApproveFamily")
    @RestReturn(value=ListWaitApproveFamilyCommandResponse.class, collection=true)
    public RestResponse listWaitApproveFamily(@Valid ListWaitApproveFamilyAdminCommand cmd) {
        SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
        resolver.checkUserPrivilege(UserContext.current().getUser().getId(), 0);
        
        ListWaitApproveFamilyCommandResponse cmdResponse = this.familyService.listWaitApproveFamily(cmd);
        
        RestResponse response = new RestResponse(cmdResponse);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /admin/family/adminApproveMember</b>
     * <p>管理员批准用户通过</p>
     */
    @RequestMapping("adminApproveMember")
    @RestReturn(value=String.class)
    public RestResponse adminApproveMember(@Valid ApproveMemberCommand cmd) {
        
//        SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
//        resolver.checkUserPrivilege(UserContext.current().getUser().getId(), 0);
        
        this.familyService.adminApproveMember(cmd);
        
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /admin/family/adminBatchApproveMember</b>
     * <p>管理员批量 批准用户通过</p>
     */
    @RequestMapping("adminBatchApproveMember")
    @RestReturn(value=String.class)
    public RestResponse adminBatchApproveMember(@Valid BatchApproveMemberCommand cmd) {
        
//        SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
//        resolver.checkUserPrivilege(UserContext.current().getUser().getId(), 0);
        
        this.familyService.adminBatchApproveMember(cmd);
        
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /admin/family/adminRejectMember</b>
     * <p>管理员拒绝用户</p>
     */
    @RequestMapping("adminRejectMember")
    @RestReturn(value=String.class)
    public RestResponse adminRejectMember(@Valid RejectMemberCommand cmd) {
        
//        SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
//        resolver.checkUserPrivilege(UserContext.current().getUser().getId(), 0);
        
        cmd.setOperatorRole(Role.SystemAdmin);
        this.familyService.rejectMember(cmd);
        
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /admin/family/adminBatchRejectMember</b>
     * <p>管理员批量  拒绝用户</p>
     */
    @RequestMapping("adminBatchRejectMember")
    @RestReturn(value=String.class)
    public RestResponse adminBatchRejectMember(@Valid BatchRejectMemberCommand cmd) {
        
//        SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
//        resolver.checkUserPrivilege(UserContext.current().getUser().getId(), 0);
        
        this.familyService.batchRejectMember(cmd);
        
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    /**
     * <b>URL: /admin/family/listAllFamilyMembers</b>
     * <p>查询系统中存在家庭且状态正常的用户</p>
     */
    @RequestMapping("listAllFamilyMembers")
    @RestReturn(value=ListAllFamilyMembersCommandResponse.class)
    public RestResponse listAllFamilyMembers(ListAllFamilyMembersAdminCommand cmd) {
        
        SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
        resolver.checkUserPrivilege(UserContext.current().getUser().getId(), 0);
        
        ListAllFamilyMembersCommandResponse cmdResponse = this.familyService.listAllFamilyMembers(cmd);
        
        RestResponse response = new RestResponse(cmdResponse);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
}
