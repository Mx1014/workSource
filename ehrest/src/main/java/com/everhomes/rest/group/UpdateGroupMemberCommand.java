// @formatter:off
package com.everhomes.rest.group;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>groupId：group id</li>
 * <li>memberId：group成员id</li>
 * <li>memberNickName：group成员昵称</li>
 * <li>memberAvatar：group成员头像URI</li>
 * <li>phonePrivateFlag: group成员是否显示手机号标记，{@link com.everhomes.rest.group.GroupMemberPhonePrivacy}</li>
 * <li>muteNotificationFlag: group成员是否免打扰标记，{@link com.everhomes.rest.group.GroupMemberMuteNotificationFlag}</li>
 * </ul>
 */
public class UpdateGroupMemberCommand {
    private Long groupId;
    
    private Long memberId;
    
    private String memberNickName;
    
    private String memberAvatar;

    private Byte phonePrivateFlag;
    
    private Byte muteNotificationFlag;

    public UpdateGroupMemberCommand() {
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public String getMemberNickName() {
        return memberNickName;
    }

    public void setMemberNickName(String memberNickName) {
        this.memberNickName = memberNickName;
    }

    public String getMemberAvatar() {
        return memberAvatar;
    }

    public void setMemberAvatar(String memberAvatar) {
        this.memberAvatar = memberAvatar;
    }

    public Byte getPhonePrivateFlag() {
        return phonePrivateFlag;
    }

    public void setPhonePrivateFlag(Byte phonePrivateFlag) {
        this.phonePrivateFlag = phonePrivateFlag;
    }

    public Byte getMuteNotificationFlag() {
        return muteNotificationFlag;
    }

    public void setMuteNotificationFlag(Byte muteNotificationFlag) {
        this.muteNotificationFlag = muteNotificationFlag;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
