package com.everhomes.banner.targethandler;

import com.everhomes.banner.BannerTargetHandleResult;
import com.everhomes.banner.BannerTargetHandler;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.module.RouterInfoService;
import com.everhomes.rest.banner.targetdata.BannerPostTargetData;
import com.everhomes.rest.common.PostDetailActionData;
import com.everhomes.rest.common.Router;
import com.everhomes.rest.launchpad.ActionType;
import com.everhomes.rest.module.RouterInfo;
import com.everhomes.rest.portal.ClientHandlerType;
import com.everhomes.util.RouterBuilder;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.StringHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by xq.tian on 2018/3/7.
 */
@Component(BannerTargetHandler.BANNER_TARGET_HANDLER_PREFIX + "POST_DETAIL")
public class BannerTargetPostDetailHandler implements BannerTargetHandler {


    @Autowired
    RouterInfoService routerInfoService;

    // {"url":"zl://post/d?forumId=180773&topicId=203708"}
    @Override
    public BannerTargetHandleResult evaluate(String targetData) {
        if (targetData == null) {
            throw RuntimeErrorException.errorWith(ErrorCodes.SCOPE_GENERAL, ErrorCodes.ERROR_INVALID_PARAMETER,
                    "Target data should be not null.");
        }

        BannerTargetHandleResult res = new BannerTargetHandleResult();
        res.setActionType(ActionType.ROUTER.getCode());

        BannerPostTargetData dataObj = (BannerPostTargetData)
                StringHelper.fromJsonString(targetData, BannerPostTargetData.class);

        PostDetailActionData actionData = new PostDetailActionData();
        actionData.setForumId(dataObj.getForumId());
        actionData.setTopicId(dataObj.getPostId());

        String uri = RouterBuilder.build(Router.POST_DETAIL, actionData);
        res.setActionData(formatURI(uri));
        return res;
    }

    @Override
    public RouterInfo getRouterInfo(String targetData) {
        BannerPostTargetData dataObj = (BannerPostTargetData)
                StringHelper.fromJsonString(targetData, BannerPostTargetData.class);

        PostDetailActionData actionData = new PostDetailActionData();
        actionData.setForumId(dataObj.getForumId());
        actionData.setTopicId(dataObj.getPostId());

        RouterInfo routerInfo = routerInfoService.getRouterInfo(10100L, "/detail", actionData.toString());

        if(routerInfo == null){
            routerInfo = new RouterInfo();
            routerInfo.setPath("/detail");
            routerInfo.setQuery(routerInfoService.getQueryInDefaultWay(actionData.toString()));
        }

        routerInfo.setModuleId(10100L);

        return routerInfo;
    }

    @Override
    public Byte getClientHandlerType(String targetData) {
        return ClientHandlerType.NATIVE.getCode();
    }
}
