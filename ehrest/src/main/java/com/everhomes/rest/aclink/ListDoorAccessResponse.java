package com.everhomes.rest.aclink;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

public class ListDoorAccessResponse {
    private Long nextPageAnchor;
    
    @ItemType(DoorAccessDTO.class)
    private List<DoorAccessDTO> doors;

    public Long getNextPageAnchor() {
        return nextPageAnchor;
    }

    public void setNextPageAnchor(Long nextPageAnchor) {
        this.nextPageAnchor = nextPageAnchor;
    }

    public List<DoorAccessDTO> getDoors() {
        return doors;
    }

    public void setDoors(List<DoorAccessDTO> doors) {
        this.doors = doors;
    }
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
