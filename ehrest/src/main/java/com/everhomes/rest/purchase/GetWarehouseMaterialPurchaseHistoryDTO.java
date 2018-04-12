//@formatter:off
package com.everhomes.rest.purchase;

/**
 * Created by Wentian Wang on 2018/3/8.
 */
/**
 *<ul>
 * <li>applicantName:申请人</li>
 * <li>applicatTime:申请时间</li>
 * <li>quantity:申请人</li>
 * <li>unitPrice:单价</li>
 * <li>submissionStatus:审核状态，参考{@link com.everhomes.purchase.PurchaseSubmissionStatus}</li>
 *</ul>
 */
public class GetWarehouseMaterialPurchaseHistoryDTO {
    private String applicantName;
    private String applicatTime;
    private String quantity;
    private String unitPrice;
    private Byte submissionStatus;

    public String getApplicantName() {
        return applicantName;
    }

    public void setApplicantName(String applicantName) {
        this.applicantName = applicantName;
    }

    public String getApplicatTime() {
        return applicatTime;
    }

    public void setApplicatTime(String applicatTime) {
        this.applicatTime = applicatTime;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(String unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Byte getSubmissionStatus() {
        return submissionStatus;
    }

    public void setSubmissionStatus(Byte submissionStatus) {
        this.submissionStatus = submissionStatus;
    }
}
