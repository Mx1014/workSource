// @formatter:off
package com.everhomes.family;

import com.everhomes.util.StringHelper;

public class FamilyMemberDTO {
    private Long id;
    private Long familyId;
    private Long memberUid;
    private String memberName;
    private String memberAvatar;
    
    public FamilyMemberDTO() {
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

    public Long getMemberUid() {
        return memberUid;
    }

    public void setMemberUid(Long memberUid) {
        this.memberUid = memberUid;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getMemberAvatar() {
        return memberAvatar;
    }

    public void setMemberAvatar(String memberAvatar) {
        this.memberAvatar = memberAvatar;
    }
    
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
