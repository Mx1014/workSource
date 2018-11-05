package com.everhomes.rest.yellowPage;

/**
 * HOUSE_KEEPER("housekeeper", "list"), 
 * FAQ("", "faq")
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
