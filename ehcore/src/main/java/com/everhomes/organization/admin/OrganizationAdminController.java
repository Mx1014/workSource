package com.everhomes.organization.admin;

import java.util.List;

import javax.validation.Valid;

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
import com.everhomes.organization.AddOrgAddressCommand;
import com.everhomes.organization.CreateOrganizationByAdminCommand;
import com.everhomes.organization.CreateOrganizationCommunityCommand;
import com.everhomes.organization.CreateOrganizationContactCommand;
import com.everhomes.organization.CreateOrganizationMemberCommand;
import com.everhomes.organization.CreatePropertyOrganizationCommand;
import com.everhomes.organization.DeleteOrganizationIdCommand;
import com.everhomes.organization.ListOrganizationMemberCommand;
import com.everhomes.organization.ListOrganizationMemberCommandResponse;
import com.everhomes.organization.ListOrganizationsCommand;
import com.everhomes.organization.ListOrganizationsCommandResponse;
import com.everhomes.organization.OrganizationService;
import com.everhomes.organization.SearchTopicsByTypeCommand;
import com.everhomes.organization.SearchTopicsByTypeResponse;
import com.everhomes.organization.pm.AddPmBuildingCommand;
import com.everhomes.organization.pm.CancelPmBuildingCommand;
import com.everhomes.organization.pm.ListPmBuildingCommand;
import com.everhomes.organization.pm.ListPmManagementsCommand;
import com.everhomes.organization.pm.PmBuildingDTO;
import com.everhomes.organization.pm.PmManagementsResponse;
import com.everhomes.organization.pm.UnassignedBuildingDTO;
import com.everhomes.rest.RestResponse;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserTokenCommand;
import com.everhomes.user.UserTokenCommandResponse;
import com.everhomes.user.admin.SystemUserPrivilegeMgr;

@RestController
@RequestMapping("/admin/org")
public class OrganizationAdminController extends ControllerBase {
	private static final Logger LOGGER = LoggerFactory.getLogger(OrganizationAdminController.class);

	@Autowired
	OrganizationService organizationService;
	
	/**
     * <b>URL: /admin/org/createOrganization</b>
     * <p>创建政府机构</p>
     */
    @RequestMapping("createOrganization")
    @RestReturn(value=String.class)
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
    @RestReturn(value=String.class)
    public RestResponse createOrganizationMember(@Valid CreateOrganizationMemberCommand cmd) {
        SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
        resolver.checkUserPrivilege(UserContext.current().getUser().getId(), 0);
        
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
     * @return 添加的结果
     */
    @RequestMapping("createOrganizationCommunity")
    @RestReturn(value=String.class)
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
     * @return 添加的结果
     */
    @RequestMapping("createPropertyOrganization")
    @RestReturn(value=String.class)
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
    @RestReturn(value=ListOrganizationsCommandResponse.class)
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
    @RestReturn(value=ListOrganizationMemberCommandResponse.class)
    public RestResponse listOrgMembers(@Valid ListOrganizationMemberCommand cmd) {
        SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
        resolver.checkUserPrivilege(UserContext.current().getUser().getId(), 0);
        
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
    @RestReturn(value=String.class)
    public RestResponse deleteOrganizationMember(@Valid DeleteOrganizationIdCommand cmd) {
        SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
        resolver.checkUserPrivilege(UserContext.current().getUser().getId(), 0);
        
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
        SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
        resolver.checkUserPrivilege(UserContext.current().getUser().getId(), 0);
        
    	organizationService.deleteOrganization(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
   
    @RequestMapping("addOrgContact")
    @RestReturn(value=String.class)
    public RestResponse addOrgContact(@Valid CreateOrganizationContactCommand cmd) {
    	organizationService.createOrgContact(cmd);
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
    }
    
    @RequestMapping("addOrgAddress")
    @RestReturn(value=String.class)
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
    @RestReturn(value=String.class)
    public RestResponse importOrganization(@RequestParam(value = "attachment") MultipartFile[] files){
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
    @RestReturn(value=String.class)
    public RestResponse importOrgPost(@RequestParam(value = "attachment") MultipartFile[] files){
        this.organizationService.importOrgPost(files);
        RestResponse response = new RestResponse("服务器正异步处理数据。请耐心等待。不能重复上传。");
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /org/addPmBuilding</b>
     * <p>增加物业楼栋</p>
     */
    @RequestMapping("addPmBuilding")
    @RestReturn(value=String.class)
    public RestResponse addPmBuilding(AddPmBuildingCommand cmd) {
    	
    	SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
        resolver.checkUserPrivilege(UserContext.current().getUser().getId(), 0);
        
    	this.organizationService.addPmBuilding(cmd);
    	RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /org/cancelPmBuilding</b>
     * <p>删除物业楼栋</p>
     */
    @RequestMapping("cancelPmBuilding")
    @RestReturn(value=String.class)
    public RestResponse cancelPmBuilding(CancelPmBuildingCommand cmd) {
    	
    	SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
        resolver.checkUserPrivilege(UserContext.current().getUser().getId(), 0);
        
    	this.organizationService.cancelPmBuilding(cmd);
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
        resolver.checkUserPrivilege(UserContext.current().getUser().getId(), 0);
        
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
        resolver.checkUserPrivilege(UserContext.current().getUser().getId(), 0);
        
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
    @RestReturn(value=PmManagementsResponse.class)
    public RestResponse listPmManagements(ListPmManagementsCommand cmd) {
    	
    	SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
        resolver.checkUserPrivilege(UserContext.current().getUser().getId(), 0);
        
        PmManagementsResponse list = this.organizationService.listPmManagements(cmd);
    	RestResponse response = new RestResponse(list);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
}
