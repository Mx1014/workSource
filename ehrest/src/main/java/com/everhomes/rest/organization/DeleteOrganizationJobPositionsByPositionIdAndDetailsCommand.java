package com.everhomes.rest.organization;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 * <li>detailIds: 成员 detailId 列表</li>
 * <li>jobPositionId: 通用岗位 id</li>
 * <li>organizationId: 公司 id</li>
 * </ul>
 */
public class DeleteOrganizationJobPositionsByPositionIdAndDetailsCommand {
    @ItemType(Long.class)
    private List<Long> detailIds;

    private Long organizationId;

    private Long jobPositionId;

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

    public Long getJobPositionId() {
        return jobPositionId;
    }

    public void setJobPositionId(Long jobPositionId) {
        this.jobPositionId = jobPositionId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
