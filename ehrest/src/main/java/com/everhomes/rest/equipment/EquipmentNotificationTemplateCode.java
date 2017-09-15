package com.everhomes.rest.equipment;

public interface EquipmentNotificationTemplateCode {

	static final String SCOPE = "equipment.notification";
    
    static final int GENERATE_EQUIPMENT_TASK_NOTIFY = 1; // 生成核查任务
    static final int ASSIGN_TASK_NOTIFY_OPERATOR = 2; // 指派任务给维修人
    
    static final int ASSIGN_TASK_MSG = 3; // 指派任务记录信息
    static final int UNQUALIFIED_EQUIPMENT_NOTIFY_EXECUTOR = 4; // 设备-标准关联审阅不合格通知
    static final int QUALIFIED_EQUIPMENT_NOTIFY_EXECUTOR = 5; // 设备-标准关联审阅合格通知
    static final int EQUIPMENT_TASK_DELAY = 6; // 过期未执行任务给管理员 审阅人员通知
    static final int EQUIPMENT_TASK_BEFORE_BEGIN = 7; // 任务开始前提醒
    static final int EQUIPMENT_TASK_BEFORE_DELAY = 8; // 任务过期前提醒
    static final int EQUIPMENT_TASK_AFTER_DELAY = 9; // 任务过期后提醒
}
