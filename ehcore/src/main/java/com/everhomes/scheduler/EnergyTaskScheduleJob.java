package com.everhomes.scheduler;

import com.everhomes.asset.AssetPaymentConstants;
import com.everhomes.asset.AssetService;
import com.everhomes.contract.ContractChargingItem;
import com.everhomes.contract.ContractChargingItemAddress;
import com.everhomes.contract.ContractChargingItemAddressProvider;
import com.everhomes.contract.ContractChargingItemProvider;
import com.everhomes.coordinator.CoordinationLocks;
import com.everhomes.coordinator.CoordinationProvider;
import com.everhomes.customer.EnterpriseCustomer;
import com.everhomes.customer.EnterpriseCustomerProvider;
import com.everhomes.customer.IndividualCustomerProvider;
import com.everhomes.energy.EnergyConsumptionService;
import com.everhomes.energy.EnergyMeter;
import com.everhomes.energy.EnergyMeterAddress;
import com.everhomes.energy.EnergyMeterAddressProvider;
import com.everhomes.energy.EnergyMeterChangeLog;
import com.everhomes.energy.EnergyMeterChangeLogProvider;
import com.everhomes.energy.EnergyMeterProvider;
import com.everhomes.energy.EnergyMeterReadingLog;
import com.everhomes.energy.EnergyMeterReadingLogProvider;
import com.everhomes.energy.EnergyMeterSettingLog;
import com.everhomes.energy.EnergyMeterSettingLogProvider;
import com.everhomes.energy.EnergyMeterTask;
import com.everhomes.energy.EnergyMeterTaskProvider;
import com.everhomes.energy.EnergyPlan;
import com.everhomes.energy.EnergyPlanMeterMap;
import com.everhomes.energy.EnergyPlanProvider;
import com.everhomes.openapi.Contract;
import com.everhomes.openapi.ContractProvider;
import com.everhomes.organization.OrganizationAddress;
import com.everhomes.organization.OrganizationDetail;
import com.everhomes.organization.OrganizationOwner;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.organization.pm.CommunityPmOwner;
import com.everhomes.organization.pm.DefaultChargingItem;
import com.everhomes.organization.pm.DefaultChargingItemProperty;
import com.everhomes.organization.pm.DefaultChargingItemProvider;
import com.everhomes.organization.pm.OrganizationOwnerAddress;
import com.everhomes.organization.pm.PropertyMgrProvider;
import com.everhomes.repeat.RepeatService;
import com.everhomes.rest.acl.PrivilegeConstants;
import com.everhomes.rest.approval.TrueOrFalseFlag;
import com.everhomes.rest.asset.ContractProperty;
import com.everhomes.rest.asset.FeeRules;
import com.everhomes.rest.asset.PaymentExpectanciesCommand;
import com.everhomes.rest.asset.PaymentVariable;
import com.everhomes.rest.asset.VariableIdAndValue;
import com.everhomes.rest.contract.ChargingVariablesDTO;
import com.everhomes.rest.customer.CustomerType;
import com.everhomes.rest.energy.EnergyMeterSettingType;
import com.everhomes.rest.energy.EnergyMeterStatus;
import com.everhomes.rest.energy.EnergyTaskStatus;
import com.everhomes.rest.energy.TaskGeneratePaymentFlag;
import com.everhomes.rest.organization.pm.DefaultChargingItemPropertyType;
import com.everhomes.search.EnergyMeterTaskSearcher;
import com.everhomes.user.UserIdentifier;
import com.everhomes.user.UserProvider;
import com.everhomes.util.DateHelper;
import com.everhomes.util.ExecutorUtil;
import com.everhomes.util.StringHelper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private ContractProvider contractProvider;

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
    private IndividualCustomerProvider individualCustomerProvider;

    @Autowired
    private EnterpriseCustomerProvider enterpriseCustomerProvider;

    @Autowired
    private EnergyMeterTaskSearcher energyMeterTaskSearcher;
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
        ExecutorUtil.submit(this::generatePaymentExpectancies);

    }

    public void calculateTaskFeeByTaskId(Long taskId) {
        EnergyMeterTask task = taskProvider.findEnergyMeterTaskById(taskId);
        generateTaskPaymentExpectancies(task);
    }
    
    public void calculateTaskFee() {
    	generatePaymentExpectancies();
	}

    private void generatePaymentExpectancies() {
        List<EnergyMeterTask> tasks = taskProvider.listNotGeneratePaymentEnergyMeterTasks();
        if(tasks != null && tasks.size() > 0) {
            tasks.forEach(task -> {
                if(EnergyTaskStatus.NON_READ.equals(EnergyTaskStatus.fromCode(task.getStatus()))) {
                    task.setStatus(EnergyTaskStatus.NON_READ_DELAY.getCode());
                    taskProvider.updateEnergyMeterTask(task);
                    energyMeterTaskSearcher.feedDoc(task);
                    return ;
                }
                generateTaskPaymentExpectancies(task);
            });
        }

    }

    private Map<Long, BigDecimal> calculateAmount(EnergyMeterTask task, EnergyMeter meter,List<EnergyMeterAddress> addresses) {
        // 拿出单个表统计当天的所有的读表记录
        List<EnergyMeterReadingLog> meterReadingLogs = meterReadingLogProvider.listMeterReadingLogByTask(task.getId());

        BigDecimal amount = BigDecimal.ZERO;
        BigDecimal taskReading = task.getReading() == null ? BigDecimal.ZERO : task.getReading();
        BigDecimal readingAnchor = task.getLastTaskReading() == null ? BigDecimal.ZERO : task.getLastTaskReading();
//        // 重置flag
        Byte resetFlag = TrueOrFalseFlag.FALSE.getCode();
//        // 换表flag
        Byte changeFlag = TrueOrFalseFlag.FALSE.getCode();

        //查看是否有换表,是否有归零
        if(meterReadingLogs != null) {
            for(EnergyMeterReadingLog log : meterReadingLogs) {
                // 有归零 量程设置为最大值-锚点,锚点设置为0
                if(TrueOrFalseFlag.fromCode(log.getResetMeterFlag()) == TrueOrFalseFlag.TRUE) {
                    resetFlag = TrueOrFalseFlag.TRUE.getCode();

                    amount = amount.add(meter.getMaxReading().subtract(readingAnchor));
                    readingAnchor = BigDecimal.ZERO;
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

        Map<Long, BigDecimal> realAmountMap = new HashMap<>();

        for (EnergyMeterAddress address : addresses) {
            //这里对接缴费之后去除  直接变成  读数 * 倍率 * 分摊比列
            if (address.getBurdenRate() != null && address.getBurdenRate().compareTo(new BigDecimal(0)) != 0) {
                BigDecimal  burdenRate = address.getBurdenRate();
                realAmountMap.put(address.getAddressId(), amount.multiply(rateSetting.getSettingValue()).multiply(burdenRate));
            }else {
                realAmountMap.put(address.getAddressId(), amount.multiply(rateSetting.getSettingValue()));
            }
        }

        return realAmountMap;
    }

    private Timestamp getEndDate(Timestamp date, Timestamp startDate) {
        Calendar cal = Calendar.getInstance();
        Calendar startCal = Calendar.getInstance();
        cal.setTime(date);
        startCal.setTime(startDate);
        if(cal.get(Calendar.MONTH) == startCal.get(Calendar.MONTH)) {
            //开始结束同月则直接取月末
            cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
            Timestamp monthEnd = getDayBegin(cal);
            return monthEnd;
        }
        //减一个月取月末
        cal.add(Calendar.MONTH, -1);
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        Timestamp monthEnd = getDayBegin(cal);
        return monthEnd;
    }

    private Timestamp getDayBegin(Calendar cal) {
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 001);
        return new Timestamp(cal.getTimeInMillis());
    }

    private void generateTaskPaymentExpectancies(EnergyMeterTask task) {
        Boolean generateFlag = false;
        EnergyMeter meter = energyMeterProvider.findById(task.getNamespaceId(), task.getMeterId());
        if (meter == null || !EnergyMeterStatus.ACTIVE.equals(EnergyMeterStatus.fromCode(meter.getStatus()))) {
            return;
        }
        //task关联的表关联的门牌有没有合同
        List<EnergyMeterAddress> addresses = meterAddressProvider.listByMeterId(task.getMeterId());
        if (addresses != null && addresses.size() > 0) {
            //add parameter address
            Map<Long, BigDecimal> amountMap = calculateAmount(task, meter, addresses);
            //EnergyMeterAddress address = addresses.get(0);
            for (EnergyMeterAddress address : addresses) {
                Timestamp endDate = getEndDate(task.getExecutiveExpireTime(), task.getExecutiveStartTime());
                List<ContractChargingItemAddress> contractChargingItemAddresses = contractChargingItemAddressProvider.findByAddressId(address.getAddressId(), meter.getMeterType(), task.getExecutiveStartTime(), endDate);
                if (contractChargingItemAddresses != null && contractChargingItemAddresses.size() > 0) {
                    List<FeeRules> feeRules = new ArrayList<>();
                    List<Long> contractId = new ArrayList<>();
                    contractChargingItemAddresses.forEach(contractChargingItemAddress -> {
                        ContractChargingItem item = contractChargingItemProvider.findById(contractChargingItemAddress.getContractChargingItemId());
                        BigDecimal amount = (amountMap != null ? amountMap.get(address.getAddressId()) : new BigDecimal(0));
                        //#36363 水费抄表后，合同的费用清单出不来 add by djm
						FeeRules feeRule = generateChargingItemsFeeRule(amount, item.getChargingItemId(), item.getChargingStandardId(), task.getExecutiveStartTime().getTime(),
								task.getExecutiveExpireTime().getTime(), item.getChargingVariables(), address, meter.getMeterType(), item.getBillGroupId());
                        feeRules.add(feeRule);
                        contractId.add(item.getContractId());
                    });
                    //suanqian paymentExpectanciesCalculate();
                    paymentExpectancies_re_struct(task, address, feeRules, contractId.get(0));
                } else {//门牌有没有默认计价条款、所属楼栋有没有默认计价条款、所属园区有没有默认计价条款 eh_default_charging_item_properties
                    List<DefaultChargingItemProperty> properties = defaultChargingItemProvider.findByPropertyId(DefaultChargingItemPropertyType.APARTMENT.getCode(), address.getAddressId(), meter.getMeterType());
                    if (properties != null && properties.size() > 0) {
                        List<FeeRules> feeRules = new ArrayList<>();
                        properties.forEach(property -> {
                            DefaultChargingItem item = defaultChargingItemProvider.findById(property.getDefaultChargingItemId());
                            BigDecimal amount = (amountMap != null ? amountMap.get(address.getAddressId()) : new BigDecimal(0));
                            FeeRules feeRule = generateChargingItemsFeeRule(amount, item.getChargingItemId(), item.getChargingStandardId(),
                                    task.getExecutiveStartTime().getTime(), task.getExecutiveExpireTime().getTime(), item.getChargingVariables(), address,meter.getMeterType(), null);
                            feeRules.add(feeRule);
                        });
                        //suanqian paymentExpectanciesCalculate();
                        paymentExpectancies_re_struct(task, address, feeRules, null);
                        generateFlag = true;
                    } else {
                        properties = defaultChargingItemProvider.findByPropertyId(DefaultChargingItemPropertyType.BUILDING.getCode(), address.getBuildingId(), meter.getMeterType());
                        if (properties != null && properties.size() > 0) {
                            List<FeeRules> feeRules = new ArrayList<>();
                            properties.forEach(property -> {
                                DefaultChargingItem item = defaultChargingItemProvider.findById(property.getDefaultChargingItemId());
                                BigDecimal amount = (amountMap != null ? amountMap.get(address.getAddressId()) : new BigDecimal(0));
                                FeeRules feeRule = generateChargingItemsFeeRule(amount, item.getChargingItemId(), item.getChargingStandardId(),
                                        task.getExecutiveStartTime().getTime(), task.getExecutiveExpireTime().getTime(), item.getChargingVariables(), address,meter.getMeterType(), null);
                                feeRules.add(feeRule);
                            });
                            //suanqian paymentExpectanciesCalculate();
                            paymentExpectancies_re_struct(task, address, feeRules, null);
                            generateFlag = true;
                        } else {
                            properties = defaultChargingItemProvider.findByPropertyId(DefaultChargingItemPropertyType.COMMUNITY.getCode(), meter.getCommunityId(), meter.getMeterType());
                            if (properties != null && properties.size() > 0) {
                                List<FeeRules> feeRules = new ArrayList<>();
                                properties.forEach(property -> {
                                    DefaultChargingItem item = defaultChargingItemProvider.findById(property.getDefaultChargingItemId());
                                    BigDecimal amount = (amountMap != null ? amountMap.get(address.getAddressId()) : new BigDecimal(0));
                                    FeeRules feeRule = generateChargingItemsFeeRule(amount, item.getChargingItemId(), item.getChargingStandardId(),
                                            task.getExecutiveStartTime().getTime(), task.getExecutiveExpireTime().getTime(), item.getChargingVariables(), address,meter.getMeterType(), null);
                                    feeRules.add(feeRule);
                                });
                                //suanqian paymentExpectanciesCalculate();
                                paymentExpectancies_re_struct(task, address, feeRules, null);
                                generateFlag = true;
                            }
                        }
                    }
                }

            }

        }
        //eh_contract_charging_item_addresses
        if (generateFlag) {
            task.setGeneratePaymentFlag(TaskGeneratePaymentFlag.GENERATED.getCode());
        } else {
            task.setGeneratePaymentFlag(TaskGeneratePaymentFlag.NON_CHARGING_ITEM.getCode());
        }
        taskProvider.updateEnergyMeterTask(task);
    }

    private void paymentExpectancies_re_struct(EnergyMeterTask task, EnergyMeterAddress address, List<FeeRules> feeRules, Long contractId) {
        PaymentExpectanciesCommand command = new PaymentExpectanciesCommand();
        command.setIsEffectiveImmediately((byte)1);
        command.setContractId(contractId);
        command.setContractIdType((byte)1);
        if(command.getContractId() == null) {
            command.setContractId(task.getId());
            command.setContractIdType((byte)0);
        }
        command.setFeesRules(feeRules);
        command.setNamesapceId(task.getNamespaceId());
        command.setOwnerId(task.getTargetId());
        command.setOwnerType("community");

//      没合同时  eh_organization_owner_address eh_organization_addresses
        if(command.getContractIdType() == 0) {
            OrganizationAddress organizationAddress = organizationProvider.findActiveOrganizationAddressByAddressId(address.getAddressId());
            LOGGER.debug("paymentExpectanciesCalculate organizationAddress: {}", StringHelper.toJsonString(organizationAddress));
            if(organizationAddress != null) {
                command.setTargetType("eh_organization");
                command.setTargetId(organizationAddress.getOrganizationId());
                OrganizationDetail detail = organizationProvider.findOrganizationDetailByOrganizationId(organizationAddress.getOrganizationId());
                command.setTargetName(detail.getDisplayName());
                command.setNoticeTel(detail.getContact());
            } else {
                List<OrganizationOwnerAddress> addresses = propertyMgrProvider.listOrganizationOwnerAuthAddressByAddressId(task.getNamespaceId(), address.getAddressId());
                LOGGER.debug("paymentExpectanciesCalculate organizationAddress: {}", StringHelper.toJsonString(addresses));
                if(addresses != null && addresses.size() > 0) {
                    command.setTargetType("eh_user");
                    for(OrganizationOwnerAddress ownerAddress : addresses) {
                        CommunityPmOwner pmOwner = propertyMgrProvider.findPropOwnerById(ownerAddress.getOrganizationOwnerId());
                        if(pmOwner != null) {
                            command.setTargetName(pmOwner.getContactName());
                            command.setNoticeTel(pmOwner.getContactToken());
                            UserIdentifier identifier = userProvider.findClaimedIdentifierByToken(pmOwner.getNamespaceId(), pmOwner.getContactToken());
                            if(identifier != null) {
                                command.setTargetId(identifier.getOwnerUid());
                                break ;
                            }
                        }

                    }

                }
            }
        } else {
            Contract contract = contractProvider.findContractById(command.getContractId());
            command.setContractNum(contract.getContractNumber());
            if(CustomerType.ENTERPRISE.equals(CustomerType.fromStatus(contract.getCustomerType()))) {
                command.setTargetType("eh_organization");
                EnterpriseCustomer customer = enterpriseCustomerProvider.findById(contract.getCustomerId());
                if(customer != null) {
                    command.setTargetId(customer.getOrganizationId());
                    command.setTargetName(customer.getName());
                    command.setNoticeTel(customer.getContactMobile());
                }
            } else if(CustomerType.INDIVIDUAL.equals(CustomerType.fromStatus(contract.getCustomerType()))) {
                command.setTargetType("eh_user");
                OrganizationOwner owner = individualCustomerProvider.findOrganizationOwnerById(contract.getCustomerId());
                if(owner != null) {
                    command.setTargetName(owner.getContactName());
                    command.setNoticeTel(owner.getContactToken());
                    UserIdentifier identifier = userProvider.findClaimedIdentifierByToken(owner.getNamespaceId(), owner.getContactToken());
                    if(identifier != null) {
                        command.setTargetId(identifier.getOwnerUid());
                    }
                }
            }
        }
        command.setModuleId(PrivilegeConstants.ENERGY_MODULE);
        LOGGER.debug("paymentExpectanciesCalculate command: {}", command);
        assetService.upodateBillStatusOnContractStatusChange(command.getContractId(), AssetPaymentConstants.CONTRACT_CANCEL);
        assetService.paymentExpectanciesCalculate(command);
    }

    private FeeRules generateChargingItemsFeeRule(BigDecimal amount, Long chargingItemId, Long chargingStandardId,
            Long chargingStartTime, Long chargingExpiredTime, String chargingVariables, EnergyMeterAddress address,Byte meterType, Long billGroupId) {
        Gson gson = new Gson();
        FeeRules feeRule = new FeeRules();
        feeRule.setChargingItemId(chargingItemId);
        feeRule.setChargingStandardId(chargingStandardId);
        feeRule.setDateStrBegin(new Date(chargingStartTime));
        feeRule.setDateStrEnd(new Date(chargingExpiredTime));
        if (billGroupId != null) {
        	feeRule.setBillGroupId(billGroupId);
		}
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
                VariableIdAndValue variableIdAndValue = new VariableIdAndValue();
                variableIdAndValue.setVaribleIdentifier(pv.getVariableIdentifier());
                variableIdAndValue.setVariableValue(pv.getVariableValue());
                vv.add(variableIdAndValue);
            });
            VariableIdAndValue variableIdAndValue = new VariableIdAndValue();
            //用量的identifier在eh_payment_variables为yl
            variableIdAndValue.setVaribleIdentifier("yl");
            //用量
            variableIdAndValue.setVariableValue(amount);
            VariableIdAndValue variableIdAndValueBurden = new VariableIdAndValue();
            variableIdAndValueBurden.setVaribleIdentifier("blxs");
            variableIdAndValueBurden.setVariableValue(new BigDecimal(1));
            vv.add(variableIdAndValue);
            vv.add(variableIdAndValueBurden);
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
            for (EnergyPlan plan : plans) {
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
            }
        }
    }

}
