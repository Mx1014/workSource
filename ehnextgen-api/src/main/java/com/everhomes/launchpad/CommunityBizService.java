package com.everhomes.launchpad;

import com.everhomes.rest.launchpadbase.*;


public interface CommunityBizService {

    CommunityBizDTO CreateCommunityBiz(CreateCommunityBizCommand cmd);

    CommunityBizDTO updateCommunityBiz(UpdateCommunityBizCommand cmd);

    void deleteCommunityBiz(DeleteCommunityBizCommand cmd);

    CommunityBizDTO findCommunityBiz(FindCommunityBizCommand cmd);


    CommunityBizDTO findCommunityBizForApp(FindCommunityBizForAppCommand cmd);
}
