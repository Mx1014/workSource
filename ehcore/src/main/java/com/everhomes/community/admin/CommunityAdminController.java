package com.everhomes.community.admin;


import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.community.CommunityService;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.address.CommunityDTO;
import com.everhomes.rest.community.*;
import com.everhomes.rest.community.admin.*;
import com.everhomes.rest.organization.ImportFileTaskDTO;
import com.everhomes.rest.user.UserServiceErrorCode;
import com.everhomes.search.SearchSyncManager;
import com.everhomes.search.SearchSyncType;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.user.admin.SystemUserPrivilegeMgr;
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
import java.util.List;

@RestDoc(value="Community admin controller", site="core")
@RestController
@RequestMapping("/admin/community")
public class CommunityAdminController extends ControllerBase {
    private static final Logger LOGGER = LoggerFactory.getLogger(CommunityAdminController.class);
    
    @Autowired
    private CommunityService communityService;

    @Autowired
    private SearchSyncManager searchSyncManager;

    /**
     * <b>URL: /admin/community/listCommunitiesByStatus</b>
     * <p>查询待审核小区列表</p>
     */
    @RequestMapping("listCommunitiesByStatus")
    @RestReturn(value=ListCommunitesByStatusCommandResponse.class)
    public RestResponse listCommunitiesByStatus(@Valid ListCommunitesByStatusCommand cmd) {
        
        SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
        //resolver.checkUserPrivilege(UserContext.current().getUser().getId(), 0);
        
        ListCommunitesByStatusCommandResponse cmdResponse = this.communityService.listCommunitiesByStatus(cmd);
        
        RestResponse response =  new RestResponse(cmdResponse);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /admin/community/approveCommunity</b>
     * <p>审核小区</p>
     */
    @RequestMapping("approveCommunity")
    @RestReturn(value=String.class)
    public RestResponse approveCommunity(@Valid ApproveCommunityAdminCommand cmd) {
        
        SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
        //resolver.checkUserPrivilege(UserContext.current().getUser().getId(), 0);
        
        this.communityService.approveCommuniy(cmd);
        
        RestResponse response =  new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /admin/community/rejectCommunity</b>
     * <p>拒绝用户添加的小区</p>
     */
    @RequestMapping("rejectCommunity")
    @RestReturn(value=String.class)
    public RestResponse rejectCommunity(@Valid RejectCommunityAdminCommand cmd) {
        
        SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
        //resolver.checkUserPrivilege(UserContext.current().getUser().getId(), 0);
        
        this.communityService.rejectCommunity(cmd);
        
        RestResponse response =  new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /admin/community/updateCommunity</b>
     * <p>更新小区信息</p>
     */
    @RequestMapping("updateCommunity")
    @RestReturn(value=String.class)
    public RestResponse updateCommunity(@Valid UpdateCommunityAdminCommand cmd) {
        
        SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
        ////resolver.checkUserPrivilege(UserContext.current().getUser().getId(), 0);
        
        this.communityService.updateCommunity(cmd);
        
        RestResponse response =  new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /admin/community/updateCommunityPartial</b>
     * <p>更新小区部分信息，传了那个字段更新那个字段</p>
     */
    @RequestMapping("updateCommunityPartial")
    @RestReturn(value=String.class)
    public RestResponse updateCommunityPartial(@Valid UpdateCommunityPartialAdminCommand cmd) {

        SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
        ////resolver.checkUserPrivilege(UserContext.current().getUser().getId(), 0);

        this.communityService.updateCommunityPartial(cmd);

        RestResponse response =  new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /admin/community/getCommunityById</b>
     * <p>根据小区id获取小区信息</p>
     */
    @RequestMapping("getCommunityById")
    @RestReturn(value=CommunityDTO.class)
    public RestResponse getCommunityById(@Valid GetCommunityByIdCommand cmd) {
        
        SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
        //resolver.checkUserPrivilege(UserContext.current().getUser().getId(), 0);
        
        CommunityDTO community = this.communityService.getCommunityById(cmd);
        RestResponse response =  new RestResponse(community);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /admin/community/getCommunityByUuid</b>
     * <p>根据小区uuId获取小区信息</p>
     */
    @RequestMapping("getCommunityByUuid")
    @RestReturn(value=CommunityDTO.class)
    public RestResponse getCommunityByUuid(@Valid GetCommunityByUuidCommand cmd) {
        
        SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
        //resolver.checkUserPrivilege(UserContext.current().getUser().getId(), 0);
        
        CommunityDTO community = this.communityService.getCommunityByUuid(cmd);
        RestResponse response =  new RestResponse(community);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
   
    /**
     * <b>URL: /admin/community/syncOwnerIndex</b>
     * <p>同步小区索引</p>
     */
    @RequestMapping("syncIndex")
    @RestReturn(value=String.class)
    public RestResponse syncIndex() {

        SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
        //resolver.checkUserPrivilege(UserContext.current().getUser().getId(), 0);
        
        this.searchSyncManager.SyncDb(SearchSyncType.COMMUNITY);
        RestResponse response =  new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }    
    
    
    /**
     * <b>URL: /community/getNearbyCommunitiesById</b>
     * <p>根据小区id查询该小区的周边小区列表</p>
     */
    @RequestMapping("getNearbyCommunitiesById")
    @RestReturn(value=CommunityDTO.class, collection=true)
    public RestResponse getNearbyCommunitiesById(@Valid GetNearbyCommunitiesByIdCommand cmd) {
        
        SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
        //resolver.checkUserPrivilege(UserContext.current().getUser().getId(), 0);
        
        List<CommunityDTO> results = this.communityService.getNearbyCommunityById(cmd);
        RestResponse response =  new RestResponse(results);

        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /admin/community/listCommunitiesByKeyword</b>
     * <p>根据关键字查询待审核小区列表</p>
     */
    @RequestMapping("listCommunitiesByKeyword")
    @RestReturn(value=ListCommunitiesByKeywordResponse.class)
    public RestResponse listCommunitiesByKeyword(@Valid ListComunitiesByKeywordAdminCommand cmd) {
    	
        SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
        //resolver.checkUserPrivilege(UserContext.current().getUser().getId(), 0);
        if(cmd.getKeyword() == null || cmd.getKeyword().equals("")){
			throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, 
					"Invalid keyword parameter");
		}
    	ListCommunitiesByKeywordResponse cmdResponse = this.communityService.listCommunitiesByKeyword(cmd);
    	RestResponse response =  new RestResponse(cmdResponse);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    	
    }
    
    /**
     * <b>URL: /admin/community/deleteBuilding</b>
     */
    @RequestMapping("deleteBuilding")
    @RestReturn(value = String.class)
	public RestResponse deleteBuilding(DeleteBuildingAdminCommand cmd) {
    	
    	SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
        ////resolver.checkUserPrivilege(UserContext.current().getUser().getId(), 0);

        this.communityService.deleteBuilding(cmd);

        RestResponse response =  new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
	}
	
    /**
     * <b>URL: /admin/community/updateBuilding</b>
     */
    
	@RequestMapping("updateBuilding")
	@RestReturn(value = BuildingDTO.class)
	public RestResponse updateBuilding(UpdateBuildingAdminCommand cmd) {
		
		SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
        ////resolver.checkUserPrivilege(UserContext.current().getUser().getId(), 0);

        BuildingDTO dto = this.communityService.updateBuilding(cmd);

        RestResponse response =  new RestResponse(dto);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
	}

	 /**
     * <b>URL: /admin/community/verifyBuildingName</b>
     */
	@RequestMapping("verifyBuildingName")
	@RestReturn(value = String.class)
	public RestResponse verifyBuildingName(VerifyBuildingNameAdminCommand cmd) {
		
		SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
        ////resolver.checkUserPrivilege(UserContext.current().getUser().getId(), 0);

        Boolean verify = this.communityService.verifyBuildingName(cmd);

        RestResponse response =  new RestResponse(verify);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
	}
	
	/**
     * <b>URL: /admin/community/getCommunityManagers</b>
     */
	@RequestMapping("getCommunityManagers")
	@RestReturn(value = CommunityManagerDTO.class, collection = true)
	public RestResponse getCommunityManagers(ListCommunityManagersAdminCommand cmd) {
		
		SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
        ////resolver.checkUserPrivilege(UserContext.current().getUser().getId(), 0);
        
        List<CommunityManagerDTO> manager = this.communityService.getCommunityManagers(cmd);
		RestResponse response =  new RestResponse(manager);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
	}
	
	/**
     * <b>URL: /admin/community/getUserCommunities</b>
     */
	@RequestMapping("getUserCommunities")
	@RestReturn(value = UserCommunityDTO.class, collection = true)
	public RestResponse getUserCommunities(ListUserCommunitiesCommand cmd) {
		
		SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
        //resolver.checkUserPrivilege(UserContext.current().getUser().getId(), 0);
        
        List<UserCommunityDTO> communities = this.communityService.getUserCommunities(cmd);
		RestResponse response =  new RestResponse(communities);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
	}

    /**
     * <b>URL: /admin/community/listBuildingsByStatus</b>
     * <p>查询待审核楼栋列表</p>
     */
    @RequestMapping("listBuildingsByStatus")
    @RestReturn(value=ListBuildingsByStatusCommandResponse.class)
    public RestResponse listBuildingsByStatus(@Valid listBuildingsByStatusCommand cmd) {
        
        SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
        //resolver.checkUserPrivilege(UserContext.current().getUser().getId(), 0);
        
        ListBuildingsByStatusCommandResponse cmdResponse = this.communityService.listBuildingsByStatus(cmd);
        
        RestResponse response =  new RestResponse(cmdResponse);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /admin/community/approveBuilding</b>
     * <p>审核楼栋</p>
     */
    @RequestMapping("approveBuilding")
    @RestReturn(value=String.class)
    public RestResponse approveBuilding(@Valid VerifyBuildingAdminCommand cmd) {
        
        SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
        //resolver.checkUserPrivilege(UserContext.current().getUser().getId(), 0);
        
        this.communityService.approveBuilding(cmd);
        
        RestResponse response =  new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /admin/community/rejectBuilding</b>
     * <p>拒绝用户添加的楼栋</p>
     */
    @RequestMapping("rejectBuilding")
    @RestReturn(value=String.class)
    public RestResponse rejectBuilding(@Valid VerifyBuildingAdminCommand cmd) {
        
        SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
        //resolver.checkUserPrivilege(UserContext.current().getUser().getId(), 0);
        
        this.communityService.rejectBuilding(cmd);
        
        RestResponse response =  new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /admin/community/exportBuildingByCommunityId</b>
     * <p>后台管理 楼栋列表</p>
     */
    @RequestMapping("exportBuildingByCommunityId")
    @RestReturn(value = String.class)
    public RestResponse exportBuildingByCommunityId(@Valid ListBuildingCommand cmd, HttpServletResponse httpServletResponse) {
        communityService.exportBuildingByCommunityId(cmd, httpServletResponse);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
//    
//    /**
//     * <b>URL: /admin/community/importBuildingData</b>
//     * <p>导入楼栋信息excel</p>
//     */
//    @RequestMapping("importBuildingData")
//    @RestReturn(value=ImportDataResponse.class)
//    public RestResponse importBuildingData(@RequestParam(value = "attachment") MultipartFile[] files){
//    	User manaUser = UserContext.current().getUser();
//    	Long userId = manaUser.getId();
//    	if(null == files || null == files[0]){
//    		LOGGER.error("files is null。userId="+userId);
//    		throw RuntimeErrorException.errorWith(UserServiceErrorCode.SCOPE, UserServiceErrorCode.ERROR_INVALID_PARAMS,
//    				"files is null");
//    	}
//    	ImportDataResponse importDataResponse = this.communityService.importBuildingData(files[0], userId);
//    	RestResponse response = new RestResponse(importDataResponse);
//    	response.setErrorCode(ErrorCodes.SUCCESS);
//    	response.setErrorDescription("OK");
//    	return response;
//    }
    
    /**
     * <b>URL: /admin/community/qryCommunityUserEnterpriseByUserId</b>
     * <p>查询用户所在的企业</p>
     */
    @RequestMapping("qryCommunityUserEnterpriseByUserId")
    @RestReturn(value=CommunityUserAddressDTO.class)
    public RestResponse qryCommunityUserEnterpriseByUserId(@Valid QryCommunityUserAddressByUserIdCommand cmd) {

        SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
        //resolver.checkUserPrivilege(UserContext.current().getUser().getId(), 0);
        RestResponse response =  new RestResponse(communityService.qryCommunityUserEnterpriseByUserId(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /admin/community/updateCommunityUser</b>
     * <p>园区管理员更新用户信息</p>
     */
    @RequestMapping("updateCommunityUser")
    @RestReturn(value=String.class)
    public RestResponse updateCommunityUser(@Valid UpdateCommunityUserCommand cmd) {
    	communityService.updateCommunityUser(cmd);
        RestResponse response =  new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /admin/community/qryCommunityUserAddressByUserId</b>
     * <p>查询用户所在的地址</p>
     */
    @RequestMapping("qryCommunityUserAddressByUserId")
    @RestReturn(value=CommunityUserAddressDTO.class)
    public RestResponse qryCommunityUserAddressByUserId(@Valid QryCommunityUserAddressByUserIdCommand cmd) {
        
        RestResponse response =  new RestResponse(communityService.qryCommunityUserAddressByUserId(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /admin/community/qryCommunityUserAllByUserId</b>
     * <p>查询用户所在的地址</p>
     */
    @RequestMapping("qryCommunityUserAllByUserId")
    @RestReturn(value=CommunityUserAddressDTO.class)
    public RestResponse qryCommunityUserAllByUserId(@Valid QryCommunityUserAllByUserIdCommand cmd) {

        RestResponse response =  new RestResponse(communityService.qryCommunityUserAllByUserId(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    /**
     * <b>URL: /admin/community/listOwnerBycommunityId</b>
     * <p>查询未注册的用户</p>
     */
    @RequestMapping("listOwnerBycommunityId")
    @RestReturn(value=CommunityUserAddressResponse.class)
    public RestResponse listOwnerBycommunityId(@Valid ListCommunityUsersCommand cmd) {
        
        RestResponse response =  new RestResponse(communityService.listOwnerBycommunityId(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /admin/community/listUserBycommunityId</b>
     * <p>查询用户</p>
     */
    @RequestMapping("listUserBycommunityId")
    @RestReturn(value=CommunityUserAddressResponse.class)
    public RestResponse listUserBycommunityId(@Valid ListCommunityUsersCommand cmd) {
        
        RestResponse response =  new RestResponse(communityService.listUserBycommunityIdV2(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /admin/community/listCommunityAuthUserAddress</b>
     * <p>用户认证列表</p>
     */
    @RequestMapping("listCommunityAuthUserAddress")
    @RestReturn(value=CommunityAuthUserAddressResponse.class)
    public RestResponse listCommunityAuthUserAddress(@Valid CommunityAuthUserAddressCommand cmd) {
        RestResponse response =  new RestResponse(communityService.listCommunityAuthUserAddress(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /admin/community/listCommunityAuthPersonnels</b>
     * <p>园区用户认证列表</p>
     */
    @RequestMapping("listCommunityAuthPersonnels")
    @RestReturn(value=ListCommunityAuthPersonnelsResponse.class)
    public RestResponse listCommunityAuthPersonnels(@Valid ListCommunityAuthPersonnelsCommand cmd) {
        RestResponse response =  new RestResponse(communityService.listCommunityAuthPersonnels(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /admin/community/listUserByNotJoinedCommunity</b>
     * <p>未加入小区的用户</p>
     */
    @RequestMapping("listUserByNotJoinedCommunity")
    @RestReturn(value=CommunityUserAddressResponse.class)
    public RestResponse listUserByNotJoinedCommunity(@Valid ListCommunityUsersCommand cmd) {
        RestResponse response =  new RestResponse(communityService.listUserByNotJoinedCommunity(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /admin/community/listUnassignedCommunitiesByNamespaceId</b>
     * <p>域下面未被管理的小区</p>
     */
    @RequestMapping("listUnassignedCommunitiesByNamespaceId")
    @RestReturn(value=CommunityDTO.class)
    public RestResponse listUnassignedCommunitiesByNamespaceId() {
        RestResponse response =  new RestResponse(communityService.listUnassignedCommunitiesByNamespaceId());
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    

	/**
	 * <b>URL: /admin/community/createCommunity</b>
	 * <p>
	 * 创建小区
	 * </p>
	 */
	@RequestMapping("createCommunity")
	@RestReturn(value = CreateCommunityResponse.class)
	public RestResponse createCommunity(CreateCommunityCommand cmd) {
		CreateCommunityResponse result = communityService.createCommunity(cmd);
		RestResponse response = new RestResponse(result);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

    /**
     * <b>URL: /admin/community/createCommunities</b>
     * <p>
     * 创建小区
     * </p>
     */
    @RequestMapping("createCommunities")
    @RestReturn(value = CreateCommunitiesResponse.class)
    public RestResponse createCommunities(CreateCommunitiesCommand cmd) {
        CreateCommunitiesResponse result = communityService.createCommunities(cmd);
        RestResponse response = new RestResponse(result);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

	/**
	 * <b>URL: /admin/community/importCommunity</b>
	 * <p>
	 * 导入小区
	 * </p>
	 */
	@RequestMapping("importCommunity")
	@RestReturn(value = String.class)
	public RestResponse importCommunity(ImportCommunityCommand cmd, @RequestParam("attachment") MultipartFile[] files) {
		communityService.importCommunity(cmd, files);
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /admin/community/listCommunityByNamespaceId</b>
	 * <p>
	 * 按域空间查询社区
	 * </p>
	 */
	@RequestMapping("listCommunityByNamespaceId")
	@RestReturn(value = ListCommunityByNamespaceIdResponse.class)
	public RestResponse listCommunityByNamespaceId(ListCommunityByNamespaceIdCommand cmd) {
		ListCommunityByNamespaceIdResponse result = communityService.listCommunityByNamespaceId(cmd);
		RestResponse response = new RestResponse(result);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	
	/**
	 * 
	 * <b>URL: /admin/community/communityImportBaseConfig</b>
	 * <p>小区导入第一步：配置域空间相关信息</p>
	 */
	@RequestMapping("communityImportBaseConfig")
	@RestReturn(String.class)
	public RestResponse communityImportBaseConfig(CommunityImportBaseConfigCommand cmd){
		communityService.communityImportBaseConfig(cmd);
		return new RestResponse();
	}
	
	/**
	 * 
	 * <b>URL: /admin/community/communityImportOrganizationConfig</b>
	 * <p>小区导入第三步：配置管理公司相关信息</p>
	 */
	@RequestMapping("communityImportOrganizationConfig")
	@RestReturn(String.class)
	public RestResponse communityImportOrganizationConfig(CommunityImportOrganizationConfigCommand cmd){
		communityService.communityImportOrganizationConfig(cmd);
		return new RestResponse();
	}

    /**
     *
     * <b>URL: /admin/community/updateBuildingOrder</b>
     * <p>
     * 更新楼栋顺序
     * </p>
     */
    @RequestMapping("updateBuildingOrder")
    @RestReturn(String.class)
    public RestResponse updateBuildingOrder(@Valid UpdateBuildingOrderCommand cmd){
        communityService.updateBuildingOrder(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
	
    /**
     * <b>URL: /admin/community/getCommunityAuthPopupConfig</b>
     * <p>获取用户认证弹窗设置</p>
     */
    @RequestMapping("getCommunityAuthPopupConfig")
    @RestReturn(CommunityAuthPopupConfigDTO.class)
    public RestResponse getCommunityAuthPopupConfig(@Valid GetCommunityAuthPopupConfigCommand cmd){
        CommunityAuthPopupConfigDTO dto = communityService.getCommunityAuthPopupConfig(cmd);
        RestResponse response = new RestResponse(dto);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /admin/community/updateCommunityAuthPopupConfig</b>
     * <p>修改用户认证弹窗设置</p>
     */
    @RequestMapping("updateCommunityAuthPopupConfig")
    @RestReturn(CommunityAuthPopupConfigDTO.class)
    public RestResponse updateCommunityAuthPopupConfig(@Valid UpdateCommunityAuthPopupConfigCommand cmd){
        CommunityAuthPopupConfigDTO dto = communityService.updateCommunityAuthPopupConfig(cmd);
        RestResponse response = new RestResponse(dto);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /admin/community/importCommunityDataAdmin</b>
     * <p>导入项目信息excel</p>
     */
    @RequestMapping("importCommunityDataAdmin")
    @RestReturn(value=ImportFileTaskDTO.class)
    public RestResponse importCommunityDataAdmin(ImportCommunityCommand cmd, @RequestParam(value = "attachment") MultipartFile[] files){
    	User manaUser = UserContext.current().getUser();
		Long userId = manaUser.getId();
		if(null == files || null == files[0]){
			LOGGER.error("files is null。userId={}",userId);
			throw RuntimeErrorException.errorWith(UserServiceErrorCode.SCOPE, UserServiceErrorCode.ERROR_INVALID_PARAMS,
					"files is null");
		}
        RestResponse response = new RestResponse(communityService.importCommunityDataAdmin(cmd, files[0]));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }


    /**
     * <b>URL: /admin/community/changeOrganizationCommunities</b>
     * <p>更改园区管理公司</p>
     */
    @RequestMapping("changeOrganizationCommunities")
    @RestReturn(String.class)
    public RestResponse changeOrganizationCommunities(@Valid ChangeOrganizationCommunitiesCommand cmd){
        communityService.changeOrganizationCommunities(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }




    /**
     * <b>URL: /admin/community/checkUserAuditing</b>
     * <p>判断当前用户是否有审核的权限</p>
     */
    @RequestMapping("checkUserAuditing")
    @RestReturn(CheckUserAuditingAdminResponse.class)
    public RestResponse checkUserAuditing(@Valid CheckUserAuditingAdminCommand cmd){
        CheckUserAuditingAdminResponse checkUserAuditingAdminResponse = communityService.checkUserAuditing(cmd);
        RestResponse response = new RestResponse(checkUserAuditingAdminResponse);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /admin/community/importBuildingData</b>
     * <p>导入楼栋信息excel</p>
     */
    @RequestMapping("importBuildingData")
    @RestReturn(value=ImportFileTaskDTO.class)
    public RestResponse importBuildingData(@RequestParam("communityId") Long communityId, @RequestParam(value = "attachment") MultipartFile[] files){
    	User manaUser = UserContext.current().getUser();
		Long userId = manaUser.getId();
		if(null == files || null == files[0]){
			LOGGER.error("files is null.userId="+userId);
			throw RuntimeErrorException.errorWith(UserServiceErrorCode.SCOPE, UserServiceErrorCode.ERROR_INVALID_PARAMS,
					"files is null");
		}
//		ImportDataResponse importDataResponse = this.communityService.importBuildingData(files[0], userId);
        RestResponse response = new RestResponse(communityService.importBuildingData(communityId, files[0]));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }


    /**
     * <b>URL: /admin/community/exportBuildingByKeywords</b>
     * <p>后台管理 楼栋列表</p>
     */
    @RequestMapping("exportBuildingByKeywords")
    @RestReturn(value = String.class)
    public RestResponse exportBuildingByKeywords(ListBuildingsByKeywordsCommand cmd, HttpServletResponse httpServletResponse) {
    	communityService.exportBuildingByKeywords(cmd, httpServletResponse);
    	RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /admin/community/exportApartmentsInCommunity</b>
     * <p>导出项目下的房源信息</p>
     */
    @RequestMapping("exportApartmentsInCommunity")
    @RestReturn(value = String.class)
    public RestResponse exportApartmentsInCommunity(ListApartmentsInCommunityCommand cmd, HttpServletResponse httpServletResponse) {
    	communityService.exportApartmentsInCommunity(cmd, httpServletResponse);
    	RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
}
