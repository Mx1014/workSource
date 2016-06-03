package com.everhomes.payment;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.order.CommonOrderDTO;
import com.everhomes.rest.payment.ApplyCardCommand;
import com.everhomes.rest.payment.ChangePasswordCommand;
import com.everhomes.rest.payment.CountCardUsersCommand;
import com.everhomes.rest.payment.CountCardUsersDTO;
import com.everhomes.rest.payment.ResetPasswordCommand;
import com.everhomes.rest.payment.GeiPaidResultCommand;
import com.everhomes.rest.payment.GeiPaidResultDTO;
import com.everhomes.rest.payment.GetCardInfoCommand;
import com.everhomes.rest.payment.CardInfoDTO;
import com.everhomes.rest.payment.GetCaptchaCommand;
import com.everhomes.rest.payment.GetCaptchaDTO;
import com.everhomes.rest.payment.GetPaidQrCodeCommand;
import com.everhomes.rest.payment.GetPaidQrCodeDTO;
import com.everhomes.rest.payment.ListTranscationsCommand;
import com.everhomes.rest.payment.ListTranscationsResponse;
import com.everhomes.rest.payment.RechargeCardCommand;
import com.everhomes.rest.payment.SearchCardUsersCommand;
import com.everhomes.rest.payment.SearchCardUsersResponse;
import com.everhomes.rest.payment.SearchCardRechargeOrderCommand;
import com.everhomes.rest.payment.SearchCardRechargeOrderResponse;
import com.everhomes.rest.payment.SearchCardTranscationsCommand;
import com.everhomes.rest.payment.SearchCardTranscationsResponse;
import com.everhomes.rest.payment.VerifyOpenCardCommand;
import com.everhomes.rest.payment.VerifyOpenCardDTO;


@Controller
@RequestMapping("payment")
public class PaymentCardController extends ControllerBase{


	
	/**
     * <b>URL: /payment/verifyOpenCard</b>
     * <p>验证是否开卡</p>
     */
	@RequestMapping("verifyOpenCard")
    @RestReturn(value=VerifyOpenCardDTO.class)
    public RestResponse verifyOpenCard(VerifyOpenCardCommand cmd) {
		VerifyOpenCardDTO dto = null;
        
        RestResponse response = new RestResponse(dto);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
	/**
     * <b>URL: /payment/getCardInfo</b>
     * <p>获取卡信息</p>
     */
    @RequestMapping("getCardInfo")
    @RestReturn(value=CardInfoDTO.class,collection=true)
    public RestResponse getCardInfo(GetCardInfoCommand cmd) {
        List<CardInfoDTO> result = null;
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
        
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
	/**
     * <b>URL: /payment/getPaidQrCode</b>
     * <p>获取二维码</p>
     */
    @RequestMapping("getPaidQrCode")
    @RestReturn(value=GetPaidQrCodeDTO.class)
    public RestResponse getPaidCode(GetPaidQrCodeCommand cmd) {
        
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /payment/geiPaidResult</b>
     * <p>获取支付结果</p>
     */
    @RequestMapping("geiPaidResult")
    @RestReturn(value=GeiPaidResultDTO.class)
    public RestResponse geiPaidResult(GeiPaidResultCommand cmd) {
        
        RestResponse response = new RestResponse();
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
    	CommonOrderDTO dto = null;
        RestResponse response = new RestResponse(dto);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
	/**
     * <b>URL: /payment/changePassword</b>
     * <p>修改密码</p>
     */
    @RequestMapping("changePassword")
    @RestReturn(value=String.class)
    public RestResponse changePassword(ChangePasswordCommand cmd) {
        
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /payment/getCaptcha</b>
     * <p>获取验证码</p>
     */
    @RequestMapping("getCaptcha")
    @RestReturn(value=GetCaptchaDTO.class)
    public RestResponse getCaptcha(GetCaptchaCommand cmd) {
        
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
	/**
     * <b>URL: /payment/resetPassword</b>
     * <p>忘记密码</p>
     */
    @RequestMapping("resetPassword")
    @RestReturn(value=String.class)
    public RestResponse forgetPassword(ResetPasswordCommand cmd) {
        
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

	/**
     * <b>URL: /payment/listTranscations</b>
     * <p>获取交易记录列表</p>
     */
    @RequestMapping("listTranscations")
    @RestReturn(value=ListTranscationsResponse.class)
    public RestResponse listTranscations(ListTranscationsCommand cmd) {
        
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
        
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    /**
     * <b>URL: /payment/countCardUsers</b>
     * <p>统计用户列表</p>
     */
    @RequestMapping("countCardUsers")
    @RestReturn(value=CountCardUsersDTO.class)
    public RestResponse countCardUsers(CountCardUsersCommand cmd) {
        
        RestResponse response = new RestResponse();
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
        
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    /**
     * <b>URL: /payment/searchCardTranscations</b>
     * <p>搜索卡交易记录列表</p>
     */
    @RequestMapping("searchCardTranscations")
    @RestReturn(value=SearchCardTranscationsResponse.class)
    public RestResponse searchCardTranscations(SearchCardTranscationsCommand cmd) {
        
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
    public void exportCardUsers(SearchCardUsersCommand cmd) {
        
        
    }
    /**
     * <b>URL: /payment/exportRechargeOrder</b>
     * <p>导出充值订单列表</p>
     */
    @RequestMapping("exportRechargeOrder")
    public void exportRechargeOrder(SearchCardRechargeOrderCommand cmd) {
        
    }
    /**
     * <b>URL: /payment/exportTranscations</b>
     * <p>导出交易记录列表</p>
     */
    @RequestMapping("exportTranscations")
    public void exportTranscations(SearchCardTranscationsCommand cmd) {
        
    }
    
}
