// @formatter:off
package com.everhomes.rest.officecubicle;

/**
 * 与{@link com.everhomes.rest.flow.FlowCaseStatus} 状态保持一致
 * PROCESSING(2):处理中
 * INVALID(3):已取消
 * RESIDED_IN(4): 已完成
 * */
public enum OfficeOrderWorkFlowStatus {

	PROCESSING((byte)2, "处理中"), INVALID((byte)3, "已取消"), RESIDED_IN((byte)4, "已完成");

	private byte code;
	private String description;

	public String getDescription() {
		return description;
	}

	private OfficeOrderWorkFlowStatus(byte code, String description){
		this.code = code;
		this.description =description;
	}

	public byte getCode() {
		return code;
	}

	public static OfficeOrderWorkFlowStatus fromType(byte code) {
		for(OfficeOrderWorkFlowStatus v : OfficeOrderWorkFlowStatus.values()) {
			if(v.getCode() == code)
				return v;
		}
		return null;
	}
}
