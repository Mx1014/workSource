package com.everhomes.officecubicle;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import com.everhomes.rest.officecubicle.*;
import com.everhomes.rest.officecubicle.admin.AddCubicleAdminCommand;
import com.everhomes.rest.officecubicle.admin.AddRoomAdminCommand;
import com.everhomes.rest.officecubicle.admin.CityDTO;
import com.everhomes.rest.officecubicle.admin.GetCubicleByStatusCommand;
import com.everhomes.rest.officecubicle.admin.GetCubicleByStatusResponse;
import com.everhomes.rest.officecubicle.admin.GetCubicleForAppCommand;
import com.everhomes.rest.officecubicle.admin.GetCubicleForAppResponse;
import com.everhomes.rest.officecubicle.admin.GetCubicleForOrderCommand;
import com.everhomes.rest.officecubicle.admin.GetCubicleForOrderResponse;
import com.everhomes.rest.officecubicle.admin.GetOfficeCubicleRentOrderCommand;
import com.everhomes.rest.officecubicle.admin.GetOfficeCubicleRentOrderResponse;
import com.everhomes.rest.officecubicle.admin.GetRoomByStatusCommand;
import com.everhomes.rest.officecubicle.admin.GetRoomByStatusResponse;
import com.everhomes.rest.officecubicle.admin.GetRoomDetailCommand;
import com.everhomes.rest.officecubicle.admin.GetRoomDetailResponse;
import com.everhomes.rest.officecubicle.admin.GetSpaceCommand;
import com.everhomes.rest.officecubicle.admin.GetSpaceResponse;
import com.everhomes.rest.officecubicle.admin.GetStationDetailCommand;
import com.everhomes.rest.officecubicle.admin.GetStationDetailResponse;
import com.everhomes.rest.officecubicle.admin.GetStationForRoomCommand;
import com.everhomes.rest.officecubicle.admin.GetStationForRoomResponse;
import com.everhomes.rest.officecubicle.admin.ListOfficeCubicleStatusCommand;
import com.everhomes.rest.officecubicle.admin.ListOfficeCubicleStatusResponse;
import com.everhomes.rest.officecubicle.admin.ListRentOrderForAppCommand;
import com.everhomes.rest.officecubicle.admin.ListRentOrderForAppResponse;
import com.everhomes.rest.officecubicle.admin.ListSpaceByCityCommand;
import com.everhomes.rest.officecubicle.admin.ListSpaceByCityResponse;
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
import com.everhomes.point.pointpool.PreOrderDTO;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.officecubicle.admin.SearchSpacesAdminResponse;
import com.everhomes.rest.promotion.order.MerchantPaymentNotificationCommand;
import com.everhomes.util.RequireAuthentication;

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
     * <b>URL: /officecubicle/updateOfficeCubicleRefundRule</b> 
     * <p>更新工会预定退款规则</p>
     */
    @RequestMapping("updateOfficeCubicleRefundRule")
    public RestResponse updateOfficeCubicleRefundRule(UpdateOfficeCubicleRefundRuleCommand cmd) {
    	 this.officeCubicleService.updateOfficeCubicleRefundRule(cmd);
    	
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    	
    }
    
    /**
     * <b>URL: /officecubicle/createCubicleOrderV2</b> 
     * <p>客户端创建订单</p>
     */
    @RequestMapping("createCubicleOrderV2")
    @RestReturn(CreateOfficeCubicleOrderResponse.class)
    public RestResponse createCubicleGeneralOrder(CreateOfficeCubicleOrderCommand cmd) {
    	CreateOfficeCubicleOrderResponse  resp = this.officeCubicleService.createCubicleOrderV2(cmd);
    	
        RestResponse response = new RestResponse(resp);
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
     * <b>URL: /officecubicle/getOfficeCubiclePayeeAccount</b> 
     * <p>6.获取工位预定收款账户</p>
     */
    @RequestMapping("getOfficeCubiclePayeeAccount")
    @RestReturn(ListOfficeCubicleAccountDTO.class)
    public RestResponse getOfficeCubiclePayeeAccount(GetOfficeCubiclePayeeAccountCommand cmd) {
    	ListOfficeCubicleAccountDTO resp = new ListOfficeCubicleAccountDTO();
    	resp = this.officeCubicleService.getOfficeCubiclePayeeAccount(cmd);
    	
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
     * <b>URL: /officecubicle/listRentOrderForApp</b> 
     * <p>搜索订单</p>
     */
    @RequestMapping("listRentOrderForApp")
    @RestReturn(value=ListRentOrderForAppResponse.class )
    public RestResponse listRentOrderForApp(ListRentOrderForAppCommand cmd) {

    	ListRentOrderForAppResponse resp = this.officeCubicleService.listRentOrderForApp(cmd);
        RestResponse response = new RestResponse(resp);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    	
    }
    
    /**
     * <b>URL: /officecubicle/getOfficeCubicleRentOrder</b> 
     * <p>搜索订单</p>
     */
    @RequestMapping("getOfficeCubicleRentOrder")
    @RestReturn(value=OfficeRentOrderDTO.class )
    public RestResponse getOfficeCubicleRentOrder(GetOfficeCubicleRentOrderCommand cmd) {

    	GetOfficeCubicleRentOrderResponse resp = this.officeCubicleService.getOfficeCubicleRentOrder(cmd);
        RestResponse response = new RestResponse(resp);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    	
    }
    
    /**
     * <b>URL: /officecubicle/refundOrder</b> 
     * <p>退款</p>
     */
    @RequestMapping("refundOrder")
    public RestResponse refundOrder(RefundOrderCommand cmd) {

    	this.officeCubicleService.refundOrder(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    	
    }
    
    /**
     * <b>URL: /officecubicle/listRentCubicle</b> 
     * <p>16.获取工位数量是否为0</p>
     */
    @RequestMapping("listRentCubicle")
    @RestReturn(ListRentCubicleResponse.class)
    public RestResponse listRentCubicle(ListRentCubicleCommand cmd) {

    	ListRentCubicleResponse resp = this.officeCubicleService.listRentCubicle(cmd);
        RestResponse response = new RestResponse(resp);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    	
    }
    
    /**
     * <b>URL: /officecubicle/getSpace</b> 
     * <p>获取空间</p>
     */
    @RequestMapping("getSpace")
    @RestReturn(GetSpaceResponse.class)
    public RestResponse getSpace(GetSpaceCommand cmd) {

    	GetSpaceResponse resp = this.officeCubicleService.getSpace(cmd);
        RestResponse response = new RestResponse(resp);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    	
    }
    
    /**
     * <b>URL: /officecubicle/listOfficeCubicleStatus</b> 
     * <p>列出空间工位状态</p>
     */
    @RequestMapping("listOfficeCubicleStatus")
    @RestReturn(ListOfficeCubicleStatusResponse.class)
    public RestResponse listOfficeCubicleStatus(ListOfficeCubicleStatusCommand cmd) {

    	ListOfficeCubicleStatusResponse resp = this.officeCubicleService.listOfficeCubicleStatus(cmd);
        RestResponse response = new RestResponse(resp);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    	
    }
    
    /**
     * <b>URL: /officecubicle/getCubicleDetail</b> 
     * <p>工位详情</p>
     */
    @RequestMapping("getCubicleDetail")
    @RestReturn(GetStationDetailResponse.class)
    public RestResponse getCubicleDetail(GetStationDetailCommand cmd) {

    	GetStationDetailResponse resp = this.officeCubicleService.getCubicleDetail(cmd);
        RestResponse response = new RestResponse(resp);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    	
    }
    /**
     * <b>URL: /officecubicle/getCubicleByStatus</b> 
     * <p>工位详情</p>
     */
    @RequestMapping("getCubicleByStatus")
    @RestReturn(GetCubicleByStatusResponse.class)
    public RestResponse getCubicleByStatus(GetCubicleByStatusCommand cmd) {

    	GetCubicleByStatusResponse resp = this.officeCubicleService.getCubicleByStatus(cmd);
        RestResponse response = new RestResponse(resp);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    	
    }
    
    
    /**
     * <b>URL: /officecubicle/getRoomDetail</b> 
     * <p>办公室详情</p>
     */
    @RequestMapping("getRoomDetail")
    @RestReturn(GetRoomDetailResponse.class)
    public RestResponse getRoomDetail(GetRoomDetailCommand cmd) {

    	GetRoomDetailResponse resp = this.officeCubicleService.getRoomDetail(cmd);
        RestResponse response = new RestResponse(resp);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    	
    }
    
    /**
     * <b>URL: /officecubicle/getRoomByStatus</b> 
     * <p>办公室详情</p>
     */
    @RequestMapping("getRoomByStatus")
    @RestReturn(GetRoomByStatusResponse.class)
    public RestResponse getRoomByStatus(GetRoomByStatusCommand cmd) {

    	GetRoomByStatusResponse resp = this.officeCubicleService.getRoomByStatus(cmd);
        RestResponse response = new RestResponse(resp);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    	
    }
    
    /**
     * <b>URL: /officecubicle/getStationForRoom</b> 
     * <p>新建办公室根据关键字获取工位</p>
     */
    @RequestMapping("getStationForRoom")
    @RestReturn(GetStationForRoomResponse.class)
    public RestResponse getStationForRoom(GetStationForRoomCommand cmd) {

    	GetStationForRoomResponse resp = this.officeCubicleService.getStationForRoom(cmd);
        RestResponse response = new RestResponse(resp);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    	
    }
    
    
    /**
     * <b>URL: /officecubicle/getOfficeCubicleRefundRule</b> 
     * <p>获取工会预定退款规则</p>
     */
    @RequestMapping("getOfficeCubicleRefundRule")
    @RestReturn(GetOfficeCubicleRefundRuleResponse.class)
    public RestResponse getOfficeCubicleRefundRule(GetOfficeCubicleRefundRuleCommand cmd) {

    	GetOfficeCubicleRefundRuleResponse resp = this.officeCubicleService.getOfficeCubicleRefundRule(cmd);
        RestResponse response = new RestResponse(resp);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    	
    }
    
    
    /**
     * <b>URL: /officecubicle/getCubicleForOrder</b> 
     * <p>创建订单根据时间/关键字获取工位/办公室</p>
     */
    @RequestMapping("getCubicleForOrder")
    @RestReturn(GetCubicleForOrderResponse.class)
    public RestResponse getStationForRoom(GetCubicleForOrderCommand cmd) {

    	GetCubicleForOrderResponse resp = this.officeCubicleService.getCubicleForOrder(cmd);
        RestResponse response = new RestResponse(resp);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    	
    }
    /**
     * <b>URL: /officecubicle/getCubicleForApp</b> 
     * <p>客户端获取工位列表</p>
     */
    @RequestMapping("getCubicleForApp")
    @RestReturn(GetCubicleForAppResponse.class)
    public RestResponse getCubicleForApp(GetCubicleForAppCommand cmd) {

    	GetCubicleForAppResponse resp = this.officeCubicleService.getCubicleForApp(cmd);
        RestResponse response = new RestResponse(resp);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    	
    }
    
    /**
     * <b>URL: /officecubicle/listSpaceByCity</b> 
     * <p>根据城市获取可见空间</p>
     */
    @RequestMapping("listSpaceByCity")
    @RestReturn(ListSpaceByCityResponse.class)
    public RestResponse listSpaceByCity(ListSpaceByCityCommand cmd) {

    	ListSpaceByCityResponse resp = this.officeCubicleService.listSpaceByCity(cmd);
        RestResponse response = new RestResponse(resp);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    	
    }
    
    
    /**
     * <b>URL: /officecubicle/payNotify</b>
     * <p>支付/退款后,支付系统回调</p>
     */
    @RequestMapping("payNotify")
    @RestReturn(value = String.class)
    @RequireAuthentication(false)
    public RestResponse payNotify(MerchantPaymentNotificationCommand cmd) {

    	officeCubicleService.payNotify(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /officecubicle/listCitiesByOrgIdAndCommunitId</b> 
     * <p>列出城市</p>
     */
    @RequestMapping("listCitiesByOrgIdAndCommunitId")
    @RestReturn(ListCitiesByOrgIdAndCommunitIdResponse.class)
    public RestResponse listCitiesByOrgIdAndCommunitId(ListCitiesByOrgIdAndCommunitIdCommand cmd) {

    	ListCitiesByOrgIdAndCommunitIdResponse resp = this.officeCubicleService.listCitiesByOrgIdAndCommunitId(cmd);
        RestResponse response = new RestResponse(resp);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    	
    }
    
	@RequestMapping("testSch")
	public String testSch(HttpServletRequest request,HttpServletResponse response){
		this.officeCubicleService.schedule();
		return null;
	}
}

