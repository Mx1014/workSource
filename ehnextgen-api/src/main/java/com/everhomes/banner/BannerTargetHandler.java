package com.everhomes.banner;

import com.everhomes.rest.module.RouterInfo;

/**
 * Created by xq.tian on 2018/3/7.
 */
public interface BannerTargetHandler {

    String BANNER_TARGET_HANDLER_PREFIX = "BannerTargetHandler-";

    BannerTargetHandleResult evaluate(String targetData);

    default String formatURI(String url) {
        return String.format("{\"url\":\"%s\"}", url);
    }

    RouterInfo getRouterInfo(String targetData);

    Byte getClientHandlerType(String targetData);
}
