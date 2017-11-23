package com.everhomes.rest.community_approve;

import com.everhomes.discover.ItemType;

import java.util.List;

/**
 * Created by zhengsiting on 2017/7/20.
 */
public class ListCommunityApproveValResponse {
    private Long nextPageAnchor;

    @ItemType(CommunityApproveValDTO.class)
    List<CommunityApproveValDTO> dtos;

    public List<CommunityApproveValDTO> getDtos() {
        return dtos;
    }

    public void setDtos(List<CommunityApproveValDTO> dtos) {
        this.dtos = dtos;
    }

    public Long getNextPageAnchor() {
        return nextPageAnchor;
    }

    public void setNextPageAnchor(Long nextPageAnchor) {
        this.nextPageAnchor = nextPageAnchor;
    }
}
