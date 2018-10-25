package com.everhomes.rest.user;

/**
 * <ul>来自于第三方用户的类型，对应eh_users表听 namespace_user_type字段
 * <li>WANKE("wanke"): 万科</li>
 * <li>WX("wx"): 微信</li>
 * <li>JINDIE("jindie"): 金蝶</li>
 * <li>GUOMAO("guomao"): 国贸</li>
 * <li>ANBANG("anbang")：安邦物业</li>
 * </ul>
 */
public enum NamespaceUserType {
	WANKE("wanke"), WX("wx"), JINDIE("jindie"), GUOMAO("guomao"), ANBANG("anbang"), ALIPAY("alipay");
    
    private String code;
    private NamespaceUserType(String code) {
        this.code = code;
    }
    
    public String getCode() {
        return this.code;
    }
    
    public static NamespaceUserType fromCode(String code) {
        if(code != null) {
        	NamespaceUserType[] values = NamespaceUserType.values();
            for(NamespaceUserType value : values) {
                if(code.equals(value.code)) {
                    return value;
                }
            }
        }
        
        return null;
    }
}
