package com.everhomes.rest.techpark.expansion;

/**
 * 暂时没用这个字段
 * DELETED(1):已删除
 * NORMAL(2): 正常 
 * */
public enum EnterpriseOpRequestBuildingStatus {
	
	DELETED((byte)1), NORMAL((byte)2) ;
	
	private byte code;
	
	private EnterpriseOpRequestBuildingStatus(byte code){
		this.code = code;
	}
	
	public byte getCode() {
		return code;
	}
	
	public static EnterpriseOpRequestBuildingStatus fromType(byte code) {
		for(EnterpriseOpRequestBuildingStatus v : EnterpriseOpRequestBuildingStatus.values()) {
			if(v.getCode() == code)
				return v;
		}
		return null;
	}
}
