package com.everhomes.contract;

import com.everhomes.community.Community;
import com.everhomes.community.CommunityProvider;
import com.everhomes.locale.LocaleTemplateService;
import com.everhomes.messaging.MessagingService;
import com.everhomes.openapi.Contract;
import com.everhomes.openapi.ContractBuildingMapping;
import com.everhomes.openapi.ContractBuildingMappingProvider;
import com.everhomes.openapi.ContractProvider;
import com.everhomes.organization.OrganizationMember;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.organization.pm.CommunityAddressMapping;
import com.everhomes.organization.pm.PropertyMgrProvider;
<<<<<<< HEAD
import com.everhomes.organization.pm.PropertyMgrService;
import com.everhomes.rest.contract.ContractParamDTO;
import com.everhomes.rest.contract.ContractStatus;
import com.everhomes.rest.contract.GetContractParamCommand;
import com.everhomes.rest.contract.PeriodUnit;
import com.everhomes.rest.customer.CustomerType;
=======
import com.everhomes.rest.app.AppConstants;
import com.everhomes.rest.contract.*;
import com.everhomes.rest.messaging.MessageBodyType;
import com.everhomes.rest.messaging.MessageChannel;
import com.everhomes.rest.messaging.MessageDTO;
import com.everhomes.rest.messaging.MessagingConstants;
>>>>>>> payment-contract
import com.everhomes.rest.organization.pm.AddressMappingStatus;
import com.everhomes.rest.pmNotify.PmNotifyType;
import com.everhomes.rest.user.MessageChannelType;
import com.everhomes.scheduler.ScheduleProvider;
import com.everhomes.search.ContractSearcher;
import com.everhomes.user.User;
import com.everhomes.util.DateHelper;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by ying.xiong on 2017/8/26.
 */
@Component
@Scope("prototype")
public class ContractScheduleJob extends QuartzJobBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(ContractScheduleJob.class);

    public static final String SCHEDELE_NAME = "contract-";

    public static String CRON_EXPRESSION = "0 0 2 * * ?";

    @Autowired
    private ScheduleProvider scheduleProvider;

    @Autowired
    private ContractProvider contractProvider;

    @Autowired
    private ContractSearcher contractSearcher;

    @Autowired
    private ContractBuildingMappingProvider contractBuildingMappingProvider;

    @Autowired
    private PropertyMgrProvider propertyMgrProvider;
    @Autowired
    private PropertyMgrService propertyMgrService;

    @Autowired
    private CommunityProvider communityProvider;

    @Autowired
    private ContractPaymentPlanProvider planProvider;

    @Autowired
    private OrganizationProvider organizationProvider;

    @Autowired
    private MessagingService messagingService;

    @Autowired
    private LocaleTemplateService localeTemplateService;

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        Timestamp now = new Timestamp(DateHelper.currentGMTTime().getTime());
        if(LOGGER.isInfoEnabled()) {
            LOGGER.info("ContractScheduleJob" + now);
        }
        Map<Long, List<Contract>> contracts = contractProvider.listContractGroupByCommunity();
        if(contracts != null && contracts.size() > 0) {
            contracts.forEach((communityId, contractList) -> {
                Community community = communityProvider.findCommunityById(communityId);
                if(community == null) {
                    return;
                }

                ContractParam communityExist = contractProvider.findContractParamByCommunityId(community.getNamespaceId(), communityId);
                if(communityExist == null && communityId != null) {
                    communityExist = contractProvider.findContractParamByCommunityId(community.getNamespaceId(), null);
                }
                ContractParam param = communityExist;

                if(param != null) {
                    if(contractList != null) {
                        String locale = "zh_CN";
                        contractList.forEach(contract -> {
                            //当前时间在合同到期时间之后 直接过期
                            if(now.after(contract.getContractEndDate())) {
                                contract.setStatus(ContractStatus.EXPIRED.getCode());
                                contractProvider.updateContract(contract);
                                contractSearcher.feedDoc(contract);
                                dealAddressLivingStatus(contract, AddressMappingStatus.FREE.getCode());
                            } else {
                                if(ContractStatus.ACTIVE.equals(ContractStatus.fromStatus(contract.getStatus()))) {
                                    //正常合同转即将过期
                                    Timestamp time = addPeriod(now, param.getExpiringPeriod(), param.getExpiringUnit());
                                    if(time.after(contract.getContractEndDate())) {
                                        contract.setStatus(ContractStatus.EXPIRING.getCode());
                                        contractProvider.updateContract(contract);
                                        contractSearcher.feedDoc(contract);
                                    }

                                    Timestamp notifyTime = addPeriod(now, param.getNotifyPeriod(), param.getNotifyUnit());
                                    if(notifyTime.after(contract.getContractEndDate())) {
                                        List<ContractParamGroupMap> maps = contractProvider.listByParamId(param.getId(), ContractParamGroupType.NOTIFY_GROUP.getCode());
                                        if(maps != null && maps.size() > 0) {
                                            Set<Long> userIds = new HashSet<Long>();
                                            maps.forEach(map -> {
                                                //目前只有部门， 先不考虑岗位
                                                List<OrganizationMember> members = organizationProvider.listOrganizationMembersByOrgId(map.getGroupId());
                                                if(members != null && members.size() > 0) {
                                                    members.forEach(member -> {
                                                        userIds.add(member.getTargetId());
                                                    });

                                                }
                                            });
                                            if(userIds.size() > 0) {
                                                String notifyText = getNotifyMessage(contract.getName(), contract.getContractEndDate());
                                                userIds.forEach(userId -> {
                                                    sendMessageToUser(userId, notifyText);
                                                });
                                            }

                                        }
                                    }
                                } else if(ContractStatus.APPROVE_QUALITIED.equals(ContractStatus.fromStatus(contract.getStatus()))) {
                                    //审批通过没有转为正常合同 过期
                                    if(contract.getReviewTime() != null) {
                                        Timestamp time = addPeriod(contract.getReviewTime(), param.getExpiredPeriod(), param.getExpiredUnit());
                                        if(time.before(now)) {
                                            contract.setStatus(ContractStatus.EXPIRED.getCode());
                                            contractProvider.updateContract(contract);
                                            contractSearcher.feedDoc(contract);
                                            dealAddressLivingStatus(contract, AddressMappingStatus.FREE.getCode());
                                        }
                                    }
                                }
                            }
                            Timestamp time = addPeriod(now, param.getPaidPeriod(), PeriodUnit.DAY.getCode());
                            List<ContractPaymentPlan> plans = planProvider.listByContractId(contract.getId());
                            if(plans != null && plans.size() > 0) {
                                plans.forEach(plan -> {
                                    if(plan.getNotifyFlag() == 0) {
                                        if(time.after(plan.getPaidTime())) {
                                            List<ContractParamGroupMap> maps = contractProvider.listByParamId(param.getId(), ContractParamGroupType.PAY_GROUP.getCode());
                                            if(maps != null && maps.size() > 0) {
                                                Set<Long> userIds = new HashSet<Long>();
                                                maps.forEach(map -> {
                                                    //目前只有部门， 先不考虑岗位
                                                    List<OrganizationMember> members = organizationProvider.listOrganizationMembersByOrgId(map.getGroupId());
                                                    if(members != null && members.size() > 0) {
                                                        members.forEach(member -> {
                                                            userIds.add(member.getTargetId());
                                                        });

                                                    }
                                                });
                                                if(userIds.size() > 0) {
                                                    String notifyText = getMessage(contract.getName(), plan.getPaidTime(), plan.getPaidAmount(), ContractNotificationTemplateCode.SCOPE, locale, ContractNotificationTemplateCode.NOTIFY_CONTRACT_PAY);
                                                    userIds.forEach(userId -> {
                                                        sendMessageToUser(userId, notifyText);
                                                    });
                                                }
                                            }
                                        }
                                        plan.setNotifyFlag((byte)1);
                                    }
                                });
                            }
                        });
                    }
                }
            });
        }
    }

    private String getMessage(String contractName, Timestamp time, BigDecimal amount, String scope, String locale, int code) {
        Map<String, Object> notifyMap = new HashMap<String, Object>();
        notifyMap.put("contractName", contractName);
        notifyMap.put("amount", amount);
        notifyMap.put("time", timeToStr(time));
        String notifyText = localeTemplateService.getLocaleTemplateString(scope, code, locale, notifyMap, "");
        return notifyText;
    }

    private String getNotifyMessage(String contractName, Timestamp time) {
        Map<String, Object> notifyMap = new HashMap<String, Object>();
        notifyMap.put("contractName", contractName);
        notifyMap.put("time", timeToStr(time));
        String notifyText = localeTemplateService.getLocaleTemplateString(ContractNotificationTemplateCode.SCOPE, ContractNotificationTemplateCode.NOTIFY_CONTRACT_EXPIRING, "zh_CN", notifyMap, "");
        return notifyText;
    }

    private String timeToStr(Timestamp time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(time);
    }

    private void sendMessageToUser(Long userId, String content) {
        MessageDTO messageDto = new MessageDTO();
        messageDto.setAppId(AppConstants.APPID_MESSAGING);
        messageDto.setSenderUid(User.SYSTEM_USER_LOGIN.getUserId());
        messageDto.setChannels(new MessageChannel(MessageChannelType.USER.getCode(), userId.toString()));
        messageDto.setChannels(new MessageChannel(MessageChannelType.USER.getCode(), Long.toString(User.SYSTEM_USER_LOGIN.getUserId())));
        messageDto.setBodyType(MessageBodyType.TEXT.getCode());
        messageDto.setBody(content);
        messageDto.setMetaAppId(AppConstants.APPID_MESSAGING);

        messagingService.routeMessage(User.SYSTEM_USER_LOGIN, AppConstants.APPID_MESSAGING, MessageChannelType.USER.getCode(),
                userId.toString(), messageDto, MessagingConstants.MSG_FLAG_STORED_PUSH.getCode());
    }

    private void dealAddressLivingStatus(Contract contract, byte livingStatus) {
        List<ContractBuildingMapping> mappings = contractBuildingMappingProvider.listByContract(contract.getId());
        boolean individualFlag = CustomerType.INDIVIDUAL.equals(CustomerType.fromStatus(contract.getCustomerType())) ? true : false;
        mappings.forEach(mapping -> {
            CommunityAddressMapping addressMapping = propertyMgrProvider.findAddressMappingByAddressId(mapping.getAddressId());
            addressMapping.setLivingStatus(livingStatus);
            propertyMgrProvider.updateOrganizationAddressMapping(addressMapping);

            if(individualFlag) {
                propertyMgrService.addAddressToOrganizationOwner(contract.getNamespaceId(), mapping.getAddressId(), contract.getCustomerId());
            }
        });
    }

    private Timestamp addPeriod(Timestamp startTime, int period, Byte unit) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startTime);
        if(PeriodUnit.MINUTE.equals(PeriodUnit.fromStatus(unit))) {
            calendar.add(Calendar.MINUTE, period);
        }
        else if(PeriodUnit.HOUR.equals(PeriodUnit.fromStatus(unit))) {
            calendar.add(Calendar.HOUR, period);
        }
        else if(PeriodUnit.DAY.equals(PeriodUnit.fromStatus(unit))) {
            calendar.add(Calendar.DATE, period);
        }
        else if(PeriodUnit.MONTH.equals(PeriodUnit.fromStatus(unit))) {
            calendar.add(Calendar.MONTH, period);
        }
        else if(PeriodUnit.YEAR.equals(PeriodUnit.fromStatus(unit))) {
            calendar.add(Calendar.YEAR, period);
        }
        Timestamp time = new Timestamp(calendar.getTimeInMillis());

        return time;
    }

}
