// @formatter:off
package com.everhomes.statistics.event.handler;

import com.everhomes.news.News;
import com.everhomes.news.NewsProvider;
import com.everhomes.rest.launchpad.Widget;
import com.everhomes.server.schema.tables.EhNews;
import com.everhomes.statistics.event.StatEventParam;
import com.everhomes.statistics.event.StatEventStatistic;
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

    @Override
    public String getEventName() {
        return LAUNCHPAD_ON_NEWS_FLASH_ITEM_CLICK;
    }

    @Override
    protected String getItemGroup(Map<String, String> paramsToValueMap) {
        return "Default";
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

    @Override
    protected List<StatEventParam> getParams(List<StatEventParam> params) {
        return params;
    }
}
