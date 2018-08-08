
package com.everhomes.energy;

import com.everhomes.rest.energy.BatchReadEnergyMeterCommand;
import com.everhomes.rest.energy.BatchUpdateEnergyMeterSettingsCommand;
import com.everhomes.rest.energy.ChangeEnergyMeterCommand;
import com.everhomes.rest.energy.CreateEnergyMeterCategoryCommand;
import com.everhomes.rest.energy.CreateEnergyMeterCommand;
import com.everhomes.rest.energy.CreateEnergyMeterDefaultSettingCommand;
import com.everhomes.rest.energy.CreateEnergyMeterFormulaCommand;
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
import com.everhomes.rest.energy.SearchTasksByEnergyPlanCommand;
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
import com.everhomes.rest.pmtask.ListAuthorizationCommunityByUserResponse;
import com.everhomes.rest.pmtask.ListAuthorizationCommunityCommand;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Set;

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
    EnergyMeterDTO toEnergyMeterDTO(EnergyMeter meter , Integer namespaceId);

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
    SearchEnergyMeterResponse searchSimpleEnergyMeter(SearchEnergyMeterCommand cmd);

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
     * @param mfile  Excel文件
     */
    ImportFileTaskDTO importEnergyMeter(ImportEnergyMeterCommand cmd, MultipartFile mfile, Long userId);

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

    EnergyPlanDTO updateEnergyPlan(UpdateEnergyPlanCommand cmd);
    EnergyPlanDTO findEnergyPlanDetails(FindEnergyPlanDetailsCommand cmd);
    void deleteEnergyPlan(DeleteEnergyPlanCommand cmd);
    ListEnergyPlanMetersResponse listEnergyPlanMeters(ListEnergyPlanMetersCommand cmd);
    ListEnergyPlanMetersResponse setEnergyPlanMeterOrder(SetEnergyPlanMeterOrderCommand cmd);
    ListUserEnergyPlanTasksResponse listUserEnergyPlanTasks(ListUserEnergyPlanTasksCommand cmd);
    void readTaskMeter(ReadTaskMeterCommand cmd);
    ReadTaskMeterOfflineResponse readTaskMeterOffline(ReadTaskMeterOfflineCommand cmd);

    void createTask(CreateEnergyTaskCommand cmd);
    void creatMeterTask(EnergyPlanMeterMap map, EnergyPlan plan);
    Set<Long> getTaskGroupUsers(Long taskId);

    BigDecimal processMonthPrompt(EnergyMeter meter,Integer namespaceId);
    BigDecimal processDayPrompt(EnergyMeter meter,Integer namespaceId);

    HttpServletResponse exportTasksByEnergyPlan(SearchTasksByEnergyPlanCommand cmd, HttpServletResponse response);
    ImportFileTaskDTO importTasksByEnergyPlan(ImportTasksByEnergyPlanCommand cmd, MultipartFile mfile, Long userId);

    SyncOfflineDataResponse syncOfflineData(SyncOfflineDataCommand cmd);

    void meterAutoReading(Boolean createPlanFlag);

    void addMeterPeriodTaskById(Long meterId);
}
