package com.everhomes.quality;

import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import com.everhomes.rest.organization.OrganizationDTO;
import com.everhomes.rest.quality.*;

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
	void sendTaskMsg(Long startTime, Long endTime);

	QualityInspectionTaskDTO findQualityInspectionTask(FindQualityInspectionTaskCommand cmd);
}
