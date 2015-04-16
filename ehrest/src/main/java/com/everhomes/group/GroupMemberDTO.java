// @formatter:off
package com.everhomes.group;

import com.everhomes.util.StringHelper;

public class GroupMemberDTO {
    private Long id;
    private Long groupId;
    private String memberType;
    private Long memberId;
    private Long memberRole;
    private String avatar;
    private Byte memberStatus;
    
    public GroupMemberDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public String getMemberType() {
        return memberType;
    }

    public void setMemberType(String memberType) {
        this.memberType = memberType;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public Long getMemberRole() {
        return memberRole;
    }

    public void setMemberRole(Long memberRole) {
        this.memberRole = memberRole;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
    
    public Byte getMemberStatus() {
        return memberStatus;
    }

    public void setMemberStatus(Byte memberStatus) {
        this.memberStatus = memberStatus;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
