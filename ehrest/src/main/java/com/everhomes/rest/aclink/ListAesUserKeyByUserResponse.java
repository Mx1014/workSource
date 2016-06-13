package com.everhomes.rest.aclink;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

public class ListAesUserKeyByUserResponse {
    @ItemType(AesUserKeyDTO.class)
    List<AesUserKeyDTO> aesUserKeys;

    public List<AesUserKeyDTO> getAesUserKeys() {
        return aesUserKeys;
    }

    public void setAesUserKeys(List<AesUserKeyDTO> aesUserKeys) {
        this.aesUserKeys = aesUserKeys;
    }
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
