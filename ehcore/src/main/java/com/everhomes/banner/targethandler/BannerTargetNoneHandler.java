package com.everhomes.banner.targethandler;

import com.everhomes.banner.BannerTargetHandleResult;
import com.everhomes.banner.BannerTargetHandler;
import com.everhomes.rest.launchpad.ActionType;
import com.everhomes.rest.module.RouterInfo;
import org.springframework.stereotype.Component;

/**
 * Created by xq.tian on 2018/3/7.
 */
@Component(BannerTargetHandler.BANNER_TARGET_HANDLER_PREFIX + "NONE")
public class BannerTargetNoneHandler implements BannerTargetHandler {

    @Override
    public BannerTargetHandleResult evaluate(String targetData) {
        BannerTargetHandleResult res = new BannerTargetHandleResult();
        res.setActionType(ActionType.NONE.getCode());
        return res;
    }

    @Override
    public RouterInfo getRouterInfo(String targetData) {
        return null;
    }

    @Override
    public Byte getClientHandlerType(String targetData) {
        return null;
    }
}
