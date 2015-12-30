// @formatter:off
package com.everhomes.rest.family;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>id: 用户申请加入家庭的申请Id</li>
 * <li>familyId: 家庭Id</li>
 * <li>familyName: 家庭名称</li>
 * <li>familyAvatar: 家庭头像Id</li>
 * <li>address: 家庭详细地址</li>
 * <li>requestorUid: 申请加入家庭的用户Id</li>
 * <li>requestorName: 申请加入家庭的用户名称</li>
 * <li>requestorAvatarUri: 申请加入家庭的用户头像Id</li>
 * <li>requestorAvatarUrl: 申请加入家庭的用户头像url</li>
 * <li>requestingTime: 申请时间</li>
 * <li>requestorComment: 申请加入原因</li>
 * </ul>
 */
public class FamilyMembershipRequestDTO {
    Long id;
    Long familyId;
    String familyName;
    String familyAvatarUri;
    String familyAvatarUrl;
    String address;
    Long requestorUid;
    String requestorName;
    String requestorAvatar;
    String requestingTime;
    String requestorComment;
    
    public FamilyMembershipRequestDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getFamilyId() {
        return familyId;
    }

    public void setFamilyId(Long familyId) {
        this.familyId = familyId;
    }

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public String getFamilyAvatarUri() {
        return familyAvatarUri;
    }

    public void setFamilyAvatarUri(String familyAvatarUri) {
        this.familyAvatarUri = familyAvatarUri;
    }

    public String getFamilyAvatarUrl() {
        return familyAvatarUrl;
    }

    public void setFamilyAvatarUrl(String familyAvatarUrl) {
        this.familyAvatarUrl = familyAvatarUrl;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Long getRequestorUid() {
        return requestorUid;
    }

    public void setRequestorUid(Long requestorUid) {
        this.requestorUid = requestorUid;
    }

    public String getRequestorName() {
        return requestorName;
    }

    public void setRequestorName(String requestorName) {
        this.requestorName = requestorName;
    }

    public String getRequestorAvatar() {
        return requestorAvatar;
    }

    public void setRequestorAvatar(String requestorAvatar) {
        this.requestorAvatar = requestorAvatar;
    }

    public String getRequestingTime() {
        return requestingTime;
    }

    public void setRequestingTime(String requestingTime) {
        this.requestingTime = requestingTime;
    }

    public String getRequestorComment() {
        return requestorComment;
    }

    public void setRequestorComment(String requestorComment) {
        this.requestorComment = requestorComment;
    }
    
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
