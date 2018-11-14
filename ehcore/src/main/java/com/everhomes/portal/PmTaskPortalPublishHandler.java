package com.everhomes.portal;

import com.alibaba.fastjson.JSONObject;
import com.everhomes.acl.WebMenuPrivilegeProvider;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.pmtask.PmTaskProvider;
import com.everhomes.rest.common.ServiceModuleConstants;
import com.everhomes.rest.flow.FlowConstants;
import com.everhomes.rest.pmtask.PmTaskAppType;
import com.everhomes.rest.portal.*;
import com.everhomes.util.StringHelper;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

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
    @Autowired
    private ConfigurationProvider configurationProvider;

    public final static String SEPARATOR = "/";

    @Override
    public String publish(Integer namespaceId, String instanceConfig, String itemLabel, HandlerPublishCommand cmd) {
        PmTaskInstanceConfig pmTaskInstanceConfig = (PmTaskInstanceConfig)StringHelper.fromJsonString(instanceConfig, PmTaskInstanceConfig.class);

        Long taskCategoryId = pmTaskInstanceConfig.getTaskCategoryId();
        Byte agentSwitch = pmTaskInstanceConfig.getAgentSwitch();
        Byte feeModel = pmTaskInstanceConfig.getFeeModel();
        Byte appAgentSwitch = pmTaskInstanceConfig.getAppAgentSwitch();
        Byte bgAgentSwitch =pmTaskInstanceConfig.getBgAgentSwitch();

        pmTaskInstanceConfig.setAppId(cmd.getAppOriginId());

        if(null == taskCategoryId){
            if(999983 == namespaceId)
                taskCategoryId = 1L;
            else
                taskCategoryId = 6L;
            pmTaskInstanceConfig.setTaskCategoryId(taskCategoryId);
        }
        if(null == agentSwitch){
            agentSwitch = (byte)1;
            pmTaskInstanceConfig.setAgentSwitch(agentSwitch);
        }
        if(null == appAgentSwitch){
            appAgentSwitch = agentSwitch;
            pmTaskInstanceConfig.setAppAgentSwitch(appAgentSwitch);
        }
        if(null == bgAgentSwitch){
            bgAgentSwitch = agentSwitch;
            pmTaskInstanceConfig.setBgAgentSwitch(bgAgentSwitch);
        }
        if(null == feeModel){
            feeModel = (byte)0;
            pmTaskInstanceConfig.setFeeModel(feeModel);
        }

        if(0 == agentSwitch.byteValue()){
            configurationProvider.setIntValue(namespaceId.intValue(),"pmtask.hide.represent." + cmd.getAppOriginId(),1);
        } else if (1 == agentSwitch.byteValue()){
            configurationProvider.setIntValue(namespaceId.intValue(),"pmtask.hide.represent." + cmd.getAppOriginId(),0);
        }

        if(0 == appAgentSwitch.byteValue()){
            configurationProvider.setIntValue(namespaceId.intValue(),"pmtask.hide.represent.app" + cmd.getAppOriginId(),1);
        } else if (1 == appAgentSwitch.byteValue()){
            configurationProvider.setIntValue(namespaceId.intValue(),"pmtask.hide.represent.app" + cmd.getAppOriginId(),0);
        }

        if(0 == bgAgentSwitch.byteValue()){
            configurationProvider.setIntValue(namespaceId.intValue(),"pmtask.hide.represent.bg" + cmd.getAppOriginId(),1);
        } else if (1 == bgAgentSwitch.byteValue()){
            configurationProvider.setIntValue(namespaceId.intValue(),"pmtask.hide.represent.bg" + cmd.getAppOriginId(),0);
        }
        configurationProvider.setValue(namespaceId.intValue(),"pmtask.feeModel." + cmd.getAppOriginId(),feeModel.toString());

        String homeUrl = configurationProvider.getValue(namespaceId,"home.url","");
        String Uri = configurationProvider.getValue(namespaceId,"pmtask.uri","property-repair-web/build/index.html?ns=%s&type=user&taskCategoryId=%s&displayName=%s&appId=%s#home#sign_suffix");
        String Url = homeUrl + SEPARATOR + Uri;
        String displayname;
        if(StringUtils.isBlank(itemLabel)){
            displayname = "物业报修";
        } else {
            displayname = itemLabel;
        }
        try {
            displayname = URLEncoder.encode(displayname,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            LOGGER.error("url encode error.");
        }
        Url = String.format(Url,namespaceId,taskCategoryId,displayname,cmd.getAppOriginId());
        pmTaskInstanceConfig.setUrl(Url);
        return StringHelper.toJsonString(pmTaskInstanceConfig);
    }

    @Override
    public String getAppInstanceConfig(Integer namespaceId, String actionData, HandlerGetAppInstanceConfigCommand cmd) {
        return actionData;
    }

    @Override
    public String getItemActionData(Integer namespaceId, String instanceConfig, HandlerGetItemActionDataCommand cmd) {
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
    public String processInstanceConfig(Integer namespaceId, String instanceConfig, HandlerProcessInstanceConfigCommand cmd) {
        return instanceConfig;
    }

    @Override
    public String getCustomTag(Integer namespaceId, Long moudleId, String instanceConfig, HandlerGetCustomTagCommand cmd){

        if(moudleId.equals(FlowConstants.PM_TASK_MODULE)){
            PmTaskInstanceConfig pmTaskInstanceConfig = (PmTaskInstanceConfig)StringHelper.fromJsonString(instanceConfig, PmTaskInstanceConfig.class);
            Long taskCategoryId = pmTaskInstanceConfig.getTaskCategoryId();
            if(null != taskCategoryId){
                return String.valueOf(taskCategoryId);
            }
//            else{
//                String str = json.getString("url");
//                String[] strs = str.split("&");
//                for(String elem : strs){
//                    if(elem.indexOf("taskCategoryId") > -1){
//                        String[] pair = elem.split("=");
//                        return pair[1];
//                    }
//                }
//            }
        }

        //edit by 马世亨
        return null;
    }

    public Long getWebMenuId(Integer namespaceId, Long moudleId, String actionData, String instanceConfig){
        if(moudleId.equals(FlowConstants.PM_TASK_MODULE)){
            String taskCategoryId = getCustomTag(namespaceId, moudleId, instanceConfig, null);
            if (Long.valueOf(taskCategoryId) == PmTaskAppType.REPAIR_ID) {
                return 20100L;
            }else if (Long.valueOf(taskCategoryId) == PmTaskAppType.SUGGESTION_ID) {
                return 20230L;
            }
        }

        return null;
    }
}
