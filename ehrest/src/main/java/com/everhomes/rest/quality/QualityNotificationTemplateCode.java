package com.everhomes.rest.quality;

public interface QualityNotificationTemplateCode {

	static final String SCOPE = "quality.notification";
    
    static final int GENERATE_QUALITY_TASK_NOTIFY_EXECUTOR = 1; // 生成核查任务给核查人
    static final int ASSIGN_TASK_NOTIFY_OPERATOR = 2; // 指派任务给整改人
}
