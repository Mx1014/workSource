// @formatter:off
package com.everhomes.rest.questionnaire;

/**
 * 
 * <ul>
 * <li>ORGANIZATION: organization,组织</li>
 * </ul>
 */
public enum QuestionnaireTargetType {
	ORGANIZATION("organization");
	private String code;

	private QuestionnaireTargetType(String code) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}

	public static QuestionnaireTargetType fromCode(String code) {
		if (code != null) {
			for (QuestionnaireTargetType targetType : QuestionnaireTargetType.values()) {
				if (targetType.getCode().equals(code)) {
					return targetType;
				}
			}
		}
		return null;
	}
}
