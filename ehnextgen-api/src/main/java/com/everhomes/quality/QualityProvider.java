package com.everhomes.quality;

import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;
import com.everhomes.rest.quality.ExecuteGroupAndPosition;
import com.everhomes.rest.quality.ListQualityInspectionTasksResponse;
import com.everhomes.rest.quality.ScoreDTO;
import com.everhomes.rest.quality.TaskCountDTO;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.Set;


public interface QualityProvider {
	
	void createVerificationTasks(QualityInspectionTasks task);
	void updateVerificationTasks(QualityInspectionTasks task);
	void deleteVerificationTasks(Long taskId);
	QualityInspectionTasks findVerificationTaskById(Long taskId);
	List<QualityInspectionTasks> listVerificationTasks(Integer offset, int count, Long ownerId, String ownerType, Long targetId, String targetType,
    		Byte taskType, Long executeUid, Timestamp startDate, Timestamp endDate, Byte executeStatus, Byte reviewStatus, boolean timeCompared,
			List<Long> standardIds, Byte manualFlag, List<ExecuteGroupAndPosition> groupDtos,Integer namespaceId,String taskName,Timestamp latestUpdateTime);
	int countVerificationTasks(Long ownerId, String ownerType, Byte taskType, Long executeUid, 
			Timestamp startDate, Timestamp endDate, Long groupId, List<Byte> executeStatus, Byte reviewStatus);


	void createQualityInspectionStandards(QualityInspectionStandards standard);
	void updateQualityInspectionStandards(QualityInspectionStandards standard);
	QualityInspectionStandards findStandardById(Long id);
	List<QualityInspectionStandards> findStandardsByCategoryId(Long categoryId);
	List<QualityInspectionStandards> listQualityInspectionStandards(ListingLocator locator, int count, Long ownerId, String ownerType, String targetType, List<Long> targetIds, Byte reviewResult,String planCondition);
	
	void createQualityInspectionEvaluationFactors(QualityInspectionEvaluationFactors factor);
	void updateQualityInspectionEvaluationFactors(QualityInspectionEvaluationFactors factor);
	void deleteQualityInspectionEvaluationFactors(Long factorId);
	List<QualityInspectionEvaluationFactors> listQualityInspectionEvaluationFactors(ListingLocator locator, int count, Long ownerId, String ownerType);
	QualityInspectionEvaluationFactors findQualityInspectionFactorById(Long id);
	QualityInspectionEvaluationFactors findQualityInspectionFactorByGroupIdAndCategoryId(Long groupId, Long categoryId);
	
	void createQualityInspectionStandardGroupMap(QualityInspectionStandardGroupMap standardGroup);
	void deleteQualityInspectionStandardGroupMap(Long standardGroupId);
	void deleteQualityInspectionStandardGroupMapByStandardId(Long standardId);
	List<Long> listQualityInspectionStandardGroupMapByGroup(List<ExecuteGroupAndPosition> groupIds, Byte groupType);
	
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
	List<QualityInspectionTasks> listTodayQualityInspectionTasks(Long startTime, Long endTime);
	
	void populateTaskRecords(QualityInspectionTasks task);
	void populateTasksRecords(final List<QualityInspectionTasks> tasks);
	
	void closeDelayTasks();
	void closeTask(QualityInspectionTasks task);
	
	List<QualityInspectionStandards> listActiveStandards();

	
	void createQualityInspectionLogs(QualityInspectionLogs log);
	List<QualityInspectionLogs> listQualityInspectionLogs(String ownerType, Long ownerId, String targetType, Long targetId,List<Long> scopeId, ListingLocator locator, int count);
	
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
	List<QualityInspectionSpecifications> listAddAndModifyChildrenSpecifications(String ownerType, Long ownerId, Byte scopeCode, List<Long> scopeIds, Long parentId, Byte inspectionType);

	List<TaskCountDTO> countTasks(String ownerType, Long ownerId, String targetType, Long targetId, Long startTime, Long endTime, int offset, int count, Long sampleId);
	ScoreDTO countScores(String ownerType, Long ownerId, String targetType, Long targetId, String superiorPath, Long startTime, Long endTime, Long sampleId);
	
	void populateRecordItemResults(final List<QualityInspectionTaskRecords> records);
	void populateRecordItemResult(QualityInspectionTaskRecords record);
	void createSpecificationItemResults(QualityInspectionSpecificationItemResults result);

	Set<Long> listRecordsTaskIdByOperatorId(Long operatorId, Long maxTaskId);
	Set<Long> listRecordsTaskIdByOperatorId(Long operatorId, Timestamp beginTime, Long targetId);
	List<QualityInspectionTaskRecords> listRecordsByOperatorId(Long operatorId, Timestamp createTime);

	List<QualityInspectionTasks> listTaskByIds(List<Long> taskIds);
	List<QualityInspectionTasks> listTaskByIds(Set<Long> taskIds);
	List<QualityInspectionTasks> listTaskByParentId(Long parentId);

	void createQualityInspectionTaskTemplates(QualityInspectionTaskTemplates template);
	void updateQualityInspectionTaskTemplates(QualityInspectionTaskTemplates template);
	void deleteQualityInspectionTaskTemplates(Long templateId);
	QualityInspectionTaskTemplates findQualityInspectionTaskTemplateById(Long templateId);
	List<QualityInspectionTaskTemplates> listUserQualityInspectionTaskTemplates(ListingLocator locator, int count, Long uid);
	List<QualityInspectionStandardGroupMap> listQualityInspectionStandardGroupMapByGroupAndPosition(List<ExecuteGroupAndPosition> groupIds);
	List<QualityInspectionStandardGroupMap> listQualityInspectionStandardGroupMapByStandardIdAndGroupType(Long standardId, Byte groupType);

	QualityInspectionTasks findLastestQualityInspectionTask(Long startTime);
	QualityInspectionStandardSpecificationMap getMapByStandardId(Long standardId);

	void createQualityInspectionSample(QualityInspectionSamples sample);
	void createQualityInspectionSampleCommunityMap(QualityInspectionSampleCommunityMap map);
	void deleteQualityInspectionSampleCommunityMap(QualityInspectionSampleCommunityMap map);
	void deleteQualityInspectionSampleGroupMap(QualityInspectionSampleGroupMap map);
	void createQualityInspectionSampleGroupMap(QualityInspectionSampleGroupMap map);
	void updateQualityInspectionSample(QualityInspectionSamples sample);
	QualityInspectionSamples findQualityInspectionSample(Long id, String ownerType, Long ownerId);
	List<QualityInspectionSampleCommunityMap> findQualityInspectionSampleCommunityMapBySample(Long sampleId);
	List<QualityInspectionSampleGroupMap> findQualityInspectionSampleGroupMapBySample(Long sampleId);
	QualityInspectionSampleCommunityMap findQualityInspectionSampleCommunityMapBySampleAndCommunity(Long sampleId, Long communityId);
	QualityInspectionSampleGroupMap findQualityInspectionSampleGroupMapBySampleAndOrg(Long sampleId, Long organizationId, Long positionId);
	List<QualityInspectionSampleGroupMap> listQualityInspectionSampleGroupMapByOrgAndPosition(List<ExecuteGroupAndPosition> groupIds);

	List<QualityInspectionSamples> listActiveQualityInspectionSamples(ListingLocator locator, int count, String ownerType, Long ownerId, List<Long> sampleIds, Long communityId,Timestamp lastUpdateSyncTime);

	List<QualityInspectionSamples> listQualityInspectionSamples(CrossShardListingLocator locator, Integer pageSize);
	List<QualityInspectionTasks> listQualityInspectionTasks(CrossShardListingLocator locator, Integer pageSize);

	Integer getSampleCommunities(Long sampleId);

	void createQualityInspectionSampleScoreStat(QualityInspectionSampleScoreStat stat);
	void updateQualityInspectionSampleScoreStat(QualityInspectionSampleScoreStat stat);
	void createQualityInspectionSampleCommunitySpecificationStat(QualityInspectionSampleCommunitySpecificationStat stat);
	void updateQualityInspectionSampleCommunitySpecificationStat(QualityInspectionSampleCommunitySpecificationStat stat);
	QualityInspectionSampleScoreStat findQualityInspectionSampleScoreStat(Long sampleId);
	Map<Long, QualityInspectionSampleScoreStat> getQualityInspectionSampleScoreStat(List<Long> sampleIds);

	List<QualityInspectionSpecificationItemResults> listSpecifitionItemResultsBySampleId(Long sampleId);
	List<QualityInspectionSpecificationItemResults> listSpecifitionItemResultsBySampleId(Long sampleId, Timestamp startTime, Timestamp endTime);
	List<QualityInspectionTasks> listQualityInspectionTasksBySample(Long sampleId, Timestamp startTime, Timestamp endTime);
	Map<Long, List<QualityInspectionSampleCommunitySpecificationStat>> listCommunitySpecifitionStatBySampleId(List<Long> sampleIds);

	List<QualityInspectionSamples> listActiveQualityInspectionSamples(Timestamp lastStatTime);
	Map<Long, Double> listCommunityScore(Long sampleId);
	Map<Long, Double> listSpecificationScore(Long sampleId);

	List<QualityInspectionSampleCommunitySpecificationStat> listSampleCommunitySpecifitionStat(Long sampleId);
	QualityInspectionSampleCommunitySpecificationStat findBySampleCommunitySpecification(Long sampleId, Long communityId, Long specificationId);

	Map<Long, QualityInspectionSpecifications> listSpecificationByIds(List<Long> ids);
	Map<Long, QualityInspectionTaskRecords> listLastRecordByTaskIds(Set<Long> taskIds);

    void createQualityModelCommunityMap(QualityInspectionModelCommunityMap map);

	void deleteQualityModelCommunityMapByCommunityIdAndModelId(Long modelId, Long targetId ,byte modelType);

	List<QualityInspectionModelCommunityMap> listQualityModelCommunityMapByTargetId(Long targetId ,byte modelType);

	List<Long> listQualityModelCommunityIdsMapByModelId(Long standard ,byte modelType);

	List<QualityInspectionSpecifications> listAllCommunitiesChildrenSpecifications(String superiorPath, String ownerType, Long ownerId, List<Long> scopeIds,Byte inspectionType);

    void deleteQualityModelCommunityMapByModelId(Long modelId , byte modelType);

	List<QualityInspectionSpecifications>  listSpecifitionByParentIds(List<Long> parentIds);

    List<QualityInspectionSpecifications> listDeletedSpecifications(Long communityId, Long ownerId, String ownerType, Timestamp lastUpdateSyncTime);

    List<QualityInspectionTasks> listVerificationTasksRefactor(Integer offset, int pageSize,
															   Timestamp startDate, Timestamp endDate,
															   List<Long> executeStandardIds, List<Long> reviewStandardIds,
															   List<ExecuteGroupAndPosition> groupDtos, ListingQueryBuilderCallback builderCallback);

	void getTodayTaskCountStat(ListQualityInspectionTasksResponse response,List<Long> executeStandardIds, List<Long> reviewStandardIds,
							   List<ExecuteGroupAndPosition> groupDtos, Timestamp todayBegin,ListingQueryBuilderCallback builderCallback);

	List<QualityInspectionStandardGroupMap> listPlanGroupMapsByPlanId(Long standardId);
}
