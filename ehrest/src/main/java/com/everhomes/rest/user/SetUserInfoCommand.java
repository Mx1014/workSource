package com.everhomes.rest.user;

import com.everhomes.util.StringHelper;

/**
 * 跟新用户信息
 * 
 * @author elians
 *         <ul>
 *         <li>nickName:昵称</li>
 *         <li>avatarUri:头像uri</li>
 *         <li>avatarUrl:头像url</li>
 *         <li>statusLine:状态</li>
 *         <li>gender:性别</li>
 *         <li>birthday:生日</li>
 *         <li>homeTown:家乡</li>
 *         <li>company:公司</li>
 *         <li>school:学校</li>
 *         <li>occupation:职业</li>
 *         <li>email: 邮箱</li>
 *         </ul>
 */
public class SetUserInfoCommand {
    private String nickName;
    private String avatarUri;
    private String avatarUrl;
    private String statusLine;
    private Byte gender;
    private String birthday;
    private Long homeTown;
    private String company;
    private String school;
    private String occupation;
    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public SetUserInfoCommand() {
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getAvatarUri() {
        return avatarUri;
    }

    public void setAvatarUri(String avatarUri) {
        this.avatarUri = avatarUri;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
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

    public Long getHomeTown() {
        return homeTown;
    }

    public void setHomeTown(Long homeTown) {
        this.homeTown = homeTown;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
