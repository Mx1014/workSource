package com.everhomes.rest.user;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

public class SearchUserImpersonationResponse {
    @ItemType(UserImpersonationDTO.class)
    private List<UserImpersonationDTO> impersonations;
    private Long nextPageAnchor;

    
    public List<UserImpersonationDTO> getImpersonations() {
        return impersonations;
    }


    public void setImpersonations(List<UserImpersonationDTO> impersonations) {
        this.impersonations = impersonations;
    }


    public Long getNextPageAnchor() {
        return nextPageAnchor;
    }


    public void setNextPageAnchor(Long nextPageAnchor) {
        this.nextPageAnchor = nextPageAnchor;
    }


    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
