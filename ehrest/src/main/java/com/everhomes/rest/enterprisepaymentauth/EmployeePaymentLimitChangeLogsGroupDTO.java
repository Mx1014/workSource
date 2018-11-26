package com.everhomes.rest.enterprisepaymentauth;

import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 * <li>operatorUid: 操作人uid</li>
 * <li>operatorName: 操作人姓名</li>
 * <li>operateTime: 操作时间</li>
 * <li>logItemDTOS: 记录明细</li>
 * </ul>
 */
public class EmployeePaymentLimitChangeLogsGroupDTO {
    private Long operatorUid;
    private String operatorName;
    private Long operateTime;
    private Long operateNo;
    private List<EmployeePaymentLimitChangeLogItemDTO> logItemDTOS;

    public Long getOperatorUid() {
        return operatorUid;
    }

    public void setOperatorUid(Long operatorUid) {
        this.operatorUid = operatorUid;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    public Long getOperateTime() {
        return operateTime;
    }

    public void setOperateTime(Long operateTime) {
        this.operateTime = operateTime;
    }

    public List<EmployeePaymentLimitChangeLogItemDTO> getLogItemDTOS() {
        return logItemDTOS;
    }

    public void setLogItemDTOS(List<EmployeePaymentLimitChangeLogItemDTO> logItemDTOS) {
        this.logItemDTOS = logItemDTOS;
    }

    public Long getOperateNo() {
        return operateNo;
    }

    public void setOperateNo(Long operateNo) {
        this.operateNo = operateNo;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
