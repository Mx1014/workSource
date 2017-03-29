package com.everhomes.rest.flow;

public interface FlowTemplateCode {
	static final String SCOPE = "flow";
	
	public static final int APPROVE_STEP = 10001;
	public static final int REJECT_STEP = 10002;
	public static final int ABSORT_STEP = 10003;
	public static final int TRANSFER_STEP = 10004;
	public static final int COMMENT_STEP = 10005;
	public static final int EVALUATE_STEP = 10006;
	public static final int PROCESSOR_ABSORT = 10007;
	public static final int APPLIER_ABSORT = 10008;
	public static final int NEXT_STEP_DONE = 10009;
}
