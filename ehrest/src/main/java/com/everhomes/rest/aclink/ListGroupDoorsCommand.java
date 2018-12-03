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
public class ListGroupDoorsCommand {
    private Long groupId;

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
