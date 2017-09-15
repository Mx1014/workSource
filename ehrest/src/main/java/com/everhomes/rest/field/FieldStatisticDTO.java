package com.everhomes.rest.field;

/**
 * <ul>
 *     <li>communityId: 项目id</li>
 *     <li>itemId: 选项在系统中的id</li>
 *     <li>itemDisplayName: 选项在域空间的显示名</li>
 *     <li>referenceCount: 被引用次数</li>
 * </ul>
 * Created by ying.xiong on 2017/8/1.
 */
public class FieldStatisticDTO {

    private Long communityId;

    private Long itemId;

    private String itemDisplayName;

    private Long referenceCount;

    public Long getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Long communityId) {
        this.communityId = communityId;
    }

    public String getItemDisplayName() {
        return itemDisplayName;
    }

    public void setItemDisplayName(String itemDisplayName) {
        this.itemDisplayName = itemDisplayName;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public Long getReferenceCount() {
        return referenceCount;
    }

    public void setReferenceCount(Long referenceCount) {
        this.referenceCount = referenceCount;
    }
}
