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
