// @formatter:off
package com.everhomes.rest.organization;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.organization.pm.OrganizationOwnerAddressDTO;
import com.everhomes.util.StringHelper;

import java.util.Date;
import java.util.List;

/**
 * <ul>
 *     <li>id:业主 id</li>
 *     <li>organizationId:organizationId</li>
 *     <li>contactName:联系人姓名</li>
 *     <li>contactToken:联系人电话</li>
 *     <li>orgOwnerType:客户类型</li>
 *     <li>gender:性别</li>
 *     <li>birthday:生日</li>
 *     <li>maritalStatus:婚姻状态</li>
 *     <li>job:工作</li>
 *     <li>company:公司</li>
 *     <li>idCardNumber:身份证号码</li>
 *     <li>registeredResidence:户籍</li>
 *     <li>avatar:头像的URI地址</li>
 *     <li>avatarUrl:头像URL链接</li>
 *     <li>livingStatus:在户状态</li>
 *     <li>authType:认证类型</li>
 *     <li>primaryFlag:首要联系人状态</li>
 * </ul>
 */
public class OrganizationOwnerDTO {

    private Long   id;
    private Long   organizationId;
    private String contactName;
    private String contactToken;
    private String orgOwnerType;
    private String gender;
    private Long   birthday;
    private String maritalStatus;
    private String job;
    private String company;
    private String idCardNumber;
    private String registeredResidence;
    private String avatar;
    private String avatarUrl;

    private String livingStatus;
    private String authType;

    private String primaryFlag;

    @ItemType(OrganizationOwnerAddressDTO.class)
    private List<OrganizationOwnerAddressDTO> addresses;
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactToken() {
        return contactToken;
    }

    public void setContactToken(String contactToken) {
        this.contactToken = contactToken;
    }

    public String getOrgOwnerType() {
        return orgOwnerType;
    }

    public void setOrgOwnerType(String orgOwnerType) {
        this.orgOwnerType = orgOwnerType;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Long getBirthday() {
        return birthday;
    }

    public void setBirthday(Long birthday) {
        this.birthday = birthday;
    }

    public String getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(String maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public String getPrimaryFlag() {
        return primaryFlag;
    }

    public void setPrimaryFlag(String primaryFlag) {
        this.primaryFlag = primaryFlag;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getIdCardNumber() {
        return idCardNumber;
    }

    public void setIdCardNumber(String idCardNumber) {
        this.idCardNumber = idCardNumber;
    }

    public String getRegisteredResidence() {
        return registeredResidence;
    }

    public void setRegisteredResidence(String registeredResidence) {
        this.registeredResidence = registeredResidence;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getLivingStatus() {
        return livingStatus;
    }

    public void setLivingStatus(String livingStatus) {
        this.livingStatus = livingStatus;
    }

    public String getAuthType() {
        return authType;
    }

    public void setAuthType(String authType) {
        this.authType = authType;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

	public List<OrganizationOwnerAddressDTO> getAddresses() {
		return addresses;
	}

	public void setAddresses(List<OrganizationOwnerAddressDTO> addresses) {
		this.addresses = addresses;
	}
}
