package com.everhomes.rest.contract;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * 	<li>SwitchStatus: 开关状态（默认关闭） ：0 关闭， 1 打开</li>
 * </ul>
 * Created djm on 2018/12/10.
 */
public enum ContractSwitchStatus {
	OFF((byte) 0), ON((byte) 1);
	private byte code;

	private ContractSwitchStatus(byte code) {
		this.code = code;
	}

	public byte getCode() {
		return code;
	}

	public static ContractSwitchStatus fromStatus(byte code) {
		for (ContractSwitchStatus v : ContractSwitchStatus.values()) {
			if (v.getCode() == code)
				return v;
		}
		return null;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
