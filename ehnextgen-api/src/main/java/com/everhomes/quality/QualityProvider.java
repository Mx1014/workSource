package com.everhomes.quality;

import java.sql.Timestamp;
import java.util.List;
import java.util.Set;

import com.everhomes.listing.ListingLocator;
import com.everhomes.rest.quality.ScoreDTO;
import com.everhomes.rest.quality.TaskCountDTO;


public interface QualityProvider {
	
	void createVerificationTasks(QualityInspectionTasks task);
	void updateVerificationTasks(QualityInspectionTasks task);
	void deleteVerificationTasks(Long taskId);
	QualityInspectionTasks findVerificationTaskById(Long taskId);
	List<QualityInspectionTasks> listVerificationTasks(ListingLocator locator, int count, Long ownerId, String ownerType, Long targetId, String targetType, 
    		Byte taskType, Long executeUid, Timestamp startDate, Timestamp endDate, List<Long> groupId, 
    		Byte executeStatus, Byte reviewStatus, boolean timeCompared, List<Long> standardIds, Byte manualFlag);
	int countVerificationTasks(Long ownerId, String ownerType, Byte taskType, Long executeUid, 
			Timestamp startDate, Timestamp endDate, Long groupId, Byte executeStatus, Byte reviewStatus);


	void createQualityInspectionStandards(QualityInspectionStandards standard);
	void updateQualityInspectionStandards(QualityInspectionStandards standard);
	QualityInspectionStandards findStandardById(Long id);
	List<QualityInspectionStandards> findStandardsByCategoryId(Long categoryId);
	List<QualityInspectionStandards> listQualityInspectionStandards(ListingLocator locator, int count, Long ownerId, String ownerType, String targetType, Long targetId, Byte reviewResult);
	
	void createQualityInspectionEvaluationFactors(QualityInspectionEvaluationFactors factor);
	void updateQualityInspectionEvaluationFactors(QualityInspectionEvaluationFactors factor);
	void deleteQualityInspectionEvaluationFactors(Long factorId);
	List<QualityInspectionEvaluationFactors> listQualityInspectionEvaluationFactors(ListingLocator locator, int count, Long ownerId, String ownerType);
	QualityInspectionEvaluationFactors findQualityInspectionFactorById(Long id);
	QualityInspectionEvaluationFactors findQualityInspectionFactorByGroupIdAndCategoryId(Long groupId, Long categoryId);
	
	void createQualityInspectionStandardGroupMap(QualityInspectionStandardGroupMap standardGroup);
	void deleteQualityInspectionStandardGroupMap(Long standardGroupId);
	void deleteQualityInspectionStandardGroupMapByStandardId(Long standardId);
	List<Long> listQualityInspectionStandardGroupMapByGroup(List<Long> groupIds, Byte groupType);
	
	void createQualityInspectionTaskAttachments(QualityInspectionTaskAttachments attachment);
	void deleteQualityInspectionTaskAttachments(Long attachmentId);
	
	void createQualityInspectionEvaluations(QualityInspectionEvaluations evaluation);
	List<QualityInspectionEvaluations> listQualityInspectionEvaluations(ListingLocator locator, int count, Long ownerId, String ownerType,
			Timestamp startDate, Timestamp endDate);
	
	int countInspectionEvaluations(Long ownerId, String ownerType, Timestamp startDate, Timestamp endDate);
	
	void createQualityInspectionTaskRecords(QualityInspectionTaskRecords record);
	List<QualityInspectionTaskRecords> listRecordsByTaskId(Long taskId);
	QualityInspectionTaskRecords listLastRecordByTaskId(Long taskId);
	
	void populateRecordAttachments(final List<QualityInspectionTaskRecords> records);
	void populateRecordAttachment(QualityInspectionTaskRecords record);
	
	void populateStandardsGroups(final List<QualityInspectionStandards> standards);
	void populateStandardGroups(QualityInspectionStandards standard);
	
	void createQualityInspectionCategories(QualityInspectionCategories category);
	void updateQualityInspectionCategories(QualityInspectionCategories category);
	QualityInspectionCategories findQualityInspectionCategoriesByCategoryId(Long id);
	List<QualityInspectionCategories> listQualityInspectionCategories(ListingLocator locator, int count, Long ownerId, String ownerType, Long parentId);
	List<QualityInspectionCategories> listQualityInspectionCategoriesByPath(String superiorPath);
	
	List<QualityInspectionTasks> listClosedTask(Timestamp startDate, Timestamp endDate);
	
	void populateTaskRecords(QualityInspectionTasks task);
	void populateTasksRecords(final List<QualityInspectionTasks> tasks);
	
	void closeDelayTasks();
	void closeTask(QualityInspectionTasks task);
	
	List<QualityInspectionStandards> listActiveStandards();

	
	void createQualityInspectionLogs(QualityInspectionLogs log);
	List<QualityInspectionLogs> listQualityInspectionLogs(String ownerType, Long ownerId, String targetType, Long targetId, ListingLocator locator, int count);
	
	QualityInspectionStandards findStandardById(Long id, String ownerType, Long ownerId, String targetType, Long targetId);
	void deleteQualityInspectionStandardSpecificationMapByStandardId(Long standardId);
	void deleteQualityInspectionStandardSpecificationMap(Long standardSpecificationId);
	QualityInspectionStandardSpecificationMap createQualityInspectionStandardSpecificationMap(QualityInspectionStandardSpecificationMap map);
	void updateQualityInspectionStandardSpecificationMap(QualityInspectionStandardSpecificationMap map);
	QualityInspectionSpecifications findSpecificationById(Long id, String ownerType, Long ownerId);
	QualityInspectionSpecifications getSpecificationById(Long id);
	
	void populateStandardsSpecifications(final List<QualityInspectionStandards> standards);
	void populateStandardSpecifications(QualityInspectionStandards standard);
	
	void createQualitySpecification(QualityInspectionSpecifications specification);
	void updateQualitySpecification(QualityInspectionSpecifications specification);
	
	void inactiveQualityInspectionStandardSpecificationMapBySpecificationId(Long specificationId);
	
	List<QualityInspectionSpecifications> listAllChildrenSpecifications(String superiorPath, String ownerType, Long ownerId, Byte scopeCode, Long scopeId, Byte inspectionType);
	List<QualityInspectionSpecifications> listChildrenSpecifications(String ownerType, Long ownerId, Byte scopeCode, Long scopeId, Long parentId, Byte inspectionType);

	List<TaskCountDTO> countTasks(String ownerType, Long ownerId, String targetType, Long targetId, Long startTime, Long endTime, int offset, int count);
	ScoreDTO countScores(String ownerType, Long ownerId, String targetType, Long targetId, String superiorPath, Long startTime, Long endTime);
	
	void populateRecordItemResults(final List<QualityInspectionTaskRecords> records);
	void populateRecordItemResult(QualityInspectionTaskRecords record);
	void createSpecificationItemResults(QualityInspectionSpecificationItemResults result);

	Set<Long> listRecordsTaskIdByOperatorId(Long operatorId, Long maxTaskId);
	
	List<QualityInspectionTasks> listTaskByIds(List<Long> taskIds);
	
	void createQualityInspectionTaskTemplates(QualityInspectionTaskTemplates template);
	void updateQualityInspectionTaskTemplates(QualityInspectionTaskTemplates template);
	void deleteQualityInspectionTaskTemplates(Long templateId);
	List<QualityInspectionTaskTemplates> listQualityInspectionTaskTemplates(ListingLocator locator, int count, Long uid);
	
}
