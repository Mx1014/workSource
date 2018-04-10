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
 * </ul>
 *
 */
public class UpdateEnterpriseDetailCommand {
    private Integer organizationId;
    private String name;
    private String displayName;
    private String memberRange;
    private Integer serviceSupportFlag;
    private List<CreateOfficeSiteCommand> officeSites;
    private List<Long> communityIds;

    public Integer getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Integer organizationId) {
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
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
