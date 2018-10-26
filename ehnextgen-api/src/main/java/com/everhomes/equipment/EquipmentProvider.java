package com.everhomes.equipment;

import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.listing.ListingLocator;
import com.everhomes.listing.ListingQueryBuilderCallback;
import com.everhomes.rest.equipment.ExecuteGroupAndPosition;
import com.everhomes.rest.equipment.ItemResultStat;
import com.everhomes.rest.equipment.ListEquipmentTasksResponse;
import com.everhomes.rest.equipment.ReviewedTaskStat;
import com.everhomes.rest.equipment.StandardAndStatus;
import com.everhomes.rest.equipment.StatTodayEquipmentTasksCommand;
import com.everhomes.rest.equipment.TaskCountDTO;
import com.everhomes.rest.equipment.TasksStatData;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface EquipmentProvider {
	
	void creatEquipmentStandard(EquipmentInspectionStandards standard);
	void updateEquipmentStandard(EquipmentInspectionStandards standard);
	void creatEquipmentInspectionEquipment(EquipmentInspectionEquipments equipment);
	void updateEquipmentInspectionEquipment(EquipmentInspectionEquipments equipment);
	void creatEquipmentInspectionAccessories(EquipmentInspectionAccessories accessory);
	void updateEquipmentInspectionAccessories(EquipmentInspectionAccessories accessory);
	
	EquipmentInspectionStandards findStandardById(Long id, String ownerType, Long ownerId);
	EquipmentInspectionStandards findStandardById(Long id);
	EquipmentInspectionEquipments findEquipmentById(Long id, String ownerType, Long ownerId);
	EquipmentInspectionEquipments findEquipmentById(Long id);
	EquipmentInspectionEquipments findEquipmentById(Long id, Integer namespaceId);
	EquipmentInspectionAccessories findAccessoryById(Long id, String ownerType, Long ownerId);
	EquipmentInspectionAccessories findAccessoryById(Long id);
	EquipmentInspectionTasks findEquipmentTaskById(Long id, String ownerType, Long ownerId);
	EquipmentInspectionTasks findEquipmentTaskById(Long id);
	
	void creatEquipmentParameter(EquipmentInspectionEquipmentParameters parameter);
	void updateEquipmentParameter(EquipmentInspectionEquipmentParameters parameter);
	void creatEquipmentAccessoryMap(EquipmentInspectionAccessoryMap map);
	void updateEquipmentAccessoryMap(EquipmentInspectionAccessoryMap map);
	void creatEquipmentAttachment(EquipmentInspectionEquipmentAttachments eqAttachment);
	void deleteEquipmentAttachmentById(Long id);
	void deleteEquipmentAccessoryMapByEquipmentId(Long equipmentId);
	List<EquipmentInspectionEquipmentAttachments> findEquipmentAttachmentsByEquipmentId(Long equipmentId);

	List<EquipmentStandardMap> findByStandardId(Long standardId);
	List<EquipmentStandardMap> findByTarget(Long targetId, String targetType);
	
	void creatEquipmentTask(EquipmentInspectionTasks task);
	void updateEquipmentTask(EquipmentInspectionTasks task);
	
	void createEquipmentInspectionTasksLogs(EquipmentInspectionTasksLogs log);
	List<EquipmentInspectionTasksLogs> listLogsByTaskId(ListingLocator locator, int count, Long taskId, List<Byte> processType,List<Long> equipmentId);
	void createEquipmentInspectionTasksAttachment(EquipmentInspectionTasksAttachments attachment);
	List<EquipmentInspectionTasksAttachments> listTaskAttachmentsByLogId(Long logId);
	
	List<EquipmentInspectionStandards> listEquipmentInspectionStandards(CrossShardListingLocator locator, Integer pageSize);
	List<EquipmentInspectionAccessories> listEquipmentInspectionAccessories(CrossShardListingLocator locator, Integer pageSize);
	List<EquipmentInspectionEquipments> listEquipments(CrossShardListingLocator locator, Integer pageSize);
	List<EquipmentInspectionTasks> listEquipmentInspectionTasks(CrossShardListingLocator locator, Integer pageSize);
	List<EquipmentInspectionTasks> listEquipmentInspectionTasks(String ownerType, Long ownerId, Long inspectionCategoryId,
			List<String> targetType, List<Long> targetId, List<Long> executeStandardIds, List<Long> reviewStandardIds, Integer offset, Integer pageSize);
	List<EquipmentInspectionTasks> listEquipmentInspectionReviewTasks(String ownerType, Long ownerId, Long inspectionCategoryId,
			List<String> targetType, List<Long> targetId, List<Long> standardIds, Integer offset, Integer pageSize);
	
	List<EquipmentInspectionEquipmentParameters> listParametersByEquipmentId(Long equipmentId);
	List<EquipmentInspectionEquipmentAttachments> listAttachmentsByEquipmentId(Long equipmentId, Byte attachmentType);
	
	List<EquipmentInspectionEquipments> listQualifiedEquipmentStandardEquipments();
	
	List<EquipmentInspectionTasks> listTasksByEquipmentId(Long equipmentId, List<Long> standardIds, Timestamp startDate, Timestamp endDate, CrossShardListingLocator locator, Integer pageSize, List<Byte> taskStatus);
	List<EquipmentInspectionTasks> listTasksByEquipmentIdAndStandards(List<StandardAndStatus> standards, Timestamp startDate, Timestamp endDate, CrossShardListingLocator locator, Integer pageSize);
	List<EquipmentInspectionTasks> listTasksByStandardId(Long standardId, CrossShardListingLocator locator, Integer pageSize);

	List<Long> listStandardIdsByType(Byte type);
	
	List<EquipmentInspectionAccessoryMap> listAccessoryMapByEquipmentId(Long equipmentId);
	
	EquipmentInspectionTasksLogs getNearestReviewLogAfterProcess(Long taskId, Long id);
	
	Long createEquipmentInspectionTemplates(EquipmentInspectionTemplates template);
	void updateEquipmentInspectionTemplates(EquipmentInspectionTemplates template);
	EquipmentInspectionTemplates findEquipmentInspectionTemplate(Long id, Long ownerId, String ownerType);

	Long createEquipmentInspectionItems(EquipmentInspectionItems item);
	Long updateEquipmentInspectionItems(EquipmentInspectionItems item);
	EquipmentInspectionItems findEquipmentInspectionItem(Long id);
	
	void createEquipmentInspectionTemplateItemMap(EquipmentInspectionTemplateItemMap map);
	void deleteEquipmentInspectionTemplateItemMap(Long id);
	EquipmentInspectionTemplateItemMap findEquipmentInspectionTemplateItemMap(Long id);
	List<EquipmentInspectionTemplateItemMap> listEquipmentInspectionTemplateItemMap(Long templateId);
	List<EquipmentInspectionTemplates> listInspectionTemplates(Long ownerId, String ownerType, String name);
	List<EquipmentInspectionTemplates> listInspectionTemplates(Integer namespaceId, String name ,Long  targetId);
	List<EquipmentInspectionStandards> listEquipmentInspectionStandardsByTemplateId(Long templateId);
	
	void createEquipmentStandardMap(EquipmentStandardMap map);
	void updateEquipmentStandardMap(EquipmentStandardMap map);
	EquipmentStandardMap findEquipmentStandardMapById(Long id);
	EquipmentStandardMap findEquipmentStandardMap(Long id, Long standardId, Long targetId, String targetType);
	List<EquipmentStandardMap> findEquipmentStandardMap( Long standardId, Long targetId, String targetType);

	void createEquipmentInspectionItemResults(EquipmentInspectionItemResults result);
	List<EquipmentInspectionItemResults> findEquipmentInspectionItemResultsByLogId(Long logId);
	
	List<EquipmentStandardMap> listQualifiedEquipmentStandardMap(Long equipmentId);

	List<EquipmentStandardMap> listEquipmentStandardMap(CrossShardListingLocator locator, Integer pageSize);
	
	void closeDelayTasks();
	void closeExpiredReviewTasks();
	void closeReviewTasks(EquipmentInspectionTasks task);
	void closeTask(EquipmentInspectionTasks task);
	
	EquipmentInspectionEquipments findEquipmentByQrCodeToken(String qrCodeToken);
	
	List<EquipmentInspectionCategories> listEquipmentInspectionCategories(Long ownerId, Integer namespaceId);
	
	Set<Long> listRecordsTaskIdByOperatorId(Long uId, Long pageAnchor);
	
	List<TaskCountDTO> statEquipmentTasks( Long targetId, String targetType,
			Long inspectionCategoryId, Long startTime, Long endTime, Integer offset, Integer pageSize);
	
	void createEquipmentInspectionStandardGroupMap(EquipmentInspectionStandardGroupMap standardGroup);
	void deleteEquipmentInspectionStandardGroupMap(Long standardGroupId);
	void deleteEquipmentInspectionStandardGroupMapByStandardId(Long standardId);
	List<Long> listEquipmentInspectionStandardGroupMapByGroup(List<Long> groupIds, Byte groupType);
	List<EquipmentInspectionStandardGroupMap> listEquipmentInspectionStandardGroupMapByGroupAndPosition(List<ExecuteGroupAndPosition> reviewGroups, Byte groupType );
	List<EquipmentInspectionStandardGroupMap> listEquipmentInspectionStandardGroupMapByStandardIdAndGroupType(Long standardId, Byte groupType);
//	void populateStandardsGroups(final List<EquipmentInspectionStandards> standards);
//	void populateStandardGroups(EquipmentInspectionStandards standard);
	void populatePlansGroups(final List<EquipmentInspectionPlans> plans);
	void populatePlanGroups(EquipmentInspectionPlans plan);

	List<EquipmentInspectionTasks> listTodayEquipmentInspectionTasks(Long startTime, Long endTime, Byte groupType);
	EquipmentInspectionTasks findLastestEquipmentInspectionTask(Long startTime);


	List<EquipmentInspectionTasks> listEquipmentInspectionTasksUseCache(List<Byte> taskStatus, Long inspectionCategoryId,
		List<String> targetType, List<Long> targetId, List<Long> executeStandardIds, List<Long> reviewStandardIds, Long offset, Integer pageSize, String cacheKey, Byte adminFlag,Timestamp lastSyncTime);


	Map<Long, EquipmentInspectionEquipments> listEquipmentsById(Set<Long> ids);
	List<EquipmentInspectionEquipments> listEquipmentsById(List<Long> ids);

	List<EquipmentInspectionTasks> listTaskByIds(List<Long> ids);

	TasksStatData statDaysEquipmentTasks(Long targetId, String targetType, Long inspectionCategoryId, Timestamp startTime, Timestamp endTime,Integer namespaceId);
	ReviewedTaskStat statDaysReviewedTasks(Long communityId, Long inspectionCategoryId, Timestamp startTime, Timestamp endTime,Integer namespaceId);
	List<ItemResultStat> statItemResults(Long equipmentId, Long standardId, Timestamp startTime, Timestamp endTime);

	List<EquipmentInspectionTasks> listDelayTasks(Long inspectionCategoryId, List<Long> planIds, String targetType, Long targetId, Integer offset, Integer pageSize, Byte adminFlag, Timestamp startTime);

	void createEquipmentModelCommunityMap(EquipmentModelCommunityMap map);

	List<EquipmentModelCommunityMap> listModelCommunityMapByCommunityId(Long targetId, byte modelType);

	void deleteModelCommunityMapByModelIdAndCommunityId(Long modelId, Long targetId, byte modelType);

	List<Integer> listDistinctNameSpace();

	List<Long> listModelCommunityMapByModelId(Long modelId, byte modelType);

	void deleteModelCommunityMapByModelId(Long modelId, byte modelType);


	//add inactiveEquipmentStandardMap
    void inActiveEquipmentStandardMap(EquipmentStandardMap equipmentStandardMap);

    void populateEquipments(EquipmentInspectionStandards standard);

	void populateItems(EquipmentInspectionStandards standard);

    EquipmentInspectionPlans createEquipmentInspectionPlans(EquipmentInspectionPlans plan);

    EquipmentInspectionPlans getEquipmmentInspectionPlanById(Long planId);

	void createEquipmentPlanMaps(EquipmentInspectionEquipmentPlanMap map);

	List<EquipmentInspectionEquipmentPlanMap> getEquipmentInspectionPlanMap(Long planId);

    void deleteEquipmentInspectionPlanById(Long planId);

	void deleteEquipmentInspectionPlanMap(Long id);

	List<EquipmentInspectionPlans> ListEquipmentInspectionPlans(ListingLocator locator, int pageSize);

    void updateEquipmentInspectionPlan(EquipmentInspectionPlans plan);

    void deleteEquipmentInspectionStandardMapByStandardId(Long  deleteId);

	void deleteEquipmentPlansMapByStandardId(Long standardId);

    List<EquipmentInspectionPlans> listQualifiedEquipmentInspectionPlans();

	List<EquipmentInspectionEquipmentPlanMap> listPlanMapByEquipmentId(Long equipmentId);

	List<EquipmentInspectionTasks> listTaskByPlanMaps(List<Long> planIds, Timestamp startTime, Timestamp endTime, ListingLocator locator,int pageSize,List<Byte> taskStatus);

    void deleteEquipmentInspectionPlanGroupMapByPlanId(Long id);

	void createEquipmentInspectionPlanGroupMap(EquipmentInspectionPlanGroupMap map);

    List<EquipmentInspectionPlanGroupMap> listEquipmentInspectionPlanGroupMapByPlanIdAndGroupType(Long planId, byte groupType);

    List<EquipmentInspectionPlanGroupMap> listEquipmentInspectionPlanGroupMapByGroupAndPosition(List<ExecuteGroupAndPosition> groupDtos, Byte groupType);

	List<EquipmentInspectionTasks> listTasksByPlanId(Long planId, CrossShardListingLocator locator, int pageSize);

	List<Long> listNotifyRecordByPlanId(Long planId, CrossShardListingLocator locator, int pageSize);

    List<EquipmentInspectionStandardGroupMap> listEquipmentInspectionStandardGroupMapByStandardId(Long id);

    List<EquipmentStandardMap> listAllActiveEquipmentStandardMap();

    void createReviewExpireDays(EquipmentInspectionReviewDate reviewDate);

	void updateReviewExpireDays(EquipmentInspectionReviewDate reviewDate);

	void deleteReviewExpireDaysByScope(Byte scopeType, Long scopeId, Long targetId, String targetType);

	void deleteReviewExpireDaysByReferId(Long id);

	EquipmentInspectionReviewDate getEquipmentInspectiomExpireDaysById(Long id);

	List<EquipmentInspectionReviewDate> getEquipmentInspectiomExpireDays(Long scopeId, Byte scopeType,Long targetId,String targetType);

    void deleteEquipmentPlansMapByEquipmentId(Long equipmentId);

    EquipmentInspectionTasksLogs getMaintanceLogByEquipmentId(Long referId,Long pmTaskId);

	void updateMaintanceInspectionLogsById(Long taskLogId ,Byte status ,Long flowCaseId);

    void statInMaintanceTaskCount(TasksStatData stat,Timestamp startTime, Timestamp endTime,StatTodayEquipmentTasksCommand cmd);

	void updateEquipmentStatus(Long equipmentId, Byte status);

	void createEquipmentOperateLogs(EquipmentInspectionEquipmentLogs log);

	List<EquipmentInspectionEquipmentLogs> listEquipmentOperateLogsByTargetId(Long equipmentId);

    void populateTodayTaskStatusCount(List<Long> executePlanIds, List<Long> reviewPlanIds, Byte  adminFlag, ListEquipmentTasksResponse response, ListingQueryBuilderCallback queryBuilderCallback);

    void populateReviewTaskStatusCount(List<Long> executePlanIds, List<Long> reviewPlanIds, Byte  adminFlag, ListEquipmentTasksResponse response, ListingQueryBuilderCallback queryBuilderCallback);

    List<EquipmentInspectionStandards> listEquipmentStandardWithReferId(Long targetId, String targetType);

    void deletePlanMapByEquipmentIdAndStandardId(Long equipmentId, Long standardId);

    List<EquipmentInspectionEquipmentPlanMap> listEquipmentPlanMaps();

	void transferPlanIdForTasks(Long equipmentId, Long standardId,Long planId);

	void batchUpdateUnusedTaskStatus();

    void updateEquipmentTaskByPlanId(Long planId);

    List<EquipmentInspectionTasks> listPersonalDoneTasks(Long targetId, Long inspectionCategoryId, int pageSize, Integer offset, Timestamp startTime);


}
