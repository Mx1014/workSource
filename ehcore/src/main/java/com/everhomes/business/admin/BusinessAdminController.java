package com.everhomes.business.admin;




import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.everhomes.business.BusinessService;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;

@RestDoc(value="Business admin controller", site="core")
@RestController
@RequestMapping("/admin/business")
public class BusinessAdminController extends ControllerBase {
    private static final Logger LOGGER = LoggerFactory.getLogger(BusinessAdminController.class);
    
    @Autowired
    private BusinessService businessService;

    /**
     * <b>URL: /admin/business/listBusinessesByKeyword</b>
     * <p>根据关键字查询店铺列表</p>
     */
    @RequestMapping("listBusinessesByKeyword")
    @RestReturn(value=ListBusinessesByKeywordAdminCommandResponse.class)
    public RestResponse listBusinessesByKeyword(@Valid ListBusinessesByKeywordAdminCommand cmd) {
        
        ListBusinessesByKeywordAdminCommandResponse res = businessService.listBusinessesByKeyword(cmd);
        RestResponse response =  new RestResponse(res);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    

    /**
     * <b>URL: /admin/business/recommendBusiness</b>
     * <p>根据关键字查询店铺列表</p>
     */
    @RequestMapping("recommendBusiness")
    @RestReturn(value=String.class)
    public RestResponse recommendBusiness(@Valid RecommendBusinessesAdminCommand cmd) {
        businessService.recommendBusiness(cmd);
        RestResponse response =  new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
}
