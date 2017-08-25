// @formatter:off
package com.everhomes.statistics.event.handler;

import com.everhomes.banner.Banner;
import com.everhomes.banner.BannerProvider;
import com.everhomes.rest.launchpad.Widget;
import com.everhomes.server.schema.tables.EhBanners;
import com.everhomes.statistics.event.StatEventParam;
import com.everhomes.statistics.event.StatEventStatistic;
import com.everhomes.util.StringHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Banner点击
 * Created by xq.tian on 2017/8/7.
 */
@Component
public class LaunchpadOnBannerClickEventHandler extends AbstractStatEventPortalItemGroupHandler {

    @Autowired
    private BannerProvider bannerProvider;

    @Override
    public String getEventName() {
        return LAUNCHPAD_ON_BANNER_CLICK;
    }

    @Override
    protected String getItemGroup(String identifierParamsValue, Long layoutId) {
        return "Default";
    }

    @Override
    protected Widget getWidget() {
        return Widget.BANNERS;
    }

    @Override
    protected StatEventStatistic getEventStat(Map<String, String> paramsToValueMap) {
        Long bannerId = Long.valueOf(paramsToValueMap.get("id"));
        Banner banner = bannerProvider.findBannerById(bannerId);
        if (banner == null) {
            return null;
        }
        StatEventStatistic eventStat = new StatEventStatistic();
        eventStat.setOwnerType(EhBanners.class.getSimpleName());
        eventStat.setOwnerId(bannerId);

        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("bannerId", bannerId);
        paramMap.put("bannerName", banner.getName());

        eventStat.setParam(StringHelper.toJsonString(paramMap));
        return eventStat;
    }

    @Override
    protected StatEventParam getIdentifierParam(List<StatEventParam> params) {
        for (StatEventParam p : params) {
            if (p.getParamKey().equals("id")) {
                return p;
            }
        }
        return null;
    }
}
