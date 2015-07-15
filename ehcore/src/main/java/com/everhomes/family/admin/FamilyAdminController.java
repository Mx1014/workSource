package com.everhomes.family.admin;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.everhomes.acl.Role;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.family.ApproveMemberCommand;
import com.everhomes.family.FamilyService;
import com.everhomes.family.ListAllFamilyMembersCommand;
import com.everhomes.family.ListAllFamilyMembersCommandResponse;
import com.everhomes.family.ListWaitApproveFamilyCommand;
import com.everhomes.family.ListWaitApproveFamilyCommandResponse;
import com.everhomes.family.RejectMemberCommand;
import com.everhomes.rest.RestResponse;

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
        cmd.setOperatorRole(Role.ResourceAdmin);
        this.familyService.approveMember(cmd);
        
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
        cmd.setOperatorRole(Role.ResourceAdmin);
        this.familyService.rejectMember(cmd);
        
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
        ListAllFamilyMembersCommandResponse cmdResponse = this.familyService.listAllFamilyMembers(cmd);
        
        RestResponse response = new RestResponse(cmdResponse);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
}
