package com.everhomes.servicehotline;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.servicehotline.AddHotlineCommand;
import com.everhomes.rest.servicehotline.DeleteHotlineCommand;
import com.everhomes.rest.servicehotline.GetHotlineListCommand;
import com.everhomes.rest.servicehotline.GetHotlineListResponse;
import com.everhomes.rest.servicehotline.GetHotlineSubjectCommand;
import com.everhomes.rest.servicehotline.GetHotlineSubjectResponse;
import com.everhomes.rest.servicehotline.SetHotlineSubjectCommand;
import com.everhomes.rest.servicehotline.UpdateHotlineCommand;
import com.everhomes.rest.servicehotline.UpdateHotlinesCommand;
import com.everhomes.techpark.servicehotline.HotlineService;
import com.everhomes.util.RequireAuthentication;

@RestController
@RequestMapping("/hotline")
public class ServiceHotlineController extends ControllerBase {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(ServiceHotlineController.class);

	private static final String DEFAULT_SORT = "default_order";

	@Autowired
	private HotlineService hotlineService;
 

	/**
	 * <b>URL: /hotline/getHotlineTopic</b>
	 * <p>
	 * 获取热线顶部标题
	 * </p>
	 */
	@RequireAuthentication(false)
	@RequestMapping("getHotlineSubject")
	@RestReturn(value = GetHotlineSubjectResponse.class)
	public RestResponse getHotlineSubject(@Valid GetHotlineSubjectCommand cmd) {
		GetHotlineSubjectResponse res = hotlineService.getHotlineSubject(cmd);
		RestResponse response = new RestResponse(res);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /hotline/getHotlineList</b>
	 * <p>
	 * 获取热线列表
	 * </p>
	 */
	@RequireAuthentication(false)
	@RequestMapping("getHotlineList")
	@RestReturn(value = GetHotlineListResponse.class)
	public RestResponse getHotlineList(@Valid GetHotlineListCommand cmd) {
		GetHotlineListResponse res =hotlineService.getHotlineList(cmd);
		RestResponse response = new RestResponse(res);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /hotline/addHotline</b>
	 * <p>
	 * 添加热线
	 * </p>
	 */
	@RequestMapping("addHotline")
	@RestReturn(value = String.class)
	public RestResponse addHotline(@Valid AddHotlineCommand cmd) {
		this.hotlineService.addHotline(cmd);
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /hotline/deleteHotline</b>
	 * <p>
	 * 删除热线
	 * </p>
	 */
	@RequestMapping("deleteHotline")
	@RestReturn(value = String.class)
	public RestResponse deleteHotline(@Valid DeleteHotlineCommand cmd) {
		this.hotlineService.deleteHotline(cmd);
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /hotline/updateHotline</b>
	 * <p>
	 * 更新热线
	 * </p>
	 */
	@RequestMapping("updateHotline")
	@RestReturn(value = String.class)
	public RestResponse updateHotline(@Valid UpdateHotlineCommand cmd) {
		this.hotlineService.updateHotline(cmd);
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /hotline/updateHotlines</b>
	 * <p>
	 * 批量更新热线
	 * </p>
	 */
	@RequestMapping("updateHotlines")
	@RestReturn(value = String.class)
	public RestResponse updateHotlines(@Valid UpdateHotlinesCommand cmd) {
		this.hotlineService.updateHotlines(cmd);
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}  
	

	/**
	 * <b>URL: /hotline/updateHotlineOrder</b>
	 * <p>
	 * 排序
	 * </p>
	 */
	@RequestMapping("updateHotlineOrder")
	@RestReturn(value = String.class)
	public RestResponse updateHotlineOrder(@Valid UpdateHotlinesCommand cmd) {
		this.hotlineService.updateHotlineOrder(cmd);
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}  
	/**
	 * <b>URL: /hotline/setHotlineSubject</b>
	 * <p>
	 * 功能开关 
	 * </p>
	 */
	@RequestMapping("setHotlineSubject")
	@RestReturn(value = String.class)
	public RestResponse setHotlineSubject(@Valid SetHotlineSubjectCommand cmd) {
		this.hotlineService.setHotlineSubject(cmd);
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
}
