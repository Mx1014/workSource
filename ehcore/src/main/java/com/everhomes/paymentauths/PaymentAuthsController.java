package com.everhomes.paymentauths;

import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestReturn;
import com.everhomes.paymentauths.PaymentAuthsService;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.acl.ServiceModuleDTO;
import com.everhomes.rest.paymentauths.*;
import com.everhomes.user.UserContext;
import com.everhomes.user.admin.SystemUserPrivilegeMgr;
import com.everhomes.rest.servicehotline.*;
import com.everhomes.techpark.servicehotline.HotlineService;
import com.everhomes.user.UserService;
import com.everhomes.util.RequireAuthentication;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
	public RestResponse checkUserAuths(CheckUserAuthsCommand cmd) {
		CheckUserAuthsResponse res = paymentAuthsService.checkUserAuths(cmd);
		RestResponse response =  new RestResponse(res);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	
	
	/**
	 * <p>列出授权用户</p>
	 * <b>URL: paymentAuths/listEnterpirsePaymentAuths</b>
	 */
	@RequestMapping("listEnterprisePaymentAuths")
	@RestReturn(value=EnterprisePaymentAuthsDTO.class, collection = true)
	public RestResponse listEnterpirsePaymentAuths(ListEnterprisePaymentAuthsCommand cmd) {
		List<EnterprisePaymentAuthsDTO> dto = paymentAuthsService.listEnterprisePaymentAuths(cmd);
		RestResponse response =  new RestResponse(dto);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
}
