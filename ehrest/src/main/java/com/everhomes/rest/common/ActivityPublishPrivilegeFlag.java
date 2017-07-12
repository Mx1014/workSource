package com.everhomes.rest.common;

/**
 * 
 * <ul>
 * <li>OPEN_MODE(0): 开放模式</li>
 * <li>ADMIN_MODE(1): 管理员模式</li>
 * </ul>
 */
public enum ActivityPublishPrivilegeFlag {
	OPEN_MODE((byte)0), ADMIN_MODE((byte)1);
	
	private byte code;
	
	private ActivityPublishPrivilegeFlag(Byte code) {
		this.code = code;
	}
	
	public byte getCode(){
		return code;
	}
	
	public static ActivityPublishPrivilegeFlag fromCode(Byte code) {
		if (code != null) {
			for (ActivityPublishPrivilegeFlag flag : ActivityPublishPrivilegeFlag.values()) {
				if (flag.getCode() == code.byteValue()) {
					return flag;
				}
			}
		}
		return null;
	}
}
