package com.everhomes.rest.enterprise;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.address.CreateOfficeSiteCommand;
import com.everhomes.rest.forum.AttachmentDescriptor;
import com.everhomes.rest.organization.OrganizationAddressDTO;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 * <li>name: 企业名称</li>
 * <li>avatar: logo</li>
 * <li>displayName: 企业简称</li>
 * <li>memberRange: 人员规模</li>
 * <li>contactor: 公司管理员姓名</li>
 * <li>entries: 公司管理员手机号</li>
 * <li>officeLocation: 关联的办公地点id</li>
 * <li>communityIds: 关联的管理项目 </li>
 * <li>workbenchFlag: 是否启用工作台 </li>
 * <li>officeSites: 工作地点信息，参考{@link CreateOfficeSiteCommand} </li>
 * <li>pmFlag: 是否是管理公司 </li>
 * <li>projectIds: 管理园区列表 </li>
 * <li>serviceSupportFlag: 是否是服务商 </li>
 * </ul>
 *
 */
public class CreateEnterpriseStandardCommand {

    private java.lang.String name;
    private java.lang.String avatar;
    private java.lang.String displayName;
    private String memberRange;
    private String contactor;
    private String entries;
    private Long officeLocation;
    private List<Long> communityIds;
    private Integer workbenchFlag;
    private List<CreateOfficeSiteCommand> officeSites;
    private Integer pmFlag;
    private List<Long> projectIds;
    private Integer serviceSupportFlag;
    private Integer namespaceId;


    private Boolean checkPrivilege;
    //物业管理公司id
    private Long manageOrganizationId;

    private java.lang.String   description;
    private Long communityId;
    private Long memberCount;
    private String contactsPhone;

    private String address;
    private String checkinDate;
    private String postUri;


    private String organizationType;

    private String longitude;

    private String latitude;
    private String emailDomain;
    private Long serviceUserId;

    @ItemType(OrganizationAddressDTO.class)
    private List<OrganizationAddressDTO> addressDTOs;

    @ItemType(AttachmentDescriptor.class)
    private List<AttachmentDescriptor> attachments;

    private String website;
    private String unifiedSocialCreditCode;



    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getMemberRange() {
        return memberRange;
    }

    public void setMemberRange(String memberRange) {
        this.memberRange = memberRange;
    }

    public String getContactor() {
        return contactor;
    }

    public void setContactor(String contactor) {
        this.contactor = contactor;
    }

    public String getEntries() {
        return entries;
    }

    public void setEntries(String entries) {
        this.entries = entries;
    }

    public Long getOfficeLocation() {
        return officeLocation;
    }

    public void setOfficeLocation(Long officeLocation) {
        this.officeLocation = officeLocation;
    }

    public List<Long> getCommunityIds() {
        return communityIds;
    }

    public void setCommunityIds(List<Long> communityIds) {
        this.communityIds = communityIds;
    }

    public Integer getWorkbenchFlag() {
        return workbenchFlag;
    }

    public void setWorkbenchFlag(Integer workbenchFlag) {
        this.workbenchFlag = workbenchFlag;
    }

    public List<CreateOfficeSiteCommand> getOfficeSites() {
        return officeSites;
    }

    public void setOfficeSites(List<CreateOfficeSiteCommand> officeSites) {
        this.officeSites = officeSites;
    }

    public Integer getPmFlag() {
        return pmFlag;
    }

    public void setPmFlag(Integer pmFlag) {
        this.pmFlag = pmFlag;
    }

    public Integer getServiceSupportFlag() {
        return serviceSupportFlag;
    }

    public void setServiceSupportFlag(Integer serviceSupportFlag) {
        this.serviceSupportFlag = serviceSupportFlag;
    }

    public List<Long> getProjectIds() {
        return projectIds;
    }

    public void setProjectIds(List<Long> projectIds) {
        this.projectIds = projectIds;
    }

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    public Boolean getCheckPrivilege() {
        return checkPrivilege;
    }

    public void setCheckPrivilege(Boolean checkPrivilege) {
        this.checkPrivilege = checkPrivilege;
    }

    public Long getManageOrganizationId() {
        return manageOrganizationId;
    }

    public void setManageOrganizationId(Long manageOrganizationId) {
        this.manageOrganizationId = manageOrganizationId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Long communityId) {
        this.communityId = communityId;
    }

    public Long getMemberCount() {
        return memberCount;
    }

    public void setMemberCount(Long memberCount) {
        this.memberCount = memberCount;
    }

    public String getContactsPhone() {
        return contactsPhone;
    }

    public void setContactsPhone(String contactsPhone) {
        this.contactsPhone = contactsPhone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCheckinDate() {
        return checkinDate;
    }

    public void setCheckinDate(String checkinDate) {
        this.checkinDate = checkinDate;
    }

    public String getPostUri() {
        return postUri;
    }

    public void setPostUri(String postUri) {
        this.postUri = postUri;
    }

    public String getOrganizationType() {
        return organizationType;
    }

    public void setOrganizationType(String organizationType) {
        this.organizationType = organizationType;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getEmailDomain() {
        return emailDomain;
    }

    public void setEmailDomain(String emailDomain) {
        this.emailDomain = emailDomain;
    }

    public Long getServiceUserId() {
        return serviceUserId;
    }

    public void setServiceUserId(Long serviceUserId) {
        this.serviceUserId = serviceUserId;
    }

    public List<OrganizationAddressDTO> getAddressDTOs() {
        return addressDTOs;
    }

    public void setAddressDTOs(List<OrganizationAddressDTO> addressDTOs) {
        this.addressDTOs = addressDTOs;
    }

    public List<AttachmentDescriptor> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<AttachmentDescriptor> attachments) {
        this.attachments = attachments;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getUnifiedSocialCreditCode() {
        return unifiedSocialCreditCode;
    }

    public void setUnifiedSocialCreditCode(String unifiedSocialCreditCode) {
        this.unifiedSocialCreditCode = unifiedSocialCreditCode;
    }

}
