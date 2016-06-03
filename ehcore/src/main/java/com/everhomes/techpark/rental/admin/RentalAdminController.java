package com.everhomes.techpark.rental.admin;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.techpark.rental.admin.AddDefaultRuleAdminCommand;
import com.everhomes.rest.techpark.rental.admin.AddResourceAdminCommand;
import com.everhomes.rest.techpark.rental.admin.GetResourceListAdminCommand;
import com.everhomes.rest.techpark.rental.admin.GetResourceListAdminResponse;
import com.everhomes.rest.techpark.rental.admin.QueryDefaultRuleAdminCommand;
import com.everhomes.rest.techpark.rental.admin.QueryDefaultRuleAdminResponse;
import com.everhomes.rest.techpark.rental.admin.UpdateResourceAdminCommand;

/**
 * <ul>
 * 预约后台系统：
 * <li>后台维护某种场所的预约规则</li>
 * <li>维护具体场所</li>
 * <li>查询预约情况</li>
 * </ul>
 */
@RestDoc(value = "rental admin controller", site = "ehcore")
@RestController
@RequestMapping("/rental/admin")
public class RentalAdminController extends ControllerBase {

	/**
	 * 
	 * <b>URL: /rental/admin/addDefaultRule<b>
	 * <p>
	 * 添加默认规则
	 * </p>
	 */
	@RequestMapping("addDefaultRule")
	@RestReturn(String.class)
	public RestResponse addDefaultRule(@Valid AddDefaultRuleAdminCommand cmd) {

		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * 
	 * <b>URL: /rental/admin/queryDefaultRule<b>
	 * <p>
	 * 查询默认规则
	 * </p>
	 */
	@RequestMapping("queryDefaultRule")
	@RestReturn(QueryDefaultRuleAdminResponse.class)
	public RestResponse queryDefaultRule(@Valid QueryDefaultRuleAdminCommand cmd) {
		QueryDefaultRuleAdminResponse queryDefaultRuleAdminResponse = new QueryDefaultRuleAdminResponse();
		RestResponse response = new RestResponse(queryDefaultRuleAdminResponse);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	
	@RequestMapping("getResourceList")
	@RestReturn(GetResourceListAdminResponse.class)
	public RestResponse getResourceList(@Valid GetResourceListAdminCommand cmd){
		List<GetResourceListAdminResponse> list = new ArrayList<GetResourceListAdminResponse>();
		RestResponse response = new RestResponse(list);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	
	@RequestMapping("addResource")
	@RestReturn(String.class)
	public RestResponse addResource(@Valid AddResourceAdminCommand cmd){
		
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	
	@RequestMapping("updateResource")
	@RestReturn(String.class)
	public RestResponse updateResource(@Valid UpdateResourceAdminCommand cmd){

		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	
	
}
