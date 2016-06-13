package com.everhomes.rest.aclink;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

public class ListDoorAuthResponse {
    private Long nextPageAnchor;
    
    @ItemType(DoorAuthDTO.class)
    List<DoorAuthDTO> dtos;

    public List<DoorAuthDTO> getDtos() {
        return dtos;
    }

    public void setDtos(List<DoorAuthDTO> dtos) {
        this.dtos = dtos;
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
