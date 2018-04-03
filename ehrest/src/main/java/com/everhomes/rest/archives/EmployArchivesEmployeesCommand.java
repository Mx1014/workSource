package com.everhomes.rest.archives;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.sql.Date;
import java.util.List;

/**
 * <ul>
 * <li>detailIds: (List)员工detailId</li>
 * <li>organizationId: 公司 id</li>
 * <li>employmentTime: 转正日期</li>
 * <li>employmentEvaluation: 转正评价</li>
 * </ul>
 */
public class EmployArchivesEmployeesCommand {

    @ItemType(Long.class)
    private List<Long> detailIds;

    private Long organizationId;

    private Date employmentTime;

    private String employmentEvaluation;

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

    public Date getEmploymentTime() {
        return employmentTime;
    }

    public void setEmploymentTime(String employmentTime) {
        this.employmentTime = ArchivesUtil.parseDate(employmentTime);
    }

    public String getEmploymentEvaluation() {
        return employmentEvaluation;
    }

    public void setEmploymentEvaluation(String employmentEvaluation) {
        this.employmentEvaluation = employmentEvaluation;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
