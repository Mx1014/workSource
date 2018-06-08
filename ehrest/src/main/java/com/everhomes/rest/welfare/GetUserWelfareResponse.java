// @formatter:off
package com.everhomes.rest.welfare;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>返回值:
 * <li>: 要发送的福利 参考{@link com.everhomes.rest.welfare.UserWelfaresDTO}</li>
 * </ul>
 */
public class GetUserWelfareResponse {

	private UserWelfaresDTO userWelFare;

	public GetUserWelfareResponse() {

	}
 

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
