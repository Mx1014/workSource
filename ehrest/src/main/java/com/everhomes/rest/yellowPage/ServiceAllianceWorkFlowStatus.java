package com.everhomes.rest.yellowPage;

/**
 * 与{@link com.everhomes.rest.flow.FlowCaseStatus} 状态保持一致
 * UNKNOWN(-1):未知
 * NONE(1):无
 * PROCESSING(2):处理中
 * INVALID(3):已取消
 * RESIDED_IN(4): 已完成
 * SUSPEND(6): 暂缓
 * */
public enum ServiceAllianceWorkFlowStatus {

	UNKNOWN((byte)-1, "未知"),
	NONE((byte)1,"无"),
	PROCESSING((byte)2, "处理中"), 
	INVALID((byte)3, "已取消"), 
	RESIDED_IN((byte)4, "已完成"), 
	SUSPEND((byte)6, "暂缓")
	;

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
	
	/**
	 * <ul>
	 * FlowCaseStatus的状态
	 * <li>INVALID(0): 无效</li>
	 * <li>INITIAL(1): 初始化</li>
	 * <li>PROCESS(2): 处理中</li>
	 * <li>ABSORTED(3): 已完成（已完成，但是处于异常的结束）</li>
	 * <li>FINISHED(4): 已完成</li>
	 * <li>EVALUATE(5): 待评价</li>
	 * <li>SUSPEND(6): 暂缓</li>
	 * <li>注意： 不能随便改 FlowCaseStatus 的参数值</li>
	 * </ul>
	 */
	public static ServiceAllianceWorkFlowStatus getFromFlowCaseStatus(Byte flowCaseStatus) {

		if (null == flowCaseStatus) {
			return NONE;
		}

		switch (flowCaseStatus) {
		case (byte) 0:
			return NONE;

		case (byte) 1:
		case (byte) 2:
			return PROCESSING;

		case (byte) 3:
			return INVALID;
		
		case (byte) 4:
		case (byte) 5:
			return RESIDED_IN;

		case (byte) 6:
			return SUSPEND;

		default:
			return UNKNOWN;
		}

	}
}
