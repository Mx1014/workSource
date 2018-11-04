package com.everhomes.energy;

import com.everhomes.acl.AclProvider;
import com.everhomes.acl.Privilege;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.energy.BatchReadEnergyMeterCommand;
import com.everhomes.rest.energy.BatchUpdateEnergyMeterSettingsCommand;
import com.everhomes.rest.energy.CalculateTaskFeeByTaskIdCommand;
import com.everhomes.rest.energy.ChangeEnergyMeterCommand;
import com.everhomes.rest.energy.CreateEnergyMeterCategoryCommand;
import com.everhomes.rest.energy.CreateEnergyMeterCommand;
import com.everhomes.rest.energy.CreateEnergyMeterDefaultSettingCommand;
import com.everhomes.rest.energy.CreateEnergyMeterPriceConfigCommand;
import com.everhomes.rest.energy.CreateEnergyTaskCommand;
import com.everhomes.rest.energy.DelelteEnergyMeterPriceConfigCommand;
import com.everhomes.rest.energy.DeleteEnergyMeterCategoryCommand;
import com.everhomes.rest.energy.DeleteEnergyMeterFormulaCommand;
import com.everhomes.rest.energy.DeleteEnergyMeterReadingLogCommand;
import com.everhomes.rest.energy.DeleteEnergyPlanCommand;
import com.everhomes.rest.energy.EnergyCommunityYoyStatDTO;
import com.everhomes.rest.energy.EnergyFormulaVariableDTO;
import com.everhomes.rest.energy.EnergyMeterCategoryDTO;
import com.everhomes.rest.energy.EnergyMeterChangeLogDTO;
import com.everhomes.rest.energy.EnergyMeterDTO;
import com.everhomes.rest.energy.EnergyMeterDefaultSettingDTO;
import com.everhomes.rest.energy.EnergyMeterDefaultSettingTemplateDTO;
import com.everhomes.rest.energy.EnergyMeterFormulaDTO;
import com.everhomes.rest.energy.EnergyMeterPriceConfigDTO;
import com.everhomes.rest.energy.EnergyMeterReadingLogDTO;
import com.everhomes.rest.energy.EnergyMeterSettingLogDTO;
import com.everhomes.rest.energy.EnergyPlanDTO;
import com.everhomes.rest.energy.EnergyStatByYearDTO;
import com.everhomes.rest.energy.EnergyStatCommand;
import com.everhomes.rest.energy.EnergyStatDTO;
import com.everhomes.rest.energy.ExportEnergyMeterQRCodeCommand;
import com.everhomes.rest.energy.FindEnergyMeterByQRCodeCommand;
import com.everhomes.rest.energy.FindEnergyPlanDetailsCommand;
import com.everhomes.rest.energy.GetEnergyMeterCommand;
import com.everhomes.rest.energy.GetEnergyMeterPriceConfigCommand;
import com.everhomes.rest.energy.GetEnergyMeterQRCodeCommand;
import com.everhomes.rest.energy.ImportEnergyMeterCommand;
import com.everhomes.rest.energy.ImportTasksByEnergyPlanCommand;
import com.everhomes.rest.energy.ListEnergyDefaultSettingsCommand;
import com.everhomes.rest.energy.ListEnergyMeterCategoriesCommand;
import com.everhomes.rest.energy.ListEnergyMeterChangeLogsCommand;
import com.everhomes.rest.energy.ListEnergyMeterFormulasCommand;
import com.everhomes.rest.energy.ListEnergyMeterPriceConfigCommand;
import com.everhomes.rest.energy.ListEnergyMeterSettingLogsCommand;
import com.everhomes.rest.energy.ListEnergyPlanMetersCommand;
import com.everhomes.rest.energy.ListEnergyPlanMetersResponse;
import com.everhomes.rest.energy.ListUserEnergyPlanTasksCommand;
import com.everhomes.rest.energy.ListUserEnergyPlanTasksResponse;
import com.everhomes.rest.energy.ReadEnergyMeterCommand;
import com.everhomes.rest.energy.ReadTaskMeterCommand;
import com.everhomes.rest.energy.ReadTaskMeterOfflineCommand;
import com.everhomes.rest.energy.ReadTaskMeterOfflineResponse;
import com.everhomes.rest.energy.SearchEnergyMeterCommand;
import com.everhomes.rest.energy.SearchEnergyMeterReadingLogsCommand;
import com.everhomes.rest.energy.SearchEnergyMeterReadingLogsResponse;
import com.everhomes.rest.energy.SearchEnergyMeterResponse;
import com.everhomes.rest.energy.SearchEnergyPlansCommand;
import com.everhomes.rest.energy.SearchEnergyPlansResponse;
import com.everhomes.rest.energy.SearchTasksByEnergyPlanCommand;
import com.everhomes.rest.energy.SearchTasksByEnergyPlanResponse;
import com.everhomes.rest.energy.SetEnergyPlanMeterOrderCommand;
import com.everhomes.rest.energy.SyncOfflineDataCommand;
import com.everhomes.rest.energy.SyncOfflineDataResponse;
import com.everhomes.rest.energy.UpdateEnergyMeterCategoryCommand;
import com.everhomes.rest.energy.UpdateEnergyMeterCommand;
import com.everhomes.rest.energy.UpdateEnergyMeterDefaultSettingCommand;
import com.everhomes.rest.energy.UpdateEnergyMeterPriceConfigCommand;
import com.everhomes.rest.energy.UpdateEnergyMeterStatusCommand;
import com.everhomes.rest.energy.UpdateEnergyPlanCommand;
import com.everhomes.rest.organization.ImportFileTaskDTO;
import com.everhomes.rest.user.UserServiceErrorCode;
import com.everhomes.scheduler.EnergyTaskScheduleJob;
import com.everhomes.search.EnergyMeterTaskSearcher;
import com.everhomes.search.EnergyPlanSearcher;
import com.everhomes.server.schema.tables.pojos.EhUsers;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
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
import java.util.Date;

/**
 * 能耗管理
 * Created by xq.tian on 2016/10/25.
 */
@RestDoc("Energy consumption controller")
@RequestMapping("/energy")
@RestController
public class EnergyConsumptionController extends ControllerBase {
    private static final Logger LOGGER = LoggerFactory.getLogger(EnergyConsumptionController.class);

    @Autowired
    private EnergyConsumptionService energyConsumptionService;

    @Autowired
    private EnergyPlanSearcher energyPlanSearcher;

    @Autowired
    private EnergyMeterTaskSearcher energyMeterTaskSearcher;

    @Autowired
    private EnergyTaskScheduleJob energyTaskScheduleJob;
    
    @Autowired
    private AclProvider aclProvider;

    /**
     * <p>新建表记(水表, 电表等)</p>
     * <b>URL: /energy/createEnergyMeter</b>
     */
    @RestReturn(EnergyMeterDTO.class)
    @RequestMapping("createEnergyMeter")
    public RestResponse createEnergyMeter(CreateEnergyMeterCommand cmd) {
        return response(energyConsumptionService.createEnergyMeter(cmd));
    }

    /**
     * <p>修改表记(水表, 电表等),表记分类, 性质, 账单项目不能修改</p>
     * <b>URL: /energy/updateEnergyMeter</b>
     */
    @RestReturn(EnergyMeterDTO.class)
    @RequestMapping("updateEnergyMeter")
    public RestResponse updateEnergyMeter(UpdateEnergyMeterCommand cmd) {
        return response(energyConsumptionService.updateEnergyMeter(cmd));
    }

    /**
     * <p>搜索表记</p>
     * <b>URL: /energy/searchEnergyMeter</b>
     */
    @RestReturn(SearchEnergyMeterResponse.class)
    @RequestMapping("searchEnergyMeter")
    public RestResponse searchEnergyMeter(SearchEnergyMeterCommand cmd) {
        return response(energyConsumptionService.searchEnergyMeter(cmd));
    }

    /**
     * <p>搜索表记简单信息</p>
     * <b>URL: /energy/searchSimpleEnergyMeter</b>
     */
    @RestReturn(SearchEnergyMeterResponse.class)
    @RequestMapping("searchSimpleEnergyMeter")
    public RestResponse searchSimpleEnergyMeter(SearchEnergyMeterCommand cmd) {
        return response(energyConsumptionService.searchSimpleEnergyMeter(cmd));
    }

    /**
     * <p>更改表记状态(删除, 报废)</p>
     * <b>URL: /energy/updateEnergyMeterStatus</b>
     */
    @RestReturn(String.class)
    @RequestMapping("updateEnergyMeterStatus")
    public RestResponse updateEnergyMeterStatus(UpdateEnergyMeterStatusCommand cmd) {
        energyConsumptionService.updateEnergyMeterStatus(cmd);
        return success();
    }

    /**
     * <p>获取表记的信息</p>
     * <b>URL: /energy/getEnergyMeter</b>
     */
    @RestReturn(EnergyMeterDTO.class)
    @RequestMapping("getEnergyMeter")
    public RestResponse getEnergyMeter(GetEnergyMeterCommand cmd) {
        return response(energyConsumptionService.getEnergyMeter(cmd));
    }

    /**
     * <p>换表</p>
     * <b>URL: /energy/changeEnergyMeter</b>
     */
    @RestReturn(String.class)
    @RequestMapping("changeEnergyMeter")
    public RestResponse changeEnergyMeter(ChangeEnergyMeterCommand cmd) {
        energyConsumptionService.changeEnergyMeter(cmd);
        return success();
    }

    /**
     * <p>抄表</p>
     * <b>URL: /energy/readEnergyMeter</b>
     */
    @RestReturn(EnergyMeterReadingLogDTO.class)
    @RequestMapping("readEnergyMeter")
    public RestResponse readEnergyMeter(ReadEnergyMeterCommand cmd) {
        return response(energyConsumptionService.readEnergyMeter(cmd));
    }

    /**
     * <p>批量修改表记属性</p>
     * <b>URL: /energy/batchUpdateEnergyMeterSettings</b>
     */
    @RestReturn(String.class)
    @RequestMapping("batchUpdateEnergyMeterSettings")
    public RestResponse batchUpdateEnergyMeterSettings(BatchUpdateEnergyMeterSettingsCommand cmd) {
        energyConsumptionService.batchUpdateEnergyMeterSettings(cmd);
        return success();
    }

    /**
     * <p>搜索读表记录</p>
     * <b>URL: /energy/searchEnergyMeterReadingLogs</b>
     */
    @RestReturn(SearchEnergyMeterReadingLogsResponse.class)
    @RequestMapping("searchEnergyMeterReadingLogs")
    public RestResponse searchEnergyMeterReadingLogs(SearchEnergyMeterReadingLogsCommand cmd) {
        return response(energyConsumptionService.searchEnergyMeterReadingLogs(cmd));
    }

    /**
     * <p>删除读表记录(只能删除当天的记录)</p>
     * <b>URL: /energy/deleteEnergyMeterReadingLog</b>
     */
    @RestReturn(String.class)
    @RequestMapping("deleteEnergyMeterReadingLog")
    public RestResponse deleteEnergyMeterReadingLog(DeleteEnergyMeterReadingLogCommand cmd) {
        energyConsumptionService.deleteEnergyMeterReadingLog(cmd);
        return success();
    }

    /**
     * <p>修改表记的默认属性值</p>
     * <b>URL: /energy/updateEnergyMeterDefaultSetting</b>
     */
    @RestReturn(EnergyMeterDefaultSettingDTO.class)
    @RequestMapping("updateEnergyMeterDefaultSetting")
    public RestResponse updateEnergyMeterDefaultSetting(UpdateEnergyMeterDefaultSettingCommand cmd) {
        return response(energyConsumptionService.updateEnergyMeterDefaultSetting(cmd));
    }

//    /**
//     * <p>新建计算公式</p>
//     * <b>URL: /energy/createEnergyMeterFormula</b>
//     */
//    @RestReturn(EnergyMeterFormulaDTO.class)
//    @RequestMapping("createEnergyMeterFormula")
//    public RestResponse createEnergyMeterFormula(CreateEnergyMeterFormulaCommand cmd) {
//        return response(energyConsumptionService.createEnergyMeterFormula(cmd));
//    }

    /**
     * <p>新建表记的分类(项目, 性质)</p>
     * <b>URL: /energy/createEnergyMeterCategory</b>
     */
    @RestReturn(EnergyMeterCategoryDTO.class)
    @RequestMapping("createEnergyMeterCategory")
    public RestResponse createEnergyMeterCategory(CreateEnergyMeterCategoryCommand cmd) {
        return response(energyConsumptionService.createEnergyMeterCategory(cmd));
    }

    /**
     * <p>修改表记的分类</p>
     * <b>URL: /energy/updateEnergyMeterCategory</b>
     */
    @RestReturn(EnergyMeterCategoryDTO.class)
    @RequestMapping("updateEnergyMeterCategory")
    public RestResponse updateEnergyMeterCategory(UpdateEnergyMeterCategoryCommand cmd) {
        return response(energyConsumptionService.updateEnergyMeterCategory(cmd));
    }

    /**
     * <p>删除表记的分类(项目, 性质)</p>
     * <b>URL: /energy/deleteEnergyMeterCategory</b>
     */
    @RestReturn(String.class)
    @RequestMapping("deleteEnergyMeterCategory")
    public RestResponse deleteEnergyMeterCategory(DeleteEnergyMeterCategoryCommand cmd) {
        energyConsumptionService.deleteEnergyMeterCategory(cmd);
        return success();
    }

    /**
     * <p>导入表记(Excel)</p>
     * <b>URL: /energy/importEnergyMeter</b>
     */
    @RestReturn(ImportFileTaskDTO.class)
    @RequestMapping("importEnergyMeter")
    public RestResponse importEnergyMeter(ImportEnergyMeterCommand cmd, @RequestParam(value = "attachment") MultipartFile[] files) {
        User manaUser = UserContext.current().getUser();
        Long userId = manaUser.getId();
        if (null == files || null == files[0]) {
            LOGGER.error("files is null。userId=" + userId);
            throw RuntimeErrorException.errorWith(UserServiceErrorCode.SCOPE, UserServiceErrorCode.ERROR_INVALID_PARAMS,
                    "files is null");
        }
        return response(energyConsumptionService.importEnergyMeter(cmd, files[0], userId));
    }

    /**
     * <p>表记分类列表</p>
     * <b>URL: /energy/listEnergyMeterCategories</b>
     */
    @RestReturn(value = EnergyMeterCategoryDTO.class, collection = true)
    @RequestMapping("listEnergyMeterCategories")
    public RestResponse listEnergyMeterCategories(ListEnergyMeterCategoriesCommand cmd) {
        return response(energyConsumptionService.listEnergyMeterCategories(cmd));
    }

    /**
     * <p>公式变量列表</p>
     * <b>URL: /energy/listEnergyFormulaVariables</b>
     */
    @RestReturn(value = EnergyFormulaVariableDTO.class, collection = true)
    @RequestMapping("listEnergyFormulaVariables")
    public RestResponse listEnergyFormulaVariables() {
        return response(energyConsumptionService.listEnergyFormulaVariables());
    }

    /**
     * <p>换表记录列表</p>
     * <b>URL: /energy/listEnergyMeterChangeLogs</b>
     */
    @RestReturn(value = EnergyMeterChangeLogDTO.class, collection = true)
    @RequestMapping("listEnergyMeterChangeLogs")
    public RestResponse listEnergyMeterChangeLogs(ListEnergyMeterChangeLogsCommand cmd) {
       return response(energyConsumptionService.listEnergyMeterChangeLogs(cmd));
    }

    /**
     * <p>setting记录列表</p>
     * <b>URL: /energy/listEnergyMeterSettingLogs</b>
     */
    @RestReturn(value = EnergyMeterSettingLogDTO.class, collection = true)
    @RequestMapping("listEnergyMeterSettingLogs")
    public RestResponse listEnergyMeterSettingLogs(ListEnergyMeterSettingLogsCommand cmd) {
       return response(energyConsumptionService.listEnergyMeterSettingLogs(cmd));
    }

    /**
     * <p>获取计算公式的列表</p>
     * <b>URL: /energy/listEnergyMeterFormulas</b>
     */
    @RestReturn(value = EnergyMeterFormulaDTO.class, collection = true)
    @RequestMapping("listEnergyMeterFormulas")
    public RestResponse listEnergyMeterFormulas(ListEnergyMeterFormulasCommand cmd) {
        return response(energyConsumptionService.listEnergyMeterFormulas(cmd));
    }

    /**
     * <p>删除公式</p>
     * <b>URL: /energy/deleteEnergyMeterFormula</b>
     */
    @RestReturn(value = String.class)
    @RequestMapping("deleteEnergyMeterFormula")
    public RestResponse deleteEnergyMeterFormula(DeleteEnergyMeterFormulaCommand cmd) {
        energyConsumptionService.deleteEnergyMeterFormula(cmd);
        return success();
    }

    /**
     * <p>同步表记索引</p>
     * <b>URL: /energy/syncEnergyMeterIndex</b>
     */
    @RestReturn(value = String.class)
    @RequestMapping("syncEnergyMeterIndex")
    public RestResponse syncEnergyMeterIndex() {
        energyConsumptionService.syncEnergyMeterIndex();
        return success();
    }

    /**
     * <p>同步读表记录索引</p>
     * <b>URL: /energy/syncEnergyMeterReadingLogIndex</b>
     */
    @RestReturn(value = String.class)
    @RequestMapping("syncEnergyMeterReadingLogIndex")
    public RestResponse syncEnergyMeterReadingLogIndex() {
        energyConsumptionService.syncEnergyMeterReadingLogIndex();
        return success();
    }

    /**
     * <p>获取默认设置</p>
     * <b>URL: /energy/listEnergyDefaultSettings</b>
     */
    @RestReturn(value = EnergyMeterDefaultSettingDTO.class, collection = true)
    @RequestMapping("listEnergyDefaultSettings")
    public RestResponse listEnergyDefaultSettings(ListEnergyDefaultSettingsCommand cmd) {
        return response(energyConsumptionService.listEnergyDefaultSettings(cmd));
    }

    /**
     * <p>每日水电总表</p>
     * <b>URL: /energy/getEnergyStatByDay</b>
     */
    @RestReturn(value = EnergyStatDTO.class)
    @RequestMapping("getEnergyStatByDay")
    public RestResponse getEnergyStatByDay(EnergyStatCommand cmd) {
       return response(energyConsumptionService.getEnergyStatByDay(cmd));
    }

    /**
     * <p>月度水电分析表</p>
     * <b>URL: /energy/getEnergyStatByMonth</b>
     */
    @RestReturn(value = EnergyStatDTO.class)
    @RequestMapping("getEnergyStatByMonth")
    public RestResponse getEnergyStatByMonth(EnergyStatCommand cmd) {
       return response(energyConsumptionService.getEnergyStatByMonth(cmd));
    }

    /**
     * <p>年度水电用量收支对比表</p>
     * <b>URL: /energy/getEnergyStatisticByYear</b>
     */
    @RestReturn(value = EnergyStatByYearDTO.class, collection = true)
    @RequestMapping("getEnergyStatisticByYear")
    public RestResponse getEnergyStatisticByYear(EnergyStatCommand cmd) {
       return response(energyConsumptionService.getEnergyStatisticByYear(cmd));
    }

    /**
     * <p>各项目月水电能耗情况（与去年同期相比)</p>
     * <b>URL: /energy/getEnergyStatisticByYoy</b>
     */
    @RestReturn(value = EnergyCommunityYoyStatDTO.class, collection = true)
    @RequestMapping("getEnergyStatisticByYoy")
    public RestResponse getEnergyStatisticByYoy(EnergyStatCommand cmd) {
       return response(energyConsumptionService.getEnergyStatisticByYoy(cmd));
    }

    private RestResponse success() {
        RestResponse response = new RestResponse();
        response.setErrorDescription("OK");
        response.setErrorCode(ErrorCodes.SUCCESS);
        return response;
    }

    private RestResponse response(Object o) {
        RestResponse response = new RestResponse(o);
        response.setErrorDescription("OK");
        response.setErrorCode(ErrorCodes.SUCCESS);
        return response;
    }

    /**
     * <p>测试:计算某一天的度数费用到日报表</p>
     * <b>URL: /energy/caculateEnergyDayStatByDate</b>
     */
    @RestReturn(value = String.class)
    @RequestMapping("caculateEnergyDayStatByDate")
    public RestResponse caculateEnergyDayStatByDate(EnergyStatCommand cmd ) {
    	energyConsumptionService.calculateEnergyDayStatByDate(new Date(cmd.getStatDate()));
    	return success();
    }

    /**
     * <p>测试:汇总某一个月的度数费用到月报表</p>
     * <b>URL: /energy/caculateEnergyMonthStatByDate</b>
     */
    @RestReturn(value = String.class)
    @RequestMapping("caculateEnergyMonthStatByDate")
    public RestResponse caculateEnergyMonthStatByDate(EnergyStatCommand cmd) {
    	energyConsumptionService.calculateEnergyMonthStatByDate(new Date(cmd.getStatDate()));
    	return success();
    }

    /**
     * <p>新建梯度单价方案</p>
     * <b>URL: /energy/createEnergyMeterPriceConfig</b>
     */
    @RestReturn(value = EnergyMeterPriceConfigDTO.class)
    @RequestMapping("createEnergyMeterPriceConfig")
    public RestResponse createEnergyMeterPriceConfig(@Valid CreateEnergyMeterPriceConfigCommand cmd) {
        return response(energyConsumptionService.createEnergyMeterPriceConfig(cmd));
    }

    /**
     * <p>修改梯度单价方案</p>
     * <b>URL: /energy/updateEnergyMeterPriceConfig</b>
     */
    @RestReturn(value = EnergyMeterPriceConfigDTO.class)
    @RequestMapping("updateEnergyMeterPriceConfig")
    public RestResponse updateEnergyMeterPriceConfig(@Valid UpdateEnergyMeterPriceConfigCommand cmd) {
        return response(energyConsumptionService.updateEnergyMeterPriceConfig(cmd));
    }

    /**
     * <p>梯度单价方案列表</p>
     * <b>URL: /energy/listEnergyMeterPriceConfig</b>
     */
    @RestReturn(value =EnergyMeterPriceConfigDTO.class, collection = true)
    @RequestMapping("listEnergyMeterPriceConfig")
    public RestResponse listEnergyMeterPriceConfig(@Valid ListEnergyMeterPriceConfigCommand cmd) {
        return response(energyConsumptionService.listEnergyMeterPriceConfig(cmd));
    }

    /**
     * <p>获取梯度单价方案</p>
     * <b>URL: /energy/getEnergyMeterPriceConfig</b>
     */
    @RestReturn(value = EnergyMeterPriceConfigDTO.class)
    @RequestMapping("getEnergyMeterPriceConfig")
    public RestResponse getEnergyMeterPriceConfig(@Valid GetEnergyMeterPriceConfigCommand cmd) {
        return response(energyConsumptionService.getEnergyMeterPriceConfig(cmd));
    }

    /**
     * <p>删除梯度单价方案</p>
     * <b>URL: /energy/deleteEnergyMeterPriceConfig</b>
     */
    @RestReturn(value = String.class)
    @RequestMapping("deleteEnergyMeterPriceConfig")
    public RestResponse deleteEnergyMeterPriceConfig(@Valid DelelteEnergyMeterPriceConfigCommand cmd) {
        energyConsumptionService.deleteEnergyMeterPriceConfig(cmd);
        return success();
    }

    /**
     * <p>新建表记的默认属性值</p>
     * <b>URL: /energy/createEnergyMeterDefaultSetting</b>
     */
    @RestReturn(String.class)
    @RequestMapping("createEnergyMeterDefaultSetting")
    public RestResponse createEnergyMeterDefaultSetting(CreateEnergyMeterDefaultSettingCommand cmd) {
        energyConsumptionService.createEnergyMeterDefaultSetting(cmd);
        return success();
    }

    /**
     * <p>获取默认设置</p>
     * <b>URL: /energy/listEnergyDefaultSettingTemplates</b>
     */
    @RestReturn(value = EnergyMeterDefaultSettingTemplateDTO.class, collection = true)
    @RequestMapping("listEnergyDefaultSettingTemplates")
    public RestResponse listEnergyDefaultSettingTemplates() {
        return response(energyConsumptionService.listEnergyDefaultSettingTemplates());
    }

    /**
     * <p>通过二维码获取表记的信息</p>
     * <b>URL: /energy/findEnergyMeterByQRCode</b>
     */
    @RestReturn(EnergyMeterDTO.class)
    @RequestMapping("findEnergyMeterByQRCode")
    public RestResponse findEnergyMeterByQRCode(FindEnergyMeterByQRCodeCommand cmd) {
        return response(energyConsumptionService.findEnergyMeterByQRCode(cmd));
    }

    /**
     * <p>获取表记的二维码</p>
     * <b>URL: /energy/getEnergyMeterQRCode</b>
     */
    @RestReturn(String.class)
    @RequestMapping("getEnergyMeterQRCode")
    public RestResponse getEnergyMeterQRCode(GetEnergyMeterQRCodeCommand cmd) {
        return response(energyConsumptionService.getEnergyMeterQRCode(cmd));
    }

    /**
     * <b>URL: /energy/exportEnergyMeterQRCode</b>
     * <p>导出表记二维码</p>
     */
    @RequestMapping("exportEnergyMeterQRCode")
    @RestReturn(value = String.class)
    public RestResponse exportEnergyMeterQRCode(ExportEnergyMeterQRCodeCommand cmd, HttpServletResponse response) {

        energyConsumptionService.exportEnergyMeterQRCode(cmd, response);

        RestResponse resp = new RestResponse();
        resp.setErrorCode(ErrorCodes.SUCCESS);
        resp.setErrorDescription("OK");
        return resp;
    }

    /**
     * <b>URL: /energy/exportSearchEnergyMeterQRCode</b>
     * <p>导出全部表记二维码</p>
     */
    @RequestMapping("exportSearchEnergyMeterQRCode")
    @RestReturn(value = String.class)
    public RestResponse exportSearchEnergyMeterQRCode(SearchEnergyMeterCommand cmd, HttpServletResponse response) {

        energyConsumptionService.exportSearchEnergyMeterQRCode(cmd, response);

        RestResponse resp = new RestResponse();
        resp.setErrorCode(ErrorCodes.SUCCESS);
        resp.setErrorDescription("OK");
        return resp;
    }

    /**
     * <p>批量抄表</p>
     * <b>URL: /energy/batchReadEnergyMeter</b>
     */
    @RestReturn(String.class)
    @RequestMapping("batchReadEnergyMeter")
    public RestResponse batchReadEnergyMeter(BatchReadEnergyMeterCommand cmd) {
        energyConsumptionService.batchReadEnergyMeter(cmd);

        RestResponse resp = new RestResponse();
        resp.setErrorCode(ErrorCodes.SUCCESS);
        resp.setErrorDescription("OK");
        return resp;
    }

    /**
     * <p>新建、修改计划 修改不会影响已生成的任务</p>
     * <b>URL: /energy/updateEnergyPlan</b>
     */
    @RestReturn(EnergyPlanDTO.class)
    @RequestMapping("updateEnergyPlan")
    public RestResponse updateEnergyPlan(UpdateEnergyPlanCommand cmd) {
        return response(energyConsumptionService.updateEnergyPlan(cmd));
    }

    /**
     * <p>删除计划</p>
     * <b>URL: /energy/deleteEnergyPlan</b>
     */
    @RestReturn(String.class)
    @RequestMapping("deleteEnergyPlan")
    public RestResponse deleteEnergyPlan(DeleteEnergyPlanCommand cmd) {
        energyConsumptionService.deleteEnergyPlan(cmd);

        RestResponse resp = new RestResponse();
        resp.setErrorCode(ErrorCodes.SUCCESS);
        resp.setErrorDescription("OK");
        return resp;
    }

    /**
     * <p>列出计划列表 搜索</p>
     * <b>URL: /energy/searchEnergyPlans</b>
     */
    @RestReturn(SearchEnergyPlansResponse.class)
    @RequestMapping("searchEnergyPlans")
    public RestResponse searchEnergyPlans(SearchEnergyPlansCommand cmd) {
        return response(energyPlanSearcher.query(cmd));
    }

    /**
     * <p>列出计划详情</p>
     * <b>URL: /energy/findEnergyPlanDetails</b>
     */
    @RestReturn(EnergyPlanDTO.class)
    @RequestMapping("findEnergyPlanDetails")
    public RestResponse findEnergyPlanDetails(FindEnergyPlanDetailsCommand cmd) {
        return response(energyConsumptionService.findEnergyPlanDetails(cmd));
    }

    /**
     * <p>列出计划关联的表计</p>
     * <b>URL: /energy/listEnergyPlanMeters</b>
     */
    @RestReturn(ListEnergyPlanMetersResponse.class)
    @RequestMapping("listEnergyPlanMeters")
    public RestResponse listEnergyPlanMeters(ListEnergyPlanMetersCommand cmd) {
        return response(energyConsumptionService.listEnergyPlanMeters(cmd));
    }

    /**
     * <p>创建计划的任务</p>
     * <b>URL: /energy/createTask</b>
     */
    @RestReturn(String.class)
    @RequestMapping("createTask")
    public RestResponse createTask(CreateEnergyTaskCommand cmd) {
        energyConsumptionService.createTask(cmd);

        RestResponse resp = new RestResponse();
        resp.setErrorCode(ErrorCodes.SUCCESS);
        resp.setErrorDescription("OK");
        return resp;
    }

    /**
     * <p>计划关联的表计排序 实时</p>
     * <b>URL: /energy/setEnergyPlanMeterOrder</b>
     */
    @RestReturn(ListEnergyPlanMetersResponse.class)
    @RequestMapping("setEnergyPlanMeterOrder")
    public RestResponse setEnergyPlanMeterOrder(SetEnergyPlanMeterOrderCommand cmd) {
        return response(energyConsumptionService.setEnergyPlanMeterOrder(cmd));
    }

    /**
     * <p>查看计划的任务</p>
     * <b>URL: /energy/searchTasksByEnergyPlan</b>
     */
    @RestReturn(SearchTasksByEnergyPlanResponse.class)
    @RequestMapping("searchTasksByEnergyPlan")
    public RestResponse searchTasksByEnergyPlan(SearchTasksByEnergyPlanCommand cmd) {
        return response(energyMeterTaskSearcher.searchTasksByEnergyPlan(cmd));
    }

    /**
     * <p>列出抄表任务 app</p>
     * <b>URL: /energy/listUserEnergyPlanTasks</b>
     */
    @RestReturn(ListUserEnergyPlanTasksResponse.class)
    @RequestMapping("listUserEnergyPlanTasks")
    public RestResponse listUserEnergyPlanTasks(ListUserEnergyPlanTasksCommand cmd) {
        return response(energyConsumptionService.listUserEnergyPlanTasks(cmd));
    }

    //执行抄表任务
    /**
     * <p>执行抄表任务</p>
     * <b>URL: /energy/readTaskMeter</b>
     */
    @RestReturn(String.class)
    @RequestMapping("readTaskMeter")
    public RestResponse readTaskMeter(ReadTaskMeterCommand cmd) {
        energyConsumptionService.readTaskMeter(cmd);

        RestResponse resp = new RestResponse();
        resp.setErrorCode(ErrorCodes.SUCCESS);
        resp.setErrorDescription("OK");
        return resp;
    }

    /**
     * <p>执行抄表任务-- 离线</p>
     * <b>URL: /energy/readTaskMeterOffline</b>
     */
    @RestReturn(ReadTaskMeterOfflineResponse.class)
    @RequestMapping("readTaskMeterOffline")
    public RestResponse readTaskMeterOffline(ReadTaskMeterOfflineCommand cmd) {
        RestResponse resp = new RestResponse(energyConsumptionService.readTaskMeterOffline(cmd));
        resp.setErrorCode(ErrorCodes.SUCCESS);
        resp.setErrorDescription("OK");
        return resp;
    }

    /**
     * <p>同步计划索引</p>
     * <b>URL: /energy/syncEnergyPlanIndex</b>
     */
    @RestReturn(value = String.class)
    @RequestMapping("syncEnergyPlanIndex")
    public RestResponse syncEnergyPlanIndex() {
        energyPlanSearcher.syncFromDb();
        return success();
    }

    /**
     * <p>同步任务索引</p>
     * <b>URL: /energy/syncEnergyTaskIndex</b>
     */
    @RestReturn(value = String.class)
    @RequestMapping("syncEnergyTaskIndex")
    public RestResponse syncEnergyTaskIndex() {
        energyMeterTaskSearcher.syncFromDb();
        return success();
    }

    /**
     * <p>计算任务费用</p>
     * <b>URL: /energy/calculateTaskFeeByTaskId</b>
     */
    @RestReturn(value = String.class)
    @RequestMapping("calculateTaskFeeByTaskId")
    public RestResponse calculateTaskFeeByTaskId(CalculateTaskFeeByTaskIdCommand cmd) {
    	//增加系统管理员验证
    	if(!this.aclProvider.checkAccess("system", null, EhUsers.class.getSimpleName(),
            UserContext.current().getUser().getId(), Privilege.Write, null)) {
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_ACCESS_DENIED, "Access denied");
        }
        energyTaskScheduleJob.calculateTaskFeeByTaskId(cmd.getTaskId());
        return success();
    }
    
    /**
     * <p>手动触发定时任务计算任务费用</p>
     * <b>URL: /energy/calculateTaskFee</b>
     */
    @RestReturn(value = String.class)
    @RequestMapping("calculateTaskFee")
    public RestResponse calculateTaskFee() {
    	//增加系统管理员验证
    	if(!this.aclProvider.checkAccess("system", null, EhUsers.class.getSimpleName(),
            UserContext.current().getUser().getId(), Privilege.Write, null)) {
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_ACCESS_DENIED, "Access denied");
        }
        energyTaskScheduleJob.calculateTaskFee();
        return success();
    }

    /**
     * <p>导出计划的任务</p>
     * <b>URL: /energy/exportTasksByEnergyPlan</b>
     */
    @RequestMapping("exportTasksByEnergyPlan")
    public HttpServletResponse exportTasksByEnergyPlan(SearchTasksByEnergyPlanCommand cmd, HttpServletResponse response) {
        HttpServletResponse resp = energyConsumptionService.exportTasksByEnergyPlan(cmd, response);

        return resp;
    }

    /**
     * <p>导入计划工单的抄表读数(Excel)</p>
     * <b>URL: /energy/importTasksByEnergyPlan</b>
     */
    @RestReturn(ImportFileTaskDTO.class)
    @RequestMapping("importTasksByEnergyPlan")
    public RestResponse importTasksByEnergyPlan(ImportTasksByEnergyPlanCommand cmd, @RequestParam(value = "attachment") MultipartFile[] files) {
        User manaUser = UserContext.current().getUser();
        Long userId = manaUser.getId();
        if (null == files || null == files[0]) {
            LOGGER.error("files is null。userId=" + userId);
            throw RuntimeErrorException.errorWith(UserServiceErrorCode.SCOPE, UserServiceErrorCode.ERROR_INVALID_PARAMS,
                    "files is null");
        }
        return response(energyConsumptionService.importTasksByEnergyPlan(cmd, files[0], userId));
    }

    /**
     * <p>同步离线数据</p>
     * <b>URL: /energy/syncOfflineData</b>
     */
    @RestReturn(SyncOfflineDataResponse.class)
    @RequestMapping("syncOfflineData")
    public RestResponse syncOfflineData(SyncOfflineDataCommand cmd) {
        return response(energyConsumptionService.syncOfflineData(cmd));
    }

    /**
     * <p>测试远程读数</p>
     * <b>URL: /energy/simulatingAutoReadMeters</b>
     */
    @RestReturn(String.class)
    @RequestMapping("simulatingAutoReadMeters")
    public RestResponse simulatingAutoReadMeters() {
        energyConsumptionService.meterAutoReading(true);
        return success();
    }
    /**
     * <p>表记补充到月底时间差工单</p>
     * <b>URL: /energy/addMeterPeriodTaskById</b>
     */
    @RestReturn(String.class)
    @RequestMapping("addMeterPeriodTaskById")
    public RestResponse addMeterPeriodTaskById(UpdateEnergyMeterCommand cmd) {
        energyConsumptionService.addMeterPeriodTaskById(cmd.getMeterId());
        return success();
    }
}
