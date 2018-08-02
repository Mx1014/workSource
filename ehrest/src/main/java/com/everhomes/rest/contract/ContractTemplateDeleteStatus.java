package com.everhomes.rest.contract;

import com.everhomes.util.StringHelper;
/**
 * <ul>
 *     <li>RENTAL: 0 表示可以删除</li>
 *     <li>PROPERTY: 1 表示关联合同不能删除(已引用)</li>
 *     <li>COMPREHENSIVE: ２　表示园区下不能删除通用模板</li>
 * </ul>
 * Created by dingjianmin on 2018/7/16.
 */
public enum ContractTemplateDeleteStatus {
	DELETED((byte) 0), CITED((byte) 1), NOCOMPETENCE((byte) 2);

	private byte code;

	private ContractTemplateDeleteStatus(byte code) {
		this.code = code;
	}

	public byte getCode() {
		return code;
	}

	public static ContractTemplateDeleteStatus fromStatus(Byte code) {
		if (code != null) {
			for (ContractTemplateDeleteStatus v : ContractTemplateDeleteStatus.values()) {
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
