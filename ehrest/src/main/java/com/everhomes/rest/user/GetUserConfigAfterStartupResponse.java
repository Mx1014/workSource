package com.everhomes.rest.user;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>smartCardInfo: 一卡通信息 {@link com.everhomes.rest.user.SmartCardInfo}</li>
 * </ul>
 * @author janson
 *
 */
public class GetUserConfigAfterStartupResponse {
	private SmartCardInfo smartCardInfo;

    public SmartCardInfo getSmartCardInfo() {
		return smartCardInfo;
	}

	public void setSmartCardInfo(SmartCardInfo smartCardInfo) {
		this.smartCardInfo = smartCardInfo;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
