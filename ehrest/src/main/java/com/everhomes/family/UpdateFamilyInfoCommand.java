// @formatter:off
package com.everhomes.family;


import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>type: 类型，详情{@link com.everhomes.family.BaseCommand}</li>
 * <li>id: 类型对应的Id，详情{@link com.everhomes.family.BaseCommand}</li>
 * <li>familyName: 家庭名称</li>
 * <li>familyDescription: 家庭描述</li>
 * <li>familyAvatar: 家庭头像Id</li>
 * <li>memberNickName: 用户在家庭内的昵称</li>
 * <li>memberAvatar: 用户在家庭的头像Id</li>
 * </ul>
 */
public class UpdateFamilyInfoCommand extends BaseCommand{
    
    private String familyName;
    private String familyDescription;
    private String familyAvatar;
    
    private String memberNickName;
    private String memberAvatar;
    
    public UpdateFamilyInfoCommand() {
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
