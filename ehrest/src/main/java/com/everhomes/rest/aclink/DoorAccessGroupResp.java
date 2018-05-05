package com.everhomes.rest.aclink;

import java.util.List;

public class DoorAccessGroupResp {

    List<DoorAccessGroupDTO> groups;

    public List<DoorAccessGroupDTO> getGroups() {
        return groups;
    }

    public void setGroups(List<DoorAccessGroupDTO> groups) {
        this.groups = groups;
    }
}
