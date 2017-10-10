// @formatter:off
package com.everhomes.rest.questionnaire;

/**
 * 
 * <ul>
 * <li>NOTSUPPORT: 0,不支持</li>
 * <li>SUPPORT: 2, 支持</li>
 * </ul>
 */
public enum QuestionnaireCommonStatus {
	NOTSUPPORT((byte)0),SUPPORT((byte)2);

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
