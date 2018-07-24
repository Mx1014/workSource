package com.everhomes.rest.remind;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>ownerType: 默认EhOrganizations，不用填</li>
 * <li>ownerId: 总公司ID，必填</li>
 * <li>keyWord: 标题关键字，可选项</li>
 * <li>remindCategoryId: 分类ID</li>
 * <li>status: 状态，参考{@link com.everhomes.rest.remind.RemindStatus}</li>
 * <li>pageOffset: 页码，不填或者从1开始，和pageSize两个都不填则不分页</li>
 * <li>pageSize: 每页大小，和pageOffset两个都不填则不分页</li>
 * </ul>
 */
public class ListSelfRemindCommand {
    private String ownerType;
    private Long ownerId;
    private String keyWord;
    private Long remindCategoryId;
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

    public Long getRemindCategoryId() {
        return remindCategoryId;
    }

    public void setRemindCategoryId(Long remindCategoryId) {
        this.remindCategoryId = remindCategoryId;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
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

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
