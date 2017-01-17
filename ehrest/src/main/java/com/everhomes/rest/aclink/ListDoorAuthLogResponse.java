package com.everhomes.rest.aclink;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

public class ListDoorAuthLogResponse {
    private Long nextPageAnchor;
    
    @ItemType(DoorAuthLogDTO.class)
    private List<DoorAuthLogDTO> dtos;

    public List<DoorAuthLogDTO> getDtos() {
        return dtos;
    }

    public void setDtos(List<DoorAuthLogDTO> dtos) {
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
