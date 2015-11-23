package com.everhomes.community.admin;


import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.everhomes.address.CommunityDTO;
import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.community.ApproveCommunityCommand;
import com.everhomes.community.BuildingDTO;
import com.everhomes.community.CommunityService;
import com.everhomes.community.GetCommunityByIdCommand;
import com.everhomes.community.GetCommunityByUuidCommand;
import com.everhomes.community.GetNearbyCommunitiesByIdCommand;
import com.everhomes.community.ListCommunitesByStatusCommand;
import com.everhomes.community.ListCommunitesByStatusCommandResponse;
import com.everhomes.community.ListCommunitiesByKeywordCommandResponse;
import com.everhomes.community.ListComunitiesByKeywordCommand;
import com.everhomes.community.RejectCommunityCommand;
import com.everhomes.community.UpdateCommunityCommand;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.search.SearchSyncManager;
import com.everhomes.search.SearchSyncType;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserServiceErrorCode;
import com.everhomes.user.admin.ImportDataResponse;
import com.everhomes.user.admin.SystemUserPrivilegeMgr;
import com.everhomes.util.RuntimeErrorException;

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
        resolver.checkUserPrivilege(UserContext.current().getUser().getId(), 0);
        
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
        resolver.checkUserPrivilege(UserContext.current().getUser().getId(), 0);
        
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
        resolver.checkUserPrivilege(UserContext.current().getUser().getId(), 0);
        
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
        resolver.checkUserPrivilege(UserContext.current().getUser().getId(), 0);
        
        this.communityService.updateCommunity(cmd);
        
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
        resolver.checkUserPrivilege(UserContext.current().getUser().getId(), 0);
        
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
        resolver.checkUserPrivilege(UserContext.current().getUser().getId(), 0);
        
        CommunityDTO community = this.communityService.getCommunityByUuid(cmd);
        RestResponse response =  new RestResponse(community);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
   
    /**
     * <b>URL: /admin/community/syncIndex</b>
     * <p>同步小区索引</p>
     */
    @RequestMapping("syncIndex")
    @RestReturn(value=String.class)
    public RestResponse syncIndex() {

        SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
        resolver.checkUserPrivilege(UserContext.current().getUser().getId(), 0);
        
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
        resolver.checkUserPrivilege(UserContext.current().getUser().getId(), 0);
        
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
    @RestReturn(value=ListCommunitiesByKeywordCommandResponse.class)
    public RestResponse listCommunitiesByKeyword(@Valid ListComunitiesByKeywordAdminCommand cmd) {
    	
        SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
        resolver.checkUserPrivilege(UserContext.current().getUser().getId(), 0);
        
    	ListCommunitiesByKeywordCommandResponse cmdResponse = this.communityService.listCommunitiesByKeyword(cmd);
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
        //resolver.checkUserPrivilege(UserContext.current().getUser().getId(), 0);

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
        //resolver.checkUserPrivilege(UserContext.current().getUser().getId(), 0);

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
        //resolver.checkUserPrivilege(UserContext.current().getUser().getId(), 0);

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
        //resolver.checkUserPrivilege(UserContext.current().getUser().getId(), 0);
        
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
        resolver.checkUserPrivilege(UserContext.current().getUser().getId(), 0);
        
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
        resolver.checkUserPrivilege(UserContext.current().getUser().getId(), 0);
        
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
        resolver.checkUserPrivilege(UserContext.current().getUser().getId(), 0);
        
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
        resolver.checkUserPrivilege(UserContext.current().getUser().getId(), 0);
        
        this.communityService.rejectBuilding(cmd);
        
        RestResponse response =  new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /admin/community/importBuildingData</b>
     * <p>导入楼栋信息excel</p>
     */
    @RequestMapping("importBuildingData")
    @RestReturn(value=ImportDataResponse.class)
    public RestResponse importBuildingData(@RequestParam(value = "attachment") MultipartFile[] files){
    	User manaUser = UserContext.current().getUser();
		Long userId = manaUser.getId();
		if(null == files || null == files[0]){
			LOGGER.error("files is null。userId="+userId);
			throw RuntimeErrorException.errorWith(UserServiceErrorCode.SCOPE, UserServiceErrorCode.ERROR_INVALID_PARAMS,
					"files is null");
		}
		ImportDataResponse importDataResponse = this.communityService.importBuildingData(files[0], userId);
        RestResponse response = new RestResponse(importDataResponse);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
}
