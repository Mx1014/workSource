package com.everhomes.rest.enterprisepaymentauth;

import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 * <li>nextPageOffset：下一页</li>
 * <li>logsGroupDTOS: 调额记录 {@link com.everhomes.rest.enterprisepaymentauth.EmployeePaymentLimitChangeLogsGroupDTO}</li>
 * </ul>
 */
public class ListEmployeePaymentLimitChangeLogsResponse {
    private Integer nextPageOffset;
    private List<EmployeePaymentLimitChangeLogsGroupDTO> logsGroupDTOS;

    public Integer getNextPageOffset() {
        return nextPageOffset;
    }

    public void setNextPageOffset(Integer nextPageOffset) {
        this.nextPageOffset = nextPageOffset;
    }

    public List<EmployeePaymentLimitChangeLogsGroupDTO> getLogsGroupDTOS() {
        return logsGroupDTOS;
    }

    public void setLogsGroupDTOS(List<EmployeePaymentLimitChangeLogsGroupDTO> logsGroupDTOS) {
        this.logsGroupDTOS = logsGroupDTOS;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
