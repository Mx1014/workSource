// @formatter:off
package com.everhomes.rest.aclink;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>获取门禁列表
 * <li> nextPageAnchor: 下一页锚点 </li>
 * <li> groupRels: 门禁组关系列表，参考{@link DoorAccessGroupRelDTO}</li>
 * </ul>
 *
 */
public class ListDoorGroupResponse {
    private Long nextPageAnchor;
    @ItemType(AclinkGroupDTO.class)
    private List<AclinkGroupDTO> group;
    
	public Long getNextPageAnchor() {
		return nextPageAnchor;
	}
	public void setNextPageAnchor(Long nextPageAnchor) {
		this.nextPageAnchor = nextPageAnchor;
	}
	public List<AclinkGroupDTO> getGroup() {
		return group;
	}
	public void setGroup(List<AclinkGroupDTO> group) {
		this.group = group;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
