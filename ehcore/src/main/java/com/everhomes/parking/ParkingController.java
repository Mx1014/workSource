// @formatter:off
package com.everhomes.parking;

import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.order.CommonOrderDTO;
import com.everhomes.rest.order.PayCallbackCommand;
import com.everhomes.rest.parking.CreateParkingRechargeOrderCommand;
import com.everhomes.rest.parking.CreateParkingRechargeRateCommand;
import com.everhomes.rest.parking.DeleteParkingRechargeOrderCommand;
import com.everhomes.rest.parking.DeleteParkingRechargeRateCommand;
import com.everhomes.rest.parking.GetParkingActivityCommand;
import com.everhomes.rest.parking.IssueParkingCardsCommand;
import com.everhomes.rest.parking.ListParkingCardRequestResponse;
import com.everhomes.rest.parking.ListParkingCardRequestsCommand;
import com.everhomes.rest.parking.ListParkingCardsCommand;
import com.everhomes.rest.parking.ListParkingLotsCommand;
import com.everhomes.rest.parking.ListParkingRechargeOrdersCommand;
import com.everhomes.rest.parking.ListParkingRechargeOrdersResponse;
import com.everhomes.rest.parking.ListParkingRechargeRatesCommand;
import com.everhomes.rest.parking.ParkingActivityDTO;
import com.everhomes.rest.parking.ParkingCardDTO;
import com.everhomes.rest.parking.ParkingCardRequestDTO;
import com.everhomes.rest.parking.ParkingLotDTO;
import com.everhomes.rest.parking.ParkingRechargeRateDTO;
import com.everhomes.rest.parking.RequestParkingCardCommand;
import com.everhomes.rest.parking.SearchParkingCardRequestsCommand;
import com.everhomes.rest.parking.SearchParkingRechargeOrdersCommand;
import com.everhomes.rest.parking.SetParkingActivityCommand;
import com.everhomes.rest.parking.SetParkingCardIssueFlagCommand;
import com.everhomes.rest.parking.SetParkingCardReserveDaysCommand;

@RestDoc(value="Parking controller", site="parking")
@RestController
@RequestMapping("/parking")
public class ParkingController extends ControllerBase {
    
    @Autowired
    private ParkingService parkingService;
    
    /**
     * <b>URL: /parking/listParkingLots</b>
     * <p>查询指定园区/小区的停车场列表</p>
     */
    @RequestMapping("listParkingLots")
    @RestReturn(value=ParkingLotDTO.class, collection=true)
    public RestResponse listParkingLots(ListParkingLotsCommand cmd) {
        List<ParkingLotDTO> parkingLotList = null;
        
        parkingLotList = parkingService.listParkingLots(cmd);
        RestResponse response = new RestResponse(parkingLotList);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /parking/listParkingCards</b>
     * <p>查询指定园区/小区、停车场、车牌号对应的月卡列表</p>
     */
    @RequestMapping("listParkingCards")
    @RestReturn(value=ParkingCardDTO.class, collection=true)
    public RestResponse listParkingCards(@Valid ListParkingCardsCommand cmd) {
        List<ParkingCardDTO> parkingCardList = null;
        
        parkingCardList = parkingService.listParkingCards(cmd);
        RestResponse response = new RestResponse(parkingCardList);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /parking/listParkingRechargeRates</b>
     * <p>查询指定园区/小区、停车场对应的充值费率列表</p>
     */
    @RequestMapping("listParkingRechargeRates")
    @RestReturn(value=ParkingRechargeRateDTO.class, collection=true)
    public RestResponse listParkingRechargeRates(ListParkingRechargeRatesCommand cmd) {
        List<ParkingRechargeRateDTO> rateList = null;
        
        rateList = parkingService.listParkingRechargeRates(cmd);
        RestResponse response = new RestResponse(rateList);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /parking/createParkingRechargeRate</b>
     * <p>创建费率，博思高厂商可以创建，ETCP厂商不支持创建</p>
     */
    @RequestMapping("createParkingRechargeRate")
    @RestReturn(value=ParkingRechargeRateDTO.class)
    public RestResponse createParkingRechargeRate(CreateParkingRechargeRateCommand cmd) {
        ParkingRechargeRateDTO rechargeRate = null;
        
        rechargeRate = parkingService.createParkingRechargeRate(cmd);
        RestResponse response = new RestResponse(rechargeRate);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /parking/deleteParkingRechargeRate</b>
     * <p>删除费率，博思高厂商可以创建，ETCP厂商不支持创建</p>
     */
    @RequestMapping("deleteParkingRechargeRate")
    @RestReturn(value=String.class)
    public RestResponse deleteParkingRechargeRate(DeleteParkingRechargeRateCommand cmd) {
        parkingService.deleteParkingRechargeRate(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /parking/listParkingRechargeOrders</b>
     * <p>查询指定园区/小区、停车场、车牌对应的充值订单列表</p>
     */
    @RequestMapping("listParkingRechargeOrders")
    @RestReturn(value=ListParkingRechargeOrdersResponse.class)
    public RestResponse listParkingRechargeOrders(ListParkingRechargeOrdersCommand cmd) {
        ListParkingRechargeOrdersResponse cmdResponse = null;
        
        cmdResponse = parkingService.listParkingRechargeOrders(cmd);
        RestResponse response = new RestResponse(cmdResponse);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /parking/searchParkingRechargeOrders</b>
     * <p>搜索指定园区/小区、停车场、车牌对应的充值订单列表</p>
     */
    @RequestMapping("searchParkingRechargeOrders")
    @RestReturn(value=ListParkingRechargeOrdersResponse.class)
    public RestResponse searchParkingRechargeOrders(SearchParkingRechargeOrdersCommand cmd) {
        ListParkingRechargeOrdersResponse cmdResponse = null;
        
        cmdResponse = parkingService.searchParkingRechargeOrders(cmd);
        RestResponse response = new RestResponse(cmdResponse);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /parking/createParkingRechargeOrder</b>
     * <p>创建充值订单</p>
     */
    @RequestMapping("createParkingRechargeOrder")
    @RestReturn(value=CommonOrderDTO.class)
    public RestResponse createParkingRechargeOrder(CreateParkingRechargeOrderCommand cmd) {
        CommonOrderDTO dto = null;
        
        dto = parkingService.createParkingRechargeOrder(cmd);
        RestResponse response = new RestResponse(dto);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /parking/listParkingCardRequests</b>
     * <p>查询指定园区/小区、停车场、车牌对应的月卡申请列表</p>
     */
    @RequestMapping("listParkingCardRequests")
    @RestReturn(value=ListParkingCardRequestResponse.class)
    public RestResponse listParkingCardRequests(ListParkingCardRequestsCommand cmd) {
        ListParkingCardRequestResponse cmdResponse = null;
        
        cmdResponse = parkingService.listParkingCardRequests(cmd);
        RestResponse response = new RestResponse(cmdResponse);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /parking/searchParkingCardRequests</b>
     * <p>搜索指定园区/小区、停车场、车牌对应的月卡申请列表</p>
     */
    @RequestMapping("searchParkingCardRequests")
    @RestReturn(value=ListParkingCardRequestResponse.class)
    public RestResponse searchParkingCardRequests(SearchParkingCardRequestsCommand cmd) {
        ListParkingCardRequestResponse cmdResponse = null;
        
        cmdResponse = parkingService.searchParkingCardRequests(cmd);
        RestResponse response = new RestResponse(cmdResponse);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /parking/requestParkingCard</b>
     * <p>申请月卡</p>
     */
    @RequestMapping("requestParkingCard")
    @RestReturn(value=ParkingCardRequestDTO.class)
    public RestResponse requestParkingCard(RequestParkingCardCommand cmd) {
        
    	ParkingCardRequestDTO dto = parkingService.requestParkingCard(cmd);
        RestResponse response = new RestResponse(dto);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /parking/setParkingCardReserveDays</b>
     * <p>设置充值卡保留多少天（在发卡后到领卡之间的天数）</p>
     */
    @RequestMapping("setParkingCardReserveDays")
    @RestReturn(value=String.class)
    public RestResponse setParkingCardReserveDays(SetParkingCardReserveDaysCommand cmd) {
        // set parking card reserve days, according to the card_reserve_days of eh_parking_lots
        
    	parkingService.setParkingCardReserveDays(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /parking/issueParkingCards</b>
     * <p>发放月卡</p>
     */
    @RequestMapping("issueParkingCards")
    @RestReturn(value=String.class)
    public RestResponse issueParkingCards(IssueParkingCardsCommand cmd) {
        //发放卡，需要从eh_parking_card_requests表中取出在排队中的的申请记录（按时间从小大到排），然后把它们的状态置为“已通知”
        
    	parkingService.issueParkingCards(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /parking/setParkingCardIssueFlag</b>
     * <p>在线下发卡后，在后台管理中更新月卡领取状态</p>
     */
    @RequestMapping("setParkingCardIssueFlag")
    @RestReturn(value=String.class)
    public RestResponse setParkingCardIssueFlag(SetParkingCardIssueFlagCommand cmd) {
        //设置eh_parking_card_requests表的issue_flag字段
        
    	parkingService.setParkingCardIssueFlag(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /parking/setParkingActivity</b>
     * <p>设置活动规则</p>
     */
    @RequestMapping("setParkingActivity")
    @RestReturn(value=ParkingActivityDTO.class)
    public RestResponse setParkingActivity(SetParkingActivityCommand cmd) {
        ParkingActivityDTO activity = null;
        
        activity = parkingService.setParkingActivity(cmd);
        RestResponse response = new RestResponse(activity);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /parking/setParkingActivity</b>
     * <p>获取活动规则</p>
     */
    @RequestMapping("getParkingActivity")
    @RestReturn(value=ParkingActivityDTO.class)
    public RestResponse getParkingActivity(GetParkingActivityCommand cmd) {
        ParkingActivityDTO activity = null;
        
        activity = parkingService.getParkingActivity(cmd);
        RestResponse response = new RestResponse(activity);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /parking/notifyParkingRechargeOrderPayment</b>
     * <p>支付后，由统一支付调用此接口来通知各厂商支付结果</p>
     */
    @RequestMapping("notifyParkingRechargeOrderPayment")
    @RestReturn(value = String.class)
    public RestResponse notifyParkingRechargeOrderPayment(PayCallbackCommand cmd) {
        
    	parkingService.notifyParkingRechargeOrderPayment(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /parking/deleteParkingRechargeOrder</b>
     * <p>支付后，由统一支付调用此接口来通知各厂商支付结果</p>
     */
    @RequestMapping("deleteParkingRechargeOrder")
    @RestReturn(value = String.class)
    public RestResponse deleteParkingRechargeOrder(DeleteParkingRechargeOrderCommand cmd) {
        
    	parkingService.deleteParkingRechargeOrder(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
	 * <b>URL: /parking/exportParkingRechageOrders</b>
	 * <p>
	 * 导出停车充值订单列表
	 * </p>
	 */
	@RequestMapping("exportParkingRechageOrders")
	public  void exportParkingRechageOrders(SearchParkingRechargeOrdersCommand cmd,HttpServletResponse response ) {
		HttpServletResponse commandResponse = parkingService.exportParkingRechageOrders(cmd, response );
//		RestResponse response = new RestResponse(commandResponse);
//		response.setErrorCode(ErrorCodes.SUCCESS);
//		response.setErrorDescription("OK");
		//return commandResponse;
	}
}
