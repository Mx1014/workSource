package com.everhomes.rest.organization;

public enum BillTransactionResult {
	FAIL((int)1,"交易失败"),SUCCESS((int)2,"交易成功");
	
	private int code;
	private String description;
	private BillTransactionResult(int code,String description){
		this.code = code;
		this.description = description;
	}
	public int getCode() {
		return code;
	}
	public String getDescription() {
		return description;
	}
	public static BillTransactionResult fromCode(int code){
		for(BillTransactionResult v : BillTransactionResult.values()){
			if(v.getCode() == code)
				return v;
		}
		return null;
	}

}
