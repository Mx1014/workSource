package com.everhomes.rest.organization;

/**
 * <ul>
 * <li>CREATED(1): 已创建未执行</li>
 * <li>EXECUTING(2): 执行中</li>
 * <li>FINISH(3): 完成</li>
 * <li>EXCEPTION(4): 完成</li>
 * </ul>
 */
public enum ImportFileTaskStatus {

    CREATED((byte)1), EXECUTING((byte)2), FINISH((byte)3), EXCEPTION((byte)4);

    private byte code;
    private ImportFileTaskStatus(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static ImportFileTaskStatus fromCode(Byte code) {
        if(code != null) {
            for(ImportFileTaskStatus value : ImportFileTaskStatus.values()) {
                if (value.code == code.byteValue()) {
                    return value;
                }
            }
        }
        
        return null;
    }
}
