-- 通用脚本
-- 增加动态表单的ownerId
ALTER TABLE `eh_var_field_scopes` ADD COLUMN `owner_id`  bigint(20) NOT NULL  DEFAULT  0  AFTER `namespace_id`;
ALTER TABLE `eh_var_field_item_scopes` ADD COLUMN `owner_id`  bigint(20) NOT NULL  DEFAULT  0 AFTER `namespace_id`;
ALTER TABLE `eh_var_field_group_scopes` ADD COLUMN `owner_id`  bigint(20) NOT NULL  DEFAULT  0 AFTER `namespace_id`;


ALTER TABLE `eh_var_field_scopes` ADD COLUMN `owner_type`  VARCHAR(1024)  NULL  AFTER `owner_id`;
ALTER TABLE `eh_var_field_item_scopes` ADD COLUMN `owner_type`  VARCHAR(1024)  NULL AFTER `owner_id`;
ALTER TABLE `eh_var_field_group_scopes` ADD COLUMN `owner_type`  VARCHAR(1024)  NULL AFTER `owner_id`;
-- end


-- 通用脚本
-- AUHOR:jiarui 20180727
-- REMARK:客户管理增加ownerId ownerype
ALTER  TABLE  `eh_enterprise_customers` ADD  COLUMN `owner_id` BIGINT(20) NOT NULL  DEFAULT  0 AFTER  `namespace_id`;
ALTER  TABLE  `eh_enterprise_customers` ADD  COLUMN `owner_type` VARCHAR(1024) NULL AFTER  `owner_id`;
-- end