// @formatter:off
package com.everhomes.rest.express;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>返回值:
 * <li>nextPageAnchor: 下页锚点</li>
 * <li>expressUserDTOs: 快递人员列表，参考{@link com.everhomes.rest.express.ExpressUserDTO}</li>
 * </ul>
 */
public class ListExpressUserResponse {

	private Long nextPageAnchor;

	@ItemType(ExpressUserDTO.class)
	private List<ExpressUserDTO> expressUserDTOs;

	public ListExpressUserResponse() {

	}

	public ListExpressUserResponse(Long nextPageAnchor, List<ExpressUserDTO> expressUserDTOs) {
		super();
		this.nextPageAnchor = nextPageAnchor;
		this.expressUserDTOs = expressUserDTOs;
	}

	public Long getNextPageAnchor() {
		return nextPageAnchor;
	}

	public void setNextPageAnchor(Long nextPageAnchor) {
		this.nextPageAnchor = nextPageAnchor;
	}

	public List<ExpressUserDTO> getExpressUserDTOs() {
		return expressUserDTOs;
	}

	public void setExpressUserDTOs(List<ExpressUserDTO> expressUserDTOs) {
		this.expressUserDTOs = expressUserDTOs;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
