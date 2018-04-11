// @formatter:off
package com.everhomes.rest.user;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>id: 用户id</li>
 *     <li>accountName: 用户名</li>
 *     <li>nickName: 用户昵称</li>
 *     <li>avatarUrl: 用户头像url</li>
 *     <li>statusLine: 用户状态</li>
 *     <li>gender: 用户性别.0代表未知，1为男性，2为女性</li>
 *     <li>birthday: 用户生日</li>
 *     <li>homeTown: 家乡id</li>
 *     <li>hometownName: 名字</li>
 *     <li>company: 公司</li>
 *     <li>school: 学校</li>
 *     <li>occupation: 职业</li>
 *     <li>communityId: 小区ID</li>
 *     <li>communityName: 小区名</li>
 *     <li>addressId: 地址id</li>
 *     <li>address: 地址</li>
 *     <li>regionId: 城市ID</li>
 *     <li>regionName: 城市名</li>
 *     <li>regionPath: 城市路径</li>
 *     <li>avatarUri: 用户头像url</li>
 *     <li>namespaceId: namespaceId</li>
 *     <li>regionCode: regionCode</li>
 *     <li>phone: phone</li>
 *     <li>email: email</li>
 *     <li>registerDaysDesc: registerDaysDesc</li>
 * </ul>
 */
public class UserInfoDTO {

    private Long id;
    private String accountName;
    private String nickName;
    private String avatarUrl;
    private String statusLine;
    private Byte gender;
    private String birthday;
    private Long homeTown;
    private String hometownName;
    private String company;
    private String school;
    private String occupation;
    private Long communityId;
    private String communityName;
    private Long addressId;
    private String address;
    private Long regionId;
    private String regionName;
    private String regionPath;
    private String avatarUri;
    private Integer namespaceId;
    private Integer regionCode;
    private String phone;

    private String email;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
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

    public String getHometownName() {
        return hometownName;
    }

    public void setHometownName(String hometownName) {
        this.hometownName = hometownName;
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

    public Long getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Long communityId) {
        this.communityId = communityId;
    }

    public String getCommunityName() {
        return communityName;
    }

    public void setCommunityName(String communityName) {
        this.communityName = communityName;
    }

    public Long getAddressId() {
        return addressId;
    }

    public void setAddressId(Long addressId) {
        this.addressId = addressId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Long getRegionId() {
        return regionId;
    }

    public void setRegionId(Long regionId) {
        this.regionId = regionId;
    }

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    public String getRegionPath() {
        return regionPath;
    }

    public void setRegionPath(String regionPath) {
        this.regionPath = regionPath;
    }

    public String getAvatarUri() {
        return avatarUri;
    }

    public void setAvatarUri(String avatarUri) {
        this.avatarUri = avatarUri;
    }

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    public Integer getRegionCode() {
        return regionCode;
    }

    public void setRegionCode(Integer regionCode) {
        this.regionCode = regionCode;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRegisterDaysDesc() {
        return registerDaysDesc;
    }

    public void setRegisterDaysDesc(String registerDaysDesc) {
        this.registerDaysDesc = registerDaysDesc;
    }

    private String registerDaysDesc;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
