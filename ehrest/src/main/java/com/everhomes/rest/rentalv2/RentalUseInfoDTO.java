package com.everhomes.rest.rentalv2;

/**
 * <ul>
 * <li>refundContactUid: 退款联系人id</li>
 * <li>refundContactOrgId: 退款联系人公司id</li>
 * </ul>
 */
public class RentalUseInfoDTO {
    private Long refundContactUid;
    private Long refundContactOrgId;

    public Long getRefundContactUid() {
        return refundContactUid;
    }

    public void setRefundContactUid(Long refundContactUid) {
        this.refundContactUid = refundContactUid;
    }

    public Long getRefundContactOrgId() {
        return refundContactOrgId;
    }

    public void setRefundContactOrgId(Long refundContactOrgId) {
        this.refundContactOrgId = refundContactOrgId;
    }
}
