//@formatter:off
package com.everhomes.rest.address;

/**
* <p>房源拆分合并计划的记录状态</p>
* <ul>
* <li>INACTIVE: 无效 </li>
* <li>CONFIRMING: 待审核 </li>
* <li>ACTIVE: 正常 </li>
* </ul>
*/
public enum AddressArrangementStatus {
	INACTIVE((byte)0), CONFIRMING((byte)1),ACTIVE((byte)2);
 
	private byte code;
	
	private AddressArrangementStatus(byte code) {
		this.code = code;
	}
 
	 public byte getCode() {
	     return this.code;
	 }
 
	 public static AddressArrangementStatus fromCode(Byte code) {
	     if(code == null)
	         return null;
	     
	     switch(code.byteValue()) {
	     case 0 :
	         return INACTIVE;
	         
	     case 1 :
	         return CONFIRMING;
	         
	     case 2 :
	         return ACTIVE;
	         
	     default :
	         assert(false);
	         break;
	     }
	     
	     return null;
	 }
}
