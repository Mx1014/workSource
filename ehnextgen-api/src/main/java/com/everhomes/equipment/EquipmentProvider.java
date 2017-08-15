package com.everhomes.equipment;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.Set;




import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.listing.ListingLocator;
import com.everhomes.quality.QualityInspectionStandards;
import com.everhomes.rest.equipment.*;


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
	List<EquipmentInspectionEquipmentAttachments> findEquipmentAttachmentsByEquipmentId(Long equipmentId);

	List<EquipmentStandardMap> findByStandardId(Long standardId);
	List<EquipmentStandardMap> findByTarget(Long targetId, String targetType);
	
	void creatEquipmentTask(EquipmentInspectionTasks task);
	void updateEquipmentTask(EquipmentInspectionTasks task);
	
	void createEquipmentInspectionTasksLogs(EquipmentInspectionTasksLogs log);
	List<EquipmentInspectionTasksLogs> listLogsByTaskId(ListingLocator locator, int count, Long taskId, List<Byte> processType);
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
	List<EquipmentInspectionTemplates> listInspectionTemplates(Integer namespaceId, String name);
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
	
	List<TaskCountDTO> statEquipmentTasks(Long ownerId, String ownerType, Long targetId, String targetType, 
			Long inspectionCategoryId, Long startTime, Long endTime, Integer offset, Integer pageSize);
	
	void createEquipmentInspectionStandardGroupMap(EquipmentInspectionStandardGroupMap standardGroup);
	void deleteEquipmentInspectionStandardGroupMap(Long standardGroupId);
	void deleteEquipmentInspectionStandardGroupMapByStandardId(Long standardId);
	List<Long> listEquipmentInspectionStandardGroupMapByGroup(List<Long> groupIds, Byte groupType);
	List<EquipmentInspectionStandardGroupMap> listEquipmentInspectionStandardGroupMapByGroupAndPosition(List<ExecuteGroupAndPosition> reviewGroups, Byte groupType );
	List<EquipmentInspectionStandardGroupMap> listEquipmentInspectionStandardGroupMapByStandardIdAndGroupType(Long standardId, Byte groupType);
	void populateStandardsGroups(final List<EquipmentInspectionStandards> standards);
	void populateStandardGroups(EquipmentInspectionStandards standard);

	List<EquipmentInspectionTasks> listTodayEquipmentInspectionTasks(Long startTime, Long endTime, Byte groupType);
	EquipmentInspectionTasks findLastestEquipmentInspectionTask(Long startTime);


	List<EquipmentInspectionTasks> listEquipmentInspectionTasksUseCache(List<Byte> taskStatus, Long inspectionCategoryId,
		List<String> targetType, List<Long> targetId, List<Long> executeStandardIds, List<Long> reviewStandardIds, Integer offset, Integer pageSize, String cacheKey, Byte adminFlag);


	Map<Long, EquipmentInspectionEquipments> listEquipmentsById(Set<Long> ids);
	List<EquipmentInspectionEquipments> listEquipmentsById(List<Long> ids);

	List<EquipmentInspectionTasks> listTaskByIds(List<Long> ids);

	TasksStatData statDaysEquipmentTasks(Long targetId, String targetType, Long inspectionCategoryId, Timestamp startTime, Timestamp endTime);
	ReviewedTaskStat statDaysReviewedTasks(Long communityId, Long inspectionCategoryId, Timestamp startTime, Timestamp endTime);
	List<ItemResultStat> statItemResults(Long equipmentId, Long standardId, Timestamp startTime, Timestamp endTime);
}
