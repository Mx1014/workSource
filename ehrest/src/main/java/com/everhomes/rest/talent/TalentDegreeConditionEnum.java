// @formatter:off
package com.everhomes.rest.talent;

/**
 * 
 * <ul>
 * <li>UNDER_SECONDARY: 1，中专以下</li>
 * <li>OVER_SECONDARY: 2，中专及以上</li>
 * <li>OVER_BACHELOR: 3，本科及以上</li>
 * <li>OVER_MASTER: 4，硕士及以上</li>
 * <li>OVER_DOCTOR: 5，博士及以上</li>
 * </ul>
 */
public enum TalentDegreeConditionEnum {
	UNDER_SECONDARY((byte)1), OVER_SECONDARY((byte)2), OVER_BACHELOR((byte)3), OVER_MASTER((byte)4), OVER_DOCTOR((byte)5);
	
	private byte code;

	private TalentDegreeConditionEnum(Byte code) {
		this.code = code;
	}

	public Byte getCode() {
		return code;
	}

	public TalentDegreeConditionEnum fromCode(Byte code) {
		if (code != null) {
			for (TalentDegreeConditionEnum talentExperienceConditionEnum : TalentDegreeConditionEnum.values()) {
				if (talentExperienceConditionEnum.getCode().byteValue() == code.byteValue()) {
					return talentExperienceConditionEnum;
				}
			}
		}
		return null;
	}
}
