// @formatter:off
package com.everhomes.rest.aclink;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.admin.NamespaceDTO;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>获取门禁列表
 * <li> nextPageAnchor: 下一页锚点 </li>
 * <li> doors: 门禁设备列表，参考{@link com.everhomes.rest.aclink.DoorAccessNewDTO}</li>
 * </ul>
 */
public class ListDoorAccessEhResponse {
    private Long nextPageAnchor;

    @ItemType(DoorAccessNewDTO.class)
    private List<DoorAccessNewDTO> doors;

    @ItemType(NamespaceDTO.class)
    private List<NamespaceDTO> namespaces;

    public List<NamespaceDTO> getNamespaces() {
        return namespaces;
    }

    public void setNamespaces(List<NamespaceDTO> namespaces) {
        this.namespaces = namespaces;
    }

    public Long getNextPageAnchor() {
        return nextPageAnchor;
    }

    public void setNextPageAnchor(Long nextPageAnchor) {
        this.nextPageAnchor = nextPageAnchor;
    }

    public List<DoorAccessNewDTO> getDoors() {
        return doors;
    }

    public void setDoors(List<DoorAccessNewDTO> doors) {
        this.doors = doors;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
