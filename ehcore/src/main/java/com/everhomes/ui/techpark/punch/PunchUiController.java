// @formatter:off
package com.everhomes.ui.techpark.punch;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.approval.CreateAbsenceRequestBySceneCommand;
import com.everhomes.rest.approval.CreateAbsenceRequestBySceneResponse;
import com.everhomes.rest.approval.CreateForgotRequestBySceneCommand;
import com.everhomes.rest.approval.CreateForgotRequestBySceneResponse;

@RestDoc(value = "Punch ui controller", site = "ehcore")
@RestController
@RequestMapping("/ui/techpark/punch")
public class PunchUiController extends ControllerBase {

	/**
	 * 
	 * <p>1.创建请假申请（客户端）</p>
	 * <b>URL: /ui/techpark/punch/createAbsenceRequestByScene</b>
	 */
	@RequestMapping("createAbsenceRequestByScene")
	@RestReturn(CreateAbsenceRequestBySceneResponse.class)
	public RestResponse createAbsenceRequestByScene(CreateAbsenceRequestBySceneCommand cmd){
		return new RestResponse();
	}

	/**
	 * 
	 * <p>2.创建忘打卡申请（客户端）</p>
	 * <b>URL: /ui/techpark/punch/createForgotRequestByScene</b>
	 */
	@RequestMapping("createForgotRequestByScene")
	@RestReturn(CreateForgotRequestBySceneResponse.class)
	public RestResponse createForgotRequestByScene(CreateForgotRequestBySceneCommand cmd){
		return new RestResponse();
	}

}
