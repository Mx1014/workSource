package com.everhomes.quality;

import java.sql.Timestamp;
import java.util.List;

import javax.validation.constraints.NotNull;

import com.everhomes.listing.ListingLocator;
import com.everhomes.rest.quality.QualityInspectionLogDTO;


public interface QualityProvider {
	
	void createVerificationTasks(QualityInspectionTasks task);
	void updateVerificationTasks(QualityInspectionTasks task);
	void deleteVerificationTasks(Long taskId);
	QualityInspectionTasks findVerificationTaskById(Long taskId);
	List<QualityInspectionTasks> listVerificationTasks(ListingLocator locator, int count, Long ownerId, String ownerType, 
    		Byte taskType, Long executeUid, Timestamp startDate, Timestamp endDate, Long groupId, 
    		Byte executeStatus, Byte reviewStatus, boolean timeCompared, List<Long> standardIds, Byte manualFlag);
	int countVerificationTasks(Long ownerId, String ownerType, Byte taskType, Long executeUid, 
			Timestamp startDate, Timestamp endDate, Long groupId, Byte executeStatus, Byte reviewStatus);


	void createQualityInspectionStandards(QualityInspectionStandards standard);
	void updateQualityInspectionStandards(QualityInspectionStandards standard);
	QualityInspectionStandards findStandardById(Long id);
	List<QualityInspectionStandards> findStandardsByCategoryId(Long categoryId);
	List<QualityInspectionStandards> listQualityInspectionStandards(ListingLocator locator, int count, Long ownerId, String ownerType);
	
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
}
