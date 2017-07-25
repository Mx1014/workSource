// @formatter:off
package com.everhomes.uniongroup;

import com.everhomes.server.schema.tables.pojos.EhUniongroupMemberDetails;
import com.everhomes.util.StringHelper;

import java.util.List;
import java.util.Map;

public class UniongroupMemberDetail extends EhUniongroupMemberDetails {

    private static final long serialVersionUID = 8751516334864351356L;

    private String employeeNo;

    private Map<Long, String> department;

    private Map<Long, String> jobPosition;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

    public Map<Long, String> getDepartment() {
        return department;
    }

    public void setDepartment(Map<Long, String> department) {
        this.department = department;
    }

    public Map<Long, String> getJobPosition() {
        return jobPosition;
    }

    public void setJobPosition(Map<Long, String> jobPosition) {
        this.jobPosition = jobPosition;
    }

    public String getEmployeeNo() {
        return employeeNo;
    }

    public void setEmployeeNo(String employeeNo) {
        this.employeeNo = employeeNo;
    }

    public static Long getGroupIdByDetailId(List<UniongroupMemberDetail> uniongroupMemberDetails, Long detailId){
        for(UniongroupMemberDetail ud: uniongroupMemberDetails){
            if(ud.getDetailId().equals(detailId)){
                return ud.getGroupId();
            }
        }
        return null;
    }
}