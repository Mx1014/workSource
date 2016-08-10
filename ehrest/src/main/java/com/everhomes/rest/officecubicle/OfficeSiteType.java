package com.everhomes.rest.officecubicle;


public enum OfficeSiteType {
	CUBICLE((byte)0,"开放式工位"),
	CUBICLEOFFICE((byte)1,"固定办公室类别-工位"),
	AREAOFFICE((byte)2,"固定办公室类别-面积");

	private byte code; 
	private String msg;

	private OfficeSiteType(byte code ,String msg){
		this.code=code; 
		this.msg=msg;
	}
	public int getCode() {
		return code;
	} 
	public String getMsg() {
		return msg;
	}

    public static OfficeSiteType fromCode(byte code) {
        for(OfficeSiteType t : OfficeSiteType.values()) {
            if (t.code == code) {
                return t;
            }
        }
        
        return null;
    }
}
