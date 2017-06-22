package com.everhomes.quality;

import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;


import com.everhomes.rest.quality.*;

import com.everhomes.module.ServiceModuleService;
import com.everhomes.rest.module.ListUserRelatedProjectByModuleCommand;
import com.everhomes.util.ConvertHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.everhomes.acl.RolePrivilegeService;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.quality.QualityService;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.acl.ListUserRelatedProjectByModuleIdCommand;
import com.everhomes.rest.address.CommunityDTO;
import com.everhomes.rest.organization.OrganizationDTO;

@RestDoc(value = "Quality Controller", site = "core")
@RestController
@RequestMapping("/quality")
public class QualityController extends ControllerBase {

	@Autowired
	private QualityService qualityService;
	
	@Autowired
	private RolePrivilegeService rolePrivilegeService;

	@Autowired
	private ServiceModuleService serviceModuleService;
	
	/**
	 * <b>URL: /quality/creatQualityStandard</b>
	 * <p>创建品质核查标准</p>
	 */
	@RequestMapping("creatQualityStandard")
	@RestReturn(value = QualityStandardsDTO.class)
	public RestResponse creatQualityStandard(CreatQualityStandardCommand cmd) {
		
		QualityStandardsDTO standard = qualityService.creatQualityStandard(cmd);
		
		RestResponse response = new RestResponse(standard);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	
	/**
	 * <b>URL: /quality/updateQualityStandard</b>
	 * <p>修改品质核查标准</p>
	 */
	@RequestMapping("updateQualityStandard")
	@RestReturn(value = QualityStandardsDTO.class)
	public RestResponse updateQualityStandard(UpdateQualityStandardCommand cmd) {
		
		QualityStandardsDTO standard = qualityService.updateQualityStandard(cmd);
		
		RestResponse response = new RestResponse(standard);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	
	/**
	 * <b>URL: /quality/deleteQualityStandard</b>
	 * <p>删除品质核查标准</p>
	 */
	@RequestMapping("deleteQualityStandard")
	@RestReturn(value = String.class)
	public RestResponse deleteQualityStandard(DeleteQualityStandardCommand cmd) {
		
		qualityService.deleteQualityStandard(cmd);
		
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	
	/**
	 * <b>URL: /quality/listQualityStandards</b>
	 * <p>查看品质核查标准</p>
	 */
	@RequestMapping("listQualityStandards")
	@RestReturn(value = ListQualityStandardsResponse.class)
	public RestResponse listQualityStandards(ListQualityStandardsCommand cmd) {
		
		ListQualityStandardsResponse standards = qualityService.listQualityStandards(cmd);
		
		RestResponse response = new RestResponse(standards);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	
	/**
	 * <b>URL: /quality/listQualityCategories</b>
	 * <p>查看品质核查类型</p>
	 */
	@RequestMapping("listQualityCategories")
	@RestReturn(value = ListQualityCategoriesResponse.class)
	public RestResponse listQualityCategories(ListQualityCategoriesCommand cmd) {
		
		ListQualityCategoriesResponse categories = qualityService.listQualityCategories(cmd);
		
		RestResponse response = new RestResponse(categories);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	
	/**
	 * <b>URL: /quality/updateQualityCategory</b>
	 * <p>修改品质核查类型 </p>
	 */
	@RequestMapping("updateQualityCategory")
	@RestReturn(value = String.class)
	public RestResponse updateQualityCategory(UpdateQualityCategoryCommand cmd) {
		
		qualityService.updateQualityCategory(cmd);
		
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	
	/**
	 * <b>URL: /quality/deleteQualityCategory</b>
	 * <p>删除品质核查类型 </p>
	 */
	@RequestMapping("deleteQualityCategory")
	@RestReturn(value = String.class)
	public RestResponse deleteQualityCategory(DeleteQualityCategoryCommand cmd) {
		
		qualityService.deleteQualityCategory(cmd);
		
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	
	/**
	 * <b>URL: /quality/listFactors</b>
	 * <p>查看权重</p>
	 */
	@RequestMapping("listFactors")
	@RestReturn(value = ListFactorsResponse.class)
	public RestResponse listFactors(ListFactorsCommand cmd) {
		
		ListFactorsResponse factors = qualityService.listFactors(cmd);
		
		RestResponse response = new RestResponse(factors);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	
	/**
	 * <b>URL: /quality/updateFactor</b>
	 * <p>创建或修改业务组对特定类型的权重</p>
	 */
	@RequestMapping("updateFactor")
	@RestReturn(value = String.class)
	public RestResponse updateFactor(UpdateFactorCommand cmd) {
		
		qualityService.updateFactor(cmd);
		
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	
	/**
	 * <b>URL: /quality/deleteFactor</b>
	 * <p>删除业务组对特定类型的权重</p>
	 */
	@RequestMapping("deleteFactor")
	@RestReturn(value = String.class)
	public RestResponse deleteFactor(DeleteFactorCommand cmd) {
		
		qualityService.deleteFactor(cmd);
		
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	
	/**
	 * <b>URL: /quality/listEvaluations</b>
	 * <p>查看绩效(暂时还是按照ownerType和ownerId)</p>
	 */
	@RequestMapping("listEvaluations")
	@RestReturn(value = ListEvaluationsResponse.class)
	public RestResponse listEvaluations(ListEvaluationsCommand cmd) {
		
		ListEvaluationsResponse performances = qualityService.listEvaluations(cmd);
		
		RestResponse response = new RestResponse(performances);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	
	/**
	 * <b>URL: /quality/exportEvaluations</b>
	 * <p>导出绩效（暂时还是按照ownerType和ownerId）</p>
	 */
	@RequestMapping("exportEvaluations")
	public HttpServletResponse exportEvaluations(@Valid ListEvaluationsCommand cmd,HttpServletResponse response) {
		
		HttpServletResponse commandResponse = qualityService.exportEvaluations(cmd, response);
		
		return commandResponse;
	}
	
	/**
	 * <b>URL: /quality/exportInspectionTasks</b>
	 * <p>导出核查任务</p>
	 */
	@RequestMapping("exportInspectionTasks")
	public HttpServletResponse exportInspectionTasks(@Valid ListQualityInspectionTasksCommand cmd,HttpServletResponse response) {
		
		HttpServletResponse commandResponse = qualityService.exportInspectionTasks(cmd, response);
		
		return commandResponse;
	}
	
	/**
	 * <b>URL: /quality/listQualityInspectionTasks</b>
	 * <p>查看核查任务列表</p>
	 */
	@RequestMapping("listQualityInspectionTasks")
	@RestReturn(value = ListQualityInspectionTasksResponse.class)
	public RestResponse listQualityInspectionTasks(ListQualityInspectionTasksCommand cmd) {
		
		ListQualityInspectionTasksResponse tasks = qualityService.listQualityInspectionTasks(cmd);
		
		RestResponse response = new RestResponse(tasks);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /quality/findQualityInspectionTask</b>
	 * <p>查看核查任务详情</p>
	 */
	@RequestMapping("findQualityInspectionTask")
	@RestReturn(value = QualityInspectionTaskDTO.class)
	public RestResponse findQualityInspectionTask(FindQualityInspectionTaskCommand cmd) {

		QualityInspectionTaskDTO task = qualityService.findQualityInspectionTask(cmd);

		RestResponse response = new RestResponse(task);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /quality/reportVerificationResult</b>
	 * <p>核查上报</p>
	 */
	@RequestMapping("reportVerificationResult")
	@RestReturn(value = QualityInspectionTaskDTO.class)
	public RestResponse reportVerificationResult(ReportVerificationResultCommand cmd) {
		
		QualityInspectionTaskDTO dto = qualityService.reportVerificationResult(cmd);
		
		RestResponse response = new RestResponse(dto);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	
	/**
	 * <b>URL: /quality/reportRectifyResult</b>
	 * <p>整改上报</p>
	 */
	@RequestMapping("reportRectifyResult")
	@RestReturn(value = QualityInspectionTaskDTO.class)
	public RestResponse reportRectifyResult(ReportRectifyResultCommand cmd) {
		
		QualityInspectionTaskDTO dto = qualityService.reportRectifyResult(cmd);
		
		RestResponse response = new RestResponse(dto);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	
	/**
	 * <b>URL: /quality/reviewVerificationResult</b>
	 * <p>审阅核查任务</p>
	 */
	@RequestMapping("reviewVerificationResult")
	@RestReturn(value = String.class)
	public RestResponse reviewVerificationResult(ReviewVerificationResultCommand cmd) {
		
		qualityService.reviewVerificationResult(cmd);
		
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	
	/**
	 * <b>URL: /quality/createTaskByStandard</b>
	 * <p>根据品质核查标准创建任务</p>
	 */
	@RequestMapping("createTaskByStandard")
	@RestReturn(value = String.class)
	public RestResponse createTaskByStandard(DeleteQualityStandardCommand cmd) {
		
		qualityService.createTaskByStandardId(cmd.getStandardId());
		
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	
	/**
	 * <b>URL: /quality/listRecordsByTaskId</b>
	 * <p>查看任务的操作记录</p>
	 */
	@RequestMapping("listRecordsByTaskId")
	@RestReturn(value = QualityInspectionTaskRecordsDTO.class, collection = true)
	public RestResponse listRecordsByTaskId(ListRecordsByTaskIdCommand cmd) {
		
		List<QualityInspectionTaskRecordsDTO> records = qualityService.listRecordsByTaskId(cmd);
		
		RestResponse response = new RestResponse(records);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	
	/**
	 * <b>URL: /quality/getGroupMembers</b>
	 * <p>查看业务组成员</p>
	 */
	@RequestMapping("getGroupMembers")
	@RestReturn(value = GroupUserDTO.class, collection = true)
	public RestResponse getGroupMembers(GetGroupMembersCommand cmd) {
		
		List<GroupUserDTO> records = qualityService.getGroupMembers(cmd.getGroupId(), true);
		
		RestResponse response = new RestResponse(records);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	
	/**
	 * <b>URL: /quality/listUserRelateOrgGroups</b>
	 * <p>查看用户所在的业务组</p>
	 */
	@RequestMapping("listUserRelateOrgGroups")
	@RestReturn(value = OrganizationDTO.class, collection = true)
	public RestResponse listUserRelateOrgGroups() {
		
		List<OrganizationDTO> groupDtos = qualityService.listUserRelateOrgGroups();
		
		RestResponse response = new RestResponse(groupDtos);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /quality/listQualityInspectionLogs</b>
	 * <p>查看品质核查的操作记录</p>
	 */
	@RequestMapping("listQualityInspectionLogs")
	@RestReturn(value = ListQualityInspectionLogsResponse.class)
	public RestResponse listQualityInspectionLogs(ListQualityInspectionLogsCommand cmd) {
		
		ListQualityInspectionLogsResponse recordDtos = qualityService.listQualityInspectionLogs(cmd);
		
		RestResponse response = new RestResponse(recordDtos);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	
	/**
	 * <b>URL: /quality/createQualityInspectionTask</b>
	 * <p>主动创建品质核查任务</p>
	 */
	@RequestMapping("createQualityInspectionTask")
	@RestReturn(value = QualityInspectionTaskDTO.class)
	public RestResponse createQualityInspectionTask(CreateQualityInspectionTaskCommand cmd) {
		
		QualityInspectionTaskDTO task = qualityService.createQualityInspectionTask(cmd);
		
		RestResponse response = new RestResponse(task);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	
	/**
	 * <b>URL: /quality/reviewQualityStandard</b>
	 * <p>审核标准</p>
	 */
	@RequestMapping("reviewQualityStandard")
	@RestReturn(value = String.class)
	public RestResponse reviewQualityStandard(ReviewReviewQualityStandardCommand cmd) {
		
		qualityService.reviewQualityStandard(cmd);
		
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	
	/**
	 * <b>URL: /quality/createQualitySpecification</b>
	 * <p>创建品质核查类型/规范 </p>
	 */
	@RequestMapping("createQualitySpecification")
	@RestReturn(value = String.class)
	public RestResponse createQualitySpecification(CreateQualitySpecificationCommand cmd) {
		
		qualityService.createQualitySpecification(cmd);
		
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	
	/**
	 * <b>URL: /quality/updateQualitySpecification</b>
	 * <p>修改品质核查类型/规范 </p>
	 */
	@RequestMapping("updateQualitySpecification")
	@RestReturn(value = String.class)
	public RestResponse updateQualitySpecification(UpdateQualitySpecificationCommand cmd) {
		
		qualityService.updateQualitySpecification(cmd);
		
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	
	/**
	 * <b>URL: /quality/deleteQualitySpecification</b>
	 * <p>删除品质核查类型/规范 </p>
	 */
	@RequestMapping("deleteQualitySpecification")
	@RestReturn(value = String.class)
	public RestResponse deleteQualitySpecification(DeleteQualitySpecificationCommand cmd) {
		
		qualityService.deleteQualitySpecification(cmd);
		
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	
	/**
	 * <b>URL: /quality/listQualitySpecifications</b>
	 * <p>查看品质核查类型/规范</p>
	 */
	@RequestMapping("listQualitySpecifications")
	@RestReturn(value = ListQualitySpecificationsResponse.class)
	public RestResponse listQualitySpecifications(ListQualitySpecificationsCommand cmd) {
		
		ListQualitySpecificationsResponse specifications = qualityService.listQualitySpecifications(cmd);
		
		RestResponse response = new RestResponse(specifications);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	
	/**
	 * <b>URL: /quality/getQualitySpecification</b>
	 * <p>查看特定规范及其事项</p>
	 */
	@RequestMapping("getQualitySpecification")
	@RestReturn(value = QualityInspectionSpecificationDTO.class)
	public RestResponse getQualitySpecification(GetQualitySpecificationCommand cmd) {
		
		QualityInspectionSpecificationDTO specification = qualityService.getQualitySpecification(cmd);
		
		RestResponse response = new RestResponse(specification);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	
	/**
	 * <b>URL: /quality/countScores</b>
	 * <p>分数统计</p>
	 */
	@RequestMapping("countScores")
	@RestReturn(value = CountScoresResponse.class)
	public RestResponse countScores(CountScoresCommand cmd) {
		
		CountScoresResponse scores = qualityService.countScores(cmd);
		
		RestResponse response = new RestResponse(scores);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	
	/**
	 * <b>URL: /quality/countTasks</b>
	 * <p>任务数统计</p>
	 */
	@RequestMapping("countTasks")
	@RestReturn(value = CountTasksResponse.class)
	public RestResponse countTasks(CountTasksCommand cmd) {
		
		CountTasksResponse tasks = qualityService.countTasks(cmd);
		
		RestResponse response = new RestResponse(tasks);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	
	/**
	 * <b>URL: /quality/listUserHistoryTasks</b>
	 * <p>个人执行过的历史任务</p>
	 */
	@RequestMapping("listUserHistoryTasks")
	@RestReturn(value = ListQualityInspectionTasksResponse.class)
	public RestResponse listUserHistoryTasks(ListUserHistoryTasksCommand cmd) {
		ListQualityInspectionTasksResponse tasks = qualityService.listUserHistoryTasks(cmd);
		
		RestResponse response = new RestResponse(tasks);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	
	/**
	 * <b>URL: /quality/listUserQualityInspectionTaskTemplates</b>
	 * <p>获得用户模板列表</p>
	 */
	@RequestMapping("listUserQualityInspectionTaskTemplates")
	@RestReturn(value = ListQualityInspectionTasksResponse.class)
	public RestResponse listUserQualityInspectionTaskTemplates(ListUserQualityInspectionTaskTemplatesCommand cmd) {
		
		ListQualityInspectionTasksResponse task = qualityService.listUserQualityInspectionTaskTemplates(cmd);
		
		RestResponse response = new RestResponse(task);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	
	/**
	 * <b>URL: /quality/deleteUserQualityInspectionTaskTemplate</b>
	 * <p>删除用户模板</p>
	 */
	@RequestMapping("deleteUserQualityInspectionTaskTemplate")
	@RestReturn(value = String.class)
	public RestResponse deleteUserQualityInspectionTaskTemplate(DeleteUserQualityInspectionTaskTemplateCommand cmd) {
		
		qualityService.deleteUserQualityInspectionTaskTemplate(cmd);
		
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	
	/**
	 * <b>URL: /quality/listUserRelatedProjectByModuleId</b>
	 * <p>用户关联项目</p>
	 */
	@RequestMapping("listUserRelatedProjectByModuleId")
	@RestReturn(value = CommunityDTO.class, collection = true)
	public RestResponse listUserRelatedProjectByModuleId(ListUserRelatedProjectByModuleIdCommand cmd) {

		List<CommunityDTO> dtos = serviceModuleService.listUserRelatedCommunityByModuleId(ConvertHelper.convert(cmd, ListUserRelatedProjectByModuleCommand.class));

		RestResponse response = new RestResponse(dtos);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	
}
