// @formatter:off
package com.everhomes.rest.aclink;

import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;

/**
 * <ul>
 * <li>ownerId: 所属上级的id</li>
 * <li>ownerType: 所属上级的类型</li>
 * <li>doorType:门禁类型{@link: com.everhomes.rest.aclink.DoorAccessType}</li>
 * <li>search: 搜索关键字</li>
 * <li>pageAnchor: 锚点</li>
 * <li>pageSize: 每页大小</li>
 * </ul>
 */
public class ListDoorGroupCommand {
    @NotNull
    private Long ownerId;
    
    @NotNull
    private Byte ownerType;

    private Long pageAnchor;

    private Integer pageSize;



    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public Byte getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(Byte ownerType) {
        this.ownerType = ownerType;
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
