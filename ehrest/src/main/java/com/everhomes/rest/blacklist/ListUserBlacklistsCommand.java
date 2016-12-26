package com.everhomes.rest.blacklist;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>ownerType: 范围类型 EhOrganizations，EhCommunities..</li>
 * <li>ownerId: 范围id</li>
 * <li>startTime: 开始时间</li>
 * <li>endTime: 结束时间</li>
 * <li>keywords: 关键字</li>
 * <li>pageSize: 页数</li>
 * <li>pageAnchor: 瞄点</li>
 * </ul>
 */
public class ListUserBlacklistsCommand {

    private String ownerType;

    private Long ownerId;

    private Long startTime;

    private Long endTime;

    private String keywords;

    private Integer pageSize;

    private Long pageAnchor;

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

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
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

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
