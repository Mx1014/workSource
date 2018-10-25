// @formatter:off
package com.everhomes.activity;

import com.everhomes.contentserver.ContentServerService;
import com.everhomes.entity.EntityType;
import com.everhomes.naming.NameMapper;
import com.everhomes.portal.PortalPublishHandler;
import com.everhomes.rest.activity.ActivityActionData;
import com.everhomes.rest.activity.ActivityCategoryDTO;
import com.everhomes.rest.activity.ActivityEntryConfigulation;
import com.everhomes.rest.approval.TrueOrFalseFlag;
import com.everhomes.rest.common.AllFlagType;
import com.everhomes.rest.common.ServiceModuleConstants;
import com.everhomes.sequence.SequenceProvider;
import com.everhomes.server.schema.tables.pojos.EhActivityCategories;
import com.everhomes.user.UserContext;
import com.everhomes.util.ConvertHelper;
import com.everhomes.util.StringHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Component(PortalPublishHandler.PORTAL_PUBLISH_OBJECT_PREFIX + ServiceModuleConstants.ACTIVITY_MODULE)
public class ActivityPortalPublishHandler implements PortalPublishHandler {

    private static final Logger LOGGER=LoggerFactory.getLogger(ActivityPortalPublishHandler.class);
    

	@Autowired
	private ActivityProivider activityProvider;

	@Autowired
	private ContentServerService contentServerService;

	@Autowired
	private SequenceProvider sequenceProvider;

	/**
	 * * 发布具体模块的内容
	 * 注：instanceConfig就是跟web端协商好的json字符串，需要解析后添加或者修改（数据是否有id来判断添加或者修改）到对应的业务表里面去,
	 * 如果是对应的配置项要添加到业务表，则会生成id，需要把生成的id字段回填到instanceConfig，再次发布的时候能通过id找到对应的业务数据，
	 * 然后进行修改，如果是修改，则通过id直接找到对应业务数据修改即可
	 * @param namespaceId
	 * @param instanceConfig 具体模块配置的参数
	 * @return
	 */
	public String publish(Integer namespaceId, String instanceConfig, String appName){

		LOGGER.info("ActivityPortalPublishHandler publish start namespaceId = {}, instanceConfig = {}, itemLabel = {}", namespaceId, instanceConfig, appName);

		ActivityEntryConfigulation config = (ActivityEntryConfigulation)StringHelper.fromJsonString(instanceConfig, ActivityEntryConfigulation.class);

		//关联到主页签活动
		if(com.everhomes.rest.common.TrueOrFalseFlag.TRUE.getCode().equals(config.getIndexFlag())){
			config.setCategoryId(1L);
		}

		//保存应用入口的信息，不存在则新增，存在则更新
		ActivityCategories activityCategory = saveEntry(namespaceId, config.getCategoryId(), appName);

		//将值组装到config中，用于后面返回服务广场
		config.setId(activityCategory.getId());
		config.setCategoryId(activityCategory.getEntryId());

		//新增、更新内容分类
		saveContencategory(config, namespaceId);
		//updateContentCategory(config, activityCategory, namespaceId);

		LOGGER.info("ActivityPortalPublishHandler publish end instanceConfig = {}", StringHelper.toJsonString(config));

		return StringHelper.toJsonString(config);
	}

	/**
	 * 处理成服务广场item需要的actionData
	 * 注：把instanceConfig中复杂的数据处理成服务广场需要的actionData字符串
	 * @param namespaceId
	 * @param instanceConfig
	 * @return
	 */
	@Override
	public String getItemActionData(Integer namespaceId, String instanceConfig){

		LOGGER.info("ActivityPortalPublishHandler getItemActionData start namespaceId = {}, instanceConfig = {}", namespaceId, instanceConfig);

		ActivityEntryConfigulation config = (ActivityEntryConfigulation)StringHelper.fromJsonString(instanceConfig, ActivityEntryConfigulation.class);

		ActivityActionData actionData = (ActivityActionData)StringHelper.fromJsonString(instanceConfig, ActivityActionData.class);

		actionData.setTitle(config.getName());

		LOGGER.info("ActivityPortalPublishHandler getItemActionData end actionData = {}", StringHelper.toJsonString(actionData));

		return StringHelper.toJsonString(actionData);
	}

	/**
	 *  根据服务广场的actionData获取业务应用的instanceConfig
	 * 注：通过actionData找到对应的所有需要配置的数据组装成之前跟web端人员协商好的instanceConfig，后面用于给web端人员解析展示对应到页面的各个配置
	 * @param namespaceId
	 * @param actionData
	 * @return
	 */
	@Override
	public String getAppInstanceConfig(Integer namespaceId, String actionData){

		LOGGER.info("ActivityPortalPublishHandler getAppInstanceConfig start namespaceId = {}, actionData = {}", namespaceId, actionData);

		ActivityActionData actionDataObj = (ActivityActionData)StringHelper.fromJsonString(actionData, ActivityActionData.class);

		ActivityEntryConfigulation config = (ActivityEntryConfigulation)StringHelper.fromJsonString(actionData, ActivityEntryConfigulation.class);

		config.setName(actionDataObj.getTitle());

		//ActionData忘写或者老数据没有categoryId，会把它认为是入口1
		if(config.getCategoryId() == null){
			config.setCategoryId(1L);
		}

		//防止老数据可能没有ActivityCategories，先更新保存一下
		ActivityCategories activityCategory = saveEntry(namespaceId, config.getCategoryId(), config.getName());

		List<ActivityCategories> oldContentCategories = activityProvider.listActivityCategory(namespaceId, activityCategory.getEntryId());

		List<ActivityCategoryDTO> categoryDTOList = new ArrayList<>();
		config.setCategoryFlag((byte)0);
		if(oldContentCategories != null && oldContentCategories.size() > 0){
			config.setCategoryFlag((byte)1);
			oldContentCategories.forEach(r -> {
				ActivityCategoryDTO dto = ConvertHelper.convert(r, ActivityCategoryDTO.class);
				categoryDTOList.add(dto);
			});
		}

		//如果没有则增加默认分类
		if(categoryDTOList.size() ==0){
			ActivityCategoryDTO newDto = new ActivityCategoryDTO();
			newDto.setAllFlag(AllFlagType.YES.getCode());
			newDto.setName("all");
			categoryDTOList.add(newDto);
		}

		config.setCategoryDTOList(categoryDTOList);

		LOGGER.info("ActivityPortalPublishHandler getAppInstanceConfig end config = {}", StringHelper.toJsonString(config));

		return StringHelper.toJsonString(config);
	}


	/**
	 * 新增或者更新活动入口
	 * @param entryId
	 * @param namespaceId
	 * @return
	 */
	private ActivityCategories saveEntry(Integer namespaceId, Long entryId, String name){

		ActivityCategories entryCategory = null;

		if(entryId != null) {
			entryCategory = activityProvider.findActivityCategoriesByEntryId(entryId, namespaceId);
		}

		if(entryCategory != null){
			entryCategory.setName(name);
			activityProvider.updateActivityCategories(entryCategory);
		} else {
			entryCategory = createActivityCategories(namespaceId, name, -1L, entryId, (byte)1, (byte) 0);
		}

		return  entryCategory;
	}


	private ActivityCategories createActivityCategories(Integer namespaceId, String name, Long parentId, Long entryId, Byte enabled, Byte allFlag){

		long id = this.sequenceProvider.getNextSequence(NameMapper.getSequenceDomainFromTablePojo(EhActivityCategories.class));
		ActivityCategories entryCategory = new ActivityCategories();
		entryCategory.setId(id);

		//设置parentId
		if(parentId == null){
			parentId = -1L;
		}
		entryCategory.setParentId(parentId);


		//设置入口Id
		if(entryId == null){
			entryId = id;
		}
		entryCategory.setEntryId(entryId);

		//设置路径
		String path = "";
		if(parentId == -1){
			path = "/" + entryId;
		}else {
			path = "/" + parentId + "/" + entryId;
		}
		entryCategory.setPath(path);

		entryCategory.setOwnerId(0L);

		if(StringUtils.isEmpty(name)){
			name = "default";
		}
		entryCategory.setName(name);
		entryCategory.setDefaultOrder(0);
		entryCategory.setStatus((byte)2);
		entryCategory.setCreatorUid(1L);
		entryCategory.setNamespaceId(namespaceId);
		entryCategory.setEnabled(enabled);

		if(allFlag == null){
			allFlag = (byte)0;
		}
		entryCategory.setAllFlag(allFlag);

		activityProvider.createActivityCategories(entryCategory);
		return entryCategory;
	}


	private void saveContencategory(ActivityEntryConfigulation config, Integer namespaceId){
		//清理部分被删除的主题分类
		deleteContentCategory(config, namespaceId);
		//更新、新增主题分类
		updateContentCategory(config, namespaceId);

	}

	private void updateContentCategory(ActivityEntryConfigulation config, Integer namespaceId){

		List<ActivityCategoryDTO> categoryDtos = new ArrayList<>();

		//校验是否所有的子分类都为关闭状态
		Integer flag = 0;
		//获取有效的子分类
		if(config.getCategoryDTOList() != null && config.getCategoryDTOList().size() > 0){
			for(int i=0; i<config.getCategoryDTOList().size(); i++) {
				ActivityCategoryDTO dto = config.getCategoryDTOList().get(i);

				if (dto.getId() == null){
				    if (TrueOrFalseFlag.FALSE.getCode().equals(dto.getEnabled())) {
				        flag++;
                    }
					dto.setParentId(config.getCategoryId());
					categoryDtos.add(dto);
				}else {
					ActivityCategories oldCategory = activityProvider.findActivityCategoriesById(dto.getId());
					if(oldCategory != null && oldCategory.getParentId() != null && oldCategory.getParentId().equals(config.getCategoryId())){
						dto.setParentId(config.getCategoryId());
						categoryDtos.add(dto);
                        if (TrueOrFalseFlag.FALSE.getCode().equals(oldCategory.getEnabled())) {
                            flag++;
                        }
					}
				}
			}
		}


		//如果没有则增加默认分类、或者子分类关闭
		if(categoryDtos.size() == 0 || (!CollectionUtils.isEmpty(config.getCategoryDTOList()) && flag.equals(config.getCategoryDTOList().size()))){
			ActivityCategoryDTO newDto = new ActivityCategoryDTO();
			newDto.setAllFlag(AllFlagType.YES.getCode());
			newDto.setName("all");
			newDto.setEnabled(TrueOrFalseFlag.TRUE.getCode());
			newDto.setParentId(config.getCategoryId());
			categoryDtos.add(newDto);
		}

//		//新增、更新入口

		for(int i=0; i<categoryDtos.size(); i++){
			ActivityCategoryDTO dto = categoryDtos.get(i);

			if(dto.getId() != null){
				ActivityCategories oldCategory = activityProvider.findActivityCategoriesById(dto.getId());
				if(dto.getName() != null){
					oldCategory.setName(dto.getName());
				}
				oldCategory.setIconUri(dto.getIconUri());
				oldCategory.setSelectedIconUri(dto.getSelectedIconUri());
				oldCategory.setEnabled(dto.getEnabled());
				activityProvider.updateActivityCategories(oldCategory);
			}else {
				ActivityCategories newCategory = createActivityCategories(namespaceId, dto.getName(), dto.getParentId(), null, (byte)1, dto.getAllFlag());

				dto.setId(newCategory.getId());
				dto.setEntryId(newCategory.getEntryId());

			}
		}

		//重新设置子分类
		config.setCategoryDTOList(categoryDtos);

	}

	/**
	 * 删除主题分类
	 * @param config
	 * @param namespaceId
	 */
	private void deleteContentCategory(ActivityEntryConfigulation config, Integer namespaceId){
		//删除分类
		List<ActivityCategories> oldContentCategories = activityProvider.listActivityCategory(namespaceId, config.getCategoryId());

		//原来没有则不用删除了
		if(oldContentCategories == null || oldContentCategories.size() == 0){
			return;
		}

		//如果主题分类是关闭的，则默认打开“全部”类型的主题分类，关闭其他主题分类。
		if(config.getCategoryFlag() == null || config.getCategoryFlag() == 0){

			if(config.getCategoryDTOList() != null ){
				for(int i=0; i<config.getCategoryDTOList().size(); i++){
					ActivityCategoryDTO dto = config.getCategoryDTOList().get(i);
					if(dto.getAllFlag() != null && dto.getAllFlag() == 1){
						dto.setEnabled((byte)1);
					}else {
						dto.setEnabled((byte)0);
					}

				}
			}

		}

		if(config.getCategoryDTOList() == null || config.getCategoryDTOList().size() == 0){
			//如果新发布的没有则删除全部
			for(int i=0; i<oldContentCategories.size(); i++){
				activityProvider.deleteActivityCategories(oldContentCategories.get(i).getId());
			}

		} else {

			//如果新发布的有则一个个对比，遍历旧的主题分类，以id为根据，不存在的话则删除
			for(int i=0; i<oldContentCategories.size(); i++){

				boolean deleteFlag = true;
				for(int j=0; j<config.getCategoryDTOList().size(); j++){
					if(config.getCategoryDTOList().get(j).getId() == null){
						continue;
					}
					//如果现在还要这个id的话，说明是更新的。这样的不删除
					if(config.getCategoryDTOList().get(j).getId().longValue() == oldContentCategories.get(i).getId().longValue()){
						deleteFlag = false;
						break;
					}
				}

				if(deleteFlag){
					activityProvider.deleteActivityCategories(oldContentCategories.get(i).getId());
				}
			}
		}
	}

	@Override
	public String processInstanceConfig(Integer namespaceId,String instanceConfig) {
		ActivityEntryConfigulation config = (ActivityEntryConfigulation)StringHelper.fromJsonString(instanceConfig, ActivityEntryConfigulation.class);
		if(config != null && null != config.getCategoryDTOList() && config.getCategoryDTOList().size() > 0){
			for (ActivityCategoryDTO dto: config.getCategoryDTOList()) {
				String iconUrl = contentServerService.parserUri(dto.getIconUri(), EntityType.USER.getCode(), UserContext.current().getUser().getId());
				dto.setIconUrl(iconUrl);
				String selectedIconUrl = contentServerService.parserUri(dto.getSelectedIconUri(), EntityType.USER.getCode(), UserContext.current().getUser().getId());
				dto.setSelectedIconUrl(selectedIconUrl);
			}
		}
		return StringHelper.toJsonString(config);
	}

	@Override
	public String getCustomTag(Integer namespaceId, Long moudleId, String instanceConfig) {

		ActivityActionData actionDataObj = (ActivityActionData)StringHelper.fromJsonString(instanceConfig, ActivityActionData.class);

//
//		if(actionDataObj == null || actionDataObj.getCategoryId() == null){
//			actionDataObj = (ActivityActionData) StringHelper.fromJsonString(instanceConfig, ActivityActionData.class);
//		}

		if(actionDataObj != null && actionDataObj.getCategoryId() != null){
			return String.valueOf(actionDataObj.getCategoryId());
		}

		return null;
	}

	@Override
	public Long getWebMenuId(Integer namespaceId, Long moudleId, String instanceConfig) {
		return 42040000L;
	}
}
