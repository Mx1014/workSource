package com.everhomes.rest.archives;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.general_approval.GeneralFormDTO;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 * <li>employeeCase: 员工状况</li>
 * <li>form: 档案详情表单(包含字段名、组名及值) 参考{@link com.everhomes.rest.general_approval.GeneralFormDTO}</li>
 * <li>los: 档案记录 参考{@link com.everhomes.rest.archives.ArchivesLogDTO}</li>
 * </ul>
 */
public class GetArchivesEmployeeResponse {

    private String employeeCase;

    private GeneralFormDTO form;

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
