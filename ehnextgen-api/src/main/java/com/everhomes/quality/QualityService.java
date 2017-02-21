package com.everhomes.quality;

import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import com.everhomes.rest.organization.OrganizationDTO;
import com.everhomes.rest.quality.CountScoresCommand;
import com.everhomes.rest.quality.CountScoresResponse;
import com.everhomes.rest.quality.CountTasksCommand;
import com.everhomes.rest.quality.CountTasksResponse;
import com.everhomes.rest.quality.CreatQualityStandardCommand;
import com.everhomes.rest.quality.CreateQualityInspectionTaskCommand;
import com.everhomes.rest.quality.CreateQualitySpecificationCommand;
import com.everhomes.rest.quality.DeleteQualityCategoryCommand;
import com.everhomes.rest.quality.DeleteUserQualityInspectionTaskTemplateCommand;
import com.everhomes.rest.quality.DeleteQualitySpecificationCommand;
import com.everhomes.rest.quality.DeleteQualityStandardCommand;
import com.everhomes.rest.quality.DeleteFactorCommand;
import com.everhomes.rest.quality.GetGroupMembersCommand;
import com.everhomes.rest.quality.GetQualitySpecificationCommand;
import com.everhomes.rest.quality.GroupUserDTO;
import com.everhomes.rest.quality.ListEvaluationsCommand;
import com.everhomes.rest.quality.ListEvaluationsResponse;
import com.everhomes.rest.quality.ListUserHistoryTasksCommand;
import com.everhomes.rest.quality.ListQualityCategoriesCommand;
import com.everhomes.rest.quality.ListQualityCategoriesResponse;
import com.everhomes.rest.quality.ListUserQualityInspectionTaskTemplatesCommand;
import com.everhomes.rest.quality.ListQualitySpecificationsCommand;
import com.everhomes.rest.quality.ListQualitySpecificationsResponse;
import com.everhomes.rest.quality.ListQualityStandardsCommand;
import com.everhomes.rest.quality.ListQualityStandardsResponse;
import com.everhomes.rest.quality.ListQualityInspectionTasksCommand;
import com.everhomes.rest.quality.ListQualityInspectionTasksResponse;
import com.everhomes.rest.quality.ListFactorsCommand;
import com.everhomes.rest.quality.ListFactorsResponse;
import com.everhomes.rest.quality.ListRecordsByTaskIdCommand;
import com.everhomes.rest.quality.ListQualityInspectionLogsCommand;
import com.everhomes.rest.quality.ListQualityInspectionLogsResponse;
import com.everhomes.rest.quality.QualityInspectionSpecificationDTO;
import com.everhomes.rest.quality.QualityInspectionTaskDTO;
import com.everhomes.rest.quality.QualityInspectionTaskRecordsDTO;
import com.everhomes.rest.quality.QualityStandardsDTO;
import com.everhomes.rest.quality.ReportRectifyResultCommand;
import com.everhomes.rest.quality.ReportVerificationResultCommand;
import com.everhomes.rest.quality.ReviewReviewQualityStandardCommand;
import com.everhomes.rest.quality.ReviewVerificationResultCommand;
import com.everhomes.rest.quality.UpdateQualityCategoryCommand;
import com.everhomes.rest.quality.UpdateQualitySpecificationCommand;
import com.everhomes.rest.quality.UpdateQualityStandardCommand;
import com.everhomes.rest.quality.UpdateFactorCommand;

public interface QualityService {

	QualityStandardsDTO creatQualityStandard(CreatQualityStandardCommand cmd);
	QualityStandardsDTO updateQualityStandard(UpdateQualityStandardCommand cmd);
	void deleteQualityStandard(DeleteQualityStandardCommand cmd);
	ListQualityStandardsResponse listQualityStandards(ListQualityStandardsCommand cmd);
	void updateQualityCategory(UpdateQualityCategoryCommand cmd);
	void deleteQualityCategory(DeleteQualityCategoryCommand cmd);
	ListFactorsResponse listFactors(ListFactorsCommand cmd);
	void updateFactor(UpdateFactorCommand cmd);
	void deleteFactor(DeleteFactorCommand cmd);
	ListEvaluationsResponse listEvaluations(ListEvaluationsCommand cmd);
	HttpServletResponse exportEvaluations(ListEvaluationsCommand cmd,HttpServletResponse response);
	HttpServletResponse exportInspectionTasks(ListQualityInspectionTasksCommand cmd,HttpServletResponse response);
	ListQualityInspectionTasksResponse listQualityInspectionTasks(ListQualityInspectionTasksCommand cmd);
	QualityInspectionTaskDTO reportVerificationResult(ReportVerificationResultCommand cmd);
	void reviewVerificationResult(ReviewVerificationResultCommand cmd);
	QualityInspectionTaskDTO reportRectifyResult(ReportRectifyResultCommand cmd);
	
	void createTaskByStandard(QualityStandardsDTO standard);
	
	ListQualityCategoriesResponse listQualityCategories(ListQualityCategoriesCommand cmd);
	
	void createTaskByStandardId(Long id);
	
	void createEvaluations();
	List<QualityInspectionTaskRecordsDTO> listRecordsByTaskId(ListRecordsByTaskIdCommand cmd);
	void closeDelayTasks();
	void createTasks();
	
	List<GroupUserDTO> getGroupMembers(Long groupId, boolean isAll);
	List<OrganizationDTO> listUserRelateOrgGroups();
	ListQualityInspectionLogsResponse listQualityInspectionLogs(ListQualityInspectionLogsCommand cmd);
	QualityInspectionTaskDTO createQualityInspectionTask(CreateQualityInspectionTaskCommand cmd);
	
	void reviewQualityStandard(ReviewReviewQualityStandardCommand cmd);
	void createQualitySpecification(CreateQualitySpecificationCommand cmd);
	void updateQualitySpecification(UpdateQualitySpecificationCommand cmd);
	void deleteQualitySpecification(DeleteQualitySpecificationCommand cmd);
	ListQualitySpecificationsResponse listQualitySpecifications(ListQualitySpecificationsCommand cmd);
	CountScoresResponse countScores(CountScoresCommand cmd);
	CountTasksResponse countTasks(CountTasksCommand cmd);
	
	QualityInspectionSpecificationDTO getQualitySpecification(GetQualitySpecificationCommand cmd);
	
	ListQualityInspectionTasksResponse listUserHistoryTasks(ListUserHistoryTasksCommand cmd);
	ListQualityInspectionTasksResponse listUserQualityInspectionTaskTemplates(ListUserQualityInspectionTaskTemplatesCommand cmd);
	void deleteUserQualityInspectionTaskTemplate(DeleteUserQualityInspectionTaskTemplateCommand cmd);
	void sendTaskMsg();
}
