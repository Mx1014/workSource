package com.everhomes.equipment;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.everhomes.rest.category.CategoryDTO;
import com.everhomes.rest.equipment.CreatEquipmentStandardCommand;
import com.everhomes.rest.equipment.DeleteEquipmentAccessoriesCommand;
import com.everhomes.rest.equipment.DeleteEquipmentStandardCommand;
import com.everhomes.rest.equipment.DeleteEquipmentStandardRelationsCommand;
import com.everhomes.rest.equipment.DeleteEquipmentsCommand;
import com.everhomes.rest.equipment.EquipmentAccessoriesDTO;
import com.everhomes.rest.equipment.EquipmentStandardsDTO;
import com.everhomes.rest.equipment.EquipmentTaskDTO;
import com.everhomes.rest.equipment.EquipmentsDTO;
import com.everhomes.rest.equipment.ListEquipmentTasksCommand;
import com.everhomes.rest.equipment.SearchEquipmentAccessoriesCommand;
import com.everhomes.rest.equipment.SearchEquipmentAccessoriesResponse;
import com.everhomes.rest.equipment.SearchEquipmentStandardRelationsCommand;
import com.everhomes.rest.equipment.SearchEquipmentStandardRelationsResponse;
import com.everhomes.rest.equipment.SearchEquipmentStandardsCommand;
import com.everhomes.rest.equipment.SearchEquipmentStandardsResponse;
import com.everhomes.rest.equipment.SearchEquipmentTasksCommand;
import com.everhomes.rest.equipment.ListEquipmentTasksResponse;
import com.everhomes.rest.equipment.SearchEquipmentsCommand;
import com.everhomes.rest.equipment.SearchEquipmentsResponse;
import com.everhomes.rest.equipment.ListLogsByTaskIdCommand;
import com.everhomes.rest.equipment.ListLogsByTaskIdResponse;
import com.everhomes.rest.equipment.ReportEquipmentTaskCommand;
import com.everhomes.rest.equipment.ReviewEquipmentStandardRelationsCommand;
import com.everhomes.rest.equipment.ReviewEquipmentTaskCommand;
import com.everhomes.rest.equipment.UpdateEquipmentAccessoriesCommand;
import com.everhomes.rest.equipment.UpdateEquipmentStandardCommand;
import com.everhomes.rest.equipment.UpdateEquipmentsCommand;
import com.everhomes.rest.equipment.VerifyEquipmentLocationCommand;
import com.everhomes.rest.user.admin.ImportDataResponse;

@Component
public class EquipmentServiceImpl implements EquipmentService {

	@Override
	public EquipmentStandardsDTO creatEquipmentStandard(
			CreatEquipmentStandardCommand cmd) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public EquipmentStandardsDTO updateEquipmentStandard(
			UpdateEquipmentStandardCommand cmd) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteEquipmentStandard(DeleteEquipmentStandardCommand cmd) {
		// TODO Auto-generated method stub

	}

	@Override
	public SearchEquipmentStandardsResponse searchEquipmentStandards(
			SearchEquipmentStandardsCommand cmd) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HttpServletResponse exportEquipmentStandards(
			SearchEquipmentStandardsCommand cmd, HttpServletResponse response) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public EquipmentStandardsDTO findEquipmentStandard(Long standardId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SearchEquipmentStandardRelationsResponse searchEquipmentStandardRelations(
			SearchEquipmentStandardRelationsCommand cmd) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void reviewEquipmentStandardRelations(
			ReviewEquipmentStandardRelationsCommand cmd) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteEquipmentStandardRelations(
			DeleteEquipmentStandardRelationsCommand cmd) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public EquipmentsDTO updateEquipments(UpdateEquipmentsCommand cmd) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteEquipments(DeleteEquipmentsCommand cmd) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public SearchEquipmentsResponse searchEquipments(SearchEquipmentsCommand cmd) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HttpServletResponse exportEquipments(SearchEquipmentsCommand cmd,
			HttpServletResponse response) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public EquipmentAccessoriesDTO updateEquipmentAccessories(
			UpdateEquipmentAccessoriesCommand cmd) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteEquipmentAccessories(DeleteEquipmentAccessoriesCommand cmd) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public SearchEquipmentAccessoriesResponse searchEquipmentAccessories(
			SearchEquipmentAccessoriesCommand cmd) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HttpServletResponse exportEquipmentAccessories(
			SearchEquipmentAccessoriesCommand cmd, HttpServletResponse response) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public EquipmentTaskDTO reportEquipmentTask(ReportEquipmentTaskCommand cmd) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void reviewEquipmentTask(ReviewEquipmentTaskCommand cmd) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void createEquipmentTaskByStandard(Long standardId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void verifyEquipmentLocation(VerifyEquipmentLocationCommand cmd) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public HttpServletResponse exportEquipmentTasks(
			SearchEquipmentTasksCommand cmd, HttpServletResponse response) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ListLogsByTaskIdResponse listLogsByTaskId(
			ListLogsByTaskIdCommand cmd) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ImportDataResponse importEquipmentStandards(MultipartFile mfile,
			Long userId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ImportDataResponse importEquipments(MultipartFile mfile, Long userId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ImportDataResponse importEquipmentAccessories(MultipartFile mfile,
			Long userId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<CategoryDTO> listEquipmentsCategories() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ListEquipmentTasksResponse searchEquipmentTasks(
			SearchEquipmentTasksCommand cmd) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ListEquipmentTasksResponse listEquipmentTasks(
			ListEquipmentTasksCommand cmd) {
		// TODO Auto-generated method stub
		return null;
	}

}
