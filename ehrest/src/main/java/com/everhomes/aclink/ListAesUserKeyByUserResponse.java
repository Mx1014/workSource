package com.everhomes.aclink;

import java.util.List;

import com.everhomes.util.StringHelper;

public class ListAesUserKeyByUserResponse {
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
