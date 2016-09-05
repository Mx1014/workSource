// @formatter:off
package com.everhomes.approval;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.approval.AgreeRequestCommand;
import com.everhomes.rest.approval.CreateApprovalFlowLevelCommand;
import com.everhomes.rest.approval.CreateApprovalFlowLevelResponse;
import com.everhomes.rest.approval.CreateApprovalFlowNameCommand;
import com.everhomes.rest.approval.CreateApprovalFlowNameResponse;
import com.everhomes.rest.approval.CreateApprovalRuleCommand;
import com.everhomes.rest.approval.CreateApprovalRuleResponse;
import com.everhomes.rest.approval.CreateApprovalCategoryCommand;
import com.everhomes.rest.approval.CreateApprovalCategoryResponse;
import com.everhomes.rest.approval.DeleteApprovalFlowCommand;
import com.everhomes.rest.approval.DeleteApprovalRuleCommand;
import com.everhomes.rest.approval.DeleteApprovalCategoryCommand;
import com.everhomes.rest.approval.GetApprovalBasicInfoOfRequestCommand;
import com.everhomes.rest.approval.GetApprovalBasicInfoOfRequestResponse;
import com.everhomes.rest.approval.ListApprovalFlowCommand;
import com.everhomes.rest.approval.ListApprovalFlowOfRequestCommand;
import com.everhomes.rest.approval.ListApprovalFlowOfRequestResponse;
import com.everhomes.rest.approval.ListApprovalFlowResponse;
import com.everhomes.rest.approval.ListApprovalLogAndFlowOfRequestCommand;
import com.everhomes.rest.approval.ListApprovalLogAndFlowOfRequestResponse;
import com.everhomes.rest.approval.ListApprovalLogOfRequestCommand;
import com.everhomes.rest.approval.ListApprovalLogOfRequestResponse;
import com.everhomes.rest.approval.ListApprovalRuleCommand;
import com.everhomes.rest.approval.ListApprovalRuleResponse;
import com.everhomes.rest.approval.ListApprovalCategoryCommand;
import com.everhomes.rest.approval.ListApprovalCategoryResponse;
import com.everhomes.rest.approval.ListAskForLeaveCommand;
import com.everhomes.rest.approval.ListAskForLeaveResponse;
import com.everhomes.rest.approval.ListBriefApprovalFlowCommand;
import com.everhomes.rest.approval.ListBriefApprovalFlowResponse;
import com.everhomes.rest.approval.ListBriefApprovalRuleCommand;
import com.everhomes.rest.approval.ListBriefApprovalRuleResponse;
import com.everhomes.rest.approval.ListForgetToPunchCommand;
import com.everhomes.rest.approval.ListForgetToPunchResponse;
import com.everhomes.rest.approval.RejectRequestCommand;
import com.everhomes.rest.approval.UpdateApprovalFlowLevelCommand;
import com.everhomes.rest.approval.UpdateApprovalFlowLevelResponse;
import com.everhomes.rest.approval.UpdateApprovalFlowNameCommand;
import com.everhomes.rest.approval.UpdateApprovalFlowNameResponse;
import com.everhomes.rest.approval.UpdateApprovalRuleCommand;
import com.everhomes.rest.approval.UpdateApprovalRuleResponse;
import com.everhomes.rest.approval.UpdateApprovalCategoryCommand;
import com.everhomes.rest.approval.UpdateApprovalCategoryResponse;

@RestDoc("approval controller")
@RestController
@RequestMapping("/approval")
public class ApprovalController extends ControllerBase {
	
	@Autowired
	private ApprovalService approvalService;

	/**
	 * 
	 * <p>1.增加审批类型的具体类型，如请假的公出、事假等</p>
	 * <b>URL: /approval/createApprovalCategory</b>
	 */
	@RequestMapping("createApprovalCategory")
	@RestReturn(CreateApprovalCategoryResponse.class)
	public RestResponse createApprovalCategory(CreateApprovalCategoryCommand cmd){
		return new RestResponse(approvalService.createApprovalCategory(cmd));
	}
	
	/**
	 * 
	 * <p>2.更新审批类型的具体类型</p>
	 * <b>URL: /approval/updateApprovalCategory</b>
	 */
	@RequestMapping("updateApprovalCategory")
	@RestReturn(UpdateApprovalCategoryResponse.class)
	public RestResponse updateApprovalCategory(UpdateApprovalCategoryCommand cmd){
		return new RestResponse(approvalService.updateApprovalCategory(cmd));
	}

	/**
	 * 
	 * <p>3.列出审批类型的具体类型</p>
	 * <b>URL: /approval/listApprovalCategory</b>
	 */
	@RequestMapping("listApprovalCategory")
	@RestReturn(ListApprovalCategoryResponse.class)
	public RestResponse listApprovalCategory(ListApprovalCategoryCommand cmd){
		return new RestResponse(approvalService.listApprovalCategory(cmd));
	}

	/**
	 * 
	 * <p>4.删除审批类型的具体类型</p>
	 * <b>URL: /approval/deleteApprovalCategory</b>
	 */
	@RequestMapping("deleteApprovalCategory")
	@RestReturn(String.class)
	public RestResponse deleteApprovalCategory(DeleteApprovalCategoryCommand cmd){
		approvalService.deleteApprovalCategory(cmd);
		return new RestResponse();
	}
	
	/**
	 * 
	 * <p>5.设置审批流程名称</p>
	 * <b>URL: /approval/createApprovalFlowName</b>
	 */
	@RequestMapping("createApprovalFlowName")
	@RestReturn(CreateApprovalFlowNameResponse.class)
	public RestResponse createApprovalFlowName(CreateApprovalFlowNameCommand cmd){
		return new RestResponse(approvalService.createApprovalFlowName(cmd));
	}
	
	/**
	 * 
	 * <p>6.更新审批流程名称</p>
	 * <b>URL: /approval/updateApprovalFlowName</b>
	 */
	@RequestMapping("updateApprovalFlowName")
	@RestReturn(UpdateApprovalFlowNameResponse.class)
	public RestResponse updateApprovalFlowName(UpdateApprovalFlowNameCommand cmd){
		return new RestResponse(approvalService.updateApprovalFlowName(cmd));
	}

	/**
	 * 
	 * <p>7.设置审批流程级别</p>
	 * <b>URL: /approval/createApprovalFlowLevel</b>
	 */
	@RequestMapping("createApprovalFlowLevel")
	@RestReturn(CreateApprovalFlowLevelResponse.class)
	public RestResponse createApprovalFlowLevel(CreateApprovalFlowLevelCommand cmd){
		return new RestResponse(approvalService.createApprovalFlowLevel(cmd));
	}

	/**
	 * 
	 * <p>8.更新审批流程级别</p>
	 * <b>URL: /approval/updateApprovalFlowLevel</b>
	 */
	@RequestMapping("updateApprovalFlowLevel")
	@RestReturn(UpdateApprovalFlowLevelResponse.class)
	public RestResponse updateApprovalFlowLevel(UpdateApprovalFlowLevelCommand cmd){
		return new RestResponse(approvalService.updateApprovalFlowLevel(cmd));
	}

	/**
	 * 
	 * <p>9.审批流程列表</p>
	 * <b>URL: /approval/listApprovalFlow</b>
	 */
	@RequestMapping("listApprovalFlow")
	@RestReturn(ListApprovalFlowResponse.class)
	public RestResponse listApprovalFlow(ListApprovalFlowCommand cmd){
		return new RestResponse(approvalService.listApprovalFlow(cmd));
	}

	/**
	 * 
	 * <p>10.审批流程简短列表</p>
	 * <b>URL: /approval/listBriefApprovalFlow</b>
	 */
	@RequestMapping("listBriefApprovalFlow")
	@RestReturn(ListBriefApprovalFlowResponse.class)
	public RestResponse listBriefApprovalFlow(ListBriefApprovalFlowCommand cmd){
		return new RestResponse();
	}
	
	/**
	 * 
	 * <p>11.删除审批流程</p>
	 * <b>URL: /approval/deleteApprovalFlow</b>
	 */
	@RequestMapping("deleteApprovalFlow")
	@RestReturn(String.class)
	public RestResponse deleteApprovalFlow(DeleteApprovalFlowCommand cmd){
		approvalService.deleteApprovalFlow(cmd);
		return new RestResponse();
	}
	
	/**
	 * 
	 * <p>12.增加审批规则</p>
	 * <b>URL: /approval/createApprovalRule</b>
	 */
	@RequestMapping("createApprovalRule")
	@RestReturn(CreateApprovalRuleResponse.class)
	public RestResponse createApprovalRule(CreateApprovalRuleCommand cmd){
		return new RestResponse(approvalService.createApprovalRule(cmd));
	}

	/**
	 * 
	 * <p>13.更新审批规则</p>
	 * <b>URL: /approval/updateApprovalRule</b>
	 */
	@RequestMapping("updateApprovalRule")
	@RestReturn(UpdateApprovalRuleResponse.class)
	public RestResponse updateApprovalRule(UpdateApprovalRuleCommand cmd){
		return new RestResponse(approvalService.updateApprovalRule(cmd));
	}

	/**
	 * 
	 * <p>14.删除审批规则</p>
	 * <b>URL: /approval/deleteApprovalRule</b>
	 */
	@RequestMapping("deleteApprovalRule")
	@RestReturn(String.class)
	public RestResponse deleteApprovalRule(DeleteApprovalRuleCommand cmd){
		approvalService.deleteApprovalRule(cmd);
		return new RestResponse();
	}
	
	/**
	 * 
	 * <p>15.审批规则列表</p>
	 * <b>URL: /approval/listApprovalRule</b>
	 */
	@RequestMapping("listApprovalRule")
	@RestReturn(ListApprovalRuleResponse.class)
	public RestResponse listApprovalRule(ListApprovalRuleCommand cmd){
		return new RestResponse(approvalService.listApprovalRule(cmd));
	}

	/**
	 * 
	 * <p>16.审批规则简短列表</p>
	 * <b>URL: /approval/listBriefApprovalRule</b>
	 */
	@RequestMapping("listBriefApprovalRule")
	@RestReturn(ListBriefApprovalRuleResponse.class)
	public RestResponse listBriefApprovalRule(ListBriefApprovalRuleCommand cmd){
		return new RestResponse();
	}

	/**
	 * 
	 * <p>17.忘打卡申请列表</p>
	 * <b>URL: /approval/listForgetToPunch</b>
	 */
	@RequestMapping("listForgetToPunch")
	@RestReturn(ListForgetToPunchResponse.class)
	public RestResponse listForgetToPunch(ListForgetToPunchCommand cmd){
		return new RestResponse();
	}

	/**
	 * 
	 * <p>18.请假申请列表</p>
	 * <b>URL: /approval/listAskForLeave</b>
	 */
	@RequestMapping("listAskForLeave")
	@RestReturn(ListAskForLeaveResponse.class)
	public RestResponse listAskForLeave(ListAskForLeaveCommand cmd){
		return new RestResponse();
	}

	/**
	 * 
	 * <p>19.同意申请</p>
	 * <b>URL: /approval/agreeRequest</b>
	 */
	@RequestMapping("agreeRequest")
	@RestReturn(String.class)
	public RestResponse agreeRequest(AgreeRequestCommand cmd){
		return new RestResponse();
	}

	/**
	 * 
	 * <p>20.驳回申请</p>
	 * <b>URL: /approval/rejectRequest</b>
	 */
	@RequestMapping("rejectRequest")
	@RestReturn(String.class)
	public RestResponse rejectRequest(RejectRequestCommand cmd){
		return new RestResponse();
	}
	
	/**
	 * 
	 * <p>21.获取申请的审批基本信息</p>
	 * <b>URL: /approval/getApprovalBasicInfoOfRequest</b>
	 */
	@RequestMapping("getApprovalBasicInfoOfRequest")
	@RestReturn(GetApprovalBasicInfoOfRequestResponse.class)
	public RestResponse getApprovalBasicInfoOfRequest(GetApprovalBasicInfoOfRequestCommand cmd){
		return new RestResponse();
	}

	/**
	 * 
	 * <p>22.获取申请的审批日志与审批流程列表</p>
	 * <b>URL: /approval/listApprovalLogAndFlowOfRequest</b>
	 */
	@RequestMapping("listApprovalLogAndFlowOfRequest")
	@RestReturn(ListApprovalLogAndFlowOfRequestResponse.class)
	public RestResponse listApprovalLogAndFlowOfRequest(ListApprovalLogAndFlowOfRequestCommand cmd){
		return new RestResponse();
	}

	/**
	 * 
	 * <p>23.获取申请的审批日志列表</p>
	 * <b>URL: /approval/listApprovalLogOfRequest</b>
	 */
	@RequestMapping("listApprovalLogOfRequest")
	@RestReturn(ListApprovalLogOfRequestResponse.class)
	public RestResponse listApprovalLogOfRequest(ListApprovalLogOfRequestCommand cmd){
		return new RestResponse();
	}

	/**
	 * 
	 * <p>24.获取申请的审批流程列表</p>
	 * <b>URL: /approval/listApprovalFlowOfRequest</b>
	 */
	@RequestMapping("listApprovalFlowOfRequest")
	@RestReturn(ListApprovalFlowOfRequestResponse.class)
	public RestResponse listApprovalFlowOfRequest(ListApprovalFlowOfRequestCommand cmd){
		return new RestResponse();
	}
}
