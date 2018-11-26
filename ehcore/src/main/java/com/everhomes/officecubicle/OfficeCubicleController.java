package com.everhomes.officecubicle;

import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import com.everhomes.rest.officecubicle.*;
import com.everhomes.rest.officecubicle.admin.AddCubicleAdminCommand;
import com.everhomes.rest.officecubicle.admin.AddRoomAdminCommand;
import com.everhomes.rest.officecubicle.admin.CityDTO;
import com.everhomes.rest.officecubicle.admin.SearchCubicleOrdersCommand;
import com.everhomes.rest.officecubicle.admin.SearchCubicleOrdersResponse;
import com.everhomes.rest.officecubicle.admin.SearchSpaceOrdersCommand;
import com.everhomes.rest.officecubicle.admin.SearchSpaceOrdersResponse;

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
    
    /**
     * <b>URL: /officecubicle/addCubicle</b> 
     * <p>增加工位</p>
     */
    @RequestMapping("addCubicle")
    public RestResponse addCubicle(AddCubicleAdminCommand cmd) {
    	 this.officeCubicleService.addCubicle(cmd);
    	
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    	
    }
    
    /**
     * <b>URL: /officecubicle/addRoom</b> 
     * <p>增加办公室</p>
     */
    @RequestMapping("addRoom")
    public RestResponse addRoom(AddRoomAdminCommand cmd) {
    	 this.officeCubicleService.addRoom(cmd);
    	
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    	
    }
    
    /**
     * <b>URL: /officecubicle/deleteRoom</b> 
     * <p>删除办公室</p>
     */
    @RequestMapping("deleteRoom")
    public RestResponse deleteRoom(DeleteRoomAdminCommand cmd) {
    	 this.officeCubicleService.deleteRoom(cmd);
    	
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    	
    }
    
    /**
     * <b>URL: /officecubicle/deleteCubicle</b> 
     * <p>删除工位</p>
     */
    @RequestMapping("deleteCubicle")
    public RestResponse deleteCubicle(DeleteCubicleAdminCommand cmd) {
    	 this.officeCubicleService.deleteCubicle(cmd);
    	
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    	
    }
    
    /**
     * <b>URL: /officecubicle/updateShortRentNums</b> 
     * <p>删除工位</p>
     */
    @RequestMapping("updateShortRentNums")
    public RestResponse updateShortRentNums(UpdateShortRentNumsCommand cmd) {
    	 this.officeCubicleService.updateShortRentNums(cmd);
    	
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    	
    }
    
	/**
	 * <b>URL: /officecubicle/exprotCubicleOrders</b>
	 * <p>
	 * 导出订单
	 * </p>
	 */
	@RequestMapping("exprotCubicleOrders")
	public String exprotSpaceOrders(@Valid SearchCubicleOrdersCommand cmd,HttpServletResponse response) {
//		HttpServletResponse commandResponse = rentalService.exportRentalBills(cmd, response );
		HttpServletResponse commandResponse = this.officeCubicleService.exportCubicleOrders(cmd,response);
		return null;
	}
	
    /**
     * <b>URL: /officecubicle/updateRoom</b> 
     * <p>编辑办公室</p>
     */
    @RequestMapping("updateRoom")
    public RestResponse updateRoom(AddRoomAdminCommand cmd) {
    	 this.officeCubicleService.updateRoom(cmd);
    	
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    	
    }
    
    /**
     * <b>URL: /officecubicle/updateCubicle</b> 
     * <p>编辑工位</p>
     */
    @RequestMapping("updateCubicle")
    public RestResponse updateCubicle(AddCubicleAdminCommand cmd) {
    	 this.officeCubicleService.updateCubicle(cmd);
    	
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    	
    }
    
    
    /**
     * <b>URL: /officecubicle/createCubicleGeneralOrder</b> 
     * <p>客户端创建订单</p>
     */
    @RequestMapping("createCubicleGeneralOrder")
    public RestResponse createCubicleGeneralOrder(CreateOfficeCubicleOrderCommand cmd) {
    	 this.officeCubicleService.createCubicleGeneralOrder(cmd);
    	
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    	
    }
    
    /**
     * <b>URL: /officecubicle/createCubicleOrderBackground</b> 
     * <p>后台创建订单</p>
     */
    @RequestMapping("createCubicleOrderBackground")
    public RestResponse createCubicleOrderBackground(CreateCubicleOrderBackgroundCommand cmd) {
    	 this.officeCubicleService.createCubicleOrderBackground(cmd);
    	
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    	
    }
    
    
    /**
     * <b>URL: /officecubicle/listOfficeCubiclPayeeAccount</b> 
     * <p>6.获取工位预定收款账户</p>
     */
    @RequestMapping("listOfficeCubiclPayeeAccount")
    public RestResponse listOfficeCubiclPayeeAccount(ListOfficeCubiclePayeeAccountCommand cmd) {
    	ListOfficeCubiclePayeeAccountResponse resp = new ListOfficeCubiclePayeeAccountResponse();
    	resp = this.officeCubicleService.listOfficeCubiclPayeeAccount(cmd);
    	
        RestResponse response = new RestResponse(resp);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    	
    }
    
    /**
     * <b>URL: /officecubicle/listOfficeCubicleAccount</b> 
     * <p>从电商处获取账户列表</p>
     */
    @RequestMapping("listOfficeCubicleAccount")
    @RestReturn(value=ListOfficeCubicleAccountDTO.class,collection = true)
    public RestResponse listOfficeCubicleAccount(ListOfficeCubicleAccountCommand cmd) {

    	List<ListOfficeCubicleAccountDTO> list = this.officeCubicleService.listOfficeCubicleAccount(cmd);
    	
        RestResponse response = new RestResponse(list);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    	
    }
    
    
    /**
     * <b>URL: /officecubicle/createOrUpdateOfficeCubiclePayeeAccount</b> 
     * <p>更新或创建收款账户</p>
     */
    @RequestMapping("createOrUpdateOfficeCubiclePayeeAccount")
    public RestResponse createOrUpdateOfficeCubiclePayeeAccount(CreateOrUpdateOfficeCubiclePayeeAccountCommand cmd) {

    	this.officeCubicleService.createOrUpdateOfficeCubiclePayeeAccount(cmd);
    	
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    	
    }
    
    /**
     * <b>URL: /officecubicle/searchCubicleOrders</b> 
     * <p>搜索订单</p>
     */
    @RequestMapping("searchCubicleOrders")
    @RestReturn(value=SearchCubicleOrdersResponse.class )
    public RestResponse searchCubicleOrders(SearchCubicleOrdersCommand cmd) {

    	SearchCubicleOrdersResponse resp = this.officeCubicleService.searchCubicleOrders(cmd);
        RestResponse response = new RestResponse(resp);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    	
    }
    
    /**
     * <b>URL: /officecubicle/refundOrder</b> 
     * <p>搜索订单</p>
     */
    @RequestMapping("refundOrder")
    public RestResponse refundOrder(RefundOrderCommand cmd) {

    	this.officeCubicleService.refundOrder(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    	
    }
    
    
}

