package com.everhomes.banner.targethandler;

import com.everhomes.banner.BannerTargetHandleResult;
import com.everhomes.banner.BannerTargetHandler;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.rest.banner.targetdata.BannerAppTargetData;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.StringHelper;
import org.springframework.stereotype.Component;

/**
 * Created by xq.tian on 2018/3/7.
 */
@Component(BannerTargetHandler.BANNER_TARGET_HANDLER_PREFIX + "APP")
public class BannerTargetAppHandler implements BannerTargetHandler {

    @Override
    public BannerTargetHandleResult evaluate(String targetData) {
        if (targetData == null) {
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "Target data should be not null.");
        }

        BannerAppTargetData tData = (BannerAppTargetData) StringHelper.fromJsonString(targetData, BannerAppTargetData.class);
        BannerTargetHandleResult res = new BannerTargetHandleResult();
        res.setActionType(tData.getActionType());
        res.setActionData(tData.getActionData());
        return res;
    }
}
