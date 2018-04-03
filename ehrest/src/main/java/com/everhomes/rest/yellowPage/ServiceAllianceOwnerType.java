package com.everhomes.rest.yellowPage;

/**
 * <ul>
 * <li>SERVICE_ALLIANCE(service_alliance): 服务联盟应用模块</li>
 * </ul>
 */
public enum ServiceAllianceOwnerType {

	SERVICE_ALLIANCE("service_alliance");

	private String code;

	private ServiceAllianceOwnerType(String code) {
		this.code = code;
	}

	public String getCode() {
		return this.code;
	}

	public static ServiceAllianceOwnerType fromCode(String code) {
		if (code == null)
			return null;
		for (ServiceAllianceOwnerType ownerType : ServiceAllianceOwnerType.values()) {
			if (ownerType.getCode().equals(code)) {
				return ownerType;
			}
		}
		return null;
	}
}
