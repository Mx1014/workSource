package com.everhomes.techpark.punch;

import java.text.SimpleDateFormat;
import java.util.Date;

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
import com.everhomes.group.GroupDTO;
import com.everhomes.rest.RestResponse;
import com.everhomes.user.UserContext;

/**
 * <ul>
 * 打卡管理：
 * <li>先调用查询是否可以打卡的接口，返回给客户端</li>
 * <li>可以打卡后，客户端调用打卡接口，进行打卡，数据库新增打卡记录</li>
 * <li>返回打卡记录list</li>
 * </ul>
 */
@RestDoc(value = "Punch controller", site = "ehccore")
@RestController
@RequestMapping("/techpark/punch")
public class PunchController extends ControllerBase {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(PunchController.class);

	@Autowired
	private PunchService punchService;

	//
	// /**
	// * <b>URL: /techpark/punch/getPunchLocation</b>
	// * <p>
	// * 根据请求 user和company的ID 确定用户办公楼栋坐标
	// * </p>
	// */
	// z @RequestMapping("getPunchLocation")
	// @RestReturn(value = GroupDTO.class)
	// public RestResponse getPunchLocation(@Valid GetPunchLocationCommand cmd)
	// {
	// // LocationDTO locationDto = this.(cmd);
	// LocationDTO locationDTO = new LocationDTO();
	// locationDTO.setLatitude(39.9291);
	// locationDTO.setLongitude(116.597);
	// RestResponse response = new RestResponse(locationDTO);
	// response.setErrorCode(ErrorCodes.SUCCESS);
	// response.setErrorDescription("OK");
	// return response;
	// }

	/**
	 * <b>URL: /techpark/punch/verifyPunchLocation</b>
	 * <p>
	 * 根据请求的坐标,和公司ID计算距离是否在设置距离内确定他是否能打卡
	 * </p>
	 */
	@RequestMapping("verifyPunchLocation")
	@RestReturn(value = String.class)
	public RestResponse verifyPunchLocation(
			@Valid VerifyPunchLocationCommand cmd) {
		// LocationDTO locationDto = this.(cmd);
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /techpark/punch/punchClock</b>
	 * <p>
	 * 根据请求 user和company的ID完成打卡功能
	 * </p>
	 */
	@RequestMapping("punchClock")
	@RestReturn(value = String.class)
	public RestResponse punchClock(@Valid PunchClockCommand cmd) {
		// 打卡返回打卡时间
		RestResponse response = new RestResponse(
				punchService.createPunchLog(cmd));
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /techpark/punch/listYearPunchLogs</b>
	 * <p>
	 * 根据请求 companyid和日期 取一年的打卡记录
	 * </p>
	 */
	@RequestMapping("listYearPunchLogs")
	@RestReturn(value = PunchLogsYearListResponse.class, collection = true)
	public RestResponse listPunchLogs(@Valid ListPunchLogsCommand cmd) {

		RestResponse res = new RestResponse();
		PunchLogsYearListResponse punchLogsYearListResponse = punchService
				.getlistPunchLogs(cmd);
		res.setErrorCode(ErrorCodes.SUCCESS);
		res.setErrorDescription("OK");
		res.setResponseObject(punchLogsYearListResponse);
		return res;
	}
}
