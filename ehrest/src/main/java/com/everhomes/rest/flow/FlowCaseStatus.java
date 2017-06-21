package com.everhomes.rest.flow;

/**
 * <ul>
 * <li>INVALID(0): 无效</li>
 * <li>INITIAL(1): 初始化</li>
 * <li>PROCESS(2): 处理中</li>
 * <li>ABSORTED(3): 已完成（已完成，但是处于异常的结束）</li>
 * <li>FINISHED(4): 已完成</li>
 * <li>EVALUATE(5): 待评价</li>
 * <li> 注意： 不能随便改 FlowCaseStatus 的参数值 </li>
 * </ul>
 * @author janson
 *
 */
public enum FlowCaseStatus {
	INVALID((byte)0),
    INITIAL((byte)1),
    PROCESS((byte)2),
    ABSORTED((byte)3),
    FINISHED((byte)4),
    EVALUATE((byte)5);

	private byte code;

    public byte getCode() {
        return this.code;
    }

    private FlowCaseStatus(byte code) {
        this.code = code;
    }

    public int getCodeInt() {
        return (int)this.code;
    }

    public static FlowCaseStatus fromCode(Byte code) {
        if(code == null)
            return null;
        
        switch(code.byteValue()) {
        case 0 :
            return INVALID;
        case 1 :
        		return INITIAL;
        case 2 :
            return PROCESS;
        case 3 :
            return ABSORTED;
        case 4 :
            return FINISHED;
        case 5 :
            return EVALUATE;
        }
        
        return null;
    }
}
