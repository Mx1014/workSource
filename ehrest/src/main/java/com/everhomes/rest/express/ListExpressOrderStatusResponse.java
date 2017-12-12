// @formatter:off
package com.everhomes.rest.express;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;
/**
 * 
 * <ul>
 * <li>expressOrderStatusDTO : 订单状态列表 参考 {@link com.everhomes.rest.express.ExpressOrderStatusDTO}</li>
 * </ul>
 *
 *  @author:dengs 2017年7月24日
 */
public class ListExpressOrderStatusResponse {
	@ItemType(ExpressOrderStatusDTO.class)
	private List<ExpressOrderStatusDTO> expressOrderStatusDTO;

	public List<ExpressOrderStatusDTO> getExpressOrderStatusDTO() {
		return expressOrderStatusDTO;
	}

	public void setExpressOrderStatusDTO(List<ExpressOrderStatusDTO> expressOrderStatusDTO) {
		this.expressOrderStatusDTO = expressOrderStatusDTO;
	}

	public ListExpressOrderStatusResponse(List<ExpressOrderStatusDTO> expressOrderStatusDTO) {
		super();
		this.expressOrderStatusDTO = expressOrderStatusDTO;
	}

	public ListExpressOrderStatusResponse() {
		super();
	}
	
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
	
}
