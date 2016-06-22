package com.everhomes.rest.aclink;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

public class ListDoorAccessQRKeyResponse {
    @ItemType(DoorAccessQRKeyDTO.class)
    List<DoorAccessQRKeyDTO> keys;

    public List<DoorAccessQRKeyDTO> getKeys() {
        return keys;
    }

    public void setKeys(List<DoorAccessQRKeyDTO> keys) {
        this.keys = keys;
    }
    
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
