package com.everhomes.rest.archives;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 * <li>detailIds: 员工id(批量)</li>
 * <li>organizationId: 公司 id</li>
 * </ul>
 */
public class DeleteArchivesEmployeesCommand {

    @ItemType(Long.class)
    private List<Long> detailIds;

    private Long organizationId;

    public DeleteArchivesEmployeesCommand() {
    }

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public List<Long> getDetailIds() {
        return detailIds;
    }

    public void setDetailIds(List<Long> detailIds) {
        this.detailIds = detailIds;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
