// @formatter:off
package com.everhomes.rest.journal;

public enum JournalStatus {
	INACTIVE((byte)0), ACTIVE((byte)1);
    
    private byte code;
    
    private JournalStatus(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static JournalStatus fromCode(Byte code) {
        if(code != null) {
            JournalStatus[] values = JournalStatus.values();
            for(JournalStatus value : values) {
                if(value.code == code.byteValue()) {
                    return value;
                }
            }
        }
        
        return null;
    }
}
