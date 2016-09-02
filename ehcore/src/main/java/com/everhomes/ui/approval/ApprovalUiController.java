// @formatter:off
package com.everhomes.ui.approval;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.approval.CreateAskForLeaveRequestCommand;
import com.everhomes.rest.approval.CreateAskForLeaveRequestResponse;

/**
 * <ul>
 * <li>客户端的新闻相关api</li>
 * </ul>
 */
@RestDoc(value="ApprovalUi controller", site="approvalUi")
@RestController
@RequestMapping("/ui/approval")
public class ApprovalUiController extends ControllerBase {

	/**
	 * 
	 * <p>创建请假申请</p>
	 * <b>URL: /ui/approval/createAskForLeaveRequest</b>
	 */
	@RequestMapping("createAskForLeaveRequest")
	@RestReturn(CreateAskForLeaveRequestResponse.class)
	public RestResponse createAskForLeaveRequestByScene(CreateAskForLeaveRequestCommand cmd){
		return new RestResponse();
	}
	
	
	
	
	
}
