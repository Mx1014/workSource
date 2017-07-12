// @formatter:off
package com.everhomes.approval;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.approval.ApproveApprovalRequestCommand;
import com.everhomes.rest.approval.CreateApprovalCategoryCommand;
import com.everhomes.rest.approval.CreateApprovalCategoryResponse;
import com.everhomes.rest.approval.CreateApprovalFlowInfoCommand;
import com.everhomes.rest.approval.CreateApprovalFlowInfoResponse;
import com.everhomes.rest.approval.CreateApprovalFlowLevelCommand;
import com.everhomes.rest.approval.CreateApprovalFlowLevelResponse;
import com.everhomes.rest.approval.CreateApprovalRuleCommand;
import com.everhomes.rest.approval.CreateApprovalRuleResponse;
import com.everhomes.rest.approval.DeleteApprovalCategoryCommand;
import com.everhomes.rest.approval.DeleteApprovalFlowCommand;
import com.everhomes.rest.approval.DeleteApprovalRuleCommand;
import com.everhomes.rest.approval.GetApprovalBasicInfoOfRequestCommand;
import com.everhomes.rest.approval.GetApprovalBasicInfoOfRequestResponse;
import com.everhomes.rest.approval.GetTargetApprovalRuleCommand;
import com.everhomes.rest.approval.GetTargetApprovalRuleResponse;
import com.everhomes.rest.approval.ListApprovalCategoryCommand;
import com.everhomes.rest.approval.ListApprovalCategoryResponse;
import com.everhomes.rest.approval.ListApprovalFlowCommand;
import com.everhomes.rest.approval.ListApprovalFlowOfRequestCommand;
import com.everhomes.rest.approval.ListApprovalFlowOfRequestResponse;
import com.everhomes.rest.approval.ListApprovalFlowResponse;
import com.everhomes.rest.approval.ListApprovalLogAndFlowOfRequestCommand;
import com.everhomes.rest.approval.ListApprovalLogAndFlowOfRequestResponse;
import com.everhomes.rest.approval.ListApprovalLogOfRequestCommand;
import com.everhomes.rest.approval.ListApprovalLogOfRequestResponse;
import com.everhomes.rest.approval.ListApprovalRequestCommand;
import com.everhomes.rest.approval.ListApprovalRequestResponse;
import com.everhomes.rest.approval.ListApprovalRuleCommand;
import com.everhomes.rest.approval.ListApprovalRuleResponse;
import com.everhomes.rest.approval.ListApprovalUserCommand;
import com.everhomes.rest.approval.ListApprovalUserResponse;
import com.everhomes.rest.approval.ListBriefApprovalFlowCommand;
import com.everhomes.rest.approval.ListBriefApprovalFlowResponse;
import com.everhomes.rest.approval.ListBriefApprovalRuleCommand;
import com.everhomes.rest.approval.ListBriefApprovalRuleResponse;
import com.everhomes.rest.approval.ListTargetUsersCommand;
import com.everhomes.rest.approval.RejectApprovalRequestCommand;
import com.everhomes.rest.approval.UpdateApprovalCategoryCommand;
import com.everhomes.rest.approval.UpdateApprovalCategoryResponse;
import com.everhomes.rest.approval.UpdateApprovalFlowInfoCommand;
import com.everhomes.rest.approval.UpdateApprovalFlowInfoResponse;
import com.everhomes.rest.approval.UpdateApprovalFlowLevelCommand;
import com.everhomes.rest.approval.UpdateApprovalFlowLevelResponse;
import com.everhomes.rest.approval.UpdateApprovalRuleCommand;
import com.everhomes.rest.approval.UpdateApprovalRuleResponse;
import com.everhomes.rest.approval.UpdateTargetApprovalRuleCommand;
import com.everhomes.rest.organization.OrganizationMemberDTO;

@RestDoc("approval controller")
@RestController
@RequestMapping("/approval")
public class ApprovalController extends ControllerBase {
	
	@Autowired
	private ApprovalService approvalService;

	/**
	 * 
	 * <p>1.增加审批类别，如请假的公出、事假等</p>
	 * <b>URL: /approval/createApprovalCategory</b>
	 */
	@RequestMapping("createApprovalCategory")
	@RestReturn(CreateApprovalCategoryResponse.class)
	public RestResponse createApprovalCategory(CreateApprovalCategoryCommand cmd){
		return new RestResponse(approvalService.createApprovalCategory(cmd));
	}
	
	/**
	 * 
	 * <p>2.更新审批类别</p>
	 * <b>URL: /approval/updateApprovalCategory</b>
	 */
	@RequestMapping("updateApprovalCategory")
	@RestReturn(UpdateApprovalCategoryResponse.class)
	public RestResponse updateApprovalCategory(UpdateApprovalCategoryCommand cmd){
		return new RestResponse(approvalService.updateApprovalCategory(cmd));
	}

	/**
	 * 
	 * <p>3.列出审批类别</p>
	 * <b>URL: /approval/listApprovalCategory</b>
	 */
	@RequestMapping("listApprovalCategory")
	@RestReturn(ListApprovalCategoryResponse.class)
	public RestResponse listApprovalCategory(ListApprovalCategoryCommand cmd){
		return new RestResponse(approvalService.listApprovalCategory(cmd));
	}

	/**
	 * 
	 * <p>4.删除审批类别</p>
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
	 * <p>5.设置审批流程信息</p>
	 * <b>URL: /approval/createApprovalFlowInfo</b>
	 */
	@RequestMapping("createApprovalFlowInfo")
	@RestReturn(CreateApprovalFlowInfoResponse.class)
	public RestResponse createApprovalFlowInfo(CreateApprovalFlowInfoCommand cmd){
		return new RestResponse(approvalService.createApprovalFlowInfo(cmd));
	}
	
	/**
	 * 
	 * <p>6.更新审批流程信息</p>
	 * <b>URL: /approval/updateApprovalFlowInfo</b>
	 */
	@RequestMapping("updateApprovalFlowInfo")
	@RestReturn(UpdateApprovalFlowInfoResponse.class)
	public RestResponse updateApprovalFlowInfo(UpdateApprovalFlowInfoCommand cmd){
		return new RestResponse(approvalService.updateApprovalFlowInfo(cmd));
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
		return new RestResponse(approvalService.listBriefApprovalFlow(cmd));
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
		return new RestResponse(approvalService.listBriefApprovalRule(cmd));
	}

	/**
	 * 
	 * <p>17.同意申请</p>
	 * <b>URL: /approval/approveApprovalRequest</b>
	 */
	@RequestMapping("approveApprovalRequest")
	@RestReturn(String.class)
	public RestResponse approveApprovalRequest(ApproveApprovalRequestCommand cmd){
		approvalService.approveApprovalRequest(cmd);
		return new RestResponse();
	}

	/**
	 * 
	 * <p>18.驳回申请</p>
	 * <b>URL: /approval/rejectApprovalRequest</b>
	 */
	@RequestMapping("rejectApprovalRequest")
	@RestReturn(String.class)
	public RestResponse rejectApprovalRequest(RejectApprovalRequestCommand cmd){
		approvalService.rejectApprovalRequest(cmd);
		return new RestResponse();
	}
	
	/**
	 * 
	 * <p>19.获取申请的审批基本信息</p>
	 * <b>URL: /approval/getApprovalBasicInfoOfRequest</b>
	 */
	@RequestMapping("getApprovalBasicInfoOfRequest")
	@RestReturn(GetApprovalBasicInfoOfRequestResponse.class)
	public RestResponse getApprovalBasicInfoOfRequest(GetApprovalBasicInfoOfRequestCommand cmd){
		return new RestResponse(approvalService.getApprovalBasicInfoOfRequest(cmd));
	}

	/**
	 * 
	 * <p>20.获取申请的审批日志与审批流程列表</p>
	 * <b>URL: /approval/listApprovalLogAndFlowOfRequest</b>
	 */
	@RequestMapping("listApprovalLogAndFlowOfRequest")
	@RestReturn(ListApprovalLogAndFlowOfRequestResponse.class)
	public RestResponse listApprovalLogAndFlowOfRequest(ListApprovalLogAndFlowOfRequestCommand cmd){
		return new RestResponse(approvalService.listApprovalLogAndFlowOfRequest(cmd));
	}

	/**
	 * 
	 * <p>21.获取申请的审批日志列表</p>
	 * <b>URL: /approval/listApprovalLogOfRequest</b>
	 */
	@RequestMapping("listApprovalLogOfRequest")
	@RestReturn(ListApprovalLogOfRequestResponse.class)
	public RestResponse listApprovalLogOfRequest(ListApprovalLogOfRequestCommand cmd){
		return new RestResponse(approvalService.listApprovalLogOfRequest(cmd));
	}

	/**
	 * 
	 * <p>22.获取申请的审批流程列表</p>
	 * <b>URL: /approval/listApprovalFlowOfRequest</b>
	 */
	@RequestMapping("listApprovalFlowOfRequest")
	@RestReturn(ListApprovalFlowOfRequestResponse.class)
	public RestResponse listApprovalFlowOfRequest(ListApprovalFlowOfRequestCommand cmd){
		return new RestResponse(approvalService.listApprovalFlowOfRequest(cmd));
	}

	/**
	 * 
	 * <p>23.人员列表，可按部门、姓名筛选</p>
	 * <b>URL: /approval/listApprovalUser</b>
	 */
	@RequestMapping("listApprovalUser")
	@RestReturn(ListApprovalUserResponse.class)
	public RestResponse listApprovalUser(ListApprovalUserCommand cmd){
		return new RestResponse(approvalService.listApprovalUser(cmd));
	}

	/**
	 * 
	 * <p>24.查询申请列表</p>
	 * <b>URL: /approval/listApprovalRequest</b>
	 */
	@RequestMapping("listApprovalRequest")
	@RestReturn(ListApprovalRequestResponse.class)
	public RestResponse listApprovalRequest(ListApprovalRequestCommand cmd){
		return new RestResponse(approvalService.listApprovalRequest(cmd));
	}
	
	/**
	 * 
	 * <p>25.查询具体某机构/人 的审批规则</p>
	 * <b>URL: /approval/getTargetApprovalRule</b>
	 */
	@RequestMapping("getTargetApprovalRule")
	@RestReturn(GetTargetApprovalRuleResponse.class)
	public RestResponse getTargetApprovalRule(GetTargetApprovalRuleCommand cmd){
		return new RestResponse(approvalService.getTargetApprovalRule(cmd));
	}
	

	/**
	 * 
	 * <p>26.更新具体某机构/人 的审批规则</p>
	 * <b>URL: /approval/updateTargetApprovalRule</b>
	 */
	@RequestMapping("updateTargetApprovalRule")
	@RestReturn(String.class)
	public RestResponse updateTargetApprovalRule(UpdateTargetApprovalRuleCommand cmd){
		approvalService.updateTargetApprovalRule(cmd);
		return new RestResponse();
	}
	
	/**
	 * 
	 * <p>27. 删除具体某机构/人 的审批规则</p>
	 * <b>URL: /approval/deleteTargetApprovalRule</b>
	 */
	@RequestMapping("deleteTargetApprovalRule")
	@RestReturn(String.class)
	public RestResponse deleteTargetApprovalRule(GetTargetApprovalRuleCommand cmd){
		approvalService.deleteTargetApprovalRule(cmd);
		return new RestResponse();
	}
	
	/**
	 * 
	 * <p>28. 获取特殊设置个人接口</p>
	 * <b>URL: /approval/listTargetUsers</b>
	 */
	@RequestMapping("listTargetUsers")
	@RestReturn(value = OrganizationMemberDTO.class, collection = true)
	public RestResponse listTargetUsers(ListTargetUsersCommand cmd){
		List<OrganizationMemberDTO> result = approvalService.listTargetUsers(cmd);
		return new RestResponse(result);
	}
}
