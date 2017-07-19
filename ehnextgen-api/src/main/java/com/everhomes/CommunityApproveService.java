package com.everhomes;

import com.everhomes.rest.community_approve.*;

/**
 * Created by Administrator on 2017/7/18.
 */
public interface CommunityApproveService {

    CommunityApproveDTO updateCommunityApprove(UpdateCommunityApproveCommand cmd);

    ListCommunityApproveResponse listCommunityApproves(ListCommunityApproveCommand cmd);

    void deleteCommunityApprove(CommunityApproveIdCommand cmd);

    CommunityApproveDTO createCommunityApprove(CreateCommunityApproveCommand cmd);
}
