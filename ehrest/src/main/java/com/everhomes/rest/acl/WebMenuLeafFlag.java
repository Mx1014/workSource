package com.everhomes.rest.acl;

/**
 * <p>叶子节点</p>
 * <ul>
 * <li>NOT_LEAF: 不是叶子节点</li>
 * <li>IS_LEAF: 是页子节点</li>
 * </ul>
 */
public enum WebMenuLeafFlag {
	
	NOT_LEAF((byte)0), IS_LEAF((byte)1);
    
    private byte code;
    private WebMenuLeafFlag(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static WebMenuLeafFlag fromCode(Byte code) {
    	WebMenuLeafFlag[] values = WebMenuLeafFlag.values();
        for(WebMenuLeafFlag value : values) {
        	  if(value.code == code.byteValue()) {
                  return value;
              }
        }
        return null;
    }
}