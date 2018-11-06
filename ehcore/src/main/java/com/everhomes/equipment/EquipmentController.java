package com.everhomes.equipment;


import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.equipment.CreateEquipmentRepairCommand;
import com.everhomes.rest.equipment.DeleteEquipmentAccessoriesCommand;
import com.everhomes.rest.equipment.DeleteEquipmentPlanCommand;
import com.everhomes.rest.equipment.DeleteEquipmentStandardCommand;
import com.everhomes.rest.equipment.DeleteEquipmentsCommand;
import com.everhomes.rest.equipment.EquipmentAccessoriesDTO;
import com.everhomes.rest.equipment.EquipmentAttachmentDTO;
import com.everhomes.rest.equipment.EquipmentInspectionPlanDTO;
import com.everhomes.rest.equipment.EquipmentInspectionReviewDateDTO;
import com.everhomes.rest.equipment.EquipmentOperateLogsDTO;
import com.everhomes.rest.equipment.EquipmentStandardRelationDTO;
import com.everhomes.rest.equipment.EquipmentStandardsDTO;
import com.everhomes.rest.equipment.EquipmentTaskDTO;
import com.everhomes.rest.equipment.EquipmentTaskOfflineResponse;
import com.everhomes.rest.equipment.EquipmentsDTO;
import com.everhomes.rest.equipment.ExportEquipmentsCardCommand;
import com.everhomes.rest.equipment.ExportTaskLogsCommand;
import com.everhomes.rest.equipment.GetInspectionObjectByQRCodeCommand;
import com.everhomes.rest.equipment.ImportOwnerCommand;
import com.everhomes.rest.equipment.InspectionItemDTO;
import com.everhomes.rest.equipment.ListAbnormalTasksCommand;
import com.everhomes.rest.equipment.ListAttachmentsByEquipmentIdCommand;
import com.everhomes.rest.equipment.ListEquipmentTasksCommand;
import com.everhomes.rest.equipment.ListEquipmentTasksResponse;
import com.everhomes.rest.equipment.ListLogsByTaskIdCommand;
import com.everhomes.rest.equipment.ListLogsByTaskIdResponse;
import com.everhomes.rest.equipment.ListParametersByStandardIdCommand;
import com.everhomes.rest.equipment.ListTaskByIdCommand;
import com.everhomes.rest.equipment.ListTasksByEquipmentIdCommand;
import com.everhomes.rest.equipment.ListTasksByTokenCommand;
import com.everhomes.rest.equipment.ListUserHistoryTasksCommand;
import com.everhomes.rest.equipment.OfflineEquipmentTaskReportCommand;
import com.everhomes.rest.equipment.OfflineEquipmentTaskReportResponse;
import com.everhomes.rest.equipment.ReportEquipmentTaskCommand;
import com.everhomes.rest.equipment.ReviewEquipmentPlanCommand;
import com.everhomes.rest.equipment.ReviewEquipmentTaskCommand;
import com.everhomes.rest.equipment.ReviewEquipmentTasksCommand;
import com.everhomes.rest.equipment.SearchEquipmentAccessoriesCommand;
import com.everhomes.rest.equipment.SearchEquipmentAccessoriesResponse;
import com.everhomes.rest.equipment.SearchEquipmentInspectionPlansCommand;
import com.everhomes.rest.equipment.SearchEquipmentStandardRelationsCommand;
import com.everhomes.rest.equipment.SearchEquipmentStandardRelationsResponse;
import com.everhomes.rest.equipment.SearchEquipmentStandardsCommand;
import com.everhomes.rest.equipment.SearchEquipmentStandardsResponse;
import com.everhomes.rest.equipment.SearchEquipmentTasksCommand;
import com.everhomes.rest.equipment.SearchEquipmentsCommand;
import com.everhomes.rest.equipment.SearchEquipmentsResponse;
import com.everhomes.rest.equipment.SetReviewExpireDaysCommand;
import com.everhomes.rest.equipment.StatEquipmentTasksCommand;
import com.everhomes.rest.equipment.StatEquipmentTasksResponse;
import com.everhomes.rest.equipment.StatIntervalAllEquipmentTasksCommand;
import com.everhomes.rest.equipment.StatIntervalAllEquipmentTasksResponse;
import com.everhomes.rest.equipment.StatLastDaysEquipmentTasksCommand;
import com.everhomes.rest.equipment.StatLastDaysEquipmentTasksResponse;
import com.everhomes.rest.equipment.StatTodayEquipmentTasksCommand;
import com.everhomes.rest.equipment.StatTodayEquipmentTasksResponse;
import com.everhomes.rest.equipment.UpdateEquipmentAccessoriesCommand;
import com.everhomes.rest.equipment.UpdateEquipmentPlanCommand;
import com.everhomes.rest.equipment.UpdateEquipmentStandardCommand;
import com.everhomes.rest.equipment.UpdateEquipmentsCommand;
import com.everhomes.rest.equipment.VerifyEquipmentLocationCommand;
import com.everhomes.rest.equipment.VerifyEquipmentLocationResponse;
import com.everhomes.rest.equipment.findScopeFieldItemCommand;
import com.everhomes.rest.equipment.searchEquipmentInspectionPlansResponse;
import com.everhomes.rest.pmNotify.DeletePmNotifyParamsCommand;
import com.everhomes.rest.pmNotify.ListPmNotifyParamsCommand;
import com.everhomes.rest.pmNotify.PmNotifyParamDTO;
import com.everhomes.rest.pmNotify.SetPmNotifyParamsCommand;
import com.everhomes.rest.user.UserServiceErrorCode;
import com.everhomes.rest.user.admin.ImportDataResponse;
import com.everhomes.rest.varField.FieldItemDTO;
import com.everhomes.search.EquipmentAccessoriesSearcher;
import com.everhomes.search.EquipmentPlanSearcher;
import com.everhomes.search.EquipmentSearcher;
import com.everhomes.search.EquipmentStandardMapSearcher;
import com.everhomes.search.EquipmentStandardSearcher;
import com.everhomes.search.EquipmentTasksSearcher;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserPrivilegeMgr;
import com.everhomes.util.RuntimeErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

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

    @Autowired
    private EquipmentPlanSearcher equipmentPlanSearcher;

    /**
     * <b>URL: /equipment/updateEquipmentStandard</b>
     * <p>创建或修改设备巡检标准</p>
     */
    @RequestMapping("updateEquipmentStandard")
    @RestReturn(value = EquipmentStandardsDTO.class)
    public RestResponse updateEquipmentStandard(UpdateEquipmentStandardCommand cmd) {
        EquipmentStandardsDTO standard = equipmentService.updateEquipmentStandard(cmd);
        return getRestResponse(standard);
    }

    /**
     * <b>URL: /equipment/findEquipmentStandard</b>
     * <p>根据id查询巡检标准</p>
     */
    @RequestMapping("findEquipmentStandard")
    @RestReturn(value = EquipmentStandardsDTO.class)
    public RestResponse findEquipmentStandard(DeleteEquipmentStandardCommand cmd) {
        EquipmentStandardsDTO standard = equipmentService.findEquipmentStandard(cmd);
        return getRestResponse(standard);
    }

    /**
     * <b>URL: /equipment/deleteEquipmentStandard</b>
     * <p>删除设备巡检标准</p>
     */
    @RequestMapping("deleteEquipmentStandard")
    @RestReturn(value = String.class)
    public RestResponse deleteEquipmentStandard(DeleteEquipmentStandardCommand cmd) {

        equipmentService.deleteEquipmentStandard(cmd);
        return getSuccessResponse();
    }

    /**
     * <b>URL: /equipment/searchEquipmentStandards</b>
     * <p>列出设备巡检标准</p>
     */
    @RequestMapping("searchEquipmentStandards")
    @RestReturn(value = SearchEquipmentStandardsResponse.class)
    public RestResponse searchEquipmentStandards(SearchEquipmentStandardsCommand cmd) {

        SearchEquipmentStandardsResponse standards = equipmentStandardSearcher.query(cmd);
        return getRestResponse(standards);
    }

    /**
     * <b>URL: /equipment/exportEquipmentStandards</b>
     * <p>导出设备巡检标准</p>
     */
    @RequestMapping("exportEquipmentStandards")
    public HttpServletResponse exportEquipmentStandards(@Valid SearchEquipmentStandardsCommand cmd, HttpServletResponse response) {

        return equipmentService.exportEquipmentStandards(cmd, response);
    }

    /**
     * <b>URL: /equipment/importEquipmentStandards</b>
     * <p>导入设备巡检标准</p>
     */
    @RequestMapping("importEquipmentStandards")
    @RestReturn(value = ImportDataResponse.class)
    public RestResponse importEquipmentStandards(ImportOwnerCommand cmd, @RequestParam(value = "attachment") MultipartFile[] files) {
        Long userId = UserContext.currentUserId();
        if (null == files || null == files[0]) {
            LOGGER.error("files is null userId=" + userId);
            throw RuntimeErrorException.errorWith(UserServiceErrorCode.SCOPE, UserServiceErrorCode.ERROR_INVALID_PARAMS,
                    "files is null");
        }
        ImportDataResponse importDataResponse = this.equipmentService.importEquipmentStandards(cmd, files[0], userId);
        return getRestResponse(importDataResponse);
    }

    /**
     * <b>URL: /equipment/updateEquipments</b>
     * <p>创建或修改设备</p>
     */
    @RequestMapping("updateEquipments")
    @RestReturn(value = String.class)
    public RestResponse updateEquipments(UpdateEquipmentsCommand cmd) {
        equipmentService.updateEquipments(cmd);
        return getSuccessResponse();
    }

    /**
     * <b>URL: /equipment/deleteEquipments</b>
     * <p>删除设备</p>
     */
    @RequestMapping("deleteEquipments")
    @RestReturn(value = String.class)
    public RestResponse deleteEquipments(DeleteEquipmentsCommand cmd) {
        equipmentService.deleteEquipments(cmd);
        return getSuccessResponse();
    }

    /**
     * <b>URL: /equipment/updateEquipmentStatus</b>
     * <p>报废巡检对象</p>
     */
    @RequestMapping("updateEquipmentStatus")
    @RestReturn(value = String.class)
    public RestResponse updateEquipmentStatus(DeleteEquipmentsCommand cmd) {
        equipmentService.updateEquipmentStatus(cmd);
        return getSuccessResponse();
    }

    /**
     * <b>URL: /equipment/findEquipment</b>
     * <p>根据id查询设备</p>
     */
    @RequestMapping("findEquipment")
    @RestReturn(value = EquipmentsDTO.class)
    public RestResponse findEquipment(DeleteEquipmentsCommand cmd) {
        EquipmentsDTO equipment = equipmentService.findEquipment(cmd);
        return getRestResponse(equipment);
    }

    /**
     * <b>URL: /equipment/searchEquipments</b>
     * <p>列出设备列表</p>
     */
    @RequestMapping("searchEquipments")
    @RestReturn(value = SearchEquipmentsResponse.class)
    public RestResponse searchEquipments(SearchEquipmentsCommand cmd) {
        SearchEquipmentsResponse equipments = equipmentSearcher.queryEquipments(cmd);
        return getRestResponse(equipments);
    }

    /**
     * <b>URL: /equipment/listOperateLogs</b>
     * <p>查看巡检对象操作记录</p>
     */
    @RequestMapping("listOperateLogs")
    @RestReturn(value = EquipmentOperateLogsDTO.class, collection = true)
    public RestResponse listOperateLogs(DeleteEquipmentsCommand cmd) {
        List<EquipmentOperateLogsDTO> equipmentOperateLogsDTOS = equipmentService.listOperateLogs(cmd);
        return getRestResponse(equipmentOperateLogsDTOS);
    }

    /**
     * <b>URL: /equipment/searchEquipmentStandardRelations</b>
     * <p>列出设备-标准关联列表 </p>
     */
    @RequestMapping("searchEquipmentStandardRelations")
    @RestReturn(value = SearchEquipmentStandardRelationsResponse.class)
    public RestResponse searchEquipmentStandardRelations(SearchEquipmentStandardRelationsCommand cmd) {
        SearchEquipmentStandardRelationsResponse relations = equipmentStandardMapSearcher.query(cmd);
        return getRestResponse(relations);
    }

	/**
	 * <b>URL: /equipment/exportEquipments</b>
	 * <p>导出设备列表</p>
	 */
	@RequestMapping("exportEquipments")
	@RestReturn(String.class)
	public RestResponse exportEquipments(@Valid SearchEquipmentsCommand cmd,HttpServletResponse httpResponse) {
		equipmentService.exportEquipments(cmd, httpResponse);
		RestResponse response = new RestResponse();
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}
	
	/**
     * <b>URL: /equipment/importEquipments</b>
     * <p>导入设备列表</p>
     */
    @RequestMapping("importEquipments")
    @RestReturn(value=ImportDataResponse.class)
    public RestResponse importEquipments(ImportOwnerCommand cmd, @RequestParam(value = "attachment") MultipartFile[] files){
        Long userId = UserContext.currentUserId();
        if(null == files || null == files[0]){
			LOGGER.error("export files is null! userId="+userId);
			throw RuntimeErrorException.errorWith(UserServiceErrorCode.SCOPE, UserServiceErrorCode.ERROR_INVALID_PARAMS,
					"export files is null");
		}
		ImportDataResponse importResponse = this.equipmentService.importEquipments(cmd, files[0], userId);
        RestResponse response = new RestResponse(importResponse);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

	
	/**
	 * <b>URL: /equipment/updateEquipmentAccessories</b>
	 * <p>创建或修改备品备件</p>
	 */
	@RequestMapping("updateEquipmentAccessories")
	@RestReturn(value = EquipmentAccessoriesDTO.class)
	public RestResponse updateEquipmentAccessories(UpdateEquipmentAccessoriesCommand cmd) {
		EquipmentAccessoriesDTO accessories = equipmentService.updateEquipmentAccessories(cmd);
        return getRestResponse(accessories);
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
        return getRestResponse(accessory);
    }
	
	/**
	 * <b>URL: /equipment/searchEquipmentAccessories</b>
	 * <p>查看备品备件</p>
	 */
	@RequestMapping("searchEquipmentAccessories")
	@RestReturn(value = SearchEquipmentAccessoriesResponse.class)
	public RestResponse searchEquipmentAccessories(SearchEquipmentAccessoriesCommand cmd) {
		SearchEquipmentAccessoriesResponse accessories = equipmentAccessoriesSearcher.query(cmd);
        return getRestResponse(accessories);
    }

	/**
	 * <b>URL: /equipment/exportEquipmentAccessories</b>
	 * <p>导出备品备件表</p>
	 */
	@RequestMapping("exportEquipmentAccessories")
	public HttpServletResponse exportEquipmentAccessories(@Valid SearchEquipmentAccessoriesCommand cmd,HttpServletResponse response) {
		  return  equipmentService.exportEquipmentAccessories(cmd, response);
	}
	
	/**
     * <b>URL: /equipment/importEquipmentAccessories</b>
     * <p>导入备品备件表</p>
     */
    @RequestMapping("importEquipmentAccessories")
    @RestReturn(value = ImportDataResponse.class)
    public RestResponse importEquipmentAccessories(ImportOwnerCommand cmd, @RequestParam(value = "attachment") MultipartFile[] files) {
        Long userId = UserContext.current().getUser().getId();
        if (null == files || null == files[0]) {
            LOGGER.error("files is null。userId=" + userId);
            throw RuntimeErrorException.errorWith(UserServiceErrorCode.SCOPE, UserServiceErrorCode.ERROR_INVALID_PARAMS,
                    "files is null");
        }
        ImportDataResponse importDataResponse = this.equipmentService.importEquipmentAccessories(cmd, files[0], userId);

        return getRestResponse(importDataResponse);
    }

    /**
     * <b>URL: /equipment/createEquipmentInspectionPlan</b>
     * <p>创建巡检计划 V3.0.3</p>
     */
    @RequestMapping("createEquipmentInspectionPlan")
    @RestReturn(value = EquipmentInspectionPlanDTO.class)
    public RestResponse createEquipmentInspectionPlan(UpdateEquipmentPlanCommand cmd) {
        EquipmentInspectionPlanDTO equipmentInspectionPlansDTO = equipmentService.createEquipmentsInspectionPlan(cmd);
        return getRestResponse(equipmentInspectionPlansDTO);
    }

    /**
     * <b>URL: /equipment/updateEquipmentInspectionPlan</b>
     * <p>更新巡检计划 V3.0.3</p>
     */
    @RequestMapping("updateEquipmentInspectionPlan")
    @RestReturn(value = String.class)
    public RestResponse updateEquipmentInspectionPlan(UpdateEquipmentPlanCommand cmd) {
        EquipmentInspectionPlanDTO equipmentInspectionPlansDTO = equipmentService.updateEquipmentInspectionPlan(cmd);
        return getRestResponse(equipmentInspectionPlansDTO);
    }

    /**
     * <b>URL: /equipment/getEquipmentInspectionPlan</b>
     * <p>根据id查询巡检计划</p>
     */
    @RequestMapping("getEquipmentInspectionPlan")
    @RestReturn(value = EquipmentInspectionPlanDTO.class)
    public RestResponse getEquipmentInspectionPlan(DeleteEquipmentPlanCommand cmd) {
        EquipmentInspectionPlanDTO equipmentInspectionPlans = equipmentService.getEquipmmentInspectionPlanById(cmd);
        return getRestResponse(equipmentInspectionPlans);
    }

    /**
     * <b>URL: /equipment/deleteEquipmentInspectionPlan</b>
     * <p>根据id删除巡检计划</p>
     */
    @RequestMapping("deleteEquipmentInspectionPlan")
    @RestReturn(value = String.class)
    public RestResponse deleteEquipmentInspectionPlan(DeleteEquipmentPlanCommand cmd) {
        equipmentService.deleteEquipmentInspectionPlan(cmd);
        return  getSuccessResponse();
    }

    /**
     * <b>URL: /equipment/searchEquipmentInspectionPlans</b>
     * <p>巡检计划列表</p>
     */
    @RequestMapping("searchEquipmentInspectionPlans")
    @RestReturn(value = searchEquipmentInspectionPlansResponse.class)
    public RestResponse searchEquipmentInspectionPlans(SearchEquipmentInspectionPlansCommand cmd) {
        searchEquipmentInspectionPlansResponse equipmentInspectionPlansResponse = equipmentPlanSearcher.query(cmd);
        return getRestResponse(equipmentInspectionPlansResponse);
    }

    /**
     * <b>URL: /equipment/reviewEquipmentInspectionplan</b>
     * <p>巡检计划审批</p>
     */
    @RequestMapping("reviewEquipmentInspectionplan")
    @RestReturn(value = String.class)
    public RestResponse reviewEquipmentInspectionplan(ReviewEquipmentPlanCommand cmd) {
        equipmentService.reviewEquipmentInspectionplan(cmd);
        return getSuccessResponse();
    }

    /**
     * <b>URL: /equipment/exportEquipmentInspectionPlans</b>
     * <p>导出计划表</p>
     */
    @RequestMapping("exportEquipmentInspectionPlans")
    public RestResponse exportEquipmentInspectionPlans(SearchEquipmentInspectionPlansCommand cmd, HttpServletResponse response) {
        equipmentService.exportInspectionPlans(cmd, response);
        return getSuccessResponse();
    }

    /**
     * <b>URL: /equipment/exportEquipmentTasks</b>
     * <p>导出任务</p>
     */
    @RequestMapping("exportEquipmentTasks")
    public HttpServletResponse exportEquipmentTasks(@Valid SearchEquipmentTasksCommand cmd, HttpServletResponse response) {
       return  equipmentService.exportEquipmentTasks(cmd, response);
    }

    /**
     * <b>URL: /equipment/searchEquipmentTasks</b>
     * <p>查看任务列表-后台</p>
     */
    @RequestMapping("searchEquipmentTasks")
    @RestReturn(value = ListEquipmentTasksResponse.class)
    public RestResponse searchEquipmentTasks(SearchEquipmentTasksCommand cmd) {
        ListEquipmentTasksResponse tasks = equipmentTasksSearcher.query(cmd);
        return getRestResponse(tasks);
    }

    /**
     * <b>URL: /equipment/listEquipmentTasks</b>
     * <p>查看任务列表-app</p>
     */
    @RequestMapping("listEquipmentTasks")
    @RestReturn(value = ListEquipmentTasksResponse.class)
    public RestResponse listEquipmentTasks(ListEquipmentTasksCommand cmd) {
        ListEquipmentTasksResponse tasks = equipmentService.listEquipmentTasks(cmd);
        return getRestResponse(tasks);
    }

    /**
     * <b>URL: /equipment/listEquipmentTasksDetails</b>
     * <p>获取任务所有信息 用于离线</p>
     */
    @RequestMapping("listEquipmentTasksDetails")
    @RestReturn(value = EquipmentTaskOfflineResponse.class)
    public RestResponse listEquipmentTasksDetails(ListEquipmentTasksCommand cmd) {
        EquipmentTaskOfflineResponse tasksDetail = equipmentService.listEquipmentTasksDetails(cmd);
        return getRestResponse(tasksDetail);
    }

    /**
     * <b>URL: /equipment/listTasksByEquipmentId</b>
     * <p>查看设备任务</p>
     */
    @RequestMapping("listTasksByEquipmentId")
    @RestReturn(value = ListEquipmentTasksResponse.class)
    public RestResponse listTasksByEquipmentId(ListTasksByEquipmentIdCommand cmd) {
        ListEquipmentTasksResponse tasks = equipmentService.listTasksByEquipmentId(cmd);
        return getRestResponse(tasks);
    }

    /**
     * <b>URL: /equipment/listTaskById</b>
     * <p>根据id查看任务</p>
     */
    @RequestMapping("listTaskById")
    @RestReturn(value = EquipmentTaskDTO.class)
    public RestResponse listTaskById(ListTaskByIdCommand cmd) {
        EquipmentTaskDTO task = equipmentService.listTaskById(cmd);
        return getRestResponse(task);
    }

    /**
     * <b>URL: /equipment/listEquipmentStandardRelationsByTaskId</b>
     * <p>查看具体任务下设备标准关系列表</p>
     */
    @RequestMapping("listEquipmentStandardRelationsByTaskId")
    @RestReturn(value = EquipmentStandardRelationDTO.class, collection = true)
    public RestResponse listEquipmentStandardRelationsByTaskId(ListTaskByIdCommand cmd) {
        List<EquipmentStandardRelationDTO> equipments = equipmentService.listEquipmentStandardRelationsByTaskId(cmd);
        return getRestResponse(equipments);
    }

    /**
     * <b>URL: /equipment/listParametersByStandardId</b>
     * <p>查看设备所需记录的参数</p>
     */
    @RequestMapping("listParametersByStandardId")
    @RestReturn(value = InspectionItemDTO.class, collection = true)
    public RestResponse listParametersByStandardId(ListParametersByStandardIdCommand cmd) {
        List<InspectionItemDTO> items = equipmentService.listParametersByStandardId(cmd);
        return getRestResponse(items);
    }

    /**
     * <b>URL: /equipment/reportEquipmentTask</b>
     * <p>任务上报</p>
     */
    @RequestMapping("reportEquipmentTask")
    @RestReturn(value = EquipmentTaskDTO.class)
    public RestResponse reportEquipmentTask(ReportEquipmentTaskCommand cmd) {
        EquipmentTaskDTO dto = equipmentService.reportEquipmentTask(cmd);
        return getRestResponse(dto);
    }

    /**
     * <b>URL: /equipment/offlineEquipmentTaskReport</b>
     * <p>离线任务上报</p>
     */
    @RequestMapping("offlineEquipmentTaskReport")
    @RestReturn(value = EquipmentTaskDTO.class)
    public RestResponse offlineEquipmentTaskReport(OfflineEquipmentTaskReportCommand cmd) {
        OfflineEquipmentTaskReportResponse reportResponse = equipmentService.offlineEquipmentTaskReport(cmd);
        return getRestResponse(reportResponse);
    }

    /**
     * <b>URL: /equipment/reviewEquipmentTask</b>
     * <p>任务审阅</p>
     */
    @RequestMapping("reviewEquipmentTask")
    @RestReturn(value = String.class)
    public RestResponse reviewEquipmentTask(ReviewEquipmentTaskCommand cmd) {

        equipmentService.reviewEquipmentTask(cmd);

        return getSuccessResponse();
    }

    /**
     * <b>URL: /equipment/reviewEquipmentTasks</b>
     * <p>任务批量审阅</p>
     */
    @RequestMapping("reviewEquipmentTasks")
    @RestReturn(value = String.class)
    public RestResponse reviewEquipmentTasks(ReviewEquipmentTasksCommand cmd) {
        equipmentService.reviewEquipmentTasks(cmd);
        return getSuccessResponse();
    }

    /**
     * <b>URL: /equipment/listLogsByTaskId</b>
     * <p>查看任务的操作记录</p>
     */
    @RequestMapping("listLogsByTaskId")
    @RestReturn(value = ListLogsByTaskIdResponse.class)
    public RestResponse listLogsByTaskId(ListLogsByTaskIdCommand cmd) {
        ListLogsByTaskIdResponse records = equipmentService.listLogsByTaskId(cmd);
        return getRestResponse(records);
    }

    /**
     * <b>URL: /equipment/exportTaskLogs</b>
     * <p>导出任务记录</p>
     */
    @RequestMapping("exportTaskLogs")
    @RestReturn(value = String.class)
    public RestResponse exportTaskLogs(ExportTaskLogsCommand cmd, HttpServletResponse response) {
        equipmentService.exportTaskLogs(cmd, response);
        return getSuccessResponse();
    }

    /**
     * <b>URL: /equipment/verifyEquipmentLocation</b>
     * <p>扫一扫验证二维码和经纬度</p>
     */
    @RequestMapping("verifyEquipmentLocation")
    @RestReturn(value = VerifyEquipmentLocationResponse.class)
    public RestResponse verifyEquipmentLocation(VerifyEquipmentLocationCommand cmd) {

        VerifyEquipmentLocationResponse resp = equipmentService.verifyEquipmentLocation(cmd);

        return getRestResponse(resp);
    }

    /**
     * <b>URL: /equipment/listAttachmentsByEquipmentId</b>
     * <p>查看设备操作图示或说明书</p>
     */
    @RequestMapping("listAttachmentsByEquipmentId")
    @RestReturn(value = EquipmentAttachmentDTO.class, collection = true)
    public RestResponse listAttachmentsByEquipmentId(ListAttachmentsByEquipmentIdCommand cmd) {
        List<EquipmentAttachmentDTO> equipmentAttachments = equipmentService.listAttachmentsByEquipmentId(cmd);
        return getRestResponse(equipmentAttachments);
    }

    /**
     * <b>URL: /equipment/syncEquipmentStandardIndex</b>
     * <p>搜索索引同步</p>
     * @return {String.class}
     */
    @RequestMapping("syncEquipmentStandardIndex")
    @RestReturn(value = String.class)
    public RestResponse syncEquipmentStandardIndex() {
        UserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
        resolver.checkUserPrivilege(UserContext.current().getUser().getId(), 0);
        equipmentStandardSearcher.syncFromDb();
        return getSuccessResponse();
    }

    /**
     * <b>URL: /equipment/syncEquipmentIndex</b>
     * <p>搜索索引同步</p>
     * @return {String.class}
     */
    @RequestMapping("syncEquipmentIndex")
    @RestReturn(value = String.class)
    public RestResponse syncEquipmentIndex() {
        UserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
        resolver.checkUserPrivilege(UserContext.current().getUser().getId(), 0);
        equipmentSearcher.syncFromDb();
        return getSuccessResponse();
    }

    /**
     * <b>URL: /equipment/syncEquipmentAccessoriesIndex</b>
     * <p>搜索索引同步</p>
     * @return {String.class}
     */
    @RequestMapping("syncEquipmentAccessoriesIndex")
    @RestReturn(value = String.class)
    public RestResponse syncEquipmentAccessoriesIndex() {
        UserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
        resolver.checkUserPrivilege(UserContext.current().getUser().getId(), 0);
        equipmentAccessoriesSearcher.syncFromDb();
        return getSuccessResponse();
    }

    /**
     * <b>URL: /equipment/syncEquipmentTasksIndex</b>
     * <p>搜索索引同步</p>
     * @return {String.class}
     */
    @RequestMapping("syncEquipmentTasksIndex")
    @RestReturn(value = String.class)
    public RestResponse syncEquipmentTasksIndex() {
        UserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
        resolver.checkUserPrivilege(UserContext.current().getUser().getId(), 0);
        equipmentTasksSearcher.syncFromDb();
        return getSuccessResponse();
    }

    /**
     * <b>URL: /equipment/syncEquipmentPlansIndex</b>
     * <p>搜索索引同步</p>
     *
     * @return {String.class}
     */
    @RequestMapping("syncEquipmentPlansIndex")
    @RestReturn(value = String.class)
    public RestResponse syncEquipmentPlansIndex() {
        UserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
        resolver.checkUserPrivilege(UserContext.current().getUser().getId(), 0);
        equipmentPlanSearcher.syncFromDb();
        return getSuccessResponse();
    }
    
    /**
     * <b>URL: /equipment/syncEquipmentStandardMapIndex</b>
     * <p>搜索索引同步</p>
     * @return {String.class}
     */
    @RequestMapping("syncEquipmentStandardMapIndex")
    @RestReturn(value=String.class)
    public RestResponse syncEquipmentStandardMapIndex() {
        UserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
        resolver.checkUserPrivilege(UserContext.current().getUser().getId(), 0);
        equipmentStandardMapSearcher.syncFromDb();
        return getSuccessResponse();
    }
    
//    /**
//     * <b>URL: /equipment/updateEquipmentCategory</b>
//     * <p>修改设备类型</p>
//     */
//    @RequestMapping("updateEquipmentCategory")
//    @RestReturn(value = String.class)
//    public RestResponse updateEquipmentCategory(UpdateEquipmentCategoryCommand cmd) {
//        equipmentService.updateEquipmentCategory(cmd);
//        return getSuccessResponse();
//    }
//
//    /**
//     * <b>URL: /equipment/createEquipmentCategory</b>
//     * <p>新建设备类型</p>
//     */
//    @RequestMapping("createEquipmentCategory")
//    @RestReturn(value = String.class)
//    public RestResponse createEquipmentCategory(CreateEquipmentCategoryCommand cmd) {
//        equipmentService.createEquipmentCategory(cmd);
//        return getSuccessResponse();
//    }
//
//    /**
//     * <b>URL: /equipment/listEquipmentsCategories</b>
//     * <p>查看设备类型</p>
//     */
//    @RequestMapping("listEquipmentsCategories")
//    @RestReturn(value = CategoryDTO.class, collection = true)
//    public RestResponse listEquipmentsCategories() {
//        List<CategoryDTO> categories = equipmentService.listEquipmentsCategories();
//        return getRestResponse(categories);
//    }
//
//
//    /**
//     * <b>URL: /pmtask/deleteEquipmentCategory</b>
//     * <p>删除设备类型</p>
//     */
//    @RequestMapping("deleteEquipmentCategory")
//    @RestReturn(value = String.class)
//    public RestResponse deleteEquipmentCategory(DeleteEquipmentCategoryCommand cmd) {
//        equipmentService.deleteEquipmentCategory(cmd);
//        return getSuccessResponse();
//    }


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
	
//	/**
//	 * <b>URL: /equipment/listEquipmentInspectionCategories</b>
//	 * <p>查看巡检对象类型</p>
//	 */
//	@RequestMapping("listEquipmentInspectionCategories")
//	@RestReturn(value = EquipmentInspectionCategoryDTO.class, collection = true)
//	public RestResponse listEquipmentInspectionCategories(ListEquipmentInspectionCategoriesCommand cmd) {
//		List<EquipmentInspectionCategoryDTO> categories = equipmentService.listEquipmentInspectionCategories(cmd);
//		RestResponse response = new RestResponse(categories);
//		response.setErrorCode(ErrorCodes.SUCCESS);
//		response.setErrorDescription("OK");
//		return response;
//	}
	
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
	 * <p>任务数统计(按照设备标准维度统计 3.0.3无法统计)</p>
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
        return getSuccessResponse();
    }

    /**
     * <b>URL: /equipment/statTodayEquipmentTasks</b>
     * <p>当天任务数统计</p>
     */
    @RequestMapping("statTodayEquipmentTasks")
    @RestReturn(value = StatTodayEquipmentTasksResponse.class)
    public RestResponse statTodayEquipmentTasks(StatTodayEquipmentTasksCommand cmd) {
        StatTodayEquipmentTasksResponse stat = equipmentService.statTodayEquipmentTasks(cmd);
        return getRestResponse(stat);
    }

    /**
     * <b>URL: /equipment/statLastDaysEquipmentTasks</b>
     * <p>最近几天任务数统计</p>
     */
    @RequestMapping("statLastDaysEquipmentTasks")
    @RestReturn(value = StatLastDaysEquipmentTasksResponse.class)
    public RestResponse statLastDaysEquipmentTasks(StatLastDaysEquipmentTasksCommand cmd) {
        StatLastDaysEquipmentTasksResponse stat = equipmentService.statLastDaysEquipmentTasks(cmd);
        return getRestResponse(stat);
    }

    /**
     * <b>URL: /equipment/statIntervalAllEquipmentTasks</b>
     * <p>区间任务数统计</p>
     */
    @RequestMapping("statIntervalAllEquipmentTasks")
    @RestReturn(value = StatIntervalAllEquipmentTasksResponse.class)
    public RestResponse statIntervalAllEquipmentTasks(StatIntervalAllEquipmentTasksCommand cmd) {
        StatIntervalAllEquipmentTasksResponse stat = equipmentService.statIntervalAllEquipmentTasks(cmd);
        return getRestResponse(stat);
    }

    /**
     * <b>URL: /equipment/listAbnormalTasks</b>
     * <p>查看异常任务列表</p>
     */
    @RequestMapping("listAbnormalTasks")
    @RestReturn(value = ListEquipmentTasksResponse.class)
    public RestResponse listAbnormalTasks(ListAbnormalTasksCommand cmd) {
        ListEquipmentTasksResponse tasks = equipmentService.listAbnormalTasks(cmd);
        return getRestResponse(tasks);
    }

    /**
     * <b>URL: /equipment/setPmNotifyParams</b>
     * <p>设置通知参数</p>
     */
    @RequestMapping("setPmNotifyParams")
    @RestReturn(value = String.class)
    public RestResponse setPmNotifyParams(SetPmNotifyParamsCommand cmd) {
        equipmentService.setPmNotifyParams(cmd);
        return getSuccessResponse();
    }

    /**
     * <b>URL: /equipment/listPmNotifyParams</b>
     * <p>列出通知参数</p>
     */
    @RequestMapping("listPmNotifyParams")
    @RestReturn(value = PmNotifyParamDTO.class, collection = true)
    public RestResponse listPmNotifyParams(ListPmNotifyParamsCommand cmd) {
        List<PmNotifyParamDTO> pmNotifyParams = equipmentService.listPmNotifyParams(cmd);
        return getRestResponse(pmNotifyParams);
    }

    /**
     * <b>URL: /equipment/deletePmNotifyParams</b>
     * <p>删除通知参数</p>
     */
    @RequestMapping("deletePmNotifyParams")
    @RestReturn(value = String.class)
    public RestResponse deletePmNotifyParams(DeletePmNotifyParamsCommand cmd) {
        equipmentService.deletePmNotifyParams(cmd);
        return getSuccessResponse();
    }
    /**
     * <b>URL: /equipment/setReviewExpireDays</b>
     * <p>设置任务审批过期时间</p>
     */
    @RequestMapping("setReviewExpireDays")
    @RestReturn(value = String.class)
    public RestResponse setReviewExpireDays(SetReviewExpireDaysCommand cmd) {
        equipmentService.setReviewExpireDays(cmd);
        return getSuccessResponse();
    }
    /**
     * <b>URL: /equipment/deleteReviewExpireDays</b>
     * <p>删除任务审批过期时间</p>
     */
    @RequestMapping("deleteReviewExpireDays")
    @RestReturn(value = String.class)
    public RestResponse deleteReviewExpireDays(SetReviewExpireDaysCommand cmd) {
        equipmentService.deleteReviewExpireDays(cmd);
        return getSuccessResponse();
    }
    /**
     * <b>URL: /equipment/listReviewExpireDays</b>
     * <p>列出任务审批过期时间</p>
     */
    @RequestMapping("listReviewExpireDays")
    @RestReturn(value = EquipmentInspectionReviewDateDTO.class)
    public RestResponse listReviewExpireDays(SetReviewExpireDaysCommand cmd) {
        EquipmentInspectionReviewDateDTO reviewDate = equipmentService.listReviewExpireDays(cmd);
        return getRestResponse(reviewDate);
    }

    private RestResponse getRestResponse(Object obj) {
        RestResponse response = new RestResponse(obj);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

    private RestResponse getSuccessResponse() {
        RestResponse response = new RestResponse();
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

	/**
	 * <b>URL: /equipment/findScopeFieldItemByBusinessValue</b>
	 * <p>根据业务值获取item信息</p>
	 */
	@RequestMapping("findScopeFieldItemByBusinessValue")
	@RestReturn(value = FieldItemDTO.class)
	public RestResponse findScopeFieldItemByBusinessValue (findScopeFieldItemCommand cmd) {
		RestResponse response = new RestResponse(equipmentService.findScopeFieldItemByFieldItemId(cmd));
		response.setErrorCode(ErrorCodes.SUCCESS);
		response.setErrorDescription("OK");
		return response;
	}

    /**
     * <b>URL: /equipment/createRepairsTask</b>
     * <p>创建物业报修任务</p>
     */
    @RequestMapping("createRepairsTask")
    @RestReturn(value = EquipmentTaskDTO.class)
    public RestResponse createRepairsTask(CreateEquipmentRepairCommand cmd) {
        equipmentService.createRepairsTask(cmd);
        return getSuccessResponse();
    }

    /**
     * <b>URL: /equipment/createTaskByPlan</b>
     * <p>创建指定计划的任务</p>
     */
    @RequestMapping("createTaskByPlan")
    @RestReturn(value = String.class)
    public RestResponse createTaskByPlan(DeleteEquipmentPlanCommand cmd) {
        equipmentService.createTaskByPlan(cmd);
        return getSuccessResponse();
    }

//    /**
//     * <b>URL: /equipment/syncStandardToEqiupmentPlan</b>
//     * <p>同步所有的标准-设备关联表到计划中</p>
//     */
//    @RequestMapping("syncStandardToEqiupmentPlan")
//    @RestReturn(value = String.class)
//    public RestResponse syncStandardToEqiupmentPlan() {
//        UserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
//        resolver.checkUserPrivilege(UserContext.current().getUser().getId(), 0);
//        equipmentService.syncStandardToEqiupmentPlan();
//        return getSuccessResponse();
//    }

    /**
     * <b>URL: /equipment/startCrontabTask</b>
     * <p>上线时间问题临时接口启动任务生成</p>
     */
    @RequestMapping("startCrontabTask")
    @RestReturn(value = String.class)
    public RestResponse startCrontabTask() {
        UserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
        resolver.checkUserPrivilege(UserContext.current().getUser().getId(), 0);
        equipmentService.startCrontabTask();
        return getSuccessResponse();
    }

}
