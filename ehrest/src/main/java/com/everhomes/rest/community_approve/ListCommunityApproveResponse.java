package com.everhomes.rest.community_approve;

import com.everhomes.discover.ItemType;

import java.util.List;

/**
 * Created by Administrator on 2017/7/18.
 */
public class ListCommunityApproveResponse {
    @ItemType(CommunityApproveDTO.class)
    List<CommunityApproveDTO> approves;

    public List<CommunityApproveDTO> getApproves() {
        return approves;
    }

    public void setApproves(List<CommunityApproveDTO> approves) {
        this.approves = approves;
    }
}
