// @formatter:off
package com.everhomes.rest.questionnaire;

/**
 * 
 * <ul>
 * <li>RADIO: 1，单选</li>
 * <li>CHECKBOX: 2，复选</li>
 * <li>BLANK: 3.填空</li>
 * <li>IMAGE_RADIO: 4，图片单选</li>
 * <li>IMAGE_CHECKBOX: 5，图片复选</li>
 * </ul>
 */
public enum QuestionType {
	RADIO((byte)1),CHECKBOX((byte)2),BLANK((byte)3),IMAGE_RADIO((byte)4),IMAGE_CHECKBOX((byte)5);
	
	private byte code;
	
	private QuestionType(byte code){
		this.code = code;
	}
	
	public byte getCode() {
		return code;
	}
	
	public static QuestionType fromCode(Byte code) {
		if (code != null) {
			for (QuestionType type : QuestionType.values()) {
				if (type.code == code.byteValue()) {
					return type;
				}
			}
		}
		return null;
	}
}
