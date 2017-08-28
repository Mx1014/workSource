// @formatter:off
package com.everhomes.activity;

import com.everhomes.portal.PortalPublishHandler;
import com.everhomes.rest.activity.*;
import com.everhomes.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class ActivityPortalPublishHandler implements PortalPublishHandler {

    private static final Logger LOGGER=LoggerFactory.getLogger(ActivityPortalPublishHandler.class);
    

	@Autowired
	private ActivityProivider activityProvider;


	/**
	 * * 发布具体模块的内容
	 * 注：instanceConfig就是跟web端协商好的json字符串，需要解析后添加或者修改（数据是否有id来判断添加或者修改）到对应的业务表里面去,
	 * 如果是对应的配置项要添加到业务表，则会生成id，需要把生成的id字段回填到instanceConfig，再次发布的时候能通过id找到对应的业务数据，
	 * 然后进行修改，如果是修改，则通过id直接找到对应业务数据修改即可
	 * @param namespaceId
	 * @param instanceConfig 具体模块配置的参数
	 * @return
	 */
	public String publish(Integer namespaceId, String instanceConfig){

		LOGGER.info("ActivityPortalPublishHandler publish start namespaceId = {}, instanceConfig = {}", namespaceId, instanceConfig);

		ActivityEntryConfigulation config = ConvertHelper.convert(instanceConfig, ActivityEntryConfigulation.class);

		//新增、更新入口
		Long maxEntryId = activityProvider.findActivityCategoriesMaxEntryId(namespaceId);
		if(maxEntryId == null){
			maxEntryId = 1L;
		}
		ActivityCategories activityCategory = updateEntry(config, maxEntryId, namespaceId);


		//将值组装到config中，用于后面返回服务广场
		config.setId(activityCategory.getId());
		config.setEntryId(activityCategory.getEntryId());

		//如果categoryFlag为否，则删掉category
		if(config.getCategoryFlag() == null && config.getCategoryFlag() == 0){
			config.setCategoryDTOList(null);
		}

		//删除内容分类
		deleteContentCategory(config, namespaceId);

		//新增、更新内容分类
		updateContentCategory(config, activityCategory,  maxEntryId, namespaceId);

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
	public String getItemActionData(Integer namespaceId, String instanceConfig){

		LOGGER.info("ActivityPortalPublishHandler getItemActionData start namespaceId = {}, instanceConfig = {}", namespaceId, instanceConfig);

		ActivityEntryConfigulation config = (ActivityEntryConfigulation)StringHelper.fromJsonString(instanceConfig, ActivityEntryConfigulation.class);

		ActivityActionData actionData = (ActivityActionData)StringHelper.fromJsonString(instanceConfig, ActivityActionData.class);

		actionData.setCategoryId(config.getEntryId());

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
	public String getAppInstanceConfig(Integer namespaceId, String actionData){

		LOGGER.info("ActivityPortalPublishHandler getAppInstanceConfig start namespaceId = {}, actionData = {}", namespaceId, actionData);

		ActivityActionData actionDataObj = (ActivityActionData)StringHelper.fromJsonString(actionData, ActivityActionData.class);

		ActivityEntryConfigulation config = (ActivityEntryConfigulation)StringHelper.fromJsonString(actionData, ActivityEntryConfigulation.class);

		config.setName(actionDataObj.getTitle());

		//之前没有categoryId的设置为1
		if(actionDataObj.getCategoryId() == null){
			config.setEntryId(1L);
		}else {
			config.setEntryId(actionDataObj.getCategoryId());
		}

		ActivityCategories entryCategory = activityProvider.findActivityCategoriesByEntryId(config.getEntryId(), namespaceId);
		if(entryCategory != null){
			config.setId(entryCategory.getId());
		}

		//防止老数据可能没有ActivityCategories，先更新一下
		ActivityCategories activityCategory = updateEntry(config, 0L, namespaceId);

		List<ActivityCategories> oldContentCategories = activityProvider.listActivityCategory(namespaceId, activityCategory.getId());
		List<ActivityCategoryDTO> categoryDTOList = new ArrayList<>();
		if(oldContentCategories != null){
			oldContentCategories.forEach(r -> {
				ActivityCategoryDTO dto = ConvertHelper.convert(r, ActivityCategoryDTO.class);
				categoryDTOList.add(dto);
			});
		}

		config.setCategoryDTOList(categoryDTOList);

		LOGGER.info("ActivityPortalPublishHandler getAppInstanceConfig end config = {}", StringHelper.toJsonString(config));

		return StringHelper.toJsonString(config);
	}


	/**
	 * 新增或者更新活动入口
	 * @param config
	 * @param maxEntryId
	 * @param namespaceId
	 * @return
	 */
	private ActivityCategories updateEntry(ActivityEntryConfigulation config, Long maxEntryId, Integer namespaceId){

		ActivityCategories activityCategory;

		if(config.getEntryId() != null) {
			activityCategory = activityProvider.findActivityCategoriesById(config.getId());

			activityCategory.setName(config.getName());

			activityProvider.updateActivityCategories(activityCategory);
		}else {

			activityCategory = new ActivityCategories();
			activityCategory.setOwnerId(0L);
			activityCategory.setParentId(-1L);
			activityCategory.setName(config.getName());
			activityCategory.setDefaultOrder(0);
			activityCategory.setStatus((byte)2);
			activityCategory.setCreatorUid(1L);
			activityCategory.setNamespaceId(namespaceId);
			activityCategory.setAllFlag((byte)0);
			activityCategory.setEnabled((byte)1);
			activityCategory.setEntryId(++maxEntryId);
			activityCategory.setPath("/" + maxEntryId);
			activityProvider.createActivityCategories(activityCategory);

		}

		return  activityCategory;
	}


	private void updateContentCategory(ActivityEntryConfigulation config, ActivityCategories activityCategory, Long maxEntryId, Integer namespaceId){
		if(config.getCategoryDTOList() != null && config.getCategoryDTOList().size() > 0){

			for(int i=0; i<config.getCategoryDTOList().size(); i++){
				ActivityCategoryDTO dto = config.getCategoryDTOList().get(i);

				if(dto.getId() != null){
					ActivityCategories oldCategory = activityProvider.findActivityCategoriesById(dto.getId());
					oldCategory.setName(dto.getName());
					oldCategory.setIconUri(dto.getIconUri());
					oldCategory.setSelectedIconUri(dto.getSelectedIconUri());
					oldCategory.setEnabled(dto.getEnabled());
					activityProvider.updateActivityCategories(oldCategory);
				}else {
					maxEntryId++;
					ActivityCategories newCategory = ConvertHelper.convert(dto, ActivityCategories.class);
					newCategory.setParentId(maxEntryId);
					newCategory.setPath(activityCategory.getPath() + "/" + maxEntryId);
					newCategory.setOwnerId(0L);
					newCategory.setDefaultOrder(0);
					newCategory.setStatus((byte)2);
					newCategory.setCreatorUid(1L);
					newCategory.setNamespaceId(namespaceId);
					newCategory.setAllFlag((byte)0);
					activityProvider.createActivityCategories(newCategory);

					dto.setId(newCategory.getId());
					dto.setEntryId(newCategory.getEntryId());

				}
			}

		}

	}

	private void deleteContentCategory(ActivityEntryConfigulation config, Integer namespaceId){
		//删除分类
		List<ActivityCategories> oldContentCategories = activityProvider.listActivityCategory(namespaceId, config.getId());

		//原来没有则不用删除了
		if(oldContentCategories == null || oldContentCategories.size() == 0){
			return;
		}

		//新发布的没有则删除全部，如果有则一个个对比
		if(config.getCategoryDTOList() == null || config.getCategoryDTOList().size() == 0){
			for(int i=0; i<oldContentCategories.size(); i++){
				activityProvider.deleteActivityCategories(oldContentCategories.get(i).getId());
			}

		} else {
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

}
