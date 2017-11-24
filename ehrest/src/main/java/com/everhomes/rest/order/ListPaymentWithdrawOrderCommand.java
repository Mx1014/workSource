package com.everhomes.rest.order;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>ownerType: 帐户类型（如EhUsers、EhOrganizations），{@link com.everhomes.rest.order.OwnerType}</li>
 *     <li>ownerId: 帐户对应的ID（如用户ID、企业ID）</li>
 *     <li>pageAnchor: 本页开始的锚点</li>
 *     <li>pageSize: 每页的数量</li>
 * </ul>
 */
public class ListPaymentWithdrawOrderCommand {
	private String ownerType;
	
	private Long ownerId;
	
    private Long pageAnchor;
    
    private Integer pageSize;

    public String getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(String ownerType) {
        this.ownerType = ownerType;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public Long getPageAnchor() {
        return pageAnchor;
    }

    public void setPageAnchor(Long pageAnchor) {
        this.pageAnchor = pageAnchor;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
