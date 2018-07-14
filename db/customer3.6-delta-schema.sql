-- 服务联盟 活动企业数据同步配置项 by jiarui 20180622
CREATE TABLE `eh_customer_configutations` (
  `id` bigint(20) NOT NULL,
  `scope_type` VARCHAR(64)  DEFAULT NULL COMMENT 'service_alliance or activity',
  `scope_id` bigint(20) NOT NULL COMMENT 'code',
  `value`  tinyint(4) NOT NULL DEFAULT '0' ,
  `status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0: invalid, 2: valid',
  `namespace_id` int(11) not NULL,
  `create_time` datetime DEFAULT NULL COMMENT 'record create time',
  `creator_uid` BIGINT(20) DEFAULT NULL COMMENT 'creatorUid',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 服务联盟 活动企业数据  by jiarui 20180629
CREATE TABLE `eh_customer_potential_datas` (
  `id` bigint(20) NOT NULL,
  `namespace_id` int(11) NOT NULL  DEFAULT 0,
  `name` text COMMENT 'potential customer name',
  `source_id` bigint(20) DEFAULT NULL COMMENT 'refer to service allance activity categoryId',
  `source_type` varchar(1024) DEFAULT NULL COMMENT 'service_alliance or activity',
  `status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0: invalid, 2: valid',
  `operate_uid` bigint(20) DEFAULT NULL,
  `update_time` datetime DEFAULT NULL ,
  `create_time` datetime NOT NULL ,
  `delete_time` datetime  NULL ,
  `delete_uid` bigint(20) DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 客户管理增加信息来源  by jiarui 20180622
ALTER  TABLE `eh_enterprise_customers` ADD COLUMN `source_id` BIGINT(20)  NULL ;
ALTER  TABLE `eh_enterprise_customers` ADD COLUMN `source_type` VARCHAR(64)  NULL;
-- 人才团队增加系统内来源  by jiarui 20180622
ALTER TABLE `eh_customer_talents` ADD COLUMN `talent_source_item_id`  BIGINT(20) NULL COMMENT 'categoryId' AFTER `age`;
ALTER TABLE `eh_customer_talents` ADD COLUMN `origin_source_id` bigint(20) NULL COMMENT 'origin potential data primary key' AFTER `age`;
ALTER TABLE `eh_customer_talents` ADD COLUMN `origin_source_type`  VARCHAR(64) NULL COMMENT 'service_alliance or activity' AFTER `age`;
ALTER TABLE `eh_customer_talents` ADD COLUMN `register_status`  TINYINT(4) NOT NULL  DEFAULT  0 AFTER `age`;

