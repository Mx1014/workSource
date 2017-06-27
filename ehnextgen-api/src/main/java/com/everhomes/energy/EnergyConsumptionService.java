package com.everhomes.energy;

import com.everhomes.rest.energy.*;
import com.everhomes.rest.pmtask.ListAuthorizationCommunityByUserResponse;
import com.everhomes.rest.pmtask.ListAuthorizationCommunityCommand;

import com.everhomes.rest.pmtask.ListAuthorizationCommunityByUserResponse;
import com.everhomes.rest.pmtask.ListAuthorizationCommunityCommand;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

/**
 * 能耗管理的service
 * Created by xq.tian on 2016/10/25.
 */
public interface EnergyConsumptionService {

    /**
     * 创建能耗表记(水表, 电表)
     * @param cmd   cmd
     * @return  返回创建好的表记DTO
     */
    EnergyMeterDTO createEnergyMeter(CreateEnergyMeterCommand cmd);

    /**
     * 换表
     * @param cmd   cmd
     */
    void changeEnergyMeter(ChangeEnergyMeterCommand cmd);

    /**
     * meter转dto
     */
    EnergyMeterDTO toEnergyMeterDTO(EnergyMeter meter);

    /**
     * 修改表记
     * @param cmd   cmd
     * @return  返回表记DTO
     */
    EnergyMeterDTO updateEnergyMeter(UpdateEnergyMeterCommand cmd);

    /**
     * 搜索表记
     * @param cmd   cmd
     * @return  SearchEnergyMeterResponse
     */
    SearchEnergyMeterResponse searchEnergyMeter(SearchEnergyMeterCommand cmd);

    /**
     * 更新表记的状态
     * @param cmd   cmd
     */
    void updateEnergyMeterStatus(UpdateEnergyMeterStatusCommand cmd);

    /**
     * 读表
     * @param cmd   cmd
     * @return  返回读表记录DTO
     */
    EnergyMeterReadingLogDTO readEnergyMeter(ReadEnergyMeterCommand cmd);

    /**
     * 批量修改表记的属性(单价, 倍率, 用量计算公式, 费用计算公式)
     * @param cmd   cmd
     */
    void batchUpdateEnergyMeterSettings(BatchUpdateEnergyMeterSettingsCommand cmd);

    /**
     * 搜索读表记录
     * @param cmd   cmd
     * @return SearchEnergyMeterReadingLogsResponse
     */
    SearchEnergyMeterReadingLogsResponse searchEnergyMeterReadingLogs(SearchEnergyMeterReadingLogsCommand cmd);

    /**
     * 删除读表记录(只能删除当天的记录)
     * @param cmd   cmd
     */
    void deleteEnergyMeterReadingLog(DeleteEnergyMeterReadingLogCommand cmd);

    /**
     * 修改默认的表记属性
     * (默认单价, 默认倍率, 默认计算公式等)
     * @param cmd   cmd
     * @return  EnergyMeterSettingLogDTO
     */
    EnergyMeterDefaultSettingDTO updateEnergyMeterDefaultSetting(UpdateEnergyMeterDefaultSettingCommand cmd);

    /**
     * 创建计算公式
     * @param cmd   cmd
     * @return  EnergyMeterFormulaDTO
     */
    EnergyMeterFormulaDTO createEnergyMeterFormula(CreateEnergyMeterFormulaCommand cmd);

    /**
     * 新建表记的分类(项目, 性质)
     * @param cmd   cmd
     * @return  EnergyMeterCategoryDTO
     */
    EnergyMeterCategoryDTO createEnergyMeterCategory(CreateEnergyMeterCategoryCommand cmd);

    /**
     * 修改表记分类
     * @param cmd   cmd
     * @return  EnergyMeterCategoryDTO
     */
    EnergyMeterCategoryDTO updateEnergyMeterCategory(UpdateEnergyMeterCategoryCommand cmd);

    /**
     * 删除表记的分类(项目, 性质)
     * @param cmd   cmd
     */
    void deleteEnergyMeterCategory(DeleteEnergyMeterCategoryCommand cmd);

    /**
     * 导入表记
     * @param cmd   cmd
     * @param file  Excel文件
     */
    void importEnergyMeter(ImportEnergyMeterCommand cmd, MultipartFile file);

    /**
     * 水电能耗每日报表
     * @param cmd   cmd
     * @return  EnergyStatDTO
     */
    EnergyStatDTO getEnergyStatByDay(EnergyStatCommand cmd);

    /**
     * 水电能耗每月报表
     * @param cmd   cmd
     * @return  EnergyStatDTO
     * @throws ParseException 
     */
    EnergyStatDTO getEnergyStatByMonth(EnergyStatCommand cmd);

    /**
     * 年度水电用量收支对比表
     * @param cmd   cmd
     */
    List<EnergyStatByYearDTO> getEnergyStatisticByYear(EnergyStatCommand cmd);

    /**
     * 各项目月水电能耗情况（与去年同期相比)
     * @param cmd   cmd
     */
    List<EnergyCommunityYoyStatDTO> getEnergyStatisticByYoy(EnergyStatCommand cmd);

    /**
     * 换表记录列表
     */
    List<EnergyMeterChangeLogDTO> listEnergyMeterChangeLogs(ListEnergyMeterChangeLogsCommand cmd);

    /**
     * 获取能耗的计算公式列表
     * @param cmd   公式类型参数, {@link com.everhomes.rest.energy.EnergyFormulaType}
     */
    List<EnergyMeterFormulaDTO> listEnergyMeterFormulas(ListEnergyMeterFormulasCommand cmd);

    /**
     * 获取默认设置属性
     */
    List<EnergyMeterDefaultSettingDTO> listEnergyDefaultSettings(ListEnergyDefaultSettingsCommand cmd);

    /**
     * 公式变量列表
     */
    List<EnergyFormulaVariableDTO> listEnergyFormulaVariables();

    /**
     * 获取表记分类列表
     */
    List<EnergyMeterCategoryDTO> listEnergyMeterCategories(ListEnergyMeterCategoriesCommand cmd);
 
     

    /**
     * 根据id获取表记信息
     */
    EnergyMeterDTO getEnergyMeter(GetEnergyMeterCommand cmd);


    /**
     * 删除公式
     */
    void deleteEnergyMeterFormula(DeleteEnergyMeterFormulaCommand cmd);

    /**
     * 获取setting记录列表
     */
    List<EnergyMeterSettingLogDTO> listEnergyMeterSettingLogs(ListEnergyMeterSettingLogsCommand cmd);

    /**
     * 计算某一天的读表数据
     * */
	void calculateEnergyDayStatByDate(Date date);

    /**
     * 计算某一月的读表数据
     * */  
	void calculateEnergyMonthStatByDate(Date date);

    void syncEnergyMeterReadingLogIndex();

    void syncEnergyMeterIndex();

    /**
     * 创建梯度价格方案
     * */
    EnergyMeterPriceConfigDTO createEnergyMeterPriceConfig(CreateEnergyMeterPriceConfigCommand cmd);

    /**
     * 修改梯度价格方案
     * */
    EnergyMeterPriceConfigDTO updateEnergyMeterPriceConfig(UpdateEnergyMeterPriceConfigCommand cmd);

    /**
     * 获得梯度价格方案
     * */
    EnergyMeterPriceConfigDTO getEnergyMeterPriceConfig(GetEnergyMeterPriceConfigCommand cmd);

    /**
     * 梯度价格方案列表
     * */
    List<EnergyMeterPriceConfigDTO> listEnergyMeterPriceConfig(ListEnergyMeterPriceConfigCommand cmd);

    /**
     * 删除梯度价格方案
     * */
    void deleteEnergyMeterPriceConfig(DelelteEnergyMeterPriceConfigCommand cmd);

    void createEnergyMeterDefaultSetting(CreateEnergyMeterDefaultSettingCommand cmd);

    List<EnergyMeterDefaultSettingTemplateDTO> listEnergyDefaultSettingTemplates();

    ListAuthorizationCommunityByUserResponse listAuthorizationCommunityByUser(ListAuthorizationCommunityCommand cmd);

    EnergyMeterDTO findEnergyMeterByQRCode(FindEnergyMeterByQRCodeCommand cmd);
    void batchReadEnergyMeter(BatchReadEnergyMeterCommand cmd);

    String getEnergyMeterQRCode(GetEnergyMeterQRCodeCommand cmd);
    void exportEnergyMeterQRCode(ExportEnergyMeterQRCodeCommand cmd, HttpServletResponse response);
    void exportSearchEnergyMeterQRCode(SearchEnergyMeterCommand cmd, HttpServletResponse response);
}
