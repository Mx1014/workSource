package com.everhomes.rest.salary;

import com.everhomes.util.StringHelper;

/**
 *
 * <ul>参数:
 * <li>reportToken: 查询计算结果的token参数</li>
 * </ul>
 */
public class FileSalaryGroupResponse {


    private String reportToken;

    public FileSalaryGroupResponse(String reportToken) {
        this.reportToken = reportToken;
    }
    public FileSalaryGroupResponse() {

    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

    public String getReportToken() {
        return reportToken;
    }

    public void setReportToken(String reportToken) {
        this.reportToken = reportToken;
    }
}
