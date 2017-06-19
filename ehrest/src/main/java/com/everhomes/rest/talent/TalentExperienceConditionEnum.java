// @formatter:off
package com.everhomes.rest.talent;

/**
 * 
 * <ul>
 * <li>UNDER_ONE_YEAR: 1，1年以内</li>
 * <li>ONE_THREE: 2，1-3年</li>
 * <li>THREE_FIVE: 3，3-5年</li>
 * <li>FIVE_TEN: 4，5-10年</li>
 * <li>OVER_TEN: 5，10年以上</li>
 * </ul>
 */
public enum TalentExperienceConditionEnum {
	UNDER_ONE_YEAR((byte)1), ONE_THREE((byte)2), THREE_FIVE((byte)3), FIVE_TEN((byte)4), OVER_TEN((byte)5);
	
	private byte code;

	private TalentExperienceConditionEnum(Byte code) {
		this.code = code;
	}

	public byte getCode() {
		return code;
	}

	public static TalentExperienceConditionEnum fromCode(Byte code) {
		if (code != null) {
			for (TalentExperienceConditionEnum talentExperienceConditionEnum : TalentExperienceConditionEnum.values()) {
				if (talentExperienceConditionEnum.getCode() == code.byteValue()) {
					return talentExperienceConditionEnum;
				}
			}
		}
		return null;
	}
}
