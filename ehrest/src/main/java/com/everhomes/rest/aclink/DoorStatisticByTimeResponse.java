package com.everhomes.rest.aclink;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

public class DoorStatisticByTimeResponse {
    @ItemType(DoorStatisticByTimeDTO.class)
    private List<DoorStatisticByTimeDTO> dtos;

    public List<DoorStatisticByTimeDTO> getDtos() {
        return dtos;
    }

    public void setDtos(List<DoorStatisticByTimeDTO> dtos) {
        this.dtos = dtos;
    }
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
