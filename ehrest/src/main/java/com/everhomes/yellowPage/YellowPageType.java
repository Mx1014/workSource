package com.everhomes.yellowPage;
 
/**
 * <ul>
 * <li>MAKERZONE(1): 创客空间  </li>
 * <li>SERVICEALLIANCE(2): 服务联盟 </li> 
 * <li>PARKENTERPRISE(3): 园区企业</li> 
 * </ul>
 */
public enum YellowPageType {
   
	MAKERZONE((byte)1),SERVICEALLIANCE((byte)2),PARKENTERPRISE((byte)3);
    
    private byte code;
    private YellowPageType(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static YellowPageType fromCode(byte code) {
        for(YellowPageType t : YellowPageType.values()) {
            if (t.code == code) {
                return t;
            }
        }
        
        return null;
    }
}
