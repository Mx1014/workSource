package com.everhomes.user;

import com.everhomes.util.StringHelper;

/**
 * 跟新用户信息
 * 
 * @author elians
 *         <ul>
 *         <li>nickName:昵称</li>
 *         <li>avatar:头像</li>
 *         <li>statusLine:状态</li>
 *         <li>gender:性别</li>
 *         <li>birthday:生日</li>
 *         <li>homeTown:家乡</li>
 *         <li>company:公司</li>
 *         <li>school:学校</li>
 *         <li>occupation:职业</li>
 *         </ul>
 */
public class SetUserInfoCommand {
    private String nickName;
    private String avatar;
    private String statusLine;
    private Byte gender;
    private String birthday;
    private Long homeTown;
    private String company;
    private String school;
    private Long occupation;
    private String occupationName;

    public SetUserInfoCommand() {
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
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

    public Long getOccupation() {
        return occupation;
    }

    public void setOccupation(Long occupation) {
        this.occupation = occupation;
    }

    public String getOccupationName() {
        return occupationName;
    }

    public void setOccupationName(String occupationName) {
        this.occupationName = occupationName;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
