package com.everhomes.portal;

import com.alibaba.fastjson.JSONObject;
import com.everhomes.acl.WebMenuPrivilegeProvider;
import com.everhomes.acl.WebMenuScope;
import com.everhomes.bigcollection.Accessor;
import com.everhomes.bigcollection.BigCollectionProvider;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.naming.NameMapper;
import com.everhomes.organization.OrganizationProvider;
import com.everhomes.rest.common.ServiceAllianceActionData;
import com.everhomes.rest.common.ServiceModuleConstants;
import com.everhomes.rest.organization.OrganizationGroupType;
import com.everhomes.rest.portal.DetailFlag;
import com.everhomes.rest.portal.ServiceAllianceInstanceConfig;
import com.everhomes.rest.portal.ServiceAllianceJump;
import com.everhomes.rest.print.PrintErrorCode;
import com.everhomes.rest.yellowPage.AllianceDisplayType;
import com.everhomes.rest.yellowPage.DisplayFlagType;
import com.everhomes.rest.yellowPage.ServiceAllianceBelongType;
import com.everhomes.rest.yellowPage.YellowPageStatus;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.tables.pojos.EhServiceAllianceCategories;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.util.StringHelper;
import com.everhomes.yellowPage.AllianceStandardService;
import com.everhomes.yellowPage.ServiceAllianceCategories;
import com.everhomes.yellowPage.ServiceAlliances;
import com.everhomes.yellowPage.YellowPageProvider;
import com.everhomes.yellowPage.YellowPageService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
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
    
	@Autowired
	AllianceStandardService allianceStandardService;
	
	@Autowired
	private YellowPageService yellowPageService;

    @Override
    public String publish(Integer namespaceId, String instanceConfig, String itemLabel, HandlerPublishCommand cmd) {
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
    public String getAppInstanceConfig(Integer namespaceId, String actionData, HandlerGetAppInstanceConfigCommand cmd) {
        ServiceAllianceActionData serviceAllianceActionData = (ServiceAllianceActionData)StringHelper.fromJsonString(actionData, ServiceAllianceActionData.class);
        ServiceAllianceInstanceConfig serviceAllianceInstanceConfig = new ServiceAllianceInstanceConfig();
        serviceAllianceInstanceConfig.setType(serviceAllianceActionData.getParentId());
        serviceAllianceInstanceConfig.setEntryId(serviceAllianceActionData.getParentId());
        serviceAllianceInstanceConfig.setDisplayType(serviceAllianceActionData.getDisplayType());
        serviceAllianceInstanceConfig.setEnableComment(serviceAllianceActionData.getEnableComment());
        serviceAllianceInstanceConfig.setEnableProvider(serviceAllianceActionData.getEnableProvider());
        return StringHelper.toJsonString(serviceAllianceInstanceConfig);
    }

    /* 
     * 获取需要展示在客户端的config
     */
    @Override
	public String getItemActionData(Integer namespaceId, String instanceConfig, HandlerGetItemActionDataCommand cmd) {
    	
		ServiceAllianceInstanceConfig config = (ServiceAllianceInstanceConfig) StringHelper
				.fromJsonString(instanceConfig, ServiceAllianceInstanceConfig.class);

		if ("native".equals(config.getAppType())) {
			return buildNativeActionData(namespaceId, config);
		}

		JSONObject json = new JSONObject();
		String pageRealDisplayType = config.getDisplayType();
		if (AllianceDisplayType.HOUSE_KEEPER.getCode().equals(config.getDisplayType())) {
			pageRealDisplayType = AllianceDisplayType.HOUSE_KEEPER.getShowType();
		}

		json.put("url", yellowPageService.buildAllianceUrl(namespaceId, config, pageRealDisplayType));
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


	private ServiceAllianceCategories createServiceAlliance(Integer namespaceId, Byte detailFlag, String name) {
		User user = UserContext.current().getUser();
		ServiceAllianceCategories serviceAllianceCategories = new ServiceAllianceCategories();
		serviceAllianceCategories.setName(name);
		serviceAllianceCategories.setNamespaceId(namespaceId);
		serviceAllianceCategories.setParentId(0L);
		serviceAllianceCategories.setOwnerType(ServiceAllianceBelongType.ORGANAIZATION.getCode());
		serviceAllianceCategories.setOwnerId(-1L);
		serviceAllianceCategories.setCreatorUid(user.getId());
		serviceAllianceCategories.setDeleteUid(user.getId());
		serviceAllianceCategories.setStatus(YellowPageStatus.ACTIVE.getCode());

		long id = this.sequenceProvider
				.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhServiceAllianceCategories.class));
		serviceAllianceCategories.setId(id);
		serviceAllianceCategories.setEntryId(generateEntryId(namespaceId, id));
		serviceAllianceCategories.setType(id);
		yellowPageProvider.createServiceAllianceCategory(serviceAllianceCategories);

		boolean iscreateMenuScope = configProvider.getBooleanValue("portal.sa.create.scope", true);
		if (iscreateMenuScope) {
			createMenuScope(namespaceId, serviceAllianceCategories.getEntryId(), serviceAllianceCategories.getName());
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
	private void updateServiceAlliance(Integer namespaceId, ServiceAllianceInstanceConfig config, String name) {

		Long type = config.getType();

		// 不清楚这个是做什么的
		boolean iscreateMenuScope = configProvider.getBooleanValue("portal.sa.create.scope", true);
		if (iscreateMenuScope) {
			createMenuScope(namespaceId, generateEntryId(namespaceId, type), name);
		}

		allianceStandardService.updateHomePageCategorysByPublish(config, name);
		return;
	}

    private void updateJumps(Integer namespaceId, List<ServiceAllianceJump> jumps){
        //暂时不做
    }

    @Override
    public String processInstanceConfig(Integer namespaceId, String instanceConfig, HandlerProcessInstanceConfigCommand cmd) {
        return instanceConfig;
    }
    
    final Pattern pattern = Pattern.compile("^.*\"type\":[\\s]*([\\d]*)");
    @Override
    public String getCustomTag(Integer namespaceId, Long moudleId, String instanceConfig, HandlerGetCustomTagCommand cmd) {
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
       String categoriesId = this.getCustomTag(namespaceId,moudleId,instanceConfig, null);
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
