package com.everhomes.techpark.rental;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.techpark.punch.ListPunchExceptionRequestCommandResponse;
import com.everhomes.techpark.punch.PunchController;

/**
 * <ul>
 * 预定系统：
 * <li>后台维护 某种场所 的预定规则 维护 具体场所</li>
 * <li>客户端可以查询某日，某具体场所 的开放时间，预定状态</li>
 * <li>客户端下预定订单，付费</li>
 * </ul>
 */
@RestDoc(value = "rental controller", site = "ehcore")
@RestController
@RequestMapping("/techpark/rental")
public class RentalController extends ControllerBase {

	private static final Logger LOGGER = LoggerFactory.getLogger(PunchController.class);

	@Autowired
	private RentalService rentalService;

	/**
	 * <b>URL: /techpark/rental/updateRentalRule</b>
	 * <p>
	 * 通用设置
	 * </p>
	 */
	@RequestMapping("updateRentalRule")
	@RestReturn(value = UpdateRentalRuleCommandResponse.class)
	public RestResponse updateRentalRule(@Valid UpdateRentalRuleCommand cmd) {
		UpdateRentalRuleCommandResponse updateRentalRuleCommandResponse = rentalService
				.updateRentalRule(cmd);
		RestResponse response = new RestResponse(
				updateRentalRuleCommandResponse);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /techpark/rental/addRentalSite</b>
	 * <p>
	 * 添加具体场所
	 * </p>
	 */
	@RequestMapping("addRentalSite")
	@RestReturn(value = String.class)
	public RestResponse addRentalSite(@Valid AddRentalSiteCommand cmd) {
		rentalService.addRentalSite(cmd);
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /techpark/rental/addRentalSiteItems</b>
	 * <p>
	 * 添加具体场所商品信息
	 * </p>
	 */
	@RequestMapping("addRentalSiteItems")
	@RestReturn(value = String.class)
	public RestResponse addRentalSiteItems(@Valid AddRentalSiteItemsCommand cmd) {
		rentalService.addRentalSiteItems(cmd);
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /techpark/rental/addRentalSiteRules</b>
	 * <p>
	 * 添加具体场所预定规则
	 * </p>
	 */

	@RequestMapping("addRentalSiteRules")
	@RestReturn(value = String.class)
	public RestResponse addRentalSiteRules(@Valid AddRentalSiteRulesCommand cmd) {
		rentalService.addRentalSiteRules(cmd);
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /techpark/rental/findRentalSiteDayStatus</b>
	 * <p>
	 * 查询某日某场所的状态
	 * </p>
	 */

	@RequestMapping("updateRentalRule")
	@RestReturn(value = FindRentalSiteDayStatusCommandResponse.class)
	public RestResponse findRentalSiteDayStatus(
			@Valid FindRentalSiteDayStatusCommand cmd) {
		FindRentalSiteDayStatusCommandResponse findRentalSiteDayStatusCommandResponse = rentalService
				.findRentalSiteDayStatus(cmd);
		RestResponse response = new RestResponse(
				findRentalSiteDayStatusCommandResponse);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	
	
}
