package com.everhomes.rest.organization;

import com.everhomes.util.StringHelper;

import java.util.Map;

/**
 * <ul>
 * <li>organizationId: 组织id</li>
 * <li>targetType：成员是否注册 参考{@link com.everhomes.rest.organization.OrganizationMemberTargetType}</li>
 * <li>targetId：注册用户对应的userId，未注册填0</li>
 * <li>contactName：成员名称</li>
 * <li>contactToken：联系信息</li>
 * <li>gender：性别</li>
 * <li>employeeNo：工号</li>
 * <li>contactDescription：描述</li>
 * <li>employeeNo: 工号</li>
 * <li>employeeType: 员工类型：0，全职 1，兼职 2，实习 3，劳动派遣 参考{@link com.everhomes.rest.organization.EmployeeType}</li>
 * <li>employeeStatus: 员工状态, 0: 试用 1: 在职 2: 离职 参考{@link com.everhomes.rest.organization.EmployeeStatus}</li>
 * <li>employmentTime: 转正时间</li>
 * <li>checkInTime: 入职时间</li>
 * <li>departmentIds：添加到多部门</li>
 * <li>jobPositionIds：添加到多群组</li>
 * <li>updateLogs: 修改记录</li>
 * </ul>
 */
public class AddOrganizationPersonnelV2Command extends AddOrganizationPersonnelCommand {

    private Map<String,String> updateLogs;

    public AddOrganizationPersonnelV2Command() {
    }

    public Map<String, String> getUpdateLogs() {
        return updateLogs;
    }

    public void setUpdateLogs(Map<String, String> updateLogs) {
        this.updateLogs = updateLogs;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
