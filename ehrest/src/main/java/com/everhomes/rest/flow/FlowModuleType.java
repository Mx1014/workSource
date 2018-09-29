package com.everhomes.rest.flow;

/**
 * 
 * <ul>
 * <li>any-module : 任何模块</li>
 * <li>service_alliance : 服务联盟应用模块</li>
 * <li>review_contract : 物业合同发起审批</li>
 * <li>denunciation_contract : 物业合同退约</li>
 * <li>change_contract : 物业合同变更</li>
 * <li>renew_contract : 物业合同续约</li>
 * </ul>
 *
 *  @author:dengs
 */
public enum FlowModuleType {

	NO_MODULE("any-module"), REPAIR_MODULE("repair"), SUGGESTION_MODULE("suggestion"), SERVICE_ALLIANCE("service_alliance"), 
	LEASE_PROMOTION("lease_promotion"), REVIEW_CONTRACT("review_contract"), DENUNCIATION_CONTRACT("denunciation_contract"), 
	CHANGE_CONTRACT("change_contract"), RENEW_CONTRACT("renew_contract");
	
	private String code;

	private FlowModuleType(String code) {
		this.code = code;
	}

	public String getCode() {
		return this.code;
	}

	public static FlowModuleType fromCode(String code) {
		if (code == null) {
			return null;
		}

		for (FlowModuleType t : FlowModuleType.values()) {
			if (code.equalsIgnoreCase(t.getCode())) {
				return t;
			}
		}

		return null;
	}
}
