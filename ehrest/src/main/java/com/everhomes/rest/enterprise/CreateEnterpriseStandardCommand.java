package com.everhomes.rest.enterprise;

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
 *
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
    private boolean workbenchFlag;


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

    public boolean isWorkbenchFlag() {
        return workbenchFlag;
    }

    public void setWorkbenchFlag(boolean workbenchFlag) {
        this.workbenchFlag = workbenchFlag;
    }
}
