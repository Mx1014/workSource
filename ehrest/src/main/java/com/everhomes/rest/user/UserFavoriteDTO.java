package com.everhomes.rest.user;

import java.sql.Timestamp;

import com.everhomes.util.StringHelper;
/**
 * 
 * @author elians
 *<ul>
 *<li>targetType:收藏类型,topic-帖子,biz-商家</li>
 *<li>targetId:收藏对象的Id</li>
 *<li>ownerUid:收藏者ID</li>
 *<li>createTime:创建时间</li>
 *<ul>
 */
public class UserFavoriteDTO {
    private Long id;
    private Long ownerUid;
    private String targetType;
    private Long targetId;
    private Timestamp createTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOwnerUid() {
        return ownerUid;
    }

    public void setOwnerUid(Long ownerUid) {
        this.ownerUid = ownerUid;
    }

    public String getTargetType() {
        return targetType;
    }

    public void setTargetType(String targetType) {
        this.targetType = targetType;
    }

    public Long getTargetId() {
        return targetId;
    }

    public void setTargetId(Long targetId) {
        this.targetId = targetId;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
