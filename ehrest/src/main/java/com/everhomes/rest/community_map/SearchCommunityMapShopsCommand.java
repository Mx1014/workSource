package com.everhomes.rest.community_map;

import com.everhomes.util.StringHelper;

/**
 * @author sw on 2017/9/25.
 */
public class SearchCommunityMapShopsCommand {
    private String ownerType;
    private Long ownerId;
    private Long buildingId;
    private String keyword;

    private Long pageAnchor;
    private Integer pageSize;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Long getPageAnchor() {
        return pageAnchor;
    }

    public void setPageAnchor(Long pageAnchor) {
        this.pageAnchor = pageAnchor;
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

    public Long getBuildingId() {
        return buildingId;
    }

    public void setBuildingId(Long buildingId) {
        this.buildingId = buildingId;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
}
