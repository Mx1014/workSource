// @formatter:off
package com.everhomes.statistics.event.handler;

import com.everhomes.configuration.ConfigConstants;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.oauth2client.HttpResponseEntity;
import com.everhomes.oauth2client.handler.RestCallTemplate;
import com.everhomes.rest.launchpad.Widget;
import com.everhomes.statistics.event.StatEventParam;
import com.everhomes.statistics.event.StatEventStatistic;
import com.everhomes.util.StringHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.charset.Charset;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.CRC32;

/**
 * 电商运营点击
 * Created by xq.tian on 2017/8/7.
 */
@Component
public class LaunchpadOnOppushBizItemClickEventHandler extends AbstractStatEventPortalItemGroupHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(LaunchpadOnOppushBizItemClickEventHandler.class);

    @Autowired
    private ConfigurationProvider configurationProvider;

    @Override
    public String getEventName() {
        return LAUNCHPAD_ON_OPPUSH_BIZ_ITEM_CLICK;
    }

    @Override
    protected String getItemGroup(Map<String, String> paramsToValueMap) {
        return "OPPushBiz";
    }

    @Override
    protected Widget getWidget() {
        return Widget.OPPUSH;
    }

    @Override
    protected StatEventStatistic getEventStat(Map<String, String> paramsToValueMap) {
        StatEventStatistic eventStat = new StatEventStatistic();

        String commodityId = paramsToValueMap.get("id");

        eventStat.setOwnerType("EhCommodities");
        CRC32 crc32 = new CRC32();
        crc32.update(commodityId.getBytes(Charset.forName("UTF-8")));
        eventStat.setOwnerId(crc32.getValue());

        String bizServer = configurationProvider.getValue("stat.biz.server.url", "");
        String commodityDetailApi = configurationProvider.getValue(ConfigConstants.BIZ_COMMONDITY_DETAIL_API, "zl-ec/rest/openapi/commodity/queryCommodityByCommoNos");

        String[] commodityName = {"unknown"};

        if (bizServer.length() > 0) {
            List<String> idList = Collections.singletonList(commodityId);

            Map<String, Object> commoNosMap = new HashMap<>();
            commoNosMap.put("commoNos", idList);

            Map<String, Object> bodyMap = new HashMap<>();
            bodyMap.put("body", commoNosMap);

            RestCallTemplate
                    .url(bizServer + commodityDetailApi)
                    .body(StringHelper.toJsonString(bodyMap))
                    .respType(Map.class)
                    .onError((r) -> LOGGER.error("biz server resp error, code = {}, body = {}", r.getStatusCode(), r.getBody()))
                    .onSuccess((HttpResponseEntity<Map<String, Object>> entity) -> {
                        Map<String, Object> response = entity.getBody();
                        if (response != null) {
                            List<Map<String, Object>> dtoList = (List<Map<String, Object>>) response.get("body");
                            if (dtoList.size() > 0) {
                                commodityName[0] = dtoList.get(0).get("subject").toString();
                            }
                        }
                    })
                    .post();
        }

        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("OPPushBizItemId", commodityId);
        paramMap.put("OPPushBizItemName", commodityName[0]);

        eventStat.setParam(StringHelper.toJsonString(paramMap));
        return eventStat;
    }

    @Override
    protected List<StatEventParam> getParams(List<StatEventParam> params) {
        return params;
    }
}