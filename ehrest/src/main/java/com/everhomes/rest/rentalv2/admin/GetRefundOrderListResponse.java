package com.everhomes.rest.rentalv2.admin;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.community.BuildingDTO;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * 列出资源列表返回值(根据图标和园区)
 * <li>nextPageAnchor: 分页，下一页锚点</li>
 * <li>refundOrders: 退款单列表{@link com.everhomes.rest.rentalv2.admin.RefundOrderDTO}</li> 
 * </ul>
 */
public class GetRefundOrderListResponse {
private Long nextPageAnchor;
	
	@ItemType(RefundOrderDTO.class)
	private List<RefundOrderDTO> refundOrders;
	
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

	public Long getNextPageAnchor() {
		return nextPageAnchor;
	}

	public void setNextPageAnchor(Long nextPageAnchor) {
		this.nextPageAnchor = nextPageAnchor;
	}

	public List<RefundOrderDTO> getRefundOrders() {
		return refundOrders;
	}

	public void setRefundOrders(List<RefundOrderDTO> refundOrders) {
		this.refundOrders = refundOrders;
	}

}
