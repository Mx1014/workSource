package com.everhomes.portal;

import com.everhomes.acl.WebMenu;
import com.everhomes.acl.WebMenuPrivilegeProvider;
import com.everhomes.rentalv2.RentalResourceType;
import com.everhomes.rentalv2.Rentalv2Provider;
import com.everhomes.rest.acl.WebMenuType;
import com.everhomes.rest.common.RentalActionData;
import com.everhomes.rest.common.ServiceModuleConstants;
import com.everhomes.rest.portal.RentalInstanceConfig;
import com.everhomes.rest.rentalv2.RentalV2ResourceType;
import com.everhomes.rest.rentalv2.admin.ResourceTypeStatus;
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
@Component(PortalPublishHandler.PORTAL_PUBLISH_OBJECT_PREFIX + ServiceModuleConstants.RENTAL_MODULE)
public class RentalPortalPublishHandler implements PortalPublishHandler{

    private static final Logger LOGGER = LoggerFactory.getLogger(RentalPortalPublishHandler.class);


    @Autowired
    private Rentalv2Provider rentalv2Provider;
    @Autowired
    private WebMenuPrivilegeProvider webMenuProvider;


    @Override
    public String publish(Integer namespaceId, String instanceConfig, String itemLabel) {
        RentalInstanceConfig rentalInstanceConfig = (RentalInstanceConfig)StringHelper.fromJsonString(instanceConfig, RentalInstanceConfig.class);
        if(null == rentalInstanceConfig.getResourceTypeId()){
            createRentalResourceType(namespaceId, itemLabel, rentalInstanceConfig);
        }else{
            updateRentalResourceType(namespaceId, rentalInstanceConfig,itemLabel);
        }
        return StringHelper.toJsonString(rentalInstanceConfig);
    }

    @Override
    public String getAppInstanceConfig(Integer namespaceId, String actionData) {
        return actionData;
    }

    @Override
    public String getItemActionData(Integer namespaceId, String instanceConfig) {
        return instanceConfig;
    }

    private RentalResourceType createRentalResourceType(Integer namespaceId, String name,RentalInstanceConfig rentalInstanceConfig){
        RentalResourceType rentalResourceType = new RentalResourceType();
        rentalResourceType.setNamespaceId(namespaceId);
        rentalResourceType.setName(name);
        if(null == rentalInstanceConfig.getPageType()){
            rentalInstanceConfig.setPageType((byte)0);
        }
        if (null == rentalInstanceConfig.getPayMode())
            rentalInstanceConfig.setPayMode((byte)0);

        if (StringUtils.isBlank(rentalInstanceConfig.getIdentify()))
            rentalInstanceConfig.setIdentify(RentalV2ResourceType.DEFAULT.getCode());
        if (null == rentalInstanceConfig.getUnauthVisible())
            rentalInstanceConfig.setUnauthVisible((byte)0);

        rentalResourceType.setPageType(rentalInstanceConfig.getPageType());
        rentalResourceType.setPayMode(rentalInstanceConfig.getPayMode());
        rentalResourceType.setIdentify(rentalInstanceConfig.getIdentify());
        rentalResourceType.setStatus(ResourceTypeStatus.NORMAL.getCode());
        rentalResourceType.setUnauthVisible(rentalInstanceConfig.getUnauthVisible());
        rentalv2Provider.createRentalResourceType(rentalResourceType);
        rentalInstanceConfig.setResourceTypeId(rentalResourceType.getId());
        return rentalResourceType;
    }

    private RentalResourceType updateRentalResourceType(Integer namespaceId,RentalInstanceConfig rentalInstanceConfig, String name){
        RentalResourceType rentalResourceType = rentalv2Provider.findRentalResourceTypeById(rentalInstanceConfig.getResourceTypeId());
        if(null != rentalResourceType){
            if(null == rentalInstanceConfig.getPageType()){
                rentalResourceType.setPageType((byte)0);
            }
            if (null != rentalInstanceConfig.getPayMode())
                rentalResourceType.setPayMode(rentalInstanceConfig.getPayMode());
            if (!StringUtils.isBlank(rentalInstanceConfig.getIdentify()))
                rentalResourceType.setIdentify(rentalInstanceConfig.getIdentify());
            if (null != rentalInstanceConfig.getUnauthVisible())
                rentalResourceType.setUnauthVisible(rentalInstanceConfig.getUnauthVisible());
            rentalResourceType.setName(name);
            rentalv2Provider.updateRentalResourceType(rentalResourceType);
        }else{
            LOGGER.error("rental resource type is null. resourceTypeId = {}", rentalInstanceConfig.getResourceTypeId());
        }
        return rentalResourceType;
    }

    @Override
    public String processInstanceConfig(Integer namespaceId,String instanceConfig) {
        return instanceConfig;
    }

    @Override
    public String getCustomTag(Integer namespaceId, Long moudleId, String instanceConfig) {
        RentalActionData actionDataObject = (RentalActionData)StringHelper.fromJsonString(instanceConfig,RentalActionData.class);
        if (actionDataObject!=null && actionDataObject.getResourceTypeId()!=null)
            return String.valueOf(actionDataObject.getResourceTypeId());
        return null;
    }

    @Override
    public Long getWebMenuId(Integer namespaceId, Long moudleId, String instanceConfig) {
        List<WebMenu> menus = webMenuProvider.listWebMenuByType(WebMenuType.PARK.getCode(), null, "/40000%", Collections.singletonList(40400L));
        if (menus!=null && menus.size()>0)
            return menus.get(0).getId();

        return null;
    }
}
