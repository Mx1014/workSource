// @formatter:off
package com.everhomes.rest.talent;

/**
 * 
 * <ul>
 * <li>SECONDARY: 1，中专</li>
 * <li>COLLEGE: 2，大专</li>
 * <li>BACHELOR: 3，本科及</li>
 * <li>MASTER: 4，硕士</li>
 * <li>DOCTOR: 5，博士</li>
 * <li>POST_DOCTOR: 6，博士后</li>
 * </ul>
 */
public enum TalentDegreeEnum {
	SECONDARY((byte)1), COLLEGE((byte)2), BACHELOR((byte)3), MASTER((byte)4), DOCTOR((byte)5), POST_DOCTOR((byte)6);
	
	private byte code;

	private TalentDegreeEnum(Byte code) {
		this.code = code;
	}

	public Byte getCode() {
		return code;
	}

	public static TalentDegreeEnum fromCode(Byte code) {
		if (code != null) {
			for (TalentDegreeEnum talentExperienceConditionEnum : TalentDegreeEnum.values()) {
				if (talentExperienceConditionEnum.getCode().byteValue() == code.byteValue()) {
					return talentExperienceConditionEnum;
				}
			}
		}
		return null;
	}
}
