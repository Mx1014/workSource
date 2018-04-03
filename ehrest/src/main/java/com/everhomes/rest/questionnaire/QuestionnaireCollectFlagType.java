// @formatter:off
package com.everhomes.rest.questionnaire;

/**
 * 
 * <ul>
 * <li>COLLECTING: 1,正在收集</li>
 * <li>FINISHED: 2, 收集完成</li>
 * </ul>
 */
public enum QuestionnaireCollectFlagType {
	COLLECTING((byte)1),FINISHED((byte)2);

	private byte code;

	private QuestionnaireCollectFlagType(byte code) {
		this.code = code;
	}
	
	public byte getCode(){
		return code;
	}
	
	public static QuestionnaireCollectFlagType fromCode(Byte code) {
		if (code != null) {
			for (QuestionnaireCollectFlagType status : QuestionnaireCollectFlagType.values()) {
				if (status.code == code.byteValue()) {
					return status;
				}
			}
		}
		return null;
	}
	
}
