package com.everhomes.rest.workReport;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>result: 0-未启用 1-已启用</li>
 * </ul>
 */
public class VerifyWorkReportResponse {

    private Long result;

    public VerifyWorkReportResponse() {
    }

    public Long getResult() {
        return result;
    }

    public void setResult(Long result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
