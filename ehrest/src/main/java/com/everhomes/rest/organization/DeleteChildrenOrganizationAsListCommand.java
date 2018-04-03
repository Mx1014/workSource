package com.everhomes.rest.organization;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * <ul>
 * <li>ids：机构id集合</li>
 * <li>enterpriseId: 当前总公司的ID</li>
 * <li>tag: 标识{@link com.everhomes.rest.organization.OrganizationGroupType}</li>
 * </ul>
 */
public class DeleteChildrenOrganizationAsListCommand {

    @ItemType(Long.class)
    private List<Long> ids;

    private Long enterpriseId;

    private String tag;

    public DeleteChildrenOrganizationAsListCommand() {
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

    public List<Long> getIds() {
        return ids;
    }

    public void setIds(List<Long> ids) {
        this.ids = ids;
    }

    public Long getEnterpriseId() {
        return enterpriseId;
    }

    public void setEnterpriseId(Long enterpriseId) {
        this.enterpriseId = enterpriseId;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}
