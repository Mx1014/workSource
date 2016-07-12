package com.everhomes.rest.organization;

/**
 * <ul>
 * 	<li>支付宝 : 10001</li>
 *	<li>微信 : 10002</li>
 *</ul>
 *
 */
public enum VendorType {
	ZHI_FU_BAO("10001","wechat"),WEI_XIN("10002","alipay");

	private String code;
	private String styleNo;

	private VendorType(String code,String styleNo){
		this.code = code;
		this.styleNo=styleNo;
	}

	public String getVendorType() {
		return code;
	}

	public String getStyleNo() {
		return styleNo;
	}

	public static VendorType fromCode(String code){
		for(VendorType v :VendorType.values()){
			if(v.getVendorType().equals(code))
				return v;
		}
		return null;
	}

	public static VendorType fromStyleNo(String styleNo){
		for(VendorType v :VendorType.values()){
			if(v.getStyleNo().equals(styleNo))
				return v;
		}
		return null;
	}
}
