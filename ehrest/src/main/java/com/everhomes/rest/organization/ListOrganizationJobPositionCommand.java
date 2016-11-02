// @formatter:off
package com.everhomes.rest.organization;

import com.everhomes.util.StringHelper;

/**
 * <p>
 * <ul>
 * <li>ownerType: 所属实体类型</li>
 * <li>ownerId: 所属实体id</li>
 * <li>keywords: 关键字搜索</li>
 * </ul>
 */
public class ListOrganizationJobPositionCommand {

    private String ownerType;

    private Long ownerId;

    private String keywords;


    public ListOrganizationJobPositionCommand() {
    }

    public String getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(String ownerType) {
        this.ownerType = ownerType;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
