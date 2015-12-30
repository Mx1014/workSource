// @formatter:off
package com.everhomes.rest.family;

import java.sql.Timestamp;

import com.everhomes.util.StringHelper;


/**
 * <ul>
 * <li>id: 家庭与用户的关联Id</li>
 * <li>familyId: 家庭Id</li>
 * <li>memberUid: 家庭成员Id</li>
 * <li>memberName: 家庭成员名称</li>
 * <li>memberAvatarUri: 家庭头像Id，图片上传到ContentServer得到的ID</li>
 * <li>memberAvatarUrl: 家庭头像url</li>
 * <li>cellPhone：用户电话号码</li>
 * <li>statusLine：用户状态</li>
 * <li>gender：用户性别.0代表未知，1为男性，2为女性</li>
 * <li>birthday：用户生日</li>
 * <li>occupation：职业名称</li>
 * <li>createTime：用户加入时间</li>
 * </ul>
 */
public class FamilyMemberDTO {
    private Long id;
    private Long familyId;
    private Long memberUid;
    private String memberName;
    private String memberAvatarUri;
    private String memberAvatarUrl;
    private String cellPhone;
    private String statusLine;
    private Byte gender;
    private String birthday;
    private String occupation;
    private Timestamp createTime;
    
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

    public String getCellPhone() {
        return cellPhone;
    }

    public void setCellPhone(String cellPhone) {
        this.cellPhone = cellPhone;
    }

    public String getStatusLine() {
        return statusLine;
    }

    public void setStatusLine(String statusLine) {
        this.statusLine = statusLine;
    }

    public Byte getGender() {
        return gender;
    }

    public void setGender(Byte gender) {
        this.gender = gender;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
