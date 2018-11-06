package com.everhomes.customer;

import com.everhomes.activity.ActivityProivider;
import com.everhomes.activity.ActivityRoster;
import com.everhomes.bus.LocalBus;
import com.everhomes.bus.LocalBusSubscriber;
import com.everhomes.bus.LocalEvent;
import com.everhomes.bus.LocalEventBus;
import com.everhomes.bus.SystemEvent;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.entity.EntityType;
import com.everhomes.general_approval.GeneralApprovalVal;
import com.everhomes.general_approval.GeneralApprovalValProvider;
import com.everhomes.general_form.GeneralFormVal;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.queue.taskqueue.WorkerPoolFactory;
import com.everhomes.rest.approval.CommonStatus;
import com.everhomes.rest.approval.TrueOrFalseFlag;
import com.everhomes.rest.customer.PotentialCustomerType;
import com.everhomes.rest.general_approval.GeneralFormDataSourceType;
import com.everhomes.rest.general_approval.PostApprovalFormTextValue;
import com.everhomes.user.UserIdentifier;
import com.everhomes.user.UserProvider;
import com.everhomes.util.ExecutorUtil;
import com.everhomes.yellowPage.ServiceAlliances;
import com.everhomes.yellowPage.YellowPageProvider;
import com.google.gson.Gson;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Rui.Jia  2018/6/28 17 :06
 */
@Component
public class PotentialCustomerListener implements LocalBusSubscriber, ApplicationListener<ContextRefreshedEvent> {
    @Autowired
    private LocalBus localBus;
    @Autowired
    private WorkerPoolFactory workerPoolFactory;
    @Autowired
    private GeneralApprovalValProvider generalApprovalValProvider;
    @Autowired
    private EnterpriseCustomerProvider customerProvider;
    @Autowired
    private ActivityProivider activityProivider;
    @Autowired
    private OrganizationProvider organizationProvider;
    @Autowired
    private YellowPageProvider yellowPageProvider;
    @Autowired
    private UserProvider userProvider;
    @Value("${equipment.ip}")
    private String equipmentIp;

    @Autowired
    private ConfigurationProvider configurationProvider;

    private static final Logger LOGGER = LoggerFactory.getLogger(PotentialCustomerListener.class);

    @Override
    public Action onLocalBusMessage(Object sender, String subject, Object args, String subscriptionPath) {
        try {
            ExecutorUtil.submit(() -> {
                LOGGER.info("potentialCustomerListener start run.....");
                LocalEvent localEvent = (LocalEvent) args;
                syncActivityAndServiceAllianceData(localEvent);
            });
        } catch (Exception e) {
            LOGGER.error("onLocalBusMessage error ", e);
        }
        return Action.none;
    }

    private void syncActivityAndServiceAllianceData(LocalEvent localEvent) {
        if (SystemEvent.ACTIVITY_ACTIVITY_ROSTER_CREATE.dft().equals(localEvent.getEventName())) {
            syncActivityData(localEvent);
        } else if (SystemEvent.SERVICE_ALLIANCE_CREATE.dft().equals(localEvent.getEventName())) {
            syncServiceAlliance(localEvent);
        }
    }

    private void syncActivityData(LocalEvent localEvent) {
        //sync  activities
        ActivityRoster activityRoster = (ActivityRoster) localEvent.getParams().get(EntityType.ACTIVITY_ROSTER.getCode());
        Integer activityNamespaceId = localEvent.getContext().getNamespaceId();
        CustomerConfiguration activityConfigutations = customerProvider.getSyncCustomerConfiguration(activityNamespaceId,PotentialCustomerType.ACTIVITY.getCode());
        if (activityConfigutations != null && TrueOrFalseFlag.TRUE.equals(TrueOrFalseFlag.fromCode(activityConfigutations.getValue()))) {
            if (activityRoster != null) {
                CustomerPotentialData existPotentialCustomer = customerProvider.findPotentialCustomerByName(activityRoster.getOrganizationName());
                Long dataId = 0L;
                if (existPotentialCustomer == null) {
                    CustomerPotentialData data = new CustomerPotentialData();
                    data.setName(activityRoster.getOrganizationName());
                    data.setNamespaceId(activityNamespaceId);
                    Long sourceId = (Long) localEvent.getParams().get("categoryId");
                    data.setSourceId(sourceId);
                    data.setSourceType(PotentialCustomerType.ACTIVITY.getValue());
                    customerProvider.createPotentialCustomer(data);
                    dataId = data.getId();
                }else {
                    dataId = existPotentialCustomer.getId();
                }

                UserIdentifier userIdentifier = userProvider.findClaimedIdentifierByToken(activityNamespaceId, activityRoster.getPhone());
                // customer talent info
                CustomerTalent talent = new CustomerTalent();
                List<EnterpriseCustomer> customers = customerProvider.listEnterpriseCustomerByNamespaceIdAndName(activityNamespaceId, activityRoster.getOrganizationName());
                if(customers!=null && customers.size()>0){
                    talent.setCustomerId(customers.get(0).getId());
                }else {
                    talent.setCustomerId(0L);
                }
                talent.setName(activityRoster.getRealName());
                talent.setPhone(activityRoster.getPhone());
                talent.setOriginSourceId(dataId);
                talent.setOriginSourceType(PotentialCustomerType.ACTIVITY.getValue());
                talent.setTalentSourceItemId((Long) localEvent.getParams().get("categoryId"));
                if (userIdentifier != null) {
                    talent.setRegisterStatus(CommonStatus.ACTIVE.getCode());
                } else {
                    talent.setRegisterStatus(CommonStatus.INACTIVE.getCode());
                }
                talent.setNamespaceId(activityNamespaceId);
                customerProvider.createCustomerTalent(talent);
            }
        }
    }

    private void syncServiceAlliance(LocalEvent localEvent) {
        List<GeneralFormVal> valsList = (List<GeneralFormVal>) localEvent.getParams().get(EntityType.GENERAL_FORM_VAL.getCode());
        String allianceTextValue = null;
        String allianceSourceText = null;
        String alliancePhone = null;
        String allianceName = null;
        for (GeneralFormVal val : valsList) {
            if (GeneralFormDataSourceType.USER_COMPANY.equals(GeneralFormDataSourceType.fromCode(val.getFieldName()))) {
                allianceTextValue = val.getFieldValue();
            }
            if (GeneralFormDataSourceType.SOURCE_ID.equals(GeneralFormDataSourceType.fromCode(val.getFieldName()))) {
                allianceSourceText = val.getFieldValue();
            }
            if (GeneralFormDataSourceType.USER_PHONE.equals(GeneralFormDataSourceType.fromCode(val.getFieldName()))) {
                alliancePhone = val.getFieldValue();
            }
            if (GeneralFormDataSourceType.USER_NAME.equals(GeneralFormDataSourceType.fromCode(val.getFieldName()))) {
                allianceName = val.getFieldValue();
            }
        }
        Integer namespaceId = localEvent.getContext().getNamespaceId();
        PostApprovalFormTextValue textValue = new Gson().fromJson(allianceTextValue, PostApprovalFormTextValue.class);
        PostApprovalFormTextValue sourceId = new Gson().fromJson(allianceSourceText, PostApprovalFormTextValue.class);
        PostApprovalFormTextValue userPhone = new Gson().fromJson(alliancePhone, PostApprovalFormTextValue.class);
        PostApprovalFormTextValue userName = new Gson().fromJson(allianceName, PostApprovalFormTextValue.class);
        // text value is enterprise name we need  sync to customer  potential  temp tables
        CustomerConfiguration configutations = customerProvider.getSyncCustomerConfiguration(namespaceId,PotentialCustomerType.SERVICE_ALLIANCE.getCode());
        if (configutations != null && TrueOrFalseFlag.TRUE.equals(TrueOrFalseFlag.fromCode(configutations.getValue()))) {
            if (textValue != null && StringUtils.isNotBlank(textValue.getText())) {
                if (sourceId != null && StringUtils.isNotBlank(sourceId.getText())) {
                    ServiceAlliances serviceAlliance = yellowPageProvider.findServiceAllianceById(Long.valueOf(sourceId.getText()), null, null);
                    if (serviceAlliance == null) {
                        LOGGER.error("service alliance does't exist!");
                        return;
                    }
                    Long dataId;
                    CustomerPotentialData existPotentialCustomer = customerProvider.findPotentialCustomerByName(textValue.getText());
                    if (existPotentialCustomer == null) {
                        // sync to customer potential tables
                        CustomerPotentialData data = new CustomerPotentialData();
                        data.setName(textValue.getText());
                        data.setSourceType(PotentialCustomerType.SERVICE_ALLIANCE.getValue());

                        data.setSourceId(serviceAlliance.getParentId());
                        data.setNamespaceId(namespaceId);
                        customerProvider.createPotentialCustomer(data);
                        dataId = data.getId();
                    } else {
                        dataId = existPotentialCustomer.getId();
                    }
                    //sync requestor info to customer talent
                    if (StringUtils.isNotBlank(userPhone.getText())) {
                            CustomerTalent talent = new CustomerTalent();
                            List<EnterpriseCustomer> customers = customerProvider.listEnterpriseCustomerByNamespaceIdAndName(namespaceId, textValue.getText());
                            if(customers!=null && customers.size()>0){
                                talent.setCustomerId(customers.get(0).getId());
                            }else {
                                talent.setCustomerId(0L);
                            }
                            talent.setName(userName.getText());
                            talent.setPhone(userPhone.getText());
                            talent.setOriginSourceId(dataId);
                            talent.setOriginSourceType(PotentialCustomerType.SERVICE_ALLIANCE.getValue());
                            talent.setTalentSourceItemId(serviceAlliance.getParentId());
                            talent.setNamespaceId(namespaceId);
                            customerProvider.createCustomerTalent(talent);
//                        }
                    }
                }
            }
        }
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        if (contextRefreshedEvent.getApplicationContext().getParent() == null) {
//            String taskServer = configurationProvider.getValue(ConfigConstants.TASK_SERVER_ADDRESS, "127.0.0.1");
            LOGGER.info("potential customer listener start ...equipmentIp:{}");
//            if (taskServer.equals(equipmentIp)) {
                LocalEventBus.subscribe(SystemEvent.ACTIVITY_ACTIVITY_ROSTER_CREATE.dft(), this);
                LocalEventBus.subscribe(SystemEvent.SERVICE_ALLIANCE_CREATE.dft(), this);
//            }
        }
    }
}
