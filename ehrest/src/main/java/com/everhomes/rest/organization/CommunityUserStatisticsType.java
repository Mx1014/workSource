package com.everhomes.rest.organization;

public enum CommunityUserStatisticsType {

	AUTHENTICATION((byte)1),USER_SOURCE((byte)2);

	private Byte code;

	private CommunityUserStatisticsType(Byte code){
		this.code = code;
	}

	public Byte getCode() {
		return code;
	}

	public static CommunityUserStatisticsType fromCode(Byte code) {
		if(code != null) {
			CommunityUserStatisticsType[] values = CommunityUserStatisticsType.values();
			for(CommunityUserStatisticsType value : values) {
				if(code.byteValue() == value.code) {
					return value;
				}
			}
		}
		return null;
	}
	

}
