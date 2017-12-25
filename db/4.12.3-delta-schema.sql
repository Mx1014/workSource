-- 工作流日志加会签标志字段 add by xq.tian 2017/12/25
ALTER TABLE eh_flow_event_logs ADD enter_log_complete_flag TINYINT NOT NULL DEFAULT 0;


-- 意见反馈增加一个字段“业务自定义参数”  add by yanjun 20171220
ALTER TABLE `eh_feedbacks` ADD COLUMN `target_param`  text NULL AFTER `target_id`;

-- add by st.zheng 增加起步价
ALTER TABLE `eh_rentalv2_price_rules`
ADD COLUMN `price_type` TINYINT(4) NULL AFTER `rental_type`,
ADD COLUMN `initiate_price` DECIMAL(10,2) NULL AFTER `weekend_price`,
ADD COLUMN `org_member_initiate_price` DECIMAL(10,2) NULL AFTER `org_member_weekend_price`,
ADD COLUMN `approving_user_initiate_price` DECIMAL(10,2) NULL AFTER `approving_user_weekend_price`;

ALTER TABLE `eh_rentalv2_price_packages`
ADD COLUMN `price_type` TINYINT(4) NULL AFTER `rental_type`,
ADD COLUMN `initiate_price` DECIMAL(10,2) NULL AFTER `price`,
ADD COLUMN `org_member_initiate_price` DECIMAL(10,2) NULL AFTER `org_member_price`,
ADD COLUMN `approving_user_initiate_price` DECIMAL(10,2) NULL AFTER `approving_user_price`;

ALTER TABLE `eh_rentalv2_cells`
ADD COLUMN `price_type` TINYINT(4) NULL AFTER `rental_type`,
ADD COLUMN `initiate_price` DECIMAL(10,2) NULL AFTER `price`,
ADD COLUMN `org_member_initiate_price` DECIMAL(10,2) NULL AFTER `org_member_price`,
ADD COLUMN `approving_user_initiate_price` DECIMAL(10,2) NULL AFTER `approving_user_price`;

-- 增加字段判断是否使用费用清单 by st.zheng
ALTER TABLE `eh_pm_tasks`
ADD COLUMN `if_use_feelist` TINYINT(4) NULL DEFAULT '0' COMMENT '是否使用费用清单 0不使用 1 使用' AFTER `organization_name`;


-- merge from forum-2.2 by yanjun 20171225 start
-- 是否支持投票
ALTER TABLE `eh_polls` ADD COLUMN `wechat_poll` TINYINT(4) DEFAULT '0' NULL COMMENT 'is support wechat poll 0:no, 1:yes';
-- merge from forum-2.2 by yanjun 20171225 end