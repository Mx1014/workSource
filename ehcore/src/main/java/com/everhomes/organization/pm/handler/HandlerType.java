package com.everhomes.organization.pm.handler;

public enum HandlerType {
	OTHER(0L,0);
	
	private long orgId;
	private int parserCode;
	
	private HandlerType(long orgId,int parserCode){
		this.orgId = orgId;
		this.parserCode = parserCode;
	}

	public long getOrgId() {
		return orgId;
	}

	public int getParserCode() {
		return parserCode;
	}
	
	public static HandlerType fromOrgId(long orgId){
		for(HandlerType p : HandlerType.values()){
			if(p.getOrgId() == orgId)
				return p;
		}
		return null;
	}

}
