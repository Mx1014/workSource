package com.everhomes.rest.enterprise;

import com.everhomes.rest.address.CreateOfficeSiteCommand;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 * <li>organizationId: 企业id</li>
 * <li>name: 企业名称</li>
 * <li>displayName: 企业简称</li>
 * <li>memberRange: 人员规模</li>
 * <li>officeLocation: 关联的办公地点id</li>
 * <li>communityIds: 关联的管理项目 </li>
 * <li>serviceSupportFlag: 是否是服务商 </li>
 * <li>avatar: logo</li>
 * </ul>
 *
 */
public class UpdateEnterpriseDetailCommand {
    private Integer pmFlag;
    private Integer namespaceId;
    private Long organizationId;
    private String name;
    private String displayName;
    private String memberRange;
    private Integer serviceSupportFlag;
    private List<CreateOfficeSiteCommand> officeSites;
    private List<Long> communityIds;
    private String entries;
    private String avatar;

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public Integer getServiceSupportFlag() {
        return serviceSupportFlag;
    }

    public void setServiceSupportFlag(Integer serviceSupportFlag) {
        this.serviceSupportFlag = serviceSupportFlag;
    }

    public List<CreateOfficeSiteCommand> getOfficeSites() {
        return officeSites;
    }

    public void setOfficeSites(List<CreateOfficeSiteCommand> officeSites) {
        this.officeSites = officeSites;
    }

    public List<Long> getCommunityIds() {
        return communityIds;
    }

    public void setCommunityIds(List<Long> communityIds) {
        this.communityIds = communityIds;
    }

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    public Integer getPmFlag() {
        return pmFlag;
    }

    public void setPmFlag(Integer pmFlag) {
        this.pmFlag = pmFlag;
    }

    public String getEntries() {
        return entries;
    }

    public void setEntries(String entries) {
        this.entries = entries;
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
