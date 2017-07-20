package com.everhomes.community_approve;

import com.everhomes.rest.community_approve.*;

/**
 * Created by Administrator on 2017/7/18.
 */
public interface CommunityApproveService {

    CommunityApproveDTO updateCommunityApprove(UpdateCommunityApproveCommand cmd);

    ListCommunityApproveResponse listCommunityApproves(ListCommunityApproveCommand cmd);

    void deleteCommunityApprove(CommunityApproveIdCommand cmd);

    CommunityApproveDTO createCommunityApprove(CreateCommunityApproveCommand cmd);

    void enableCommunityApprove(CommunityApproveIdCommand cmd);

    void disableCommunityApprove(CommunityApproveIdCommand cmd);

    //void getTemplateByApprovalId(GetTemplateByApprovalIdCommand cmd);

    GetTemplateByCommunityApproveIdResponse postApprovalForm(PostCommunityApproveFormCommand cmd);
}
