package com.everhomes.rest.techpark.punch;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>adminFlag: 是否管理员 1是 0否</li>
 * <li>queryPrivilege: 是否有查询统计权限 1是 0否</li>
 * <li>deptName: 部门经理- 部门名称</li>
 * <li>deptId: 部门经理- 部门id</li>
 * </ul>
 */
public class CheckUserStatisticPrivilegeResponse {
    private Byte adminFlag;
    private Byte queryPrivilege;
    private String deptName;
    private Long deptId;

    public Byte getAdminFlag() {
        return adminFlag;
    }

    public void setAdminFlag(Byte adminFlag) {
        this.adminFlag = adminFlag;
    }

    public Long getDeptId() {
        return deptId;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public Byte getQueryPrivilege() {
        return queryPrivilege;
    }

    public void setQueryPrivilege(Byte queryPrivilege) {
        this.queryPrivilege = queryPrivilege;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
