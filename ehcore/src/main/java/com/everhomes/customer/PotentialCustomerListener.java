package com.everhomes.customer;

import com.everhomes.activity.Activity;
import com.everhomes.activity.ActivityProivider;
import com.everhomes.activity.ActivityRoster;
import com.everhomes.bus.LocalBus;
import com.everhomes.bus.LocalBusSubscriber;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.general_approval.GeneralApprovalVal;
import com.everhomes.general_approval.GeneralApprovalValProvider;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.queue.taskqueue.WorkerPoolFactory;
import com.everhomes.rest.approval.TrueOrFalseFlag;
import com.everhomes.rest.customer.PotentialCustomerType;
import com.everhomes.rest.general_approval.GeneralFormDataSourceType;
import com.everhomes.rest.general_approval.PostApprovalFormTextValue;
import com.everhomes.server.schema.tables.EhActivityRoster;
import com.everhomes.server.schema.tables.pojos.EhGeneralApprovalVals;
import com.everhomes.user.User;
import com.everhomes.user.UserProvider;
import com.everhomes.util.ExecutorUtil;
import com.everhomes.yellowPage.ServiceAlliances;
import com.everhomes.yellowPage.YellowPageProvider;
import com.google.gson.Gson;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

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

    private static final Logger LOGGER = LoggerFactory.getLogger(PotentialCustomerListener.class);

    @Override
    public Action onLocalBusMessage(Object o, String s, Object o1, String s1) {
        try {
            ExecutorUtil.submit(() -> {
                LOGGER.info("potentialCustomerListener start run.....");
                Long id = (Long) o1;
                if (null == id) {
                    LOGGER.error("None of activities or service alliance info id ......");
                } else {
                    syncActivityAndServiceAllianceData(id, s);
                }
            });
        } catch (Exception e) {
            LOGGER.error("onLocalBusMessage error ", e);
        }
        return Action.none;
    }

    private void syncActivityAndServiceAllianceData(Long id, String s) {
        if (s.contains(com.everhomes.server.schema.tables.pojos.EhActivityRoster.class.getSimpleName())) {
            syncActivityData(id);
        } else if (s.contains(EhGeneralApprovalVals.class.getSimpleName())) {
            syncServiceAlliance(id);
        }
    }

    private void syncActivityData(Long id) {
        //sync  activities
        ActivityRoster activityRoster = activityProivider.findRosterById(id);
        Long activityId = activityRoster == null ? 0 : activityRoster.getActivityId();
        Activity activity = activityProivider.findActivityById(activityId);
        Integer activityNamespaceId = activity == null ? 0 : activity.getNamespaceId();
        CustomerConfiguration activityConfigutations = customerProvider.getSyncCustomerConfiguration(activityNamespaceId);
        if (activityConfigutations != null && TrueOrFalseFlag.TRUE.equals(TrueOrFalseFlag.fromCode(activityConfigutations.getValue()))) {
            if (activityRoster != null) {
                CustomerPotentialData existPotentialCustomer = customerProvider.findPotentialCustomerByName(activityRoster.getOrganizationName());
                Long dataId = 0L;
                if(existPotentialCustomer==null){
                    CustomerPotentialData data = new CustomerPotentialData();
                    data.setName(activityRoster.getOrganizationName());
                    if (activity != null) {
                        data.setSourceId(activity.getCategoryId());
                    }
                    data.setSourceType(PotentialCustomerType.ACTIVITY.getValue());
                    data.setSourceType(PotentialCustomerType.SERVICE_ALLIANCE.getValue());
                    customerProvider.createPotentialCustomer(data);
                    dataId = data.getId();
                }else {
                    dataId = existPotentialCustomer.getId();
                }

                User user = userProvider.findUserById(activityRoster.getUid());
                if (user != null) {
                    // customer talent info
                    CustomerTalent talent = new CustomerTalent();
                    talent.setCustomerId(0L);
                    talent.setName(user.getNickName());
                    talent.setPhone(user.getIdentifierToken());
                    talent.setOriginSourceId(dataId);
                    customerProvider.createCustomerTalent(talent);
                }
            }
        }
    }

    private void syncServiceAlliance(Long id) {
        Integer namespaceId;GeneralApprovalVal generaApporvalObject = generalApprovalValProvider.getGeneralApprovalById(id);
        String allianceTextValue = null;
        String allianceSourceText = null;
        String alliancePhone = null;
        if (GeneralFormDataSourceType.USER_COMPANY.equals(GeneralFormDataSourceType.fromCode(generaApporvalObject.getFieldName()))) {
            allianceTextValue = generaApporvalObject.getFieldStr3();
        }
        if(GeneralFormDataSourceType.SOURCE_ID.equals(GeneralFormDataSourceType.fromCode(generaApporvalObject.getFieldName()))){
            allianceSourceText = generaApporvalObject.getFieldStr3();
        }
        if(GeneralFormDataSourceType.USER_PHONE.equals(GeneralFormDataSourceType.fromCode(generaApporvalObject.getFieldName()))){
            alliancePhone = generaApporvalObject.getFieldStr3();
        }

        namespaceId = generaApporvalObject.getNamespaceId();
        if (StringUtils.isBlank(allianceTextValue)||StringUtils.isBlank(allianceSourceText)) {
            return;
        }
        PostApprovalFormTextValue textValue = new Gson().fromJson(allianceTextValue, PostApprovalFormTextValue.class);
        PostApprovalFormTextValue sourceId = new Gson().fromJson(allianceSourceText, PostApprovalFormTextValue.class);
        PostApprovalFormTextValue userPhone = new Gson().fromJson(alliancePhone, PostApprovalFormTextValue.class);
        // text value is enterprise name we need  sync to customer  potential  temp tables
        CustomerConfiguration configutations = customerProvider.getSyncCustomerConfiguration(namespaceId);
        if (configutations != null && TrueOrFalseFlag.TRUE.equals(TrueOrFalseFlag.fromCode(configutations.getValue()))) {
            CustomerPotentialData existPotentialCustomer = customerProvider.findPotentialCustomerByName(textValue.getText());
            Long dataId = 0L;
            if (existPotentialCustomer == null) {
                ServiceAlliances serviceAlliance = yellowPageProvider.findServiceAllianceById(Long.valueOf(sourceId.getText()), null, null);
                if (serviceAlliance == null) {
                    LOGGER.error("service alliance does't exist!");
                    return;
                }
                // sync to customer potential tables
                CustomerPotentialData data = new CustomerPotentialData();
                data.setName(textValue.getText());
                data.setSourceType(PotentialCustomerType.SERVICE_ALLIANCE.getValue());
                data.setSourceId(serviceAlliance.getParentId());
                data.setNamespaceId(generaApporvalObject.getNamespaceId());
                customerProvider.createPotentialCustomer(data);
                dataId = data.getId();
            }else {
                dataId = existPotentialCustomer.getId();
            }
            //sync requestor info to customer talent
            if (StringUtils.isNotBlank(userPhone.getText())) {
               List<User> users = userProvider.listUserByKeyword(userPhone.getText(), namespaceId, new CrossShardListingLocator(), 2);
               if(users!=null && users.size()>0){
                   CustomerTalent talent = new CustomerTalent();
                   talent.setCustomerId(0L);
                   talent.setName(users.get(0).getNickName());
                   talent.setPhone(userPhone.getText());
                   talent.setOriginSourceId(dataId);
                   customerProvider.createCustomerTalent(talent);
               }
            }
        }
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        if (contextRefreshedEvent.getApplicationContext().getParent() == null) {
            String activitySubcribeKey = DaoHelper.getDaoActionPublishSubject(DaoAction.CREATE, EhActivityRoster.class, null);
            String serviceallianceSubcribeKey = DaoHelper.getDaoActionPublishSubject(DaoAction.CREATE, EhGeneralApprovalVals.class, null);
            localBus.subscribe(activitySubcribeKey, this);
            localBus.subscribe(serviceallianceSubcribeKey, this);
        }
    }
}
