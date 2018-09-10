package com.everhomes.rest.techpark.punch;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>exceptionId: 考勤异常申请id</li>
 * <li>flowCaseId: 审批单工作流id</li>
 * <li>title: 异常申请显示标题</li>
 * <li>waitForApproval: 是否审批中，1：是  0：否,参考{@link com.everhomes.rest.techpark.punch.NormalFlag}</li>
 * <li>description: 申请时间等描述信息</li>
 * </ul>
 */
public class PunchExceptionRequestItemDetailDTO {
    private Long exceptionId;
    private Long flowCaseId;
    private String title;
    private Byte waitForApproval;
    private String description;

    public Long getExceptionId() {
        return exceptionId;
    }

    public void setExceptionId(Long exceptionId) {
        this.exceptionId = exceptionId;
    }

    public Long getFlowCaseId() {
        return flowCaseId;
    }

    public void setFlowCaseId(Long flowCaseId) {
        this.flowCaseId = flowCaseId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Byte getWaitForApproval() {
        return waitForApproval;
    }

    public void setWaitForApproval(Byte waitForApproval) {
        this.waitForApproval = waitForApproval;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
