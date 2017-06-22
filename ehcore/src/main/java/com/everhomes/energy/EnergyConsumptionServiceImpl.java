package com.everhomes.energy;

import com.everhomes.acl.RolePrivilegeService;
import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.community.Community;
import com.everhomes.community.CommunityProvider;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.coordinator.CoordinationLocks;
import com.everhomes.coordinator.CoordinationProvider;
import com.everhomes.db.DbProvider;
import com.everhomes.entity.EntityType;
import com.everhomes.locale.LocaleString;
import com.everhomes.locale.LocaleStringProvider;
import com.everhomes.locale.LocaleStringService;
import com.everhomes.mail.MailHandler;
import com.everhomes.module.ServiceModuleService;
import com.everhomes.organization.OrganizationMember;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.rest.acl.ListUserRelatedProjectByModuleIdCommand;
import com.everhomes.rest.acl.PrivilegeConstants;
import com.everhomes.rest.address.CommunityDTO;
import com.everhomes.rest.approval.MeterFormulaVariable;
import com.everhomes.rest.approval.TrueOrFalseFlag;
import com.everhomes.rest.energy.*;
import com.everhomes.rest.module.ListUserRelatedProjectByModuleCommand;
import com.everhomes.rest.pmtask.ListAuthorizationCommunityByUserResponse;
import com.everhomes.rest.pmtask.ListAuthorizationCommunityCommand;
import com.everhomes.rest.pmtask.PmTaskCheckPrivilegeFlag;
import com.everhomes.rest.pmtask.PmTaskErrorCode;
import com.everhomes.search.EnergyMeterReadingLogSearcher;
import com.everhomes.search.EnergyMeterSearcher;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.user.UserProvider;
import com.everhomes.user.admin.SystemUserPrivilegeMgr;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.Tuple;
import com.everhomes.util.excel.MySheetContentsHandler;
import com.everhomes.util.excel.RowResult;
import com.everhomes.util.excel.SAXHandlerEventUserModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sun.org.apache.xpath.internal.operations.Bool;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.poi.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
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

    private static ThreadLocal<SimpleDateFormat> yearSF = new ThreadLocal<SimpleDateFormat>(){
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy");
        }
    };
    private static ThreadLocal<SimpleDateFormat> monthSF = new ThreadLocal<SimpleDateFormat>(){
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyyMM");
        }
    };

    @Autowired
    private CommunityProvider communityProvider;

    @Autowired
    private EnergyYoyStatisticProvider energyYoyStatisticProvider;

    @Autowired
    private EnergyDateStatisticProvider energyDateStatisticProvider;

    @Autowired
    private EnergyCountStatisticProvider energyCountStatisticProvider;

    @Autowired
    private EnergyMonthStatisticProvider energyMonthStatisticProvider;

    @Autowired
    private DbProvider dbProvider;

    @Autowired
    private EnergyMeterChangeLogProvider meterChangeLogProvider;

    @Autowired
    private OrganizationProvider organizationProvider;

    @Autowired
    private ConfigurationProvider configurationProvider;

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

    @Autowired
    private EnergyMeterDefaultSettingProvider defaultSettingProvider;

    @Autowired
    private EnergyMeterFormulaVariableProvider meterFormulaVariableProvider;

    @Autowired
    private CoordinationProvider coordinationProvider;

    @Autowired
    private EnergyMeterPriceConfigProvider priceConfigProvider;

    @Autowired
    private RolePrivilegeService rolePrivilegeService;

    @Autowired
    private ServiceModuleService serviceModuleService;

//    @Override
//    public ListAuthorizationCommunityByUserResponse listAuthorizationCommunityByUser(ListAuthorizationCommunityCommand cmd) {
//
//        if (null != cmd.getCheckPrivilegeFlag() && cmd.getCheckPrivilegeFlag() == PmTaskCheckPrivilegeFlag.CHECKED.getCode()) {
//            if(null == cmd.getOrganizationId()) {
//                LOGGER.error("Not privilege", cmd);
//                throw RuntimeErrorException.errorWith(PmTaskErrorCode.SCOPE, PmTaskErrorCode.ERROR_CREATE_TASK_PRIVILEGE,
//                        "Not privilege");
//            }
//        }
//
//        ListAuthorizationCommunityByUserResponse response = new ListAuthorizationCommunityByUserResponse();
//
//        ListUserRelatedProjectByModuleIdCommand listUserRelatedProjectByModuleIdCommand = new ListUserRelatedProjectByModuleIdCommand();
//        listUserRelatedProjectByModuleIdCommand.setOrganizationId(cmd.getOrganizationId());
//        listUserRelatedProjectByModuleIdCommand.setModuleId(49100L);
//
//        List<CommunityDTO> dtos = rolePrivilegeService.listUserRelatedProjectByModuleId(listUserRelatedProjectByModuleIdCommand);
//
//        if (null != cmd.getCheckPrivilegeFlag() && cmd.getCheckPrivilegeFlag() == PmTaskCheckPrivilegeFlag.CHECKED.getCode()) {
//            SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
//            User user = UserContext.current().getUser();
//            List<CommunityDTO> result = new ArrayList<>();
//            dtos.forEach(r -> {
//                if (resolver.checkUserPrivilege(user.getId(), EntityType.COMMUNITY.getCode(), r.getId(), cmd.getOrganizationId(), PrivilegeConstants.REPLACE_CREATE_TASK)) {
//                    result.add(r);
//                }
//            });
//            response.setCommunities(result);
//
//        }else{
//            response.setCommunities(dtos);
//        }
//        return response;
//    }

    @Override
    public EnergyMeterDTO createEnergyMeter(CreateEnergyMeterCommand cmd) {
        validate(cmd);
        checkCurrentUserNotInOrg(cmd.getOwnerId());

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
        if (cmd.getStartReading().doubleValue() > cmd.getMaxReading().doubleValue()) {
            LOGGER.error("The energy meter start reading is greater then max reading");
            throw errorWith(SCOPE, ERR_METER_START_GREATER_THEN_MAX, "The energy meter start reading is greater then max reading");
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
            updateCmd.setStartTime(Date.valueOf(LocalDate.now()).getTime());
            updateCmd.setMeterId(meter.getId());
            updateCmd.setCalculationType(cmd.getCalculationType());
            updateCmd.setConfigId(cmd.getConfigId());
            this.insertMeterSettingLog(EnergyMeterSettingType.PRICE, updateCmd);
            this.insertMeterSettingLog(EnergyMeterSettingType.RATE, updateCmd);
            this.insertMeterSettingLog(EnergyMeterSettingType.AMOUNT_FORMULA, updateCmd);
            this.insertMeterSettingLog(EnergyMeterSettingType.COST_FORMULA, updateCmd);

            // 创建一条初始读表记录
            ReadEnergyMeterCommand readEnergyMeterCmd = new ReadEnergyMeterCommand();
            readEnergyMeterCmd.setCommunityId(cmd.getCommunityId());
            readEnergyMeterCmd.setCurrReading(meter.getStartReading());
            readEnergyMeterCmd.setMeterId(meter.getId());
            readEnergyMeterCmd.setOrganizationId(cmd.getOwnerId());
            readEnergyMeterCmd.setResetMeterFlag(TrueOrFalseFlag.FALSE.getCode());
            this.readEnergyMeter(readEnergyMeterCmd);
            return true;
        });
        meterSearcher.feedDoc(meter);
        return toEnergyMeterDTO(meter);
    }

    @Override
    public EnergyMeterDTO toEnergyMeterDTO(EnergyMeter meter) {
        EnergyMeterDTO dto = ConvertHelper.convert(meter, EnergyMeterDTO.class);

        // 表的类型
        String meterType = localeStringService.getLocalizedString(EnergyLocalStringCode.SCOPE_METER_TYPE, String.valueOf(meter.getMeterType()), currLocale(), "");
        dto.setMeterType(meterType);

        // 表的状态
        String meterStatus = localeStringService.getLocalizedString(EnergyLocalStringCode.SCOPE_METER_STATUS, String.valueOf(meter.getStatus()), currLocale(), "");
        dto.setStatus(meterStatus);

        // 账单项目
        EnergyMeterCategory billCategory = meterCategoryProvider.findById(currNamespaceId(), meter.getBillCategoryId());
        dto.setBillCategory(billCategory != null ? billCategory.getName() : null);

        // 分类
        EnergyMeterCategory serviceCategory = meterCategoryProvider.findById(currNamespaceId(), meter.getServiceCategoryId());
        dto.setServiceCategory(serviceCategory != null ? serviceCategory.getName() : null);

        // 当前价格
        EnergyMeterSettingLog priceLog = meterSettingLogProvider.findCurrentSettingByMeterId(currNamespaceId(), meter.getId(), EnergyMeterSettingType.PRICE);
        dto.setPrice(priceLog != null ? priceLog.getSettingValue() : null);

        if(PriceCalculationType.BLOCK_TARIFF.equals(PriceCalculationType.fromCode(priceLog.getCalculationType()))) {
            EnergyMeterPriceConfig priceConfig = priceConfigProvider.findById(priceLog.getConfigId(), meter.getOwnerId(),
                    meter.getOwnerType(), meter.getCommunityId(), currNamespaceId());
            if(priceConfig != null) {
                dto.setPriceConfig(toEnergyMeterPriceConfigDTO(priceConfig));
            }
        }
        // 当前倍率
        EnergyMeterSettingLog rateLog = meterSettingLogProvider.findCurrentSettingByMeterId(currNamespaceId(), meter.getId(), EnergyMeterSettingType.RATE);
        dto.setRate(rateLog != null ? rateLog.getSettingValue() : null);

        // 当前费用公式名称
        EnergyMeterSettingLog costLog = meterSettingLogProvider.findCurrentSettingByMeterId(currNamespaceId(), meter.getId(), EnergyMeterSettingType.COST_FORMULA);
        if (costLog != null) {
            EnergyMeterFormula costFormula = meterFormulaProvider.findById(currNamespaceId(), costLog.getFormulaId());
            dto.setCostFormula(toEnergyMeterFormulaDTO(costFormula));
        }

        // 当前用量公式名称
        EnergyMeterSettingLog amountLog = meterSettingLogProvider.findCurrentSettingByMeterId(currNamespaceId(), meter.getId(), EnergyMeterSettingType.AMOUNT_FORMULA);
        if (amountLog != null) {
            EnergyMeterFormula amountFormula = meterFormulaProvider.findById(currNamespaceId(), amountLog.getFormulaId());
            dto.setAmountFormula(toEnergyMeterFormulaDTO(amountFormula));
        }

        // 抄表提示, 判断今天是否读表
        Timestamp lastReadTime = meter.getLastReadTime();
        if (lastReadTime != null && (lastReadTime.getTime() - Date.valueOf(LocalDate.now()).getTime() >= 0)) {
            dto.setTodayReadStatus(TrueOrFalseFlag.TRUE.getCode());
            // 日读表差
            dto.setDayPrompt(this.processDayPrompt(meter));
            // 月读表差
            dto.setMonthPrompt(this.processMonthPrompt(meter));
        }

        return dto;
    }

    private BigDecimal processDayPrompt(EnergyMeter meter) {
        Timestamp lastReadTime = meter.getLastReadTime();
        EnergyMeterDefaultSetting dayPromptSetting = defaultSettingProvider.findBySettingType(currNamespaceId(), EnergyMeterSettingType.DAY_PROMPT);
        if (lastReadTime != null && dayPromptSetting != null && Objects.equals(dayPromptSetting.getStatus(), EnergyCommonStatus.ACTIVE.getCode())) {
            LocalDateTime lastReadDateTime = lastReadTime.toLocalDateTime();
            // lastReadingTime 前一天的开始
            Date lastReadingTimeLastDayBegin = Date.valueOf(LocalDate.of(lastReadDateTime.getYear(), lastReadDateTime.getMonthValue(), lastReadDateTime.getDayOfMonth()).minusDays(1));
            // lastReadingTime 前一天的结束, 下一天的开始
            Date lastReadingTimeLastDayEnd = Date.valueOf(lastReadDateTime.toLocalDate());

            EnergyDateStatistic statistic = energyDateStatisticProvider.findByMeterAndDate(currNamespaceId(), meter.getId(), lastReadingTimeLastDayBegin);
            if (statistic != null) {
                // 上次读表前一天的最后一条读表记录
                EnergyMeterReadingLog lastLastReadingLog = meterReadingLogProvider.getLastMeterReadingLogByDate(meter.getId(), null, new Timestamp(lastReadingTimeLastDayEnd.getTime()));
                // 上次读表记录
                EnergyMeterReadingLog lastReadingLog = meterReadingLogProvider.getLastMeterReadingLogByDate(meter.getId(), null, meter.getLastReadTime());

                // 读表用量差
                BigDecimal amount = new BigDecimal(0);

                // 默认初始值为表的初始值，如果有昨天之前和前天之前的读表记录则置换初始值
                BigDecimal readingAnchor = meter.getStartReading();
                BigDecimal dayCurrReading = meter.getStartReading();
                if(null != lastLastReadingLog){
                    readingAnchor = lastLastReadingLog.getReading();
                }
                if(null != lastReadingLog){
                    dayCurrReading = lastReadingLog.getReading();
                }

                // 拿出单个表当天所有的读表记录
                List<EnergyMeterReadingLog> meterReadingLogs = meterReadingLogProvider.listMeterReadingLogByDate(meter.getId(), new Timestamp(lastReadingTimeLastDayEnd.getTime()), meter.getLastReadTime());

                // 查看是否有换表,是否有归零
                if(meterReadingLogs != null){
                    for(EnergyMeterReadingLog log : meterReadingLogs){

                        //有归零 量程设置为最大值-锚点,锚点设置为0
                        if(log.getResetMeterFlag()!=null && TrueOrFalseFlag.TRUE.getCode() == log.getResetMeterFlag()){
                            amount = amount.add(meter.getMaxReading().subtract(readingAnchor));
                            readingAnchor = new BigDecimal(0);
                        }
                        //有换表 量程加上旧表读数-锚点,锚点重置为新读数
                        if(log.getChangeMeterFlag() != null && TrueOrFalseFlag.TRUE.getCode() == log.getChangeMeterFlag()){
                            EnergyMeterChangeLog changeLog = this.meterChangeLogProvider.getEnergyMeterChangeLogByLogId(log.getId());
                            amount = amount.add(changeLog.getOldReading().subtract(readingAnchor));
                            readingAnchor = changeLog.getNewReading();
                        }
                    }
                }
                //计算当天走了多少字 量程+昨天最后一次读数 - 锚点
                amount = amount.add(dayCurrReading.subtract(readingAnchor));

                //获取公式,计算当天的费用
                EnergyMeterSettingLog priceSetting = meterSettingLogProvider.findCurrentSettingByMeterId(meter.getNamespaceId(), meter.getId(), EnergyMeterSettingType.PRICE, meter.getLastReadTime());
                EnergyMeterSettingLog rateSetting = meterSettingLogProvider.findCurrentSettingByMeterId(meter.getNamespaceId(), meter.getId(), EnergyMeterSettingType.RATE , meter.getLastReadTime());
                EnergyMeterSettingLog amountSetting = meterSettingLogProvider.findCurrentSettingByMeterId(meter.getNamespaceId(), meter.getId(), EnergyMeterSettingType.AMOUNT_FORMULA , meter.getLastReadTime());
                if (Stream.of(priceSetting, rateSetting, amountSetting).anyMatch(Objects::isNull)) {
                    return null;
                }
                EnergyMeterFormula amountFormula = meterFormulaProvider.findById(amountSetting.getNamespaceId(), amountSetting.getFormulaId());
                if (amountFormula == null) {
                    return null;
                }
                ScriptEngineManager manager = new ScriptEngineManager();
                ScriptEngine engine = manager.getEngineByName("js");

                engine.put(MeterFormulaVariable.AMOUNT.getCode(), amount);
                engine.put(MeterFormulaVariable.PRICE.getCode(), priceSetting.getSettingValue());
                engine.put(MeterFormulaVariable.TIMES.getCode(), rateSetting.getSettingValue());

                BigDecimal realAmount;
                try {
                    realAmount = BigDecimal.valueOf(Double.valueOf(engine.eval(amountFormula.getExpression()).toString()));
                } catch (Exception e) {
                    e.printStackTrace();
                    LOGGER.error("The energy meter error");
                    throw errorWith(SCOPE, EnergyConsumptionServiceErrorCode.ERR_METER_FORMULA_ERROR, "The energy meter error");
                }
                if (realAmount.doubleValue() > 0 && statistic.getCurrentAmount().doubleValue() != 0) {
                    BigDecimal percent = realAmount.subtract(statistic.getCurrentAmount()).divide(statistic.getCurrentAmount(), BigDecimal.ROUND_HALF_UP);
                    if ((percent.doubleValue() * 100) >= dayPromptSetting.getSettingValue().doubleValue()) {
                        return dayPromptSetting.getSettingValue();
                    }
                }
            }
        }
        return null;
    }

    private BigDecimal processMonthPrompt(EnergyMeter meter) {
        Timestamp lastReadTime = meter.getLastReadTime();
        EnergyMeterDefaultSetting monthPromptSetting = defaultSettingProvider.findBySettingType(currNamespaceId(), EnergyMeterSettingType.MONTH_PROMPT);
        if (lastReadTime != null && monthPromptSetting != null && Objects.equals(monthPromptSetting.getStatus(), EnergyCommonStatus.ACTIVE.getCode())) {
            LocalDateTime lastReadDateTime = lastReadTime.toLocalDateTime();
            // lastReadingTime 前一月的开始
            Date lastReadingTimeLastMonthBegin = Date.valueOf(LocalDate.of(lastReadDateTime.getYear(), lastReadDateTime.getMonth().minus(1), 1));

            EnergyMonthStatistic statistic = energyMonthStatisticProvider.findByMeterAndDate(currNamespaceId(), meter.getId(), "" + lastReadDateTime.toLocalDate().getYear() + lastReadDateTime.toLocalDate().getMonthValue());
            if (statistic != null) {
                BigDecimal thisMonthAmount = energyDateStatisticProvider.getSumAmountBetweenDate(meter.getId(), lastReadingTimeLastMonthBegin, Date.valueOf(lastReadDateTime.toLocalDate().plusDays(1)));
                if (thisMonthAmount.doubleValue() > 0 && statistic.getCurrentAmount().doubleValue() != 0) {
                    BigDecimal percent = thisMonthAmount.subtract(statistic.getCurrentAmount()).divide(statistic.getCurrentAmount(), BigDecimal.ROUND_HALF_UP);
                    if ((percent.doubleValue() * 100) >= monthPromptSetting.getSettingValue().doubleValue()) {
                        return monthPromptSetting.getSettingValue();
                    }
                }
            }
        }
        return null;
    }

    @Override
    public EnergyMeterDTO updateEnergyMeter(UpdateEnergyMeterCommand cmd) {
        validate(cmd);
        checkCurrentUserNotInOrg(cmd.getOrganizationId());
        Tuple<EnergyMeter, Boolean> result = coordinationProvider.getNamedLock(CoordinationLocks.ENERGY_METER.getCode() + cmd.getMeterId()).enter(() -> {
            EnergyMeter meter = this.findMeterById(cmd.getMeterId());
            if (cmd.getName() != null) {
                meter.setName(cmd.getName());
            }
            if (cmd.getMeterNumber() != null) {
                meter.setMeterNumber(cmd.getMeterNumber());
            }
            dbProvider.execute(r -> {
                if (cmd.getPrice() != null || cmd.getConfigId() != null) {
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
            return meter;
        });
        return result.second() ? toEnergyMeterDTO(result.first()) : new EnergyMeterDTO();
    }

    private void insertMeterSettingLog(EnergyMeterSettingType settingType, UpdateEnergyMeterCommand cmd) {
        checkUpdateCommand(cmd);
        EnergyMeterSettingLog log = new EnergyMeterSettingLog();
        log.setStatus(EnergyCommonStatus.ACTIVE.getCode());
        log.setStartTime(new Timestamp(cmd.getStartTime()));
        // Timestamp endTime = cmd.getEndTime() == null ? Timestamp.valueOf(LocalDateTime.now().plusYears(100)): new Timestamp(cmd.getEndTime());
        if (cmd.getEndTime() != null) {
            log.setEndTime(new Timestamp(cmd.getEndTime()));
        }
        log.setMeterId(cmd.getMeterId());
        log.setSettingType(settingType.getCode());
        log.setNamespaceId(currNamespaceId());
        switch (settingType) {
            case PRICE:
                log.setCalculationType(cmd.getCalculationType());
                if(PriceCalculationType.STANDING_CHARGE_TARIFF.equals(PriceCalculationType.fromCode(cmd.getCalculationType()))) {
                    log.setSettingValue(cmd.getPrice());
                }
                if(PriceCalculationType.BLOCK_TARIFF.equals(PriceCalculationType.fromCode(cmd.getCalculationType()))) {
                    log.setConfigId(cmd.getConfigId());
                }

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

    private void checkUpdateCommand(UpdateEnergyMeterCommand cmd) {
        // 开始时间不能小于现在
        if (cmd.getStartTime() != null && cmd.getStartTime() < Date.valueOf(LocalDate.now()).getTime()) {
            LOGGER.error("Energy meter setting start time less then now.");
            throw errorWith(SCOPE, ERR_METER_SETTING_START_TIME_ERROR, "Energy meter setting start time less them now.");
        }
        // 结束时间不能小于开始时间
        if (cmd.getStartTime() != null && cmd.getEndTime() != null && cmd.getEndTime() - cmd.getStartTime() < 0) {
            LOGGER.error("Energy meter setting end time less then start time.");
            throw errorWith(SCOPE, ERR_METER_SETTING_END_TIME_ERROR, "Energy meter setting end time less them start time.");
        }
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

        EnergyMeter meter = this.findMeterById(cmd.getMeterId());

        // 创建读表记录
        EnergyMeterReadingLog log = new EnergyMeterReadingLog();
        log.setStatus(EnergyCommonStatus.ACTIVE.getCode());
        log.setReading(cmd.getNewReading());
        log.setCommunityId(cmd.getCommunityId());
        log.setMeterId(meter.getId());
        log.setNamespaceId(currNamespaceId());
        log.setOldMeterReading(cmd.getOldReading());
        log.setChangeMeterFlag(TrueOrFalseFlag.TRUE.getCode());
        log.setOperateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        log.setOperatorId(UserContext.current().getUser().getId());

        // 设置表记的信息
        meter.setLastReading(cmd.getNewReading());
        meter.setMaxReading(cmd.getMaxReading());
        meter.setLastReadTime(new Timestamp(DateHelper.currentGMTTime().getTime()));

        dbProvider.execute(r -> {
            meterReadingLogProvider.createEnergyMeterReadingLog(log);
            meterProvider.updateEnergyMeter(meter);

            // 创建换表记录
            EnergyMeterChangeLog changeLog = new EnergyMeterChangeLog();
            changeLog.setNamespaceId(currNamespaceId());
            changeLog.setMaxReading(cmd.getMaxReading());
            changeLog.setNewReading(cmd.getNewReading());
            changeLog.setOldReading(cmd.getOldReading());
            changeLog.setMeterId(cmd.getMeterId());
            changeLog.setReadingLogId(log.getId());
            changeLog.setStatus(EnergyCommonStatus.ACTIVE.getCode());
            changeLog.setOperateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
            changeLog.setCreatorUid(UserContext.current().getUser().getId());

            meterChangeLogProvider.createEnergyMeterChangeLog(changeLog);
            return true;
        });
        // 添加读表记录的索引
        readingLogSearcher.feedDoc(log);
    }

    @Override
    public void updateEnergyMeterStatus(UpdateEnergyMeterStatusCommand cmd) {
        validate(cmd);
        checkCurrentUserNotInOrg(cmd.getOrganizationId());
        coordinationProvider.getNamedLock(CoordinationLocks.ENERGY_METER.getCode() + cmd.getMeterId()).tryEnter(() -> {
            EnergyMeter meter = this.findMeterById(cmd.getMeterId());
            meter.setStatus(cmd.getStatus());
            dbProvider.execute(s -> {
                // 1.更新表记状态
                meterProvider.updateEnergyMeter(meter);
                if (EnergyMeterStatus.fromCode(cmd.getStatus()) == EnergyMeterStatus.INACTIVE) {
                    // 2.删除表记对应的读表记录
                    meterReadingLogProvider.deleteMeterReadingLogsByMeterId(currNamespaceId(), meter.getId());
                    List<EnergyMeterReadingLog> logs = meterReadingLogProvider.listMeterReadingLogsByMeterId(currNamespaceId(), meter.getId());
                    // 3.删除读表记录的索引
                    logs.forEach(log -> readingLogSearcher.deleteById(log.getId()));
                    // 4.删除表记索引
                    meterSearcher.deleteById(meter.getId());
                } else {
                    meterSearcher.feedDoc(meter);
                }
                return true;
            });
        });
    }

    @Override
    public EnergyMeterReadingLogDTO readEnergyMeter(ReadEnergyMeterCommand cmd) {
        validate(cmd);
        checkCurrentUserNotInOrg(cmd.getOrganizationId());
        EnergyMeter meter = this.findMeterById(cmd.getMeterId());

        // 读数大于最大量程
        if (cmd.getCurrReading().doubleValue() > meter.getMaxReading().doubleValue()) {
            LOGGER.error("Current reading greater then meter max reading, meterId = ", meter.getId());
            throw errorWith(SCOPE, ERR_CURR_READING_GREATER_THEN_MAX_READING, "Current reading greater then meter max reading, meterId = %s", meter.getId());
        }
        EnergyMeterReadingLog log = new EnergyMeterReadingLog();
        log.setStatus(EnergyCommonStatus.ACTIVE.getCode());
        log.setReading(cmd.getCurrReading());
        log.setCommunityId(meter.getCommunityId());
        log.setMeterId(meter.getId());
        log.setNamespaceId(currNamespaceId());
        log.setOperateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
        log.setOperatorId(UserContext.current().getUser().getId());
        log.setResetMeterFlag(cmd.getResetMeterFlag() != null ? cmd.getResetMeterFlag() : TrueOrFalseFlag.FALSE.getCode());
        dbProvider.execute(r -> {
            meterReadingLogProvider.createEnergyMeterReadingLog(log);
            meter.setLastReading(log.getReading());
            meter.setLastReadTime(Timestamp.valueOf(LocalDateTime.now()));
            meterProvider.updateEnergyMeter(meter);
            return true;
        });
        readingLogSearcher.feedDoc(log);
        return toEnergyMeterReadingLogDTO(log);
    }

    private EnergyMeterReadingLogDTO toEnergyMeterReadingLogDTO(EnergyMeterReadingLog log) {
        EnergyMeterReadingLogDTO dto = ConvertHelper.convert(log, EnergyMeterReadingLogDTO.class);
        User user = userProvider.findUserById(log.getOperatorId());
        dto.setOperatorName(user.getNickName());
        EnergyMeter meter = this.findMeterById(log.getMeterId());
        dto.setMeterName(meter.getName());
        // 处理抄表提示
        dto.setDayPrompt(this.processDayPrompt(meter));
        dto.setMonthPrompt(this.processMonthPrompt(meter));
        return dto;
    }

    @Override
    public void batchUpdateEnergyMeterSettings(BatchUpdateEnergyMeterSettingsCommand cmd) {
        validate(cmd);
        checkCurrentUserNotInOrg(cmd.getOrganizationId());
        if (cmd.getMeterIds() != null) {
            List<EnergyMeter> meters = meterProvider.listByIds(currNamespaceId(), cmd.getMeterIds());
            if (meters != null && meters.size() > 0) {
                meters.forEach(r -> {
                    UpdateEnergyMeterCommand updateCmd = new UpdateEnergyMeterCommand();
                    updateCmd.setMeterId(r.getId());
                    // 价格
                    if (cmd.getPrice() != null || cmd.getConfigId() != null) {
                        updateCmd.setPrice(cmd.getPrice());
                        updateCmd.setStartTime(cmd.getPriceStart());
                        updateCmd.setEndTime(cmd.getPriceEnd());
                        updateCmd.setConfigId(cmd.getConfigId());
                        updateCmd.setCalculationType(cmd.getCalculationType());
                        this.insertMeterSettingLog(EnergyMeterSettingType.PRICE, updateCmd);
                    }
                    // 倍率
                    if (cmd.getRate() != null) {
                        updateCmd.setRate(cmd.getRate());
                        updateCmd.setStartTime(cmd.getRateStart());
                        updateCmd.setEndTime(cmd.getRateEnd());
                        this.insertMeterSettingLog(EnergyMeterSettingType.RATE, updateCmd);
                    }
                    // 费用
                    if (cmd.getCostFormulaId() != null) {
                        updateCmd.setCostFormulaId(cmd.getCostFormulaId());
                        updateCmd.setStartTime(cmd.getCostFormulaStart());
                        updateCmd.setEndTime(cmd.getCostFormulaEnd());
                        this.insertMeterSettingLog(EnergyMeterSettingType.COST_FORMULA, updateCmd);
                    }
                    // 用量
                    if (cmd.getAmountFormulaId() != null) {
                        updateCmd.setAmountFormulaId(cmd.getAmountFormulaId());
                        updateCmd.setStartTime(cmd.getAmountFormulaStart());
                        updateCmd.setEndTime(cmd.getAmountFormulaEnd());
                        this.insertMeterSettingLog(EnergyMeterSettingType.AMOUNT_FORMULA, updateCmd);
                    }
                });
            }
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
        validate(cmd);
        checkCurrentUserNotInOrg(cmd.getOrganizationId());
        EnergyMeterReadingLog log = meterReadingLogProvider.getEnergyMeterReadingLogById(cmd.getLogId());
        if (log == null) {
            LOGGER.error("The energy meter reading log is not exist, id = {}", cmd.getLogId());
            throw errorWith(SCOPE, ERR_METER_READING_LOG_NOT_EXIST, "The energy meter reading log is not exist, id = %s", cmd.getLogId());
        }
        if (log.getOperateTime().toLocalDateTime().toLocalDate().isBefore(LocalDate.now())) {
            LOGGER.error("The energy meter reading log operate time is before today, id = {}", cmd.getLogId());
            throw errorWith(SCOPE, ERR_METER_READING_LOG_BEFORE_TODAY, "The energy meter reading log operate time is before today, id = %s", cmd.getLogId());
        }

        dbProvider.execute(r -> {
            EnergyMeterReadingLog lastReadingLog = meterReadingLogProvider.findLastReadingLogByMeterId(currNamespaceId(), log.getMeterId());
            meterReadingLogProvider.deleteEnergyMeterReadingLog(log);

            // 删除的记录是最后一条, 把表记的lastReading修改成新的最后一次读数
            if (Objects.equals(lastReadingLog.getId(), log.getId())) {
                EnergyMeter meter = meterProvider.findById(currNamespaceId(), log.getMeterId());
                lastReadingLog = meterReadingLogProvider.findLastReadingLogByMeterId(currNamespaceId(), log.getMeterId());
                if (lastReadingLog != null) {
                    meter.setLastReading(lastReadingLog.getReading());
                    meter.setLastReadTime(lastReadingLog.getOperateTime());
                } else {
                    meter.setLastReading(null);
                    meter.setLastReadTime(null);
                }
                meterProvider.updateEnergyMeter(meter);
            }
            return true;
        });
        readingLogSearcher.deleteById(log.getId());
    }

    @Override
    public EnergyMeterDefaultSettingDTO updateEnergyMeterDefaultSetting(UpdateEnergyMeterDefaultSettingCommand cmd) {
        validate(cmd);
        checkCurrentUserNotInOrg(cmd.getOrganizationId());
        EnergyMeterDefaultSetting setting = defaultSettingProvider.findById(currNamespaceId(), cmd.getSettingId());
        if (setting != null) {
            EnergyMeterSettingType settingType = EnergyMeterSettingType.fromCode(setting.getSettingType());
            if (settingType != null) {
                switch (settingType) {
                    case MONTH_PROMPT:
                    case DAY_PROMPT:
                        // 抄表提示类型
                        setting.setStatus(cmd.getSettingStatus() != null ? cmd.getSettingStatus() : setting.getStatus());
                        setting.setSettingValue(cmd.getSettingValue() != null ? cmd.getSettingValue() : setting.getSettingValue());
                        break;
                    case PRICE:
                        setting.setCalculationType(cmd.getCalculationType());
                        if(PriceCalculationType.STANDING_CHARGE_TARIFF.equals(PriceCalculationType.fromCode(cmd.getCalculationType()))) {
                            setting.setSettingValue(cmd.getSettingValue() != null ? cmd.getSettingValue() : setting.getSettingValue());
                            setting.setConfigId(0L);
                        }
                        if(PriceCalculationType.BLOCK_TARIFF.equals(PriceCalculationType.fromCode(cmd.getCalculationType()))) {
                            setting.setSettingValue(null);
                            setting.setConfigId(cmd.getConfigId());
                        }
                    case RATE:
                        // 倍率类型
                        setting.setSettingValue(cmd.getSettingValue() != null ? cmd.getSettingValue() : setting.getSettingValue());
                        break;
                    case AMOUNT_FORMULA:
                    case COST_FORMULA:
                        // 公式类型
                        setting.setFormulaId(cmd.getFormulaId() != null ? cmd.getFormulaId() : setting.getFormulaId());
                        break;
                    default:
                        break;
                }
            }
            defaultSettingProvider.updateEnergyMeterDefaultSetting(setting);
            return toEnergyMeterDefaultSettingDTO(setting);
        }
        return null;
    }

    @Override
    public EnergyMeterFormulaDTO createEnergyMeterFormula(CreateEnergyMeterFormulaCommand cmd) {
        validate(cmd);
        checkCurrentUserNotInOrg(cmd.getOwnerId());

        // 预处理公式的表达式
        String processedExpression = this.processFormulaExpression(cmd.getExpression());
        // 检查公式的合法性
        this.checkFormulaExpressionValid(processedExpression);

        EnergyMeterFormula formula = new EnergyMeterFormula();
        formula.setOwnerId(cmd.getOwnerId());
        formula.setOwnerType(cmd.getOwnerType());
        formula.setCommunityId(cmd.getCommunityId());

        formula.setStatus(EnergyCommonStatus.ACTIVE.getCode());
        formula.setExpression(processedExpression);
        formula.setName(cmd.getName());
        EnergyFormulaType formulaType = EnergyFormulaType.fromCode(cmd.getFormulaType());
        if (formulaType == null) {
            invalidParameterException("formulaType", cmd.getFormulaType());
        }
        formula.setFormulaType(cmd.getFormulaType());
        formula.setNamespaceId(currNamespaceId());
        formula.setDisplayExpression(cmd.getExpression());
        meterFormulaProvider.createEnergyMeterFormula(formula);
        return toEnergyMeterFormulaDTO(formula);
    }

    private void checkFormulaExpressionValid(String processedExpression) {
        if (processedExpression == null) {
            LOGGER.error("The energy meter formula format error");
            throw errorWith(SCOPE, ERR_METER_FORMULA_ERROR, "The energy meter error");
        }
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("js");

        engine.put(MeterFormulaVariable.AMOUNT.getCode(), 1);
        engine.put(MeterFormulaVariable.PRICE.getCode(), 2);
        engine.put(MeterFormulaVariable.TIMES.getCode(), 3);
        engine.put(MeterFormulaVariable.REAL_AMOUNT.getCode(), 4);

        try {
            engine.eval(processedExpression);
        } catch (Exception e) {
            LOGGER.error("The energy meter formula format error, expression = {}", processedExpression);
            throw errorWith(SCOPE, ERR_METER_FORMULA_ERROR, "The energy meter formula format error, expression = %s", processedExpression);
        }
    }

    private String processFormulaExpression(String inputExpression) {
        List<EnergyMeterFormulaVariable> variables = meterFormulaVariableProvider.listMeterFormulaVariables();
        String processedExpression = inputExpression;
        for (EnergyMeterFormulaVariable var : variables) {
            Pattern pattern = Pattern.compile("(\\[\\[" + var.getDisplayName() + "\\]\\])");
            Matcher matcher = pattern.matcher(processedExpression);
            while (matcher.find()) {
                int start = matcher.start(1);
                int end = matcher.end(1);
                processedExpression = new StringBuilder(processedExpression).replace(start, end, var.getName()).toString();
                matcher = pattern.matcher(processedExpression);
            }
        }
        return processedExpression;
    }

    @Override
    public EnergyMeterCategoryDTO createEnergyMeterCategory(CreateEnergyMeterCategoryCommand cmd) {
        validate(cmd);
        checkCurrentUserNotInOrg(cmd.getOwnerId());
        EnergyMeterCategory category = new EnergyMeterCategory();
        category.setOwnerId(cmd.getOwnerId());
        category.setOwnerType(cmd.getOwnerType());
        category.setCommunityId(cmd.getCommunityId());
        category.setNamespaceId(currNamespaceId());
        category.setName(cmd.getName());
        category.setDeleteFlag(TrueOrFalseFlag.TRUE.getCode());
        category.setCategoryType(cmd.getCategoryType());
        category.setStatus(EnergyCommonStatus.ACTIVE.getCode());
        meterCategoryProvider.createEnergyMeterCategory(category);
        return ConvertHelper.convert(category, EnergyMeterCategoryDTO.class);
    }

    @Override
    public EnergyMeterCategoryDTO updateEnergyMeterCategory(UpdateEnergyMeterCategoryCommand cmd) {
        validate(cmd);
        checkCurrentUserNotInOrg(cmd.getOrganizationId());
        Tuple<EnergyMeterCategory, Boolean> result = coordinationProvider.getNamedLock(CoordinationLocks.ENERGY_METER_CATEGORY.getCode() + cmd.getCategoryId()).enter(() -> {
            EnergyMeterCategory category = meterCategoryProvider.findById(currNamespaceId(), cmd.getCategoryId());
            if (category != null) {
                category.setName(cmd.getName());
                meterCategoryProvider.updateEnergyMeterCategory(category);
            }
            return category;
        });
        return result.second() ? ConvertHelper.convert(result.first(), EnergyMeterCategoryDTO.class) : new EnergyMeterCategoryDTO();
    }

    @Override
    public void deleteEnergyMeterCategory(DeleteEnergyMeterCategoryCommand cmd) {
        validate(cmd);
        checkCurrentUserNotInOrg(cmd.getOrganizationId());
        coordinationProvider.getNamedLock(CoordinationLocks.ENERGY_METER_CATEGORY.getCode() + cmd.getCategoryId()).tryEnter(() -> {
            EnergyMeterCategory category = meterCategoryProvider.findById(currNamespaceId(), cmd.getCategoryId());
            if (category != null) {
                EnergyMeter meter = meterProvider.findAnyByCategoryId(currNamespaceId(), category.getId());
                if (meter != null) {
                    LOGGER.info("Energy meter category has been reference, categoryId = {}", category.getId());
                    throw errorWith(SCOPE, ERR_METER_CATEGORY_HAS_BEEN_REFERENCE, "Energy meter category has been reference");
                }
                meterCategoryProvider.deleteEnergyMeterCategory(category);
            }
        });
    }

    @Override
    public void importEnergyMeter(ImportEnergyMeterCommand cmd, MultipartFile file) {
        validate(cmd);
        checkCurrentUserNotInOrg(cmd.getOwnerId());

        List<EnergyMeter> meterList = new ArrayList<>();
        ArrayList list = processorExcel(file);
        for (int i = 2; i < list.size(); i++) {
            RowResult result = (RowResult) list.get(i);
            if (Stream.of(result.getA(), result.getB(), result.getC(), result.getD(), result.getE(), result.getF()).anyMatch(StringUtils::isEmpty)) {
                continue;
            }
            EnergyMeter meter = new EnergyMeter();
            meter.setOwnerId(cmd.getOwnerId());
            meter.setOwnerType(cmd.getOwnerType());
            meter.setStatus(EnergyMeterStatus.ACTIVE.getCode());
            meter.setCommunityId(cmd.getCommunityId());
            meter.setNamespaceId(currNamespaceId());
            meter.setName(result.getA());
            meter.setMeterNumber(result.getB());
            LocaleString meterTypeLocale = localeStringProvider.findByText(EnergyLocalStringCode.SCOPE_METER_TYPE, result.getC(), currLocale());
            if (meterTypeLocale != null) {
                meter.setMeterType(Byte.valueOf(meterTypeLocale.getCode()));
            } else {
                LOGGER.error("Import energy meter error, error field meterType");
                throw errorWith(SCOPE, ERR_METER_IMPORT, "Import energy meter error, error field meterType");
            }
            EnergyMeterCategory category = meterCategoryProvider.findByName(currNamespaceId(), result.getD());
            if (category != null) {
                meter.setBillCategoryId(category.getId());
            } else {
                LOGGER.error("Import energy meter error, error field meterType");
                throw errorWith(SCOPE, ERR_METER_IMPORT, "Import energy meter error, error field category");
            }
            category = meterCategoryProvider.findByName(currNamespaceId(), result.getE());
            if (category != null) {
                meter.setServiceCategoryId(category.getId());
            } else {
                LOGGER.error("Import energy meter error, error field meterType");
                throw errorWith(SCOPE, ERR_METER_IMPORT, "Import energy meter error, error field category");
            }
            if (NumberUtils.isNumber(result.getF())) {
                meter.setMaxReading(new BigDecimal(result.getF()));
            } else {
                LOGGER.error("Import energy meter error, error field meterType");
                throw errorWith(SCOPE, ERR_METER_IMPORT, "Import energy meter error, error field category");
            }
            if (NumberUtils.isNumber(result.getG())) {
                meter.setStartReading(new BigDecimal(result.getG()));
            } else {
                meter.setStartReading(new BigDecimal("0"));
            }
            if (NumberUtils.isNumber(result.getH())) {
                meter.setPrice(new BigDecimal(result.getH()));
            }
//            else {
//                meter.setStartReading(new BigDecimal("1"));
//            }
            if (NumberUtils.isNumber(result.getI())) {
                meter.setRate(new BigDecimal(result.getI()));
            } else {
                meter.setStartReading(new BigDecimal("1"));
            }
            EnergyMeterFormula formula = meterFormulaProvider.findByName(currNamespaceId(), result.getJ());
            if (formula != null) {
                meter.setAmountFormulaId(formula.getId());
            } else {
                LOGGER.error("Import energy meter error, error field meterType");
                throw errorWith(SCOPE, ERR_METER_IMPORT, "Import energy meter error, error field formula");
            }
            formula = meterFormulaProvider.findByName(currNamespaceId(), result.getK());
            if (formula != null) {
                meter.setCostFormulaId(formula.getId());
            } else {
                LOGGER.error("Import energy meter error, error field meterType");
                throw errorWith(SCOPE, ERR_METER_IMPORT, "Import energy meter error, error field formula");
            }
            if(NumberUtils.isNumber(result.getL())) {
                meter.setCalculationType(Byte.valueOf(result.getL()));
            }
            if(!StringUtils.isEmpty(result.getM())) {
                EnergyMeterPriceConfig priceConfig = priceConfigProvider.findByName(result.getM(), cmd.getOwnerId(),
                                                            cmd.getOwnerType(), cmd.getCommunityId(), currNamespaceId());
                if(priceConfig != null) {
                    meter.setConfigId(priceConfig.getId());
                } else {
                    LOGGER.error("Import energy meter error, error field price config");
                    throw errorWith(SCOPE, ERR_METER_IMPORT, "Import energy meter error, error field price config");
                }
            }
            dbProvider.execute(r -> {
                meterProvider.createEnergyMeter(meter);
                // 创建setting log 记录
                UpdateEnergyMeterCommand updateCmd = new UpdateEnergyMeterCommand();
                updateCmd.setPrice(meter.getPrice());
                updateCmd.setRate(meter.getRate());
                updateCmd.setCostFormulaId(meter.getCostFormulaId());
                updateCmd.setAmountFormulaId(meter.getAmountFormulaId());
                updateCmd.setStartTime(Date.valueOf(LocalDate.now()).getTime());
                updateCmd.setMeterId(meter.getId());
                updateCmd.setCalculationType(meter.getCalculationType());
                updateCmd.setConfigId(meter.getConfigId());
                this.insertMeterSettingLog(EnergyMeterSettingType.PRICE, updateCmd);
                this.insertMeterSettingLog(EnergyMeterSettingType.RATE, updateCmd);
                this.insertMeterSettingLog(EnergyMeterSettingType.AMOUNT_FORMULA, updateCmd);
                this.insertMeterSettingLog(EnergyMeterSettingType.COST_FORMULA, updateCmd);

                // 创建一条初始读表记录
                ReadEnergyMeterCommand readEnergyMeterCmd = new ReadEnergyMeterCommand();
                readEnergyMeterCmd.setCommunityId(cmd.getCommunityId());
                readEnergyMeterCmd.setCurrReading(meter.getStartReading());
                readEnergyMeterCmd.setMeterId(meter.getId());
                readEnergyMeterCmd.setOrganizationId(cmd.getOwnerId());
                readEnergyMeterCmd.setResetMeterFlag(TrueOrFalseFlag.FALSE.getCode());
                this.readEnergyMeter(readEnergyMeterCmd);
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
        MySheetContentsHandler sheetContentHandler = new MySheetContentsHandler();
        try {
            SAXHandlerEventUserModel userModel = new SAXHandlerEventUserModel(sheetContentHandler);
            userModel.processASheets(file.getInputStream(), 0);
        } catch (Exception e) {
            LOGGER.error("Process excel error.", e);
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_GENERAL_EXCEPTION,
                    "Process excel error.");
        }
        return sheetContentHandler.getResultList();
    }

    /**
     * 获取月份起始日期
     */
    public static Calendar getStartDate(Date date)  {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        return calendar ;
    }

    /**
     * 获取月份最后日期 -如果是本月就取今天
     */
    private static Calendar getMaxMonthDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        //本月则就是今天了
        if (monthSF.get().format(date).equals(monthSF.get().format(DateHelper.currentGMTTime()))){
            calendar.setTime(DateHelper.currentGMTTime());
        }
        else {
            calendar.setTime(date);
            calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        }
        return calendar ;
    }

    /**
     * 获取去年12月
     */
    private static Calendar getYearBeginDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.YEAR, -1);
        calendar.set(Calendar.MONTH, calendar.getActualMaximum(Calendar.MONTH));
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
        calendar.set(Calendar.HOUR_OF_DAY, calendar.getActualMinimum(Calendar.HOUR_OF_DAY));
        calendar.set(Calendar.SECOND, calendar.getActualMinimum(Calendar.SECOND));
        calendar.set(Calendar.MILLISECOND, calendar.getActualMinimum(Calendar.MILLISECOND));
        calendar.set(Calendar.MINUTE, calendar.getActualMinimum(Calendar.MINUTE));
        return calendar ;
    }

    /**
     * 获取年查询的endDate - 如果是今年则就上个月 其他则末月
     * @param date
     * @return
     * @throws ParseException
     */
    public static Calendar getYearEndDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        //本月则就是上个月了
        if (yearSF.get().format(date).equals(yearSF.get().format(DateHelper.currentGMTTime()))){
            calendar.setTime(DateHelper.currentGMTTime());
            calendar.add(Calendar.MONTH, -1);
        }
        else {
            calendar.setTime(date);
            calendar.set(Calendar.MONTH, calendar.getActualMaximum(Calendar.MONTH));
        }

        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
        calendar.set(Calendar.HOUR_OF_DAY, calendar.getActualMinimum(Calendar.HOUR_OF_DAY));
        calendar.set(Calendar.SECOND, calendar.getActualMinimum(Calendar.SECOND));
        calendar.set(Calendar.MILLISECOND, calendar.getActualMinimum(Calendar.MILLISECOND));
        calendar.set(Calendar.MINUTE, calendar.getActualMinimum(Calendar.MINUTE));
        return calendar ;
    }

    private List<DayStatDTO> deepCopyStatDays(List<DayStatDTO> days){
        List<DayStatDTO> result = new ArrayList<>();
        if(days == null )
            return null;
        for(DayStatDTO day : days ){
            result.add(ConvertHelper.convert(day, DayStatDTO.class));
        }
        return result;
    }

    @Override
    public EnergyStatDTO getEnergyStatByDay(EnergyStatCommand cmd) {
        //查所有符合条件的记录
        EnergyStatDTO result = new EnergyStatDTO();
        result.setBillDayStats(new ArrayList<>());
        result.setDayBurdenStats(new ArrayList<>());
        SimpleDateFormat dateStrSF = new SimpleDateFormat("yyyy年MM月");
        result.setDateStr(dateStrSF.format(new Date(cmd.getStatDate())));
        Community com = this.communityProvider.findCommunityById(cmd.getCommunityId());
        if (null == com) {
            return null;
        }
        result.setCommunityName(com.getName());
        result.setMeterType(EnergyMeterType.fromCode(cmd.getMeterType()) == EnergyMeterType.WATER ? "用水分析"
                : (EnergyMeterType.fromCode(cmd.getMeterType()) == EnergyMeterType.ELECTRIC ? "用电分析" : ""));

        // 参数日期的开始
        Date startDate = new Date(getStartDate(new Date(cmd.getStatDate())).getTime().getTime());
        Date endDate = new Date(getMaxMonthDate(new Date(cmd.getStatDate())).getTime().getTime());

        List<EnergyDateStatistic> dayStats = this.energyDateStatisticProvider.listEnergyDateStatistics(cmd.getMeterType(),
                cmd.getCommunityId(), cmd.getBillCategoryIds(), cmd.getServiceCategoryIds(), startDate, endDate);

        Calendar beginCalendar = Calendar.getInstance();
        beginCalendar.setTime(startDate);

        Calendar endCalendar = Calendar.getInstance();
        endCalendar.setTime(endDate);
        int loopTimes = 0;
        result.setDates(new ArrayList<>());

        while(beginCalendar.compareTo(endCalendar) < 1) {
            dateStrSF = new SimpleDateFormat("MM月dd日");
            DayStatDTO date = new DayStatDTO();
            date.setStatDate(new Date(beginCalendar.getTime().getTime()).getTime());
            date.setDateStr(dateStrSF.format(beginCalendar.getTime()));
            result.getDates().add(date);
            //日期增加每一天,循环不能超过33天
            beginCalendar.add(Calendar.DAY_OF_MONTH, 1);
            if(loopTimes ++ > 33)
                break;
        }

        //查不到就返回null
        if(null == dayStats)
            return null;

        for(EnergyDateStatistic dayStat : dayStats) {
            dateStrSF = new SimpleDateFormat("MM月dd日");
            DayStatDTO dayDTO = ConvertHelper.convert(dayStat, DayStatDTO.class);
            dayDTO.setStatDate(dayStat.getStatDate().getTime());
            dayDTO.setDateStr(dateStrSF.format(dayStat.getStatDate()));

            //查询是否有项目dto,没有则添加,有则累加用量
            BillStatDTO billDTO = findBillDTO(result, dayStat.getBillCategoryId());
            if(null == billDTO) {
                billDTO = new BillStatDTO();
                EnergyMeterCategory billCategory = this.meterCategoryProvider.findById(dayStat.getBillCategoryId());
                billDTO.setBillCategoryId(billCategory.getId());
                billDTO.setBillCategoryName(billCategory.getName());
                billDTO.setDayBillStats(deepCopyStatDays(result.getDates()));
                billDTO.setServiceDayStats(new ArrayList<ServiceStatDTO>());
                result.getBillDayStats().add(billDTO);
            }
            DayStatDTO dayBillStat = findDayStat(billDTO.getDayBillStats(), dayDTO.getStatDate());
            dayBillStat.setCurrentAmount(dayBillStat.getCurrentAmount().add(dayDTO.getCurrentAmount()));
            dayBillStat.setCurrentCost(dayBillStat.getCurrentCost().add(dayDTO.getCurrentCost()));

            //查询是否有性质dto,没有则添加,有则累加用量
            ServiceStatDTO serviceDTO = findServiceStatDTO(billDTO, dayStat.getServiceCategoryId());
            if(null == serviceDTO) {
                serviceDTO = new ServiceStatDTO();
                EnergyMeterCategory serviceCategory = this.meterCategoryProvider.findById(dayStat.getServiceCategoryId());
                serviceDTO.setServiceCategoryId(serviceCategory.getId());
                serviceDTO.setServiceCategoryName(serviceCategory.getName());
                serviceDTO.setDayServiceStats(deepCopyStatDays(result.getDates()));
                serviceDTO.setMeterDayStats(new ArrayList<MeterStatDTO>());
                billDTO.getServiceDayStats().add(serviceDTO);
            }
            DayStatDTO dayServiceStat = findDayStat(serviceDTO.getDayServiceStats(), dayDTO.getStatDate());
            dayServiceStat.setCurrentAmount(dayServiceStat.getCurrentAmount().add(dayDTO.getCurrentAmount()));
            dayServiceStat.setCurrentCost(dayServiceStat.getCurrentCost().add(dayDTO.getCurrentCost()));

            //查询是否有表DTO 没有则添加 有则累加用量
            MeterStatDTO meterDTO = findMeterStatDTO(serviceDTO, dayStat.getMeterId());
            if(meterDTO == null) {
                meterDTO = ConvertHelper.convert(dayStat, MeterStatDTO.class);
                meterDTO.setDayStats(deepCopyStatDays(result.getDates()));
                serviceDTO.getMeterDayStats().add(meterDTO);
            }
            DayStatDTO meterDayStat = findDayStat(meterDTO.getDayStats(), dayDTO.getStatDate());
            meterDayStat.setCurrentAmount(dayDTO.getCurrentAmount());
            meterDayStat.setCurrentCost(dayDTO.getCurrentCost());
            meterDayStat.setCurrentReading(dayDTO.getCurrentReading());
            meterDayStat.setLastReading(dayDTO.getLastReading());
        }

        //最终计算实际负担
        result.setDayBurdenStats(new ArrayList<>());
        BillStatDTO receivableDTO = findBillDTO(result, EnergyCategoryDefault.RECEIVABLE.getCode());
        BillStatDTO payableDTO = findBillDTO(result, EnergyCategoryDefault.PAYABLE.getCode());

        Set<Long> statDates = new HashSet<>();
        if (receivableDTO != null && receivableDTO.getDayBillStats() != null) {
            receivableDTO.getDayBillStats().forEach(r -> {
                statDates.add(r.getStatDate());
            });
        }
        if (payableDTO != null && payableDTO.getDayBillStats() != null) {
            payableDTO.getDayBillStats().forEach(r -> {
                statDates.add(r.getStatDate());
            });
        }

        if (null != receivableDTO && null != payableDTO) {
            for (Long statDate : statDates) {
                DayStatDTO receivableDay = findDayStat(receivableDTO.getDayBillStats(), statDate);
                DayStatDTO payableDay = findDayStat(payableDTO.getDayBillStats(), statDate);
                DayStatDTO burdenDay = new DayStatDTO();
                //应收减应付
                burdenDay.setCurrentAmount(payableDay.getCurrentAmount().subtract(receivableDay.getCurrentAmount()));
                burdenDay.setCurrentCost(payableDay.getCurrentCost().subtract(receivableDay.getCurrentCost()));
                result.getDayBurdenStats().add(burdenDay);
            }
        }

        sumStatDTO(result);
        return result;
    }

    /**
     * 计算result的所有合计
     * */
    private void sumStatDTO(EnergyStatDTO result) {
        sumStatDTO(result.getDayBurdenStats());
        sumStatDTO(result.getLastYearPayableStats());
        if (null != result.getBillDayStats()) {
            for (BillStatDTO bill : result.getBillDayStats()) {
                sumStatDTO(bill.getDayBillStats());
                if (null != bill.getServiceDayStats()) {
                    for (ServiceStatDTO service : bill.getServiceDayStats()) {
                        sumStatDTO(service.getDayServiceStats());
                        if (null != service.getMeterDayStats()) {
                            for (MeterStatDTO meter : service.getMeterDayStats()) {
                                sumStatDTO(meter.getDayStats());
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * 计算一个DayStatDTO list 的 合计
     * */
    private void sumStatDTO(List<DayStatDTO> dayStats) {
        if(dayStats == null)
            return;
        DayStatDTO sumDTO = new DayStatDTO();
        sumDTO.setDateStr("合计");
        for(DayStatDTO dto : dayStats){
            sumDTO.setCurrentAmount(sumDTO.getCurrentAmount().add(dto.getCurrentAmount()));
            sumDTO.setCurrentCost(sumDTO.getCurrentCost().add(dto.getCurrentCost()));
        }
        dayStats.add(sumDTO);
    }

    private DayStatDTO findDayStat(List<DayStatDTO> list , Long statDate){
        DayStatDTO dayStatDTO = new DayStatDTO();
        dayStatDTO.setStatDate(statDate);
        dayStatDTO.setCurrentAmount(BigDecimal.ZERO);
        dayStatDTO.setCurrentCost(BigDecimal.ZERO);
        if(null != list && list.size()>0) {
            for (DayStatDTO dto : list) {
                if (dto.getStatDate().equals(statDate)) {
                    return dto;
                }
            }
            list.add(dayStatDTO);
        }
        return dayStatDTO;
    }

    /**
     * 通过id查DTO是否有该项目
     */
    private BillStatDTO findBillDTO(EnergyStatDTO fatherDto , Long id){
        if(null != fatherDto.getBillDayStats() && fatherDto.getBillDayStats().size()>0)
            for(BillStatDTO dto : fatherDto.getBillDayStats() )
                if(dto.getBillCategoryId().equals(id))
                    return dto;
        return null;
    }

    /**
     * 通过id查DTO是否有该性质
     */
    private ServiceStatDTO findServiceStatDTO(BillStatDTO fatherDto , Long id){
        if(null != fatherDto.getServiceDayStats() && fatherDto.getServiceDayStats().size()>0)
            for(ServiceStatDTO dto : fatherDto.getServiceDayStats() )
                if(dto.getServiceCategoryId().equals(id))
                    return dto;
        return null;
    }

    /**
     * 通过id查DTO是否有该表记
     */
    private MeterStatDTO findMeterStatDTO(ServiceStatDTO fatherDto , Long id){
        if(null != fatherDto.getMeterDayStats() && fatherDto.getMeterDayStats().size()>0)
            for(MeterStatDTO dto : fatherDto.getMeterDayStats() )
                if(dto.getMeterId().equals(id))
                    return dto;
        return null;
    }

    @Override
    public EnergyStatDTO getEnergyStatByMonth(EnergyStatCommand cmd)   {
        SimpleDateFormat dateStrSF = new SimpleDateFormat("yyyy年MM月");

        //TODO: check cmd
        //查所有符合条件的记录
        EnergyStatDTO result = new EnergyStatDTO();
        result.setBillDayStats(new ArrayList<>());
        result.setDayBurdenStats(new ArrayList<>());
        result.setDateStr(yearSF.get().format(new Date(cmd.getStatDate())));
        Community com = this.communityProvider.findCommunityById(cmd.getCommunityId());
        if(null == com)
            return null;
        result.setCommunityName(com.getName());
        result.setMeterType(EnergyMeterType.WATER.getCode().equals(cmd.getMeterType())?"用水分析"
                :EnergyMeterType.ELECTRIC.getCode().equals(cmd.getMeterType())?"用电分析":"");

        List<EnergyMonthStatistic> monthStats = this.energyMonthStatisticProvider.listEnergyMonthStatistics(cmd.getMeterType(),
                cmd.getCommunityId(),cmd.getBillCategoryIds(),cmd.getServiceCategoryIds(),yearSF.get().format(new Date(cmd.getStatDate())));

        //查不到就返回null
        if(null == monthStats)
            return null;

        try {
            //把所有的日期单独拿出来,做表头
            Calendar beginCalendar = Calendar.getInstance();
            beginCalendar.setTime(getYearBeginDate(new Date(cmd.getStatDate())).getTime());
            Calendar endCalendar = Calendar.getInstance();
            endCalendar.setTime(getYearEndDate(new Date(cmd.getStatDate())).getTime());
            int loopTimes = 0;
            result.setDates(new ArrayList<>());
            while(beginCalendar.compareTo(endCalendar) < 1) {
                DayStatDTO date = new DayStatDTO();
                date.setStatDate(beginCalendar.getTime().getTime());
                date.setDateStr(dateStrSF.format(beginCalendar.getTime()));
                result.getDates().add(date);

                //日期增加每一天,循环不能超过13个月
                beginCalendar.add(Calendar.MONTH, 1);
                if(loopTimes ++ > 13)
                    break;
            }

            for(EnergyMonthStatistic monthStat : monthStats) {
                DayStatDTO dayDTO = ConvertHelper.convert(monthStat, DayStatDTO.class);

                dayDTO.setStatDate(monthSF.get().parse(monthStat.getDateStr()).getTime());
                dayDTO.setDateStr(dateStrSF.format(monthSF.get().parse(monthStat.getDateStr())));

                // 查询是否有项目dto,没有则添加,有则累加用量
                BillStatDTO billDTO = findBillDTO(result, monthStat.getBillCategoryId());
                if(null == billDTO){
                    billDTO = new BillStatDTO();
                    EnergyMeterCategory billCategory = this.meterCategoryProvider.findById(monthStat.getBillCategoryId());
                    billDTO.setBillCategoryId(billCategory.getId());
                    billDTO.setBillCategoryName(billCategory.getName());
                    billDTO.setDayBillStats(deepCopyStatDays(result.getDates()));
                    billDTO.setServiceDayStats(new ArrayList<>());
                    result.getBillDayStats().add(billDTO);
                }
                DayStatDTO dayBillStat = findDayStat(billDTO.getDayBillStats(), dayDTO.getStatDate());
                dayBillStat.setCurrentAmount(dayBillStat.getCurrentAmount().add(dayDTO.getCurrentAmount()));
                dayBillStat.setCurrentCost(dayBillStat.getCurrentCost().add(dayDTO.getCurrentCost()));

                //查询是否有性质dto,没有则添加,有则累加用量
                ServiceStatDTO serviceDTO = findServiceStatDTO(billDTO, monthStat.getServiceCategoryId());
                if(null == serviceDTO){
                    serviceDTO = new ServiceStatDTO();
                    EnergyMeterCategory serviceCategory = this.meterCategoryProvider.findById(monthStat.getServiceCategoryId());
                    serviceDTO.setServiceCategoryId(serviceCategory.getId());
                    serviceDTO.setServiceCategoryName(serviceCategory.getName());
                    serviceDTO.setDayServiceStats(deepCopyStatDays(result.getDates()));
                    serviceDTO.setMeterDayStats(new ArrayList<>());
                    billDTO.getServiceDayStats().add(serviceDTO);
                }
                DayStatDTO dayServiceStat = findDayStat(serviceDTO.getDayServiceStats(), dayDTO.getStatDate());
                dayServiceStat.setCurrentAmount(dayServiceStat.getCurrentAmount().add(dayDTO.getCurrentAmount()));
                dayServiceStat.setCurrentCost(dayServiceStat.getCurrentCost().add(dayDTO.getCurrentCost()));

                //查询是否有表DTO 没有则添加 有则累加用量
                MeterStatDTO meterDTO = findMeterStatDTO(serviceDTO, monthStat.getMeterId());
                if(meterDTO == null){
                    meterDTO = ConvertHelper.convert(monthStat, MeterStatDTO.class);
                    meterDTO.setDayStats(deepCopyStatDays(result.getDates()));
                    serviceDTO.getMeterDayStats().add(meterDTO);
                }
                // TODO did not have to add this amount or cost ?
                DayStatDTO meterDayStat = findDayStat(meterDTO.getDayStats(), dayDTO.getStatDate());
                meterDayStat.setCurrentAmount(dayDTO.getCurrentAmount());
                meterDayStat.setCurrentCost(dayDTO.getCurrentCost());
                meterDayStat.setCurrentReading(dayDTO.getCurrentReading());
                meterDayStat.setLastReading(dayDTO.getLastReading());
            }

            //最终计算实际负担  和同比
            result.setLastYearPayableStats(new ArrayList<>());
            result.setDayBurdenStats(new ArrayList<>());

            BillStatDTO receivableDTO = findBillDTO(result, EnergyCategoryDefault.RECEIVABLE.getCode());
            BillStatDTO payableDTO = findBillDTO(result, EnergyCategoryDefault.PAYABLE.getCode());

            Set<Long> statDates = new HashSet<>();

            if (receivableDTO != null && receivableDTO.getDayBillStats() != null) {
                receivableDTO.getDayBillStats().forEach(r -> statDates.add(r.getStatDate()));
            }
            if (payableDTO != null && payableDTO.getDayBillStats() != null) {
                payableDTO.getDayBillStats().forEach(r -> statDates.add(r.getStatDate()));
            }

            for(Long statDate : statDates) {
                BigDecimal receivableAmount = BigDecimal.ZERO;
                BigDecimal receivableCost = BigDecimal.ZERO;
                BigDecimal payableAmount = BigDecimal.ZERO;
                BigDecimal payableCost = BigDecimal.ZERO;

                if (receivableDTO != null) {
                    DayStatDTO receivableDay = findDayStat(receivableDTO.getDayBillStats(), statDate);
                    receivableAmount = receivableDay.getCurrentAmount();
                    receivableCost = receivableDay.getCurrentCost();
                }
                if (payableDTO != null) {
                    DayStatDTO payableDay = findDayStat(payableDTO.getDayBillStats(), statDate);
                    payableAmount = payableDay.getCurrentAmount();
                    payableCost = payableDay.getCurrentCost();
                }

                DayStatDTO burdenDay = new DayStatDTO();
                DayStatDTO lastYearDTO = new DayStatDTO();
                // 应付减应收
                burdenDay.setStatDate(statDate);
                burdenDay.setCurrentAmount(payableAmount.subtract(receivableAmount));
                burdenDay.setCurrentCost(payableCost.subtract(receivableCost));
                result.getDayBurdenStats().add(burdenDay);
                result.getLastYearPayableStats().add(lastYearDTO);
                lastYearDTO.setStatDate(statDate);
                //得到去年的字符串
                Calendar lastYear = Calendar.getInstance();
                lastYear.setTime(new Date(statDate));
                lastYear.add(Calendar.YEAR,-1);
                lastYearDTO.setCurrentAmount(energyCountStatisticProvider.getSumAmount(monthSF.get().format(lastYear.getTime()),cmd.getMeterType()
                        ,EnergyStatisticType.BILL.getCode(),EnergyCategoryDefault.PAYABLE.getCode()));
                lastYearDTO.setCurrentCost(energyCountStatisticProvider.getSumCost(monthSF.get().format(lastYear.getTime()),cmd.getMeterType()
                        ,EnergyStatisticType.BILL.getCode(),EnergyCategoryDefault.PAYABLE.getCode()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        sumStatDTO(result);
        return result ;
    }

    @Override
    public List<EnergyStatByYearDTO> getEnergyStatisticByYear(EnergyStatCommand cmd) {
        List<EnergyStatByYearDTO> result = new ArrayList<>();
        Calendar statDate = Calendar.getInstance();
        statDate.setTime(new Date(cmd.getStatDate()));
        Calendar anchorDate = Calendar.getInstance();
        anchorDate.setTime(new Date(cmd.getStatDate()));
        //从年初开始
        anchorDate.set(Calendar.DAY_OF_YEAR, anchorDate.getActualMinimum(Calendar.DAY_OF_YEAR));
        int i = 0;
        while(anchorDate.compareTo(statDate)<=0){
            //避免死循环
            if(i++ >12)
                break;
            String dateStr = monthSF.get().format(anchorDate.getTime());
            EnergyStatByYearDTO dto = new EnergyStatByYearDTO();
            dto.setDateStr(dateStr);
            result.add(dto);
            //循环插入每一个数据
            //用量
            //本月-水费-账单项目-应收
            dto.setWaterReceivableAmount(energyCountStatisticProvider.getSumAmount(dateStr,EnergyMeterType.WATER.getCode()
                    ,EnergyStatisticType.BILL.getCode(),EnergyCategoryDefault.RECEIVABLE.getCode()) );
            //本月-水费-账单项目-应付
            dto.setWaterPayableAmount(energyCountStatisticProvider.getSumAmount(dateStr,EnergyMeterType.WATER.getCode()
                    ,EnergyStatisticType.BILL.getCode(),EnergyCategoryDefault.PAYABLE.getCode()) );

            dto.setWaterBurdenAmount(dto.getWaterPayableAmount().subtract(dto.getWaterReceivableAmount()));


            //本月-电费-账单项目-应收
            dto.setElectricReceivableAmount(energyCountStatisticProvider.getSumAmount(dateStr,EnergyMeterType.ELECTRIC.getCode()
                    ,EnergyStatisticType.BILL.getCode(),EnergyCategoryDefault.RECEIVABLE.getCode()) );
            //本月-电费-账单项目-应付
            dto.setElectricPayableAmount(energyCountStatisticProvider.getSumAmount(dateStr,EnergyMeterType.ELECTRIC.getCode()
                    ,EnergyStatisticType.BILL.getCode(),EnergyCategoryDefault.PAYABLE.getCode()) );

            dto.setElectricBurdenAmount(dto.getElectricPayableAmount().subtract(dto.getElectricReceivableAmount()));

            //费用
            //本月-水费-账单项目-应收
            dto.setWaterReceivableCost(energyCountStatisticProvider.getSumCost(dateStr,EnergyMeterType.WATER.getCode()
                    ,EnergyStatisticType.BILL.getCode(),EnergyCategoryDefault.RECEIVABLE.getCode()) );
            //本月-水费-账单项目-应付
            dto.setWaterPayableCost(energyCountStatisticProvider.getSumCost(dateStr,EnergyMeterType.WATER.getCode()
                    ,EnergyStatisticType.BILL.getCode(),EnergyCategoryDefault.PAYABLE.getCode()) );

            dto.setWaterBurdenCost(dto.getWaterPayableCost().subtract(dto.getWaterReceivableCost()));


            //本月-电费-账单项目-应收
            dto.setElectricReceivableCost(energyCountStatisticProvider.getSumCost(dateStr,EnergyMeterType.ELECTRIC.getCode()
                    ,EnergyStatisticType.BILL.getCode(),EnergyCategoryDefault.RECEIVABLE.getCode()) );
            //本月-电费-账单项目-应付
            dto.setElectricPayableCost(energyCountStatisticProvider.getSumCost(dateStr,EnergyMeterType.ELECTRIC.getCode()
                    ,EnergyStatisticType.BILL.getCode(),EnergyCategoryDefault.PAYABLE.getCode()) );

            dto.setElectricBurdenCost(dto.getElectricPayableCost().subtract(dto.getElectricReceivableCost()));

            //下一个月
            anchorDate.add(Calendar.MONTH, 1);
        }
        return result;

    }

    /* (non-Javadoc)
     * @see com.everhomes.energy.EnergyConsumptionService#getEnergyStatisticByYoy(com.everhomes.rest.energy.EnergyStatCommand)
     */
    @Override
    public List<EnergyCommunityYoyStatDTO> getEnergyStatisticByYoy(EnergyStatCommand cmd) {
        List<EnergyYoyStatistic> stats = this.energyYoyStatisticProvider.listenergyYoyStatistics(UserContext.getCurrentNamespaceId(),
                monthSF.get().format(new Date(cmd.getStatDate())));
        if(null == stats)
            return null;

        Calendar lastYear = Calendar.getInstance();
        lastYear.setTime(new Date(cmd.getStatDate()));
        lastYear.add(Calendar.YEAR,-1);

        List<EnergyCommunityYoyStatDTO> result = new ArrayList<EnergyCommunityYoyStatDTO>();
        EnergyCommunityYoyStatDTO totalDTO = new EnergyCommunityYoyStatDTO();
        totalDTO.setCommunityName("合计");
        EnergyStatByYearDTO totalYearDTO = new EnergyStatByYearDTO();
        totalYearDTO.setDateStr(monthSF.get().format(lastYear.getTime()));
        EnergyStatByYearDTO totalLastYearDTO = new EnergyStatByYearDTO();
        totalLastYearDTO.setDateStr(monthSF.get().format(lastYear.getTime()));
        totalDTO.setStatlist(new ArrayList<EnergyStatByYearDTO>());
        totalDTO.getStatlist().add(totalYearDTO);
        totalDTO.getStatlist().add(totalLastYearDTO);
        for(EnergyYoyStatistic stat : stats){
            EnergyCommunityYoyStatDTO dto = new EnergyCommunityYoyStatDTO();
            result.add(dto);
            dto.setCommunityId(stat.getCommunityId());
            Community com = this.communityProvider.findCommunityById(stat.getCommunityId());
            if (com != null)
                dto.setCommunityName(com.getName());
            dto.setStatlist(new ArrayList<EnergyStatByYearDTO>());
            EnergyStatByYearDTO thisYearDTO = ConvertHelper.convert(stat, EnergyStatByYearDTO.class);
            dto.getStatlist().add(thisYearDTO);
            EnergyStatByYearDTO lastYearDTO = new EnergyStatByYearDTO();
            dto.getStatlist().add(lastYearDTO);
            lastYearDTO.setDateStr(monthSF.get().format(lastYear.getTime()));
            lastYearDTO.setElectricBurdenAmount(stat.getElectricLastBurdenAmount());
            lastYearDTO.setElectricPayableAmount(stat.getElectricLastPayableAmount());
            lastYearDTO.setElectricReceivableAmount(stat.getElectricLastReceivableAmount());
            lastYearDTO.setElectricAverageAmount(stat.getElectricLastAverageAmount());
            lastYearDTO.setWaterBurdenAmount(stat.getWaterLastBurdenAmount());
            lastYearDTO.setWaterPayableAmount(stat.getWaterLastPayableAmount());
            lastYearDTO.setWaterReceivableAmount(stat.getWaterLastReceivableAmount());
            lastYearDTO.setWaterAverageAmount(stat.getWaterLastAverageAmount());

            //合计
            totalYearDTO.setElectricBurdenAmount(totalYearDTO.getElectricBurdenAmount().add(thisYearDTO.getElectricBurdenAmount()));
            totalYearDTO.setElectricPayableAmount(totalYearDTO.getElectricPayableAmount().add(thisYearDTO.getElectricPayableAmount()));
            totalYearDTO.setElectricReceivableAmount(totalYearDTO.getElectricReceivableAmount().add(thisYearDTO.getElectricReceivableAmount()));
            totalYearDTO.setElectricAverageAmount(totalYearDTO.getElectricAverageAmount().add(thisYearDTO.getElectricAverageAmount()));
            totalYearDTO.setWaterBurdenAmount(totalYearDTO.getWaterBurdenAmount().add(thisYearDTO.getWaterBurdenAmount()));
            totalYearDTO.setWaterPayableAmount(totalYearDTO.getWaterPayableAmount().add(thisYearDTO.getWaterPayableAmount()));
            totalYearDTO.setWaterReceivableAmount(totalYearDTO.getWaterReceivableAmount().add(thisYearDTO.getWaterReceivableAmount()));
            totalYearDTO.setWaterAverageAmount(totalYearDTO.getWaterAverageAmount().add(thisYearDTO.getWaterAverageAmount()));

            totalLastYearDTO.setElectricBurdenAmount(totalLastYearDTO.getElectricBurdenAmount().add(totalYearDTO.getElectricBurdenAmount()));
            totalLastYearDTO.setElectricPayableAmount(totalLastYearDTO.getElectricPayableAmount().add(totalYearDTO.getElectricPayableAmount()));
            totalLastYearDTO.setElectricReceivableAmount(totalLastYearDTO.getElectricReceivableAmount().add(totalYearDTO.getElectricReceivableAmount()));
            totalLastYearDTO.setElectricAverageAmount(totalLastYearDTO.getElectricAverageAmount().add(totalYearDTO.getElectricAverageAmount()));
            totalLastYearDTO.setWaterBurdenAmount(totalLastYearDTO.getWaterBurdenAmount().add(totalYearDTO.getWaterBurdenAmount()));
            totalLastYearDTO.setWaterPayableAmount(totalLastYearDTO.getWaterPayableAmount().add(totalYearDTO.getWaterPayableAmount()));
            totalLastYearDTO.setWaterReceivableAmount(totalLastYearDTO.getWaterReceivableAmount().add(totalYearDTO.getWaterReceivableAmount()));
            totalLastYearDTO.setWaterAverageAmount(totalLastYearDTO.getWaterAverageAmount().add(totalYearDTO.getWaterAverageAmount()));
        }
        return result;
    }

    @Override
    public List<EnergyMeterChangeLogDTO> listEnergyMeterChangeLogs(ListEnergyMeterChangeLogsCommand cmd) {
        validate(cmd);
        checkCurrentUserNotInOrg(cmd.getOrganizationId());
        List<EnergyMeterChangeLog> logs = meterChangeLogProvider.listEnergyMeterChangeLogsByMeter(currNamespaceId(), cmd.getMeterId());
        return logs.stream().map(r -> ConvertHelper.convert(r, EnergyMeterChangeLogDTO.class)).collect(Collectors.toList());
    }

    @Override
    public List<EnergyMeterFormulaDTO> listEnergyMeterFormulas(ListEnergyMeterFormulasCommand cmd) {
        validate(cmd);
        checkCurrentUserNotInOrg(cmd.getOwnerId());
        List<EnergyMeterFormula> formulas = meterFormulaProvider.listMeterFormulas(cmd.getOwnerId(), cmd.getOwnerType(),
                                                cmd.getCommunityId(), currNamespaceId(), cmd.getFormulaType());
        return formulas.stream().map(this::toEnergyMeterFormulaDTO).collect(Collectors.toList());
    }

    @Override
    public void deleteEnergyMeterFormula(DeleteEnergyMeterFormulaCommand cmd) {
        validate(cmd);
        checkCurrentUserNotInOrg(cmd.getOrganizationId());
        coordinationProvider.getNamedLock(CoordinationLocks.ENERGY_METER_FORMULA.getCode() + cmd.getFormulaId()).tryEnter(() -> {
            EnergyMeterFormula formula = meterFormulaProvider.findById(currNamespaceId(), cmd.getFormulaId());
            if (formula != null) {
                // 查看当前公式是否被引用, 被引用则无法删除
                EnergyMeterSettingLog settingLog = meterSettingLogProvider.findSettingByFormulaId(currNamespaceId(), formula.getId());
                if (settingLog != null) {
                    LOGGER.info("The formula has been reference, formula id = {}", formula.getId());
                    throw errorWith(SCOPE, ERR_FORMULA_HAS_BEEN_REFERENCE, "The formula has been reference");
                } else {
                    meterFormulaProvider.deleteFormula(formula);
                }
            }
        });
    }

    @Override
    public List<EnergyMeterSettingLogDTO> listEnergyMeterSettingLogs(ListEnergyMeterSettingLogsCommand cmd) {
        validate(cmd);
        checkCurrentUserNotInOrg(cmd.getOrganizationId());

        if(EnergyMeterSettingType.PRICE.equals(EnergyMeterSettingType.fromCode(cmd.getSettingType()))) {
            //log按createTime升序排 放入map时 对同一天的修改 后改的覆盖先前的
            List<EnergyMeterSettingLog> logs = meterSettingLogProvider.listEnergyMeterSettingLogsOrderByCreateTime(currNamespaceId(), cmd.getMeterId(), cmd.getSettingType());
            Map<Long, EnergyMeterPriceDTO> maps = mapEnergyMeterPriceDTO(logs);
            return dealEnergyMeterPriceDTO(maps);
        } else {
            List<EnergyMeterSettingLog> logs = meterSettingLogProvider.listEnergyMeterSettingLogs(currNamespaceId(), cmd.getMeterId(), cmd.getSettingType());
            return logs.stream().map(this::toEnergyMeterSettingLogDTO).collect(Collectors.toList());
        }

    }

    private Map<Long, EnergyMeterPriceDTO> mapEnergyMeterPriceDTO(List<EnergyMeterSettingLog> logs) {
        Map<Long, EnergyMeterPriceDTO> maps = new HashMap<>();
        logs.forEach(log -> {
            EnergyMeterPriceDTO dto = new EnergyMeterPriceDTO();
            //开始时间-创建时间-价钱    结束时间-创建时间-价钱
            dto.setMeterId(log.getMeterId());
            dto.setCreateTime(log.getCreateTime());
            dto.setTime(log.getStartTime());
            dto.setSettingValue(log.getSettingValue());
            if(log.getConfigId() != null && log.getConfigId() > 0L) {
                //价钱梯度表的方案名
                EnergyMeterPriceConfig priceConfig = priceConfigProvider.findById(log.getConfigId());
                if(priceConfig != null)
                    dto.setPlanName(priceConfig.getName());
            }
            if(dto.getTime() != null) {
                maps.put(dto.getTime().getTime(), dto);
            } else {
                maps.put(Long.MAX_VALUE-1, dto);
            }

            EnergyMeterPriceDTO endDTO = new EnergyMeterPriceDTO();
            endDTO.setMeterId(dto.getMeterId());
            endDTO.setCreateTime(dto.getCreateTime());
            endDTO.setSettingValue(dto.getSettingValue());
            endDTO.setPlanName(dto.getPlanName());
            endDTO.setTime(log.getEndTime());
            if(endDTO.getTime() != null) {
                maps.put(endDTO.getTime().getTime(), endDTO);
            } else {
                maps.put(Long.MAX_VALUE-1, endDTO);
            }
        });
        return maps;
    }

    private List<EnergyMeterSettingLogDTO> dealEnergyMeterPriceDTO(Map<Long, EnergyMeterPriceDTO> maps) {
        List<EnergyMeterSettingLogDTO> dtos = new ArrayList<EnergyMeterSettingLogDTO>();
        Object[] key_arr = maps.keySet().toArray();
        if(key_arr.length > 0) {
            Arrays.sort(key_arr);
            if(key_arr.length >= 2) {
                for(int i = 0; i < key_arr.length-2; i++) {
                    EnergyMeterPriceDTO value = maps.get(key_arr[i]);
                    EnergyMeterSettingLogDTO dto = new EnergyMeterSettingLogDTO();
                    dto.setMeterId(value.getMeterId());
                    dto.setSettingValue(value.getSettingValue());
                    dto.setPriceConfigName(value.getPlanName());
//                    dto.setFormulaName(value.getPlanName());
                    dto.setStartTime(value.getTime());
                    EnergyMeterPriceDTO nextValue = maps.get(key_arr[i+1]);
                    dto.setEndTime(nextValue.getTime());

                    dtos.add(dto);
                }

                EnergyMeterPriceDTO value = maps.get(key_arr[key_arr.length-2]);
                EnergyMeterSettingLogDTO dto = new EnergyMeterSettingLogDTO();
                dto.setMeterId(value.getMeterId());
                dto.setStartTime(value.getTime());
                EnergyMeterPriceDTO nextValue = maps.get(key_arr[key_arr.length-1]);
                dto.setEndTime(nextValue.getTime());

                dto.setSettingValue(nextValue.getSettingValue());
                dto.setPriceConfigName(value.getPlanName());

                dtos.add(dto);
            } else if(key_arr.length == 1) {
                EnergyMeterPriceDTO value = maps.get(key_arr[key_arr.length-1]);
                EnergyMeterSettingLogDTO dto = new EnergyMeterSettingLogDTO();
                dto.setMeterId(value.getMeterId());
                dto.setStartTime(value.getTime());
                dto.setSettingValue(value.getSettingValue());
                dto.setPriceConfigName(value.getPlanName());

                dtos.add(dto);
            }

        }
        return dtos;
    }

    private EnergyMeterSettingLogDTO toEnergyMeterSettingLogDTO(EnergyMeterSettingLog log) {
        EnergyMeterSettingLogDTO dto = ConvertHelper.convert(log, EnergyMeterSettingLogDTO.class);
        if (log.getFormulaId() != null) {
            EnergyMeterFormula formula = meterFormulaProvider.findById(currNamespaceId(), log.getFormulaId());
            dto.setFormulaName(formula.getName());
        }
        if(log.getConfigId() != null && log.getConfigId() != 0L) {
            EnergyMeterPriceConfig priceConfig = priceConfigProvider.findById(log.getConfigId());
            if(priceConfig != null) {
                dto.setPriceConfigName(priceConfig.getName());
            }
        }
        return dto;
    }

    private EnergyMeterFormulaDTO toEnergyMeterFormulaDTO(EnergyMeterFormula formula) {
        EnergyMeterFormulaDTO dto = ConvertHelper.convert(formula, EnergyMeterFormulaDTO.class);
        dto.setExpression(formula.getDisplayExpression());
        return dto;
    }

    @Override
    public List<EnergyMeterDefaultSettingDTO> listEnergyDefaultSettings(ListEnergyDefaultSettingsCommand cmd) {
        validate(cmd);
        checkCurrentUserNotInOrg(cmd.getOwnerId());
        List<EnergyMeterDefaultSetting> settings = new ArrayList<>();
        if (cmd.getMeterType() != null) {
            settings = defaultSettingProvider.listDefaultSetting(cmd.getOwnerId(), cmd.getOwnerType(),
                            cmd.getCommunityId(),currNamespaceId(), cmd.getMeterType());
        } else {
            List<EnergyMeterDefaultSetting> waterSettings = defaultSettingProvider.listDefaultSetting(cmd.getOwnerId(), cmd.getOwnerType(),
                                                                cmd.getCommunityId(),currNamespaceId(), EnergyMeterType.WATER.getCode());
            List<EnergyMeterDefaultSetting> elecSettings = defaultSettingProvider.listDefaultSetting(cmd.getOwnerId(), cmd.getOwnerType(),
                                                                cmd.getCommunityId(),currNamespaceId(), EnergyMeterType.ELECTRIC.getCode());
            settings.addAll(waterSettings);
            settings.addAll(elecSettings);
        }
        return settings.stream().map(this::toEnergyMeterDefaultSettingDTO).collect(Collectors.toList());
    }

    private EnergyMeterDefaultSettingDTO toEnergyMeterDefaultSettingDTO(EnergyMeterDefaultSetting setting) {
        EnergyMeterDefaultSettingDTO dto = ConvertHelper.convert(setting, EnergyMeterDefaultSettingDTO.class);
        dto.setSettingStatus(setting.getStatus());
        if (setting.getFormulaId() != null) {
            EnergyMeterFormula formula = meterFormulaProvider.findById(currNamespaceId(), setting.getFormulaId());
            if (formula != null) {
                dto.setFormulaName(formula.getName());
                dto.setFormulaType(formula.getFormulaType());
            }
        }
        if(EnergyMeterSettingType.PRICE.equals(EnergyMeterSettingType.fromCode(setting.getSettingType()))) {
            if(PriceCalculationType.BLOCK_TARIFF.equals(PriceCalculationType.fromCode(setting.getCalculationType()))) {
                EnergyMeterPriceConfig priceConfig = priceConfigProvider.findById(setting.getConfigId(), setting.getOwnerId(),
                        setting.getOwnerType(), setting.getCommunityId(), currNamespaceId());
                dto.setPriceConfigName(priceConfig.getName());
            }

        }
        // 表的类型
        String meterType = localeStringService.getLocalizedString(EnergyLocalStringCode.SCOPE_METER_TYPE, String.valueOf(setting.getMeterType()), currLocale(), "");
        dto.setMeterType(meterType);
        return dto;
    }

    @Override
    public List<EnergyFormulaVariableDTO> listEnergyFormulaVariables() {
        List<EnergyMeterFormulaVariable> variables = meterFormulaVariableProvider.listMeterFormulaVariables();
        return variables.stream().map(r -> ConvertHelper.convert(r, EnergyFormulaVariableDTO.class)).collect(Collectors.toList());
    }

    @Override
    public List<EnergyMeterCategoryDTO> listEnergyMeterCategories(ListEnergyMeterCategoriesCommand cmd) {
        validate(cmd);
        checkCurrentUserNotInOrg(cmd.getOwnerId());
        List<EnergyMeterCategory> categoryList = meterCategoryProvider.listMeterCategories(currNamespaceId(), cmd.getCategoryType(),
                                                        cmd.getOwnerId(), cmd.getOwnerType(), cmd.getCommunityId());
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

    private void invalidParameterException(String name, Object param) {
        LOGGER.error("Invalid parameter {} [ {} ].", name, param);
        throw errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                "Invalid parameter %s [ %s ].", name, param);
    }

    private EnergyMeter findMeterById(Long id) {
        EnergyMeter meter = meterProvider.findById(currNamespaceId(), id);
        if (meter != null) {
            return meter;
        }
        LOGGER.error("EnergyMeter not exist, id = {}", id);
        throw errorWith(SCOPE, ERR_METER_NOT_EXIST, "The meter is not exist id = %s", id);
    }

    private Timestamp getDayBegin(Calendar cal) {
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 001);
        return new Timestamp(cal.getTimeInMillis());
    }

    private void checkCurrentUserNotInOrg(Long orgId) {
        if (orgId == null) {
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
        }
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

    /**
     * 每天早上1点10分刷前一天的读表
     * */
    @Scheduled(cron = "0 10 1 * * ?")
    public void calculateEnergyDayStat(){
        coordinationProvider.getNamedLock(CoordinationLocks.ENERGY_DAY_STAT_SCHEDULE.getCode()).tryEnter(() -> {
            try {
                LOGGER.info("calculate energy day stat start...");
                //刷今天的
                calculateEnergyDayStatByDate(DateHelper.currentGMTTime());
                LOGGER.info("calculate energy day stat end...");
            } catch (Exception e) {
                LOGGER.error("calculate energy day stat error...", e);
                sendErrorMessage(e);
                e.printStackTrace();
            }
        });
    }

    private void sendErrorMessage(Exception e) {
        String xiongying = "ying.xiong@zuolin.com";
        String handlerName = MailHandler.MAIL_RESOLVER_PREFIX + MailHandler.HANDLER_JSMTP;
        MailHandler handler = PlatformContext.getComponent(handlerName);
        String account = configurationProvider.getValue(0,"mail.smtp.account", "zuolin@zuolin.com");

        try (   ByteArrayOutputStream out = new ByteArrayOutputStream();
                PrintStream stream = new PrintStream(out))
        {
            e.printStackTrace(stream);
            String message = out.toString("UTF-8");
            handler.sendMail(0, account, xiongying, "calculateEnergyDayStat error", message);
            // out.reset();
            e.getCause().printStackTrace(stream);
            message = out.toString("UTF-8");
            handler.sendMail(0, account, xiongying, "calculateEnergyDayStat error cause", message);

        } catch (Exception ignored) {
            //
        }
    }

    @Override
    public void calculateEnergyDayStatByDate(java.util.Date date){
        // 查出所有的表
        List<EnergyMeter> meters = meterProvider.listEnergyMeters();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        Timestamp todayBegin = getDayBegin(cal);
        cal.add(Calendar.DAY_OF_MONTH, -1);
        Timestamp yesterdayBegin = getDayBegin(cal);
        cal.add(Calendar.DAY_OF_MONTH, -1);
        // Timestamp dayBeforeYestBegin = getDayBegin(cal);
        for(EnergyMeter meter : meters){
            try {

                EnergyDateStatistic dayStat = ConvertHelper.convert(meter, EnergyDateStatistic.class);
                // 前天的最后的读数
                EnergyMeterReadingLog dayBeforeYestLastLog = meterReadingLogProvider.getLastMeterReadingLogByDate(meter.getId(),null,yesterdayBegin);
                // 昨天的最后的读数
                EnergyMeterReadingLog yesterdayLastLog = meterReadingLogProvider.getLastMeterReadingLogByDate(meter.getId(),null,todayBegin);

                // 读表用量差
                BigDecimal amount = new BigDecimal("0");

                // 默认初始值为表的初始值，如果有昨天之前和前天之前的读表记录则置换初始值
                BigDecimal readingAnchor = meter.getStartReading();
                // 统计当天的开始读数
                BigDecimal dayLastReading = meter.getStartReading();
                // 统计当天的最后读数
                BigDecimal dayCurrReading = meter.getStartReading();
                if(null != dayBeforeYestLastLog){
                    readingAnchor = dayBeforeYestLastLog.getReading();
                    dayLastReading =  dayBeforeYestLastLog.getReading();
                }

                if(null != yesterdayLastLog){
                    dayCurrReading = yesterdayLastLog.getReading();
                }

                // 拿出单个表统计当天的所有的读表记录
                List<EnergyMeterReadingLog> meterReadingLogs = meterReadingLogProvider.listMeterReadingLogByDate(meter.getId(),yesterdayBegin,todayBegin);

                // 重置flag
                Byte resetFlag = TrueOrFalseFlag.FALSE.getCode();
                // 换表flag
                Byte changeFlag = TrueOrFalseFlag.FALSE.getCode();

                //查看是否有换表,是否有归零
                if(meterReadingLogs != null) {
                    for(EnergyMeterReadingLog log : meterReadingLogs) {
                        // 有归零 量程设置为最大值-锚点,锚点设置为0
                        if(TrueOrFalseFlag.fromCode(log.getResetMeterFlag()) == TrueOrFalseFlag.TRUE) {
                            resetFlag = TrueOrFalseFlag.TRUE.getCode();
                            amount = amount.add(meter.getMaxReading().subtract(readingAnchor));
                            readingAnchor = new BigDecimal(0);
                            dayStat.setResetMeterFlag(resetFlag);
                        }
                        // 有换表 量程加上旧表读数-锚点,锚点重置为新读数
                        if(TrueOrFalseFlag.fromCode(log.getChangeMeterFlag()) == TrueOrFalseFlag.TRUE) {
                            changeFlag = TrueOrFalseFlag.TRUE.getCode();
                            EnergyMeterChangeLog changeLog = this.meterChangeLogProvider.getEnergyMeterChangeLogByLogId(log.getId());
                            amount = amount.add(changeLog.getOldReading().subtract(readingAnchor));
                            readingAnchor = changeLog.getNewReading();
                            dayStat.setChangeMeterFlag(changeFlag);
                        }
                    }
                }

                //计算当天走了多少字 量程+昨天最后一次读数-锚点
                amount = amount.add(dayCurrReading.subtract(readingAnchor));

                //获取公式,计算当天的费用
                EnergyMeterSettingLog priceSetting  = meterSettingLogProvider
                        .findCurrentSettingByMeterId(meter.getNamespaceId(),meter.getId(),EnergyMeterSettingType.PRICE,yesterdayBegin);
                EnergyMeterSettingLog rateSetting   = meterSettingLogProvider
                        .findCurrentSettingByMeterId(meter.getNamespaceId(),meter.getId(),EnergyMeterSettingType.RATE ,yesterdayBegin);
                EnergyMeterSettingLog amountSetting = meterSettingLogProvider
                        .findCurrentSettingByMeterId(meter.getNamespaceId(),meter.getId(),EnergyMeterSettingType.AMOUNT_FORMULA ,yesterdayBegin);
                EnergyMeterSettingLog costSetting   = meterSettingLogProvider
                        .findCurrentSettingByMeterId(meter.getNamespaceId(),meter.getId(),EnergyMeterSettingType.COST_FORMULA ,yesterdayBegin);

                Stream<EnergyMeterSettingLog> settings = Stream.of(amountSetting, rateSetting, priceSetting, costSetting);
                if (settings.anyMatch(Objects::isNull)) {
                    LOGGER.error("not found energy meter settings, meterId={}, priceSetting={}, rateSetting={}, amountSetting={}, costSetting={}",
                            meter.getId(), priceSetting, rateSetting, amountSetting, costSetting);
                    continue;
                }
                String amountFormula = meterFormulaProvider.findById(amountSetting.getNamespaceId(), amountSetting.getFormulaId()).getExpression();
                String costFormula = meterFormulaProvider.findById(costSetting.getNamespaceId(), costSetting.getFormulaId()).getExpression();

                ScriptEngineManager manager = new ScriptEngineManager();
                ScriptEngine engine = manager.getEngineByName("js");

                engine.put(MeterFormulaVariable.AMOUNT.getCode(), amount);
                //分段计费后 settingValue可能为null
//                engine.put(MeterFormulaVariable.PRICE.getCode(), priceSetting.getSettingValue());
                engine.put(MeterFormulaVariable.TIMES.getCode(), rateSetting.getSettingValue());

                BigDecimal realAmount = new BigDecimal(0);
                BigDecimal realCost = new BigDecimal(0);

//                try {
//                	double ra = Double.valueOf(engine.eval(amountFormula).toString());
//                    realAmount = BigDecimal.valueOf(ra);
//                    engine.put(MeterFormulaVariable.REAL_AMOUNT.getCode(), realAmount);
////                    realCost = BigDecimal.valueOf((double) engine.eval(costFormula));
//                } catch (ScriptException e) {
//                    String paramsStr = "{AMOUNT:" + amount +
//                            ", TIMES:" + rateSetting.getSettingValue() +
//                            "}";
//                    LOGGER.error("evaluate formula error, amountFormula={}, params={}", amountFormula, paramsStr);
//                    e.printStackTrace();
//                    throw errorWith(SCOPE, EnergyConsumptionServiceErrorCode.ERR_METER_FORMULA_ERROR, "evaluate formula error", e);
//                }

                if(PriceCalculationType.STANDING_CHARGE_TARIFF.equals(
                        PriceCalculationType.fromCode(priceSetting.getCalculationType()))) {
                    engine.put(MeterFormulaVariable.REAL_AMOUNT.getCode(), amount);
                    realCost = calculateStandingChargeTariff(engine, priceSetting, costFormula);
                }

                if(PriceCalculationType.BLOCK_TARIFF.equals(
                        PriceCalculationType.fromCode(priceSetting.getCalculationType()))) {
                    realCost = calculateBlockTariff(manager,priceSetting,realAmount, costFormula);
                }

                //删除昨天的记录（手工刷的时候）
                energyDateStatisticProvider.deleteEnergyDateStatisticByDate(meter.getId(), new Date(yesterdayBegin.getTime()));

                //写数据库
                dayStat.setMeterId(meter.getId());
                dayStat.setStatDate(new Date(yesterdayBegin.getTime()));
                dayStat.setMeterName(meter.getName());
                dayStat.setMeterBill(meterCategoryProvider.findById(meter.getNamespaceId(), meter.getBillCategoryId()).getName());
                dayStat.setMeterService(meterCategoryProvider.findById(meter.getNamespaceId(), meter.getServiceCategoryId()).getName());
                dayStat.setMeterRate(rateSetting.getSettingValue());
                dayStat.setMeterPrice(priceSetting.getSettingValue());
                dayStat.setLastReading(dayLastReading);
                dayStat.setCurrentReading(dayCurrReading);
                dayStat.setCurrentAmount(realAmount);
                dayStat.setCurrentCost(realCost);
                dayStat.setResetMeterFlag(resetFlag);
                dayStat.setChangeMeterFlag(changeFlag);
                dayStat.setStatus(EnergyCommonStatus.ACTIVE.getCode());
			    // dayStat.setCreatorUid(UserContext.current().getUser().getId());
                dayStat.setCreateTime(new Timestamp(DateHelper.currentGMTTime().getTime()));
                energyDateStatisticProvider.createEnergyDateStatistic(dayStat);
            } catch (Exception e) {
                throw new RuntimeException(String.format("meter id = %s, meterName = %s", meter.getId(), meter.getName()), e);
            }
        }
    }

    private BigDecimal calculateStandingChargeTariff(ScriptEngine engine, EnergyMeterSettingLog priceSetting, String costFormula) {
        engine.put(MeterFormulaVariable.PRICE.getCode(), priceSetting.getSettingValue());
        BigDecimal realCost = new BigDecimal(0);
        try {
            realCost = BigDecimal.valueOf(Double.valueOf(engine.eval(costFormula).toString()));
        } catch (ScriptException e) {
            String paramsStr = "{PRICE:" + priceSetting.getSettingValue() +
                    ", AMOUNT:" + engine.get(MeterFormulaVariable.AMOUNT.getCode()) +
                    ", TIMES:" + engine.get(MeterFormulaVariable.TIMES.getCode()) +
                    "}";
            LOGGER.error("evaluate formula error, costFormula={}, params={}", costFormula, paramsStr);
            e.printStackTrace();
            throw errorWith(SCOPE, EnergyConsumptionServiceErrorCode.ERR_METER_FORMULA_ERROR, "evaluate formula error", e);
        }

        return realCost;
    }

    private BigDecimal calculateBlockTariff(ScriptEngineManager manager, EnergyMeterSettingLog priceSetting, BigDecimal realAmount, String costFormula) {
        EnergyMeterPriceConfig priceConfig = priceConfigProvider.findById(priceSetting.getConfigId());
        EnergyMeterPriceConfigDTO priceConfigDTO = toEnergyMeterPriceConfigDTO(priceConfig);
        BigDecimal totalCost = new BigDecimal(0);
        if(priceConfigDTO != null && priceConfigDTO.getExpression() != null) {
            ScriptEngine engine = manager.getEngineByName("javascript");
            List<EnergyMeterRangePriceDTO> rangePriceDTOs = priceConfigDTO.getExpression().getRangePrice();
            if(rangePriceDTOs != null && rangePriceDTOs.size() > 0) {
                BigDecimal zero = new BigDecimal(0);

                for(EnergyMeterRangePriceDTO rangePriceDTO : rangePriceDTOs) {

                    BigDecimal minValue = StringUtils.isEmpty(rangePriceDTO.getMinValue()) ? new BigDecimal(-1) : new BigDecimal(rangePriceDTO.getMinValue());
                    BigDecimal maxValue = StringUtils.isEmpty(rangePriceDTO.getMaxValue()) ? new BigDecimal(-1) : new BigDecimal(rangePriceDTO.getMaxValue());
                    RangeBoundaryType lowerBoundary = RangeBoundaryType.fromCode(rangePriceDTO.getLowerBoundary());
                    RangeBoundaryType upperBoundary = RangeBoundaryType.fromCode(rangePriceDTO.getUpperBoundary());
                    //在区间内
                    if((minValue.compareTo(zero) < 0 || calculateLowerBoundary(lowerBoundary, minValue, realAmount))
                            && (maxValue.compareTo(zero) < 0  || calculateUpperBoundary(upperBoundary, maxValue, realAmount))) {
                        engine.put(MeterFormulaVariable.PRICE.getCode(),rangePriceDTO.getPrice());
                        engine.put(MeterFormulaVariable.REAL_AMOUNT.getCode(), realAmount.subtract(minValue));
                        totalCost.add(calculateBlockTariffByCostFormula(engine, costFormula));

                    }
                    //比该区间最大值大
                    if(maxValue.compareTo(zero) >= 0 && greaterThanMax(upperBoundary, maxValue, realAmount)) {
                        if(minValue.compareTo(zero) < 0) {
                            engine.put(MeterFormulaVariable.PRICE.getCode(),rangePriceDTO.getPrice());
                            engine.put(MeterFormulaVariable.REAL_AMOUNT.getCode(), maxValue.subtract(zero));
                            totalCost.add(calculateBlockTariffByCostFormula(engine, costFormula));
                        } else {
                            engine.put(MeterFormulaVariable.PRICE.getCode(),rangePriceDTO.getPrice());
                            engine.put(MeterFormulaVariable.REAL_AMOUNT.getCode(), maxValue.subtract(minValue));
                            totalCost.add(calculateBlockTariffByCostFormula(engine, costFormula));

                        }

                    }
                    //比该区间最小值小则不计算
                }
            }
        }
        return totalCost;
    }

    private BigDecimal calculateBlockTariffByCostFormula(ScriptEngine engine, String costFormula) {
        BigDecimal cost = new BigDecimal(0);
        try {
            cost.add(BigDecimal.valueOf(Double.valueOf(engine.eval(costFormula).toString())));
        } catch (ScriptException e) {
            String paramsStr = "{PRICE:" + engine.get(MeterFormulaVariable.REAL_AMOUNT.getCode()) +
                    ", REALAMOUNT:" + engine.get(MeterFormulaVariable.AMOUNT.getCode()) +
                    "}";
            LOGGER.error("evaluate formula error, costFormula={}, params={}", costFormula, paramsStr);
            e.printStackTrace();
            throw errorWith(SCOPE, EnergyConsumptionServiceErrorCode.ERR_METER_FORMULA_ERROR, "evaluate formula error", e);
        }
        return cost;
    }

    private Boolean calculateLowerBoundary(RangeBoundaryType lowerBoundary, BigDecimal minValue, BigDecimal realAmount) {
        Boolean rangeFlag = false;
        switch (lowerBoundary) {
            case LESS:
                rangeFlag = (minValue.compareTo(realAmount) < 0);
                break;

            case LESS_AND_EQUAL:
                rangeFlag = (minValue.compareTo(realAmount) <= 0);
                break;
        }

        return rangeFlag;
    }

    private Boolean calculateUpperBoundary(RangeBoundaryType upperBoundary, BigDecimal maxValue, BigDecimal realAmount) {
        Boolean rangeFlag = false;
        switch (upperBoundary) {
            case GREATER:
                rangeFlag = (maxValue.compareTo(realAmount) > 0);
                break;

            case GREATER_AND_EQUAL:
                rangeFlag = (maxValue.compareTo(realAmount) >= 0);
                break;
        }

        return rangeFlag;
    }

    private Boolean greaterThanMax(RangeBoundaryType upperBoundary, BigDecimal maxValue, BigDecimal realAmount) {
        Boolean flag = false;
        switch (upperBoundary) {
            case GREATER:
                flag = (maxValue.compareTo(realAmount) <= 0);
                break;

            case GREATER_AND_EQUAL:
                flag = (maxValue.compareTo(realAmount) < 0);
                break;
        }

        return flag;
    }

    /**
     * 每月1日  早上3点10分刷前一个月的读表
     * */
    @Scheduled(cron = "0 10 3 1 * ?")
    public void calculateEnergyMonthStat() {
        coordinationProvider.getNamedLock(CoordinationLocks.ENERGY_MONTH_STAT_SCHEDULE.getCode()).tryEnter(() -> {
            try {
                LOGGER.info("calculate energy month stat start...");
                calculateEnergyMonthStatByDate(DateHelper.currentGMTTime());
                LOGGER.info("calculate energy month stat end...");
            } catch (Exception e) {
                LOGGER.error("calculate energy month stat error...", e);
                sendErrorMessage(e);
                e.printStackTrace();
            }
        });
    }

    @Override
    public void calculateEnergyMonthStatByDate(java.util.Date date){
        //查出所有的表
        List<EnergyMeter> meters = meterProvider.listEnergyMeters();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        //减一个月取月末和月初
        cal.add(Calendar.MONTH, -1);
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        Timestamp monthEnd = getDayBegin(cal);
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
        Timestamp monthBegin = getDayBegin(cal);
        cal.add(Calendar.YEAR, -1);
        Timestamp lastYear = getDayBegin(cal);
        BigDecimal zero = new BigDecimal(0);
        ScriptEngineManager manager = new ScriptEngineManager();
        for(EnergyMeter meter : meters){

            //取月初的上次度数和月末的当前读数
            EnergyDateStatistic monthEndStat = energyDateStatisticProvider.getEnergyDateStatisticByStatDate(meter.getId(),new Date(monthEnd.getTime()));
            EnergyMonthStatistic monthStat = ConvertHelper.convert(monthEndStat, EnergyMonthStatistic.class);
            if (null == monthEndStat) {
                LOGGER.error("energy meter monthEndStat is null, meterId={}, month={}", meter.getId(), monthEnd.toLocalDateTime());
                continue;
            }
            monthStat.setMeterId(meter.getId());
            monthStat.setDateStr(monthSF.get().format(monthBegin));
            EnergyDateStatistic monthBeginStat = energyDateStatisticProvider.getEnergyDateStatisticByStatDate(meter.getId(),new Date(monthBegin.getTime()));

            if (null == monthBeginStat) {
                LOGGER.error("energy meter monthBeginStat is null, meterId={}, month={}", meter.getId(), monthBegin.toLocalDateTime());
                continue;
            }
            monthStat.setLastReading(monthBeginStat.getLastReading());
            //统计该表sum 用量和费用
            BigDecimal currentAmount = energyDateStatisticProvider.getSumAmountBetweenDate(meter.getId(),new Date(monthBegin.getTime()),new Date(monthEnd.getTime()));
            monthStat.setCurrentAmount(currentAmount);
            //收费按最新规则算
            EnergyMeterSettingLog priceSetting  = meterSettingLogProvider
                    .findCurrentSettingByMeterId(meter.getNamespaceId(),meter.getId(),EnergyMeterSettingType.PRICE,monthEnd);
            EnergyMeterSettingLog costSetting   = meterSettingLogProvider
                    .findCurrentSettingByMeterId(meter.getNamespaceId(),meter.getId(),EnergyMeterSettingType.COST_FORMULA ,monthEnd);

            String costFormula = meterFormulaProvider.findById(costSetting.getNamespaceId(), costSetting.getFormulaId()).getExpression();

            BigDecimal realCost = new BigDecimal(0);
            
            if(PriceCalculationType.STANDING_CHARGE_TARIFF.equals(
                    PriceCalculationType.fromCode(priceSetting.getCalculationType()))) {
                ScriptEngine engine = manager.getEngineByName("js");
                engine.put(MeterFormulaVariable.AMOUNT.getCode(), currentAmount);
                //由于currentAmount其实是realAmount  已经算了一遍times，所以此处times赋值为1 by xiongying20170401
                engine.put(MeterFormulaVariable.TIMES.getCode(), 1);
                engine.put(MeterFormulaVariable.REAL_AMOUNT.getCode(), currentAmount);
                realCost = calculateStandingChargeTariff(engine, priceSetting, costFormula);
            }

            if(PriceCalculationType.BLOCK_TARIFF.equals(
                    PriceCalculationType.fromCode(priceSetting.getCalculationType()))) {
                realCost = calculateBlockTariff(manager,priceSetting,currentAmount, costFormula);
            }
            monthStat.setCurrentCost(realCost);

            //delete
            energyMonthStatisticProvider.deleteEnergyMonthStatisticByDate(meter.getId(), monthSF.get().format(monthBegin));
            //写数据库

            energyMonthStatisticProvider.createEnergyMonthStatistic(monthStat);
        }
        //总量记录表 -用sql汇总
        List<EnergyCountStatistic> countStats = energyMonthStatisticProvider.listEnergyCountStatistic(monthSF.get().format(monthBegin));
        if(null != countStats){
            List<Long> comIds = new ArrayList<Long>();
            for(EnergyCountStatistic cs : countStats){

                cs.setStatus(EnergyCommonStatus.ACTIVE.getCode());
//				cs.setCreatorUid(UserContext.current().getUser().getId());
                cs.setCreateTime(new Timestamp(DateHelper.currentGMTTime()
                        .getTime()));
                energyCountStatisticProvider.createEnergyCountStatistic(cs);
                comIds.add(cs.getCommunityId());
            }
            //各项目月水电能耗情况-同比
            for(Long comId : comIds){
                Community com = communityProvider.findCommunityById(comId);
                EnergyYoyStatistic yoy = new EnergyYoyStatistic();
                yoy.setNamespaceId(com.getNamespaceId());
                yoy.setCommunityId(com.getId());
                yoy.setAreaSize(com.getAreaSize());
                yoy.setDateStr(monthSF.get().format(monthBegin));
                // 通过sql计算每一个值
                //本月-水费-账单项目-应收
                yoy.setWaterReceivableAmount(energyCountStatisticProvider.getSumAmount(monthSF.get().format(monthBegin),EnergyMeterType.WATER.getCode()
                        ,EnergyStatisticType.BILL.getCode(),EnergyCategoryDefault.RECEIVABLE.getCode()) );
                //本月-水费-账单项目-应付
                yoy.setWaterPayableAmount(energyCountStatisticProvider.getSumAmount(monthSF.get().format(monthBegin),EnergyMeterType.WATER.getCode()
                        ,EnergyStatisticType.BILL.getCode(),EnergyCategoryDefault.PAYABLE.getCode()) );

                yoy.setWaterBurdenAmount(yoy.getWaterPayableAmount().subtract(yoy.getWaterReceivableAmount()));
                yoy.setWaterAverageAmount(com.getAreaSize() == null ? new BigDecimal(0):yoy.getWaterBurdenAmount().divide(new BigDecimal(com.getAreaSize())
                        , 3, RoundingMode.HALF_UP));


                //本月-电费-账单项目-应收
                yoy.setElectricReceivableAmount(energyCountStatisticProvider.getSumAmount(monthSF.get().format(monthBegin),EnergyMeterType.ELECTRIC.getCode()
                        ,EnergyStatisticType.BILL.getCode(),EnergyCategoryDefault.RECEIVABLE.getCode()) );
                //本月-电费-账单项目-应付
                yoy.setElectricPayableAmount(energyCountStatisticProvider.getSumAmount(monthSF.get().format(monthBegin),EnergyMeterType.ELECTRIC.getCode()
                        ,EnergyStatisticType.BILL.getCode(),EnergyCategoryDefault.PAYABLE.getCode()) );

                yoy.setElectricBurdenAmount(yoy.getElectricPayableAmount().subtract(yoy.getElectricReceivableAmount()));
                yoy.setElectricAverageAmount(com.getAreaSize() == null ? new BigDecimal(0):yoy.getElectricBurdenAmount().divide(new BigDecimal(com.getAreaSize())
                        , 3, RoundingMode.HALF_UP));



                //去年本月-水费-账单项目-应收
                yoy.setWaterLastReceivableAmount(energyCountStatisticProvider.getSumAmount(monthSF.get().format(lastYear),EnergyMeterType.WATER.getCode()
                        ,EnergyStatisticType.BILL.getCode(),EnergyCategoryDefault.RECEIVABLE.getCode()) );
                //去年本月-水费-账单项目-应付
                yoy.setWaterLastPayableAmount(energyCountStatisticProvider.getSumAmount(monthSF.get().format(lastYear),EnergyMeterType.WATER.getCode()
                        ,EnergyStatisticType.BILL.getCode(),EnergyCategoryDefault.PAYABLE.getCode()) );

                yoy.setWaterLastBurdenAmount(yoy.getWaterLastPayableAmount().subtract(yoy.getWaterLastReceivableAmount()));
                yoy.setWaterLastAverageAmount(com.getAreaSize() == null ? new BigDecimal(0):yoy.getWaterLastBurdenAmount().divide(new BigDecimal(com.getAreaSize())
                        , 3, RoundingMode.HALF_UP));


                //去年本月-电费-账单项目-应收
                yoy.setElectricLastReceivableAmount(energyCountStatisticProvider.getSumAmount(monthSF.get().format(lastYear),EnergyMeterType.ELECTRIC.getCode()
                        ,EnergyStatisticType.BILL.getCode(),EnergyCategoryDefault.RECEIVABLE.getCode()) );
                //去年本月-电费-账单项目-应付
                yoy.setElectricLastPayableAmount(energyCountStatisticProvider.getSumAmount(monthSF.get().format(lastYear),EnergyMeterType.ELECTRIC.getCode()
                        ,EnergyStatisticType.BILL.getCode(),EnergyCategoryDefault.PAYABLE.getCode()) );

                yoy.setElectricLastBurdenAmount(yoy.getElectricLastPayableAmount().subtract(yoy.getElectricLastReceivableAmount()));
                yoy.setElectricLastAverageAmount(com.getAreaSize() == null ? new BigDecimal(0):yoy.getElectricLastBurdenAmount().divide(new BigDecimal(com.getAreaSize())
                        , 3, RoundingMode.HALF_UP));


                yoy.setStatus(EnergyCommonStatus.ACTIVE.getCode());
//				yoy.setCreatorUid(UserContext.current().getUser().getId());
                yoy.setCreateTime(new Timestamp(DateHelper.currentGMTTime()
                        .getTime()));
                energyYoyStatisticProvider.deleteEnergyYoyStatistic(yoy.getCommunityId(), yoy.getDateStr());
                energyYoyStatisticProvider.createEnergyYoyStatistic(yoy);
            }

        }

    }

    @Override
    public void syncEnergyMeterReadingLogIndex() {
        readingLogSearcher.syncFromDb();
    }

    @Override
    public void syncEnergyMeterIndex() {
        meterSearcher.syncFromDb();
    }

    @Override
    public EnergyMeterPriceConfigDTO createEnergyMeterPriceConfig(CreateEnergyMeterPriceConfigCommand cmd) {
        EnergyMeterPriceConfig priceConfig = ConvertHelper.convert(cmd, EnergyMeterPriceConfig.class);
        priceConfig.setStatus(EnergyCommonStatus.ACTIVE.getCode());
        priceConfig.setNamespaceId(currNamespaceId());
        priceConfigProvider.createEnergyMeterPriceConfig(priceConfig);
        return toEnergyMeterPriceConfigDTO(priceConfig);
    }

    @Override
    public EnergyMeterPriceConfigDTO updateEnergyMeterPriceConfig(UpdateEnergyMeterPriceConfigCommand cmd) {
        //暂时没有update
        return null;
    }

    @Override
    public EnergyMeterPriceConfigDTO getEnergyMeterPriceConfig(GetEnergyMeterPriceConfigCommand cmd) {
        EnergyMeterPriceConfig priceConfig = priceConfigProvider.findById(cmd.getId(), cmd.getOwnerId(), cmd.getOwnerType(),
                cmd.getCommunityId(),currNamespaceId());

        return toEnergyMeterPriceConfigDTO(priceConfig);
    }

    @Override
    public List<EnergyMeterPriceConfigDTO> listEnergyMeterPriceConfig(ListEnergyMeterPriceConfigCommand cmd) {
        List<EnergyMeterPriceConfig> priceConfig = priceConfigProvider.listPriceConfig(cmd.getOwnerId(), cmd.getOwnerType(),
                cmd.getCommunityId(), currNamespaceId());
        return priceConfig.stream().map(this::toEnergyMeterPriceConfigDTO).collect(Collectors.toList());
    }

    @Override
    public void deleteEnergyMeterPriceConfig(DelelteEnergyMeterPriceConfigCommand cmd) {
        validate(cmd);
        checkCurrentUserNotInOrg(cmd.getOwnerId());
        coordinationProvider.getNamedLock(CoordinationLocks.ENERGY_METER_PRICE_CONFIG.getCode() + cmd.getId()).tryEnter(() -> {
            EnergyMeterPriceConfig priceConfig = priceConfigProvider.findById(cmd.getId(), cmd.getOwnerId(), cmd.getOwnerType(),
                    cmd.getCommunityId(),currNamespaceId());

            if(priceConfig != null) {
                EnergyMeterSettingLog settingLog = meterSettingLogProvider.findSettingByPriceConfigId(currNamespaceId(), priceConfig.getId());
                if(settingLog != null) {
                    LOGGER.info("The price config has been reference, config id = {}", priceConfig.getId());
                    throw errorWith(SCOPE, ERR_PRICE_CONFIG_HAS_BEEN_REFERENCE, "The price config has been reference");
                } else {
                    priceConfigProvider.deletePriceConfig(priceConfig);
                }
            }
        });
    }

    @Override
    public void createEnergyMeterDefaultSetting(CreateEnergyMeterDefaultSettingCommand cmd) {
        if(cmd != null && cmd.getDefaultSettings() != null) {
            cmd.getDefaultSettings().forEach(defaultSetting -> {
                EnergyMeterDefaultSetting df = ConvertHelper.convert(defaultSetting, EnergyMeterDefaultSetting.class);
                defaultSettingProvider.createEnergyMeterDefaultSetting(df);
            });
        }
    }

    @Override
    public List<EnergyMeterDefaultSettingTemplateDTO> listEnergyDefaultSettingTemplates() {
        List<EnergyMeterDefaultSetting> defaultSettings = defaultSettingProvider.listDefaultSetting(0L, "", 0L, 0, null);
        if(defaultSettings != null) {
            List<EnergyMeterDefaultSettingTemplateDTO> dtos = defaultSettings.stream().map(defaultSetting -> {
                EnergyMeterDefaultSettingTemplateDTO dto = ConvertHelper.convert(defaultSetting, EnergyMeterDefaultSettingTemplateDTO.class);
                return dto;
            }).collect(Collectors.toList());

            return dtos;
        }
        return null;
    }

    @Override
    public ListAuthorizationCommunityByUserResponse listAuthorizationCommunityByUser(ListAuthorizationCommunityCommand cmd) {
        if (null != cmd.getCheckPrivilegeFlag() && cmd.getCheckPrivilegeFlag() == PmTaskCheckPrivilegeFlag.CHECKED.getCode()) {
            if(null == cmd.getOrganizationId()) {
                LOGGER.error("Not privilege", cmd);
                throw RuntimeErrorException.errorWith(PmTaskErrorCode.SCOPE, PmTaskErrorCode.ERROR_CREATE_TASK_PRIVILEGE,
                        "Not privilege");
            }
        }

        ListAuthorizationCommunityByUserResponse response = new ListAuthorizationCommunityByUserResponse();

        ListUserRelatedProjectByModuleCommand listUserRelatedProjectByModuleCommand = new ListUserRelatedProjectByModuleCommand();
        listUserRelatedProjectByModuleCommand.setOrganizationId(cmd.getOrganizationId());
        listUserRelatedProjectByModuleCommand.setModuleId(49100L);

        List<CommunityDTO> dtos = serviceModuleService.listUserRelatedCommunityByModuleId(listUserRelatedProjectByModuleCommand);

        if (null != cmd.getCheckPrivilegeFlag() && cmd.getCheckPrivilegeFlag() == PmTaskCheckPrivilegeFlag.CHECKED.getCode()) {
            SystemUserPrivilegeMgr resolver = PlatformContext.getComponent("SystemUser");
            User user = UserContext.current().getUser();
            List<CommunityDTO> result = new ArrayList<>();
            dtos.forEach(r -> {
                if (resolver.checkUserPrivilege(user.getId(), EntityType.COMMUNITY.getCode(), r.getId(), cmd.getOrganizationId(), PrivilegeConstants.REPLACE_CREATE_TASK)) {
                    result.add(r);
                }
            });
            response.setCommunities(result);

        }else{
            response.setCommunities(dtos);
        }
        return response;

    }

    private EnergyMeterPriceConfigDTO toEnergyMeterPriceConfigDTO(EnergyMeterPriceConfig priceConfig) {
        EnergyMeterPriceConfigDTO dto = ConvertHelper.convert(priceConfig, EnergyMeterPriceConfigDTO.class);

        if(priceConfig.getExpression() != null) {
            dto.setExpression(analyzeExpression(priceConfig.getExpression()));
        }
        return dto;
    }

    private EnergyMeterPriceConfigExpressionDTO analyzeExpression(String expression) {
        Gson gson = new Gson();
        EnergyMeterPriceConfigExpressionDTO expressionDTO = gson.fromJson(expression, new TypeToken<EnergyMeterPriceConfigExpressionDTO>() {}.getType());
        return expressionDTO;
    }

}
