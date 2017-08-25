// @formatter:off
package com.everhomes.statistics.event.handler;

import com.everhomes.business.CommodityRespDTO;
import com.everhomes.configuration.ConfigConstants;
import com.everhomes.configuration.ConfigurationProvider;
import com.everhomes.oauth2client.HttpResponseEntity;
import com.everhomes.oauth2client.handler.RestCallTemplate;
import com.everhomes.rest.launchpad.Widget;
import com.everhomes.rest.promotion.ModulePromotionEntityDTO;
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
    protected String getItemGroup(String key, Long layoutId) {
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

        if (bizServer.isEmpty()) {
            List<String> idList = Collections.singletonList(commodityId);
            RestCallTemplate
                    .url(bizServer + commodityDetailApi)
                    .body(idList.toString())
                    .respType(CommodityRespDTO.class)
                    .onError((r) -> LOGGER.error("biz server resp error, code = {}, body = {}", r.getStatusCode(), r.getBody()))
                    .onSuccess((HttpResponseEntity<CommodityRespDTO> entity) -> {
                        CommodityRespDTO dto = entity.getBody();
                        List<ModulePromotionEntityDTO> response = dto.getResponse();
                        commodityName[0] = response.get(0).getSubject();
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
    protected StatEventParam getIdentifierParam(List<StatEventParam> params) {
        for (StatEventParam param : params) {
            if (param.getParamKey().equals("id")) {
                return param;
            }
        }
        return null;
    }
}