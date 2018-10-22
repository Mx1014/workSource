package com.everhomes.rest.aclink;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <ul>获取门禁列表
 * <li> role: 0 没有权限，1 有权限 </li>
 * <li> nextPageAnchor: 下一页锚点 </li>
 * <li> doors: 门禁设备列表，参考{@link com.everhomes.rest.aclink.DoorAccessLiteDTO}</li>
 * </ul>
 *
 */
public class ListDoorAccessLiteResponse {
	private Long nextPageAnchor;
    private Long role;
    
    @ItemType(DoorAccessLiteDTO.class)
    private List<DoorAccessLiteDTO> doors;

    public Long getNextPageAnchor() {
        return nextPageAnchor;
    }

    public void setNextPageAnchor(Long nextPageAnchor) {
        this.nextPageAnchor = nextPageAnchor;
    }

    public List<DoorAccessLiteDTO> getDoors() {
		return doors;
	}

	public void setDoors(List<DoorAccessLiteDTO> doors) {
		this.doors = doors;
	}

	public Long getRole() {
        return role;
    }

    public void setRole(Long role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
