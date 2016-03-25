package com.everhomes.rest.acl;

/**
 * <p>是否关联菜单展示</p>
 * <ul>
 * <li>MENU_HIDE: 不展示菜单</li>
 * <li>MENU_SHOW: 展示菜单</li>
 * </ul>
 */
public enum WebMenuPrivilegeShowFlag {
	
	MENU_HIDE((byte)0), MENU_SHOW((byte)1);
    
    private byte code;
    private WebMenuPrivilegeShowFlag(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static WebMenuPrivilegeShowFlag fromCode(Byte code) {
    	WebMenuPrivilegeShowFlag[] values = WebMenuPrivilegeShowFlag.values();
        for(WebMenuPrivilegeShowFlag value : values) {
        	  if(value.code == code.byteValue()) {
                  return value;
              }
        }
        return null;
    }
}