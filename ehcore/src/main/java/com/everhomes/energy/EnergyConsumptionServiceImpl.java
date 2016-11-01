package com.everhomes.energy;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.db.DbProvider;
import com.everhomes.locale.LocaleString;
import com.everhomes.locale.LocaleStringProvider;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.rest.energy.*;
import com.everhomes.search.EnergyMeterSearcher;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.excel.MySheetContentsHandler;
import com.everhomes.util.excel.RowResult;
import com.everhomes.util.excel.SAXHandlerEventUserModel;
import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static com.everhomes.rest.energy.EnergyConsumptionServiceErrorCode.*;
import static com.everhomes.util.RuntimeErrorException.errorWith;

/**
 * 能耗管理service
 * Created by xq.tian on 2016/10/25.
 */
@Service
public class EnergyConsumptionServiceImpl implements EnergyConsumptionService {

    private final Logger LOGGER = LoggerFactory.getLogger(EnergyConsumptionServiceImpl.class);
    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Autowired
    private DbProvider dbProvider;

    @Autowired
    private OrganizationProvider organizationProvider;

    @Autowired
    private LocaleStringProvider localeStringProvider;

    @Autowired
    private EnergyMeterProvider meterProvider;

    @Autowired
    private EnergyMeterSettingLogProvider meterSettingLogProvider;

    @Autowired
    private EnergyMeterFormulaProvider meterFormulaProvider;

    @Autowired
    private EnergyMeterCategoryProvider meterCategoryProvider;

    @Autowired
    private EnergyMeterSearcher meterSearcher;

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
        return ConvertHelper.convert(meter, EnergyMeterDTO.class);
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
        log.setEndTime(cmd.getEndTime());
        log.setMeterId(cmd.getMeterId());
        log.setSettingType(settingType.getCode());
        log.setNamespaceId(currNamespaceId());
        switch (settingType) {
            case PRICE:
            case RATE:
                log.setSettingValue(cmd.getRate());
                break;
            case AMOUNT_FORMULA:
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

    }

    @Override
    public void updateEnergyMeterStatus(UpdateEnergyMeterStatusCommand cmd) {

    }

    @Override
    public EnergyMeterReadingLogDTO readEnergyMeter(ReadEnergyMeterCommand cmd) {
        return null;
    }

    @Override
    public void batchUpdateEnergyMeterSettings(BatchUpdateEnergyMeterSettingsCommand cmd) {

    }

    @Override
    public SearchEnergyMeterReadingLogsResponse searchEnergyMeterReadingLogs(SearchEnergyMeterReadingLogsCommand cmd) {
        return null;
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
            LocaleString meterTypeLocale = localeStringProvider.findByText(EnergyLocaleStirngCode.SCOPE_METER_TYPE, result.getC(), currLocale());
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
        return null;
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
        return null;
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
}
