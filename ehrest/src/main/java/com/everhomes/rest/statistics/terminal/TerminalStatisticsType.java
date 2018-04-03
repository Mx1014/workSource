package com.everhomes.rest.statistics.terminal;

public enum TerminalStatisticsType {
	NEW_USER("new_user"), ACTIVE_USER("active_user"), START("start"), CUMULATIVE_USER("cumulative_user");

	private String code;

	private TerminalStatisticsType(String code){
		this.code = code;
	}

	public String getCode() {
		return code;
	}
	
	public static TerminalStatisticsType fromCode(String code){
		if(null == code){
			return null;
		}
		TerminalStatisticsType[] values = TerminalStatisticsType.values();
		for (TerminalStatisticsType value : values) {
			if(value.code.equals(code)){
				return value;
			}
		}
		return null;
	}
	
}
