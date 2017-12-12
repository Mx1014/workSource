package com.everhomes.rest.techpark.expansion;

/**
 * 与{@link com.everhomes.rest.flow.FlowCaseStatus} 状态保持一致
 * PROCESSING(2):处理中
 * RESIDED_IN(4): 已完成
 * INVALID(3):已取消
 * */
public enum ApplyEntryStatus {

	PROCESSING((byte)2, "处理中"), INVALID((byte)3, "已取消"), RESIDED_IN((byte)4, "已完成");

	private byte code;
	private String description;

	public String getDescription() {
		return description;
	}

	private ApplyEntryStatus(byte code, String description){
		this.code = code;
		this.description =description;
	}

	public byte getCode() {
		return code;
	}

	public static ApplyEntryStatus fromType(byte code) {
		for(ApplyEntryStatus v : ApplyEntryStatus.values()) {
			if(v.getCode() == code)
				return v;
		}
		return null;
	}
}
