package com.everhomes.portal;

import com.everhomes.acl.WebMenuPrivilegeProvider;
import com.everhomes.acl.WebMenuScope;
import com.everhomes.community.Community;
import com.everhomes.community.CommunityProvider;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.rest.common.ServiceAllianceActionData;
import com.everhomes.rest.common.ServiceModuleConstants;
import com.everhomes.rest.portal.DetailFlag;
import com.everhomes.rest.portal.ServiceAllianceInstanceConfig;
import com.everhomes.rest.portal.ServiceAllianceJump;
import com.everhomes.rest.yellowPage.DisplayFlagType;
import com.everhomes.rest.yellowPage.YellowPageStatus;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.util.StringHelper;
import com.everhomes.yellowPage.ServiceAllianceCategories;
import com.everhomes.yellowPage.ServiceAllianceSkipRule;
import com.everhomes.yellowPage.ServiceAlliances;
import com.everhomes.yellowPage.YellowPageProvider;

import org.hamcrest.core.Is;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created by sfyan on 2017/8/30.
 */
@Component(PortalPublishHandler.PORTAL_PUBLISH_OBJECT_PREFIX + ServiceModuleConstants.SERVICE_ALLIANCE_MODULE)
public class ServiceAlliancePortalPublishHandler implements PortalPublishHandler{

    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceAlliancePortalPublishHandler.class);


    @Autowired
    private CommunityProvider communityProvider;

    @Autowired
    private YellowPageProvider yellowPageProvider;
    
    @Autowired
    private ConfigurationProvider configProvider;

	@Autowired
	private WebMenuPrivilegeProvider webMenuProvider;
	
    @Override
    public String publish(Integer namespaceId, String instanceConfig, String itemLabel) {
        ServiceAllianceInstanceConfig serviceAllianceInstanceConfig = (ServiceAllianceInstanceConfig)StringHelper.fromJsonString(instanceConfig, ServiceAllianceInstanceConfig.class);
        if(null == serviceAllianceInstanceConfig.getType()){
            ServiceAllianceCategories serviceAllianceCategories = createServiceAlliance(namespaceId, serviceAllianceInstanceConfig.getDetailFlag(), itemLabel);
            serviceAllianceInstanceConfig.setType(serviceAllianceCategories.getId());
        }else{
            updateServiceAlliance(namespaceId, serviceAllianceInstanceConfig.getType(), serviceAllianceInstanceConfig.getDetailFlag(), itemLabel);
        }
        return StringHelper.toJsonString(serviceAllianceInstanceConfig);
    }

    @Override
    public String getAppInstanceConfig(Integer namespaceId, String actionData) {
        ServiceAllianceActionData serviceAllianceActionData = (ServiceAllianceActionData)StringHelper.fromJsonString(actionData, ServiceAllianceActionData.class);
        ServiceAllianceSkipRule rule = yellowPageProvider.getCateorySkipRule(serviceAllianceActionData.getParentId(), namespaceId);
        ServiceAllianceInstanceConfig serviceAllianceInstanceConfig = new ServiceAllianceInstanceConfig();
        serviceAllianceInstanceConfig.setType(serviceAllianceActionData.getParentId());
        serviceAllianceInstanceConfig.setDisplayType(serviceAllianceActionData.getDisplayType());
        if(null == rule){
            serviceAllianceInstanceConfig.setDetailFlag(DetailFlag.NO.getCode());
        }else{
            serviceAllianceInstanceConfig.setDetailFlag(DetailFlag.YES.getCode());
        }
        return StringHelper.toJsonString(serviceAllianceInstanceConfig);
    }

    @Override
    public String getItemActionData(Integer namespaceId, String instanceConfig) {
        ServiceAllianceInstanceConfig serviceAllianceInstanceConfig = (ServiceAllianceInstanceConfig)StringHelper.fromJsonString(instanceConfig, ServiceAllianceInstanceConfig.class);
        ServiceAllianceActionData serviceAllianceActionData = new ServiceAllianceActionData();
        serviceAllianceActionData.setType(serviceAllianceInstanceConfig.getType());
        serviceAllianceActionData.setParentId(serviceAllianceInstanceConfig.getType());
        serviceAllianceActionData.setDisplayType(serviceAllianceInstanceConfig.getDisplayType());
        return StringHelper.toJsonString(serviceAllianceActionData);
    }

    private ServiceAllianceCategories createServiceAlliance(Integer namespaceId, Byte detailFlag, String name){
        User user = UserContext.current().getUser();
        ServiceAllianceCategories serviceAllianceCategories = new ServiceAllianceCategories();
        serviceAllianceCategories.setName(name);
        serviceAllianceCategories.setNamespaceId(namespaceId);
        serviceAllianceCategories.setParentId(0L);
        List<Community> communities = communityProvider.listCommunitiesByNamespaceId(namespaceId);
        if(null != communities && communities.size() > 0){
            Community community = communities.get(0);
            serviceAllianceCategories.setOwnerType("community");
            serviceAllianceCategories.setOwnerId(community.getId());
            serviceAllianceCategories.setCreatorUid(user.getId());
            serviceAllianceCategories.setDeleteUid(user.getId());
            serviceAllianceCategories.setStatus(YellowPageStatus.ACTIVE.getCode());
            serviceAllianceCategories.setEntryId(generateEntryId(namespaceId));
            yellowPageProvider.createServiceAllianceCategory(serviceAllianceCategories);

            ServiceAlliances serviceAlliances = new ServiceAlliances();
            serviceAlliances.setParentId(0L);
            serviceAlliances.setOwnerType("community");
            serviceAlliances.setOwnerId(community.getId());
            serviceAlliances.setName(name);
            serviceAlliances.setDisplayName(name);
            serviceAlliances.setType(serviceAllianceCategories.getId());
            serviceAlliances.setStatus(YellowPageStatus.ACTIVE.getCode());
            serviceAlliances.setAddress("");
            serviceAlliances.setSupportType((byte)0);
            serviceAlliances.setDisplayFlag(DisplayFlagType.SHOW.getCode());
            yellowPageProvider.createServiceAlliances(serviceAlliances);

            if(DetailFlag.fromCode(detailFlag) == DetailFlag.YES){
                ServiceAllianceSkipRule serviceAllianceSkipRule = new ServiceAllianceSkipRule();
                serviceAllianceSkipRule.setNamespaceId(namespaceId);
                serviceAllianceSkipRule.setServiceAllianceCategoryId(serviceAllianceCategories.getId());
                yellowPageProvider.createServiceAllianceSkipRule(serviceAllianceSkipRule);
            }
            
            boolean iscreateMenuScope = configProvider.getBooleanValue("portal.sa.create.scope", true);
            if(iscreateMenuScope){
            	createMenuScope(namespaceId,serviceAllianceCategories.getEntryId(),serviceAllianceCategories.getName());
            }
        }else{
            LOGGER.error("namespace not community. namespaceId = {}", namespaceId);
        }

        return serviceAllianceCategories;
    }

    private void createMenuScope(Integer namespaceId, Integer entryId, String name) {
		if(entryId == null || namespaceId == null){
			return ;
		}
		long parentMenuId = entryId*100+41600;
		ArrayList<WebMenuScope> newSocpes = new ArrayList<WebMenuScope>();
		ArrayList<Long> socpeIds = new ArrayList<Long>();
		Map<Long, WebMenuScope> map = webMenuProvider.getWebMenuScopeMapByOwnerId("EhNamespaces", Long.valueOf(namespaceId));
		boolean insertflag = false;
		for (int i = 0; i < 7; i++) {
			Long menuid = parentMenuId+i*10;
			newSocpes.add(generteObjWebMenuScope(namespaceId,name,menuid));
			WebMenuScope scope = map.get(Long.valueOf(menuid));
			if(scope == null){
				insertflag = true;
			}else{
				socpeIds.add(scope.getId());
			}
		}
		if(insertflag){
			webMenuProvider.deleteWebMenuScopes(socpeIds);
			webMenuProvider.createWebMenuScopes(newSocpes);
		}
	}

	private WebMenuScope generteObjWebMenuScope(Integer namespaceId, String name, Long menuid) {
		WebMenuScope socpe = new WebMenuScope();
		socpe.setMenuId(menuid);
		socpe.setMenuName(name);
		socpe.setOwnerId(Long.valueOf(namespaceId));
		socpe.setOwnerType("EhNamespaces");
		if(menuid%100 == 0){
			socpe.setApplyPolicy((byte)1);
		}else{
			socpe.setApplyPolicy((byte)2);
		}
		return socpe;
	}

	private Integer generateEntryId(Integer namespaceId) {
		if(namespaceId == null){
			LOGGER.error("service alliance generateEntryId namespaceId "+namespaceId);
			return null;
		}
    	 List<Integer> entryIds = yellowPageProvider.listAscEntryIds(namespaceId);
    	 for (int i = 0; i < entryIds.size(); i++) {
			if(entryIds.get(i)!=i+1){
				return i+1;
			}
		}
    	 int maxEntryId = configProvider.getIntValue("portal.max.entryid", 30);
    	 if(entryIds.size() == maxEntryId){
    		 LOGGER.error("service alliance maxEntryId exceed "+maxEntryId);
    		 return null;
    	 }
    	return entryIds.size()+1;
	}

	private ServiceAllianceCategories updateServiceAlliance(Integer namespaceId, Long type, Byte detailFlag, String name){
        ServiceAllianceCategories serviceAllianceCategories = yellowPageProvider.findCategoryById(type);
        List<Community> communities = communityProvider.listCommunitiesByNamespaceId(namespaceId);
        if(null != communities && communities.size() > 0 && null != serviceAllianceCategories){
            Community community = communities.get(0);
            serviceAllianceCategories.setName(name);
            if(serviceAllianceCategories.getEntryId() == null){
            	serviceAllianceCategories.setEntryId(generateEntryId(namespaceId));
            	 boolean iscreateMenuScope = configProvider.getBooleanValue("portal.sa.create.scope", true);
                 if(iscreateMenuScope){
                	 createMenuScope(namespaceId, serviceAllianceCategories.getEntryId(), name);
                 }
            }
            yellowPageProvider.updateServiceAllianceCategory(serviceAllianceCategories);

            ServiceAlliances serviceAlliances = yellowPageProvider.queryServiceAllianceTopic("community", community.getId(), type);
            if(null != serviceAlliances){
                serviceAlliances.setName(name);
                serviceAlliances.setDisplayName(name);
                yellowPageProvider.updateServiceAlliances(serviceAlliances);
            }else{
                LOGGER.error("serviceAlliances is null. communityId = {}, type = {}", community.getId(), type);
            }

            ServiceAllianceSkipRule rule = yellowPageProvider.getCateorySkipRule(type);
            if(DetailFlag.fromCode(detailFlag) == DetailFlag.YES){
                if(null == rule){
                    ServiceAllianceSkipRule serviceAllianceSkipRule = new ServiceAllianceSkipRule();
                    serviceAllianceSkipRule.setNamespaceId(namespaceId);
                    serviceAllianceSkipRule.setServiceAllianceCategoryId(serviceAllianceCategories.getId());
                }
            }else{
                if(null != rule){
                    yellowPageProvider.deleteServiceAllianceSkipRule(rule.getId());
                }
            }
        }else{
            LOGGER.error("namespace not community or service alliance category is null. namespaceId = {}, type = {}", namespaceId, type);
        }
        return serviceAllianceCategories;
    }

    private void updateJumps(Integer namespaceId, List<ServiceAllianceJump> jumps){
        //暂时不做
    }

    @Override
    public String processInstanceConfig(String instanceConfig) {
        return instanceConfig;
    }
}
