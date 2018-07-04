package com.everhomes.launchpad;

import com.everhomes.rest.launchpadbase.*;


public interface CommunityBizService {

    CommunityBizDTO CreateCommunityBiz(CreateCommunityBiz cmd);

    CommunityBizDTO updateCommunityBiz(updateCommunityBiz cmd);

    void deleteCommunityBiz(DeleteCommunityBiz cmd);

    CommunityBizDTO findCommunityBiz(FindCommunityBiz cmd);


    CommunityBizDTO findCommunityBizForApp();
}
