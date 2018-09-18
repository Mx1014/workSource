package com.everhomes.payment;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.everhomes.pay.order.OrderPaymentNotificationCommand;
import com.everhomes.rest.asset.ListPayeeAccountsCommand;
import com.everhomes.rest.order.ListBizPayeeAccountDTO;
import com.everhomes.rest.order.PreOrderDTO;
import com.everhomes.rest.payment.*;
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
import com.everhomes.util.RequireAuthentication;


@RestDoc(value="Payment controller", site="payment")
@RestController
@RequestMapping("/payment")
public class PaymentCardController extends ControllerBase{

	@Autowired
	private PaymentCardService paymentCardService;
	@Autowired
    private PaymentCardPayService paymentCardPayService;

	/**
     * <b>URL: /payment/listCardInfo</b>
     * <p>获取卡信息</p>
     */
    @RequestMapping("listCardInfo")
    @RestReturn(value=CardInfoDTO.class,collection=true)
    public RestResponse listCardInfo(ListCardInfoCommand cmd) {
        List<CardInfoDTO> result = paymentCardService.listCardInfo(cmd);
        RestResponse response = new RestResponse(result);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /payment/getCardInfo</b>
     * <p>获取卡信息</p>
     */
    @RequestMapping("getCardInfo")
    @RestReturn(value=CardInfoDTO.class)
    public RestResponse getCardInfo(@RequestParam Long cardId) {
        CardInfoDTO cardInfo = paymentCardService.getCardInfo(cardId);
        RestResponse response = new RestResponse(cardInfo);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /payment/listCardIssuer</b>
     * <p>获取卡发行人</p>
     */
    @RequestMapping("listCardIssuer")
    @RestReturn(value=CardIssuerDTO.class,collection=true)
    public RestResponse listCardIssuer(ListCardIssuerCommand cmd) {
    	List<CardIssuerDTO> result = paymentCardService.listCardIssuer(cmd);
        RestResponse response = new RestResponse(result);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
	
	/**
     * <b>URL: /payment/applyCard</b>
     * <p>一卡通开卡</p>
     */
    @RequestMapping("applyCard")
    @RestReturn(value=CardInfoDTO.class)
    public RestResponse applyCard(ApplyCardCommand cmd) {
    	CardInfoDTO dto = paymentCardService.applyCard(cmd);
        RestResponse response = new RestResponse(dto);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
	/**
     * <b>URL: /payment/getCardPaidQrCode</b>
     * <p>获取二维码</p>
     */
    @RequestMapping("getCardPaidQrCode")
    @RestReturn(value=GetCardPaidQrCodeDTO.class)
    public RestResponse getCardPaidQrCode(GetCardPaidQrCodeCommand cmd) {
    	GetCardPaidQrCodeDTO dto = paymentCardService.getCardPaidQrCode(cmd);
        RestResponse response = new RestResponse(dto);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /payment/getCardPaidResult</b>
     * <p>获取支付结果</p>
     */
    @RequestMapping("getCardPaidResult")
    @RestReturn(value=GetCardPaidResultDTO.class)
    public RestResponse getCardPaidResult(GetCardPaidResultCommand cmd) {
    	GetCardPaidResultDTO dto = paymentCardService.getCardPaidResult(cmd);
        RestResponse response = new RestResponse(dto);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /payment/notifyPaidResult</b>
     * <p>第三方通知</p>
     */
    @RequireAuthentication(false)
    @RequestMapping("notifyPaidResult")
    @RestReturn(value=NotifyEntityDTO.class)
    public RestResponse notifyPaidResult(NotifyEntityCommand cmd) {
    	NotifyEntityDTO dto = paymentCardService.notifyPaidResult(cmd);
        RestResponse response = new RestResponse(dto);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
	/**
     * <b>URL: /payment/rechargeCard</b>
     * <p>一卡通充值</p>
     */
    @RequestMapping("rechargeCard")
    @RestReturn(value=CommonOrderDTO.class)
    public RestResponse rechargeCard(RechargeCardCommand cmd) {
    	CommonOrderDTO dto = paymentCardService.rechargeCard(cmd);
        RestResponse response = new RestResponse(dto);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /payment/rechargeCardV2</b>
     * <p>一卡通充值</p>
     */
    @RequestMapping("rechargeCardV2")
    @RestReturn(value=PreOrderDTO.class)
    public RestResponse rechargeCardV2(RechargeCardCommand cmd) {
        PreOrderDTO dto = paymentCardService.rechargeCardV2(cmd);
        RestResponse response = new RestResponse(dto);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /payment/refundChargingOrderV2</b>
     * <p>退款</p>
     */
    @RequestMapping("refundChargingOrderV2")
    @RestReturn(value=String.class)
    public RestResponse refundChargingOrderV2(@RequestParam Long orderId) {
        paymentCardService.refundOrderV2(orderId);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

	/**
     * <b>URL: /payment/setCardPassword</b>
     * <p>修改密码</p>
     */
    @RequestMapping("setCardPassword")
    @RestReturn(value=String.class)
    public RestResponse setCardPassword(SetCardPasswordCommand cmd) {
    	paymentCardService.setCardPassword(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /payment/sendCardVerifyCode</b>
     * <p>获取验证码</p>
     */
    @RequestMapping("sendCardVerifyCode")
    @RestReturn(value=SendCardVerifyCodeDTO.class)
    public RestResponse sendCardVerifyCode(SendCardVerifyCodeCommand cmd) {
    	SendCardVerifyCodeDTO dto = paymentCardService.sendCardVerifyCode(cmd);
        RestResponse response = new RestResponse(dto);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
	/**
     * <b>URL: /payment/resetCardPassword</b>
     * <p>忘记密码</p>
     */
    @RequestMapping("resetCardPassword")
    @RestReturn(value=String.class)
    public RestResponse resetCardPassword(ResetCardPasswordCommand cmd) {
    	paymentCardService.resetCardPassword(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

	/**
     * <b>URL: /payment/listCardTransactions</b>
     * <p>获取交易记录列表</p>
     */
    @RequestMapping("listCardTransactions")
    @RestReturn(value=ListCardTransactionsResponse.class)
    public RestResponse listCardTransactions(ListCardTransactionsCommand cmd) {
    	ListCardTransactionsResponse resp = paymentCardService.listCardTransactions(cmd);
        RestResponse response = new RestResponse(resp);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /payment/freezeCard</b>
     * <p>挂失或解挂 账户</p>
     */
    @RequestMapping("freezeCard")
    @RestReturn(value=String.class)
    public RestResponse freezeCard(FreezeCardCommand cmd) {
        paymentCardService.freezeCard(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /payment/unbundleCard</b>
     * <p>解绑 账户</p>
     */
    @RequestMapping("unbundleCard")
    @RestReturn(value=String.class)
    public RestResponse unbundleCard(@RequestParam Long cardId) {
        paymentCardService.unbunleCard(cardId);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /*---------web ---------- */
    /**
     * <b>URL: /payment/searchCardUsers</b>
     * <p>搜索用户列表</p>
     */
    @RequestMapping("searchCardUsers")
    @RestReturn(value=SearchCardUsersResponse.class)
    public RestResponse searchCardUsers(SearchCardUsersCommand cmd) {
    	SearchCardUsersResponse resp = paymentCardService.searchCardUsers(cmd);
        RestResponse response = new RestResponse(resp);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    /**
     * <b>URL: /payment/getCardUserStatistics</b>
     * <p>统计用户列表</p>
     */
    @RequestMapping("getCardUserStatistics")
    @RestReturn(value=GetCardUserStatisticsDTO.class)
    public RestResponse getCardUserStatistics(GetCardUserStatisticsCommand cmd) {
    	GetCardUserStatisticsDTO dto = paymentCardService.getCardUserStatistics(cmd);
        RestResponse response = new RestResponse(dto);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    /**
     * <b>URL: /payment/searchCardRechargeOrder</b>
     * <p>搜索充值订单列表</p>
     */
    @RequestMapping("searchCardRechargeOrder")
    @RestReturn(value=SearchCardRechargeOrderResponse.class)
    public RestResponse searchCardRechargeOrder(SearchCardRechargeOrderCommand cmd) {
    	SearchCardRechargeOrderResponse resp = paymentCardService.searchCardRechargeOrder(cmd);
        RestResponse response = new RestResponse(resp);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /payment/updateCardRechargeOrder</b>
     * <p>更新充值订单</p>
     */
    @RequestMapping("updateCardRechargeOrder")
    @RestReturn(value=String.class)
    public RestResponse updateCardRechargeOrder(UpdateCardRechargeOrderCommand cmd) {
    	paymentCardService.updateCardRechargeOrder(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    /**
     * <b>URL: /payment/searchCardTransactions</b>
     * <p>搜索卡交易记录列表</p>
     */
    @RequestMapping("searchCardTransactions")
    @RestReturn(value=SearchCardTransactionsResponse.class)
    public RestResponse searchCardTransactions(SearchCardTransactionsCommand cmd) {
    	SearchCardTransactionsResponse resp = paymentCardService.searchCardTransactions(cmd);
        RestResponse response = new RestResponse(resp);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     *
     * <b>URL: /payment/payNotify <b>
     * <p>支付回调</p>
     */
    @RequireAuthentication(false)
    @RequestMapping("payNotify")
    @RestReturn(String.class)
    public RestResponse payNotify(OrderPaymentNotificationCommand cmd) {
        this.paymentCardService.payNotify(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     *
     * <b>URL: /rental/refundNotify <b>
     * <p>退款回调</p>
     */
    @RequireAuthentication(false)
    @RequestMapping("refundNotify")
    @RestReturn(String.class)
    public RestResponse refundNotify(OrderPaymentNotificationCommand  cmd) {
        this.paymentCardService.refundNotify(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /payment/exportCardUsers</b>
     * <p>导出用户列表</p>
     */
    @RequestMapping("exportCardUsers")
    public void exportCardUsers(SearchCardUsersCommand cmd,HttpServletResponse response) {
    	paymentCardService.exportCardUsers(cmd, response);
        
    }
    /**
     * <b>URL: /payment/exportCardRechargeOrder</b>
     * <p>导出充值订单列表</p>
     */
    @RequestMapping("exportCardRechargeOrder")
    public void exportCardRechargeOrder(SearchCardRechargeOrderCommand cmd,HttpServletResponse response) {
    	paymentCardService.exportCardRechargeOrder(cmd, response);
    }
    /**
     * <b>URL: /payment/exportCardTransactions</b>
     * <p>导出交易记录列表</p>
     */
    @RequestMapping("exportCardTransactions")
    public void exportCardTransactions(SearchCardTransactionsCommand cmd,HttpServletResponse response) {
    	paymentCardService.exportCardTransactions(cmd, response);
    }

    /**
     * <b>URL: /payment/listPayeeAccounts</b>
     * <p>
     * 查询企业账户信息
     * </p>
     */
    @RequestMapping("listPayeeAccounts")
    @RestReturn(value = ListBizPayeeAccountDTO.class,collection = true)
    public RestResponse listPayeeAccounts( ListPayeeAccountsCommand cmd) {
        List<ListBizPayeeAccountDTO> list =  paymentCardPayService.listPayeeAccounts(cmd);
        RestResponse response = new RestResponse(list);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /payment/updateAccountSetting</b>
     * <p>
     * 更新支付账户设定
     * </p>
     */
    @RequestMapping("updateAccountSetting")
    @RestReturn(value = String.class)
    public RestResponse updateAccountSetting( UpdateAccountSettingCommand cmd) {
        paymentCardPayService.updateAccountSetting(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /payment/getAccountSetting</b>
     * <p>
     * 获取支付账户设定
     * </p>
     */
    @RequestMapping("getAccountSetting")
    @RestReturn(value = ListBizPayeeAccountDTO.class)
    public RestResponse getAccountSetting( GetAccountSettingCommand cmd) {
        ListBizPayeeAccountDTO dto = paymentCardPayService.getAccountSetting(cmd);
        RestResponse response = new RestResponse(dto);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /payment/getHotline</b>
     * <p>
     * 获取服务热线
     * </p>
     */
    @RequestMapping("getHotline")
    @RestReturn(value = PaymentCardHotlineDTO.class)
    public RestResponse getHotline( GetHotlineCommand cmd) {
        PaymentCardHotlineDTO dto = paymentCardService.getHotline(cmd);
        RestResponse response = new RestResponse(dto);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    /**
     * <b>URL: /payment/updateHotline</b>
     * <p>
     * 更新服务热线
     * </p>
     */
    @RequestMapping("updateHotline")
    @RestReturn(value = String.class)
    public RestResponse updateHotline( UpdateHotlineCommand cmd) {
        paymentCardService.updateHotline(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

}
