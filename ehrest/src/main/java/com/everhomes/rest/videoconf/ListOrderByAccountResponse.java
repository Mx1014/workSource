package com.everhomes.rest.videoconf;


import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>nextPageAnchor：分页的锚点，下一页开始取数据的位置</li>
 * <li>counts: count信息，参考{@link com.everhomes.rest.videoconf.CountAccountOrdersAndMonths}</li>
 * <li>orderBriefs: order信息，参考{@link com.everhomes.rest.videoconf.OrderBriefDTO}</li>
 * </ul>
 */
public class ListOrderByAccountResponse {

	private Long nextPageAnchor;
	
	private CountAccountOrdersAndMonths counts;
	@ItemType(OrderBriefDTO.class)
	private List<OrderBriefDTO> orderBriefs;

	public Long getNextPageAnchor() {
		return nextPageAnchor;
	}

	public void setNextPageAnchor(Long nextPageAnchor) {
		this.nextPageAnchor = nextPageAnchor;
	}

	
	public CountAccountOrdersAndMonths getCounts() {
		return counts;
	}

	public void setCounts(CountAccountOrdersAndMonths counts) {
		this.counts = counts;
	}

	public List<OrderBriefDTO> getOrderBriefs() {
		return orderBriefs;
	}

	public void setOrderBriefs(List<OrderBriefDTO> orderBriefs) {
		this.orderBriefs = orderBriefs;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
