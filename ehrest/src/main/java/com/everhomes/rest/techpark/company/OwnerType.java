package com.everhomes.rest.techpark.company;

public enum OwnerType {
	COMPANY("company");
	
	private String code;
	
	private  OwnerType(String code){
		this.code = code;
	}

	public String getCode() {
		return code;
	}

	public static OwnerType fromCode(String code){
		if(code != null){
			for(OwnerType r:OwnerType.values()){
				if(r.getCode().equals(code)){
					return r;
				}
			}
		}
		return null;
	}
	 

}
