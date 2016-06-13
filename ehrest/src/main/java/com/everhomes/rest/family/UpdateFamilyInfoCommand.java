// @formatter:off
package com.everhomes.rest.family;


import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>type: 类型，详情{@link com.everhomes.rest.family.BaseCommand}</li>
 * <li>id: 类型对应的Id，详情{@link com.everhomes.rest.family.BaseCommand}</li>
 * <li>familyName: 家庭名称</li>
 * <li>familyDescription: 家庭描述</li>
 * <li>familyAvatarUri: 家庭头像Id</li>
 * <li>familyAvatarUrl: 家庭头像url</li>
 * <li>memberNickName: 用户在家庭内的昵称</li>
 * <li>memberAvatarUri: 用户在家庭的头像Id</li>
 * <li>memberAvatarUrl: 用户在家庭的头像url</li>
 * <li>proofResourceUrl: 存在该字段有值表名是加速审核的</li>
 * </ul>
 */
public class UpdateFamilyInfoCommand extends BaseCommand{
    
    private String familyName;
    private String familyDescription;
    private String familyAvatarUri;
    private String familyAvatarUrl;
    
    private String memberNickName;
    private String memberAvatarUri;
    private String memberAvatarUrl;
    
    private String proofResourceUri;
    
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

    public String getMemberNickName() {
        return memberNickName;
    }

    public void setMemberNickName(String memberNickName) {
        this.memberNickName = memberNickName;
    }
    
    public String getMemberAvatarUri() {
        return memberAvatarUri;
    }

    public void setMemberAvatarUri(String memberAvatarUri) {
        this.memberAvatarUri = memberAvatarUri;
    }

    public String getMemberAvatarUrl() {
        return memberAvatarUrl;
    }

    public void setMemberAvatarUrl(String memberAvatarUrl) {
        this.memberAvatarUrl = memberAvatarUrl;
    }

    public String getProofResourceUri() {
        return proofResourceUri;
    }

    public void setProofResourceUri(String proofResourceUri) {
        this.proofResourceUri = proofResourceUri;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
