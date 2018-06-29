-- 服务联盟 活动企业数据同步配置项 by jiarui 20180622
CREATE TABLE `eh_customer_configutations` (
  `id` bigint(20) NOT NULL,
  `scope_type` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0: all; 1: namespace; 2: community',
  `scope_id` bigint(20) NOT NULL COMMENT 'communityId namespaceId',
  `value`  tinyint(4) NOT NULL DEFAULT '0' ,
  `status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0: invalid, 2: valid',
  `namespace_id` int(11) not NULL,
  `create_time` datetime DEFAULT NULL COMMENT 'record create time',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 服务联盟 活动企业数据  by jiarui 20180629
CREATE TABLE `eh_customer_potential_datas` (
  `id` bigint(20) NOT NULL,
  `name` text COMMENT 'potential customer name',
  `source_id` BIGINT(20) DEFAULT NULL,
  `source_type` text DEFAULT NULL,
  `source_value` text,
  `operate_uid` bigint(20) DEFAULT NULL,
  `update_time` datetime DEFAULT NULL ,
  `create_time` datetime NOT NULL ,
  `delete_uid` bigint(20) DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


-- 人才团队增加系统内来源  by jiarui 20180622
ALTER TABLE `eh_customer_talents` ADD COLUMN `talent_source_item_id`  tinyint(4) NULL AFTER `age`;
-- 动态表单增加系统来源同步标识  by jiarui
ALTER TABLE `eh_var_field_item_scopes` ADD COLUMN `sync_flag`  tinyint(4) NULL AFTER `business_value`;
ALTER TABLE `eh_var_field_item_scopes` ADD COLUMN `module_id`  tinyint(4) NULL AFTER `business_value`;
ALTER TABLE `eh_var_field_item_scopes` ADD COLUMN `category_id`  tinyint(4) NULL AFTER `business_value`;