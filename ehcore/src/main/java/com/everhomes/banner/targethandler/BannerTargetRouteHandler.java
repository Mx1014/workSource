package com.everhomes.banner.targethandler;

import com.everhomes.banner.BannerTargetHandleResult;
import com.everhomes.banner.BannerTargetHandler;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.rest.banner.targetdata.BannerRouteTargetData;
import com.everhomes.rest.launchpad.ActionType;
import com.everhomes.rest.module.RouterInfo;
import com.everhomes.rest.portal.ClientHandlerType;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.StringHelper;
import org.springframework.stereotype.Component;

/**
 * Created by xq.tian on 2018/3/7.
 */
@Component(BannerTargetHandler.BANNER_TARGET_HANDLER_PREFIX + "ROUTE")
public class BannerTargetRouteHandler implements BannerTargetHandler {

    @Override
    public BannerTargetHandleResult evaluate(String targetData) {
        if (targetData == null) {
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "Target data should be not null.");
        }

        BannerTargetHandleResult res = new BannerTargetHandleResult();
        res.setActionType(ActionType.ROUTER.getCode());

        BannerRouteTargetData dataObj = (BannerRouteTargetData)
                StringHelper.fromJsonString(targetData, BannerRouteTargetData.class);

        res.setActionData(formatURI(dataObj.getUri()));
        return res;
    }

    @Override
    public RouterInfo getRouterInfo(String targetData) {
        return null;
    }

    @Override
    public Byte getClientHandlerType(String targetData) {
        return ClientHandlerType.NATIVE.getCode();
    }
}
