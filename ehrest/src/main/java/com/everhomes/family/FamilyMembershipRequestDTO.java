// @formatter:off
package com.everhomes.family;

import com.everhomes.util.StringHelper;

public class FamilyMembershipRequestDTO {
    Long id;
    Long familyId;
    String familyName;
    String familyAvatar;
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

    public String getFamilyAvatar() {
        return familyAvatar;
    }

    public void setFamilyAvatar(String familyAvatar) {
        this.familyAvatar = familyAvatar;
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
