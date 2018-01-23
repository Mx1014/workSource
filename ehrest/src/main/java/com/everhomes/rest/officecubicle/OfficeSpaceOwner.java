package com.everhomes.rest.officecubicle;

public enum OfficeSpaceOwner {
	COMMUNITY("community");

	private String code;

	private OfficeSpaceOwner(String code) {
		this.code = code;
	}

	public String getCode() {
		return this.code;
	}

	public static OfficeSpaceOwner fromCode(String code) {
		if (code == null)
			return null;
		for (OfficeSpaceOwner ownerType : OfficeSpaceOwner.values()) {
			if (ownerType.getCode().equals(code)) {
				return ownerType;
			}
		}
		return null;
	}

}
