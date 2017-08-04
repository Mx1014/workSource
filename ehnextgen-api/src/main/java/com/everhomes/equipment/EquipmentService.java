package com.everhomes.equipment;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.everhomes.rest.equipment.*;
import org.springframework.web.multipart.MultipartFile;

import com.everhomes.rest.category.CategoryDTO;
import com.everhomes.rest.organization.OrganizationDTO;
import com.everhomes.rest.user.admin.ImportDataResponse;

public interface EquipmentService {
	
	EquipmentStandardsDTO updateEquipmentStandard(UpdateEquipmentStandardCommand cmd);
	EquipmentStandardsDTO findEquipmentStandard(DeleteEquipmentStandardCommand cmd);
	void deleteEquipmentStandard(DeleteEquipmentStandardCommand cmd);
	HttpServletResponse exportEquipmentStandards(SearchEquipmentStandardsCommand cmd,HttpServletResponse response);
	void reviewEquipmentStandardRelations(ReviewEquipmentStandardRelationsCommand cmd);
	void deleteEquipmentStandardRelations(DeleteEquipmentStandardRelationsCommand cmd);
	void updateEquipments(UpdateEquipmentsCommand cmd);
	void deleteEquipments(DeleteEquipmentsCommand cmd);
	HttpServletResponse exportEquipments(SearchEquipmentsCommand cmd,HttpServletResponse response);
	EquipmentAccessoriesDTO updateEquipmentAccessories(UpdateEquipmentAccessoriesCommand cmd);
	void deleteEquipmentAccessories(DeleteEquipmentAccessoriesCommand cmd);
	HttpServletResponse exportEquipmentAccessories(SearchEquipmentAccessoriesCommand cmd,HttpServletResponse response);
	ListEquipmentTasksResponse listEquipmentTasks(ListEquipmentTasksCommand cmd);
	EquipmentTaskDTO reportEquipmentTask(ReportEquipmentTaskCommand cmd);
	void reviewEquipmentTask(ReviewEquipmentTaskCommand cmd);
	void reviewEquipmentTasks(ReviewEquipmentTasksCommand cmd);
	void createEquipmentTask(DeleteEquipmentsCommand equipmentId);
	VerifyEquipmentLocationResponse verifyEquipmentLocation(VerifyEquipmentLocationCommand cmd);
	HttpServletResponse exportEquipmentTasks(SearchEquipmentTasksCommand cmd,HttpServletResponse response);
	ListLogsByTaskIdResponse listLogsByTaskId(ListLogsByTaskIdCommand cmd);
	ImportDataResponse importEquipmentStandards(ImportOwnerCommand cmd, MultipartFile mfile, Long userId);
	ImportDataResponse importEquipments(ImportOwnerCommand cmd, MultipartFile mfile, Long userId);
	ImportDataResponse importEquipmentAccessories(ImportOwnerCommand cmd, MultipartFile mfile, Long userId);
	List<CategoryDTO> listEquipmentsCategories();
	EquipmentsDTO findEquipment(DeleteEquipmentsCommand cmd);
	List<EquipmentParameterDTO> listParametersByEquipmentId(DeleteEquipmentsCommand cmd);
	List<EquipmentAttachmentDTO> listAttachmentsByEquipmentId(ListAttachmentsByEquipmentIdCommand cmd);
	
	void creatTaskByStandard(EquipmentInspectionEquipments equipment, EquipmentInspectionStandards standard);
	List<OrganizationDTO> listRelatedOrgGroups(ListRelatedOrgGroupsCommand cmd);
	
	ListEquipmentTasksResponse listTasksByEquipmentId(ListTasksByEquipmentIdCommand cmd);
	EquipmentAccessoriesDTO findEquipmentAccessoriesById(DeleteEquipmentAccessoriesCommand cmd);
	EquipmentTaskDTO listTaskById(ListTaskByIdCommand cmd);
	
	void createEquipmentCategory(CreateEquipmentCategoryCommand cmd);
	void updateEquipmentCategory(UpdateEquipmentCategoryCommand cmd);
	void deleteEquipmentCategory(DeleteEquipmentCategoryCommand cmd);
	
	List<InspectionItemDTO> listParametersByStandardId(ListParametersByStandardIdCommand cmd);
	void createInspectionTemplate(CreateInspectionTemplateCommand cmd);
	void updateInspectionTemplate(UpdateInspectionTemplateCommand cmd);
	void deleteInspectionTemplate(DeleteInspectionTemplateCommand cmd);
	InspectionTemplateDTO findInspectionTemplate(DeleteInspectionTemplateCommand cmd);
	List<InspectionTemplateDTO> listInspectionTemplates(ListInspectionTemplatesCommand cmd);
	ListEquipmentTasksResponse listTasksByToken(ListTasksByTokenCommand cmd);
	
	List<EquipmentInspectionCategoryDTO> listEquipmentInspectionCategories(ListEquipmentInspectionCategoriesCommand cmd);
	EquipmentsDTO getInspectionObjectByQRCode(GetInspectionObjectByQRCodeCommand cmd);
	
	ListEquipmentTasksResponse listUserHistoryTasks(ListUserHistoryTasksCommand cmd);
	
	StatEquipmentTasksResponse statEquipmentTasks(StatEquipmentTasksCommand cmd);
	void sendTaskMsg(Long startTime, Long endTime, Byte groupType);
	void exportEquipmentsCard(ExportEquipmentsCardCommand cmd, HttpServletResponse response);

	StatTodayEquipmentTasksResponse statTodayEquipmentTasks(StatTodayEquipmentTasksCommand cmd);
	StatLastDaysEquipmentTasksResponse statLastDaysEquipmentTasks(StatLastDaysEquipmentTasksCommand cmd);
	StatIntervalAllEquipmentTasksResponse statIntervalAllEquipmentTasks(StatIntervalAllEquipmentTasksCommand cmd);
	StatItemResultsInEquipmentTasksResponse statItemResultsInEquipmentTasks(StatItemResultsInEquipmentTasksCommand cmd);
	ListEquipmentTasksResponse listAbnormalTasks(ListAbnormalTasksCommand cmd);
}
