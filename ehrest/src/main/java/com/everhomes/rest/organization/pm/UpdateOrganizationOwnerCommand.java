package com.everhomes.rest.organization.pm;

import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * <ul>
 * <li>id: 业主id</li>
 * <li>organizationId: 公司id</li>
 * <li>contactName: 联系人名称</li>
 * <li>orgOwnerTypeId: 业主类型Id</li>
 * <li>gender: 性别 {@link com.everhomes.rest.user.UserGender}</li>
 * <li>birthday: 生日</li>
 * <li>maritalStatus: 婚姻状态</li>
 * <li>job: 工作</li>
 * <li>company: 公司</li>
 * <li>idCardNumber: 身份照号码</li>
 * <li>registeredResidence: 户籍</li>
 * <li>avatar: 头像URI地址</li>
 * <li>ownerType: ownerType EhCommunities 权限校验时用的</li>
 * <li>ownerId: ownerId, communityId</li>
 * </ul>
 */
public class UpdateOrganizationOwnerCommand {

    @NotNull private Long id;
    @NotNull private Long organizationId;
    @Size(max = 20)
    private String contactName;
    @Size(max = 20)
    private String contactToken;
    private Long orgOwnerTypeId;
    private Byte gender;
    private Long birthday;
    @Size(max = 10)
    private String maritalStatus;
    @Size(max = 10)
    private String job;
    @Size(max = 100)
    private String company;
    @Size(max = 18)
    private String idCardNumber;
    @Size(max = 128)
    private String registeredResidence;
    @Size(max = 1024)
    private String avatar;

    private String ownerType;
    private Long ownerId;

    public String getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(String ownerType) {
        this.ownerType = ownerType;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public Long getOrgOwnerTypeId() {
        return orgOwnerTypeId;
    }

    public void setOrgOwnerTypeId(Long orgOwnerTypeId) {
        this.orgOwnerTypeId = orgOwnerTypeId;
    }

    public String getContactToken() {
        return contactToken;
    }

    public void setContactToken(String contactToken) {
        this.contactToken = contactToken;
    }

    public Byte getGender() {
        return gender;
    }

    public void setGender(Byte gender) {
        this.gender = gender;
    }

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
