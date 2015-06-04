package com.everhomes.community;


import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.search.SearchSyncManager;

@RestController
@RequestMapping("/community")
public class CommunityController extends ControllerBase {
    private static final Logger LOGGER = LoggerFactory.getLogger(CommunityController.class);
    
    @Autowired
    private CommunityService communityService;

    @Autowired
    private SearchSyncManager searchSyncManager;

    /**
     * <b>URL: /community/admin/listCommunitiesByStatus</b>
     * <p>查询待审核小区列表</p>
     */
    @RequestMapping("admin/listCommunitiesByStatus")
    @RestReturn(value=ListCommunitesByStatusCommandResponse.class)
    public RestResponse listCommunitiesByStatus(@Valid ListCommunitesByStatusCommand cmd) {
        
        ListCommunitesByStatusCommandResponse cmdResponse = this.communityService.listCommunitiesByStatus(cmd);
        
        RestResponse response =  new RestResponse(cmdResponse);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /community/admin/approveCommunity</b>
     * <p>查询待审核小区列表</p>
     */
    @RequestMapping("admin/approveCommunity")
    @RestReturn(value=String.class)
    public RestResponse approveCommunity(@Valid ApproveCommunityCommand cmd) {
        
        this.communityService.approveCommuniy(cmd);
        
        RestResponse response =  new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
   
    /**
     * <b>URL: /community/admin/syncIndex</b>
     * <p>查询待审核小区列表</p>
     */
    @RequestMapping("admin/syncIndex")
    @RestReturn(value=String.class)
    public RestResponse syncIndex() {

        this.searchSyncManager.SyncDb("community");
        
        RestResponse response =  new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }    

}
