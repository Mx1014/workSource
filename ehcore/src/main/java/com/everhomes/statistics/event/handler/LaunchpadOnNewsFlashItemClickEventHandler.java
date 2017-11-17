// @formatter:off
package com.everhomes.statistics.event.handler;

import com.everhomes.news.News;
import com.everhomes.news.NewsProvider;
import com.everhomes.rest.launchpad.Widget;
import com.everhomes.server.schema.tables.EhNews;
import com.everhomes.server.schema.tables.EhPortalLayouts;
import com.everhomes.statistics.event.*;
import com.everhomes.util.StringHelper;
import com.everhomes.util.WebTokenGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 新闻NewsFlash点击
 * Created by xq.tian on 2017/8/7.
 */
@Component
public class LaunchpadOnNewsFlashItemClickEventHandler extends AbstractStatEventPortalItemGroupHandler {

    @Autowired
    private NewsProvider newsProvider;

    @Autowired
    private PortalLayoutProvider portalLayoutProvider;

    @Override
    public String getEventName() {
        return LAUNCHPAD_ON_NEWS_FLASH_ITEM_CLICK;
    }

    @Override
    protected String getItemGroup(Map<String, String> paramsToValueMap) {
        Long newsId = WebTokenGenerator.getInstance().fromWebToken(paramsToValueMap.get("newsToken"), Long.class);
        News news = newsProvider.findNewsById(newsId);

        // 主要就是通过news去查找对应的itemGroup, 写的比较丑
        if (news != null) {
            Long layoutId = Long.valueOf(paramsToValueMap.get("layoutId"));
            PortalLaunchPadMapping mapping = portalLaunchPadMappingProvider.findPortalLaunchPadMapping(EhPortalLayouts.class.getSimpleName(), layoutId);
            PortalLayout portalLayout = portalLayoutProvider.findPortalLayoutById(mapping.getPortalContentId());

            if (portalLayout != null) {
                List<PortalItemGroup> itemGroups = portalItemGroupProvider.listPortalItemGroupByStatus(portalLayout.getId(), (byte) 4);
                for (PortalItemGroup itemGroup : itemGroups) {
                    if (itemGroup.getWidget().equals(this.getWidget().getCode())
                            && Long.valueOf(itemGroup.getInstanceConfigMap().get("categoryId")).equals(news.getCategoryId())) {
                        return itemGroup.getName();
                    }
                }
            }
            return "Default";
        }
        return null;
    }

    @Override
    protected Widget getWidget() {
        return Widget.NEWS_FLASH;
    }

    @Override
    protected StatEventStatistic getEventStat(Map<String, String> paramsToValueMap) {
        Long newsId = WebTokenGenerator.getInstance().fromWebToken(paramsToValueMap.get("newsToken"), Long.class);
        News news = newsProvider.findNewsById(newsId);
        if (news == null) {
            return null;
        }
        StatEventStatistic eventStat = new StatEventStatistic();
        eventStat.setOwnerType(EhNews.class.getSimpleName());
        eventStat.setOwnerId(newsId);

        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("newsId", newsId);
        paramMap.put("newsTitle", news.getTitle());

        eventStat.setParam(StringHelper.toJsonString(paramMap));
        return eventStat;
    }
}
