package com.everhomes.recommend;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;

@RestDoc(value="Recommendation", site="ehcore")
@RestController
@RequestMapping("/admin/recommend")
public class RecommendationAdminController extends ControllerBase {
    @Autowired
    RecommendationService recommendationService;
    
    @RequestMapping("createConfig")
    @RestReturn(value=String.class)
    public RestResponse createConfig(@Valid CreateRecommendConfig cmd) {
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
    
//    @RequestMapping("listConfig")
//    @RestReturn(value=RecommendConfigResponse.class)
}
