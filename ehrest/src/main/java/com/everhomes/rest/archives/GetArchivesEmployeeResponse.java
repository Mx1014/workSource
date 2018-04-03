package com.everhomes.rest.archives;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.general_approval.GeneralFormDTO;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 * <li>employeeCase: 员工状况</li>
 * <li>form: 档案详情表单(包含字段名、组名及值) 参考{@link com.everhomes.rest.general_approval.GeneralFormDTO}</li>
 * <li>departmentIds: 员工部门ids</li>
 * <li>jobPositionIds: 员工岗位ids</li>
 * <li>jobLevelIds: 员工职级ids</li>
 * <li>logs: logs {@link com.everhomes.rest.archives.ArchivesLogDTO}</li>
 * </ul>
 */
public class GetArchivesEmployeeResponse {

    private String employeeCase;

    private GeneralFormDTO form;

    @ItemType(Long.class)
    private List<Long> departmentIds;

    @ItemType(Long.class)
    private List<Long> jobPositionIds;

    @ItemType(Long.class)
    private List<Long> jobLevelIds;

    @ItemType(ArchivesLogDTO.class)
    public List<ArchivesLogDTO> logs;

    public GetArchivesEmployeeResponse() {
    }

    public String getEmployeeCase() {
        return employeeCase;
    }

    public void setEmployeeCase(String employeeCase) {
        this.employeeCase = employeeCase;
    }

    public GeneralFormDTO getForm() {
        return form;
    }

    public void setForm(GeneralFormDTO form) {
        this.form = form;
    }

    public List<Long> getDepartmentIds() {
        return departmentIds;
    }

    public void setDepartmentIds(List<Long> departmentIds) {
        this.departmentIds = departmentIds;
    }

    public List<Long> getJobPositionIds() {
        return jobPositionIds;
    }

    public void setJobPositionIds(List<Long> jobPositionIds) {
        this.jobPositionIds = jobPositionIds;
    }

    public List<Long> getJobLevelIds() {
        return jobLevelIds;
    }

    public void setJobLevelIds(List<Long> jobLevelIds) {
        this.jobLevelIds = jobLevelIds;
    }

    public List<ArchivesLogDTO> getLogs() {
        return logs;
    }

    public void setLogs(List<ArchivesLogDTO> logs) {
        this.logs = logs;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
