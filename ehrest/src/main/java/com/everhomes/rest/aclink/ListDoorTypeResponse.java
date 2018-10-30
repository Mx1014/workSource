// @formatter:off
package com.everhomes.rest.aclink;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>获取门禁列表
 * <li> nextPageAnchor: 下一页锚点 </li>
 * <li> doors: 门禁设备列表，参考{@link com.everhomes.rest.aclink.DoorAccessDTO}</li>
 * </ul>
 */
public class ListDoorTypeResponse {
    private Long nextPageAnchor;

    @ItemType(AclinkDeviceDTO.class)
    private List<AclinkDeviceDTO> dtos;

    public Long getNextPageAnchor() {
        return nextPageAnchor;
    }

    public void setNextPageAnchor(Long nextPageAnchor) {
        this.nextPageAnchor = nextPageAnchor;
    }

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
