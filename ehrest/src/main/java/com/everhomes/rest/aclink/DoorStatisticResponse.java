// @formatter
package com.everhomes.rest.aclink;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;
/**
 * <ul>
 *     <li>dtos:数据统计列表 {@link com.everhomes.rest.aclink.DoorStatisticDTO}</li>
 *     <li>若用户无权限，抛出异常"errorCode":100055,"errorDescription":"校验应用权限失败"</li>
 * </ul>
 */
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
