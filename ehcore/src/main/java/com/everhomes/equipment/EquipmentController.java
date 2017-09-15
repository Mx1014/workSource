package com.everhomes.equipment;


import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import com.everhomes.rest.equipment.*;
import com.everhomes.rest.pmNotify.DeletePmNotifyParamsCommand;
import com.everhomes.rest.pmNotify.ListPmNotifyParamsCommand;
import com.everhomes.rest.pmNotify.PmNotifyParamDTO;
import com.everhomes.rest.pmNotify.SetPmNotifyParamsCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.category.CategoryDTO;
import com.everhomes.rest.organization.OrganizationDTO;
import com.everhomes.rest.user.UserServiceErrorCode;
import com.everhomes.rest.user.admin.ImportDataResponse;
import com.everhomes.search.EquipmentAccessoriesSearcher;
import com.everhomes.search.EquipmentSearcher;
import com.everhomes.search.EquipmentStandardMapSearcher;
import com.everhomes.search.EquipmentStandardSearcher;
import com.everhomes.search.EquipmentTasksSearcher;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.user.admin.SystemUserPrivilegeMgr;
import com.everhomes.util.RuntimeErrorException;

@RestDoc(value = "Equipment Controller", site = "core")
@RestController
@RequestMapping("/equipment")
public class EquipmentController extends ControllerBase {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(EquipmentController.class);
	
	@Autowired
	private EquipmentService equipmentService;
	
	@Autowired
	private EquipmentStandardSearcher equipmentStandardSearcher;
	
	@Autowired
	private EquipmentSearcher equipmentSearcher;
	
	@Autowired
	private EquipmentAccessoriesSearcher equipmentAccessoriesSearcher;
	
	@Autowired
	private EquipmentTasksSearcher equipmentTasksSearcher;
	
	@Autowired
	private EquipmentStandardMapSearcher equipmentStandardMapSearcher;
	
//	/**
//	 * <b>URL: /equipment/creatEquipmentStandard</b>
//	 * <p>创建设备巡检标准</p>
//	 */
//	@RequestMapping("creatEquipmentStandard")
//	@RestReturn(value = EquipmentStandardsDTO.class)
//	public RestResponse creatEquipmentStandard(CreatEquipmentStandardCommand cmd) {
//		
//		EquipmentStandardsDTO standard = equipmentService.creatEquipmentStandard(cmd);
//		
//		RestResponse response = new RestResponse(standard);
//		response.setErrorCode(ErrorCodes.SUCCESS);
//		response.setErrorDescription("OK");
//		return response;
//	}
	
	/**
	 * <b>URL: /equipment/updateEquipmentStandard</b>
	 * <p>创建或修改设备巡检标准</p>
	 */
	@RequestMapping("updateEquipmentStandard")
	@RestReturn(value = EquipmentStandardsDTO.class)
	public RestResponse updateEquipmentStandard(UpdateEquipmentStandardCommand cmd) {
		
		EquipmentStandardsDTO standard = equipmentService.updateEquipmentStandard(cmd);
		
		RestResponse response = new RestResponse(standard);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	
	/**
	 * <b>URL: /equipment/findEquipmentStandard</b>
	 * <p>根据id查询巡检标准</p>
	 */
	@RequestMapping("findEquipmentStandard")
	@RestReturn(value = EquipmentStandardsDTO.class)
	public RestResponse findEquipmentStandard(DeleteEquipmentStandardCommand cmd) {
		
		EquipmentStandardsDTO standard = equipmentService.findEquipmentStandard(cmd);
		
		RestResponse response = new RestResponse(standard);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	
	/**
	 * <b>URL: /equipment/deleteEquipmentStandard</b>
	 * <p>删除设备巡检标准</p>
	 */
	@RequestMapping("deleteEquipmentStandard")
	@RestReturn(value = String.class)
	public RestResponse deleteEquipmentStandard(DeleteEquipmentStandardCommand cmd) {
		
		equipmentService.deleteEquipmentStandard(cmd);
		
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	
	/**
	 * <b>URL: /equipment/searchEquipmentStandards</b>
	 * <p>查看设备巡检标准</p>
	 */
	@RequestMapping("searchEquipmentStandards")
	@RestReturn(value = SearchEquipmentStandardsResponse.class)
	public RestResponse searchEquipmentStandards(SearchEquipmentStandardsCommand cmd) {
		
//		SearchEquipmentStandardsResponse standards = equipmentService.searchEquipmentStandards(cmd);
		SearchEquipmentStandardsResponse standards = equipmentStandardSearcher.query(cmd);
		
		RestResponse response = new RestResponse(standards);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /equipment/exportEquipmentStandards</b>
	 * <p>导出设备巡检标准</p>
	 */
	@RequestMapping("exportEquipmentStandards")
	public HttpServletResponse exportEquipmentStandards(@Valid SearchEquipmentStandardsCommand cmd,HttpServletResponse response) {
		
		HttpServletResponse commandResponse = equipmentService.exportEquipmentStandards(cmd, response);
		
		return commandResponse;
	}
	
	/**
     * <b>URL: /equipment/importEquipmentStandards</b>
     * <p>导入设备巡检标准</p>
     */
    @RequestMapping("importEquipmentStandards")
    @RestReturn(value=ImportDataResponse.class)
    public RestResponse importEquipmentStandards(ImportOwnerCommand cmd, @RequestParam(value = "attachment") MultipartFile[] files){
    	User manaUser = UserContext.current().getUser();
		Long userId = manaUser.getId();
		if(null == files || null == files[0]){
			LOGGER.error("files is null。userId="+userId);
			throw RuntimeErrorException.errorWith(UserServiceErrorCode.SCOPE, UserServiceErrorCode.ERROR_INVALID_PARAMS,
					"files is null");
		}
		ImportDataResponse importDataResponse = this.equipmentService.importEquipmentStandards(cmd, files[0], userId);
        RestResponse response = new RestResponse(importDataResponse);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
	 * <b>URL: /equipment/searchEquipmentStandardRelations</b>
	 * <p>查看设备-标准关联</p>
	 */
	@RequestMapping("searchEquipmentStandardRelations")
	@RestReturn(value = SearchEquipmentStandardRelationsResponse.class)
	public RestResponse searchEquipmentStandardRelations(SearchEquipmentStandardRelationsCommand cmd) {
		
		SearchEquipmentStandardRelationsResponse relations = equipmentStandardMapSearcher.query(cmd);
		
		RestResponse response = new RestResponse(relations);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	
	/**
	 * <b>URL: /equipment/reviewEquipmentStandardRelations</b>
	 * <p>审批设备-标准关联</p>
	 */
	@RequestMapping("reviewEquipmentStandardRelations")
	@RestReturn(value = String.class)
	public RestResponse reviewEquipmentStandardRelations(ReviewEquipmentStandardRelationsCommand cmd) {
		
		equipmentService.reviewEquipmentStandardRelations(cmd);
		
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	
	/**
	 * <b>URL: /equipment/deleteEquipmentStandardRelations</b>
	 * <p>删除失效的设备-标准关联</p>
	 */
	@RequestMapping("deleteEquipmentStandardRelations")
	@RestReturn(value = String.class)
	public RestResponse deleteEquipmentStandardRelations(DeleteEquipmentStandardRelationsCommand cmd) {
		
		equipmentService.deleteEquipmentStandardRelations(cmd);
		
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	
//	/**
//	 * <b>URL: /equipment/creatEquipments</b>
//	 * <p>创建设备</p>
//	 */
//	@RequestMapping("creatEquipments")
//	@RestReturn(value = EquipmentsDTO.class)
//	public RestResponse creatEquipments(CreatEquipmentsCommand cmd) {
//		
//		EquipmentsDTO equipment = equipmentService.creatEquipments(cmd);
//		
//		RestResponse response = new RestResponse(equipment);
//		response.setErrorCode(ErrorCodes.SUCCESS);
//		response.setErrorDescription("OK");
//		return response;
//	}
	
	/**
	 * <b>URL: /equipment/updateEquipments</b>
	 * <p>创建或修改设备</p>
	 */
	@RequestMapping("updateEquipments")
	@RestReturn(value = String.class)
	public RestResponse updateEquipments(UpdateEquipmentsCommand cmd) {
		
		equipmentService.updateEquipments(cmd);
		
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	
	/**
	 * <b>URL: /equipment/deleteEquipments</b>
	 * <p>删除设备</p>
	 */
	@RequestMapping("deleteEquipments")
	@RestReturn(value = String.class)
	public RestResponse deleteEquipments(DeleteEquipmentsCommand cmd) {
		
		equipmentService.deleteEquipments(cmd);
		
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	
	/**
	 * <b>URL: /equipment/findEquipment</b>
	 * <p>根据id查询设备</p>
	 */
	@RequestMapping("findEquipment")
	@RestReturn(value = EquipmentsDTO.class)
	public RestResponse findEquipment(DeleteEquipmentsCommand cmd) {
		
		EquipmentsDTO equipment = equipmentService.findEquipment(cmd);
		
		RestResponse response = new RestResponse(equipment);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	
	/**
	 * <b>URL: /equipment/searchEquipments</b>
	 * <p>查看设备列表</p>
	 */
	@RequestMapping("searchEquipments")
	@RestReturn(value = SearchEquipmentsResponse.class)
	public RestResponse searchEquipments(SearchEquipmentsCommand cmd) {
		
		SearchEquipmentsResponse equipments = equipmentSearcher.queryEquipments(cmd);
		
		RestResponse response = new RestResponse(equipments);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	
	/**
	 * <b>URL: /equipment/listEquipmentsCategories</b>
	 * <p>查看设备类型</p>
	 */
	@RequestMapping("listEquipmentsCategories")
	@RestReturn(value = CategoryDTO.class, collection = true)
	public RestResponse listEquipmentsCategories() {
		
		List<CategoryDTO> categories = equipmentService.listEquipmentsCategories();
		
		RestResponse response = new RestResponse(categories);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /equipment/exportEquipments</b>
	 * <p>导出设备列表</p>
	 */
	@RequestMapping("exportEquipments")
	public HttpServletResponse exportEquipments(@Valid SearchEquipmentsCommand cmd,HttpServletResponse response) {
		
		HttpServletResponse commandResponse = equipmentService.exportEquipments(cmd, response);
		
		return commandResponse;
	}
	
	/**
     * <b>URL: /equipment/importEquipments</b>
     * <p>导入设备列表</p>
     */
    @RequestMapping("importEquipments")
    @RestReturn(value=ImportDataResponse.class)
    public RestResponse importEquipments(ImportOwnerCommand cmd, @RequestParam(value = "attachment") MultipartFile[] files){
    	User manaUser = UserContext.current().getUser();
		Long userId = manaUser.getId();
		if(null == files || null == files[0]){
			LOGGER.error("files is null。userId="+userId);
			throw RuntimeErrorException.errorWith(UserServiceErrorCode.SCOPE, UserServiceErrorCode.ERROR_INVALID_PARAMS,
					"files is null");
		}
		ImportDataResponse importDataResponse = this.equipmentService.importEquipments(cmd, files[0], userId);
        RestResponse response = new RestResponse(importDataResponse);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
//    /**
//	 * <b>URL: /equipment/creatEquipmentAccessories</b>
//	 * <p>创建备品备件</p>
//	 */
//	@RequestMapping("creatEquipmentAccessories")
//	@RestReturn(value = EquipmentAccessoriesDTO.class)
//	public RestResponse creatEquipmentAccessories(UpdateEquipmentAccessoriesCommand cmd) {
//		
//		EquipmentAccessoriesDTO accessories = equipmentService.creatEquipmentAccessories(cmd);
//		
//		RestResponse response = new RestResponse(accessories);
//		response.setErrorCode(ErrorCodes.SUCCESS);
//		response.setErrorDescription("OK");
//		return response;
//	}
	
	/**
	 * <b>URL: /equipment/updateEquipmentAccessories</b>
	 * <p>创建或修改备品备件</p>
	 */
	@RequestMapping("updateEquipmentAccessories")
	@RestReturn(value = EquipmentAccessoriesDTO.class)
	public RestResponse updateEquipmentAccessories(UpdateEquipmentAccessoriesCommand cmd) {
		
		EquipmentAccessoriesDTO accessories = equipmentService.updateEquipmentAccessories(cmd);
		
		RestResponse response = new RestResponse(accessories);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	
	/**
	 * <b>URL: /equipment/deleteEquipmentAccessories</b>
	 * <p>删除备品备件</p>
	 */
	@RequestMapping("deleteEquipmentAccessories")
	@RestReturn(value = String.class)
	public RestResponse deleteEquipmentAccessories(DeleteEquipmentAccessoriesCommand cmd) {
		
		equipmentService.deleteEquipmentAccessories(cmd);
		
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	
	/**
	 * <b>URL: /equipment/findEquipmentAccessoriesById</b>
	 * <p>根据id查询备品备件</p>
	 */
	@RequestMapping("findEquipmentAccessoriesById")
	@RestReturn(value = EquipmentAccessoriesDTO.class)
	public RestResponse findEquipmentAccessoriesById(DeleteEquipmentAccessoriesCommand cmd) {
		
		EquipmentAccessoriesDTO accessory = equipmentService.findEquipmentAccessoriesById(cmd);
		
		RestResponse response = new RestResponse(accessory);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	
	/**
	 * <b>URL: /equipment/searchEquipmentAccessories</b>
	 * <p>查看备品备件</p>
	 */
	@RequestMapping("searchEquipmentAccessories")
	@RestReturn(value = SearchEquipmentAccessoriesResponse.class)
	public RestResponse searchEquipmentAccessories(SearchEquipmentAccessoriesCommand cmd) {
		
		SearchEquipmentAccessoriesResponse accessories = equipmentAccessoriesSearcher.query(cmd);
		
		RestResponse response = new RestResponse(accessories);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /equipment/exportEquipmentAccessories</b>
	 * <p>导出备品备件表</p>
	 */
	@RequestMapping("exportEquipmentAccessories")
	public HttpServletResponse exportEquipmentAccessories(@Valid SearchEquipmentAccessoriesCommand cmd,HttpServletResponse response) {
		
		HttpServletResponse commandResponse = equipmentService.exportEquipmentAccessories(cmd, response);
		
		return commandResponse;
	}
	
	/**
     * <b>URL: /equipment/importEquipmentAccessories</b>
     * <p>导入备品备件表</p>
     */
    @RequestMapping("importEquipmentAccessories")
    @RestReturn(value=ImportDataResponse.class)
    public RestResponse importEquipmentAccessories(ImportOwnerCommand cmd, @RequestParam(value = "attachment") MultipartFile[] files){
    	User manaUser = UserContext.current().getUser();
		Long userId = manaUser.getId();
		if(null == files || null == files[0]){
			LOGGER.error("files is null。userId="+userId);
			throw RuntimeErrorException.errorWith(UserServiceErrorCode.SCOPE, UserServiceErrorCode.ERROR_INVALID_PARAMS,
					"files is null");
		}
		ImportDataResponse importDataResponse = this.equipmentService.importEquipmentAccessories(cmd, files[0], userId);
        RestResponse response = new RestResponse(importDataResponse);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
	 * <b>URL: /equipment/exportEquipmentTasks</b>
	 * <p>导出任务</p>
	 */
	@RequestMapping("exportEquipmentTasks")
	public HttpServletResponse exportEquipmentTasks(@Valid SearchEquipmentTasksCommand cmd,HttpServletResponse response) {
		
		HttpServletResponse commandResponse = equipmentService.exportEquipmentTasks(cmd, response);
		
		return commandResponse;
	}
	
	/**
	 * <b>URL: /equipment/searchEquipmentTasks</b>
	 * <p>查看任务列表-后台</p>
	 */
	@RequestMapping("searchEquipmentTasks")
	@RestReturn(value = ListEquipmentTasksResponse.class)
	public RestResponse searchEquipmentTasks(SearchEquipmentTasksCommand cmd) {
		
		ListEquipmentTasksResponse tasks = equipmentTasksSearcher.query(cmd);
		
		RestResponse response = new RestResponse(tasks);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	
	/**
	 * <b>URL: /equipment/listEquipmentTasks</b>
	 * <p>查看任务列表-app</p>
	 */
	@RequestMapping("listEquipmentTasks")
	@RestReturn(value = ListEquipmentTasksResponse.class)
	public RestResponse listEquipmentTasks(ListEquipmentTasksCommand cmd) {
		
		ListEquipmentTasksResponse tasks = equipmentService.listEquipmentTasks(cmd);
		
		RestResponse response = new RestResponse(tasks);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	
	/**
	 * <b>URL: /equipment/listTasksByEquipmentId</b>
	 * <p>查看设备任务</p>
	 */
	@RequestMapping("listTasksByEquipmentId")
	@RestReturn(value = ListEquipmentTasksResponse.class)
	public RestResponse listTasksByEquipmentId(ListTasksByEquipmentIdCommand cmd) {
		
		ListEquipmentTasksResponse tasks = equipmentService.listTasksByEquipmentId(cmd);
		
		RestResponse response = new RestResponse(tasks);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	
	/**
	 * <b>URL: /equipment/listTaskById</b>
	 * <p>根据id查看任务</p>
	 */
	@RequestMapping("listTaskById")
	@RestReturn(value = EquipmentTaskDTO.class)
	public RestResponse listTaskById(ListTaskByIdCommand cmd) {
		
		EquipmentTaskDTO task = equipmentService.listTaskById(cmd);
		
		RestResponse response = new RestResponse(task);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /equipment/reportEquipmentTask</b>
	 * <p>任务上报</p>
	 */
	@RequestMapping("reportEquipmentTask")
	@RestReturn(value = EquipmentTaskDTO.class)
	public RestResponse reportEquipmentTask(ReportEquipmentTaskCommand cmd) {
		
		EquipmentTaskDTO dto = equipmentService.reportEquipmentTask(cmd);
		
		RestResponse response = new RestResponse(dto);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	
	/**
	 * <b>URL: /equipment/reviewEquipmentTask</b>
	 * <p>任务审阅</p>
	 */
	@RequestMapping("reviewEquipmentTask")
	@RestReturn(value = String.class)
	public RestResponse reviewEquipmentTask(ReviewEquipmentTaskCommand cmd) {
		
		equipmentService.reviewEquipmentTask(cmd);
		
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /equipment/reviewEquipmentTasks</b>
	 * <p>任务批量审阅</p>
	 */
	@RequestMapping("reviewEquipmentTasks")
	@RestReturn(value = String.class)
	public RestResponse reviewEquipmentTasks(ReviewEquipmentTasksCommand cmd) {

		equipmentService.reviewEquipmentTasks(cmd);

		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /equipment/createEquipmentTask</b>
	 * <p>创建某设备的任务</p>
	 */
	@RequestMapping("createEquipmentTask")
	@RestReturn(value = String.class)
	public RestResponse createEquipmentTask(DeleteEquipmentsCommand cmd) {
		
		equipmentService.createEquipmentTask(cmd);
		
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	
	/**
	 * <b>URL: /equipment/listLogsByTaskId</b>
	 * <p>查看任务的操作记录</p>
	 */
	@RequestMapping("listLogsByTaskId")
	@RestReturn(value = ListLogsByTaskIdResponse.class)
	public RestResponse listLogsByTaskId(ListLogsByTaskIdCommand cmd) {
		
		ListLogsByTaskIdResponse records = equipmentService.listLogsByTaskId(cmd);
		
		RestResponse response = new RestResponse(records);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	
	/**
	 * <b>URL: /equipment/verifyEquipmentLocation</b>
	 * <p>扫一扫验证二维码和经纬度</p>
	 */
	@RequestMapping("verifyEquipmentLocation")
	@RestReturn(value = VerifyEquipmentLocationResponse.class)
	public RestResponse verifyEquipmentLocation(VerifyEquipmentLocationCommand cmd) {
		
		VerifyEquipmentLocationResponse resp = equipmentService.verifyEquipmentLocation(cmd);
		
		RestResponse response = new RestResponse(resp);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	
	/**
	 * <b>URL: /equipment/listParametersByEquipmentId</b>
	 * <p>查看设备所需记录的参数</p>
	 */
	@RequestMapping("listParametersByEquipmentId")
	@RestReturn(value = EquipmentParameterDTO.class, collection = true)
	public RestResponse listParametersByEquipmentId(DeleteEquipmentsCommand cmd) {
		
		List<EquipmentParameterDTO> paras = equipmentService.listParametersByEquipmentId(cmd);
		
		RestResponse response = new RestResponse(paras);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	
	/**
	 * <b>URL: /equipment/listAttachmentsByEquipmentId</b>
	 * <p>查看设备操作图示或说明书</p>
	 */
	@RequestMapping("listAttachmentsByEquipmentId")
	@RestReturn(value = EquipmentAttachmentDTO.class, collection = true)
	public RestResponse listAttachmentsByEquipmentId(ListAttachmentsByEquipmentIdCommand cmd) {
		
		List<EquipmentAttachmentDTO> attachments = equipmentService.listAttachmentsByEquipmentId(cmd);
		
		RestResponse response = new RestResponse(attachments);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	
	/**
	 * <b>URL: /equipment/listRelatedOrgGroups</b>
	 * <p>查看管理处</p>
	 */
	@RequestMapping("listRelatedOrgGroups")
	@RestReturn(value = OrganizationDTO.class, collection = true)
	public RestResponse listRelatedOrgGroups(ListRelatedOrgGroupsCommand cmd) {
		
		List<OrganizationDTO> groups = equipmentService.listRelatedOrgGroups(cmd);
		
		RestResponse response = new RestResponse(groups);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	
	/**
     * <b>URL: /equipment/syncEquipmentStandardIndex</b>
     * <p>搜索索引同步</p>
     * @return {String.class}
     */
    @RequestMapping("syncEquipmentStandardIndex")
    @RestReturn(value=String.class)
    public RestResponse syncEquipmentStandardIndex() {
    	SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
        resolver.checkUserPrivilege(UserContext.current().getUser().getId(), 0);
        
        equipmentStandardSearcher.syncFromDb();
        RestResponse res = new RestResponse();
        res.setErrorCode(ErrorCodes.SUCCESS);
        res.setErrorDescription("OK");
        return res;
    }
    
    /**
     * <b>URL: /equipment/syncEquipmentIndex</b>
     * <p>搜索索引同步</p>
     * @return {String.class}
     */
    @RequestMapping("syncEquipmentIndex")
    @RestReturn(value=String.class)
    public RestResponse syncEquipmentIndex() {
    	SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
        resolver.checkUserPrivilege(UserContext.current().getUser().getId(), 0);
        
        equipmentSearcher.syncFromDb();
        RestResponse res = new RestResponse();
        res.setErrorCode(ErrorCodes.SUCCESS);
        res.setErrorDescription("OK");
        return res;
    }
    
    /**
     * <b>URL: /equipment/syncEquipmentAccessoriesIndex</b>
     * <p>搜索索引同步</p>
     * @return {String.class}
     */
    @RequestMapping("syncEquipmentAccessoriesIndex")
    @RestReturn(value=String.class)
    public RestResponse syncEquipmentAccessoriesIndex() {
    	SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
        resolver.checkUserPrivilege(UserContext.current().getUser().getId(), 0);
        
        equipmentAccessoriesSearcher.syncFromDb();
        RestResponse res = new RestResponse();
        res.setErrorCode(ErrorCodes.SUCCESS);
        res.setErrorDescription("OK");
        return res;
    }
    
    /**
     * <b>URL: /equipment/syncEquipmentTasksIndex</b>
     * <p>搜索索引同步</p>
     * @return {String.class}
     */
    @RequestMapping("syncEquipmentTasksIndex")
    @RestReturn(value=String.class)
    public RestResponse syncEquipmentTasksIndex() {
    	SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
        resolver.checkUserPrivilege(UserContext.current().getUser().getId(), 0);
        
        equipmentTasksSearcher.syncFromDb();
        RestResponse res = new RestResponse();
        res.setErrorCode(ErrorCodes.SUCCESS);
        res.setErrorDescription("OK");
        return res;
    }
    
    /**
     * <b>URL: /equipment/syncEquipmentStandardMapIndex</b>
     * <p>搜索索引同步</p>
     * @return {String.class}
     */
    @RequestMapping("syncEquipmentStandardMapIndex")
    @RestReturn(value=String.class)
    public RestResponse syncEquipmentStandardMapIndex() {
    	SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
        resolver.checkUserPrivilege(UserContext.current().getUser().getId(), 0);
        
        equipmentStandardMapSearcher.syncFromDb();
        RestResponse res = new RestResponse();
        res.setErrorCode(ErrorCodes.SUCCESS);
        res.setErrorDescription("OK");
        return res;
    }
    
    /**
	 * <b>URL: /equipment/updateEquipmentCategory</b>
	 * <p>修改设备类型</p>
	 */
	@RequestMapping("updateEquipmentCategory")
	@RestReturn(value = String.class)
	public RestResponse updateEquipmentCategory(UpdateEquipmentCategoryCommand cmd) {
		
		equipmentService.updateEquipmentCategory(cmd);
		
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
    
    /**
     * <b>URL: /equipment/createEquipmentCategory</b>
     * <p>新建设备类型</p>
     */
    @RequestMapping("createEquipmentCategory")
    @RestReturn(value=String.class)
    public RestResponse createEquipmentCategory(CreateEquipmentCategoryCommand cmd) {
  	  
    	equipmentService.createEquipmentCategory(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /pmtask/deleteEquipmentCategory</b>
     * <p>删除设备类型</p>
     */
    @RequestMapping("deleteEquipmentCategory")
    @RestReturn(value=String.class)
    public RestResponse deleteEquipmentCategory(DeleteEquipmentCategoryCommand cmd) {
    	  
    	equipmentService.deleteEquipmentCategory(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
	 * <b>URL: /equipment/listParametersByStandardId</b>
	 * <p>查看设备所需记录的参数</p>
	 */
	@RequestMapping("listParametersByStandardId")
	@RestReturn(value = InspectionItemDTO.class, collection = true)
	public RestResponse listParametersByStandardId(ListParametersByStandardIdCommand cmd) {
		
		List<InspectionItemDTO> paras = equipmentService.listParametersByStandardId(cmd);
		
		RestResponse response = new RestResponse(paras);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	
	/**
     * <b>URL: /equipment/createInspectionTemplate</b>
     * <p>新建巡检模板</p>
     */
    @RequestMapping("createInspectionTemplate")
    @RestReturn(value=String.class)
    public RestResponse createInspectionTemplate(CreateInspectionTemplateCommand cmd) {
  	  
    	equipmentService.createInspectionTemplate(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /equipment/updateInspectionTemplate</b>
     * <p>修改巡检模板</p>
     */
    @RequestMapping("updateInspectionTemplate")
    @RestReturn(value=String.class)
    public RestResponse updateInspectionTemplate(UpdateInspectionTemplateCommand cmd) {
  	  
    	equipmentService.updateInspectionTemplate(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /equipment/deleteInspectionTemplate</b>
     * <p>删除巡检模板</p>
     */
    @RequestMapping("deleteInspectionTemplate")
    @RestReturn(value=String.class)
    public RestResponse deleteInspectionTemplate(DeleteInspectionTemplateCommand cmd) {
  	  
    	equipmentService.deleteInspectionTemplate(cmd);
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /equipment/findInspectionTemplate</b>
     * <p>查询巡检模板</p>
     */
    @RequestMapping("findInspectionTemplate")
    @RestReturn(value=InspectionTemplateDTO.class)
    public RestResponse findInspectionTemplate(DeleteInspectionTemplateCommand cmd) {
  	  
    	InspectionTemplateDTO dto = equipmentService.findInspectionTemplate(cmd);
        RestResponse response = new RestResponse(dto);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
     * <b>URL: /equipment/listInspectionTemplates</b>
     * <p>列出巡检模板</p>
     */
    @RequestMapping("listInspectionTemplates")
    @RestReturn(value=InspectionTemplateDTO.class, collection = true)
    public RestResponse listInspectionTemplates(ListInspectionTemplatesCommand cmd) {
  	  
    	List<InspectionTemplateDTO> dto = equipmentService.listInspectionTemplates(cmd);
        RestResponse response = new RestResponse(dto);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }
    
    /**
	 * <b>URL: /equipment/listTasksByToken</b>
	 * <p>扫码查看设备任务</p>
	 */
	@RequestMapping("listTasksByToken")
	@RestReturn(value = ListEquipmentTasksResponse.class)
	public RestResponse listTasksByToken(ListTasksByTokenCommand cmd) {
		
		ListEquipmentTasksResponse tasks = equipmentService.listTasksByToken(cmd);
		
		RestResponse response = new RestResponse(tasks);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	
	/**
	 * <b>URL: /equipment/getInspectionObjectByQRCode</b>
	 * <p>扫码查看巡检对象</p>
	 */
	@RequestMapping("getInspectionObjectByQRCode")
	@RestReturn(value = EquipmentsDTO.class)
	public RestResponse getInspectionObjectByQRCode(GetInspectionObjectByQRCodeCommand cmd) {
		
		EquipmentsDTO equipment = equipmentService.getInspectionObjectByQRCode(cmd);
		
		RestResponse response = new RestResponse(equipment);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	
	/**
	 * <b>URL: /equipment/listEquipmentInspectionCategories</b>
	 * <p>查看巡检对象类型</p>
	 */
	@RequestMapping("listEquipmentInspectionCategories")
	@RestReturn(value = EquipmentInspectionCategoryDTO.class, collection = true)
	public RestResponse listEquipmentInspectionCategories(ListEquipmentInspectionCategoriesCommand cmd) {
		
		List<EquipmentInspectionCategoryDTO> categories = equipmentService.listEquipmentInspectionCategories(cmd);
		
		RestResponse response = new RestResponse(categories);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	
	/**
	 * <b>URL: /equipment/listUserHistoryTasks</b>
	 * <p>个人执行过的历史任务</p>
	 */
	@RequestMapping("listUserHistoryTasks")
	@RestReturn(value = ListEquipmentTasksResponse.class)
	public RestResponse listUserHistoryTasks(ListUserHistoryTasksCommand cmd) {
		
		ListEquipmentTasksResponse tasks = equipmentService.listUserHistoryTasks(cmd);
		
		RestResponse response = new RestResponse(tasks);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	
	/**
	 * <b>URL: /equipment/statEquipmentTasks</b>
	 * <p>任务数统计</p>
	 */
	@RequestMapping("statEquipmentTasks")
	@RestReturn(value = StatEquipmentTasksResponse.class)
	public RestResponse statEquipmentTasks(StatEquipmentTasksCommand cmd) {
		
		StatEquipmentTasksResponse stat = equipmentService.statEquipmentTasks(cmd);
		
		RestResponse response = new RestResponse(stat);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /equipment/exportEquipmentsCard</b>
	 * <p>导出设备卡</p>
	 */
	@RequestMapping("exportEquipmentsCard")
	@RestReturn(value = String.class)
	public RestResponse exportEquipmentsCard(ExportEquipmentsCardCommand cmd, HttpServletResponse response) {

		equipmentService.exportEquipmentsCard(cmd, response);

		RestResponse resp = new RestResponse();
		resp.setErrorCode(ErrorCodes.SUCCESS);
		resp.setErrorDescription("OK");
		return resp;
	}

	/**
	 * <b>URL: /equipment/statTodayEquipmentTasks</b>
	 * <p>当天任务数统计</p>
	 */
	@RequestMapping("statTodayEquipmentTasks")
	@RestReturn(value = StatTodayEquipmentTasksResponse.class)
	public RestResponse statTodayEquipmentTasks(StatTodayEquipmentTasksCommand cmd) {

		StatTodayEquipmentTasksResponse stat = equipmentService.statTodayEquipmentTasks(cmd);

		RestResponse response = new RestResponse(stat);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /equipment/statLastDaysEquipmentTasks</b>
	 * <p>最近几天任务数统计</p>
	 */
	@RequestMapping("statLastDaysEquipmentTasks")
	@RestReturn(value = StatLastDaysEquipmentTasksResponse.class)
	public RestResponse statLastDaysEquipmentTasks(StatLastDaysEquipmentTasksCommand cmd) {

		StatLastDaysEquipmentTasksResponse stat = equipmentService.statLastDaysEquipmentTasks(cmd);

		RestResponse response = new RestResponse(stat);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /equipment/statIntervalAllEquipmentTasks</b>
	 * <p>区间任务数统计</p>
	 */
	@RequestMapping("statIntervalAllEquipmentTasks")
	@RestReturn(value = StatIntervalAllEquipmentTasksResponse.class)
	public RestResponse statIntervalAllEquipmentTasks(StatIntervalAllEquipmentTasksCommand cmd) {

		StatIntervalAllEquipmentTasksResponse stat = equipmentService.statIntervalAllEquipmentTasks(cmd);

		RestResponse response = new RestResponse(stat);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /equipment/statItemResultsInEquipmentTasks</b>
	 * <p>按设备-标准统计任务的细项</p>
	 */
	@RequestMapping("statItemResultsInEquipmentTasks")
	@RestReturn(value = StatItemResultsInEquipmentTasksResponse.class)
	public RestResponse statItemResultsInEquipmentTasks(StatItemResultsInEquipmentTasksCommand cmd) {

		StatItemResultsInEquipmentTasksResponse stat = equipmentService.statItemResultsInEquipmentTasks(cmd);

		RestResponse response = new RestResponse(stat);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /equipment/listAbnormalTasks</b>
	 * <p>查看异常任务列表</p>
	 */
	@RequestMapping("listAbnormalTasks")
	@RestReturn(value = ListEquipmentTasksResponse.class)
	public RestResponse listAbnormalTasks(ListAbnormalTasksCommand cmd) {

		ListEquipmentTasksResponse tasks = equipmentService.listAbnormalTasks(cmd);

		RestResponse response = new RestResponse(tasks);
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /equipment/setPmNotifyParams</b>
	 * <p>设置通知参数</p>
	 */
	@RequestMapping("setPmNotifyParams")
	@RestReturn(value = String.class)
	public RestResponse setPmNotifyParams(SetPmNotifyParamsCommand cmd) {
		equipmentService.setPmNotifyParams(cmd);
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /equipment/listPmNotifyParams</b>
	 * <p>列出通知参数</p>
	 */
	@RequestMapping("listPmNotifyParams")
	@RestReturn(value = PmNotifyParamDTO.class, collection = true)
	public RestResponse listPmNotifyParams(ListPmNotifyParamsCommand cmd) {
		RestResponse response = new RestResponse(equipmentService.listPmNotifyParams(cmd));
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

	/**
	 * <b>URL: /equipment/deletePmNotifyParams</b>
	 * <p>删除通知参数</p>
	 */
	@RequestMapping("deletePmNotifyParams")
	@RestReturn(value = String.class)
	public RestResponse deletePmNotifyParams(DeletePmNotifyParamsCommand cmd) {
		equipmentService.deletePmNotifyParams(cmd);
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}


}
