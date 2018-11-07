// @formatter:off
package com.everhomes.rest.aclink;

import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul> 添加访客授权。
 * <li>ownerId: 所属者id</li>
 * <li>ownerType: 所属者type：0园区 1企业</li>
 * <li>namespaceId: 域空间ID</li>
 * <li>path: 路径</li>
 * <li>name: 字段名</li>
 * <li>type: 字段类型：0 选项 1文本 2单选</li>
 * <li>status: 状态：0失效 1必填 2非必填</li>
 * <li>itemName: 子项名称</li>
 * </ul>
 *
 */
public class ListTempAuthPriorityCommand {

    private Long ownerId;
    private Byte ownerType;
    private List<Long> doorIds;

    public List<Long> getDoorIds() {
        return doorIds;
    }

    public void setDoorIds(List<Long> doorIds) {
        this.doorIds = doorIds;
    }

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

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
