package com.everhomes.rest.archives;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 * <li>detailIds: 成员 detailId 列表</li>
 * <li>departmentId: 部门 id 列表</li>
 * </ul>
 */
public class TransferArchivesContactsCommand {

    @ItemType(Long.class)
    private List<Long> detailIds;

    @ItemType(Long.class)
    private List<Long> departmentIds;

    public TransferArchivesContactsCommand() {
    }

    public List<Long> getDetailIds() {
        return detailIds;
    }

    public void setDetailIds(List<Long> detailIds) {
        this.detailIds = detailIds;
    }

    public List<Long> getDepartmentIds() {
        return departmentIds;
    }

    public void setDepartmentIds(List<Long> departmentIds) {
        this.departmentIds = departmentIds;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
