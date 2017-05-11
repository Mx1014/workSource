// @formatter:off
package com.everhomes.rest.talent;

/**
 * 
 * <ul>
 * <li>COMMUNITY: community</li>
 * </ul>
 */
public enum TalentOwnerType {
	COMMUNITY("community");
	
	private String code;

	private TalentOwnerType(String code) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}

	public static TalentOwnerType fromCode(String code) {
		if (code != null && !code.isEmpty()) {
			for (TalentOwnerType talentOwnerType : TalentOwnerType.values()) {
				if (talentOwnerType.getCode().equals(code)) {
					return talentOwnerType;
				}
			}
		}
		return null;
	}
}
