package com.everhomes.equipment;

import java.util.List;

import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.listing.ListingLocator;


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
	
	List<EquipmentInspectionEquipments> findEquipmentByStandardId(Long standardId);
	
	void creatEquipmentTask(EquipmentInspectionTasks task);
	void updateEquipmentTask(EquipmentInspectionTasks task);
	
	void createEquipmentInspectionTasksLogs(EquipmentInspectionTasksLogs log);
	List<EquipmentInspectionTasksLogs> listLogsByTaskId(ListingLocator locator, int count, Long taskId, Byte processType);
	void createEquipmentInspectionTasksAttachment(EquipmentInspectionTasksAttachments attachment);
	
	List<EquipmentInspectionStandards> listEquipmentInspectionStandards(CrossShardListingLocator locator, Integer pageSize);
	List<EquipmentInspectionAccessories> listEquipmentInspectionAccessories(CrossShardListingLocator locator, Integer pageSize);
	List<EquipmentInspectionEquipments> listEquipments(CrossShardListingLocator locator, Integer pageSize);
	List<EquipmentInspectionTasks> listEquipmentInspectionTasks(CrossShardListingLocator locator, Integer pageSize);
	List<EquipmentInspectionTasks> listEquipmentInspectionTasks(String ownerType, Long ownerId, 
			List<String> targetType, List<Long> targetId, CrossShardListingLocator locator, Integer pageSize);
	
	List<EquipmentInspectionEquipmentParameters> listParametersByEquipmentId(Long equipmentId);
	List<EquipmentInspectionEquipmentAttachments> listAttachmentsByEquipmentId(Long equipmentId, Byte attachmentType);
	
	List<EquipmentInspectionEquipments> listQualifiedEquipmentStandardEquipments();
	
	List<EquipmentInspectionTasks> listTasksByEquipmentId(Long equipmentId, List<Long> standardIds, CrossShardListingLocator locator, Integer pageSize);

	List<Long> listStandardIdsByType(Byte type);
	
	List<EquipmentInspectionAccessoryMap> listAccessoryMapByEquipmentId(Long equipmentId);
}
