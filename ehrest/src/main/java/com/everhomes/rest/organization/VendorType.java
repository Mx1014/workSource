package com.everhomes.rest.organization;

/**
 * <ul>
 * 	<li>支付宝 : 10001</li>
 *	<li>微信 : 10002</li>
 *</ul>
 *
 */
public enum VendorType {
	ZHI_FU_BAO("10001"),WEI_XIN("10002");

	private String code;

	private VendorType(String code){
		this.code = code;
	}

	public String getCode() {
		return code;
	}

	public static VendorType fromCode(String code){
		for(VendorType v :VendorType.values()){
			if(v.getCode().equals(code))
				return v;
		}
		return null;
	}

}
