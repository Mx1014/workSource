package com.everhomes.scheduler;

import com.everhomes.asset.AssetPaymentStrings;
import com.everhomes.asset.AssetService;
import com.everhomes.configuration.ConfigConstants;
import com.everhomes.contract.ContractChargingItem;
import com.everhomes.contract.ContractChargingItemAddress;
import com.everhomes.contract.ContractChargingItemAddressProvider;
import com.everhomes.contract.ContractChargingItemProvider;
import com.everhomes.coordinator.CoordinationLocks;
import com.everhomes.coordinator.CoordinationProvider;
import com.everhomes.customer.EnterpriseCustomer;
import com.everhomes.energy.*;
import com.everhomes.openapi.Contract;
import com.everhomes.organization.OrganizationAddress;
import com.everhomes.organization.OrganizationDetail;
import com.everhomes.organization.OrganizationOwner;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.organization.pm.*;
import com.everhomes.repeat.RepeatService;
import com.everhomes.rest.approval.MeterFormulaVariable;
import com.everhomes.rest.approval.TrueOrFalseFlag;
import com.everhomes.rest.asset.*;
import com.everhomes.rest.contract.ChargingVariablesDTO;
import com.everhomes.rest.contract.ContractChargingItemDTO;
import com.everhomes.rest.contract.PeriodUnit;
import com.everhomes.rest.customer.CustomerType;
import com.everhomes.rest.energy.*;
import com.everhomes.rest.organization.pm.DefaultChargingItemPropertyType;
import com.everhomes.user.UserIdentifier;
import com.everhomes.user.UserProvider;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.DateHelper;
import com.everhomes.util.ExecutorUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.*;

import static com.everhomes.rest.energy.EnergyConsumptionServiceErrorCode.SCOPE;
import static com.everhomes.util.RuntimeErrorException.errorWith;

/**
 * Created by ying.xiong on 2017/10/26.
 */
@Component
public class EnergyTaskScheduleJob extends QuartzJobBean {
    private static final Logger LOGGER = LoggerFactory.getLogger(EnergyTaskScheduleJob.class);

    @Autowired
    private PropertyMgrProvider propertyMgrProvider;

    @Autowired
    private OrganizationProvider organizationProvider;

    @Autowired
    private UserProvider userProvider;

    @Autowired
    private ScheduleProvider scheduleProvider;

    @Autowired
    private EnergyPlanProvider energyPlanProvider;

    @Autowired
    private RepeatService repeatService;

    @Autowired
    private EnergyMeterProvider meterProvider;

    @Autowired
    private CoordinationProvider coordinationProvider;

    @Autowired
    private EnergyConsumptionService energyConsumptionService;

    @Autowired
    private EnergyMeterTaskProvider taskProvider;

    @Autowired
    private EnergyMeterAddressProvider meterAddressProvider;

    @Autowired
    private EnergyMeterProvider energyMeterProvider;

    @Autowired
    private ContractChargingItemAddressProvider contractChargingItemAddressProvider;

    @Autowired
    private ContractChargingItemProvider contractChargingItemProvider;

    @Autowired
    private DefaultChargingItemProvider defaultChargingItemProvider;

    @Autowired
    private AssetService assetService;

    @Autowired
    private EnergyMeterReadingLogProvider meterReadingLogProvider;

    @Autowired
    private EnergyMeterChangeLogProvider meterChangeLogProvider;

    @Autowired
    private EnergyMeterSettingLogProvider meterSettingLogProvider;

    @Autowired
    private EnergyMeterFormulaProvider meterFormulaProvider;

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        if(LOGGER.isInfoEnabled()) {
            LOGGER.info("EnergyTaskScheduleJob" + new Timestamp(DateHelper.currentGMTTime().getTime()));
        }

        //双机判断
        if(RunningFlag.fromCode(scheduleProvider.getRunningFlag()) == RunningFlag.TRUE) {
            calculateCloseTaskFee();
            createTask();
        }
    }

    private void calculateCloseTaskFee() {
        LOGGER.info("EnergyTaskScheduleJob: calculateCloseTaskFee.");
        //suanqian
        ExecutorUtil.submit(new Runnable() {
            @Override
            public void run() {
                generatePaymentExpectancies();
            }
        });

    }

    public void calculateTaskFeeByTaskId(Long taskId) {
        EnergyMeterTask task = taskProvider.findEnergyMeterTaskById(taskId);
        generateTaskPaymentExpectancies(task);
    }

    private void generatePaymentExpectancies() {
        List<EnergyMeterTask> tasks = taskProvider.listNotGeneratePaymentEnergyMeterTasks();
        if(tasks != null && tasks.size() > 0) {
            tasks.forEach(task -> {
                generateTaskPaymentExpectancies(task);
            });
        }

    }

    private BigDecimal calculateAmount(EnergyMeterTask task, EnergyMeter meter) {
        // 拿出单个表统计当天的所有的读表记录
        List<EnergyMeterReadingLog> meterReadingLogs = meterReadingLogProvider.listMeterReadingLogByTask(task.getId());

        BigDecimal amount = new BigDecimal("0");
        BigDecimal taskReading = task.getReading();
        BigDecimal readingAnchor = task.getLastTaskReading();
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
                }
                // 有换表 量程加上旧表读数-锚点,锚点重置为新读数
                if(TrueOrFalseFlag.fromCode(log.getChangeMeterFlag()) == TrueOrFalseFlag.TRUE) {
                    changeFlag = TrueOrFalseFlag.TRUE.getCode();
                    EnergyMeterChangeLog changeLog = meterChangeLogProvider.getEnergyMeterChangeLogByLogId(log.getId());
                    amount = amount.add(changeLog.getOldReading().subtract(readingAnchor));
                    readingAnchor = changeLog.getNewReading();
                }
            }
        }

        //计算走了多少字 量程+任务最后一次读数-锚点
        amount = amount.add(taskReading.subtract(readingAnchor));

        //获取公式,计算当天的费用
        EnergyMeterSettingLog rateSetting = meterSettingLogProvider.findCurrentSettingByMeterId(meter.getNamespaceId(),meter.getId(),EnergyMeterSettingType.RATE ,task.getExecutiveStartTime());
        EnergyMeterSettingLog amountSetting = meterSettingLogProvider.findCurrentSettingByMeterId(meter.getNamespaceId(),meter.getId(),EnergyMeterSettingType.AMOUNT_FORMULA ,task.getExecutiveStartTime());
        if(amountSetting == null || rateSetting == null) {
            return null;
        }

        String aoumtFormula = meterFormulaProvider.findById(amountSetting.getNamespaceId(), amountSetting.getFormulaId()).getExpression();

        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("js");

        engine.put(MeterFormulaVariable.AMOUNT.getCode(), amount);
        engine.put(MeterFormulaVariable.TIMES.getCode(), rateSetting.getSettingValue());

        BigDecimal realAmount = new BigDecimal(0);

        try {
            realAmount = BigDecimal.valueOf((double) engine.eval(aoumtFormula));
        } catch (ScriptException e) {
            e.printStackTrace();
            LOGGER.error("The energy meter error");
            throw errorWith(SCOPE, EnergyConsumptionServiceErrorCode.ERR_METER_FORMULA_ERROR, "The energy meter error");
        }

        return realAmount;
    }

    private void generateTaskPaymentExpectancies(EnergyMeterTask task) {
        Boolean generateFlag = false;
        EnergyMeter meter = energyMeterProvider.findById(task.getNamespaceId(), task.getMeterId());
        if(meter == null || !EnergyMeterStatus.ACTIVE.equals(EnergyMeterStatus.fromCode(meter.getStatus()))) {
            return ;
        }
        //task关联的表关联的门牌有没有合同
        List<EnergyMeterAddress> addresses = meterAddressProvider.listByMeterId(task.getMeterId());
        if(addresses != null && addresses.size() > 0) {
            BigDecimal amount = calculateAmount(task, meter);
            EnergyMeterAddress address = addresses.get(0);
            //eh_contract_charging_item_addresses
            List<ContractChargingItemAddress> contractChargingItemAddresses = contractChargingItemAddressProvider.findByAddressId(address.getAddressId(), meter.getMeterType());
            if(contractChargingItemAddresses != null && contractChargingItemAddresses.size() > 0) {
                List<FeeRules> feeRules = new ArrayList<>();
                List<Long> contractId = new ArrayList<>();
                contractChargingItemAddresses.forEach(contractChargingItemAddress -> {
                    ContractChargingItem item = contractChargingItemProvider.findById(contractChargingItemAddress.getContractChargingItemId());
                    FeeRules feeRule = generateChargingItemsFeeRule(amount, item.getChargingItemId(), item.getChargingStandardId(),
                            item.getChargingStartTime().getTime(), item.getChargingExpiredTime().getTime(), item.getChargingVariables(), address);
                    feeRules.add(feeRule);
                    contractId.add(item.getContractId());
                });
                //suanqian paymentExpectancies_re_struct();
                paymentExpectancies_re_struct(task, address, feeRules, contractId.get(0));
                generateFlag = true;
            } else {//门牌有没有默认计价条款、所属楼栋有没有默认计价条款、所属园区有没有默认计价条款 eh_default_charging_item_properties
                List<DefaultChargingItemProperty> properties = defaultChargingItemProvider.findByPropertyId(DefaultChargingItemPropertyType.APARTMENT.getCode(), address.getAddressId(), meter.getMeterType());
                if(properties != null) {
                    List<FeeRules> feeRules = new ArrayList<>();
                    properties.forEach(property -> {
                        DefaultChargingItem item = defaultChargingItemProvider.findById(property.getDefaultChargingItemId());
                        FeeRules feeRule = generateChargingItemsFeeRule(amount, item.getChargingItemId(), item.getChargingStandardId(),
                                item.getChargingStartTime().getTime(), item.getChargingExpiredTime().getTime(), item.getChargingVariables(), address);
                        feeRules.add(feeRule);
                    });
                    //suanqian paymentExpectancies_re_struct();
                    paymentExpectancies_re_struct(task, address, feeRules, null);
                    generateFlag = true;
                } else {
                    properties = defaultChargingItemProvider.findByPropertyId(DefaultChargingItemPropertyType.BUILDING.getCode(), address.getBuildingId(), meter.getMeterType());
                    if(properties != null) {
                        List<FeeRules> feeRules = new ArrayList<>();
                        properties.forEach(property -> {
                            DefaultChargingItem item = defaultChargingItemProvider.findById(property.getDefaultChargingItemId());
                            FeeRules feeRule = generateChargingItemsFeeRule(amount, item.getChargingItemId(), item.getChargingStandardId(),
                                    item.getChargingStartTime().getTime(), item.getChargingExpiredTime().getTime(), item.getChargingVariables(), address);
                            feeRules.add(feeRule);
                        });
                        //suanqian paymentExpectancies_re_struct();
                        paymentExpectancies_re_struct(task, address, feeRules, null);
                        generateFlag = true;
                    } else {
                        properties = defaultChargingItemProvider.findByPropertyId(DefaultChargingItemPropertyType.BUILDING.getCode(), meter.getCommunityId(), meter.getMeterType());
                        if(properties != null) {
                            List<FeeRules> feeRules = new ArrayList<>();
                            properties.forEach(property -> {
                                DefaultChargingItem item = defaultChargingItemProvider.findById(property.getDefaultChargingItemId());
                                FeeRules feeRule = generateChargingItemsFeeRule(amount, item.getChargingItemId(), item.getChargingStandardId(),
                                        item.getChargingStartTime().getTime(), item.getChargingExpiredTime().getTime(), item.getChargingVariables(), address);
                                feeRules.add(feeRule);
                            });
                            //suanqian paymentExpectancies_re_struct();
                            paymentExpectancies_re_struct(task, address, feeRules, null);
                            generateFlag = true;
                        }
                    }
                }
            }

        }
        if(generateFlag) {
            task.setGeneratePaymentFlag(TaskGeneratePaymentFlag.GENERATED.getCode());
        } else {
            task.setGeneratePaymentFlag(TaskGeneratePaymentFlag.NON_CHARGING_ITEM.getCode());
        }
        taskProvider.updateEnergyMeterTask(task);
    }

    private void paymentExpectancies_re_struct(EnergyMeterTask task, EnergyMeterAddress address, List<FeeRules> feeRules, Long contractId) {
        PaymentExpectanciesCommand command = new PaymentExpectanciesCommand();
        command.setContractId(contractId);
        command.setFeesRules(feeRules);
        command.setNamesapceId(task.getNamespaceId());
        command.setOwnerId(task.getTargetId());
        command.setOwnerType("community");

//        eh_organization_owner_address eh_organization_addresses
        OrganizationAddress organizationAddress = organizationProvider.findActiveOrganizationAddressByAddressId(address.getAddressId());
        if(organizationAddress != null) {
            command.setTargetType("eh_organization");
            command.setTargetId(organizationAddress.getOrganizationId());
            OrganizationDetail detail = organizationProvider.findOrganizationDetailByOrganizationId(organizationAddress.getOrganizationId());
            command.setTargetName(detail.getDisplayName());
            command.setNoticeTel(detail.getContact());
        } else {
            List<CommunityPmOwner> pmOwners = propertyMgrProvider.listCommunityPmOwners(task.getTargetId(), address.getAddressId());
            if(pmOwners != null && pmOwners.size() > 0) {
                command.setTargetType("eh_user");
                command.setTargetName(pmOwners.get(0).getContactName());
                command.setNoticeTel(pmOwners.get(0).getContactToken());
                UserIdentifier identifier = userProvider.findClaimedIdentifierByToken(pmOwners.get(0).getNamespaceId(), pmOwners.get(0).getContactToken());
                if(identifier != null) {
                    command.setTargetId(identifier.getOwnerUid());
                }
            }
        }
        LOGGER.debug("paymentExpectancies_re_struct command: {}", command);
        assetService.paymentExpectancies_re_struct(command);
    }

    private FeeRules generateChargingItemsFeeRule(BigDecimal amount, Long chargingItemId, Long chargingStandardId,
            Long chargingStartTime, Long chargingExpiredTime, String chargingVariables, EnergyMeterAddress address) {
        Gson gson = new Gson();
        FeeRules feeRule = new FeeRules();
        feeRule.setChargingItemId(chargingItemId);
        feeRule.setChargingStandardId(chargingStandardId);
        feeRule.setDateStrBegin(new Date(chargingStartTime));
        feeRule.setDateStrEnd(new Date(chargingExpiredTime));
        List<ContractProperty> contractProperties = new ArrayList<>();
        ContractProperty cp = new ContractProperty();
        cp.setApartmentName(address.getApartmentName());
        cp.setBuldingName(address.getBuildingName());
        cp.setAddressId(address.getAddressId());
        contractProperties.add(cp);
        feeRule.setProperties(contractProperties);

        ChargingVariablesDTO chargingVariableDTO = gson.fromJson(chargingVariables, new TypeToken<ChargingVariablesDTO>() {}.getType());
        List<PaymentVariable> pvs = chargingVariableDTO.getChargingVariables();
        List<VariableIdAndValue> vv = new ArrayList<>();
        if(pvs != null && pvs.size() > 0) {
            pvs.forEach(pv -> {
                VariableIdAndValue variableIdAndValue = ConvertHelper.convert(pv, VariableIdAndValue.class);
                vv.add(variableIdAndValue);
            });
            VariableIdAndValue variableIdAndValue = new VariableIdAndValue();
            //用量的identifier在eh_payment_variables为yl
            variableIdAndValue.setVaribleIdentifier("yl");
            //用量
            variableIdAndValue.setVariableValue(amount);
        }
        feeRule.setVariableIdAndValueList(vv);

        return feeRule;
    }

    private void createTask() {
        if(LOGGER.isInfoEnabled()) {
            LOGGER.info("EnergyTaskScheduleJob: createTask");
        }
        List<EnergyPlan> plans = energyPlanProvider.listActivePlan();

        if(plans != null && plans.size() > 0) {
            plans.forEach(plan -> {
                List<EnergyPlanMeterMap> maps = energyPlanProvider.listMetersByEnergyPlan(plan.getId());
                if(maps != null && maps.size() > 0) {
                    boolean isRepeat = repeatService.isRepeatSettingActive(plan.getRepeatSettingId());
                    LOGGER.info("EnergyScheduleJob: plan id = " + plan.getId()
                            + "repeat setting id = "+ plan.getRepeatSettingId() + "is repeat setting active: " + isRepeat);

                    for(EnergyPlanMeterMap map : maps) {
                        EnergyMeter meter = meterProvider.findById(plan.getNamespaceId(), map.getMeterId());
                        if(meter == null || meter.getStatus() == null
                                || !EnergyMeterStatus.ACTIVE.equals(EnergyMeterStatus.fromCode(meter.getStatus()))) {
                            LOGGER.info("EnergyScheduleJob meter is not exist or active! meterId = " + map.getMeterId());
                            continue;
                        } else if(isRepeat){
                            this.coordinationProvider.getNamedLock(CoordinationLocks.CREATE_ENERGY_TASK.getCode()).tryEnter(()-> {
                                energyConsumptionService.creatMeterTask(map, plan);
                            });
                        }
                    }
                }
            });
        }
    }

}
