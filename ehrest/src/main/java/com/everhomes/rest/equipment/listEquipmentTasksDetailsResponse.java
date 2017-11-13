package com.everhomes.rest.equipment;


import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 *  <li>tasks: 任务离线列表 参考{@link com.everhomes.rest.equipment.EquipmentTaskOffLineDTO}</li>
 *  <li>nextPageAnchor: 下一页的锚点，没有下一页则没有</li>
 * </ul>
 */
public class listEquipmentTasksDetailsResponse {
    private List<EquipmentTaskOffLineDTO> tasks;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
