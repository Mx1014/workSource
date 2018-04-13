//@formatter:off
package com.everhomes.rest.requisition;

/**
 * Created by Wentian Wang on 2018/1/20.
 */
/**
 * <ul>
 * <li>theme: 请求主题</li>
 * <li>applicantName: 请示人姓名</li>
 * <li>applicantDepartment: 请示人部门</li>
 * <li>typeId: 请示类型id</li>
 * <li>description: 说明</li>
 * <li>attachmentUrl: 附件地址</li>
 * <li>amount: 金额</li>
 * </ul>
 */
public class GetRequisitionDetailResponse {
    private String theme;
    private String applicantName;
    private String applicantDepartment;
    private String amount;
    private Long typeId;
    private String description;
    private String attachmentUrl;
    private Long flowCaseId;
    private String fileName;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Long getFlowCaseId() {
        return flowCaseId;
    }

    public void setFlowCaseId(Long flowCaseId) {
        this.flowCaseId = flowCaseId;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public String getApplicantName() {
        return applicantName;
    }

    public void setApplicantName(String applicantName) {
        this.applicantName = applicantName;
    }

    public String getApplicantDepartment() {
        return applicantDepartment;
    }

    public void setApplicantDepartment(String applicantDepartment) {
        this.applicantDepartment = applicantDepartment;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public Long getTypeId() {
        return typeId;
    }

    public void setTypeId(Long typeId) {
        this.typeId = typeId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAttachmentUrl() {
        return attachmentUrl;
    }

    public void setAttachmentUrl(String attachmentUrl) {
        this.attachmentUrl = attachmentUrl;
    }
}
