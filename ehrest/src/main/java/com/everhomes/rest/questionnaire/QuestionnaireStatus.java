// @formatter:off
package com.everhomes.rest.questionnaire;

/**
 * 
 * <ul>
 * <li>INACTIVE: 0, 已失效</li>
 * <li>DRAFT: 1, 草稿</li>
 * <li>ACTIVE: 2, 有效</li>
 * </ul>
 */
public enum QuestionnaireStatus {
	INACTIVE((byte)0),DRAFT((byte)1),ACTIVE((byte)2);
	
	private byte code;
	
	private QuestionnaireStatus(byte code) {
		this.code = code;
	}
	
	public byte getCode(){
		return code;
	}
	
	public static QuestionnaireStatus fromCode(Byte code) {
		if (code != null) {
			for (QuestionnaireStatus status : QuestionnaireStatus.values()) {
				if (status.code == code.byteValue()) {
					return status;
				}
			}
		}
		return null;
	}
	
	public static void main(String[] args) {
		System.out.println(QuestionnaireStatus.fromCode(null));
		System.out.println(QuestionnaireStatus.fromCode((byte)1));
		System.out.println(QuestionnaireStatus.fromCode(Byte.valueOf("2")));
	}
}
