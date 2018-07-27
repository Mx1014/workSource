-- 通用脚本
-- add by yanlong.liang 20180727
-- 报名表增加表单值ID
ALTER TABLE `eh_activity_roster` ADD COLUMN `general_form_value_id` VARCHAR(512) COMMENT '表单值ID集合';
-- END