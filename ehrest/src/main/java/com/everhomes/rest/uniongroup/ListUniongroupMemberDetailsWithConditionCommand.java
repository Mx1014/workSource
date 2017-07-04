package com.everhomes.rest.uniongroup;

import com.everhomes.util.StringHelper;

/**
 * Created by Administrator on 2017/7/4.
 */
public class ListUniongroupMemberDetailsWithConditionCommand {
    private Long ownerId;
    private String keywords;
    private Long departmentId;
    private Long groupId;
    private String groupType;
    private Boolean allGroupFlag;

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public String getGroupType() {
        return groupType;
    }

    public void setGroupType(String groupType) {
        this.groupType = groupType;
    }

    public Boolean getAllGroupFlag() {
        return allGroupFlag;
    }

    public void setAllGroupFlag(Boolean allGroupFlag) {
        this.allGroupFlag = allGroupFlag;
    }
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
