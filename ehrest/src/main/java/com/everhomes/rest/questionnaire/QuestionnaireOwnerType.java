// @formatter:off
package com.everhomes.rest.questionnaire;

import org.apache.commons.lang.StringUtils;

/**
 * 
 * <ul>
 * <li>COMMUNITY: community,园区</li>
 * </ul>
 */
public enum QuestionnaireOwnerType {
	COMMUNITY("community");
	private String code;

	private QuestionnaireOwnerType(String code) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}

	public static QuestionnaireOwnerType fromCode(String code) {
		if (StringUtils.isNotBlank(code)) {
			for (QuestionnaireOwnerType ownerType : QuestionnaireOwnerType.values()) {
				if (ownerType.code.equals(code)) {
					return ownerType;
				}
			}
		}
		return null;
	}
}
