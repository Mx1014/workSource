// @formatter:off
package com.everhomes.rest.express;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>返回值:
 * <li>expressQueryHistoryDTOs: 快递查询历史，参考{@link com.everhomes.rest.express.ExpressQueryHistoryDTO}</li>
 * </ul>
 */
public class ListExpressQueryHistoryResponse {

	@ItemType(ExpressQueryHistoryDTO.class)
	private List<ExpressQueryHistoryDTO> expressQueryHistoryDTOs;

	public ListExpressQueryHistoryResponse() {

	}

	public ListExpressQueryHistoryResponse(List<ExpressQueryHistoryDTO> expressQueryHistoryDTOs) {
		super();
		this.expressQueryHistoryDTOs = expressQueryHistoryDTOs;
	}

	public List<ExpressQueryHistoryDTO> getExpressQueryHistoryDTOs() {
		return expressQueryHistoryDTOs;
	}

	public void setExpressQueryHistoryDTOs(List<ExpressQueryHistoryDTO> expressQueryHistoryDTOs) {
		this.expressQueryHistoryDTOs = expressQueryHistoryDTOs;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
