//@formatter:off
package com.everhomes.rest.requisition;

/**
 * Created by Wentian Wang on 2018/1/20.
 */
/**
 *<ul>
 * <li>requisitionId:请示单id</li>
 *</ul>
 */
public class GetRequisitionDetailCommand {
    private Long requisitionId;
    private Long communityId;

    public Long getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Long communityId) {
        this.communityId = communityId;
    }

    public Long getRequisitionId() {
        return requisitionId;
    }

    public void setRequisitionId(Long requisitionId) {
        this.requisitionId = requisitionId;
    }
}
