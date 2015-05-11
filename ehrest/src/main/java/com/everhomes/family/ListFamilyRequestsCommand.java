// @formatter:off
package com.everhomes.family;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>familyId: 家庭Id</li>
 * <li>pageOffset: 页码（可选）</li>
 * </ul>
 */
public class ListFamilyRequestsCommand {
    private Long familyId;
    private Long pageOffset;
    public ListFamilyRequestsCommand() {
    }

    public Long getFamilyId() {
        return familyId;
    }

    public void setFamilyId(Long familyId) {
        this.familyId = familyId;
    }

    public Long getPageOffset() {
        return pageOffset;
    }

    public void setPageOffset(Long pageOffset) {
        this.pageOffset = pageOffset;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
