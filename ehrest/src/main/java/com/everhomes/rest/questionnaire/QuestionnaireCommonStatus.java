// @formatter:off
package com.everhomes.rest.questionnaire;

/**
 * 
 * <ul>
 * <li>FALSE: 0, 否，否定的，不支持的,结束的</li>
 * <li>TRUE: 2, 是，肯定的，支持的，进行中的</li>
 * </ul>
 */
public enum QuestionnaireCommonStatus {
	FALSE((byte)0),TRUE((byte)2);

	private byte code;

	private QuestionnaireCommonStatus(byte code) {
		this.code = code;
	}
	
	public byte getCode(){
		return code;
	}
	
	public static QuestionnaireCommonStatus fromCode(Byte code) {
		if (code != null) {
			for (QuestionnaireCommonStatus status : QuestionnaireCommonStatus.values()) {
				if (status.code == code.byteValue()) {
					return status;
				}
			}
		}
		return null;
	}
	
}
