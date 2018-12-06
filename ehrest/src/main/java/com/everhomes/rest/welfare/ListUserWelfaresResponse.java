// @formatter:off
package com.everhomes.rest.welfare;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>返回值:
 * <li>welfares: 福利列表  参考{@link com.everhomes.rest.welfare.WelfaresDTO}</li>
 * <li>nextPageOffset: 下页页码</li>
 * </ul>
 */
public class ListUserWelfaresResponse {

	@ItemType(WelfaresDTO.class)
	private List<WelfaresDTO> welfares;

	private Integer nextPageOffset;

	public ListUserWelfaresResponse() {

	}

	public ListUserWelfaresResponse(List<WelfaresDTO> welfares) {
		super();
		this.welfares = welfares;
	}

	public List<WelfaresDTO> getWelfares() {
		return welfares;
	}

	public void setWelfares(List<WelfaresDTO> welfares) {
		this.welfares = welfares;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

	public Integer getNextPageOffset() {
		return nextPageOffset;
	}

	public void setNextPageOffset(Integer nextPageOffset) {
		this.nextPageOffset = nextPageOffset;
	}
}
