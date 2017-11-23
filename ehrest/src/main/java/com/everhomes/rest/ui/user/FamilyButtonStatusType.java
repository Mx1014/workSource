// @formatter:off
package com.everhomes.rest.ui.user;

/**
* 
* <ul>通用状态：
* <li>HIDDEN: 0，隐藏</li>
* <li>SHOW: 1，显示</li>
* </ul>
*/
public enum FamilyButtonStatusType {
	HIDDEN((byte)0), SHOW((byte)1);
	
	private byte code;
	
	private FamilyButtonStatusType(byte code) {
		this.code = code;
	}
	
	public byte getCode(){
		return this.code;
	}
	
	public static FamilyButtonStatusType fromCode(Byte code){
		if (code != null) {
			for (FamilyButtonStatusType status : FamilyButtonStatusType.values()) {
				if (code.byteValue() == status.getCode()) {
					return status;
				}
			}
		}
		
		return null;
	}
}
