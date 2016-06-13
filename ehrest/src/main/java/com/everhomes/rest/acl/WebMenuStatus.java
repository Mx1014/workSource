package com.everhomes.rest.acl;

/**
 * <p>菜单状态</p>
 * <ul>
 * <li>INACTIVE: 无效</li>
 * <li>ACTIVE: 有效</li>
 * </ul>
 */
public enum WebMenuStatus {
	
	INACTIVE((byte)0), ACTIVE((byte)2);
    
    private byte code;
    private WebMenuStatus(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static WebMenuStatus fromCode(Byte code) {
    	WebMenuStatus[] values = WebMenuStatus.values();
        for(WebMenuStatus value : values) {
        	  if(value.code == code.byteValue()) {
                  return value;
              }
        }
        return null;
    }
}