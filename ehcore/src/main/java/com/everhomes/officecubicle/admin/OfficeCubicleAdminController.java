package com.everhomes.officecubicle.admin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.officecubicle.admin.AddSpaceAdminCommand;
import com.everhomes.rest.officecubicle.admin.GetSpaceListAdminCommand;
import com.everhomes.rest.officecubicle.admin.GetSpaceListAdminResponse;
import com.everhomes.rest.officecubicle.admin.GetSpaceOrderListAdminCommand;
import com.everhomes.rest.officecubicle.admin.UpdateSpaceAdminCommand;

/**
 * <ul>
 * <li>工位预定的控制器</li>
 * <li>提供给web-app端的用户操作，查询空间，查询详情，下订单和删除（不可见）订单</li>
 * </ul>
 */
@RestDoc(value="OfficeCubicle controller", site="officecubicle")
@RestController
@RequestMapping("/officecubicle/admin")
public class OfficeCubicleAdminController extends ControllerBase {
    private static final Logger LOGGER = LoggerFactory.getLogger(OfficeCubicleAdminController.class);

    /**
     * <b>URL: /officecubicle/admin/getSpaceList</b> 
     * <p>空间管理-获取空间列表</p>
     */
    @RequestMapping("getSpaceList")
    @RestReturn(value=GetSpaceListAdminResponse.class )
    public RestResponse getSpaceList(GetSpaceListAdminCommand cmd) {
    	
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    	
    }
    
    /**
     * <b>URL: /officecubicle/admin/addSpace</b> 
     * <p>空间管理-添加空间</p>
     */
    @RequestMapping("addSpace")
    @RestReturn(value=String.class )
    public RestResponse addSpace(AddSpaceAdminCommand cmd) {
    	
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    	
    } 
    
    /**
     * <b>URL: /officecubicle/admin/updateSpace</b> 
     * <p>空间管理-更新空间</p>
     */
    @RequestMapping("updateSpace")
    @RestReturn(value=String.class )
    public RestResponse updateSpace(UpdateSpaceAdminCommand cmd) {
    	
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    	
    }

    /**
     * <b>URL: /officecubicle/admin/getSpaceOrderList</b> 
     * <p>预定详情-获取列表</p>
     */
    @RequestMapping("getSpaceOrderList")
    @RestReturn(value=GetSpaceOrderListAdminResponse.class )
    public RestResponse getSpaceOrderList(GetSpaceOrderListAdminCommand cmd) {
    	
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    	
    }
    
}
