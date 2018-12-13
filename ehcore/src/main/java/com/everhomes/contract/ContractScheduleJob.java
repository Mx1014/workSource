package com.everhomes.contract;

import com.everhomes.community.Community;
import com.everhomes.community.CommunityProvider;
import com.everhomes.equipment.EquipmentService;
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
import com.everhomes.organization.pm.PropertyMgrService;
import com.everhomes.rest.app.AppConstants;
import com.everhomes.rest.common.ServiceModuleConstants;
import com.everhomes.rest.contract.ContractNotificationTemplateCode;
import com.everhomes.rest.contract.ContractParamGroupType;
import com.everhomes.rest.contract.ContractStatus;
import com.everhomes.rest.contract.PeriodUnit;
import com.everhomes.rest.customer.CustomerType;
import com.everhomes.rest.messaging.MessageBodyType;
import com.everhomes.rest.messaging.MessageChannel;
import com.everhomes.rest.messaging.MessageDTO;
import com.everhomes.rest.messaging.MessagingConstants;
import com.everhomes.rest.organization.OrganizationDTO;
import com.everhomes.rest.organization.pm.AddressMappingStatus;
import com.everhomes.rest.user.MessageChannelType;
import com.everhomes.search.ContractSearcher;
import com.everhomes.user.User;
import com.everhomes.util.DateHelper;
import com.everhomes.util.StringHelper;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by ying.xiong on 2017/8/26.
 */
@Component
@Scope("prototype")
public class ContractScheduleJob extends QuartzJobBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(ContractScheduleJob.class);

    public static final String SCHEDELE_NAME = "contract-";

<<<<<<< HEAD
    //public static String CRON_EXPRESSION = "0 0 2 * * ?";
    public static String CRON_EXPRESSION = "0 30 2 * * ? ";
    //public static String CRON_EXPRESSION = "0/10 * *  * * ?";
=======
    public static String CRON_EXPRESSION = "0 0 2 * * ?";
    //public static String CRON_EXPRESSION = "0 10 * * * ?";

    @Autowired
    private ScheduleProvider scheduleProvider;
>>>>>>> asset7.4

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

    @Autowired
    private EquipmentService equipmentService;

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

                ContractParam communityExist = contractProvider.findContractParamByCommunityId(community.getNamespaceId(), communityId, null,null,null,null);
                if(communityExist == null && communityId != null) {
                    // find the organization of this community
                    OrganizationDTO owner = equipmentService.getAuthOrgByProjectIdAndModuleId(communityId, community.getNamespaceId(), ServiceModuleConstants.CONTRACT_MODULE);
                    if(owner!=null)
                    communityExist = contractProvider.findContractParamByCommunityId(community.getNamespaceId(), null,null,owner.getId(),null,null);
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
                                    SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
                                    Date date = new Date();
                                    try {
                                        date = df.parse(df.format(contract.getContractEndDate()));
                                    } catch (ParseException e) {
                                        LOGGER.error("contract end date ");
                                    }

                                    Timestamp contractEndTime = new Timestamp(date.getTime());
                                    if(time.after(contractEndTime)) {
                                        contract.setStatus(ContractStatus.EXPIRING.getCode());
                                        contractProvider.updateContract(contract);
                                        contractSearcher.feedDoc(contract);
                                    }

                                    Timestamp notifyTime = addPeriod(now, param.getNotifyPeriod(), param.getNotifyUnit());
                                    if(notifyTime.after(contractEndTime)) {
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
                                            LOGGER.debug("ContractScheduleJob userIds: {}", StringHelper.toJsonString(userIds));
                                            if(userIds.size() > 0) {
                                                String notifyText = getNotifyMessage(contract.getName(), contract.getContractEndDate());
                                                userIds.forEach(userId -> {
                                                    sendMessageToUser (userId, notifyText);
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
                                                	List<OrganizationMember> members = null;
                                                	if (map.getGroupId()!=null && map.getGroupId()!=0) {
                                                		//按部门找人
                                                		members = organizationProvider.listOrganizationMembersByOrgId(map.getGroupId());
                                                		findUser(userIds, members);
													}else if(map.getPositionId()!=null && map.getPositionId()!=0){
														//按岗位找人
														members = organizationProvider.listOrganizationMembersByOrgId(map.getPositionId());
														findUser(userIds, members);
													}else if(map.getUserId()!= null && map.getUserId()!=0){
														//直接找到人
														userIds.add(map.getUserId());
													}
                                                    
                                                });
                                                LOGGER.debug("ContractScheduleJob userIds: {}", StringHelper.toJsonString(userIds));
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
    
    private void findUser(Set<Long> userIds,List<OrganizationMember> members){
    	if(members != null && members.size() > 0) {
            members.forEach(member -> {
                userIds.add(member.getTargetId());
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
        LOGGER.debug("contractScheduleJob sendMessageToUser, userId: {}, content: {}", userId, content);
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
