package com.everhomes.customer;

import com.everhomes.activity.Activity;
import com.everhomes.activity.ActivityProivider;
import com.everhomes.activity.ActivityRoster;
import com.everhomes.bus.LocalBus;
import com.everhomes.bus.LocalBusSubscriber;
import com.everhomes.db.DaoAction;
import com.everhomes.db.DaoHelper;
import com.everhomes.general_form.GeneralFormVal;
import com.everhomes.general_form.GeneralFormValProvider;
import com.everhomes.queue.taskqueue.WorkerPoolFactory;
import com.everhomes.rest.approval.TrueOrFalseFlag;
import com.everhomes.rest.general_approval.GeneralFormDataSourceType;
import com.everhomes.rest.general_approval.PostApprovalFormTextValue;
import com.everhomes.server.schema.tables.EhActivityRoster;
import com.everhomes.server.schema.tables.pojos.EhGeneralFormVals;
import com.everhomes.util.ExecutorUtil;
import com.google.gson.Gson;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;


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
    private GeneralFormValProvider generalFormValProvider;
    @Autowired
    private EnterpriseCustomerProvider customerProvider;
    @Autowired
    private ActivityProivider activityProivider;

    private static final String queueName = "potential_customer";
    private static final Logger LOGGER = LoggerFactory.getLogger(PotentialCustomerListener.class);

    @Override
    public Action onLocalBusMessage(Object o, String s, Object o1, String s1) {
        try {
            ExecutorUtil.submit(() -> {
                LOGGER.info("potentialCustomerListener start run.....");
                Long id = (Long) o1;
                if (null == id) {
                    LOGGER.error("None of activities or service allience info id ......");
                } else {
                    try {
                        syncActivityAndServiceAllienceData(id, s);
                    } catch (Exception e) {
                        LOGGER.error("execute potentialCustomerListener error service id =" + id, e);
                    }
                }
            });
        } catch (Exception e) {
            LOGGER.error("onLocalBusMessage error ", e);
        }
        return Action.none;
    }

    private void syncActivityAndServiceAllienceData(Long id, String s) {
        Integer namespaceId;
        if (s.contains(com.everhomes.server.schema.tables.pojos.EhActivityRoster.class.getSimpleName())) {
            //sync  activities
            ActivityRoster activityRoster = activityProivider.findRosterById(id);
            Long activityId = activityRoster == null ? 0 : activityRoster.getActivityId();
            Activity activity = activityProivider.findActivityById(activityId);
            Integer activityNamespaceId = activity == null ? 0 : activity.getNamespaceId();
            CustomerConfiguration activityConfigutations = customerProvider.getSyncCustomerConfiguration(activityNamespaceId);
            if (activityConfigutations != null && TrueOrFalseFlag.TRUE.equals(TrueOrFalseFlag.fromCode(activityConfigutations.getValue()))) {
                //todo: sync this record to customer potencial customer temp tables
            }
        } else if (s.contains(EhGeneralFormVals.class.getSimpleName())) {
            GeneralFormVal generalFormValObject = generalFormValProvider.getGeneralFormValById(id);
            if (generalFormValObject != null && GeneralFormDataSourceType.USER_COMPANY.equals(GeneralFormDataSourceType.fromCode(generalFormValObject.getFieldName()))) {
                LOGGER.info("current service allience data is not user company");
                return;
            }
            String allienceTextValue = generalFormValObject == null ? null : generalFormValObject.getFieldValue();
            namespaceId = generalFormValObject == null ? 0 : generalFormValObject.getNamespaceId();
            if (StringUtils.isNotBlank(allienceTextValue)) {
                PostApprovalFormTextValue textValue = new Gson().fromJson(allienceTextValue, PostApprovalFormTextValue.class);
            }
            // text value is enterprise name we need  sync to customer  potential  temp tables
            CustomerConfiguration configutations = customerProvider.getSyncCustomerConfiguration(namespaceId);
            if (configutations != null && TrueOrFalseFlag.TRUE.equals(TrueOrFalseFlag.fromCode(configutations.getValue()))) {
                // sync to customer potential tables
                //todo: sync this record to customer potencial customer temp tables
            }
        }
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        if (contextRefreshedEvent.getApplicationContext().getParent() == null) {
            String activitySubcribeKey = DaoHelper.getDaoActionPublishSubject(DaoAction.CREATE, EhActivityRoster.class, null);
            String serviceAllienceSubcribeKey = DaoHelper.getDaoActionPublishSubject(DaoAction.CREATE, EhGeneralFormVals.class, null);
            localBus.subscribe(activitySubcribeKey, this);
            localBus.subscribe(serviceAllienceSubcribeKey, this);
            workerPoolFactory.getWorkerPool().addQueue(queueName);
        }
    }
}
