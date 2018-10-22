package com.everhomes.rest.usercurrentscene;

import com.everhomes.util.StringHelper;

import java.sql.Timestamp;

/**
 * <ul>
 * <li>id: ID</li>
 * <li>uid: 人员ID (必填)</li>
 * <li>namespaceId: 域空间id(必填)</li>
 * <li>communityId: 园区ID(必填)</li>
 * <li>communityType: 园区类型(必填) {@link com.everhomes.rest.community.CommunityType}</li>
 * </ul>
 */
public class UserCurrentSceneCommand {
    private Long id ;
    private Long uid ;
    private Integer namespaceId;
    private Long communityId ;
    private Byte communityType ;
    private String signToken ;

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

    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
