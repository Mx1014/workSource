package com.everhomes.rest.approval;

/**
 * 
 * <ul>true false
 * <li>PRICE: p-当期单价</li>
 * <li>TIMES: t-当期倍率</li>
 * <li>AMOUNT: a-每日或每月读表差</li>
 * <li>REAL_AMOUNT: ra-每日或每月计算用量值</li>
 * </ul>
 */
public enum MeterFormulaVariable {
	PRICE("p"), TIMES("t"),AMOUNT("a"), REAL_AMOUNT("ra");
	
	private String code;
	
	private MeterFormulaVariable(String code) {
		this.code = code;
	}
	
	public String getCode() {
		return this.code;
	}
	
	public MeterFormulaVariable fromCode(String code) {
		if (code != null) {
			for (MeterFormulaVariable trueOrFalseFlag : MeterFormulaVariable.values()) {
				if (trueOrFalseFlag.getCode().equals(code)) {
					return trueOrFalseFlag;
				}
			}
		}
		return null;
	}
}
