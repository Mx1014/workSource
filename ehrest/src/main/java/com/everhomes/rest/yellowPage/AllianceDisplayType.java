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
public enum AllianceDisplayType {

	HOUSE_KEEPER("housekeeper", "list"), 
	FAQ("", "faq")
	;

	private String code;
	private String showType;

	private AllianceDisplayType(String code, String showType) {
		this.code = code;
		this.showType = showType;
	}

	public static AllianceDisplayType fromType(String code) {
		for (AllianceDisplayType v : AllianceDisplayType.values()) {
			if (v.getCode().equals(code))
				return v;
		}
		return null;
	}

	public String getCode() {
		return code;
	}

	public String getShowType() {
		return showType;
	}

}
