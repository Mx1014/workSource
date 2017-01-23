package com.everhomes.equipment;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.multipart.MultipartFile;

import com.everhomes.rest.category.CategoryDTO;
import com.everhomes.rest.equipment.CreatEquipmentStandardCommand;
import com.everhomes.rest.equipment.CreateEquipmentCategoryCommand;
import com.everhomes.rest.equipment.CreateInspectionTemplateCommand;
import com.everhomes.rest.equipment.DeleteEquipmentAccessoriesCommand;
import com.everhomes.rest.equipment.DeleteEquipmentCategoryCommand;
import com.everhomes.rest.equipment.DeleteInspectionTemplateCommand;
import com.everhomes.rest.equipment.EquipmentAccessoriesDTO;
import com.everhomes.rest.equipment.EquipmentAttachmentDTO;
import com.everhomes.rest.equipment.EquipmentInspectionCategoryDTO;
import com.everhomes.rest.equipment.EquipmentParameterDTO;
import com.everhomes.rest.equipment.EquipmentTaskDTO;
import com.everhomes.rest.equipment.GetInspectionObjectByQRCodeCommand;
import com.everhomes.rest.equipment.ImportOwnerCommand;
import com.everhomes.rest.equipment.InspectionItemDTO;
import com.everhomes.rest.equipment.InspectionTemplateDTO;
import com.everhomes.rest.equipment.ListAttachmentsByEquipmentIdCommand;
import com.everhomes.rest.equipment.ListEquipmentInspectionCategoriesCommand;
import com.everhomes.rest.equipment.ListEquipmentTasksCommand;
import com.everhomes.rest.equipment.ListInspectionTemplatesCommand;
import com.everhomes.rest.equipment.ListParametersByStandardIdCommand;
import com.everhomes.rest.equipment.ListRelatedOrgGroupsCommand;
import com.everhomes.rest.equipment.ListTaskByIdCommand;
import com.everhomes.rest.equipment.ListTasksByEquipmentIdCommand;
import com.everhomes.rest.equipment.ListTasksByTokenCommand;
import com.everhomes.rest.equipment.ListUserHistoryTasksCommand;
import com.everhomes.rest.equipment.SearchEquipmentAccessoriesCommand;
import com.everhomes.rest.equipment.SearchEquipmentAccessoriesResponse;
import com.everhomes.rest.equipment.SearchEquipmentTasksCommand;
import com.everhomes.rest.equipment.ListEquipmentTasksResponse;
import com.everhomes.rest.equipment.SearchEquipmentsCommand;
import com.everhomes.rest.equipment.SearchEquipmentsResponse;
import com.everhomes.rest.equipment.ListLogsByTaskIdCommand;
import com.everhomes.rest.equipment.ListLogsByTaskIdResponse;
import com.everhomes.rest.equipment.ReportEquipmentTaskCommand;
import com.everhomes.rest.equipment.ReviewEquipmentTaskCommand;
import com.everhomes.rest.equipment.StatEquipmentTasksCommand;
import com.everhomes.rest.equipment.StatEquipmentTasksResponse;
import com.everhomes.rest.equipment.UpdateEquipmentAccessoriesCommand;
import com.everhomes.rest.equipment.UpdateEquipmentCategoryCommand;
import com.everhomes.rest.equipment.UpdateEquipmentStandardCommand;
import com.everhomes.rest.equipment.UpdateEquipmentsCommand;
import com.everhomes.rest.equipment.DeleteEquipmentStandardCommand;
import com.everhomes.rest.equipment.DeleteEquipmentStandardRelationsCommand;
import com.everhomes.rest.equipment.DeleteEquipmentsCommand;
import com.everhomes.rest.equipment.EquipmentStandardsDTO;
import com.everhomes.rest.equipment.EquipmentsDTO;
import com.everhomes.rest.equipment.SearchEquipmentStandardRelationsCommand;
import com.everhomes.rest.equipment.SearchEquipmentStandardRelationsResponse;
import com.everhomes.rest.equipment.SearchEquipmentStandardsCommand;
import com.everhomes.rest.equipment.SearchEquipmentStandardsResponse;
import com.everhomes.rest.equipment.ReviewEquipmentStandardRelationsCommand;
import com.everhomes.rest.equipment.UpdateInspectionTemplateCommand;
import com.everhomes.rest.equipment.VerifyEquipmentLocationCommand;
import com.everhomes.rest.equipment.VerifyEquipmentLocationResponse;
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
}
