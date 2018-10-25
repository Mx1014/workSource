package com.everhomes.rest.archives;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 * <li>detailIds: (List)员工detailId</li>
 * <li>organizationId: 公司 id</li>
 * <li>employmentTime: 转正日期</li>
 * <li>employmentEvaluation: 转正评价</li>
 * <li>logFlag: 0-无需人事记录, 1-需要人事记录</li>
 * </ul>
 */
public class EmployArchivesEmployeesCommand {

    @ItemType(Long.class)
    private List<Long> detailIds;

    private Long organizationId;

    private String employmentTime;

    private String employmentEvaluation;

    //  当无需生成档案记录的时候,可以使用此标志 add by ryan.
    private Byte logFlag;

    public EmployArchivesEmployeesCommand() {
    }

    public List<Long> getDetailIds() {
        return detailIds;
    }

    public void setDetailIds(List<Long> detailIds) {
        this.detailIds = detailIds;
    }

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public String getEmploymentTime() {
        return employmentTime;
    }

    public void setEmploymentTime(String employmentTime) {
        this.employmentTime = employmentTime;
    }

    public String getEmploymentEvaluation() {
        return employmentEvaluation;
    }

    public void setEmploymentEvaluation(String employmentEvaluation) {
        this.employmentEvaluation = employmentEvaluation;
    }

    public Byte getLogFlag() {
        return logFlag;
    }

    public void setLogFlag(Byte logFlag) {
        this.logFlag = logFlag;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
