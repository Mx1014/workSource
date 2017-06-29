// @formatter:off
package com.everhomes.parking;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import com.everhomes.bus.LocalBusOneshotSubscriber;
import com.everhomes.bus.LocalBusOneshotSubscriberBuilder;
import com.everhomes.rest.parking.*;
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
import com.everhomes.util.RequireAuthentication;
import org.springframework.web.context.request.async.DeferredResult;

@RestDoc(value="Parking controller", site="parking")
@RestController
@RequestMapping("/parking")
public class ParkingController extends ControllerBase {
    
    @Autowired
    private ParkingService parkingService;
    @Autowired
    private LocalBusOneshotSubscriberBuilder localBusSubscriberBuilder;
    /**
     * <b>URL: /parking/listParkingLots</b>
     * <p>查询指定园区/小区的停车场列表</p>
     */
    @RequestMapping("listParkingLots")
    @RestReturn(value=ParkingLotDTO.class, collection=true)
    public RestResponse listParkingLots(ListParkingLotsCommand cmd) {
        
    	List<ParkingLotDTO> parkingLotList = parkingService.listParkingLots(cmd);
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
    @Deprecated
    public RestResponse listParkingCards(@Valid ListParkingCardsCommand cmd) {
        List<ParkingCardDTO> parkingCardList;
        
        parkingCardList = parkingService.listParkingCards(cmd);
        RestResponse response = new RestResponse(parkingCardList);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /parking/getParkingCards</b>
     * <p>查询指定园区/小区、停车场、车牌号对应的月卡列表</p>
     */
    @RequestMapping("getParkingCards")
    @RestReturn(value=GetParkingCardsResponse.class)
    public RestResponse getParkingCards(GetParkingCardsCommand cmd) {

        RestResponse response = new RestResponse(parkingService.getParkingCards(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /parking/getOpenCardInfo</b>
     * <p>查询开通月卡信息</p>
     */
    @RequestMapping("getOpenCardInfo")
    @RestReturn(value=OpenCardInfoDTO.class)
    public RestResponse getOpenCardInfo(GetOpenCardInfoCommand cmd) {
    	
    	OpenCardInfoDTO dto = parkingService.getOpenCardInfo(cmd);
    	
        RestResponse response = new RestResponse(dto);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /parking/getParkingTempFee</b>
     * <p>查询停车临时费用</p>
     */
    @RequestMapping("getParkingTempFee")
    @RestReturn(value=ParkingTempFeeDTO.class)
    public RestResponse getParkingTempFee(GetParkingTempFeeCommand cmd) {
        
    	ParkingTempFeeDTO dto = parkingService.getParkingTempFee(cmd);
        RestResponse response = new RestResponse(dto);
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
        List<ParkingRechargeRateDTO> rateList = parkingService.listParkingRechargeRates(cmd);
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
	 * <b>URL: /parking/listCardType</b>
	 * @return
	 */
	@RequestMapping("listCardType")
	@RestReturn(value = ListCardTypeResponse.class)
	public RestResponse listCardType(ListCardTypeCommand cmd) {
		
		ListCardTypeResponse result = parkingService.listCardType(cmd);
		RestResponse response = new RestResponse(result);
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

        ListParkingRechargeOrdersResponse cmdResponse = parkingService.listParkingRechargeOrders(cmd);
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
        ListParkingRechargeOrdersResponse cmdResponse = parkingService.searchParkingRechargeOrders(cmd);
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
        
        CommonOrderDTO dto = parkingService.createParkingRechargeOrder(cmd);
        RestResponse response = new RestResponse(dto);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /parking/createParkingTempOrder</b>
     * <p>创建临时订单</p>
     */
    @RequestMapping("createParkingTempOrder")
    @RestReturn(value=CommonOrderDTO.class)
    public RestResponse createParkingTempOrder(CreateParkingTempOrderCommand cmd) {
        CommonOrderDTO dto = parkingService.createParkingTempOrder(cmd);
        
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
        
        ListParkingCardRequestResponse cmdResponse = parkingService.listParkingCardRequests(cmd);
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
        
        ListParkingCardRequestResponse cmdResponse = parkingService.searchParkingCardRequests(cmd);
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
     * <b>URL: /parking/getRequestParkingCardDetail</b>
     * <p>获取申请月卡详情</p>
     */
    @RequestMapping("getRequestParkingCardDetail")
    @RestReturn(value=ParkingCardRequestDTO.class)
    public RestResponse getRequestParkingCardDetail(GetRequestParkingCardDetailCommand cmd) {
        //TODO: 获取申请月卡详情
    	ParkingCardRequestDTO dto = parkingService.getRequestParkingCardDetail(cmd);
        RestResponse response = new RestResponse(dto);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /parking/setParkingLotConfig</b>
     */
    @RequestMapping("setParkingLotConfig")
    @RestReturn(value=String.class)
    public RestResponse setParkingLotConfig(SetParkingLotConfigCommand cmd) {
    	
    	parkingService.setParkingLotConfig(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /parking/setParkingRequestCardConfig</b>
     * <p>设置申请月卡参数</p>
     */
    @RequestMapping("setParkingRequestCardConfig")
    @RestReturn(value=String.class)
    public RestResponse setParkingRequestCardConfig(SetParkingRequestCardConfigCommand cmd) {
        
    	parkingService.setParkingRequestCardConfig(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /parking/getParkingRequestCardConfig</b>
     * <p>获取申请月卡参数</p>
     */
    @RequestMapping("getParkingRequestCardConfig")
    @RestReturn(value=ParkingRequestCardConfigDTO.class)
    public RestResponse getParkingRequestCardConfig(HttpServletRequest request, GetParkingRequestCardConfigCommand cmd) {

    	ParkingRequestCardConfigDTO dto = parkingService.getParkingRequestCardConfig(request, cmd);

        RestResponse response = new RestResponse(dto);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /parking/getParkingRequestCardAgreement</b>
     * <p>获取申请月卡参数</p>
     */
    @RequireAuthentication(false)
    @RequestMapping("getParkingRequestCardAgreement")
    @RestReturn(value=ParkingRequestCardAgreementDTO.class)
    public RestResponse getParkingRequestCardAgreement(GetParkingRequestCardAgreementCommand cmd) {

    	ParkingRequestCardAgreementDTO dto = parkingService.getParkingRequestCardAgreement(cmd);

        RestResponse response = new RestResponse(dto);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /parking/getSurplusCardCount</b>
     * <p>获取剩余月考数</p>
     */
    @RequestMapping("getSurplusCardCount")
    @RestReturn(value=SurplusCardCountDTO.class)
    public RestResponse getSurplusCardCount(GetParkingRequestCardConfigCommand cmd) {

    	SurplusCardCountDTO dto = parkingService.getSurplusCardCount(cmd);

        RestResponse response = new RestResponse(dto);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /parking/issueParkingCards</b>
     * <p>批量发放月卡</p>
     */
    @RequestMapping("issueParkingCards")
    @RestReturn(value=String.class)
    public RestResponse issueParkingCards(IssueParkingCardsCommand cmd) {
     
    	parkingService.issueParkingCards(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /parking/listParkingCarSeries</b>
     * <p>获取车品牌与车系</p>
     */
    @RequestMapping("listParkingCarSeries")
    @RestReturn(value=ListParkingCarSeriesResponse.class)
    public RestResponse listParkingCarSeries(ListParkingCarSeriesCommand cmd) {
        
    	ListParkingCarSeriesResponse resp = parkingService.listParkingCarSeries(cmd);
        RestResponse response = new RestResponse(resp);
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
     * <b>URL: /parking/getRechargeOrderResult</b>
            * <p>支付后，获取支付结果</p>
            */
    @RequestMapping("getRechargeOrderResult")
    @RestReturn(value = ParkingRechargeOrderDTO.class)
    public DeferredResult getRechargeOrderResult(GetRechargeResultCommand cmd) {

        final DeferredResult<RestResponse> deferredResult = new DeferredResult<RestResponse>(5000L,
                new RestResponse("time out"));
//        System.out.println(Thread.currentThread().getName());
//        map.put("test", deferredResult);

//        new Thread(() -> {
//            RestResponse response = new RestResponse("Received deferTest response");
//
//            deferredResult.setResult(response);
//        });
        localBusSubscriberBuilder.build("Parking-Recharge" + cmd.getOrderId(), new LocalBusOneshotSubscriber() {
            @Override
            public Action onLocalBusMessage(Object sender, String subject,
                                            Object pingResponse, String path) {
                ParkingRechargeOrderDTO dto = (ParkingRechargeOrderDTO) pingResponse;
                //    	ParkingCardDTO dto = parkingService.getRechargeResult(cmd);
                RestResponse response = new RestResponse(dto);
                response.setErrorCode(ErrorCodes.SUCCESS);
                response.setErrorDescription("OK");
                deferredResult.setResult(response);

                return null;
            }

            @Override
            public void onLocalBusListeningTimeout() {
                RestResponse response = new RestResponse("Notify timed out");
                deferredResult.setResult(response);
            }
        }).setTimeout(60000).create();

        return deferredResult;
    }

    /**
     * <b>URL: /parking/getRechargeResult</b>
     * <p>支付后，获取支付结果</p>
     * 兼容老接口，新写一个getRechargeOrderResult 重新定义返回值，提供给app使用
     */
    @RequestMapping("getRechargeResult")
    @RestReturn(value = ParkingCardDTO.class)
    @Deprecated
    public RestResponse getRechargeResult(GetRechargeResultCommand cmd) {

        ParkingCardDTO dto = parkingService.getRechargeResult(cmd);
        RestResponse response = new RestResponse(dto);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /parking/deleteParkingRechargeOrder</b>
     * <p>删除订单</p>
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
	 * <b>URL: /parking/exportParkingRechargeOrders</b>
	 * <p>
	 * 导出停车充值订单列表
	 * </p>
	 */
	@RequestMapping("exportParkingRechargeOrders")
	public void exportParkingRechargeOrders(SearchParkingRechargeOrdersCommand cmd,HttpServletResponse response ) {
		parkingService.exportParkingRechargeOrders(cmd, response );
	}
	
	/**
     * <b>URL: /parking/synchronizedData</b>
     * <p>同步申请月卡数据</p>
     */
    @RequestMapping("synchronizedData")
    @RestReturn(value = String.class)
    public RestResponse synchronizedData(ListParkingCardRequestsCommand cmd) {
        
    	parkingService.synchronizedData(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /parking/getParkingCarLockInfo</b>
     * <p>查询指定车牌锁车信息</p>
     */
    @RequestMapping("getParkingCarLockInfo")
    @RestReturn(value=ParkingCarLockInfoDTO.class)
    public RestResponse getParkingCarLockInfo(GetParkingCarLockInfoCommand cmd) {

        ParkingCarLockInfoDTO dto = parkingService.getParkingCarLockInfo(cmd);
        RestResponse response = new RestResponse(dto);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /parking/lockParkingCar</b>
     * <p>根据指定车牌锁车</p>
     */
    @RequestMapping("lockParkingCar")
    @RestReturn(value=String.class)
    public RestResponse lockParkingCar(LockParkingCarCommand cmd) {

        parkingService.lockParkingCar(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /parking/getParkingCarNums</b>
     * <p>获取停车场在场车辆数，目前只对接了博士高接口（科技园），其他园区请考虑返回为空的情况。</p>
     */
    @RequestMapping("getParkingCarNums")
    @RestReturn(value=GetParkingCarNumsResponse.class)
    public RestResponse getParkingCarNums(GetParkingCarNumsCommand cmd) {

        RestResponse response = new RestResponse(parkingService.getParkingCarNums(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /parking/updateParkingOrder</b>
     * <p>更新订单</p>
     */
    @RequestMapping("updateParkingOrder")
    @RestReturn(value=UpdateParkingOrderDTO.class)
    public RestResponse updateParkingOrder(UpdateParkingOrderCommand cmd) {

        UpdateParkingOrderDTO dto = parkingService.updateParkingOrder(cmd);
        RestResponse response = new RestResponse(dto);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /parking/refundParkingOrder</b>
     * <p>更新订单</p>
     */
    @RequestMapping("refundParkingOrder")
    @RestReturn(value=String.class)
    public RestResponse refundParkingOrder(UpdateParkingOrderCommand cmd) {

        parkingService.refundParkingOrder(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
}
