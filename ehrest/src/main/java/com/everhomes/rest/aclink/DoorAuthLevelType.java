package com.everhomes.rest.aclink;

/**
 * <ul>
 * <li>0园区 1组织架构节点 2家庭 3项目(公司) 4楼栋(公司) 5楼层(公司) 6门牌(公司) 7项目(家庭) 8楼栋(家庭) 9楼层(家庭) 10门牌(家庭)</li>
 * </ul>
 * 
 */
public enum DoorAuthLevelType {
	COMMUNITY((byte) 0), ENTERPRISE((byte) 1), FAMILY((byte) 2), ORG_COMMUNITY((byte) 3), ORG_BUILDING((byte) 4), ORG_FLOOR((byte) 5),
	ORG_ADDRESS((byte) 6), FAMILY_COMMUNITY((byte) 7), FAMILY_BUILDING((byte) 8), FAMILY_FLOOR((byte) 9), FAMILY_ADDRESS((byte) 10);

	private byte code;

	public byte getCode() {
		return this.code;
	}

	private DoorAuthLevelType(byte code) {
		this.code = code;
	}

	public static DoorAuthLevelType fromCode(Byte code) {
		if (code == null)
			return null;

		switch (code.byteValue()) {
		case 0:
			return COMMUNITY;
		case 1:
			return ENTERPRISE;
		case 2:
			return FAMILY;
		case 3:
			return ORG_COMMUNITY;
		case 4:
			return ORG_BUILDING;
		case 5:
			return ORG_FLOOR;
		case 6:
			return ORG_ADDRESS;
		case 7:
			return FAMILY_COMMUNITY;
		case 8:
			return FAMILY_BUILDING;
		case 9:
			return FAMILY_FLOOR;
		case 10:
			return FAMILY_ADDRESS;
		}

		return null;
	}
}
