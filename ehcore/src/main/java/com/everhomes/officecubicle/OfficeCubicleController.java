package com.everhomes.officecubicle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.officecubicle.AddSpaceOrderCommand;
import com.everhomes.rest.officecubicle.CityDTO;
import com.everhomes.rest.officecubicle.GetSpaceDetailCommand;
import com.everhomes.rest.officecubicle.GetSpaceListCommand;
import com.everhomes.rest.officecubicle.GetSpaceListResponse;
import com.everhomes.rest.officecubicle.OfficeOrderDTO;
import com.everhomes.rest.officecubicle.OfficeSpaceDTO;
import com.everhomes.rest.officecubicle.UnvisibleSpaceOrderCommand;

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

    /**
     * <b>URL: /officecubicle/getCityList</b> 
     * <p>工位预定-获取城市列表</p>
     */
    @RequestMapping("getCityList")
    @RestReturn(value=CityDTO.class ,collection=true)
    public RestResponse getCityList() {
    	
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    	
    }
    
    /**
     * <b>URL: /officecubicle/getSpaceList</b> 
     * <p>工位预定-获取空间列表</p>
     */
    @RequestMapping("getSpaceList")
    @RestReturn(value=GetSpaceListResponse.class )
    public RestResponse getSpaceList(GetSpaceListCommand cmd) {
    	
        RestResponse response = new RestResponse();
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
    	
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    	
    }

    /**
     * <b>URL: /officecubicle/addSpaceOrder</b> 
     * <p>工位预定-添加预定</p>
     */
    @RequestMapping("addSpaceOrder")
    @RestReturn(value=String.class )
    public RestResponse addSpaceOrder(AddSpaceOrderCommand cmd) {
    	
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    	
    }

    /**
     * <b>URL: /officecubicle/getUserOrderList</b> 
     * <p>预订记录-列表</p>
     */
    @RequestMapping("getUserOrderList")
    @RestReturn(value=OfficeOrderDTO.class ,collection = true)
    public RestResponse getUserOrderList() {
    	
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    	
    }
    

    /**
     * <b>URL: /officecubicle/unvisibleSpaceOrder</b> 
     * <p>预订记录-删除预订记录（置为不可见）</p>
     */
    @RequestMapping("unvisibleSpaceOrder")
    @RestReturn(value=String.class )
    public RestResponse unvisibleSpaceOrder(UnvisibleSpaceOrderCommand cmd) {
    	
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    	
    }
    
}

