package com.everhomes.rest.usercurrentscene;

import com.everhomes.util.StringHelper;

import java.sql.Timestamp;

/**
 * <ul>
 * <li>id: ID</li>
 * <li>uid: 人员ID</li>
 * <li>namespaceId: 域空间id</li>
 * <li>communityId: 园区ID</li>
 * <li>communityType: 园区类型</li>
 * <li>createTime: </li>
 * <li>updateTime: </li>
 * </ul>
 */
public class UserCurrentSceneDTO {
    private Long id ;
    private Long uid ;
    private Integer namespaceId;
    private Long communityId ;
    private Byte communityType ;
    private String signToken ;
    private Timestamp createTime;
    private Timestamp updateTime;

    public String getSignToken() {
        return signToken;
    }

    public void setSignToken(String signToken) {
        this.signToken = signToken;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    public Long getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Long communityId) {
        this.communityId = communityId;
    }

    public Byte getCommunityType() {
        return communityType;
    }

    public void setCommunityType(Byte communityType) {
        this.communityType = communityType;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public Timestamp getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }

    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
