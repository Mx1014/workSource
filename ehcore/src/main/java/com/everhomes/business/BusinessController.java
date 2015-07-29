package com.everhomes.business;



import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;

@RestDoc(value="Business controller", site="core")
@RestController
@RequestMapping("/business")
public class BusinessController extends ControllerBase {
    private static final Logger LOGGER = LoggerFactory.getLogger(BusinessController.class);
    
    @Autowired
    private BusinessService businessService;

    /**
     * <b>URL: /business/getBusinessesByCategory</b>
     * <p>根据分类获取该类别下的店铺列表</p>
     */
    @RequestMapping("getBusinessesByCategory")
    @RestReturn(value=BusinessDTO.class,collection=true)
    public RestResponse getBusinessesByCategory(@Valid GetBusinessesByCategoryCommand cmd) {
        
        List<BusinessDTO> result = businessService.getBusinessesByCategory(cmd);
        RestResponse response =  new RestResponse(result);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /business/createBusiness</b>
     * <p>创建店铺</p>
     */
    @RequestMapping("createBusiness")
    @RestReturn(value=String.class)
    public RestResponse createBusinesses(@Valid CreateBusinessCommand cmd) {
        
        businessService.createBusiness(cmd);
        RestResponse response =  new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /business/updateBusiness</b>
     * <p>更新店铺信息</p>
     */
    @RequestMapping("updateBusiness")
    @RestReturn(value=String.class)
    public RestResponse updateBusiness(@Valid UpdateBusinessCommand cmd) {
        
        businessService.updateBusiness(cmd);
        RestResponse response =  new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /business/deleteBusiness</b>
     * <p>删除店铺</p>
     */
    @RequestMapping("deleteBusiness")
    @RestReturn(value=String.class)
    public RestResponse deleteBusiness(@Valid DeleteBusinessCommand cmd) {
        
        businessService.deleteBusiness(cmd);
        RestResponse response =  new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    


}
