package com.everhomes.cooperation;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.cooperation.NewCooperationCommand;
import com.everhomes.util.RequireAuthentication;

@RestController
@RequestMapping("/org")
public class CooperationController extends ControllerBase {

	@Autowired
	CooperationService cooperationService;

	/**
	 * <b>URL: /org/newCooperation</b>
	 * <p>
	 * 前台页面新增合作机构申请
	 * </p>
	 * 
	 * @return 添加的结果
	 */
	@RequireAuthentication(false)
	@RequestMapping("newCooperation")
	@RestReturn(value = String.class)
	public RestResponse newCooperation(@Valid NewCooperationCommand cmd) {
		cooperationService.newCooperation(cmd);
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	} 

}
