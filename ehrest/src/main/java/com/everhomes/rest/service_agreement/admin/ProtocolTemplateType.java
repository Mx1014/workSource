package com.everhomes.rest.service_agreement.admin;


/**
 * SERVICE(1):服务协议
 * PRIVACY(2):隐私协议
 */
public enum ProtocolTemplateType {
    SERVICE((byte)1),PRIVACY((byte)2);

    private Byte code;
    private ProtocolTemplateType(Byte code) {
        this.code = code;
    }
    
    public Byte getCode() {
        return this.code;
    }
    
    public static ProtocolTemplateType fromCode(Byte code) {
        if(code != null) {
        	ProtocolTemplateType[] values = ProtocolTemplateType.values();
            for(ProtocolTemplateType value : values) {
                if(code.equals(value.code)) {
                    return value;
                }
            }
        }
        
        return null;
    }
}
