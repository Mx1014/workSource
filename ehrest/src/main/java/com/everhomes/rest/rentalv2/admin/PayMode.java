package com.everhomes.rest.rentalv2.admin;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>ONLINE_PAY: 0 :线上支付</li>
 * <li>OFFLINE_PAY: 1 :线下支付</li>
 * <li>APPROVE_ONLINE_PAY: 2 :审批线上支付</li>
 * </ul>
 */
public enum PayMode {
	ONLINE_PAY((byte) 0), OFFLINE_PAY((byte) 1), APPROVE_ONLINE_PAY((byte) 2);
	private Byte code;

	private PayMode(Byte code) {
		this.code = code;
	}

	public Byte getCode() {
		return code;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
	public static PayMode fromCode(Byte code) {
		if (code != null) {
			for (PayMode a : PayMode.values()) {
				if (code.byteValue() == a.code.byteValue()) {
					return a;
				}
			}
		}
		return null;
	}
}
