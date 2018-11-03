// @formatter:off
package com.everhomes.banner.admin;

import com.everhomes.banner.BannerCategory;
import com.everhomes.banner.BannerProvider;
import com.everhomes.portal.PortalPublishHandler;
import com.everhomes.rest.banner.BannerStatus;
import com.everhomes.rest.common.ServiceModuleConstants;
import com.everhomes.rest.widget.BannersInstanceConfig;
import com.everhomes.user.User;
import com.everhomes.user.UserContext;
import com.everhomes.util.StringHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * create by yanlong.liang 20181026
 * 广告管理多应用发布
 */
@Component(PortalPublishHandler.PORTAL_PUBLISH_OBJECT_PREFIX + ServiceModuleConstants.BANNER_MODULE)
public class BannerPortalPublishHandler implements PortalPublishHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(BannerPortalPublishHandler.class);

    @Autowired
    private BannerProvider bannerProvider;


    @Override
    public String publish(Integer namespaceId, String instanceConfig, String appName) {
        LOGGER.error("publish news. instanceConfig = {}, itemLabel = {}", instanceConfig, appName);
        BannersInstanceConfig bannersInstanceConfig = (BannersInstanceConfig) StringHelper.fromJsonString(instanceConfig, BannersInstanceConfig.class);
        if (bannersInstanceConfig == null) {
            bannersInstanceConfig = new BannersInstanceConfig();
        }
        if(null == bannersInstanceConfig.getCategoryId()){
            BannerCategory bannerCategory = createBannerCategory(namespaceId, appName);
            bannersInstanceConfig.setCategoryId(bannerCategory.getId());
        }else{
            updateBannerCategory(namespaceId, bannersInstanceConfig.getCategoryId(), appName);
        }
        return StringHelper.toJsonString(bannersInstanceConfig);
    }

    @Override
    public String processInstanceConfig(Integer namespaceId, String instanceConfig) {
        return instanceConfig;
    }

    @Override
    public String getItemActionData(Integer namespaceId, String instanceConfig) {
        return instanceConfig;
    }

    @Override
    public String getAppInstanceConfig(Integer namespaceId, String actionData) {
        return actionData;
    }

    @Override
    public String getCustomTag(Integer namespaceId, Long moudleId, String instanceConfig) {

        BannersInstanceConfig bannersInstanceConfig = (BannersInstanceConfig)StringHelper.fromJsonString(instanceConfig, BannersInstanceConfig.class);

        if(bannersInstanceConfig != null && bannersInstanceConfig.getCategoryId() != null){
            return String.valueOf(bannersInstanceConfig.getCategoryId());
        }

        return null;
    }
    private BannerCategory createBannerCategory(Integer namespaceId, String name){
        User user = UserContext.current().getUser();
        BannerCategory bannerCategory = new BannerCategory();
        bannerCategory.setNamespaceId(namespaceId);
        bannerCategory.setOwnerType("0");
        bannerCategory.setOwnerId(0L);
        bannerCategory.setParentId(0L);
        bannerCategory.setName(name);
        bannerCategory.setStatus(BannerStatus.ACTIVE.getCode());
        bannerCategory.setCreatorUid(user.getId());
        bannerCategory.setDeleteUid(user.getId());
        bannerProvider.createBannerCategory(bannerCategory);
        return bannerCategory;
    }

    private BannerCategory updateBannerCategory(Integer namespaceId, Long categoryId, String name){
        BannerCategory bannerCategory = bannerProvider.findBannerCategoryById(categoryId);
        if(null != bannerCategory){
            bannerCategory.setName(name);
            bannerProvider.updateBannerCategory(bannerCategory);
        }else{
            LOGGER.error("banner category is null. categoryId = {}", categoryId);
        }
        return bannerCategory;
    }
}
