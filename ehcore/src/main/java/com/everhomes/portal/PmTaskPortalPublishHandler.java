package com.everhomes.portal;

import com.alibaba.fastjson.JSONObject;
import com.everhomes.acl.WebMenu;
import com.everhomes.acl.WebMenuPrivilegeProvider;
import com.everhomes.pmtask.PmTaskProvider;
import com.everhomes.rest.acl.WebMenuType;
import com.everhomes.rest.common.ServiceModuleConstants;
import com.everhomes.rest.flow.FlowConstants;
import com.everhomes.rest.pmtask.PmTaskAppType;
import com.everhomes.rest.portal.PmTaskInstanceConfig;
import com.everhomes.util.StringHelper;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

/**
 * Created by sfyan on 2017/8/30.
 */
@Component(PortalPublishHandler.PORTAL_PUBLISH_OBJECT_PREFIX + ServiceModuleConstants.PM_TASK_MODULE)
public class PmTaskPortalPublishHandler implements PortalPublishHandler{

    private static final Logger LOGGER = LoggerFactory.getLogger(PmTaskPortalPublishHandler.class);


    @Autowired
    private PmTaskProvider pmTaskProvider;
    @Autowired
    private WebMenuPrivilegeProvider webMenuProvider;

    @Override
    public String publish(Integer namespaceId, String instanceConfig, String itemLabel) {
        PmTaskInstanceConfig pmTaskInstanceConfig = (PmTaskInstanceConfig)StringHelper.fromJsonString(instanceConfig, PmTaskInstanceConfig.class);
//        if(null == pmTaskInstanceConfig.getTaskCategoryId()){
//            RentalResourceType rentalResourceType = createRentalResourceType(namespaceId, itemLabel, rentalInstanceConfig.getPageType());
//            rentalInstanceConfig.setResourceTypeId(rentalResourceType.getId());
//        }else{
//            updateRentalResourceType(namespaceId, rentalInstanceConfig.getResourceTypeId(), rentalInstanceConfig.getPageType(), itemLabel);
//        }
        return StringHelper.toJsonString(pmTaskInstanceConfig);
    }

    @Override
    public String getAppInstanceConfig(Integer namespaceId, String actionData) {
        return actionData;
    }

    @Override
    public String getItemActionData(Integer namespaceId, String instanceConfig) {
        return instanceConfig;
    }

//    private RentalResourceType createPmTask(Integer namespaceId, String name, Byte type){
//        RentalResourceType rentalResourceType = new RentalResourceType();
//        rentalResourceType.setNamespaceId(namespaceId);
//        rentalResourceType.setName(name);
//        if(null == pageType){
//            pageType = 0;
//        }
//        rentalResourceType.setPageType(pageType);
//        rentalResourceType.setStatus(ResourceTypeStatus.NORMAL.getCode());
//        rentalv2Provider.createRentalResourceType(rentalResourceType);
//        return rentalResourceType;
//    }
//
//    private RentalResourceType updateRentalResourceType(Integer namespaceId, Long resourceTypeId, Byte pageType, String name){
//        RentalResourceType rentalResourceType = rentalv2Provider.findRentalResourceTypeById(resourceTypeId);
//        if(null != rentalResourceType){
//            if(null == pageType){
//                pageType = 0;
//            }
//            rentalResourceType.setPageType(pageType);
//            rentalResourceType.setName(name);
//            rentalv2Provider.updateRentalResourceType(rentalResourceType);
//        }else{
//            LOGGER.error("rental resource type is null. resourceTypeId = {}", resourceTypeId);
//        }
//        return rentalResourceType;
//    }

    @Override
    public String processInstanceConfig(String instanceConfig) {
        return instanceConfig;
    }

    @Override
    public String getCustomTag(Integer namespaceId, Long moudleId, String actionData, String instanceConfig){

        if(moudleId.equals(FlowConstants.PM_TASK_MODULE)){
            JSONObject json = JSONObject.parseObject(actionData);
            String url = json.getString("url");
            String[] arrs = url.split("&");
            for (String s: arrs) {
                int spe = s.indexOf("=");
                String key = s.substring(0, spe);
                String value = s.substring(spe + 1);
                if ("taskCategoryId".equals(key)) {
                    return value;
                }
            }
        }

        return actionData;
    }

    public Long getWebMenuId(Integer namespaceId, Long moudleId, String actionData, String instanceConfig){
        if(moudleId.equals(FlowConstants.PM_TASK_MODULE)){
            String taskCategoryId = getCustomTag(namespaceId, moudleId, actionData, instanceConfig);
            if (Long.valueOf(taskCategoryId) == PmTaskAppType.REPAIR_ID) {
                return 20100L;
            }else if (Long.valueOf(taskCategoryId) == PmTaskAppType.SUGGESTION_ID) {
                return 20230L;
            }
        }

        return null;
    }
}
