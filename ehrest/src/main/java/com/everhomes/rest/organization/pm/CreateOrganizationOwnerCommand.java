package com.everhomes.rest.organization.pm;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * <ul>
 * <li>communityId: 小区id</li>
 * <li>organizationId: 公司id</li>
 * <li>contactName: 联系人名称</li>
 * <li>contactToken:联系人电话</li>
 * <li>orgOwnerTypeId: 业主类型Id</li>
 * <li>gender: 性别 {@link com.everhomes.rest.user.UserGender}</li>
 * <li>birthday: 生日</li>
 * <li>maritalStatus: 婚姻状态</li>
 * <li>job: 工作</li>
 * <li>company: 公司</li>
 * <li>idCardNumber: 身份照号码</li>
 * <li>registeredResidence: 户籍</li>
 * <li>avatar: 头像URI地址</li>
 * <li>addresses: 地址列表</li>
 * <li>ownerAttachments: 附件列表</li>
 * <li>ownerType: ownerType EhCommunities 权限校验时用的</li>
 * <li>ownerId: ownerId, communityId</li>
 * </ul>
 */
public class CreateOrganizationOwnerCommand {

    @NotNull
    private Long organizationId;
    @NotNull
    private Long communityId;
    @NotNull @Size(max = 20)
    private String contactName;
    @NotNull @Size(max = 20)
    private String contactToken;
    @NotNull
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
    @NotNull
    @ItemType(OrganizationOwnerAddressCommand.class)
    @Valid private List<OrganizationOwnerAddressCommand> addresses;

    @ItemType(UploadOrganizationOwnerAttachmentCommand.class)
    private List<UploadOrganizationOwnerAttachmentCommand> ownerAttachments;

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
    public Long getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Long communityId) {
        this.communityId = communityId;
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

    public Long getOrgOwnerTypeId() {
        return orgOwnerTypeId;
    }

    public void setOrgOwnerTypeId(Long orgOwnerTypeId) {
        this.orgOwnerTypeId = orgOwnerTypeId;
    }

    public Byte getGender() {
        return gender;
    }

    public void setGender(Byte gender) {
        this.gender = gender;
    }

    public List<UploadOrganizationOwnerAttachmentCommand> getOwnerAttachments() {
        return ownerAttachments;
    }

    public void setOwnerAttachments(List<UploadOrganizationOwnerAttachmentCommand> ownerAttachments) {
        this.ownerAttachments = ownerAttachments;
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

    public List<OrganizationOwnerAddressCommand> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<OrganizationOwnerAddressCommand> addresses) {
        this.addresses = addresses;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
