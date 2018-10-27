package com.everhomes.rest.aclink;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 * </ul>
 */
public class ChangeUpdateFirmwareResponse {
    private Long nextPageAnchor;
    
    @ItemType(AclinkDeviceDTO.class)
    private List<AclinkDeviceDTO> dtos;

    public List<AclinkDeviceDTO> getDtos() {
        return dtos;
    }

    public void setDtos(List<AclinkDeviceDTO> dtos) {
        this.dtos = dtos;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
