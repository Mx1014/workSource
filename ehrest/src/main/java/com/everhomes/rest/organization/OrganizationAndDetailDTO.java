package com.everhomes.rest.organization;

import com.everhomes.rest.user.UserDTO;
import com.everhomes.util.StringHelper;
import org.apache.commons.collections.CollectionUtils;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class OrganizationAndDetailDTO {
    //企业编号
    private Long organizationId;
    //企业父类编号
    private Long parentId;
    //节点属性
    private String organizationType;
    //节点名称
    private String name;
    //节点地址编号
    private Long addressId;
    //节点描述
    private String description;
    //该公司管理的项目的编号集合
    private List<Long> communityIds;



    //部门类型
    private String departmentType;

    //创建时间
    private Timestamp createTime;
    //更新时间
    private Timestamp updateTime;
    //总公司编号
    private Long directlyEnterpriseId;
    //域空间ID
    private Integer namespaceId;


    private Byte showFlag;
    private String namespaceOrganizationToken;
    private String namespaceOrganizationType;
    private Integer size;
    //创建者的编号
    private Long creatorUid;
    //操作者的编号
    private Long operatorUid;
    //管理员标志
    private Byte setAdminFlag;
    //邮箱
    private String emailContent;
    //网站
    private String website;
    //统一社会信用代码
    private String unifiedSocialCreditCode;
    //
    private Integer order;
    //管理员id
    private Long adminTargetId;
    //工作平台标志
    private Byte workbenchFlag;

    //企业明细编号
    private Long OrgDetailId;
    //手机号
    private String contact;
    //详细地址
    private String address;
    //简称
    private String displayName;
    //管理员姓名
    private String contactor;
    //管理员手机号
    private String entries;
    //人员数量
    private Long memberCount;
    //logo
    private String avatar;

    private String postUri;

    //是否是管理公司（1-是，0-否）
    private Byte pmFlag;
    //是否是服务商（1-是，0-否）
    private Byte serviceSupportFlag;

    //人员规模
    private String memberRange;

    //项目以及项目关联的楼栋和门牌
    private List<OfficeSiteDTO> officeSites;

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getOrganizationType() {
        return organizationType;
    }

    public void setOrganizationType(String organizationType) {
        this.organizationType = organizationType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getAddressId() {
        return addressId;
    }

    public void setAddressId(Long addressId) {
        this.addressId = addressId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public String getDepartmentType() {
        return departmentType;
    }

    public void setDepartmentType(String departmentType) {
        this.departmentType = departmentType;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public Timestamp getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }

    public Long getDirectlyEnterpriseId() {
        return directlyEnterpriseId;
    }

    public void setDirectlyEnterpriseId(Long directlyEnterpriseId) {
        this.directlyEnterpriseId = directlyEnterpriseId;
    }

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    public Byte getShowFlag() {
        return showFlag;
    }

    public void setShowFlag(Byte showFlag) {
        this.showFlag = showFlag;
    }

    public String getNamespaceOrganizationToken() {
        return namespaceOrganizationToken;
    }

    public void setNamespaceOrganizationToken(String namespaceOrganizationToken) {
        this.namespaceOrganizationToken = namespaceOrganizationToken;
    }

    public String getNamespaceOrganizationType() {
        return namespaceOrganizationType;
    }

    public void setNamespaceOrganizationType(String namespaceOrganizationType) {
        this.namespaceOrganizationType = namespaceOrganizationType;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public Long getCreatorUid() {
        return creatorUid;
    }

    public void setCreatorUid(Long creatorUid) {
        this.creatorUid = creatorUid;
    }

    public Long getOperatorUid() {
        return operatorUid;
    }

    public void setOperatorUid(Long operatorUid) {
        this.operatorUid = operatorUid;
    }

    public Byte getSetAdminFlag() {
        return setAdminFlag;
    }

    public void setSetAdminFlag(Byte setAdminFlag) {
        this.setAdminFlag = setAdminFlag;
    }

    public String getEmailContent() {
        return emailContent;
    }

    public void setEmailContent(String emailContent) {
        this.emailContent = emailContent;
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

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public Long getAdminTargetId() {
        return adminTargetId;
    }

    public void setAdminTargetId(Long adminTargetId) {
        this.adminTargetId = adminTargetId;
    }

    public Byte getWorkbenchFlag() {
        return workbenchFlag;
    }

    public void setWorkbenchFlag(Byte workbenchFlag) {
        this.workbenchFlag = workbenchFlag;
    }

    public Long getOrgDetailId() {
        return OrgDetailId;
    }

    public void setOrgDetailId(Long orgDetailId) {
        OrgDetailId = orgDetailId;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getContactor() {
        return contactor;
    }

    public void setContactor(String contactor) {
        this.contactor = contactor;
    }

    public Long getMemberCount() {
        return memberCount;
    }

    public void setMemberCount(Long memberCount) {
        this.memberCount = memberCount;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getPostUri() {
        return postUri;
    }

    public void setPostUri(String postUri) {
        this.postUri = postUri;
    }

    public String getMemberRange() {
        return memberRange;
    }

    public void setMemberRange(String memberRange) {
        this.memberRange = memberRange;
    }


    public List<OfficeSiteDTO> getOfficeSites() {
        if(CollectionUtils.isEmpty(officeSites)){
            return new ArrayList<OfficeSiteDTO>();
        }else{
            return officeSites;
        }
    }

    public void setOfficeSites(List<OfficeSiteDTO> officeSites) {
        this.officeSites = officeSites;
    }

    public Byte getPmFlag() {
        return pmFlag;
    }

    public void setPmFlag(Byte pmFlag) {
        this.pmFlag = pmFlag;
    }

    public Byte getServiceSupportFlag() {
        return serviceSupportFlag;
    }

    public void setServiceSupportFlag(Byte serviceSupportFlag) {
        this.serviceSupportFlag = serviceSupportFlag;
    }

    public String getEntries() {
        return entries;
    }

    public void setEntries(String entries) {
        this.entries = entries;
    }

    public List<Long> getCommunityIds() {
        return communityIds;
    }

    public void setCommunityIds(List<Long> communityIds) {
        this.communityIds = communityIds;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
