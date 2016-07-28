package com.everhomes.rest.aclink;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

public class ListDoorAccessQRKeyResponse {
    @ItemType(DoorAccessQRKeyDTO.class)
    List<DoorAccessQRKeyDTO> keys;
    
    Long qrTimeout;

    public List<DoorAccessQRKeyDTO> getKeys() {
        return keys;
    }

    public void setKeys(List<DoorAccessQRKeyDTO> keys) {
        this.keys = keys;
    }

    public Long getQrTimeout() {
        return qrTimeout;
    }

    public void setQrTimeout(Long qrTimeout) {
        this.qrTimeout = qrTimeout;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
