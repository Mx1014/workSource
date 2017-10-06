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

    int GENERAL_BUTTON_FIRE_LOG_TEMPLATE = 20001;// 在 ${nodeName} 执行 ${buttonName}

    int TIMEOUT_ABSORT = 20003;// 任务超时 已取消任务
    int DEFAULT_APPROVAL_BUTTON_TRACK_TEXT = 20004;// ${text_tracker_curr_node_name} 已完成
    int DEFAULT_REJECT_BUTTON_TRACK_TEXT = 20005;// 任务已被 ${text_tracker_curr_processors_name} 驳回
    int DEFAULT_SUPERVISE_MESSAGE_TEXT = 20006;// 你有1个 ${text_button_msg_flow_case_title} 任务尚未处理
    int DEFAULT_REMIND_MESSAGE_TEXT = 20007;// 你有1个任务尚未处理

}
