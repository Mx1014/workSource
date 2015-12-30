package com.everhomes.rest.videoconf;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>nextPageAnchor：分页的锚点，下一页开始取数据的位置</li>
 * <li>confOrders: confOrders信息，参考{@link com.everhomes.rest.videoconf.ConfOrderDTO}</li>
 * </ul>
 */
public class ListVideoConfAccountOrderResponse {

	private Long nextPageAnchor;
	
	@ItemType(ConfOrderDTO.class)
	private List<ConfOrderDTO> confOrders;

	public Long getNextPageAnchor() {
		return nextPageAnchor;
	}

	public void setNextPageAnchor(Long nextPageAnchor) {
		this.nextPageAnchor = nextPageAnchor;
	}

	public List<ConfOrderDTO> getConfOrders() {
		return confOrders;
	}

	public void setConfOrders(List<ConfOrderDTO> confOrders) {
		this.confOrders = confOrders;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
	
}
