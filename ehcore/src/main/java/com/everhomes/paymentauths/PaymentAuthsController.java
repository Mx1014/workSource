package com.everhomes.paymentauths;


import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestReturn;
import com.everhomes.paymentauths.PaymentAuthsService;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.paymentauths.*;
import com.everhomes.util.RequireAuthentication;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/paymentAuths")
public class PaymentAuthsController extends ControllerBase {
	@Autowired
	private PaymentAuthsService paymentAuthsService;

	/**
	 * <p>判断所选用户是否拥有企业支付权限</p>
	 * <b>URL: paymentAuths/checkUserAuths</b>
	 */
	@RequestMapping("checkUserAuths")
	@RestReturn(value=CheckUserAuthsResponse.class)
	@RequireAuthentication(value=false)
	public RestResponse checkUserAuths(CheckUserAuthsCommand cmd) {
		CheckUserAuthsResponse res = paymentAuthsService.checkUserAuths(cmd);
		RestResponse response =  new RestResponse(res);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	
	
	/**
	 * <p>列出授权用户</p>
	 * <b>URL: paymentAuths/listEnterprisePaymentAuths</b>
	 */
	@RequestMapping("listEnterprisePaymentAuths")
	@RestReturn(value=EnterprisePaymentAuthsDTO.class, collection = true)
	public RestResponse listEnterprisePaymentAuths(ListEnterprisePaymentAuthsCommand cmd) {
		List<EnterprisePaymentAuthsDTO> dto = paymentAuthsService.listEnterprisePaymentAuths(cmd);
		RestResponse response =  new RestResponse(dto);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	
	/**
	 * <p>更新授权用户</p>
	 * <b>URL: paymentAuths/updateEnterprisePaymentAuths</b>
	 */
	@RequestMapping("updateEnterprisePaymentAuths")
	@RestReturn(value=EnterprisePaymentAuthsDTO.class, collection = true)
	public RestResponse updateEnterprisePaymentAuths(UpdateEnterprisePaymentAuthsCommand cmd) {
		paymentAuthsService.updateEnterprisePaymentAuths(cmd);
		RestResponse response =  new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
}
