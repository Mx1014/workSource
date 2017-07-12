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
import com.everhomes.rest.openapi.techpark.CustomerResponse;
import com.everhomes.rest.openapi.techpark.SyncDataCommand;
import com.everhomes.user.UserActivityService;
import com.everhomes.user.UserService;
import com.everhomes.util.RequireAuthentication;

@RestDoc(value="Synch Info Controller", site="synchInfo")
@RestController
@RequestMapping("/openapi/techpark")
public class TechparkOpenController extends ControllerBase {
	private static final Logger LOGGER = LoggerFactory.getLogger(TechparkOpenController.class);

	@Autowired
	private UserService userService;
	@Autowired
	private AppProvider appProvider;
	@Autowired
	private UserActivityService userActivityService;
	@Autowired
	private TechparkOpenService techparkOpenService;


	/**
	 * <b>URL: /openapi/techpark/syncData</b>
	 * <p>同步数据</p>
	 */
	@SuppressDiscover
	@RequireAuthentication(false)
	@RequestMapping("syncData")
	@RestReturn(String.class)
	public CustomerResponse initBizInfo(@Valid SyncDataCommand cmd,HttpServletRequest request, HttpServletResponse response) {
		LOGGER.debug("Sync techpark data, cmd={}", cmd);
		
		techparkOpenService.syncData(cmd);
		
		return new CustomerResponse(cmd.getSyncState(), cmd.getDataType());
	}
}
