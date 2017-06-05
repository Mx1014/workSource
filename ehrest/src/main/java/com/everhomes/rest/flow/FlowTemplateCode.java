package com.everhomes.rest.flow;

public interface FlowTemplateCode {
	String SCOPE = "flow";
	
	int APPROVE_STEP = 10001;
	int REJECT_STEP = 10002;
	int ABSORT_STEP = 10003;
	int TRANSFER_STEP = 10004;
	int COMMENT_STEP = 10005;
	int EVALUATE_STEP = 10006;
	int PROCESSOR_ABSORT = 10007;
	int APPLIER_ABSORT = 10008;
	int NEXT_STEP_DONE = 10009;

	int EVALUATE_STEP_MULTI = 10010;// 用户已评价，查看详情
	int EVALUATE_STEP_WITH_CONTENT = 10011;// 用户评价：${score}分，${content}

	int COMMENT_STEP_IMAGE_COUNT_WITH_USERNAME = 10012;// ${userName}：上传了${imageCount}张图片
	int COMMENT_STEP_IMAGE_COUNT_WITH_APPLIER = 10013;// 发起人：上传了${imageCount}张图片

	int COMMENT_STEP_CONTENT_WITH_USERNAME = 10014;// ${userName}：${content}
	int COMMENT_STEP_CONTENT_WITH_APPLIER = 10015;// 发起人：${content}
}
