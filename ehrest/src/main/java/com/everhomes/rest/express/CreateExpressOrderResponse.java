// @formatter:off
package com.everhomes.rest.express;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>返回值:
 * <li>expressOrderDTO: 快递订单，参考{@link com.everhomes.rest.express.ExpressOrderDTO}</li>
 * </ul>
 */
public class CreateExpressOrderResponse {

	private ExpressOrderDTO expressOrderDTO;

	public CreateExpressOrderResponse() {

	}

	public CreateExpressOrderResponse(ExpressOrderDTO expressOrderDTO) {
		super();
		this.expressOrderDTO = expressOrderDTO;
	}

	public ExpressOrderDTO getExpressOrderDTO() {
		return expressOrderDTO;
	}

	public void setExpressOrderDTO(ExpressOrderDTO expressOrderDTO) {
		this.expressOrderDTO = expressOrderDTO;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
