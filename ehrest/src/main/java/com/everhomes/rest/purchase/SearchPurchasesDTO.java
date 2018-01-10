//@formatter:off
package com.everhomes.rest.purchase;

import com.everhomes.util.StringHelper;

/**
 * Created by Wentian Wang on 2018/1/10.
 */
/**
 *<ul>
 * <li>purchaseRequestId:采购单id</li>
 * <li>applicant:申请人</li>
 * <li>applicationTime:申请时间</li>
 * <li>submissionStatus:审核状态，1：处理中；2：已完成；3：已取消；4:未完成</li>
 * <li>totalAmount:总金额</li>
 * <li>warehouseStatus:入库状态, 1:未入库；2：已入库</li>
 *</ul>
 */
public class SearchPurchasesDTO {
    private Long purchaseRequestId;
    private String applicant;
    private String applicationTime;
    private Byte submissionStatus;
    private String totalAmount;
    private Byte warehouseStatus;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

    public String getApplicant() {
        return applicant;
    }

    public Long getPurchaseRequestId() {
        return purchaseRequestId;
    }

    public void setPurchaseRequestId(Long purchaseRequestId) {
        this.purchaseRequestId = purchaseRequestId;
    }

    public void setApplicant(String applicant) {
        this.applicant = applicant;
    }

    public String getApplicationTime() {
        return applicationTime;
    }

    public void setApplicationTime(String applicationTime) {
        this.applicationTime = applicationTime;
    }

    public Byte getSubmissionStatus() {
        return submissionStatus;
    }

    public void setSubmissionStatus(Byte submissionStatus) {
        this.submissionStatus = submissionStatus;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Byte getWarehouseStatus() {
        return warehouseStatus;
    }

    public void setWarehouseStatus(Byte warehouseStatus) {
        this.warehouseStatus = warehouseStatus;
    }
}
