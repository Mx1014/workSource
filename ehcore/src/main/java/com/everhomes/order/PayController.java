package com.everhomes.order;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestReturn;
import com.everhomes.pay.order.OrderPaymentNotificationCommand;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.order.GetPaymentBalanceCommand;
import com.everhomes.rest.order.ListPaymentWithdrawOrderCommand;
import com.everhomes.rest.order.ListPaymentWithdrawOrderResponse;
import com.everhomes.rest.order.PaymentBalanceDTO;
import com.everhomes.rest.order.PaymentWithdrawCommand;
import com.everhomes.server.schema.tables.records.EhNamespacePayMappingsRecord;
import com.everhomes.user.UserContext;
import com.everhomes.util.RequireAuthentication;


@RestController
@RequestMapping("/pay")
public class PayController extends ControllerBase {
	private static final Logger LOGGER = LoggerFactory.getLogger(PayController.class);
	
	@Autowired
	private PayService payService;
	@Autowired
	private PayProvider payProvider;
	
	/**
	 * <b>URL: /pay/payNotify</b>
	 * <p>支付模块回调接口，通知支付结果</p>
	 */
	@RequestMapping("payNotify")
	@RestReturn(value=String.class)
	@RequireAuthentication(false)
	public RestResponse payNotify(@Valid OrderPaymentNotificationCommand cmd) {
//		try{
			payService.payNotify(cmd);
			RestResponse response = new RestResponse();
			response.setErrorCode(ErrorCodes.SUCCESS);
			response.setErrorDescription("OK");
			return response;
//		}catch (Exception ex){
//			RestResponse response = new RestResponse();
//			response.setErrorCode(ErrorCodes.ERROR_GENERAL_EXCEPTION);
//			response.setErrorDescription("FAIL");
//			return response;
//		}

	}

//	/**
//	 * <b>URL: /pay/createPreOrder</b>
//	 * <p>用于测试</p>
//	 */
//	@RequestMapping("createPreOrder")
//	@RestReturn(value=PreOrderDTO.class)
//	public RestResponse createPreOrder(PreOrderCommand cmd) {
//		PreOrderDTO dto = payService.createPreOrder(cmd);
//		RestResponse response = new RestResponse(dto);
//		response.setErrorCode(ErrorCodes.SUCCESS);
//		response.setErrorDescription("OK");
//		return response;
//	}



//	/**
//	 * <b>URL: /order/refundCallback</b>
//	 * <p>支付模块回调接口，通知支付结果</p>
//	 */
//	@RequestMapping("refundCallback")
//	@RestReturn(value=String.class)
//	public RestResponse refundCallback(@Valid RefundCallbackCommand  cmd) {
//		payService.refundCallback(cmd);
//		RestResponse response = new RestResponse();
//		response.setErrorCode(ErrorCodes.SUCCESS);
//		response.setErrorDescription("OK");
//		return response;
//	}
	
    /**
     * <b>URL: /pay/getPaymentBalance</b>
     * <p>获取帐户结算金额和可提现金额</p>
     */
    @RequestMapping("getPaymentBalance")
    @RestReturn(value=PaymentBalanceDTO.class)
    public RestResponse getPaymentSettlementAmounts(GetPaymentBalanceCommand cmd) {
        PaymentBalanceDTO dto = payService.getPaymentBalance(cmd.getOwnerType(), cmd.getOwnerId());
        
        RestResponse response = new RestResponse(dto);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /pay/withdrawPayment</b>
     * <p>提现</p>
     */
    @RequestMapping("withdrawPayment")
    @RestReturn(value=String.class)
    public RestResponse withdrawPayment(PaymentWithdrawCommand cmd) {
        payService.withdraw(cmd);
        
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /pay/listPaymentWithdrawOrders</b>
     * <p>列出提现订单详情</p>
     */
    @RequestMapping("listPaymentWithdrawOrders")
    @RestReturn(value=ListPaymentWithdrawOrderResponse.class)
    public RestResponse listPaymentWithdrawOrders(ListPaymentWithdrawOrderCommand cmd) {
        ListPaymentWithdrawOrderResponse cmdResponse = payService.listPaymentWithdrawOrders(cmd);
        
        RestResponse response = new RestResponse(cmdResponse);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /pay/getPayServerHomeURL</b>
     * <p>查询支付服务域名</p>
     */
    @RequestMapping("getPayServerHomeURL")
    public RestResponse getPayServerHomeURL() {

        String url = payService.getPayServerHomeURL();
        url = url.substring(0, url.length() - 4);
        RestResponse response = new RestResponse(url);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /pay/getPayAppKey</b>
     * <p>查询当前用户所在的域对应的支付appKey</p>
     */
    @RequestMapping("getPayAppKey")
    public RestResponse getPayAppKey() {
        EhNamespacePayMappingsRecord payMapping = payProvider.getNamespacePayMapping(UserContext.getCurrentNamespaceId());

        String appKey = payMapping.getAppKey();
        RestResponse response = new RestResponse(appKey);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
}
