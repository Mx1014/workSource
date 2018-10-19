// @formatter:off
package com.everhomes.rest.aclink;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>type:设备类型分类 0 ：自有设备 1：第三方设备</li>
 * <li>firmwareID：固件id</li>
 * <li>pageSize：每页大小</li>
 * <li>pageAnchor：锚点</li>
 * </ul>
 */
public class ListDoorTypeCommand {

    private Byte type;

    private Long firmwareId;

    private Long pageAnchor;

    private Integer pageSize;

    public Long getFirmwareId() {
        return firmwareId;
    }

    public void setFirmwareId(Long firmwareId) {
        this.firmwareId = firmwareId;
    }

    public Byte getType() {
        return type;
    }

    public void setType(Byte type) {
        this.type = type;
    }

    public Long getPageAnchor() {
        return pageAnchor;
    }

    public void setPageAnchor(Long pageAnchor) {
        this.pageAnchor = pageAnchor;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
