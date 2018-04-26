package com.everhomes.rest.organization;

import com.everhomes.util.StringHelper;

import java.sql.Timestamp;

public class OrganizationAndDetailDTO {
    //企业编号
    private Long orgId;
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
    //节点路径
    private String path;
    //节点等级
    private Integer level;
    //节点状态
    private Byte status;
    //部门类型
    private String departmentType;
    //群组类型
    private String groupType;
    //创建时间
    private Timestamp createTime;
    //更新时间
    private Timestamp updateTime;
    //总公司编号
    private Long directlyEnterpriseId;
    //域空间ID
    private Integer namespaceId;
    //群组编号
    private Long groupId;

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
    //管理员标志
    private Long adminTargetId;
    //工作平台标志
    private Byte workPlatformFlag;

    //企业明细编号
    private Long OrgDetailId;
    //手机号
    private String contact;
    //详细地址
    private String address;
    //简称
    private String displayName;
    //手机号的属于者
    private String contactor;
    //人员数量
    private Long memberCount;
    //logo
    private String avatar;

    private String postUri;

    //人员规模
    private String memberRange;

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
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

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public String getDepartmentType() {
        return departmentType;
    }

    public void setDepartmentType(String departmentType) {
        this.departmentType = departmentType;
    }

    public String getGroupType() {
        return groupType;
    }

    public void setGroupType(String groupType) {
        this.groupType = groupType;
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

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
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

    public Byte getWorkPlatformFlag() {
        return workPlatformFlag;
    }

    public void setWorkPlatformFlag(Byte workPlatformFlag) {
        this.workPlatformFlag = workPlatformFlag;
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

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
