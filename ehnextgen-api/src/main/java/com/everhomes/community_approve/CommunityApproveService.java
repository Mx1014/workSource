package com.everhomes.community_approve;

import com.everhomes.rest.community_approve.*;

import javax.servlet.http.HttpServletResponse;

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

    public void exportCommunityApproveValWithForm(ListCommunityApproveValCommand cmd
            , HttpServletResponse httpResponse);

    ListCommunityApproveValResponse listCommunityApproveVals(ListCommunityApproveValCommand cmd);

    GetTemplateByCommunityApproveIdResponse postApprovalForm(PostCommunityApproveFormCommand cmd);
}
