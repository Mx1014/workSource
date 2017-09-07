package com.everhomes.rest.aclink;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

public class ListDoorAuthLevelResponse {
    private Long nextPageAnchor;
    
    @ItemType(DoorAuthLevelDTO.class)
    List<DoorAuthLevelDTO> dtos;

    public Long getNextPageAnchor() {
        return nextPageAnchor;
    }

    public void setNextPageAnchor(Long nextPageAnchor) {
        this.nextPageAnchor = nextPageAnchor;
    }

    public List<DoorAuthLevelDTO> getDtos() {
        return dtos;
    }

    public void setDtos(List<DoorAuthLevelDTO> dtos) {
        this.dtos = dtos;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
