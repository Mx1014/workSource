package com.everhomes.rest.organization;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>department: 部门修改记录</li>
 * <li>jobPosition: 职位修改记录</li>
 * <li>jobLevelIds: 职级修改记录</li>
 * </ul>
 */
public class OrganizationMemberUpdatePersonnelDataDTO {

    private String department;
    private String jobPosition;
    private String jobLevelIds;

    public OrganizationMemberUpdatePersonnelDataDTO() {
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getJobPosition() {
        return jobPosition;
    }

    public void setJobPosition(String jobPosition) {
        this.jobPosition = jobPosition;
    }

    public String getJobLevelIds() {
        return jobLevelIds;
    }

    public void setJobLevelIds(String jobLevelIds) {
        this.jobLevelIds = jobLevelIds;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
