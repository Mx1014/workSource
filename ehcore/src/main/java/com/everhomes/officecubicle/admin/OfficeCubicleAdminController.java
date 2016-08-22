package com.everhomes.officecubicle.admin;

import javax.servlet.http.HttpServletResponse;
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
import com.everhomes.officecubicle.OfficeCubicleService;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.officecubicle.DeleteSpaceCommand;
import com.everhomes.rest.officecubicle.admin.AddSpaceCommand;
import com.everhomes.rest.officecubicle.admin.SearchSpaceOrdersCommand;
import com.everhomes.rest.officecubicle.admin.SearchSpaceOrdersResponse;
import com.everhomes.rest.officecubicle.admin.SearchSpacesAdminCommand;
import com.everhomes.rest.officecubicle.admin.SearchSpacesAdminResponse;
import com.everhomes.rest.officecubicle.admin.UpdateSpaceCommand;

/**
 * <ul>
 * <li>工位预定的控制器</li>
 * <li>提供给web-app端的用户操作，查询空间，查询详情，下订单和删除（不可见）订单</li>
 * </ul>
 */
@RestDoc(value="OfficeCubicle controller", site="officecubicle")
@RestController
@RequestMapping("/officecubicle")
public class OfficeCubicleAdminController extends ControllerBase {
    private static final Logger LOGGER = LoggerFactory.getLogger(OfficeCubicleAdminController.class);
    @Autowired
    private OfficeCubicleService officeCubicleService;
    /**
     * <b>URL: /officecubicle/searchSpaces</b> 
     * <p>空间管理-获取空间列表</p>
     */
    @RequestMapping("searchSpaces")
    @RestReturn(value=SearchSpacesAdminResponse.class )
    public RestResponse searchSpaces(SearchSpacesAdminCommand cmd) {
    	SearchSpacesAdminResponse resp = this.officeCubicleService.searchSpaces(cmd);
        RestResponse response = new RestResponse(resp);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    	
    }
    
    /**
     * <b>URL: /officecubicle/addSpace</b> 
     * <p>空间管理-添加空间</p>
     */
    @RequestMapping("addSpace")
    @RestReturn(value=String.class )
    public RestResponse addSpace(AddSpaceCommand cmd) {
    	this.officeCubicleService.addSpace(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    	
    } 
    
    /**
     * <b>URL: /officecubicle/updateSpace</b> 
     * <p>空间管理-更新空间</p>
     */
    @RequestMapping("updateSpace")
    @RestReturn(value=String.class )
    public RestResponse updateSpace(UpdateSpaceCommand cmd) {

    	this.officeCubicleService.updateSpace(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    	
    }
    
    

    /**
     * <b>URL: /officecubicle/deleteSpace</b> 
     * <p>空间管理-删除空间</p>
     */
    @RequestMapping("deleteSpace")
    @RestReturn(value=String.class )
    public RestResponse deleteSpace(DeleteSpaceCommand cmd) {
    	 this.officeCubicleService.deleteSpace(cmd);

        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    	
    }
    

    /**
     * <b>URL: /officecubicle/searchSpaceOrders</b> 
     * <p>预定详情-获取列表</p>
     */
    @RequestMapping("searchSpaceOrders")
    @RestReturn(value=SearchSpaceOrdersResponse.class )
    public RestResponse searchSpaceOrders(SearchSpaceOrdersCommand cmd) {

    	SearchSpaceOrdersResponse resp = this.officeCubicleService.searchSpaceOrders(cmd);
        RestResponse response = new RestResponse(resp);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    	
    }
	/**
	 * <b>URL: /officecubicle/exprotSpaceOrders</b>
	 * <p>
	 * 导出预订详情
	 * </p>
	 */
	@RequestMapping("exprotSpaceOrders")
	public String exprotSpaceOrders(@Valid SearchSpaceOrdersCommand cmd,HttpServletResponse response) {
//		HttpServletResponse commandResponse = rentalService.exportRentalBills(cmd, response );
		HttpServletResponse commandResponse = this.officeCubicleService.exprotSpaceOrders(cmd,response);
		return null;
	}
    
}
