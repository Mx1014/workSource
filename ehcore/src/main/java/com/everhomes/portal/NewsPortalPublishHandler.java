package com.everhomes.portal;

import com.everhomes.news.NewsCategory;
import com.everhomes.news.NewsProvider;
import com.everhomes.rest.common.ServiceModuleConstants;
import com.everhomes.rest.news.NewsStatus;
import com.everhomes.rest.portal.NewsInstanceConfig;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.util.StringHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by sfyan on 2017/8/30.
 */
@Component(PortalPublishHandler.PORTAL_PUBLISH_OBJECT_PREFIX + ServiceModuleConstants.NEWS_MODULE)
public class NewsPortalPublishHandler implements PortalPublishHandler{

    private static final Logger LOGGER = LoggerFactory.getLogger(NewsPortalPublishHandler.class);


    @Autowired
    private NewsProvider newsProvider;


    @Override
    public String publish(Integer namespaceId, String instanceConfig, String itemLabel) {
        LOGGER.error("publish news. instanceConfig = {}, itemLabel = {}", instanceConfig, itemLabel);
        NewsInstanceConfig newsInstanceConfig = (NewsInstanceConfig)StringHelper.fromJsonString(instanceConfig, NewsInstanceConfig.class);
        if(null == newsInstanceConfig.getCategoryId()){
            NewsCategory newsCategory = createNewsCategory(namespaceId, itemLabel);
            newsInstanceConfig.setCategoryId(newsCategory.getId());
        }else{
            updateNewsCategory(namespaceId, newsInstanceConfig.getCategoryId(), itemLabel);
        }
        return StringHelper.toJsonString(newsInstanceConfig);
    }

    @Override
    public String getAppInstanceConfig(Integer namespaceId, String actionData) {
        return actionData;
    }

    @Override
    public String getItemActionData(Integer namespaceId, String instanceConfig) {
        return instanceConfig;
    }

    private NewsCategory createNewsCategory(Integer namespaceId, String name){
        User user = UserContext.current().getUser();
        NewsCategory newsCategory = new NewsCategory();
        newsCategory.setNamespaceId(namespaceId);
        newsCategory.setOwnerType("0");
        newsCategory.setOwnerId(0L);
        newsCategory.setParentId(0L);
        newsCategory.setName(name);
        newsCategory.setStatus(NewsStatus.ACTIVE.getCode());
        newsCategory.setCreatorUid(user.getId());
        newsCategory.setDeleteUid(user.getId());
        newsProvider.createNewsCategory(newsCategory);
        return newsCategory;
    }

    private NewsCategory updateNewsCategory(Integer namespaceId, Long categoryId, String name){
        NewsCategory newsCategory = newsProvider.findNewsCategoryById(categoryId);
        if(null != newsCategory){
            newsCategory.setName(name);
            newsProvider.updateNewsCategory(newsCategory);
        }else{
            LOGGER.error("news category is null. categoryId = {}", categoryId);
        }
        return newsCategory;
    }

    @Override
    public String processInstanceConfig(String instanceConfig) {
        return instanceConfig;
    }
}
