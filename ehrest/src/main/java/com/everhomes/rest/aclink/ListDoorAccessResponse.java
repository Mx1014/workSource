package com.everhomes.rest.aclink;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <ul>获取门禁列表
 * <li> role: 0: 没有权限，1 有权限 </li>
 * </ul>
 * @author janson
 *
 */
public class ListDoorAccessResponse {
    private Long nextPageAnchor;
    private Long role;
    
    @ItemType(DoorAccessDTO.class)
    private List<DoorAccessDTO> doors;

    public Long getNextPageAnchor() {
        return nextPageAnchor;
    }

    public void setNextPageAnchor(Long nextPageAnchor) {
        this.nextPageAnchor = nextPageAnchor;
    }

    public List<DoorAccessDTO> getDoors() {
        return doors;
    }

    public void setDoors(List<DoorAccessDTO> doors) {
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
