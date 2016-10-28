package com.everhomes.rest.aclink;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

public class ListQRUserPermissionResponse {
    @ItemType(DoorUserPermissionDTO.class)
    List<DoorUserPermissionDTO> dtos;
    
    private Long nextPageAnchor;

    public List<DoorUserPermissionDTO> getDtos() {
        return dtos;
    }

    public void setDtos(List<DoorUserPermissionDTO> dtos) {
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
