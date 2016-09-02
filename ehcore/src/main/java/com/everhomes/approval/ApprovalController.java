// @formatter:off
package com.everhomes.approval;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.approval.CreateAskForLeaveCategoryCommand;
import com.everhomes.rest.approval.CreateAskForLeaveCategoryResponse;
import com.everhomes.rest.approval.CreateApprovalFlowLevelCommand;
import com.everhomes.rest.approval.CreateApprovalFlowLevelResponse;
import com.everhomes.rest.approval.CreateApprovalFlowNameCommand;
import com.everhomes.rest.approval.CreateApprovalFlowNameResponse;
import com.everhomes.rest.approval.CreateApprovalRuleCommand;
import com.everhomes.rest.approval.CreateApprovalRuleResponse;
import com.everhomes.rest.approval.DeleteAskForLeaveCategoryCommand;
import com.everhomes.rest.approval.DeleteApprovalFlowCommand;
import com.everhomes.rest.approval.DeleteApprovalRuleCommand;
import com.everhomes.rest.approval.ListAskForLeaveCategoryCommand;
import com.everhomes.rest.approval.ListAskForLeaveCategoryResponse;
import com.everhomes.rest.approval.ListApprovalFlowCommand;
import com.everhomes.rest.approval.ListApprovalFlowResponse;
import com.everhomes.rest.approval.ListApprovalRuleCommand;
import com.everhomes.rest.approval.ListApprovalRuleResponse;
import com.everhomes.rest.approval.ListBriefApprovalFlowCommand;
import com.everhomes.rest.approval.ListBriefApprovalFlowResponse;
import com.everhomes.rest.approval.ListBriefApprovalRuleCommand;
import com.everhomes.rest.approval.ListBriefApprovalRuleResponse;
import com.everhomes.rest.approval.UpdateAskForLeaveCategoryCommand;
import com.everhomes.rest.approval.UpdateAskForLeaveCategoryResponse;
import com.everhomes.rest.approval.UpdateApprovalFlowLevelCommand;
import com.everhomes.rest.approval.UpdateApprovalFlowLevelResponse;
import com.everhomes.rest.approval.UpdateApprovalFlowNameCommand;
import com.everhomes.rest.approval.UpdateApprovalFlowNameResponse;
import com.everhomes.rest.approval.UpdateApprovalRuleCommand;
import com.everhomes.rest.approval.UpdateApprovalRuleResponse;

@RestDoc("approval controller")
@RestController
@RequestMapping("/approval")
public class ApprovalController extends ControllerBase {
	
	@Autowired
	private ApprovalService approvalService;

	/**
	 * 
	 * <p>增加请假类型</p>
	 * <b>URL: /approval/createAskForLeaveCategory</b>
	 */
	@RequestMapping("createAskForLeaveCategory")
	@RestReturn(CreateAskForLeaveCategoryResponse.class)
	public RestResponse createAskForLeaveCategory(CreateAskForLeaveCategoryCommand cmd){
		return new RestResponse(approvalService.createAskForLeaveCategory(cmd));
	}
	
	/**
	 * 
	 * <p>更新请假类型</p>
	 * <b>URL: /approval/updateAskForLeaveCategory</b>
	 */
	@RequestMapping("updateAskForLeaveCategory")
	@RestReturn(UpdateAskForLeaveCategoryResponse.class)
	public RestResponse updateAskForLeaveCategory(UpdateAskForLeaveCategoryCommand cmd){
		return new RestResponse(approvalService.updateAskForLeaveCategory(cmd));
	}

	/**
	 * 
	 * <p>列出请假类型</p>
	 * <b>URL: /approval/listAskForLeaveCategory</b>
	 */
	@RequestMapping("listAskForLeaveCategory")
	@RestReturn(ListAskForLeaveCategoryResponse.class)
	public RestResponse listAskForLeaveCategory(ListAskForLeaveCategoryCommand cmd){
		return new RestResponse(approvalService.listAskForLeaveCategory(cmd));
	}

	/**
	 * 
	 * <p>删除请假类型</p>
	 * <b>URL: /approval/deleteAskForLeaveCategory</b>
	 */
	@RequestMapping("deleteAskForLeaveCategory")
	@RestReturn(String.class)
	public RestResponse deleteAskForLeaveCategory(DeleteAskForLeaveCategoryCommand cmd){
		approvalService.deleteAskForLeaveCategory(cmd);
		return new RestResponse();
	}
	
	/**
	 * 
	 * <p>设置审批流程名称</p>
	 * <b>URL: /approval/createApprovalFlowName</b>
	 */
	@RequestMapping("createApprovalFlowName")
	@RestReturn(CreateApprovalFlowNameResponse.class)
	public RestResponse createApprovalFlowName(CreateApprovalFlowNameCommand cmd){
		return new RestResponse(approvalService.createApprovalFlowName(cmd));
	}
	
	/**
	 * 
	 * <p>更新审批流程名称</p>
	 * <b>URL: /approval/updateApprovalFlowName</b>
	 */
	@RequestMapping("updateApprovalFlowName")
	@RestReturn(UpdateApprovalFlowNameResponse.class)
	public RestResponse updateApprovalFlowName(UpdateApprovalFlowNameCommand cmd){
		return new RestResponse(approvalService.updateApprovalFlowName(cmd));
	}

	/**
	 * 
	 * <p>设置审批流程级别</p>
	 * <b>URL: /approval/createApprovalFlowLevel</b>
	 */
	@RequestMapping("createApprovalFlowLevel")
	@RestReturn(CreateApprovalFlowLevelResponse.class)
	public RestResponse createApprovalFlowLevel(CreateApprovalFlowLevelCommand cmd){
		return new RestResponse(approvalService.createApprovalFlowLevel(cmd));
	}

	/**
	 * 
	 * <p>更新审批流程级别</p>
	 * <b>URL: /approval/updateApprovalFlowLevel</b>
	 */
	@RequestMapping("updateApprovalFlowLevel")
	@RestReturn(UpdateApprovalFlowLevelResponse.class)
	public RestResponse updateApprovalFlowLevel(UpdateApprovalFlowLevelCommand cmd){
		return new RestResponse(approvalService.updateApprovalFlowLevel(cmd));
	}

	/**
	 * 
	 * <p>审批流程列表</p>
	 * <b>URL: /approval/listApprovalFlow</b>
	 */
	@RequestMapping("listApprovalFlow")
	@RestReturn(ListApprovalFlowResponse.class)
	public RestResponse listApprovalFlow(ListApprovalFlowCommand cmd){
		return new RestResponse(approvalService.listApprovalFlow(cmd));
	}

	/**
	 * 
	 * <p>审批流程简短列表</p>
	 * <b>URL: /approval/listBriefApprovalFlow</b>
	 */
	@RequestMapping("listBriefApprovalFlow")
	@RestReturn(ListBriefApprovalFlowResponse.class)
	public RestResponse listBriefApprovalFlow(ListBriefApprovalFlowCommand cmd){
		return new RestResponse();
	}
	
	/**
	 * 
	 * <p>删除审批流程</p>
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
	 * <p>增加审批规则</p>
	 * <b>URL: /approval/createApprovalRule</b>
	 */
	@RequestMapping("createApprovalRule")
	@RestReturn(CreateApprovalRuleResponse.class)
	public RestResponse createApprovalRule(CreateApprovalRuleCommand cmd){
		return new RestResponse(approvalService.createApprovalRule(cmd));
	}

	/**
	 * 
	 * <p>增加审批规则</p>
	 * <b>URL: /approval/updateApprovalRule</b>
	 */
	@RequestMapping("updateApprovalRule")
	@RestReturn(UpdateApprovalRuleResponse.class)
	public RestResponse updateApprovalRule(UpdateApprovalRuleCommand cmd){
		return new RestResponse(approvalService.updateApprovalRule(cmd));
	}

	/**
	 * 
	 * <p>删除审批规则</p>
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
	 * <p>审批规则列表</p>
	 * <b>URL: /approval/listApprovalRule</b>
	 */
	@RequestMapping("listApprovalRule")
	@RestReturn(ListApprovalRuleResponse.class)
	public RestResponse listApprovalRule(ListApprovalRuleCommand cmd){
		return new RestResponse(approvalService.listApprovalRule(cmd));
	}

	/**
	 * 
	 * <p>审批规则简短列表</p>
	 * <b>URL: /approval/listBriefApprovalRule</b>
	 */
	@RequestMapping("listBriefApprovalRule")
	@RestReturn(ListBriefApprovalRuleResponse.class)
	public RestResponse listBriefApprovalRule(ListBriefApprovalRuleCommand cmd){
		return new RestResponse();
	}
	
}
