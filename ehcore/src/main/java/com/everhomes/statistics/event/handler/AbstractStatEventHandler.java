// @formatter:off
package com.everhomes.statistics.event.handler;

import com.everhomes.namespace.Namespace;
import com.everhomes.statistics.event.StatEvent;
import com.everhomes.statistics.event.StatEventHandler;
import com.everhomes.statistics.event.StatEventStatistic;

import java.time.LocalDate;
import java.util.List;

/**
 * Created by xq.tian on 2017/8/7.
 */
abstract public class AbstractStatEventHandler implements StatEventHandler {

    public static final String EVENT_HANDLER_PREFIX = "StatEventHandlerPrefix-";

    static final String PORTAL_ON_BOTTOM_NAVIGATION_CLICK = "portal_on_bottom_navigation_click";
    static final String PORTAL_ON_NAVIGATION_CLICK = "portal_on_navigation_click";
    static final String LAUNCHPAD_ON_BANNER_CLICK = "launchpad_on_banner_click";
    static final String LAUNCHPAD_ON_LAUNCH_PAD_ITEM_CLICK = "launchpad_on_launch_pad_item_click";
    static final String LAUNCHPAD_ON_BULLETIN_CLICK = "launchpad_on_bulletin_click";
    static final String LAUNCHPAD_ON_OPPUSH_BIZ_ITEM_CLICK = "launchpad_on_oppush_biz_item_click";
    static final String LAUNCHPAD_ON_OPPUSH_ACTIVITY_ITEM_CLICK = "launchpad_on_oppush_activity_item_click";
    static final String LAUNCHPAD_ON_NEWS_ITEM_CLICK = "launchpad_on_news_item_click";

    @Override
    public List<StatEventStatistic> process(Namespace namespace, StatEvent statEvent, LocalDate statDate) {
        return null;
    }
}
