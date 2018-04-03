// @formatter:off
package com.everhomes.statistics.event.handler;

import com.everhomes.rest.launchpad.Widget;
import org.springframework.stereotype.Component;

/**
 * 新闻News点击
 * Created by xq.tian on 2017/8/7.
 */
@Component
public class LaunchpadOnNewsItemClickEventHandler extends LaunchpadOnNewsFlashItemClickEventHandler {

    @Override
    public String getEventName() {
        return LAUNCHPAD_ON_NEWS_ITEM_CLICK;
    }

    @Override
    protected Widget getWidget() {
        return Widget.NEWS;
    }
}
