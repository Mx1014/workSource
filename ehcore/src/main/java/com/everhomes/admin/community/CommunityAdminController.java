package com.everhomes.admin.community;


import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.everhomes.community.ApproveCommunityCommand;
import com.everhomes.community.CommunityService;
import com.everhomes.community.ListCommunitesByStatusCommand;
import com.everhomes.community.ListCommunitesByStatusCommandResponse;
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
     * <b>URL: /admin/community/syncIndex</b>
     * <p>查询待审核小区列表</p>
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

}
