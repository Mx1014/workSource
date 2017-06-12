// @formatter:off
package com.everhomes.rest.talent;

/**
 * 
 * <ul>
 * <li>UNKNOWN: -1，未知</li>
 * <li>HIGH: 0，高中</li>
 * <li>SECONDARY: 1，中专</li>
 * <li>COLLEGE: 2，大专</li>
 * <li>BACHELOR: 3，本科</li>
 * <li>MASTER: 4，硕士</li>
 * <li>DOCTOR: 5，博士</li>
 * <li>POST_DOCTOR: 6，博士后</li>
 * </ul>
 */
public enum TalentDegreeEnum {
	UNKNOWN((byte)-1,"未知"),HIGH((byte)0,"高中"),SECONDARY((byte)1,"中专"), COLLEGE((byte)2,"大专"), BACHELOR((byte)3,"本科"), 
		MASTER((byte)4,"硕士"), DOCTOR((byte)5,"博士"), POST_DOCTOR((byte)6,"博士后");
	
	private byte code;
	private String name;

	private TalentDegreeEnum(Byte code, String name) {
		this.code = code;
		this.name = name;
	}

	public Byte getCode() {
		return code;
	}
	
	public String getName() {
		return name;
	}

	public static TalentDegreeEnum fromCode(Byte code) {
		if (code != null) {
			for (TalentDegreeEnum talentDegreeEnum : TalentDegreeEnum.values()) {
				if (talentDegreeEnum.getCode().byteValue() == code.byteValue()) {
					return talentDegreeEnum;
				}
			}
		}
		return null;
	}
	
	public static TalentDegreeEnum fromName(String name) {
		if (name != null) {
			for (TalentDegreeEnum talentDegreeEnum : TalentDegreeEnum.values()) {
				if (talentDegreeEnum.getName().equals(name)) {
					return talentDegreeEnum;
				}
			}
		}
		return null;
	}
}
