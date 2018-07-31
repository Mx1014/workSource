// @formatter:off
package com.everhomes.rest.aclink;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>dtos:门禁授权列表 {@link com.everhomes.rest.aclink.DoorAuthDTO}</li>
 * <li>nextPageAnchor: 下一页锚点</li>
 * </ul>
 */
public class ListDoorAuthResponse {
    private Long nextPageAnchor;
    
    @ItemType(DoorAuthDTO.class)
    List<DoorAuthDTO> dtos;

    public List<DoorAuthDTO> getDtos() {
        return dtos;
    }

    public void setDtos(List<DoorAuthDTO> dtos) {
        this.dtos = dtos;
    }

    public Long getNextPageAnchor() {
        return nextPageAnchor;
    }

    public void setNextPageAnchor(Long nextPageAnchor) {
        this.nextPageAnchor = nextPageAnchor;
    }
   
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
