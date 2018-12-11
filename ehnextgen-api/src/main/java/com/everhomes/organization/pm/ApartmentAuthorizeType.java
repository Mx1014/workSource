package com.everhomes.organization.pm;

/**
 * 授权价周期单位(1:每天; 2:每月; 3:每季度; 4:每年;)
 * @author steve
 */
public enum ApartmentAuthorizeType {
	
	DAY((byte)1, "每天"),
	MONTH((byte)2, "每月"),
	QUARTER((byte)3, "每季度"),
	YEAR((byte)4, "每年");
    
    private byte code;
    private String desc;
    
    private ApartmentAuthorizeType(byte code, String desc) {
        this.code = code;
        this.desc = desc;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public String getDesc() {
		return this.desc;
	}

	public static ApartmentAuthorizeType fromCode(Byte code) {
       if (code != null) {
    	   for (ApartmentAuthorizeType type : ApartmentAuthorizeType.values()) {
    		   if (type.code == code.byteValue()) {
    			   return type;
    		   }
    	   	}
       }
       return null;
    }
    
    public static ApartmentAuthorizeType fromDesc(String desc) {
    	if (desc != null && !desc.isEmpty()) {
			for (ApartmentAuthorizeType type : ApartmentAuthorizeType.values()) {
				if (type.desc.equals(desc)) {
					return type;
				}
			}
		}
    	return null;
    }
}
