// @formatter:off
package com.everhomes.rest.pmsy;

/**
 * <ul>停车充值订单状态
 * <li>ALL("019"): 所有账单</li>
 * <li>UNPAID("01"): 未支付账单</li>
 * </ul>
 */
public enum PmsyBillType {
    ALL("019"), UNPAID("01");
    
    private String code;

	private PmsyBillType(String code){
		this.code = code;
	}

	public String getCode() {
		return code;
	}

	public static PmsyBillType fromCode(String code){
		for(PmsyBillType v :PmsyBillType.values()){
			if(v.getCode().equals(code))
				return v;
		}
		return null;
	}
}
