package com.everhomes.rest.user;

import com.everhomes.util.StringHelper;

public class SmartCardDisplayConfig {
	private Byte smartCardType;
	private Byte isDisplay;
	private Byte defaultValue;

	public Byte getSmartCardType() {
		return smartCardType;
	}

	public void setSmartCardType(Byte smartCardType) {
		this.smartCardType = smartCardType;
	}

	public Byte getIsDisplay() {
		return isDisplay;
	}

	public void setIsDisplay(Byte isDisplay) {
		this.isDisplay = isDisplay;
	}

	public Byte getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(Byte defaultValue) {
		this.defaultValue = defaultValue;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
