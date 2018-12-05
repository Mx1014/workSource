// @formatter:off
package com.everhomes.rest.aclink;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>获取门禁列表
 * <li> role: 0 没有权限，1 有权限 </li>
 * <li> nextPageAnchor: 下一页锚点 </li>
 * <li> doors: 门禁设备列表，参考{@link DoorAccessDTO}</li>
 * </ul>
 * @author janson
 *
 */
public class SearchDoorServerResponse {

    
    @ItemType(AclinkServerDTO.class)
    private AclinkServerDTO server;

    @ItemType(AclinkCameraDTO.class)
    private List<AclinkCameraDTO> cameras;

    @ItemType(AclinkIPadDTO.class)
    private List<AclinkIPadDTO> Ipad;

    public AclinkServerDTO getServer() {
        return server;
    }

    public void setServer(AclinkServerDTO server) {
        this.server = server;
    }

    public List<AclinkCameraDTO> getCameras() {
        return cameras;
    }

    public void setCameras(List<AclinkCameraDTO> cameras) {
        this.cameras = cameras;
    }

    public List<AclinkIPadDTO> getIpad() {
        return Ipad;
    }

    public void setIpad(List<AclinkIPadDTO> ipad) {
        Ipad = ipad;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
