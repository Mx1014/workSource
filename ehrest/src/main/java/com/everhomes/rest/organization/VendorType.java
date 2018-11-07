package com.everhomes.rest.organization;

/**
 * <ul>
 * 	<li>支付宝 : 10001</li>
 *	<li>微信 : 10002</li>
 *	<li>个人钱包 : 10003</li>
 *	<li>企业钱包 : 10004</li>
 *  <li>线下支付 : 10005</li>
 *</ul>
 *
 */
public enum VendorType {
	ZHI_FU_BAO("10001","alipay","支付宝支付"),WEI_XIN("10002","wechat","微信支付"),
	PERSONAL_WALLET("10003","personal_wallet","个人钱包支付"),ENTERPRISE_WALLET("10004","enterprise_wallet","企业钱包支付"),
	OFFLINE("10005","offline","线下支付");
	

	private String code;
	private String styleNo;
	private String describe;

	private VendorType(String code,String styleNo,String describe){
		this.code = code;
		this.styleNo = styleNo;
		this.describe = describe;
	}

	public String getVendorType() {
		return code;
	}

	public String getCode() {
		return code;
	}
	public String getStyleNo() {
		return styleNo;
	}

	public String getDescribe() {
		return describe;
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
