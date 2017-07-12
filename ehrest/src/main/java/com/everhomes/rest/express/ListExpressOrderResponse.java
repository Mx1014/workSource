// @formatter:off
package com.everhomes.rest.express;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>返回值:
 * <li>nextPageAnchor: 下页锚点</li>
 * <li>expressOrderDTOs: 快递订单列表，参考{@link com.everhomes.rest.express.ExpressOrderDTO}</li>
 * </ul>
 */
public class ListExpressOrderResponse {

	private Long nextPageAnchor;

	@ItemType(ExpressOrderDTO.class)
	private List<ExpressOrderDTO> expressOrderDTOs;

	public ListExpressOrderResponse() {

	}

	public ListExpressOrderResponse(Long nextPageAnchor, List<ExpressOrderDTO> expressOrderDTOs) {
		super();
		this.nextPageAnchor = nextPageAnchor;
		this.expressOrderDTOs = expressOrderDTOs;
	}

	public Long getNextPageAnchor() {
		return nextPageAnchor;
	}

	public void setNextPageAnchor(Long nextPageAnchor) {
		this.nextPageAnchor = nextPageAnchor;
	}

	public List<ExpressOrderDTO> getExpressOrderDTOs() {
		return expressOrderDTOs;
	}

	public void setExpressOrderDTOs(List<ExpressOrderDTO> expressOrderDTOs) {
		this.expressOrderDTOs = expressOrderDTOs;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
