package com.everhomes.rest.parking;

/**
 * <ul>
 * <li>typeId: 卡类型id</li>
 * <li>typeName: 卡类型名称</li>
 * </ul>
 */
public class ParkingCardType {
	private String typeId;
	private String typeName;

	public String getTypeId() {
		return typeId;
	}

	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

}
