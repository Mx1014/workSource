package com.everhomes.launchpad;

import com.everhomes.rest.common.ScopeType;
import com.everhomes.rest.launchpad.*;
import com.everhomes.rest.launchpad.admin.*;
import com.everhomes.rest.launchpadbase.*;
import com.everhomes.rest.ui.launchpad.*;
import com.everhomes.rest.ui.user.SearchContentsBySceneCommand;
import com.everhomes.rest.ui.user.SearchContentsBySceneReponse;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;


public interface CommunityBizService {

    CommunityBizDTO CreateCommunityBiz(CreateCommunityBiz cmd);

    CommunityBizDTO updateCommunityBiz(updateCommunityBiz cmd);

    void deleteCommunityBiz(DeleteCommunityBiz cmd);

    CommunityBizDTO findCommunityBizByCommunityId(FindCommunityBizByCommunityId cmd);



}
