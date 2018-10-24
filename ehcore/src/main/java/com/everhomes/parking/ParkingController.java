// @formatter:off
package com.everhomes.parking;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import com.everhomes.pay.order.OrderPaymentNotificationCommand;
import com.everhomes.rest.order.ListBizPayeeAccountDTO;
import com.everhomes.rest.order.PreOrderDTO;
import com.everhomes.rest.parking.*;
import com.everhomes.rest.promotion.order.MerchantPaymentNotificationCommand;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
import org.springframework.web.multipart.MultipartFile;

@RestDoc(value="Parking controller", site="parking")
@RestController
@RequestMapping("/parking")
public class ParkingController extends ControllerBase {
    
    @Autowired
    private ParkingService parkingService;

    /**
     * <b>URL: /parking/getParkingLotByToken</b>
     * <p>根据token查询停车场</p>
     */
    @RequestMapping("getParkingLotByToken")
    @RestReturn(value=ParkingLotDTO.class)
    public RestResponse getParkingLotByToken(GetParkingLotByTokenCommand cmd) {

        ParkingLotDTO parkingLot = parkingService.getParkingLotByToken(cmd);
        RestResponse response = new RestResponse(parkingLot);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /parking/transformToken</b>
     * <p>停车场id转token</p>
     */
    @RequestMapping("transformToken")
    @RestReturn(value=String.class, collection=true)
    public RestResponse transformToken(TransformTokenCommand cmd) {

        String token = parkingService.transformToken(cmd);
        RestResponse response = new RestResponse(token);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

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
    public RestResponse listParkingCards(@Valid ListParkingCardsCommand cmd) {

        List<ParkingCardDTO> parkingCardList = parkingService.listParkingCards(cmd);
        RestResponse response = new RestResponse(parkingCardList);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }


    /**
     * <b>URL: /parking/updateUserNotice</b>
     * <p>编辑用户须知</p>
     */
    @RequestMapping("updateUserNotice")
    @RestReturn(value=String.class)
    public RestResponse updateUserNotice(UpdateUserNoticeCommand cmd) {
        parkingService.updateParkingUserNotice(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    
    /**
     * <b>URL: /parking/getParkingCards</b>
     * <p>查询指定园区/小区、停车场、车牌号对应的月卡列表</p>
     */
    @RequestMapping("getParkingCards")
    @RestReturn(value=ParkingCardDTO.class, collection=true)
    public RestResponse getParkingCards(@Valid ListParkingCardsCommand cmd) {
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
     * <b>URL: /parking/getExpiredRechargeInfo</b>
     * <p>查询 过期月卡充值信息</p>
     */
    @RequestMapping("getExpiredRechargeInfo")
    @RestReturn(value=ParkingExpiredRechargeInfoDTO.class)
    public RestResponse getExpiredRechargeInfo(GetExpiredRechargeInfoCommand cmd) {

        RestResponse response = new RestResponse(parkingService.getExpiredRechargeInfo(cmd));
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
     * <b>URL: /parking/createParkingRechargeOrderV2</b>
     * <p>创建充值订单</p>
     */
    @RequestMapping("createParkingRechargeOrderV2")
    @RestReturn(value=PreOrderDTO.class)
    public RestResponse createParkingRechargeOrderV2(CreateParkingRechargeOrderCommand cmd) {

        PreOrderDTO dto = parkingService.createParkingRechargeOrderV2(cmd);
        RestResponse response = new RestResponse(dto);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /parking/createParkingTempOrderV2</b>
     * <p>创建临时订单</p>
     */
    @RequestMapping("createParkingTempOrderV2")
    @RestReturn(value=PreOrderDTO.class)
    public RestResponse createParkingTempOrderV2(CreateParkingTempOrderCommand cmd) {
        PreOrderDTO dto = parkingService.createParkingTempOrderV2(cmd);

        RestResponse response = new RestResponse(dto);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /parking/createParkingTempGeneralOrder</b>
     * <p>创建临时订单</p>
     */
    @RequestMapping("createParkingTempGeneralOrder")
    @RestReturn(value=CreateParkingGeneralOrderResponse.class)
    public RestResponse createParkingTempGeneralOrder(CreateParkingTempGeneralOrderCommand cmd) {
    	CreateParkingGeneralOrderResponse dto = parkingService.createParkingTempGeneralOrder(cmd);

        RestResponse response = new RestResponse(dto);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /parking/createParkingRechargeGeneralOrder</b>
     * <p>创建充值订单</p>
     */
    @RequestMapping("createParkingRechargeGeneralOrder")
    @RestReturn(value=CreateParkingGeneralOrderResponse.class)
    public RestResponse createParkingRechargeGeneralOrder(CreateParkingRechargeGeneralOrderCommand cmd) {
    	CreateParkingGeneralOrderResponse dto = parkingService.createParkingRechargeGeneralOrder(cmd);

        RestResponse response = new RestResponse(dto);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    

    /**
     * <b>URL: /parking/listPayeeAccount </b>
     * <p>获取收款方账号</p>
     */
    @RequestMapping("listPayeeAccount")
    @RestReturn(value=ListBizPayeeAccountDTO.class,collection = true)
    public RestResponse listPayeeAccount(ListPayeeAccountCommand cmd) {

        List<ListBizPayeeAccountDTO> list = parkingService.listPayeeAccount(cmd);
        RestResponse response = new RestResponse(list);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /parking/createOrUpdateBusinessPayeeAccount </b>
     * <p>关联收款方账号到具体业务</p>
     */
    @RequestMapping("createOrUpdateBusinessPayeeAccount")
    @RestReturn(value=String.class)
    public RestResponse createOrUpdateBusinessPayeeAccount(CreateOrUpdateBusinessPayeeAccountCommand cmd) {

        parkingService.createOrUpdateBusinessPayeeAccount(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /parking/listBusinessPayeeAccount </b>
     * <p>获取已关联收款账号的业务列表</p>
     */
    @RequestMapping("listBusinessPayeeAccount")
    @RestReturn(value=ListBusinessPayeeAccountResponse.class)
    public RestResponse listBusinessPayeeAccount(ListBusinessPayeeAccountCommand cmd) {

        RestResponse response = new RestResponse(parkingService.listBusinessPayeeAccount(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /parking/delBusinessPayeeAccount </b>
     * <p>删除已关联收款账号的业务</p>
     */
    @RequestMapping("delBusinessPayeeAccount")
    @RestReturn(value=String.class)
    public RestResponse delBusinessPayeeAccount(CreateOrUpdateBusinessPayeeAccountCommand cmd) {

        parkingService.delBusinessPayeeAccount(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }


    /**
     * <b>URL: /parking/rechargeOrderMigration </b>
     * <p>迁移支付系统订单号到停车订单表</p>
     */
    @RequestMapping("rechargeOrderMigration")
    @RestReturn(value=String.class)
    public RestResponse rechargeOrderMigration() {
        parkingService.rechargeOrderMigration();
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /parking/initPayeeAccount </b>
     * <p>将老的账户初始化到账号表</p>
     */
    @RequestMapping("initPayeeAccount")
    @RestReturn(value=String.class)
    public RestResponse initPayeeAccount(@RequestParam("attachment") MultipartFile[] files) {
        parkingService.initPayeeAccount(files);
        RestResponse response = new RestResponse();
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
     * <b>URL: /parking/exportParkingCardRequests</b>
     * <p>导出指定园区/小区、停车场、车牌对应的月卡申请列表</p>
     */
    @RequestMapping("exportParkingCardRequests")
    @RestReturn(value=ListParkingCardRequestResponse.class)
    public void exportParkingCardRequests(SearchParkingCardRequestsCommand cmd, HttpServletResponse response) {

        parkingService.exportParkingCardRequests(cmd, response);
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

    	ParkingCardRequestDTO dto = parkingService.getRequestParkingCardDetail(cmd);
        RestResponse response = new RestResponse(dto);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /parking/setParkingLotConfig</b>
     * <p>设置充值参数</p>
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
     * <p>获取申请月卡协议内容</p>
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
     * <b>URL: /parking/notifyParkingRechargeOrderPaymentV2</b>
     * <p>支付/退款后,支付系统回调</p>
     */
    @RequestMapping("notifyParkingRechargeOrderPaymentV2")
    @RestReturn(value = String.class)
    @RequireAuthentication(false)
    public RestResponse notifyParkingRechargeOrderPaymentV2(MerchantPaymentNotificationCommand cmd) {

        parkingService.notifyParkingRechargeOrderPaymentV2(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /parking/payNotify</b>
     * <p>提供微信/支付宝扫码支付成功后调用(临时)</p>
     */
    @RequestMapping("payNotify")
    @RestReturn(value = String.class)
    @RequireAuthentication(false)
    public RestResponse payNotify(WechatPayNotifyCommand cmd) {
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

        return parkingService.getRechargeOrderResult(cmd);
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
    @RestReturn(value=ParkingRechargeOrderDTO.class)
    public RestResponse updateParkingOrder(UpdateParkingOrderCommand cmd) {

        ParkingRechargeOrderDTO dto = parkingService.updateParkingOrder(cmd);
        RestResponse response = new RestResponse(dto);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /parking/refundParkingOrder</b>
     * <p>退款</p>
     */
    @RequestMapping("refundParkingOrder")
    @RestReturn(value=String.class)
    public RestResponse refundParkingOrder(RefundParkingOrderCommand cmd) {

        parkingService.refundParkingOrder(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /parking/getFreeSpaceNum</b>
     * <p>获取空闲车位数量</p>
     */
    @RequestMapping("getFreeSpaceNum")
    @RestReturn(value=ParkingFreeSpaceNumDTO.class)
    public RestResponse getFreeSpaceNum(GetFreeSpaceNumCommand cmd) {

        RestResponse response = new RestResponse(parkingService.getFreeSpaceNum(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /parking/getCarLocation</b>
     * <p>获取车位置</p>
     */
    @RequestMapping("getCarLocation")
    @RestReturn(value=ParkingCarLocationDTO.class)
    public RestResponse getCarLocation(GetCarLocationCommand cmd) {

        RestResponse response = new RestResponse(parkingService.getCarLocation(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /parking/listParkingCardRequestTypes</b>
     * <p>获取月卡申请卡类型</p>
     */
    @RequestMapping("listParkingCardRequestTypes")
    @RestReturn(value=ParkingCardRequestTypeDTO.class, collection = true)
    public RestResponse listParkingCardRequestTypes(ListParkingCardRequestTypesCommand cmd) {

        RestResponse response = new RestResponse(parkingService.listParkingCardRequestTypes(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /parking/listParkingInvoiceTypes</b>
     * <p>获取发票列表</p>
     */
    @RequestMapping("listParkingInvoiceTypes")
    @RestReturn(value=ParkingInvoiceTypeDTO.class, collection = true)
    public RestResponse listParkingInvoiceTypes(ListParkingInvoiceTypesCommand cmd) {

        RestResponse response = new RestResponse(parkingService.listParkingInvoiceTypes(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /parking/searchParkingCarVerifications</b>
     * <p>搜索车辆认证申请</p>
     */
    @RequestMapping("searchParkingCarVerifications")
    @RestReturn(value=SearchParkingCarVerificationResponse.class)
    public RestResponse searchParkingCarVerifications(SearchParkingCarVerificationsCommand cmd) {

        RestResponse response = new RestResponse(parkingService.searchParkingCarVerifications(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /parking/exportParkingCarVerifications</b>
     * <p>导出车辆认证申请</p>
     */
    @RequestMapping("exportParkingCarVerifications")
    @RestReturn(value=String.class)
    public RestResponse exportParkingCarVerifications(SearchParkingCarVerificationsCommand cmd,HttpServletResponse resp) {
        parkingService.exportParkingCarVerifications(cmd,resp);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /parking/listParkingCarVerifications</b>
     * <p>获取车辆认证申请列表</p>
     */
    @RequestMapping("listParkingCarVerifications")
    @RestReturn(value=ListParkingCarVerificationsResponse.class)
    public RestResponse listParkingCarVerifications(ListParkingCarVerificationsCommand cmd) {

        RestResponse response = new RestResponse(parkingService.listParkingCarVerifications(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /parking/getParkingCarVerificationById</b>
     * <p>获取车辆认证申请</p>
     */
    @RequestMapping("getParkingCarVerificationById")
    @RestReturn(value=ParkingCarVerificationDTO.class)
    public RestResponse getParkingCarVerificationById(GetParkingCarVerificationByIdCommand cmd) {

        RestResponse response = new RestResponse(parkingService.getParkingCarVerificationById(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /parking/requestCarVerification</b>
     * <p>车辆认证申请</p>
     */
    @RequestMapping("requestCarVerification")
    @RestReturn(value=ParkingCarVerificationDTO.class)
    public RestResponse requestCarVerification(RequestCarVerificationCommand cmd) {

        RestResponse response = new RestResponse(parkingService.requestCarVerification(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /parking/deleteCarVerification</b>
     * <p>删除车辆认证</p>
     */
    @RequestMapping("deleteCarVerification")
    @RestReturn(value=String.class)
    public RestResponse deleteCarVerification(DeleteCarVerificationCommand cmd) {

        parkingService.deleteCarVerification(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /*------------------------车位管理-------------------------- */

    /**
     * <b>URL: /parking/searchParkingHubs</b>
     * <p>搜索Hub</p>
     */
    @RequestMapping("searchParkingHubs")
    @RestReturn(value=SearchParkingHubsResponse.class)
    public RestResponse searchParkingHubs(SearchParkingHubsCommand cmd) {

        RestResponse response = new RestResponse(parkingService.searchParkingHubs(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /parking/createOrUpdateParkingHub</b>
     * <p>新增或者修改Hub</p>
     */
    @RequestMapping("createOrUpdateParkingHub")
    @RestReturn(value=ParkingHubDTO.class)
    public RestResponse createOrUpdateParkingHub(CreateOrUpdateParkingHubCommand cmd) {

        RestResponse response = new RestResponse(parkingService.createOrUpdateParkingHub(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /parking/deleteParkingHub</b>
     * <p>删除Hub</p>
     */
    @RequestMapping("deleteParkingHub")
    @RestReturn(value=String.class)
    public RestResponse deleteParkingHub(DeleteParkingHubCommand cmd) {
        parkingService.deleteParkingHub(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /parking/getParkingSpaceLockFullStatus</b>
     * <p>获取车位锁全部状态</p>
     */
    @RequestMapping("getParkingSpaceLockFullStatus")
    @RestReturn(value=GetParkingSpaceLockFullStatusDTO.class)
    public RestResponse getParkingSpaceLockFullStatus(DeleteParkingSpaceCommand cmd) {
        GetParkingSpaceLockFullStatusDTO fullStatus = parkingService.getParkingSpaceLockFullStatus(cmd);
        RestResponse response = new RestResponse(fullStatus);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /parking/searchParkingSpaces</b>
     * <p>搜索停车位</p>
     */
    @RequestMapping("searchParkingSpaces")
    @RestReturn(value=SearchParkingSpacesResponse.class)
    public RestResponse searchParkingSpaces(SearchParkingSpacesCommand cmd) {

        RestResponse response = new RestResponse(parkingService.searchParkingSpaces(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /parking/addParkingSpace</b>
     * <p>新增停车位</p>
     */
    @RequestMapping("addParkingSpace")
    @RestReturn(value=ParkingSpaceDTO.class)
    public RestResponse addParkingSpace(AddParkingSpaceCommand cmd) {

        RestResponse response = new RestResponse(parkingService.addParkingSpace(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /parking/updateParkingSpace</b>
     * <p>编辑停车位</p>
     */
    @RequestMapping("updateParkingSpace")
    @RestReturn(value=ParkingSpaceDTO.class)
    public RestResponse updateParkingSpace(UpdateParkingSpaceCommand cmd) {

        RestResponse response = new RestResponse(parkingService.updateParkingSpace(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /parking/updateParkingSpaceStatus</b>
     * <p>开放/关闭 停车位</p>
     */
    @RequestMapping("updateParkingSpaceStatus")
    @RestReturn(value=String.class)
    public RestResponse updateParkingSpaceStatus(UpdateParkingSpaceStatusCommand cmd) {

        parkingService.updateParkingSpaceStatus(cmd);

        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /parking/deleteParkingSpace</b>
     * <p>删除停车位</p>
     */
    @RequestMapping("deleteParkingSpace")
    @RestReturn(value=String.class)
    public RestResponse deleteParkingSpace(DeleteParkingSpaceCommand cmd) {

        parkingService.deleteParkingSpace(cmd);

        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /parking/listParkingSpaceLogs</b>
     * <p>获取停车位操作日志</p>
     */
    @RequestMapping("listParkingSpaceLogs")
    @RestReturn(value=ListParkingSpaceLogsResponse.class)
    public RestResponse listParkingSpaceLogs(ListParkingSpaceLogsCommand cmd) {

        RestResponse response = new RestResponse(parkingService.listParkingSpaceLogs(cmd));
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /parking/exportParkingSpaceLogs</b>
     * <p>获取停车位操作日志</p>
     */
    @RequestMapping("exportParkingSpaceLogs")
    @RestReturn(value=ListParkingSpaceLogsResponse.class)
    public RestResponse exportParkingSpaceLogs(ListParkingSpaceLogsCommand cmd,HttpServletResponse resp) {
    	
    	RestResponse response = new RestResponse(parkingService.exportParkingSpaceLogs(cmd,resp));
    	response.setErrorCode(ErrorCodes.SUCCESS);
    	response.setErrorDescription("OK");
    	return response;
    }

    /**
     * <b>URL: /parking/raiseParkingSpaceLock</b>
     * <p>升起车锁</p>
     */
    @RequestMapping("raiseParkingSpaceLock")
    @RestReturn(value=String.class)
    public RestResponse raiseParkingSpaceLock(RaiseParkingSpaceLockCommand cmd) {

        parkingService.raiseParkingSpaceLock(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /parking/downParkingSpaceLock</b>
     * <p>降下车锁</p>
     */
    @RequestMapping("downParkingSpaceLock")
    @RestReturn(value=String.class)
    public RestResponse downParkingSpaceLock(DownParkingSpaceLockCommand cmd) {

        parkingService.downParkingSpaceLock(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /parking/raiseParkingSpaceLockForWeb</b>
     * <p>升起车锁</p>
     */
    @RequestMapping("raiseParkingSpaceLockForWeb")
    @RestReturn(value=String.class)
    @RequireAuthentication(false)
    public RestResponse raiseParkingSpaceLockForWeb(RaiseParkingSpaceLockCommand cmd) {

        parkingService.raiseParkingSpaceLockForWeb(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /parking/downParkingSpaceLockForWeb</b>
     * <p>降下车锁</p>
     */
    @RequestMapping("downParkingSpaceLockForWeb")
    @RestReturn(value=String.class)
    @RequireAuthentication(false)
    public RestResponse downParkingSpaceLockForWeb(DownParkingSpaceLockCommand cmd) {

        parkingService.downParkingSpaceLockForWeb(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /parking/refreshToken</b>
     * <p>刷新token</p>
     */
    @RequestMapping("refreshToken")
    @RestReturn(value=String.class)
    public RestResponse refreshToken(RefreshTokenCommand cmd) {

        parkingService.refreshToken(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /parking/getWxParkingQrcode</b>
     * <p>
     * 点击下载临时车二维码，扫码支付临时车费用 V6.6
     * </p>
     */
    @RequestMapping("getWxParkingQrcode")
    @RestReturn(String.class)
    public RestResponse getWxParkingQrcode(GetWxParkingQrcodeCommand cmd, HttpServletResponse resp) {
        parkingService.getWxParkingQrcode(cmd,resp);

        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /parking/getParkingBussnessStatus</b>
     * <p>
     * 获取停车场对接功能，以及对外开放的功能集合 V6.6
     * </p>
     */
    @RequestMapping("getParkingBussnessStatus")
    @RestReturn(GetParkingBussnessStatusResponse.class)
    public RestResponse getParkingBussnessStatus(GetParkingBussnessStatusCommand cmd) {
        GetParkingBussnessStatusResponse baseResponse =  parkingService.getParkingBussnessStatus(cmd);

        RestResponse response = new RestResponse(baseResponse);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /parking/initFuncLists </b>
     * <p>初始化停车场对接功能表</p>
     */
    @RequestMapping("initFuncLists")
    @RestReturn(value=String.class)
    public RestResponse initFuncLists(GetParkingBussnessStatusCommand cmd) {
        parkingService.initFuncLists(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /parking/getInvoiceUrl </b>
     * <p>申请开票url</p>
     */
    @RequestMapping("getInvoiceUrl")
    @RestReturn(value=GetInvoiceUrlResponse.class)
    public RestResponse getInvoiceUrl(GetInvoiceUrlCommand cmd) {
    	GetInvoiceUrlResponse resp =  parkingService.getInvoiceUrl(cmd);
        RestResponse response = new RestResponse(resp);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
}
