package com.everhomes.recommend.admin;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.recommend.RecommendationConfig;
import com.everhomes.recommend.RecommendationService;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.recommend.CreateRecommendConfig;
import com.everhomes.rest.recommend.ListRecommendConfigCommand;
import com.everhomes.rest.recommend.ListRecommendConfigResponse;
import com.everhomes.user.UserContext;
import com.everhomes.user.admin.SystemUserPrivilegeMgr;

@RestDoc(value="Recommendation", site="ehcore")
@RestController
@RequestMapping("/admin/recommend")
public class RecommendationAdminController extends ControllerBase {
    @Autowired
    RecommendationService recommendationService;
    
    @RequestMapping("createConfig")
    @RestReturn(value=String.class)
    public RestResponse createConfig(@Valid CreateRecommendConfig cmd) {
        SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
        resolver.checkUserPrivilege(UserContext.current().getUser().getId(), 0);
        
        RestResponse response = new RestResponse();
        RecommendationConfig config = recommendationService.createConfig(cmd);
        if(config != null && config.getId() > 0) {
            response.setErrorCode(ErrorCodes.SUCCESS);
            response.setErrorDescription("OK");    
        } else {
            response.setErrorCode(ErrorCodes.ERROR_GENERAL_EXCEPTION);
            response.setErrorDescription("Cannot create suggest config");
        }
        
        return response;
    }
    
    @RequestMapping("listConfig")
    @RestReturn(value=ListRecommendConfigResponse.class)
    public RestResponse listRecommendConfig(@Valid ListRecommendConfigCommand cmd) {
        SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
        resolver.checkUserPrivilege(UserContext.current().getUser().getId(), 0);
        
        RestResponse response = new RestResponse(recommendationService.listRecommendConfigsBySource(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
}
