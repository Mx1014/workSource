package com.everhomes.rest.yellowPage;

/**
 * 与{@link com.everhomes.rest.flow.FlowCaseStatus} 状态保持一致
 * NONE(1):无
 * PROCESSING(2):处理中
 * INVALID(3):已取消
 * RESIDED_IN(4): 已完成
 * */
public enum ServiceAllianceWorkFlowStatus {

	NONE((byte)1,"无"),PROCESSING((byte)2, "处理中"), INVALID((byte)3, "已取消"), RESIDED_IN((byte)4, "已完成");

	private byte code;
	private String description;

	public String getDescription() {
		return description;
	}

	private ServiceAllianceWorkFlowStatus(byte code, String description){
		this.code = code;
		this.description =description;
	}

	public byte getCode() {
		return code;
	}

	public static ServiceAllianceWorkFlowStatus fromType(byte code) {
		for(ServiceAllianceWorkFlowStatus v : ServiceAllianceWorkFlowStatus.values()) {
			if(v.getCode() == code)
				return v;
		}
		return null;
	}
}
