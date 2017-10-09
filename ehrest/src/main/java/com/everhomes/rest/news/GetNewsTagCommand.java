package com.everhomes.rest.news;

import javax.validation.constraints.NotNull;

/**
 * <li>isSearch: 传1只显示筛选项 为空都显示</li>
 */
public class GetNewsTagCommand {
    @NotNull
    private String ownerType;
    @NotNull
    private Long ownerId;
    private Byte isSearch;
    private Long pageAnchor;
    private Integer pageSize;

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

    public Long getPageAnchor() {
        return pageAnchor;
    }

    public void setPageAnchor(Long pageAnchor) {
        this.pageAnchor = pageAnchor;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Byte getIsSearch() {
        return isSearch;
    }

    public void setIsSearch(Byte isSearch) {
        this.isSearch = isSearch;
    }
}
