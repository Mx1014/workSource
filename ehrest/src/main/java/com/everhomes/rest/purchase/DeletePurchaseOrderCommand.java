//@formatter:off
package com.everhomes.rest.purchase;

/**
 * Created by Wentian Wang on 2018/1/10.
 */
/**
 *<ul>
 * <li>purchaseRequestId:采购单id</li>
 * <li>communityId:园区id</li>
 *</ul>
 */
public class DeletePurchaseOrderCommand {
    private Long purchaseRequestId;
    private Long communityId;

    public Long getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Long communityId) {
        this.communityId = communityId;
    }

    public Long getPurchaseRequestId() {
        return purchaseRequestId;
    }

    public void setPurchaseRequestId(Long purchaseRequestId) {
        this.purchaseRequestId = purchaseRequestId;
    }
}
