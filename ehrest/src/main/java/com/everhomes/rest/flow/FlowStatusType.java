package com.everhomes.rest.flow;

/**
 * <ul>
 * <li>INVALID: 无效</li>
 * <li>VALID: 有效</li>
 * <li>CONFIG: 配置中</li>
 * <li>SNAPSHOT: 生成 Snapshot 中</li>
 * <li>RUNNING: 运行中</li>
 * <li>STOP: 停止中</li>
 * 
 * </ul>
 * @author janson
 *
 */
public enum FlowStatusType {
	INVALID((byte)0), VALID((byte)1), CONFIG((byte)2), SNAPSHOT((byte)3), RUNNING((byte)4), STOP((byte)5);
	private byte code;
    
    public byte getCode() {
        return this.code;
    }
    
    private FlowStatusType(byte code) {
        this.code = code;
    }
    
    public static FlowStatusType fromCode(Byte code) {
        if(code == null)
            return null;
        
        switch(code.byteValue()) {
        case 0 :
            return INVALID;
        case 1 :
        		return VALID;
        case 2 :
            return CONFIG;
        case 3:
            return RUNNING;
        case 4:
        	   return STOP;
        }
        
        return null;
    }
}
