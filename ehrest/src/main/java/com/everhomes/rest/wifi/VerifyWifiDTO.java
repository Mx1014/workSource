package com.everhomes.rest.wifi;

/**
 * <ul>
 * <li>flag: true表示成功  ,false 失败</li>
 * </ul>
 */
public class VerifyWifiDTO {
	private boolean flag;

	public boolean isFlag() {
		return flag;
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}
}
