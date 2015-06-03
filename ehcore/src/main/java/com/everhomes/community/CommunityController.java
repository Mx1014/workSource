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

@RestController
@RequestMapping("/community")
public class CommunityController extends ControllerBase {
    private static final Logger LOGGER = LoggerFactory.getLogger(CommunityController.class);
    
    @Autowired
    private CommunityService communityService;


    /**
     * <b>URL: /community/admin/listWaitingForApproveCommunities</b>
     * <p>查询待审核小区列表</p>
     */
    @RequestMapping("listWaitingForApproveCommunities")
    @RestReturn(value=ListWaitingForCommunitesCommandResponse.class)
    public RestResponse listWaitForApproveCommunities(@Valid ListWaitingForCommunitesCommand cmd) {
        
        ListWaitingForCommunitesCommandResponse cmdResponse = this.communityService.listWaitingForApproveCommunities(cmd);
        
        RestResponse response =  new RestResponse(cmdResponse);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
   
    

}
