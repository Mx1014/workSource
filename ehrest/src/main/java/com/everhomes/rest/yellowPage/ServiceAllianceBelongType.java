// @formatter:off
package com.everhomes.rest.yellowPage;

/**
 * 
 * <ul>
 * <li>COMMUNITY("community") : 服务联盟属于园区</li>
 * <li>ORGANAIZATION("organaization") : 服务联盟属于园区管理公司</li>
 * </ul>
 *
 *  @author:dengs 2017年4月28日
 */
public enum ServiceAllianceBelongType {
	COMMUNITY("community"), ORGANAIZATION("organaization");
	private String code;

	private ServiceAllianceBelongType(String code) {
		this.code = code;
	}

	public String getCode() {
		return this.code;
	}

	public static ServiceAllianceBelongType fromCode(String code) {
		if (code != null) {
			ServiceAllianceBelongType[] values = ServiceAllianceBelongType.values();
			for (ServiceAllianceBelongType value : values) {
				if (code.equals(value.code)) {
					return value;
				}
			}
		}
		return null;
	}
}
