package com.everhomes.portal;

import com.alibaba.fastjson.JSONObject;
import com.everhomes.acl.WebMenuPrivilegeProvider;
import com.everhomes.acl.WebMenuScope;
import com.everhomes.bigcollection.Accessor;
import com.everhomes.bigcollection.BigCollectionProvider;
import com.everhomes.community.Community;
import com.everhomes.community.CommunityProvider;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.listing.CrossShardListingLocator;
import com.everhomes.naming.NameMapper;
import com.everhomes.organization.Organization;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.rest.common.ServiceAllianceActionData;
import com.everhomes.rest.common.ServiceModuleConstants;
import com.everhomes.rest.organization.OrganizationGroupType;
import com.everhomes.rest.portal.DetailFlag;
import com.everhomes.rest.portal.ServiceAllianceInstanceConfig;
import com.everhomes.rest.portal.ServiceAllianceJump;
import com.everhomes.rest.print.PrintErrorCode;
import com.everhomes.rest.yellowPage.DisplayFlagType;
import com.everhomes.rest.yellowPage.ServiceAllianceBelongType;
import com.everhomes.rest.yellowPage.ServiceAllianceOwnerType;
import com.everhomes.rest.yellowPage.YellowPageStatus;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.tables.pojos.EhServiceAllianceCategories;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.util.StringHelper;
import com.everhomes.yellowPage.ServiceAllianceCategories;
import com.everhomes.yellowPage.ServiceAllianceSkipRule;
import com.everhomes.yellowPage.ServiceAlliances;
import com.everhomes.yellowPage.YellowPageProvider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by sfyan on 2017/8/30.
 */
@Component(PortalPublishHandler.PORTAL_PUBLISH_OBJECT_PREFIX + ServiceModuleConstants.SERVICE_ALLIANCE_MODULE)
public class ServiceAlliancePortalPublishHandler implements PortalPublishHandler{

    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceAlliancePortalPublishHandler.class);


    @Autowired
    private OrganizationProvider organizationProvider;

    @Autowired
    private YellowPageProvider yellowPageProvider;
    
    @Autowired
    private ConfigurationProvider configProvider;

	@Autowired
	private WebMenuPrivilegeProvider webMenuProvider;
	
	@Autowired
	private BigCollectionProvider bigCollectionProvider;

    @Autowired
    private SequenceProvider sequenceProvider;
    @Override
    public String publish(Integer namespaceId, String instanceConfig, String itemLabel) {
        ServiceAllianceInstanceConfig serviceAllianceInstanceConfig = (ServiceAllianceInstanceConfig)StringHelper.fromJsonString(instanceConfig, ServiceAllianceInstanceConfig.class);
        if(null == serviceAllianceInstanceConfig.getType()){
            ServiceAllianceCategories serviceAllianceCategories = createServiceAlliance(namespaceId, serviceAllianceInstanceConfig.getDetailFlag(), itemLabel);
            serviceAllianceInstanceConfig.setType(serviceAllianceCategories.getId());
        }else{
            updateServiceAlliance(namespaceId, serviceAllianceInstanceConfig, itemLabel);
        }
        
        return StringHelper.toJsonString(serviceAllianceInstanceConfig);
    }

    /* 
     * actionData 为业务配置时在前端传入
     * 最后返回业务需要保存的config
     * 注：当该域空间没有layout的时候会调用
     */
    @Override
    public String getAppInstanceConfig(Integer namespaceId, String actionData) {
        ServiceAllianceActionData serviceAllianceActionData = (ServiceAllianceActionData)StringHelper.fromJsonString(actionData, ServiceAllianceActionData.class);
        ServiceAllianceSkipRule rule = yellowPageProvider.getCateorySkipRule(serviceAllianceActionData.getParentId(), namespaceId);
        ServiceAllianceInstanceConfig serviceAllianceInstanceConfig = new ServiceAllianceInstanceConfig();
        serviceAllianceInstanceConfig.setType(serviceAllianceActionData.getParentId());
        serviceAllianceInstanceConfig.setEntryId(serviceAllianceActionData.getParentId());
        serviceAllianceInstanceConfig.setDisplayType(serviceAllianceActionData.getDisplayType());
        serviceAllianceInstanceConfig.setEnableComment(serviceAllianceActionData.getEnableComment());
        serviceAllianceInstanceConfig.setEnableProvider(serviceAllianceActionData.getEnableProvider());
        
        if(null == rule){
            serviceAllianceInstanceConfig.setDetailFlag(DetailFlag.NO.getCode());
        }else{
            serviceAllianceInstanceConfig.setDetailFlag(DetailFlag.YES.getCode());
        }
        return StringHelper.toJsonString(serviceAllianceInstanceConfig);
    }

    /* 
     * 获取需要展示在客户端的config
     */
    @Override
	public String getItemActionData(Integer namespaceId, String instanceConfig) {
    	
		ServiceAllianceInstanceConfig config = (ServiceAllianceInstanceConfig) StringHelper
				.fromJsonString(instanceConfig, ServiceAllianceInstanceConfig.class);
		
		if ("native".equals(config.getAppType())) {
			return buildNativeActionData(namespaceId, config);
		}
		
		JSONObject json = new JSONObject();
		if (namespaceId == 999953) { // 先在万智汇上做测试
			json.put("realm", config.getRealm());
			json.put("entryUrl", buildRenderUrl(namespaceId, config));
		} else {
			 json.put("url", buildRenderUrl(namespaceId, config));
		}
	
		return json.toJSONString();
	}
    
    
	private String buildNativeActionData(Integer namespaceId, ServiceAllianceInstanceConfig config) {
		
		ServiceAllianceActionData serviceAllianceActionData = new ServiceAllianceActionData();
		serviceAllianceActionData.setType(config.getType());
		serviceAllianceActionData.setParentId(config.getType());
		serviceAllianceActionData.setDisplayType(config.getDisplayType());
		serviceAllianceActionData.setEnableComment(config.getEnableComment());
		return StringHelper.toJsonString(serviceAllianceActionData);
	}

	private String buildRenderUrl(Integer namespaceId, ServiceAllianceInstanceConfig config) {

		// 服务联盟v3.4 web化之后，直接设置为跳转链接即可
		// http://dev15.zuolin.com/service-alliance-web/build/index.html#/home/filterlist?displayType=filterlist&parentId=213729&enableComment=1#sign_suffix
		StringBuilder url = new StringBuilder();
		String homeUrl = configProvider.getValue(namespaceId, "home.url", "");
		url.append(homeUrl+"/service-alliance-web/build/index.html#/home/" + config.getDisplayType());
		url.append("?displayType=" + config.getDisplayType());
		url.append("&parentId=" + config.getType());
		url.append("&enableComment=" + config.getEnableComment());
		url.append("&ns=" + namespaceId);
		url.append("#sign_suffix");

		return url.toString();
	}

    private ServiceAllianceCategories createServiceAlliance(Integer namespaceId, Byte detailFlag, String name){
        User user = UserContext.current().getUser();
        ServiceAllianceCategories serviceAllianceCategories = new ServiceAllianceCategories();
        serviceAllianceCategories.setName(name);
        serviceAllianceCategories.setNamespaceId(namespaceId);
        serviceAllianceCategories.setParentId(0L);
        List<Organization> organizations = organizationProvider.listEnterpriseByNamespaceIds(namespaceId, "PM",null,null,new CrossShardListingLocator(), 10);
//        List<Community> communities = communityProvider.listCommunitiesByNamespaceId(namespaceId);
//        if(null != communities && communities.size() > 0){
        if(null != organizations && organizations.size() > 0){
//            Community community = communities.get(0);
        	Organization organization = organizations.get(0);
            serviceAllianceCategories.setOwnerType(ServiceAllianceBelongType.ORGANAIZATION.getCode());
            serviceAllianceCategories.setOwnerId(organization.getId());
            serviceAllianceCategories.setCreatorUid(user.getId());
            serviceAllianceCategories.setDeleteUid(user.getId());
            serviceAllianceCategories.setStatus(YellowPageStatus.ACTIVE.getCode());

            long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhServiceAllianceCategories.class));
            serviceAllianceCategories.setId(id);
            serviceAllianceCategories.setEntryId(generateEntryId(namespaceId,id));
            yellowPageProvider.createServiceAllianceCategory(serviceAllianceCategories);

            ServiceAlliances serviceAlliances = new ServiceAlliances();
            serviceAlliances.setParentId(0L);
            serviceAlliances.setOwnerType(ServiceAllianceBelongType.ORGANAIZATION.getCode());
            serviceAlliances.setOwnerId(organization.getId());
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
            LOGGER.error("namespace not pm. namespaceId = {}", namespaceId);
        }

        return serviceAllianceCategories;
    }

    private void createMenuScope(Integer namespaceId, Integer entryId, String name) {
		if(entryId == null || namespaceId == null){
            return ;
        }
        clearMenuScope(namespaceId);
        String rkey = "sa_menuscope_"+entryId+"_"+namespaceId+"_"+Thread.currentThread().getId();
		if(needCreateMenu(namespaceId,entryId,name,rkey)) {
            long parentMenuId = entryId * 100 + 41600;
            ArrayList<WebMenuScope> newSocpes = new ArrayList<WebMenuScope>();
            ArrayList<Long> socpeIds = new ArrayList<Long>();
            for (int i = 0; i < 7; i++) {
                Long menuid = parentMenuId + i * 10;
                newSocpes.add(generteObjWebMenuScope(namespaceId, name, menuid));
            }
            webMenuProvider.createWebMenuScopes(newSocpes);
            hasCreatedMenu(namespaceId,entryId,name,rkey);
        }
	}

    private void hasCreatedMenu(Integer namespaceId, Integer entryId, String name,String rkey) {
        ValueOperations<String, String> valuemap = getValueOperations(rkey);
        valuemap.set(rkey,"1",20, TimeUnit.MINUTES);
    }

    private boolean needCreateMenu(Integer namespaceId, Integer entryId, String name,String rkey) {
        ValueOperations<String, String> valuemap = getValueOperations(rkey);
        String value = valuemap.get(rkey);
        if(value == null){
            return true;
        }
        return false;
    }

    private void clearMenuScope(Integer namespaceId) {
		String rkey = "sa_menuscope_"+namespaceId+"_"+Thread.currentThread().getId();
		ValueOperations<String, String> valuemap = getValueOperations(rkey);
		String value = valuemap.get(rkey);
		if(value == null){
			value = "1";
			valuemap.set(rkey, value, 20, TimeUnit.MINUTES);
			int maxEntryId = configProvider.getIntValue("portal.max.entryid", 30);
			ArrayList<Integer> socpeIds = new ArrayList<>(maxEntryId*7);
			for (int i = 0; i < maxEntryId; i++) {
				socpeIds.add(41700+i*100);
				socpeIds.add(41700+i*100+10);
				socpeIds.add(41700+i*100+20);
				socpeIds.add(41700+i*100+30);
				socpeIds.add(41700+i*100+40);
				socpeIds.add(41700+i*100+50);
				socpeIds.add(41700+i*100+60);
			}
			webMenuProvider.deleteWebMenuScopesByMenuIdAndNamespace(socpeIds,namespaceId);
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

	private Integer generateEntryId(Integer namespaceId,Long category_id) {
		if(namespaceId == null || category_id == null){
			LOGGER.error("service alliance generateEntryId namespaceId "+namespaceId);
			return null;
		}
        String rkey = "sa_entryid_"+namespaceId+"_"+Thread.currentThread().getId();
        int currentEntryId = clearEntryId(namespaceId,rkey);
        Integer originEntryId = getEntryIdByCategoryId(namespaceId,category_id);
        if(originEntryId != null)
            return originEntryId;
        int maxEntryId = configProvider.getIntValue("portal.max.entryid", 30);
    	if(currentEntryId == maxEntryId){
            LOGGER.error("service alliance maxEntryId exceed "+maxEntryId);
    		return null;
    	}
        currentEntryId++;
    	putNewEntryIdToRedis(currentEntryId,rkey,namespaceId,category_id);
    	return currentEntryId;
	}

    private void putNewEntryIdToRedis(Integer newentryId,String rkey, Integer namespaceId, Long category_id) {
        String entryKey = "sa_entryid_"+category_id+"_"+namespaceId+"_"+Thread.currentThread().getId();
        ValueOperations<String, String> valuemap = getValueOperations(entryKey);
        valuemap.set(entryKey,String.valueOf(newentryId),20, TimeUnit.MINUTES);

        ValueOperations<String, String> valuemap2 = getValueOperations(rkey);
        valuemap2.set(rkey,String.valueOf(newentryId),20, TimeUnit.MINUTES);
    }

    private Integer getEntryIdByCategoryId(Integer namespaceId, Long category_id) {
        String entryKey = "sa_entryid_"+category_id+"_"+namespaceId+"_"+Thread.currentThread().getId();
        ValueOperations<String, String> valuemap = getValueOperations(entryKey);
        String value = valuemap.get(entryKey);
        if(value == null){
            return null;
        }
        return Integer.valueOf(value);
    }

    private int clearEntryId(Integer namespaceId,String rkey) {
		 ValueOperations<String, String> valuemap = getValueOperations(rkey);
		 String value = valuemap.get(rkey);
        if(value == null){
        	value = "0";
        	valuemap.set(rkey, value, 20, TimeUnit.MINUTES);
        	yellowPageProvider.updateEntryIdNullByNamespaceId(namespaceId);
        }
        return Integer.valueOf(value);
	}

	/**
	 * 更新服务联盟配置项，可以在发布时，
	 * 将业务的配置，使用这个方法进行更新
	 * @param namespaceId
	 * @param config
	 * @param name
	 * @return
	 */
	private ServiceAllianceCategories updateServiceAlliance(Integer namespaceId, ServiceAllianceInstanceConfig config, String name){
		
		Long type = config.getType();
		Byte detailFlag = config.getDetailFlag();
		Byte enableComment = config.getEnableComment(); //是否允许评论
		Byte enableProvider = config.getEnableProvider(); //是否打开供应商开关
		
        ServiceAllianceCategories serviceAllianceCategories = yellowPageProvider.findCategoryById(type);
//        List<Community> communities = communityProvider.listCommunitiesByNamespaceId(namespaceId);
        List<Organization> organizations = organizationProvider.listEnterpriseByNamespaceIds(namespaceId, "PM", null,null,new CrossShardListingLocator(), 10);
        if(null != organizations && organizations.size() > 0 && null != serviceAllianceCategories){
        	Organization organization = organizations.get(0);
            serviceAllianceCategories.setName(name);
            serviceAllianceCategories.setPath(name);
            serviceAllianceCategories.setEntryId(generateEntryId(namespaceId,type));
            boolean iscreateMenuScope = configProvider.getBooleanValue("portal.sa.create.scope", true);
            if(iscreateMenuScope){
                createMenuScope(namespaceId, serviceAllianceCategories.getEntryId(), name);
            }
            yellowPageProvider.updateServiceAllianceCategory(serviceAllianceCategories);

           
            ServiceAlliances serviceAlliances = yellowPageProvider.queryServiceAllianceTopic(ServiceAllianceBelongType.ORGANAIZATION.getCode(), organization.getId(), type);
            if(null != serviceAlliances){
            	//更新现有配置
                serviceAlliances.setName(name);
                serviceAlliances.setDisplayName(name);
                serviceAlliances.setEnableComment(enableComment);
                serviceAlliances.setEnableProvider(enableProvider);
                yellowPageProvider.updateServiceAlliances(serviceAlliances);
                
            }else{
                LOGGER.error("serviceAlliances is null. pmId = {}, type = {}", organization.getId(), type);
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
    
    final Pattern pattern = Pattern.compile("^.*\"type\":[\\s]*([\\d]*)");
    @Override
    public String getCustomTag(Integer namespaceId, Long moudleId, String instanceConfig) {
        LOGGER.info("ServiceAlliancePortalPublishHandler instanceConfig = {}",instanceConfig);
        if(instanceConfig == null || instanceConfig.length() == 0){
            return null;
        }
        Matcher m = pattern.matcher(instanceConfig);
        if(m.find()){
            return m.group(1);
        }
    	return null;
    }

    @Override
    public Long getWebMenuId(Integer namespaceId, Long moudleId, String instanceConfig) {
       String categoriesId = this.getCustomTag(namespaceId,moudleId,instanceConfig);
       ServiceAllianceCategories category = yellowPageProvider.findCategoryById(Long.valueOf(categoriesId));
       if(category == null || category.getEntryId() == null){
           return null;
       }
       return 41600L+category.getEntryId()*100L;
    }
	
    /**
	 * 获取key在redis操作的valueOperations
	 */
	private ValueOperations<String, String> getValueOperations(String key) {
		final StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
		Accessor acc = this.bigCollectionProvider.getMapAccessor(key, "");
		RedisTemplate redisTemplate = acc.getTemplate(stringRedisSerializer);
		ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
		return valueOperations;
	}


	/**
	 * 清除redis中key的缓存
	 */
	private void deleteValueOperations(String key) {
		final StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
		Accessor acc = this.bigCollectionProvider.getMapAccessor(key, "");
		RedisTemplate redisTemplate = acc.getTemplate(stringRedisSerializer);
		redisTemplate.delete(key);
	}
}
