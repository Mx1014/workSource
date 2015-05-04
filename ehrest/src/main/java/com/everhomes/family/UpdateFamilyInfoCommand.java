// @formatter:off
package com.everhomes.family;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>familyId: 家庭Id</li>
 * <li>familyName: 家庭名称</li>
 * <li>familyDescription: 家庭描述</li>
 * <li>familyAvatar: 家庭头像Id</li>
 * <li>memberNickName: 用户在家庭内的昵称</li>
 * <li>memberAvatar: 用户在家庭的头像Id</li>
 * </ul>
 */
public class UpdateFamilyInfoCommand {
    @NotNull
    private Long familyId;
    
    private String familyName;
    private String familyDescription;
    private String familyAvatar;
    
    private String memberNickName;
    private String memberAvatar;
    
    public UpdateFamilyInfoCommand() {
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

    public String getFamilyDescription() {
        return familyDescription;
    }

    public void setFamilyDescription(String familyDescription) {
        this.familyDescription = familyDescription;
    }

    public String getFamilyAvatar() {
        return familyAvatar;
    }

    public void setFamilyAvatar(String familyAvatar) {
        this.familyAvatar = familyAvatar;
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
    
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
