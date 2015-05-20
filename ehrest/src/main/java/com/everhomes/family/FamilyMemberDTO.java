// @formatter:off
package com.everhomes.family;

import com.everhomes.util.StringHelper;


/**
 * <ul>
 * <li>id: 家庭与用户的关联Id</li>
 * <li>familyId: 家庭Id</li>
 * <li>memberUid: 家庭成员Id</li>
 * <li>memberName: 家庭成员名称</li>
 * <li>memberAvatar: 家庭头像Id，图片上传到ContentServer得到的ID</li>
 * <li>cellPhone:用户电话号码</li>
 * </ul>
 */
public class FamilyMemberDTO {
    private Long id;
    private Long familyId;
    private Long memberUid;
    private String memberName;
    private String memberAvatar;
    private String cellPhone;
    
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
    
    public String getCellPhone() {
        return cellPhone;
    }

    public void setCellPhone(String cellPhone) {
        this.cellPhone = cellPhone;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
