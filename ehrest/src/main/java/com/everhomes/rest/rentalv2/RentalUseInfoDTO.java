package com.everhomes.rest.rentalv2;

/**
 * <ul>
 * <li>refundContractUid: 退款联系人id</li>
 * <li>refundContractOrgId: 退款联系人公司id</li>
 * </ul>
 */
public class RentalUseInfoDTO {
    private Long refundContractUid;
    private Long refundContractOrgId;

    public Long getRefundContractUid() {
        return refundContractUid;
    }

    public void setRefundContractUid(Long refundContractUid) {
        this.refundContractUid = refundContractUid;
    }

    public Long getRefundContractOrgId() {
        return refundContractOrgId;
    }

    public void setRefundContractOrgId(Long refundContractOrgId) {
        this.refundContractOrgId = refundContractOrgId;
    }
}
