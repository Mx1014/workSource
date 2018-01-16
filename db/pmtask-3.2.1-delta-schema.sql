-- by st.zheng
ALTER TABLE `eh_pm_tasks`
ADD COLUMN `refer_type` VARCHAR(32) NULL COMMENT '引用类型' AFTER `if_use_feelist`;
ALTER TABLE `eh_pm_tasks`
ADD COLUMN `refer_id` BIGINT(20) NULL COMMENT '引用id' AFTER `refer_type`;
