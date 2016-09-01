package com.everhomes.rest.approval;

public enum CommonStatus {
	INACTIVE((byte)0), WAITING_FOR_CONFIRMATION((byte)1), ACTIVE((byte)2);
	
	private byte code;
	
	private CommonStatus(byte code) {
		this.code = code;
	}
	
	public byte getCode(){
		return this.code;
	}
	
	public CommonStatus fromCode(Byte code){
		if (code != null) {
			for (CommonStatus status : CommonStatus.values()) {
				if (code.byteValue() == status.getCode()) {
					return status;
				}
			}
		}
		
		return null;
	}
}
