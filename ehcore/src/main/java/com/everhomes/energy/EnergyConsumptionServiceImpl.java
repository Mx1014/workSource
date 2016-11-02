package com.everhomes.energy;
 
import static com.everhomes.rest.energy.EnergyConsumptionServiceErrorCode.ERR_METER_CATEGORY_NOT_EXIST;
import static com.everhomes.rest.energy.EnergyConsumptionServiceErrorCode.ERR_METER_FORMULA_NOT_EXIST;
import static com.everhomes.rest.energy.EnergyConsumptionServiceErrorCode.ERR_METER_NOT_EXIST;
import static com.everhomes.rest.energy.EnergyConsumptionServiceErrorCode.SCOPE;
import static com.everhomes.util.RuntimeErrorException.errorWith;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.db.DbProvider;
import com.everhomes.locale.LocaleString;
import com.everhomes.locale.LocaleStringProvider;
import com.everhomes.locale.LocaleStringService;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.rest.approval.MeterFormulaVariable;
import com.everhomes.rest.approval.TrueOrFalseFlag;
import com.everhomes.rest.energy.BatchUpdateEnergyMeterSettingsCommand;
import com.everhomes.rest.energy.ChangeEnergyMeterCommand;
import com.everhomes.rest.energy.CreateEnergyMeterCategoryCommand;
import com.everhomes.rest.energy.CreateEnergyMeterCommand;
import com.everhomes.rest.energy.CreateEnergyMeterFormulaCommand;
import com.everhomes.rest.energy.DeleteEnergyMeterCategoryCommand;
import com.everhomes.rest.energy.DeleteEnergyMeterReadingLogCommand;
import com.everhomes.rest.energy.EnergyCommonStatus;
import com.everhomes.rest.energy.EnergyFormulaVariableDTO;
import com.everhomes.rest.energy.EnergyLocaleStringCode;
import com.everhomes.rest.energy.EnergyMeterCategoryDTO;
import com.everhomes.rest.energy.EnergyMeterChangeLogDTO;
import com.everhomes.rest.energy.EnergyMeterDTO;
import com.everhomes.rest.energy.EnergyMeterDefaultSettingDTO;
import com.everhomes.rest.energy.EnergyMeterFormulaDTO;
import com.everhomes.rest.energy.EnergyMeterReadingLogDTO;
import com.everhomes.rest.energy.EnergyMeterSettingLogDTO;
import com.everhomes.rest.energy.EnergyMeterSettingType;
import com.everhomes.rest.energy.EnergyMeterStatus;
import com.everhomes.rest.energy.EnergyMeterType;
import com.everhomes.rest.energy.EnergyStatByYearDTO;
import com.everhomes.rest.energy.EnergyStatCommand;
import com.everhomes.rest.energy.EnergyStatDTO;
import com.everhomes.rest.energy.GetEnergyMeterCommand;
import com.everhomes.rest.energy.ImportEnergyMeterCommand;
import com.everhomes.rest.energy.ListEnergyDefaultSettingsCommand;
import com.everhomes.rest.energy.ListEnergyFormulasCommand;
import com.everhomes.rest.energy.ListMeterCategoriesCommand;
import com.everhomes.rest.energy.ListMeterChangeLogCommand;
import com.everhomes.rest.energy.ReadEnergyMeterCommand;
import com.everhomes.rest.energy.SearchEnergyMeterCommand;
import com.everhomes.rest.energy.SearchEnergyMeterReadingLogsCommand;
import com.everhomes.rest.energy.SearchEnergyMeterReadingLogsResponse;
import com.everhomes.rest.energy.SearchEnergyMeterResponse;
import com.everhomes.rest.energy.UpdateEnergyMeterCategoryCommand;
import com.everhomes.rest.energy.UpdateEnergyMeterCommand;
import com.everhomes.rest.energy.UpdateEnergyMeterDefaultSettingCommand;
import com.everhomes.rest.energy.UpdateEnergyMeterStatusCommand;
import com.everhomes.search.EnergyMeterReadingLogSearcher;
import com.everhomes.search.EnergyMeterSearcher;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserProvider;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.excel.MySheetContentsHandler;
import com.everhomes.util.excel.RowResult;
import com.everhomes.util.excel.SAXHandlerEventUserModel;
 

/**
 * 能耗管理service
 * Created by xq.tian on 2016/10/25.
 */
@Service
public class EnergyConsumptionServiceImpl implements EnergyConsumptionService {

    private final Logger LOGGER = LoggerFactory.getLogger(EnergyConsumptionServiceImpl.class);
    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    
    @Autowired
    private EnergyDateStatisticProvider energyDateStatisticProvider;
    @Autowired
    private DbProvider dbProvider;
 
    @Autowired
    private EnergyMeterChangeLogProvider meterChangeLogProvider;
    
    @Autowired
    private OrganizationProvider organizationProvider;

    @Autowired
    private UserProvider userProvider;

    @Autowired
    private LocaleStringProvider localeStringProvider;

    @Autowired
    private LocaleStringService localeStringService;

    @Autowired
    private EnergyMeterProvider meterProvider;

    @Autowired
    private EnergyMeterSettingLogProvider meterSettingLogProvider;

    @Autowired
    private EnergyMeterFormulaProvider meterFormulaProvider;

    @Autowired
    private EnergyMeterCategoryProvider meterCategoryProvider;

    @Autowired
    private EnergyMeterReadingLogProvider meterReadingLogProvider;

    @Autowired
    private EnergyMeterSearcher meterSearcher;

    @Autowired
    private EnergyMeterReadingLogSearcher readingLogSearcher;

    @Override
    public EnergyMeterDTO createEnergyMeter(CreateEnergyMeterCommand cmd) {
        validate(cmd);
        checkCurrentUserNotInOrg(cmd.getOrganizationId());

        EnergyMeterType meterType = EnergyMeterType.fromCode(cmd.getMeterType());
        if (meterType == null) {
            invalidParameterException("meterType", cmd.getMeterType());
        }
        EnergyMeterCategory category = meterCategoryProvider.findById(currNamespaceId(), cmd.getBillCategoryId());
        if (category == null) {
            LOGGER.error("The energy meter category is not exist, id = {}", cmd.getBillCategoryId());
            throw errorWith(SCOPE, ERR_METER_CATEGORY_NOT_EXIST, "The energy meter category is not exist, id = %s", cmd.getBillCategoryId());
        }
        category = meterCategoryProvider.findById(currNamespaceId(), cmd.getServiceCategoryId());
        if (category == null) {
            LOGGER.error("The energy meter category is not exist, id = {}", cmd.getBillCategoryId());
            throw errorWith(SCOPE, ERR_METER_CATEGORY_NOT_EXIST, "The energy meter category is not exist, id = %s", cmd.getBillCategoryId());
        }
        EnergyMeterFormula formula = meterFormulaProvider.findById(currNamespaceId(), cmd.getAmountFormulaId());
        if (formula == null) {
            LOGGER.error("The energy meter formula is not exist, id = {}", cmd.getAmountFormulaId());
            throw errorWith(SCOPE, ERR_METER_FORMULA_NOT_EXIST, "The energy meter formula is not exist, id = %s", cmd.getAmountFormulaId());
        }
        formula = meterFormulaProvider.findById(currNamespaceId(), cmd.getCostFormulaId());
        if (formula == null) {
            LOGGER.error("The energy meter formula is not exist, id = {}", cmd.getCostFormulaId());
            throw errorWith(SCOPE, ERR_METER_FORMULA_NOT_EXIST, "The energy meter formula is not exist, id = %s", cmd.getCostFormulaId());
        }

        EnergyMeter meter = ConvertHelper.convert(cmd, EnergyMeter.class);
        meter.setStatus(EnergyMeterStatus.ACTIVE.getCode());
        meter.setNamespaceId(currNamespaceId());
        dbProvider.execute(r -> {
            meterProvider.createEnergyMeter(meter);
            // 创建setting log 记录
            UpdateEnergyMeterCommand updateCmd = new UpdateEnergyMeterCommand();
            updateCmd.setPrice(cmd.getPrice());
            updateCmd.setRate(cmd.getRate());
            updateCmd.setCostFormulaId(cmd.getCostFormulaId());
            updateCmd.setAmountFormulaId(cmd.getAmountFormulaId());
            updateCmd.setStartTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
            updateCmd.setMeterId(meter.getId());
            this.insertMeterSettingLog(EnergyMeterSettingType.PRICE, updateCmd);
            this.insertMeterSettingLog(EnergyMeterSettingType.RATE, updateCmd);
            this.insertMeterSettingLog(EnergyMeterSettingType.AMOUNT_FORMULA, updateCmd);
            this.insertMeterSettingLog(EnergyMeterSettingType.COST_FORMULA, updateCmd);
            return true;
        });
        meterSearcher.feedDoc(meter);
        return toEnergyMeterDTO(meter);
    }

    private EnergyMeterDTO toEnergyMeterDTO(EnergyMeter meter) {
        EnergyMeterDTO dto = ConvertHelper.convert(meter, EnergyMeterDTO.class);

        // 表的类型
        String meterType = localeStringService.getLocalizedString(EnergyLocaleStringCode.SCOPE_METER_TYPE, String.valueOf(meter.getMeterType()), currLocale(), "");
        dto.setMeterType(meterType);

        // 账单项目
        EnergyMeterCategory billCategory = meterCategoryProvider.findById(currNamespaceId(), meter.getBillCategoryId());
        dto.setBillCategory(billCategory.getName());

        // 分类
        EnergyMeterCategory serviceCategory = meterCategoryProvider.findById(currNamespaceId(), meter.getServiceCategoryId());
        dto.setServiceCategory(serviceCategory.getName());

        // 当前价格
        EnergyMeterSettingLog priceLog = meterSettingLogProvider.findCurrentSettingByMeterId(currNamespaceId(), meter.getId(), EnergyMeterSettingType.PRICE);
        dto.setPrice(priceLog.getSettingValue());

        // 当前倍率
        EnergyMeterSettingLog rateLog = meterSettingLogProvider.findCurrentSettingByMeterId(currNamespaceId(), meter.getId(), EnergyMeterSettingType.RATE);
        dto.setRate(rateLog.getSettingValue());

        // 当前费用公式名称
        EnergyMeterSettingLog costLog = meterSettingLogProvider.findCurrentSettingByMeterId(currNamespaceId(), meter.getId(), EnergyMeterSettingType.COST_FORMULA);
        EnergyMeterFormula costFormula = meterFormulaProvider.findById(currNamespaceId(), costLog.getFormulaId());
        dto.setCostFormula(toEnergyMeterFormulaDTO(costFormula));

        // 当前用量公式名称
        EnergyMeterSettingLog amountLog = meterSettingLogProvider.findCurrentSettingByMeterId(currNamespaceId(), meter.getId(), EnergyMeterSettingType.AMOUNT_FORMULA);
        EnergyMeterFormula amountFormula = meterFormulaProvider.findById(currNamespaceId(), amountLog.getFormulaId());
        dto.setAmountFormula(toEnergyMeterFormulaDTO(amountFormula));

        // 当前最大量程

        return dto;
    }

    @Override
    public EnergyMeterDTO updateEnergyMeter(UpdateEnergyMeterCommand cmd) {
        validate(cmd);
        checkCurrentUserNotInOrg(cmd.getOrganizationId());
        EnergyMeter meter = this.findMeterById(cmd.getMeterId());
        if (cmd.getName() != null) {
            meter.setName(cmd.getName());
        }
        if (cmd.getMeterNumber() != null) {
            meter.setMeterNumber(cmd.getMeterNumber());
        }
        dbProvider.execute(r -> {
            if (cmd.getPrice() != null) {
                this.insertMeterSettingLog(EnergyMeterSettingType.PRICE, cmd);
            }
            if (cmd.getRate() != null) {
                this.insertMeterSettingLog(EnergyMeterSettingType.RATE, cmd);
            }
            if (cmd.getAmountFormulaId() != null) {
                this.insertMeterSettingLog(EnergyMeterSettingType.AMOUNT_FORMULA, cmd);
            }
            if (cmd.getCostFormulaId() != null) {
                this.insertMeterSettingLog(EnergyMeterSettingType.COST_FORMULA, cmd);
            }
            meterProvider.updateEnergyMeter(meter);
            return true;
        });
        meterSearcher.feedDoc(meter);
        return toEnergyMeterDTO(meter);
    }

    private void insertMeterSettingLog(EnergyMeterSettingType settingType, UpdateEnergyMeterCommand cmd) {
        EnergyMeterSettingLog log = new EnergyMeterSettingLog();
        log.setStatus(EnergyCommonStatus.ACTIVE.getCode());
        log.setStartTime(cmd.getStartTime());
        Timestamp endTime = cmd.getEndTime();
        endTime = endTime == null ? Timestamp.valueOf(LocalDateTime.now().plusYears(100)): endTime;
        log.setEndTime(endTime);
        log.setMeterId(cmd.getMeterId());
        log.setSettingType(settingType.getCode());
        log.setNamespaceId(currNamespaceId());
        switch (settingType) {
            case PRICE:
                log.setSettingValue(cmd.getPrice());
                break;
            case RATE:
                log.setSettingValue(cmd.getRate());
                break;
            case AMOUNT_FORMULA:
                log.setFormulaId(cmd.getAmountFormulaId());
                break;
            case COST_FORMULA:
                log.setFormulaId(cmd.getCostFormulaId());
                break;
        }
        meterSettingLogProvider.createSettingLog(log);
    }

    @Override
    public SearchEnergyMeterResponse searchEnergyMeter(SearchEnergyMeterCommand cmd) {
        validate(cmd);
        checkCurrentUserNotInOrg(cmd.getOrganizationId());
        return meterSearcher.queryMeters(cmd);
    }

    @Override
    public void changeEnergyMeter(ChangeEnergyMeterCommand cmd) {
        validate(cmd);
        checkCurrentUserNotInOrg(cmd.getOrganizationId());


    }

    @Override
    public void updateEnergyMeterStatus(UpdateEnergyMeterStatusCommand cmd) {
        validate(cmd);
        checkCurrentUserNotInOrg(cmd.getOrganizationId());
        EnergyMeter meter = this.findMeterById(cmd.getMeterId());
        meter.setStatus(cmd.getStatus());
        meterProvider.updateEnergyMeter(meter);
    }

    @Override
    public EnergyMeterReadingLogDTO readEnergyMeter(ReadEnergyMeterCommand cmd) {
        validate(cmd);
        checkCurrentUserNotInOrg(cmd.getOrganizationId());
        EnergyMeter meter = this.findMeterById(cmd.getMeterId());
        EnergyMeterReadingLog log = new EnergyMeterReadingLog();
        log.setStatus(EnergyCommonStatus.ACTIVE.getCode());
        log.setReading(cmd.getCurrReading());
        log.setCommunityId(cmd.getCommunityId());
        log.setMeterId(meter.getId());
        log.setNamespaceId(currNamespaceId());
        log.setOperateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        log.setOperatorId(UserContext.current().getUser().getId());
        log.setResetMeterFlag(cmd.getResetMeterFlag());
        dbProvider.execute(r -> {
            meterReadingLogProvider.createEnergyMeterReadingLog(log);
            meter.setLastReading(log.getReading());
            meterProvider.updateEnergyMeter(meter);
            return true;
        });
        return toEnergyMeterReadingLogDTO(log);
    }

    private EnergyMeterReadingLogDTO toEnergyMeterReadingLogDTO(EnergyMeterReadingLog log) {
        EnergyMeterReadingLogDTO dto = ConvertHelper.convert(log, EnergyMeterReadingLogDTO.class);
        User user = userProvider.findUserById(log.getOperatorId());
        dto.setOperatorName(user.getNickName());
        EnergyMeter meter = this.findMeterById(log.getMeterId());
        dto.setMeterName(meter.getName());
        return dto;
    }

    @Override
    public void batchUpdateEnergyMeterSettings(BatchUpdateEnergyMeterSettingsCommand cmd) {
        validate(cmd);
        checkCurrentUserNotInOrg(cmd.getOrganizationId());
        List<EnergyMeter> meters = meterProvider.listByIds(currNamespaceId(), cmd.getMeterIds());
        if (meters != null && meters.size() > 0) {
            meters.forEach(r -> {
                UpdateEnergyMeterCommand updateCmd = new UpdateEnergyMeterCommand();
                updateCmd.setMeterId(r.getId());
                // 价格
                if (cmd.getPrice() != null) {
                    updateCmd.setPrice(cmd.getPrice());
                    updateCmd.setStartTime(new Timestamp(cmd.getPriceStart()));
                    updateCmd.setEndTime(cmd.getPriceEnd() != null ? new Timestamp(cmd.getPriceEnd()) : null);
                    this.insertMeterSettingLog(EnergyMeterSettingType.PRICE, updateCmd);
                }
                // 倍率
                if (cmd.getRate() != null) {
                    updateCmd.setRate(cmd.getRate());
                    updateCmd.setStartTime(new Timestamp(cmd.getRateStart()));
                    updateCmd.setEndTime(cmd.getRateEnd() != null ? new Timestamp(cmd.getRateEnd()) : null);
                    this.insertMeterSettingLog(EnergyMeterSettingType.RATE, updateCmd);
                }
                // 费用
                if (cmd.getCostFormulaId() != null) {
                    updateCmd.setCostFormulaId(cmd.getCostFormulaId());
                    updateCmd.setStartTime(new Timestamp(cmd.getCostFormulaStart()));
                    updateCmd.setEndTime(cmd.getCostFormulaEnd() != null ? new Timestamp(cmd.getCostFormulaEnd()) : null);
                    this.insertMeterSettingLog(EnergyMeterSettingType.COST_FORMULA, updateCmd);
                }
                // 用量
                if (cmd.getAmountFormulaId() != null) {
                    updateCmd.setAmountFormulaId(cmd.getAmountFormulaId());
                    updateCmd.setStartTime(new Timestamp(cmd.getAmountFormulaStart()));
                    updateCmd.setEndTime(cmd.getAmountFormulaEnd() != null ? new Timestamp(cmd.getAmountFormulaEnd()) : null);
                    this.insertMeterSettingLog(EnergyMeterSettingType.AMOUNT_FORMULA, updateCmd);
                }
            });
        }
    }

    @Override
    public SearchEnergyMeterReadingLogsResponse searchEnergyMeterReadingLogs(SearchEnergyMeterReadingLogsCommand cmd) {
        validate(cmd);
        checkCurrentUserNotInOrg(cmd.getOrganizationId());
        return readingLogSearcher.queryMeterReadingLogs(cmd);
    }

    @Override
    public void deleteEnergyMeterReadingLog(DeleteEnergyMeterReadingLogCommand cmd) {

    }

    @Override
    public EnergyMeterSettingLogDTO updateEnergyMeterDefaultSetting(UpdateEnergyMeterDefaultSettingCommand cmd) {
        return null;
    }

    @Override
    public EnergyMeterFormulaDTO createEnergyMeterFormula(CreateEnergyMeterFormulaCommand cmd) {
        return null;
    }

    @Override
    public EnergyMeterCategoryDTO createEnergyMeterCategory(CreateEnergyMeterCategoryCommand cmd) {
        return null;
    }

    @Override
    public EnergyMeterCategoryDTO updateEnergyMeterCategory(UpdateEnergyMeterCategoryCommand cmd) {
        return null;
    }

    @Override
    public void deleteEnergyMeterCategory(DeleteEnergyMeterCategoryCommand cmd) {

    }

    @Override
    public void importEnergyMeter(ImportEnergyMeterCommand cmd, MultipartFile file) {
        validate(cmd);
        checkCurrentUserNotInOrg(cmd.getOrganizationId());

        List<EnergyMeter> meterList = new ArrayList<>();
        ArrayList list = processorExcel(file);
        for (int i = 2; i < list.size(); i++) {
            RowResult result = (RowResult) list.get(i);
            if (Stream.of(result.getA(), result.getB(), result.getC(), result.getD(), result.getE(), result.getF()).anyMatch(StringUtils::isEmpty)) {
                continue;
            }
            EnergyMeter meter = new EnergyMeter();
            meter.setStatus(EnergyMeterStatus.ACTIVE.getCode());
            meter.setCommunityId(cmd.getCommunityId());
            meter.setNamespaceId(currNamespaceId());
            meter.setName(result.getA());
            meter.setMeterNumber(result.getB());
            LocaleString meterTypeLocale = localeStringProvider.findByText(EnergyLocaleStringCode.SCOPE_METER_TYPE, result.getC(), currLocale());
            if (meterTypeLocale != null) {
                meter.setMeterType(Byte.valueOf(meterTypeLocale.getCode()));
            }
            EnergyMeterCategory category = meterCategoryProvider.findByName(currNamespaceId(), result.getD());
            if (category != null) {
                meter.setBillCategoryId(category.getId());
            }
            category = meterCategoryProvider.findByName(currNamespaceId(), result.getE());
            if (category != null) {
                meter.setServiceCategoryId(category.getId());
            }
            if (NumberUtils.isNumber(result.getF())) {
                meter.setMaxReading(new BigDecimal(result.getF()));
            }
            if (NumberUtils.isNumber(result.getG())) {
                meter.setStartReading(new BigDecimal(result.getG()));
            }
            if (NumberUtils.isNumber(result.getH())) {
                meter.setPrice(new BigDecimal(result.getH()));
            }
            if (NumberUtils.isNumber(result.getI())) {
                meter.setRate(new BigDecimal(result.getI()));
            }
            EnergyMeterFormula formula = meterFormulaProvider.findByName(currNamespaceId(), result.getJ());
            if (formula != null) {
                meter.setAmountFormulaId(formula.getId());
            }
            formula = meterFormulaProvider.findByName(currNamespaceId(), result.getK());
            if (formula != null) {
                meter.setCostFormulaId(formula.getId());
            }
            dbProvider.execute(r -> {
                meterProvider.createEnergyMeter(meter);
                // 创建setting log 记录
                UpdateEnergyMeterCommand updateCmd = new UpdateEnergyMeterCommand();
                updateCmd.setPrice(meter.getPrice());
                updateCmd.setRate(meter.getRate());
                updateCmd.setCostFormulaId(meter.getCostFormulaId());
                updateCmd.setAmountFormulaId(meter.getAmountFormulaId());
                updateCmd.setStartTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
                updateCmd.setMeterId(meter.getId());
                this.insertMeterSettingLog(EnergyMeterSettingType.PRICE, updateCmd);
                this.insertMeterSettingLog(EnergyMeterSettingType.RATE, updateCmd);
                this.insertMeterSettingLog(EnergyMeterSettingType.AMOUNT_FORMULA, updateCmd);
                this.insertMeterSettingLog(EnergyMeterSettingType.COST_FORMULA, updateCmd);
                return true;
            });
            meterList.add(meter);
        }
        meterSearcher.bulkUpdate(meterList);
    }

    private String currLocale() {
        return UserContext.current().getUser().getLocale();
    }

    public ArrayList processorExcel(MultipartFile file) {
        MySheetContentsHandler sheetContenthandler = new MySheetContentsHandler();
        try {
            SAXHandlerEventUserModel userModel = new SAXHandlerEventUserModel(sheetContenthandler);
            userModel.processASheets(file.getInputStream(), 0);
        } catch (Exception e) {
            LOGGER.error("Process excel error.", e);
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
                    "Process excel error.");
        }
        return sheetContenthandler.getResultList();
    }

    @Override
    public EnergyStatDTO getEnergyStatByDay(EnergyStatCommand cmd) {
        return null;
    }

    @Override
    public EnergyStatDTO getEnergyStatByMonth(EnergyStatCommand cmd) {
        return null;
    }

    @Override
    public List<EnergyStatByYearDTO> getEnergyStatisticByYear(EnergyStatCommand cmd) {
        return null;
    }

    @Override
    public List<EnergyStatByYearDTO> getEnergyStatisticByYoy(EnergyStatCommand cmd) {
        return null;
    }

    @Override
    public List<EnergyMeterChangeLogDTO> listEnergyMeterChangeLogs(ListMeterChangeLogCommand cmd) {

        return null;
    }

    @Override
    public List<EnergyMeterFormulaDTO> listEnergyMeterFormulas(ListEnergyFormulasCommand cmd) {
        validate(cmd);
        checkCurrentUserNotInOrg(cmd.getOrganizationId());
        List<EnergyMeterFormula> formulas = meterFormulaProvider.listMeterFormulas(currNamespaceId(), cmd.getFormulaType());
        return formulas.stream().map(this::toEnergyMeterFormulaDTO).collect(Collectors.toList());
    }

    private EnergyMeterFormulaDTO toEnergyMeterFormulaDTO(EnergyMeterFormula formula) {
        return ConvertHelper.convert(formula, EnergyMeterFormulaDTO.class);
    }

    @Override
    public EnergyMeterDefaultSettingDTO listEnergyDefaultSettings(ListEnergyDefaultSettingsCommand cmd) {
        return null;
    }

    @Override
    public List<EnergyFormulaVariableDTO> listEnergyFormulaVariables() {
        return null;
    }

    @Override
    public List<EnergyMeterCategoryDTO> listEnergyMeterCategories(ListMeterCategoriesCommand cmd) {
        validate(cmd);
        checkCurrentUserNotInOrg(cmd.getOrganizationId());
        List<EnergyMeterCategory> categoryList = meterCategoryProvider.listMeterCategories(currNamespaceId(), cmd.getCategoryType());
        return categoryList.stream().map(this::toMeterCategoryDto).collect(Collectors.toList());
    }

    @Override
    public EnergyMeterDTO getEnergyMeter(GetEnergyMeterCommand cmd) {
        validate(cmd);
        checkCurrentUserNotInOrg(cmd.getOrganizationId());
        EnergyMeter meter = this.findMeterById(cmd.getMeterId());
        return toEnergyMeterDTO(meter);
    }

    private EnergyMeterCategoryDTO toMeterCategoryDto(EnergyMeterCategory category) {
        return ConvertHelper.convert(category, EnergyMeterCategoryDTO.class);
    }

    private void checkCurrentUserNotInOrg(Long orgId) {
        /*if (orgId == null) {
            LOGGER.error("Invalid parameter organizationId [ null ]");
            throw errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "Invalid parameter organizationId [ null ]");
        }
        Long userId = UserContext.current().getUser().getId();
        OrganizationMember member = this.organizationProvider.findOrganizationMemberByOrgIdAndUId(userId, orgId);
        if (member == null) {
            LOGGER.error("User is not in the organization.");
            throw errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "User is not in the organization.");
        }*/
    }

    private void invalidParameterException(String name, Object param) {
        LOGGER.error("Invalid parameter {} [ {} ].", name, param);
        throw errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                "Invalid parameter %s [ %s ].", name, param);
    }

    // 参数校验方法
    // 可以校验带bean validation 注解的对象
    // 校验失败, 抛出异常, 异常信息附带参数值信息
    private void validate(Object o) {
        Set<ConstraintViolation<Object>> result = validator.validate(o);
        if (!result.isEmpty()) {
            result.stream().map(r -> r.getPropertyPath().toString() + " [ " + r.getInvalidValue() + " ]")
                    .reduce((i, a) -> i + ", " + a).ifPresent(r -> {
                LOGGER.error("Invalid parameter {}", r);
                throw errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER, "Invalid parameter %s", r);
            });
        }
    }

    private Integer currNamespaceId() {
        return UserContext.getCurrentNamespaceId();
    }

    private EnergyMeter findMeterById(Long id) {
        EnergyMeter meter = meterProvider.findById(currNamespaceId(), id);
        if (meter != null) {
            return meter;
        }
        LOGGER.error("EnergyMeter not exist, id = {}", id);
        throw errorWith(SCOPE, ERR_METER_NOT_EXIST, "The meter is not exist id = %s", id);
    }

    public Timestamp getDayBegin( Calendar cal) {
    	 
    	  cal.set(Calendar.HOUR_OF_DAY, 0);
    	  cal.set(Calendar.SECOND, 0);
    	  cal.set(Calendar.MINUTE, 0);
    	  cal.set(Calendar.MILLISECOND, 001);
    	  return new Timestamp(cal.getTimeInMillis());
    	 } 
	/**
	 * 每天早上0点10分刷前一天的读表
	 * */
	@Scheduled(cron = "0 10 0 * * ?")
	@Override
    public void caculateEnergyDayStat(){
		//查出所有的表
		List<EnergyMeter> meters = meterProvider.listEnergyMeters();
		Calendar cal = Calendar.getInstance();
		Timestamp todayBegin = getDayBegin(cal);
  	  	cal.add(Calendar.DAY_OF_MONTH, -1);
  	  	Timestamp yesterdayBegin = getDayBegin(cal);
  	  	cal.add(Calendar.DAY_OF_MONTH, -1);
  	  	Timestamp dayBeforeYestBegin = getDayBegin(cal);
		for(EnergyMeter meter : meters){
			//拿前天的度数
			EnergyMeterReadingLog dayBeforeYestLastLog = meterReadingLogProvider.getLastMeterReadingLogByDate(meter.getId(),yesterdayBegin,dayBeforeYestBegin);
			EnergyMeterReadingLog yesterdayLastLog = meterReadingLogProvider.getLastMeterReadingLogByDate(meter.getId(),todayBegin,yesterdayBegin);
			BigDecimal ReadingAnchor = dayBeforeYestLastLog.getReading();
			//拿出单个表前一天所有的读表记录
			List<EnergyMeterReadingLog> meterReadingLogs = meterReadingLogProvider.listMeterReadingLogByDate(meter.getId(),yesterdayBegin,todayBegin);			
			/**读表用量差*/
			BigDecimal amount = new BigDecimal(0);
			
			Byte resetFlag = TrueOrFalseFlag.FALSE.getCode();
			Byte changeFlag = TrueOrFalseFlag.FALSE.getCode();
			//查看是否有换表,是否有归零
			for(EnergyMeterReadingLog log : meterReadingLogs){
				
				//有归零 量程设置为最大值-锚点,锚点设置为0
				if(TrueOrFalseFlag.TRUE.getCode() == log.getResetMeterFlag().byteValue()){
					resetFlag = TrueOrFalseFlag.TRUE.getCode();
					amount = amount.add(meter.getMaxReading().subtract(ReadingAnchor));
					ReadingAnchor = new BigDecimal(0);
				}
				//有换表 量程加上旧表读数-锚点,锚点重置为新读数
				if(TrueOrFalseFlag.TRUE.getCode() == log.getChangeMeterFlag().byteValue()){
					changeFlag = TrueOrFalseFlag.TRUE.getCode();
					EnergyMeterChangeLog changeLog = this.meterChangeLogProvider.getEnergyMeterChangeLogByLogId(log.getId());
					amount = amount.add(changeLog.getOldReading().subtract(ReadingAnchor));
					ReadingAnchor = changeLog.getNewReading();
				}
			}
			//计算当天走了多少字 量程+昨天最后一次读数-锚点
			amount = amount.add(yesterdayLastLog.getReading().subtract(ReadingAnchor));
			//获取公式,计算当天的费用
			EnergyMeterSettingLog priceSetting = meterSettingLogProvider.findCurrentSettingByMeterId(meter.getNamespaceId(),meter.getId(),EnergyMeterSettingType.PRICE,yesterdayBegin);
			EnergyMeterSettingLog rateSetting = meterSettingLogProvider.findCurrentSettingByMeterId(meter.getNamespaceId(),meter.getId(),EnergyMeterSettingType.RATE ,yesterdayBegin);
			EnergyMeterSettingLog amountSetting = meterSettingLogProvider.findCurrentSettingByMeterId(meter.getNamespaceId(),meter.getId(),EnergyMeterSettingType.AMOUNT_FORMULA ,yesterdayBegin);
			EnergyMeterSettingLog costSetting = meterSettingLogProvider.findCurrentSettingByMeterId(meter.getNamespaceId(),meter.getId(),EnergyMeterSettingType.COST_FORMULA ,yesterdayBegin);
			String aoumtFormula = meterFormulaProvider.findById(amountSetting.getNamespaceId(), amountSetting.getFormulaId()).getExpression();
			String costFormula = meterFormulaProvider.findById(costSetting.getNamespaceId(), costSetting.getFormulaId()).getExpression();
			
			ScriptEngineManager manager = new ScriptEngineManager();
			ScriptEngine engine = manager.getEngineByName("js");
			
			engine.put(MeterFormulaVariable.AMOUNT.getCode(), amount);
			engine.put(MeterFormulaVariable.PRICE.getCode(), priceSetting.getSettingValue());
			engine.put(MeterFormulaVariable.TIMES.getCode(), rateSetting.getSettingValue());
			
			BigDecimal realAmount = new BigDecimal(0);
			BigDecimal realCost = new BigDecimal(0);
			 
			realAmount = BigDecimal.valueOf((double) engine.eval(aoumtFormula));
			realCost = BigDecimal.valueOf((double) engine.eval(costFormula));
			 
			//删除昨天的记录（手工刷的时候）
			energyDateStatisticProvider.deleteEnergyDateStatisticByDate(meter.getId(),new Date(yesterdayBegin.getTime()));
			//写数据库
			EnergyDateStatistic dayStat = ConvertHelper.convert(meter, EnergyDateStatistic.class)  ; 
//			dayStat.set
			dayStat.setStatDate(new Date(yesterdayBegin.getTime()));
			dayStat.setMeterName(meter.getName()); 
			dayStat.setMeterBill(meterCategoryProvider.findById(meter.getNamespaceId(), meter.getBillCategoryId()).getName());
			dayStat.setMeterService(meterCategoryProvider.findById(meter.getNamespaceId(), meter.getServiceCategoryId()).getName());
			dayStat.setMeterRate(rateSetting.getSettingValue());
			dayStat.setMeterPrice(priceSetting.getSettingValue());
			dayStat.setLastReading(dayBeforeYestLastLog.getReading());
			dayStat.setCurrentReading(yesterdayLastLog.getReading());
			dayStat.setCurrentAmount(realAmount);
			dayStat.setCurrentCost(realCost);
			dayStat.setResetMeterFlag(resetFlag);
			dayStat.setChangeMeterFlag(changeFlag);
			dayStat.setStatus(EnergyCommonStatus.ACTIVE.getCode());
			dayStat.setCreatorUid(UserContext.current().getUser().getId()); 
			dayStat.setCreateTime(new Timestamp(DateHelper.currentGMTTime()
					.getTime()));
			energyDateStatisticProvider.createEnergyDateStatistic(dayStat);
		}
	}
	
	/**
	 * 每月1日  早上3点10分刷前一个月的读表
	 * */
	@Scheduled(cron = "0 10 3 1 * ?")
	@Override
    public void caculateEnergyMonthStat(){
		//查出所有的表
		List<EnergyMeter> meters = meterProvider.listEnergyMeters();
		Calendar cal = Calendar.getInstance();
		Timestamp todayBegin = getDayBegin(cal);
  	  	cal.add(Calendar.DAY_OF_MONTH, -1);
  	  	Timestamp yesterdayBegin = getDayBegin(cal);
  	  	cal.add(Calendar.DAY_OF_MONTH, -1);
  	  	Timestamp dayBeforeYestBegin = getDayBegin(cal);
		for(EnergyMeter meter : meters){
			//拿前天的度数
			EnergyMeterReadingLog dayBeforeYestLastLog = meterReadingLogProvider.getLastMeterReadingLogByDate(meter.getId(),yesterdayBegin,dayBeforeYestBegin);
			EnergyMeterReadingLog yesterdayLastLog = meterReadingLogProvider.getLastMeterReadingLogByDate(meter.getId(),todayBegin,yesterdayBegin);
			BigDecimal ReadingAnchor = dayBeforeYestLastLog.getReading();
			//拿出单个表前一天所有的读表记录
			List<EnergyMeterReadingLog> meterReadingLogs = meterReadingLogProvider.listMeterReadingLogByDate(meter.getId(),yesterdayBegin,todayBegin);			
			/**读表用量差*/
			BigDecimal amount = new BigDecimal(0);
			
			Byte resetFlag = TrueOrFalseFlag.FALSE.getCode();
			Byte changeFlag = TrueOrFalseFlag.FALSE.getCode();
			//查看是否有换表,是否有归零
			for(EnergyMeterReadingLog log : meterReadingLogs){
				
				//有归零 量程设置为最大值-锚点,锚点设置为0
				if(TrueOrFalseFlag.TRUE.getCode() == log.getResetMeterFlag().byteValue()){
					resetFlag = TrueOrFalseFlag.TRUE.getCode();
					amount = amount.add(meter.getMaxReading().subtract(ReadingAnchor));
					ReadingAnchor = new BigDecimal(0);
				}
				//有换表 量程加上旧表读数-锚点,锚点重置为新读数
				if(TrueOrFalseFlag.TRUE.getCode() == log.getChangeMeterFlag().byteValue()){
					changeFlag = TrueOrFalseFlag.TRUE.getCode();
					EnergyMeterChangeLog changeLog = this.meterChangeLogProvider.getEnergyMeterChangeLogByLogId(log.getId());
					amount = amount.add(changeLog.getOldReading().subtract(ReadingAnchor));
					ReadingAnchor = changeLog.getNewReading();
				}
			}
			//计算当天走了多少字 量程+昨天最后一次读数-锚点
			amount = amount.add(yesterdayLastLog.getReading().subtract(ReadingAnchor));
			//获取公式,计算当天的费用
			EnergyMeterSettingLog priceSetting = meterSettingLogProvider.findCurrentSettingByMeterId(meter.getNamespaceId(),meter.getId(),EnergyMeterSettingType.PRICE,yesterdayBegin);
			EnergyMeterSettingLog rateSetting = meterSettingLogProvider.findCurrentSettingByMeterId(meter.getNamespaceId(),meter.getId(),EnergyMeterSettingType.RATE ,yesterdayBegin);
			EnergyMeterSettingLog amountSetting = meterSettingLogProvider.findCurrentSettingByMeterId(meter.getNamespaceId(),meter.getId(),EnergyMeterSettingType.AMOUNT_FORMULA ,yesterdayBegin);
			EnergyMeterSettingLog costSetting = meterSettingLogProvider.findCurrentSettingByMeterId(meter.getNamespaceId(),meter.getId(),EnergyMeterSettingType.COST_FORMULA ,yesterdayBegin);
			String aoumtFormula = meterFormulaProvider.findById(amountSetting.getNamespaceId(), amountSetting.getFormulaId()).getExpression();
			String costFormula = meterFormulaProvider.findById(costSetting.getNamespaceId(), costSetting.getFormulaId()).getExpression();
			
			ScriptEngineManager manager = new ScriptEngineManager();
			ScriptEngine engine = manager.getEngineByName("js");
			
			engine.put(MeterFormulaVariable.AMOUNT.getCode(), amount);
			engine.put(MeterFormulaVariable.PRICE.getCode(), priceSetting.getSettingValue());
			engine.put(MeterFormulaVariable.TIMES.getCode(), rateSetting.getSettingValue());
			
			BigDecimal realAmount = new BigDecimal(0);
			BigDecimal realCost = new BigDecimal(0);
			 
			realAmount = BigDecimal.valueOf((double) engine.eval(aoumtFormula));
			realCost = BigDecimal.valueOf((double) engine.eval(costFormula));
			 
			//写数据库
			EnergyDateStatistic dayStat = ConvertHelper.convert(meter, EnergyDateStatistic.class)  ; 
//			dayStat.set
			dayStat.setStatDate(new Date(yesterdayBegin.getTime()));
			dayStat.setMeterName(meter.getName()); 
			dayStat.setMeterBill(meterCategoryProvider.findById(meter.getNamespaceId(), meter.getBillCategoryId()).getName());
			dayStat.setMeterService(meterCategoryProvider.findById(meter.getNamespaceId(), meter.getServiceCategoryId()).getName());
			dayStat.setMeterRate(rateSetting.getSettingValue());
			dayStat.setMeterPrice(priceSetting.getSettingValue());
			dayStat.setLastReading(dayBeforeYestLastLog.getReading());
			dayStat.setCurrentReading(yesterdayLastLog.getReading());
			dayStat.setCurrentAmount(realAmount);
			dayStat.setCurrentCost(realCost);
			dayStat.setResetMeterFlag(resetFlag);
			dayStat.setChangeMeterFlag(changeFlag);
			dayStat.setStatus(EnergyCommonStatus.ACTIVE.getCode());
			dayStat.setCreatorUid(UserContext.current().getUser().getId()); 
			dayStat.setCreateTime(new Timestamp(DateHelper.currentGMTTime()
					.getTime()));
			energyDateStatisticProvider.createEnergyDateStatistic(dayStat);
		}
	}
}
