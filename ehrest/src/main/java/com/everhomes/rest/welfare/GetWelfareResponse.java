// @formatter:off
package com.everhomes.rest.welfare;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>参数:
 * <li>welfare: 保存草稿的福利 参考{@link WelfaresDTO};</li>
 * </ul>
 */
public class GetWelfareResponse {

	private WelfaresDTO welfare;

	public GetWelfareResponse() {

	}

	public GetWelfareResponse(WelfaresDTO welfare) {
		super();
		this.welfare = welfare;
	}

	public WelfaresDTO getWelfare() {
		return welfare;
	}

	public void setWelfare(WelfaresDTO welfare) {
		this.welfare = welfare;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
