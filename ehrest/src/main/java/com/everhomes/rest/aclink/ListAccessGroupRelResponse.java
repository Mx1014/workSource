// @formatter:off
package com.everhomes.rest.aclink;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <ul>获取门禁列表
 * <li> nextPageAnchor: 下一页锚点 </li>
 * <li> groupRels: 门禁组关系列表，参考{@link com.everhomes.rest.aclink.DoorAccessGroupRelDTO}</li>
 * </ul>
 *
 */
public class ListAccessGroupRelResponse {
    private Long nextPageAnchor;
    @ItemType(DoorAccessGroupRelDTO.class)
    private List<DoorAccessGroupRelDTO> groupRels;
    
	public Long getNextPageAnchor() {
		return nextPageAnchor;
	}
	public void setNextPageAnchor(Long nextPageAnchor) {
		this.nextPageAnchor = nextPageAnchor;
	}
	public List<DoorAccessGroupRelDTO> getGroupRels() {
		return groupRels;
	}
	public void setGroupRels(List<DoorAccessGroupRelDTO> groupRels) {
		this.groupRels = groupRels;
	}
    
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
