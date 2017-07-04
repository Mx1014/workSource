// @formatter:off
package com.everhomes.uniongroup;

import com.everhomes.server.schema.tables.pojos.EhUniongroupMemberDetails;
import com.everhomes.util.StringHelper;

import java.util.Map;

public class UniongroupMemberDetail extends EhUniongroupMemberDetails {

    private static final long serialVersionUID = 8751516334864351356L;

    private Map<Long, String> department;

    private Map<Long, String> job_position;

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

    public Map<Long, String> getJob_position() {
        return job_position;
    }

    public void setJob_position(Map<Long, String> job_position) {
        this.job_position = job_position;
    }
}