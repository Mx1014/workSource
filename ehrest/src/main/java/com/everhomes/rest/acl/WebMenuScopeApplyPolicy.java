package com.everhomes.rest.acl;

/**
 * <p>菜单控制</p>
 * <ul>
 * <li>DELETE: 删掉菜单</li>
 * <li>OVERRIDE: 覆盖掉菜单</li>
 * <li>REVERT: 添加菜单</li>
 * </ul>
 */
public enum WebMenuScopeApplyPolicy {
	
	DELETE((byte)0), OVERRIDE((byte)1), REVERT((byte)2);
    
    private byte code;
    private WebMenuScopeApplyPolicy(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static WebMenuScopeApplyPolicy fromCode(Byte code) {
        if(null == code){
            return null;
        }
        for (WebMenuScopeApplyPolicy flag : WebMenuScopeApplyPolicy.values()) {
            if (flag.code == code.byteValue()) {
                return flag;
            }
        }
        return null;
    }
}