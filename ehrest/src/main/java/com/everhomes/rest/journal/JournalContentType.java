// @formatter:off
package com.everhomes.rest.journal;

/**
 * <ul>
 * <li>LINK(1): 链接</li>
 * </ul>
 */
public enum JournalContentType {
    LINK((byte)1);
    
    private byte code;
    
    private JournalContentType(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static JournalContentType fromCode(Byte code) {
        if(code != null) {
            JournalContentType[] values = JournalContentType.values();
            for(JournalContentType value : values) {
                if(value.code == code.byteValue()) {
                    return value;
                }
            }
        }
        
        return null;
    }
}
