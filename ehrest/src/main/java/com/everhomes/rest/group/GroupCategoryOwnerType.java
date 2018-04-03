// @formatter:off
package com.everhomes.rest.group;

public enum GroupCategoryOwnerType {
	ORGANIZATION("organization");
	
	private String code;
	
	private GroupCategoryOwnerType(String code) {
		this.code = code;
	}
	
	public String getCode(){
		return this.code;
	}
	
	public static GroupCategoryOwnerType fromCode(String code){
		if (code != null && code.length() != 0) {
			for (GroupCategoryOwnerType groupCategoryOwnerType : GroupCategoryOwnerType.values()) {
				if (code.equals(groupCategoryOwnerType.getCode())) {
					return groupCategoryOwnerType;
				}
			}
		}
		
		return null;
	}
}
