// @formatter:off
package com.everhomes.rest.talent;

/**
 * 
 * <ul>
 * <li>UNDER_SECONDARY: 1，中专以下</li>
 * <li>OVER_SECONDARY: 2，中专及以上</li>
 * <li>OVER_COLLEGE: 3，大专及以上</li>
 * <li>OVER_BACHELOR: 4，本科及以上</li>
 * <li>OVER_MASTER: 5，硕士及以上</li>
 * <li>OVER_DOCTOR: 6，博士及以上</li>
 * </ul>
 */
public enum TalentDegreeConditionEnum {
	UNDER_SECONDARY((byte)1), OVER_SECONDARY((byte)2), OVER_COLLEGE((byte)3), OVER_BACHELOR((byte)4), OVER_MASTER((byte)5), OVER_DOCTOR((byte)6);
	
	private byte code;

	private TalentDegreeConditionEnum(Byte code) {
		this.code = code;
	}

	public byte getCode() {
		return code;
	}

	public static TalentDegreeConditionEnum fromCode(Byte code) {
		if (code != null) {
			for (TalentDegreeConditionEnum talentExperienceConditionEnum : TalentDegreeConditionEnum.values()) {
				if (talentExperienceConditionEnum.getCode() == code.byteValue()) {
					return talentExperienceConditionEnum;
				}
			}
		}
		return null;
	}
}
