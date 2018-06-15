package com.everhomes.rest.aclink;

import com.everhomes.rest.RestResponseBase;

import java.util.List;

public class DoorAccessGroupResp {

    private Long userId;
    List<DoorAccessGroupDTO> groups;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public List<DoorAccessGroupDTO> getGroups() {
        return groups;
    }

    public void setGroups(List<DoorAccessGroupDTO> groups) {
        this.groups = groups;
    }
}
