package com.everhomes.rest.payment;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>status: 1：开通  0：未开通 {@link com.everhomes.rest.payment.CardOpenStatus}</li>
 * </ul>
 */
public class VerifyOpenCardDTO {
	private byte status;

	public byte getStatus() {
		return status;
	}

	public void setStatus(byte status) {
		this.status = status;
	}
	
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
