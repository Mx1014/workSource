package com.everhomes.banner.targethandler;

import com.everhomes.banner.BannerTargetHandleResult;
import com.everhomes.banner.BannerTargetHandler;
import com.everhomes.rest.launchpad.ActionType;
import org.springframework.stereotype.Component;

/**
 * Created by xq.tian on 2018/3/7.
 */
@Component(BannerTargetHandler.BANNER_TARGET_HANDLER_PREFIX + "APP")
public class BannerTargetAppHandler implements BannerTargetHandler {

    @Override
    public BannerTargetHandleResult evaluate(String targetData) {
        BannerTargetHandleResult res = new BannerTargetHandleResult();
        res.setActionType(ActionType.NONE.getCode());
        return res;
    }
}
