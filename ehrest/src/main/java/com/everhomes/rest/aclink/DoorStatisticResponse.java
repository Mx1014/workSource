// @formatter
package com.everhomes.rest.aclink;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

public class DoorStatisticResponse {
    @ItemType(DoorStatisticDTO.class)
    private DoorStatisticDTO dtos;

    public DoorStatisticDTO getDtos() {
        return dtos;
    }

    public void setDtos(DoorStatisticDTO dtos) {
        this.dtos = dtos;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
