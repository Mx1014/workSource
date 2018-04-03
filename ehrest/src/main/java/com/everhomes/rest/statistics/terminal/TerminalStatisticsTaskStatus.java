package com.everhomes.rest.statistics.terminal;

public enum TerminalStatisticsTaskStatus {
	GENERATE_APP_VERSION_CUMULATIVE((byte)10),
	GENERATE_APP_VERSION_ACTIVE((byte)20),
	GENERATE_DAY_STAT((byte)30),
	GENERATE_HOUR_STAT((byte)40),
	GENERATE_APP_VERSION_STAT((byte)50),
	FINISH((byte)100);

	private byte code;

	private TerminalStatisticsTaskStatus(byte code){
		this.code = code;
	}

	public byte getCode() {
		return code;
	}
	
	public static TerminalStatisticsTaskStatus fromCode(Byte code){
		if(null == code){
			return null;
		}
		TerminalStatisticsTaskStatus[] values = TerminalStatisticsTaskStatus.values();
		for (TerminalStatisticsTaskStatus value : values) {
			if(value.code == code.byteValue()){
				return value;
			}
		}
		return null;
	}
	
}
