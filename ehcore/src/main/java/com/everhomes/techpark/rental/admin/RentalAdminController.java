package com.everhomes.techpark.rental.admin;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.techpark.rental.admin.AddDefaultRuleAdminCommand;
import com.everhomes.rest.techpark.rental.admin.QueryDefaultRuleAdminCommand;
import com.everhomes.rest.techpark.rental.admin.QueryDefaultRuleAdminResponse;

@RestDoc(value = "rental admin controller", site = "ehcore")
@RestController
@RequestMapping("/rental/admin")
public class RentalAdminController extends ControllerBase {

	@RequestMapping("addDefaultRule")
	@RestReturn(String.class)
	public RestResponse addDefaultRule(@Valid AddDefaultRuleAdminCommand addDefaultRuleAdminCommand) {

		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	@RequestMapping("queryDefaultRule")
	@RestReturn(QueryDefaultRuleAdminResponse.class)
	public RestResponse queryDefaultRule(@Valid QueryDefaultRuleAdminCommand queryDefaultRuleAdminCommand) {
		QueryDefaultRuleAdminResponse queryDefaultRuleAdminResponse = new QueryDefaultRuleAdminResponse();
		RestResponse response = new RestResponse(queryDefaultRuleAdminResponse);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

}
