package com.everhomes.remind;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>namespaceId: 域空间ID</li>
 * <li>ownerType: 默认EhOrganizations，不用填</li>
 * <li>ownerId: 总公司ID，必填</li>
 * <li>shareUserId: 共享人的userId</li>
 * <li>currentUserDetailId: 当前用户的detailId</li>
 * <li>keyWord: 标题关键字，可选项</li>
 * <li>status: 状态，参考{@link com.everhomes.rest.remind.RemindStatus}</li>
 * <li>pageOffset: 页码，不填或者从1开始</li>
 * <li>pageSize: 每页大小</li>
 * </ul>
 */
public class QueryShareRemindsCondition {
    private Integer namespaceId;
    private Long shareUserId;
    private Long currentUserDetailId;
    private String ownerType;
    private Long ownerId;
    private String keyWord;
    private Byte status;
    private Integer pageOffset;
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

    public String getKeyWord() {
        return keyWord;
    }

    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
    }

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    public Long getShareUserId() {
        return shareUserId;
    }

    public void setShareUserId(Long shareUserId) {
        this.shareUserId = shareUserId;
    }

    public Long getCurrentUserDetailId() {
        return currentUserDetailId;
    }

    public void setCurrentUserDetailId(Long currentUserDetailId) {
        this.currentUserDetailId = currentUserDetailId;
    }

    public Integer getPageOffset() {
        return pageOffset;
    }

    public void setPageOffset(Integer pageOffset) {
        this.pageOffset = pageOffset;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
