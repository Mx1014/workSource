package com.everhomes.rest.enterprise;

import com.everhomes.rest.address.CreateOfficeSiteCommand;
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
}
