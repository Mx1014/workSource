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
import com.everhomes.rest.approval.CreateForgetToPunchRequestBySceneCommand;
import com.everhomes.rest.approval.GetApprovalBasicInfoBySceneCommand;
import com.everhomes.rest.approval.GetApprovalBasicInfoBySceneResponse;
import com.everhomes.rest.approval.ListApprovalFlowBySceneCommand;
import com.everhomes.rest.approval.ListApprovalFlowBySceneResponse;
import com.everhomes.rest.approval.ListApprovalLogAndFlowBySceneCommand;
import com.everhomes.rest.approval.ListApprovalLogAndFlowBySceneResponse;
import com.everhomes.rest.approval.ListApprovalLogBySceneCommand;
import com.everhomes.rest.approval.ListApprovalLogBySceneResponse;
import com.everhomes.rest.approval.ListAskForLeaveRequestBySceneCommand;
import com.everhomes.rest.approval.ListAskForLeaveRequestBySceneResponse;

/**
 * <ul>
 * <li>客户端的审批相关api</li>
 * </ul>
 */
@RestDoc(value="ApprovalUi controller", site="approvalUi")
@RestController
@RequestMapping("/ui/approval")
public class ApprovalUiController extends ControllerBase {

	/**
	 * 
	 * <p>1.创建请假申请（客户端）</p>
	 * <b>URL: /ui/approval/createAskForLeaveRequestByScene</b>
	 */
	@RequestMapping("createAskForLeaveRequestByScene")
	@RestReturn(CreateAskForLeaveRequestResponse.class)
	public RestResponse createAskForLeaveRequestByScene(CreateAskForLeaveRequestCommand cmd){
		return new RestResponse();
	}

	/**
	 * 
	 * <p>2.请假申请列表（客户端）</p>
	 * <b>URL: /ui/approval/listAskForLeaveRequestByScene</b>
	 */
	@RequestMapping("listAskForLeaveRequestByScene")
	@RestReturn(ListAskForLeaveRequestBySceneResponse.class)
	public RestResponse listAskForLeaveRequestByScene(ListAskForLeaveRequestBySceneCommand cmd){
		return new RestResponse();
	}

	/**
	 * 
	 * <p>3.创建忘打卡申请（客户端）</p>
	 * <b>URL: /ui/approval/createForgetToPunchRequestByScene</b>
	 */
	@RequestMapping("createForgetToPunchRequestByScene")
	@RestReturn(String.class)
	public RestResponse createForgetToPunchRequestByScene(CreateForgetToPunchRequestBySceneCommand cmd){
		return new RestResponse();
	}

	/**
	 * 
	 * <p>4.获取审批基本信息（客户端）</p>
	 * <b>URL: /ui/approval/getApprovalBasicInfoByScene</b>
	 */
	@RequestMapping("getApprovalBasicInfoByScene")
	@RestReturn(GetApprovalBasicInfoBySceneResponse.class)
	public RestResponse getApprovalBasicInfoByScene(GetApprovalBasicInfoBySceneCommand cmd){
		return new RestResponse();
	}

	/**
	 * 
	 * <p>4.获取拼好的审批日志与审批流程列表（客户端）</p>
	 * <b>URL: /ui/approval/listApprovalLogAndFlowByScene</b>
	 */
	@RequestMapping("listApprovalLogAndFlowByScene")
	@RestReturn(ListApprovalLogAndFlowBySceneResponse.class)
	public RestResponse listApprovalLogAndFlowByScene(ListApprovalLogAndFlowBySceneCommand cmd){
		return new RestResponse();
	}

	/**
	 * 
	 * <p>4.获取单独的审批日志列表（客户端）</p>
	 * <b>URL: /ui/approval/listApprovalLogByScene</b>
	 */
	@RequestMapping("listApprovalLogByScene")
	@RestReturn(ListApprovalLogBySceneResponse.class)
	public RestResponse listApprovalLogByScene(ListApprovalLogBySceneCommand cmd){
		return new RestResponse();
	}

	/**
	 * 
	 * <p>4.获取单独的审批流程列表（客户端）</p>
	 * <b>URL: /ui/approval/listApprovalFlowByScene</b>
	 */
	@RequestMapping("listApprovalFlowByScene")
	@RestReturn(ListApprovalFlowBySceneResponse.class)
	public RestResponse listApprovalFlowByScene(ListApprovalFlowBySceneCommand cmd){
		return new RestResponse();
	}
	
}
