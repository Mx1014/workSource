package com.everhomes.payment;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.everhomes.rest.order.PreOrderDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.order.CommonOrderDTO;
import com.everhomes.rest.payment.ApplyCardCommand;
import com.everhomes.rest.payment.CardIssuerDTO;
import com.everhomes.rest.payment.NotifyEntityCommand;
import com.everhomes.rest.payment.NotifyEntityDTO;
import com.everhomes.rest.payment.SetCardPasswordCommand;
import com.everhomes.rest.payment.GetCardUserStatisticsCommand;
import com.everhomes.rest.payment.GetCardUserStatisticsDTO;
import com.everhomes.rest.payment.ListCardIssuerCommand;
import com.everhomes.rest.payment.ResetCardPasswordCommand;
import com.everhomes.rest.payment.GetCardPaidResultCommand;
import com.everhomes.rest.payment.GetCardPaidResultDTO;
import com.everhomes.rest.payment.ListCardInfoCommand;
import com.everhomes.rest.payment.CardInfoDTO;
import com.everhomes.rest.payment.SendCardVerifyCodeCommand;
import com.everhomes.rest.payment.SendCardVerifyCodeDTO;
import com.everhomes.rest.payment.GetCardPaidQrCodeCommand;
import com.everhomes.rest.payment.GetCardPaidQrCodeDTO;
import com.everhomes.rest.payment.ListCardTransactionsCommand;
import com.everhomes.rest.payment.ListCardTransactionsResponse;
import com.everhomes.rest.payment.RechargeCardCommand;
import com.everhomes.rest.payment.SearchCardUsersCommand;
import com.everhomes.rest.payment.SearchCardUsersResponse;
import com.everhomes.rest.payment.SearchCardRechargeOrderCommand;
import com.everhomes.rest.payment.SearchCardRechargeOrderResponse;
import com.everhomes.rest.payment.SearchCardTransactionsCommand;
import com.everhomes.rest.payment.SearchCardTransactionsResponse;
import com.everhomes.rest.payment.UpdateCardRechargeOrderCommand;
import com.everhomes.util.RequireAuthentication;


@RestDoc(value="Payment controller", site="payment")
@RestController
@RequestMapping("/payment")
public class PaymentCardController extends ControllerBase{

	@Autowired
	private PaymentCardService paymentCardService;

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
    
}
