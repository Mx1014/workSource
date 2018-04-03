
-- 意见反馈增加一个字段“业务自定义参数”  add by yanjun 20171220
ALTER TABLE `eh_feedbacks` ADD COLUMN `target_param`  text NULL AFTER `target_id`;