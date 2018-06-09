package com.everhomes.rest.contract;

import com.everhomes.util.StringHelper;
/**
 * <ul>
 *     <li>RENTAL: 0 租赁合同场景</li>
 *     <li>PROPERTY: 1 物业合同场景</li>
 *     <li>COMPREHENSIVE: ２　综合合同场景</li>
 * </ul>
 * Created by dingjianmin on 2018/6/8.
 */
public enum ContractApplicationScene {
	RENTAL((byte) 0), PROPERTY((byte) 1), COMPREHENSIVE((byte) 2);

	private byte code;

	private ContractApplicationScene(byte code) {
		this.code = code;
	}

	public byte getCode() {
		return code;
	}

	public static ContractApplicationScene fromStatus(Byte code) {
		if (code != null) {
			for (ContractApplicationScene v : ContractApplicationScene.values()) {
				if (v.getCode() == code)
					return v;
			}
		}
		return null;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
