// @formatter:off
package com.everhomes.rest.group;

/**
 * 
 * <ul>
 * <li>GROUP: group，圈</li>
 * </ul>
 */
public enum BroadcastOwnerType {
	GROUP("group");
	
	private String code;
	
	private BroadcastOwnerType(String code) {
		this.code = code;
	}
	
	public String getCode(){
		return this.code;
	}
	
	public static BroadcastOwnerType fromCode(String code){
		if (code != null && code.length() != 0) {
			for (BroadcastOwnerType broadcastOwnerType : BroadcastOwnerType.values()) {
				if (code.equals(broadcastOwnerType.getCode())) {
					return broadcastOwnerType;
				}
			}
		}
		
		return null;
	}
}
