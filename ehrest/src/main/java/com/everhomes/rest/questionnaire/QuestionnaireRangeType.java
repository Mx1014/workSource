// @formatter:off
package com.everhomes.rest.questionnaire;

/**
 * 
 * <ul>
 <li>NAMESPACE_ALL:namespace_all 域空间全部用户</li>
 <li>NAMESPACE_AUTHENTICATED:namespace_authenticated 域空间认证用户</li>
 <li>NAMESPACE_UNAUTHORIZED:namespace_unauthorized 域空间非认证用户</li>
 <li>COMMUNITY_ALL:community_all 园区全部用户</li>
 <li>COMMUNITY_AUTHENTICATED:community_authenticated 园区认证用户</li>
 <li>COMMUNITY_UNAUTHORIZED:community_unauthorized 园区非认证用户</li>
 <li>BUILDING:building 楼栋</li>
 <li>ENTERPRISE:enterprise 企业</li>
 <li>USER:user 用户</li>


 * </ul>
 */
public enum QuestionnaireRangeType {
	NAMESPACE_ALL("namespace_all"),
	NAMESPACE_AUTHENTICATED("namespace_authenticated"),
	NAMESPACE_UNAUTHORIZED("namespace_unauthorized"),
	COMMUNITY_ALL("community_all"),
	COMMUNITY_AUTHENTICATED("community_authenticated"),
	COMMUNITY_UNAUTHORIZED("community_unauthorized"),
	BUILDING("building"),
	ENTERPRISE("enterprise"),
	USER("user");

	private String code;

	private QuestionnaireRangeType(String code) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}

	public static QuestionnaireRangeType fromCode(String code) {
		if (code != null) {
			for (QuestionnaireRangeType targetType : QuestionnaireRangeType.values()) {
				if (targetType.getCode().equals(code)) {
					return targetType;
				}
			}
		}
		return null;
	}
}
