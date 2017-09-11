package com.everhomes.rest.archives;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 * <li>detailIds: (List)成员 detailIds 列表</li>
 * <li>organizationId: 公司 id</li>
 * </ul>
 */
public class DeleteArchivesContactsCommand {

    @ItemType(Long.class)
    private List<Long> detailIds;

    private Long organizationId;

    public DeleteArchivesContactsCommand() {
    }

    public List<Long> getDetailIds() {
        return detailIds;
    }

    public void setDetailIds(List<Long> detailIds) {
        this.detailIds = detailIds;
    }

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
