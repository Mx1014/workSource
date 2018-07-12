-- --合同管理 基础设置合同规则
ALTER TABLE `eh_contract_params` ADD COLUMN `payorreceive_contract_type` tinyint(2) DEFAULT '0' COMMENT '0 收款合同 1付款合同';
ALTER TABLE `eh_contract_params` ADD COLUMN `contract_number_rulejson` text NULL COMMENT '合同规则';
ALTER TABLE `eh_contract_params` ADD COLUMN `update_time` datetime NULL COMMENT '更新时间';
ALTER TABLE `eh_contract_params` ADD COLUMN `category_id` bigint(20) NULL COMMENT 'contract category id';
-- --合同管理 合同多入口设置
ALTER TABLE `eh_contracts` ADD COLUMN `category_id` bigint(20) NULL COMMENT 'contract category id';
-- --合同管理，表单设置，动态字段
ALTER TABLE `eh_var_field_scopes` ADD COLUMN `category_id` bigint(20) NULL COMMENT 'category id';
ALTER TABLE `eh_var_field_item_scopes` ADD COLUMN `category_id` bigint(20) NULL COMMENT 'category id';
ALTER TABLE `eh_var_field_group_scopes` ADD COLUMN `category_id` bigint(20) NULL COMMENT 'category id';


CREATE TABLE `eh_contract_categories` (
  `id` bigint(20) NOT NULL,
  `owner_type` varchar(32) DEFAULT '' COMMENT 'the type of who own the category, community, etc',
  `owner_id` bigint(20) DEFAULT '0',
  `parent_id` bigint(20) DEFAULT '0',
  `name` varchar(64) DEFAULT NULL,
  `path` varchar(128) DEFAULT NULL,
  `default_order` int(11) DEFAULT NULL,
  `status` tinyint(4) DEFAULT '0' COMMENT '0: disabled, 1: waiting for confirmation, 2: active',
  `creator_uid` bigint(20) DEFAULT '0' COMMENT 'record creator user id',
  `create_time` datetime DEFAULT NULL,
  `delete_uid` bigint(20) DEFAULT '0' COMMENT 'record deleter user id',
  `delete_time` datetime DEFAULT NULL,
  `namespace_id` int(11) DEFAULT '0',
  `logo_uri` varchar(1024) DEFAULT NULL COMMENT 'default cover uri',
  `entry_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

