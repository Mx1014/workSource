package com.everhomes.openapi;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.everhomes.app.AppProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.discover.SuppressDiscover;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.openapi.GetOrgCheckInDataCommand;
import com.everhomes.rest.openapi.GetOrgCheckInDataResponse;
import com.everhomes.rest.openapi.techpark.CustomerResponse;
import com.everhomes.rest.openapi.techpark.SyncDataCommand;
import com.everhomes.techpark.punch.PunchService;
import com.everhomes.user.UserActivityService;
import com.everhomes.user.UserService;
import com.everhomes.util.RequireAuthentication;
import com.fasterxml.jackson.annotation.JsonFormat.Value;

@RestDoc(value="Synch Info Controller", site="core")
@RestController
@RequestMapping("/openapi")
public class PunchOpenController extends ControllerBase {
	private static final Logger LOGGER = LoggerFactory.getLogger(PunchOpenController.class);

	@Autowired
	private PunchService punchService;

	/**
	 * <b>URL: /openapi/getOrgCheckInData</b>
	 * <p>同步数据</p>
	 */
	@SuppressDiscover
	@RequireAuthentication(false)
	@RequestMapping("getOrgCheckInData")
	@RestReturn(value = GetOrgCheckInDataResponse.class)
	public RestResponse getOrgCheckInData(@Valid GetOrgCheckInDataCommand cmd,HttpServletRequest request, HttpServletResponse response) {  
		return new RestResponse(punchService.getOrgCheckInData(cmd));
	}
}
