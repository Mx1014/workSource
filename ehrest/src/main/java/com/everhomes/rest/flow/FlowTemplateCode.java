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

	public static final int EVALUATE_STEP_MULTI = 10010;// 用户已评价，查看详情
	public static final int EVALUATE_STEP_WITH_CONTENT = 10011;// 用户评价：${score}分，${content}

	public static final int COMMENT_STEP_IMAGE_COUNT_WITH_USERNAME = 10012;// ${userName}：上传了${imageCount}张图片
	public static final int COMMENT_STEP_IMAGE_COUNT_WITH_APPLIER = 10013;// 发起人：上传了${imageCount}张图片

	public static final int COMMENT_STEP_CONTENT_WITH_USERNAME = 10014;// ${userName}：${content}
	public static final int COMMENT_STEP_CONTENT_WITH_APPLIER = 10015;// 发起人：${content}
}
