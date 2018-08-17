package com.everhomes.rest.address;

public enum AddressArrangementType {
	
	SPLIT((byte)0),MERGE((byte)1);
	
	private byte code;

	private AddressArrangementType(byte code) {
		this.code = code;
	};
	
	public byte getCode() {
	     return this.code;
	 }

	 public static AddressArrangementType fromCode(Byte code) {
	     if(code == null)
	         return null;
	     
	     switch(code.byteValue()) {
	     case 0 :
	         return SPLIT;
	         
	     case 1 :
	         return MERGE;
	         
	     default :
	         assert(false);
	         break;
	     }
	     
	     return null;
	 }
}