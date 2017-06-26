package com.everhomes.energy;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.energy.*;
import com.everhomes.rest.equipment.ExportEquipmentsCardCommand;
import com.everhomes.rest.pmtask.ListAuthorizationCommunityByUserResponse;
import com.everhomes.rest.pmtask.ListAuthorizationCommunityCommand;
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

    @Autowired
    private EnergyConsumptionService energyConsumptionService;

    /**
     * <b>URL: /energy/listAuthorizationCommunityByUser</b>
     * <p>授权人员 管理小区列表</p>
     */
    @RequestMapping("listAuthorizationCommunityByUser")
    @RestReturn(value=ListAuthorizationCommunityByUserResponse.class)
    public RestResponse listAuthorizationCommunityByUser(ListAuthorizationCommunityCommand cmd) {
        ListAuthorizationCommunityByUserResponse resp = energyConsumptionService.listAuthorizationCommunityByUser(cmd);
        RestResponse response = new RestResponse(resp);
        response.setErrorCode(ErrorCodes.SUCCESS);
        response.setErrorDescription("OK");
        return response;
    }

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

    /**
     * <p>新建计算公式</p>
     * <b>URL: /energy/createEnergyMeterFormula</b>
     */
    @RestReturn(EnergyMeterFormulaDTO.class)
    @RequestMapping("createEnergyMeterFormula")
    public RestResponse createEnergyMeterFormula(CreateEnergyMeterFormulaCommand cmd) {
        return response(energyConsumptionService.createEnergyMeterFormula(cmd));
    }

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
    @RestReturn(String.class)
    @RequestMapping("importEnergyMeter")
    public RestResponse importEnergyMeter(ImportEnergyMeterCommand cmd, @RequestParam("attachment") MultipartFile file) {
        energyConsumptionService.importEnergyMeter(cmd, file);
        return success();
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



}
