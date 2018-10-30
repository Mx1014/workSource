package com.everhomes.user;

import com.everhomes.util.StringHelper;

public class SmartCardItem {
	private Byte smartCardType;
	private byte[] smartCardCode;

	public Byte getSmartCardType() {
		return smartCardType;
	}

	public void setSmartCardType(Byte smartCardType) {
		this.smartCardType = smartCardType;
	}

	public byte[] getSmartCardCode() {
		return smartCardCode;
	}

	public void setSmartCardCode(byte[] smartCardCode) {
		this.smartCardCode = smartCardCode;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
