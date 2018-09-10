package com.everhomes.rest.aclink;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 *     <li>activeDoor: 已激活门禁数</li>
 *     <li>openTotal: 开门次数合计</li>
 *     <li>tempAuthTotal: 临时授权合计</li>
 *     <li></li>
 * </ul>
 */

public class QryDoorStatisticsResponse {
    @ItemType(DoorStatisticsDTO.class)
    private List<DoorStatisticsDTO> dtos;

    public List<DoorStatisticsDTO> getDtos() {
        return dtos;
    }

    public void setDtos(List<DoorStatisticsDTO> dtos) {
        this.dtos = dtos;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
