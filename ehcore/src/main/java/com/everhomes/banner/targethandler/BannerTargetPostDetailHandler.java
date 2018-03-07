package com.everhomes.banner.targethandler;

import com.everhomes.banner.BannerTargetHandleResult;
import com.everhomes.banner.BannerTargetHandler;
import com.everhomes.constants.ErrorCodes;
import com.everhomes.rest.banner.targetdata.BannerPostTargetData;
import com.everhomes.rest.common.PostDetailActionData;
import com.everhomes.rest.common.Router;
import com.everhomes.rest.launchpad.ActionType;
import com.everhomes.util.RouterBuilder;
import com.everhomes.util.RuntimeErrorException;
import com.everhomes.util.StringHelper;
import org.springframework.stereotype.Component;

/**
 * Created by xq.tian on 2018/3/7.
 */
@Component(BannerTargetHandler.BANNER_TARGET_HANDLER_PREFIX + "POST_DETAIL")
public class BannerTargetPostDetailHandler implements BannerTargetHandler {

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
        actionData.setForumId(dataObj.getEntryId());
        actionData.setTopicId(dataObj.getPostId());

        String uri = RouterBuilder.build(Router.POST_DETAIL, actionData);
        res.setActionData(formatURI(uri));
        return res;
    }
}
