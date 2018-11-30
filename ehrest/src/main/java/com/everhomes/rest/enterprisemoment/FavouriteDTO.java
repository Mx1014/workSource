package com.everhomes.rest.enterprisemoment;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>id: id</li>
 * <li>enterpriseMomentId: 动态id</li>
 * <li>detailId: detailid</li>
 * <li>contactName: 创建人名称</li>
 * <li>userId: 创建人id</li>
 * <li>avatarUrl: 创建人头像</li>
 * <li>createTime: 点赞时间</li>
 * </ul>
 */

public class FavouriteDTO {
    private Long id;
    private Long enterpriseMomentId;
    private Long userId;
    private String contactName;
    private Long detailId;
    private String avatarUrl;
    private Long createTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getEnterpriseMomentId() {
        return enterpriseMomentId;
    }

    public void setEnterpriseMomentId(Long enterpriseMomentId) {
        this.enterpriseMomentId = enterpriseMomentId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public Long getDetailId() {
        return detailId;
    }

    public void setDetailId(Long detailId) {
        this.detailId = detailId;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
