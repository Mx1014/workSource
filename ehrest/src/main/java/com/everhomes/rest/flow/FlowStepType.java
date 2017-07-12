package com.everhomes.rest.flow;

/**
 * <ul>
 * <li>approve_step: 下一步</li>
 * <li>reject_step: 驳回</li>
 * <li>transfer_step: 转交</li>
 * <li>comment_step: 评论</li>
 * <li>absort_step: 终止</li>
 * <li>reminder_step: 催办</li>
 * <li>evaluate_step: 评价</li>
 * </ul>
 * @author janson
 *
 */
public enum FlowStepType {
	NO_STEP("no_step"),
    START_STEP("start_step"),
    APPROVE_STEP("approve_step"),
    REJECT_STEP("reject_step"),
    TRANSFER_STEP("transfer_step"),
    COMMENT_STEP("comment_step"),
	ABSORT_STEP("absort_step"),
    REMINDER_STEP("reminder_step"),
    EVALUATE_STEP("evaluate_step"),
    END_STEP("end_step"),
    ;
	
	private String code;
    private FlowStepType(String code) {
        this.code = code;
    }
    
    public String getCode() {
        return this.code;
    }
    
    public static FlowStepType fromCode(String code) {
    	if(code == null) {
    		return null;
    	}
    	
    	for(FlowStepType t : FlowStepType.values()) {
    		if(code.equalsIgnoreCase(t.getCode())) {
    			return t;
    		}
    	}

        return null;
    }
    
    public int getCodeInt() {
    	int i = 0;
    	for(FlowStepType t : FlowStepType.values()) {
    		if(code.equalsIgnoreCase(t.getCode())) {
    			return i;
    		}
    		i++;
    	}
    	return 0xFF;
    }
}
