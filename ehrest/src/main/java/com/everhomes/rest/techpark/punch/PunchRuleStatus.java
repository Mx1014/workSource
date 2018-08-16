package com.everhomes.rest.techpark.punch;

/**
 * <ul>打卡规则的状态
 * <li>DELETE(5): 删除次日生效</li>
 * <li>NEW(4): 新规则次日生效</li>
 * <li>MODIFYED(3): 修改次日生效</li>
 * <li>ACTIVE(2): 生效中</li>
 * <li>DELETED(1): 已删除</li>
 * </ul>
 */
public enum PunchRuleStatus {
	DELETING((byte)5),
	/**NEW(4): 新规则次日生效*/
	NEW((byte)4),
	/**MODIFYED(3): 修改次日生效*/
	MODIFYED((byte)3),
	/**ACTIVE(2): 生效中*/
	ACTIVE((byte)2),
	/*** <li>DELETED(1): 删除</li>*/
	DELETED((byte)1) ;

    private byte code;
    private PunchRuleStatus(byte code) {
        this.code = code;
    }
    
    public byte getCode() {
        return this.code;
    }
    
    public static PunchRuleStatus fromCode(Byte code) {
    	if(null == code)
    		return null;
        for(PunchRuleStatus t : PunchRuleStatus.values()) {
            if (code.equals(t.code)) {
                return t;
            }
        }
        
        return null;
    }
}
