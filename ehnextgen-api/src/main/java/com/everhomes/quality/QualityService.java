package com.everhomes.quality;

import com.everhomes.rest.organization.OrganizationDTO;
import com.everhomes.rest.pmNotify.DeletePmNotifyParamsCommand;
import com.everhomes.rest.pmNotify.ListPmNotifyParamsCommand;
import com.everhomes.rest.pmNotify.PmNotifyParamDTO;
import com.everhomes.rest.pmNotify.SetPmNotifyParamsCommand;
import com.everhomes.rest.quality.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Set;

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

	SampleQualityInspectionDTO createSampleQualityInspection(CreateSampleQualityInspectionCommand cmd);
	SampleQualityInspectionDTO updateSampleQualityInspection(UpdateSampleQualityInspectionCommand cmd);
	SampleQualityInspectionDTO findSampleQualityInspection(FindSampleQualityInspectionCommand cmd);
	void deleteSampleQualityInspection(FindSampleQualityInspectionCommand cmd);
	ListSampleQualityInspectionResponse listSampleQualityInspection(ListSampleQualityInspectionCommand cmd);
	ListQualityInspectionTasksResponse listSampleQualityInspectionTasks(ListSampleQualityInspectionTasksCommand cmd);

	CountSampleTaskScoresResponse countSampleTaskScores(CountSampleTaskScoresCommand cmd);
	CountSampleTasksResponse countSampleTasks(CountSampleTasksCommand cmd);
	CountScoresResponse countSampleTaskCommunityScores(CountSampleTaskCommunityScoresCommand cmd);
	CountSampleTaskSpecificationItemScoresResponse countSampleTaskSpecificationItemScores(CountSampleTaskSpecificationItemScoresCommand cmd);

	void updateSampleScoreStat();
	QualityInspectionTaskDTO findQualityInspectionTask(FindQualityInspectionTaskCommand cmd);

	CurrentUserInfoDTO getCurrentUserInfo();

	HttpServletResponse exportSampleTaskCommunityScores(CountSampleTaskCommunityScoresCommand cmd, HttpServletResponse httpResponse);

    QualityOfflineTaskDetailsResponse getOfflineTaskDetail(ListQualityInspectionTasksCommand cmd);

    OfflineSampleQualityInspectionResponse getOfflineSampleQualityInspection(ListSampleQualityInspectionCommand cmd);

    List<QualityInspectionSpecificationDTO> batchUpdateQualitySpecification(BatchUpdateQualitySpecificationsCommand cmd);

    QualityStandardsDTO getQualityStandards(DeleteQualityStandardCommand cmd);

    QualityOfflineTaskReportResponse offlineTaskReport(OfflineTaskReportCommand cmd);

    void startCrontabTask();

    void deletePmNotifyParams(DeletePmNotifyParamsCommand cmd);

	List<PmNotifyParamDTO> listPmNotifyParams(ListPmNotifyParamsCommand cmd);

	void setPmNotifyParams(SetPmNotifyParamsCommand cmd);

    Set<Long> getTaskGroupUsers(Long ownerId);
}
