package com.everhomes.rest.address;

public enum ArrangementOperationFlag {
	
	NOT_EXCUTED((byte)0), EXCUTED((byte)1);
	
	private byte code;
	
	private ArrangementOperationFlag(byte code){
		this.code = code;
	}
	
	public byte getCode() {
	     return this.code;
	 }

	 public static ArrangementOperationFlag fromCode(Byte code) {
	     if(code == null)
	         return null;
	     
	     switch(code.byteValue()) {
		     case 0 :
		         return NOT_EXCUTED;
		         
		     case 1 :
		         return EXCUTED;
		         
		     default :
		         assert(false);
		         break;
	     }
	     return null;
	 }
}