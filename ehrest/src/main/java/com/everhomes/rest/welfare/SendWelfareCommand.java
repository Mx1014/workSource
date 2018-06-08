// @formatter:off
package com.everhomes.rest.welfare;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>参数:
 * <li>welfare: 要发送的福利 参考{@link com.everhomes.rest.welfare.WelfaresDTO};</li>
 * </ul>
 */
public class SendWelfareCommand {

	private WelfaresDTO welfare;

	public SendWelfareCommand() {

	}

	public SendWelfareCommand(WelfaresDTO welfare) {
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
