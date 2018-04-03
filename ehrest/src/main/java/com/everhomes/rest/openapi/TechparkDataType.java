package com.everhomes.rest.openapi;

/**
 * 
 * <ul>金蝶同步数据过来的dataType
 * <li>BUILDING: 1, 楼栋</li>
 * <li>APARTMENT: 2, 门牌</li>
 * <li>RENTING: 3, 客户租赁</li>
 * <li>WAITING_FOR_RENTING: 4, 待租赁</li>
 * </ul>
 */
public enum TechparkDataType {
	BUILDING((byte)1),APARTMENT((byte)2),RENTING((byte)3),WAITING_FOR_RENTING((byte)4);
	
	private Byte code;
	
	private TechparkDataType(Byte code) {
		this.code = code;
	}
	
	public Byte getCode() {
		return this.code;
	}
	
	public static TechparkDataType fromCode(Byte code) {
		if (code != null) {
			for (TechparkDataType techparkDataType : TechparkDataType.values()) {
				if (techparkDataType.getCode().byteValue() == code.byteValue()) {
					return techparkDataType;
				}
			}
		}
		return null;
	}
}
