package com.everhomes.banner.targethandler;

import com.everhomes.banner.BannerTargetHandleResult;
import com.everhomes.banner.BannerTargetHandler;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.module.RouterInfoService;
import com.everhomes.rest.banner.targetdata.BannerActivityTargetData;
import com.everhomes.rest.common.ActivityDetailActionData;
import com.everhomes.rest.common.Router;
import com.everhomes.rest.launchpad.ActionType;
import com.everhomes.rest.launchpadbase.routerjson.ActivityContentRouterJson;
import com.everhomes.rest.module.RouterInfo;
import com.everhomes.rest.portal.ClientHandlerType;
import com.everhomes.serviceModuleApp.ServiceModuleAppService;
import com.everhomes.util.RouterBuilder;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.StringHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by xq.tian on 2018/3/7.
 */
@Component(BannerTargetHandler.BANNER_TARGET_HANDLER_PREFIX + "ACTIVITY_DETAIL")
public class BannerTargetActivityDetailHandler implements BannerTargetHandler {


    @Autowired
    RouterInfoService routerInfoService;

    @Override
    public BannerTargetHandleResult evaluate(String targetData) {
        if (targetData == null) {
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "Target data should be not null.");
        }

        BannerTargetHandleResult res = new BannerTargetHandleResult();
        res.setActionType(ActionType.ROUTER.getCode());

        BannerActivityTargetData dataObj = (BannerActivityTargetData)
                StringHelper.fromJsonString(targetData, BannerActivityTargetData.class);

        ActivityDetailActionData actionData = new ActivityDetailActionData();
        actionData.setForumId(dataObj.getForumId());
        actionData.setTopicId(dataObj.getPostId());

        String uri = RouterBuilder.build(Router.ACTIVITY_DETAIL, actionData);
        res.setActionData(formatURI(uri));
        return res;
    }

    @Override
    public RouterInfo getRouterInfo(String targetData) {

        BannerActivityTargetData dataObj = (BannerActivityTargetData)
                StringHelper.fromJsonString(targetData, BannerActivityTargetData.class);

        ActivityDetailActionData actionDataObject = new ActivityDetailActionData();
        actionDataObject.setForumId(dataObj.getForumId());
        actionDataObject.setTopicId(dataObj.getPostId());
        RouterInfo routerInfo = routerInfoService.getRouterInfo(10600L, "/detail", actionDataObject.toString());
        if(routerInfo == null){
            routerInfo = new RouterInfo();
            routerInfo.setPath("/detail");
            routerInfo.setQuery(routerInfoService.getQueryInDefaultWay(actionDataObject.toString()));
        }
        routerInfo.setModuleId(10600L);
        return routerInfo;
    }

    @Override
    public Byte getClientHandlerType(String targetData) {
        return ClientHandlerType.NATIVE.getCode();
    }
}
