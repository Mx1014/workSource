package com.everhomes.rest.fixedasset;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 * <li>statusList: 资产状态列表，参考{@link com.everhomes.rest.fixedasset.ByteStringMapDTO}</li>
 * <li>addFromList: 资产来源列表，参考{@link com.everhomes.rest.fixedasset.ByteStringMapDTO}</li>
 * </ul>
 */
public class GetFixedAssetDictionaryResponse {
    @ItemType(ByteStringMapDTO.class)
    private List<ByteStringMapDTO> statusList;
    @ItemType(ByteStringMapDTO.class)
    private List<ByteStringMapDTO> addFromList;

    public List<ByteStringMapDTO> getStatusList() {
        return statusList;
    }

    public void setStatusList(List<ByteStringMapDTO> statusList) {
        this.statusList = statusList;
    }

    public List<ByteStringMapDTO> getAddFromList() {
        return addFromList;
    }

    public void setAddFromList(List<ByteStringMapDTO> addFromList) {
        this.addFromList = addFromList;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
