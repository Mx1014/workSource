package com.everhomes.organization.admin;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestReturn;
import com.everhomes.organization.CreateOrganizationCommand;
import com.everhomes.organization.CreateOrganizationCommunityCommand;
import com.everhomes.organization.CreateOrganizationMemberCommand;
import com.everhomes.organization.CreatePropertyOrganizationCommand;
import com.everhomes.organization.DeleteOrganizationIdCommand;
import com.everhomes.organization.ListOrganizationMemberCommand;
import com.everhomes.organization.ListOrganizationMemberCommandResponse;
import com.everhomes.organization.ListOrganizationsCommand;
import com.everhomes.organization.ListOrganizationsCommandResponse;
import com.everhomes.organization.OrganizationService;
import com.everhomes.rest.RestResponse;
import com.everhomes.user.UserTokenCommand;
import com.everhomes.user.UserTokenCommandResponse;

@RestController
@RequestMapping("/admin/org")
public class OrganizationAdminController extends ControllerBase {
	private static final Logger LOGGER = LoggerFactory.getLogger(OrganizationAdminController.class);

	@Autowired
	OrganizationService organizationService;
	
	/**
     * <b>URL: /admin/org/createOrganization</b>
     * <p>创建政府机构</p>
     * @return 添加的结果
     */
    @RequestMapping("createOrganization")
    @RestReturn(value=String.class)
    public RestResponse createOrganization(@Valid CreateOrganizationCommand cmd) {
    	organizationService.createOrganization(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /admin/org/createOrganizationMember</b>
     * <p>创建政府机构成员</p>
     * @return 添加的结果
     */
    @RequestMapping("createOrganizationMember")
    @RestReturn(value=String.class)
    public RestResponse createOrganizationMember(@Valid CreateOrganizationMemberCommand cmd) {
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
    @RestReturn(value=UserTokenCommandResponse.class)
    public RestResponse findUserByIndentifier(@Valid UserTokenCommand cmd) {
        UserTokenCommandResponse commandResponse = organizationService.findUserByIndentifier(cmd);
        RestResponse response = new RestResponse(commandResponse);
        
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /admin/org/createOrganizationCommunity</b>
     * <p>创建政府机构对应的小区列表</p>
     * @return 添加的结果
     */
    @RequestMapping("createOrganizationCommunity")
    @RestReturn(value=String.class)
    public RestResponse createOrganizationCommunity(@Valid CreateOrganizationCommunityCommand cmd) {
    	organizationService.createOrganizationCommunity(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /admin/org/createPropertyOrganization</b>
     * <p>创建小区的物业机构</p>
     * @return 添加的结果
     */
    @RequestMapping("createPropertyOrganization")
    @RestReturn(value=String.class)
    public RestResponse createPropertyOrganization(@Valid CreatePropertyOrganizationCommand cmd) {
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
    @RestReturn(value=ListOrganizationsCommandResponse.class)
    public RestResponse listOrganizations(@Valid ListOrganizationsCommand cmd) {
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
    @RestReturn(value=ListOrganizationMemberCommandResponse.class)
    public RestResponse listOrgMembers(@Valid ListOrganizationMemberCommand cmd) {
    	ListOrganizationMemberCommandResponse commandResponse = organizationService.listOrgMembers(cmd);
        RestResponse response = new RestResponse(commandResponse);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /admin/org/deleteOrganizationMember</b>
     * <p>删除政府机构成员</p>
     * @return 删除的结果
     */
    @RequestMapping("deleteOrganizationMember")
    @RestReturn(value=String.class)
    public RestResponse deleteOrganizationMember(@Valid DeleteOrganizationIdCommand cmd) {
    	organizationService.deleteOrganizationMember(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /admin/org/deleteOrganization</b>
     * <p>删除政府机构</p>
     * @return 删除的结果
     */
    @RequestMapping("deleteOrganization")
    @RestReturn(value=String.class)
    public RestResponse deleteOrganization(@Valid DeleteOrganizationIdCommand cmd) {
    	organizationService.deleteOrganization(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
   
   
    
    
}
