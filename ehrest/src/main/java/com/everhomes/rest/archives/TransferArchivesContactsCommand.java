package com.everhomes.rest.archives;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 * <li>detailIds: 成员 detailId 列表</li>
 * <li>departmentId: 部门 id</li>
 * </ul>
 */
public class TransferArchivesContactsCommand {

    @ItemType(Long.class)
    private List<Long> detailIds;

    private Long departmentId;

    public TransferArchivesContactsCommand() {
    }

    public List<Long> getDetailIds() {
        return detailIds;
    }

    public void setDetailIds(List<Long> detailIds) {
        this.detailIds = detailIds;
    }

    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
