package com.everhomes.admin.community;


import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.everhomes.address.CommunityDTO;
import com.everhomes.community.ApproveCommunityCommand;
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
    public RestResponse approveCommunity(@Valid ApproveCommunityCommand cmd) {
        
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
    public RestResponse rejectCommunity(@Valid RejectCommunityCommand cmd) {
        
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
    public RestResponse updateCommunity(@Valid UpdateCommunityCommand cmd) {
        
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
    public RestResponse listCommunitiesByKeyword(@Valid ListComunitiesByKeywordCommand cmd) {
    	
    	ListCommunitiesByKeywordCommandResponse cmdResponse = this.communityService.listCommunitiesByKeyword(cmd);
    	
    	RestResponse response =  new RestResponse(cmdResponse);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    	
    }


}
