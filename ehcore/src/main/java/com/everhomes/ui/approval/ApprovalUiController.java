// @formatter:off
package com.everhomes.ui.approval;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.everhomes.approval.ApprovalService;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.approval.ApproveApprovalRequesBySceneCommand;
import com.everhomes.rest.approval.CancelApprovalRequestBySceneCommand;
import com.everhomes.rest.approval.CreateApprovalRequestBySceneCommand;
import com.everhomes.rest.approval.CreateApprovalRequestBySceneResponse;
import com.everhomes.rest.approval.GetApprovalBasicInfoOfRequestBySceneCommand;
import com.everhomes.rest.approval.GetApprovalBasicInfoOfRequestBySceneResponse;
import com.everhomes.rest.approval.ListApprovalCategoryBySceneCommand;
import com.everhomes.rest.approval.ListApprovalCategoryBySceneResponse;
import com.everhomes.rest.approval.ListApprovalFlowOfRequestBySceneCommand;
import com.everhomes.rest.approval.ListApprovalFlowOfRequestBySceneResponse;
import com.everhomes.rest.approval.ListApprovalLogAndFlowOfRequestBySceneCommand;
import com.everhomes.rest.approval.ListApprovalLogAndFlowOfRequestBySceneResponse;
import com.everhomes.rest.approval.ListApprovalLogOfRequestBySceneCommand;
import com.everhomes.rest.approval.ListApprovalLogOfRequestBySceneResponse;
import com.everhomes.rest.approval.ListApprovalRequestBySceneCommand;
import com.everhomes.rest.approval.ListApprovalRequestBySceneResponse;
import com.everhomes.rest.approval.ListMyApprovalsBySceneCommand;
import com.everhomes.rest.approval.RejectApprovalRequestBySceneCommand;

/**
 * <ul>
 * <li>客户端的审批相关api</li>
 * </ul>
 */
@RestDoc(value="ApprovalUi controller", site="approvalUi")
@RestController
@RequestMapping("/ui/approval")
public class ApprovalUiController extends ControllerBase {

	@Autowired
	private ApprovalService approvalService;
	
	/**
	 * 
	 * <p>1.个人申请列表（客户端）</p>
	 * <b>URL: /ui/approval/listApprovalRequestByScene</b>
	 */
	@RequestMapping("listApprovalRequestByScene")
	@RestReturn(ListApprovalRequestBySceneResponse.class)
	public RestResponse listApprovalRequestByScene(ListApprovalRequestBySceneCommand cmd){
		return new RestResponse(approvalService.listApprovalRequestByScene(cmd));
	}

	/**
	 * 
	 * <p>2.获取申请的审批基本信息（客户端）</p>
	 * <b>URL: /ui/approval/getApprovalBasicInfoOfRequestByScene</b>
	 */
	@RequestMapping("getApprovalBasicInfoOfRequestByScene")
	@RestReturn(GetApprovalBasicInfoOfRequestBySceneResponse.class)
	public RestResponse getApprovalBasicInfoOfRequestByScene(GetApprovalBasicInfoOfRequestBySceneCommand cmd){
		return new RestResponse(approvalService.getApprovalBasicInfoOfRequestByScene(cmd));
	}

	/**
	 * 
	 * <p>3.获取申请的审批日志与审批流程列表（客户端）</p>
	 * <b>URL: /ui/approval/listApprovalLogAndFlowOfRequestByScene</b>
	 */
	@RequestMapping("listApprovalLogAndFlowOfRequestByScene")
	@RestReturn(ListApprovalLogAndFlowOfRequestBySceneResponse.class)
	public RestResponse listApprovalLogAndFlowOfRequestByScene(ListApprovalLogAndFlowOfRequestBySceneCommand cmd){
		return new RestResponse(approvalService.listApprovalLogAndFlowOfRequestByScene(cmd));
	}

	/**
	 * 未用
	 * <p>4.获取申请的审批日志列表（客户端）</p>
	 * <b>URL: /ui/approval/listApprovalLogOfRequestByScene</b>
	 */
	@RequestMapping("listApprovalLogOfRequestByScene")
	@RestReturn(ListApprovalLogOfRequestBySceneResponse.class)
	public RestResponse listApprovalLogOfRequestByScene(ListApprovalLogOfRequestBySceneCommand cmd){
		return new RestResponse(approvalService.listApprovalLogOfRequestByScene(cmd));
	}

	/**
	 * 未用
	 * <p>5.获取申请的审批流程列表（客户端）</p>
	 * <b>URL: /ui/approval/listApprovalFlowOfRequestByScene</b>
	 */
	@RequestMapping("listApprovalFlowOfRequestByScene")
	@RestReturn(ListApprovalFlowOfRequestBySceneResponse.class)
	public RestResponse listApprovalFlowOfRequestByScene(ListApprovalFlowOfRequestBySceneCommand cmd){
		return new RestResponse(approvalService.listApprovalFlowOfRequestByScene(cmd));
	}

	/**
	 * 
	 * <p>6.创建申请（客户端）</p>
	 * <b>URL: /ui/approval/createApprovalRequestByScene</b>
	 */
	@RequestMapping("createApprovalRequestByScene")
	@RestReturn(CreateApprovalRequestBySceneResponse.class)
	public RestResponse createApprovalRequestByScene(CreateApprovalRequestBySceneCommand cmd){
		return new RestResponse(approvalService.createApprovalRequestByScene(cmd));
	}

	/**
	 * 
	 * <p>7.取消申请（客户端）</p>
	 * <b>URL: /ui/approval/cancelApprovalRequestByScene</b>
	 */
	@RequestMapping("cancelApprovalRequestByScene")
	@RestReturn(String.class)
	public RestResponse cancelApprovalRequestByScene(CancelApprovalRequestBySceneCommand cmd) {
		approvalService.cancelApprovalRequestByScene(cmd);
		return new RestResponse();
	}
	
	/**
	 * 
	 * <p>8.列出审批类别（客户端）</p>
	 * <b>URL: /ui/approval/listApprovalCategoryByScene</b>
	 */
	@RequestMapping("listApprovalCategoryByScene")
	@RestReturn(ListApprovalCategoryBySceneResponse.class)
	public RestResponse listApprovalCategoryByScene(ListApprovalCategoryBySceneCommand cmd){
		return new RestResponse(approvalService.listApprovalCategoryByScene(cmd));
	}
	
	/**
	 * 
	 * <p>9.查询我的审批列表</p>
	 * <b>URL: /ui/approval/listMyApprovalsByScene</b>
	 */
	@RequestMapping("listMyApprovalsByScene")
	@RestReturn(ListApprovalRequestBySceneResponse.class)
	public RestResponse listMyApprovalsByScene(ListMyApprovalsBySceneCommand cmd){
		return new RestResponse(approvalService.listMyApprovalsByScene(cmd));
	}
		
	

	/**
	 * 
	 * <p>10.同意申请</p>
	 * <b>URL: /ui/approval/approveApprovalRequestByScene</b>
	 */
	@RequestMapping("approveApprovalRequestByScene")
	@RestReturn(String.class)
	public RestResponse approveApprovalRequestByScene(ApproveApprovalRequesBySceneCommand cmd){
		approvalService.approveApprovalRequest(cmd);
		return new RestResponse();
	}

	/**
	 * 
	 * <p>11.驳回申请</p>
	 * <b>URL: /ui/approval/rejectApprovalRequestByScene</b>
	 */
	@RequestMapping("rejectApprovalRequestByScene")
	@RestReturn(String.class)
	public RestResponse rejectApprovalRequestByScene(RejectApprovalRequestBySceneCommand cmd){
		approvalService.rejectApprovalRequest(cmd);
		return new RestResponse();
	}


}
