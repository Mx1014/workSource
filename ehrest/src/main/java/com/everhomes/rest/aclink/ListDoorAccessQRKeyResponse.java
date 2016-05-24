package com.everhomes.rest.aclink;

import java.util.List;

import com.everhomes.discover.ItemType;

public class ListDoorAccessQRKeyResponse {
    @ItemType(DoorAccessQRKeyDTO.class)
    List<DoorAccessQRKeyDTO> keys;

    public List<DoorAccessQRKeyDTO> getKeys() {
        return keys;
    }

    public void setKeys(List<DoorAccessQRKeyDTO> keys) {
        this.keys = keys;
    }
    
}
