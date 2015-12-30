package com.everhomes.rest.organization.pm;

public enum PmBillOperLogType {
	
		INSERT("INSERT"),UPDATE("UPDATE"),DELETE("DELETE");
		private String code;
		
		private PmBillOperLogType(String code){
			this.code = code;
		}
		public String getCode() {
			return code;
		}
		
		public PmBillOperLogType fromCode(String code){
			for(PmBillOperLogType v : PmBillOperLogType.values()){
				if(v.getCode().equals(code))
					return v;
			}
			return null;
		}

}
