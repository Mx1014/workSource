package com.everhomes.banner.targethandler;

import com.everhomes.banner.BannerTargetHandleResult;
import com.everhomes.banner.BannerTargetHandler;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.rest.launchpad.ActionType;
import com.everhomes.util.RuntimeErrorException;
import org.springframework.stereotype.Component;


/**
 * Created by xq.tian on 2018/3/7.
 */
@Component(BannerTargetHandler.BANNER_TARGET_HANDLER_PREFIX + "URL")
public class BannerTargetURLHandler implements BannerTargetHandler {

    @Override
    public BannerTargetHandleResult evaluate(String targetData) {
        if (targetData == null) {
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "Target data should be not null.");
        }
        BannerTargetHandleResult res = new BannerTargetHandleResult();
        if (targetData.contains("zuolin.com")) {
            res.setActionType(ActionType.OFFICIAL_URL.getCode());
        } else {
            res.setActionType(ActionType.THIRDPART_URL.getCode());
        }
        res.setActionData(targetData);
        return res;
    }
}
