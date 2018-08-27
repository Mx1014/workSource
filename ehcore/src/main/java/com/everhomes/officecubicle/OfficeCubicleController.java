package com.everhomes.officecubicle;

import java.util.List;

import com.everhomes.rest.officecubicle.*;
import com.everhomes.rest.officecubicle.admin.CityDTO;
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
import com.everhomes.rest.officecubicle.admin.SearchSpacesAdminResponse;

/**
 * <ul>
 * <li>工位预定的控制器</li>
 * <li>提供给web-app端的用户操作，查询空间，查询详情，下订单和删除（不可见）订单</li>
 * </ul>
 */
@RestDoc(value="OfficeCubicle controller", site="officecubicle")
@RestController
@RequestMapping("/officecubicle")
public class OfficeCubicleController extends ControllerBase {
    private static final Logger LOGGER = LoggerFactory.getLogger(OfficeCubicleController.class);

    @Autowired
    private OfficeCubicleService officeCubicleService;
    /**
     * <b>URL: /officecubicle/getCityList</b> 
     * <p>工位预定-app端获取城市列表</p>
     */
    @RequestMapping("queryCities")
    @RestReturn(value=CityDTO.class ,collection=true)
    public RestResponse queryCities(QueryCitiesCommand cmd) {
    	List<CityDTO> resp = this.officeCubicleService.queryCities(cmd);
    	
        RestResponse response = new RestResponse(resp);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    	
    }
    
    /**
     * <b>URL: /officecubicle/querySpaces</b> 
     * <p>工位预定-获取空间列表</p>
     */
    @RequestMapping("querySpaces")
    @RestReturn(value=QuerySpacesResponse.class )
    public RestResponse querySpaces(QuerySpacesCommand cmd) {
    	QuerySpacesResponse resp = this.officeCubicleService.querySpaces(cmd);
    	this.officeCubicleService.updateCurrentUserSelectedCity(cmd.getProvinceName(),cmd.getCityName());
        RestResponse response = new RestResponse(resp);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    	
    }
    

    
    /**
     * <b>URL: /officecubicle/getSpaceDetail</b> 
     * <p>工位预定-空间详情</p>
     */
    @RequestMapping("getSpaceDetail")
    @RestReturn(value=OfficeSpaceDTO.class )
    public RestResponse getSpaceDetail(GetSpaceDetailCommand cmd) {
    	OfficeSpaceDTO resp = this.officeCubicleService.getSpaceDetail(cmd);
    	
        RestResponse response = new RestResponse(resp);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    	
    }
 
    
    
    /**
     * <b>URL: /officecubicle/addSpaceOrder</b> 
     * <p>工位预定-添加预定</p>
     */
    @RequestMapping("addSpaceOrder")
    @RestReturn(value=AddSpaceOrderResponse.class )
    public RestResponse addSpaceOrder(AddSpaceOrderCommand cmd) {
        RestResponse response = new RestResponse(officeCubicleService.addSpaceOrder(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;

    }

    /**
     * <b>URL: /officecubicle/getUserOrders</b> 
     * <p>预订记录-列表</p>
     */
    @RequestMapping("getUserOrders")
    @RestReturn(value=OfficeOrderDTO.class ,collection = true)
    public RestResponse getUserOrders(GetUserOrdersCommand cmd) {
    	List<OfficeOrderDTO> resp = this.officeCubicleService.getUserOrders();
    	
        RestResponse response = new RestResponse(resp);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    	
    }
    

    /**
     * <b>URL: /officecubicle/deleteUserSpaceOrder</b> 
     * <p>预订记录-删除预订记录（置为不可见）</p>
     */
    @RequestMapping("deleteUserSpaceOrder")
    @RestReturn(value=String.class )
    public RestResponse deleteUserSpaceOrder(DeleteUserSpaceOrderCommand cmd) {
    	this.officeCubicleService.deleteUserSpaceOrder(cmd);
    	
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    	
    }


    /**
     * <b>URL: /officecubicle/getCurrentProjectOnlyFlag</b>
     * <p>工位预定-是否本项目可见 1为本项目可见 0为全部可见</p>
     */
    @RequestMapping("getCurrentProjectOnlyFlag")
    @RestReturn(value=String.class )
    public RestResponse getCurrentProjectOnlyFlag(GetCurrentProjectOnlyFlagCommand cmd) {

        RestResponse response = new RestResponse(this.officeCubicleService.getCurrentProjectOnlyFlag(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
}

