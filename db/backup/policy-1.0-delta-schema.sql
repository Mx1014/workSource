-- 文档管理1.2 start by ryan.
ALTER TABLE `eh_file_management_catalog_scopes` ADD COLUMN `source_type` VARCHAR(64) COMMENT'the type of the source' AFTER `source_id`;
UPDATE `eh_file_management_catalog_scopes` SET source_type = 'MEMBERDETAIL';
-- 文档管理1.2 end by ryan.


-- issue-26471 服务热线V1.5（客服聊天记录保存和导出) by tianxiaoqiang 2018.5.8
CREATE TABLE `eh_message_records` (
  `id` BIGINT(20) NOT NULL COMMENT 'id of the record',
  `namespace_id` INT(11) DEFAULT '0',
  `dst_channel_token` VARCHAR(32) DEFAULT NULL,
  `dst_channel_type` VARCHAR(32) DEFAULT NULL,
  `status` VARCHAR(32) COMMENT 'message status',
  `app_id` BIGINT(20) DEFAULT '1' COMMENT 'default to messaging app itself',
  `message_seq` BIGINT(20) DEFAULT NULL COMMENT 'message sequence id generated at server side',
  `sender_uid` BIGINT(20) DEFAULT NULL,
  `sender_tag` VARCHAR(32) DEFAULT NULL COMMENT 'sender generated tag',
  `channels_info` VARCHAR(2048) DEFAULT NULL,
  `body_type`VARCHAR(32),
  `body` VARCHAR(2048),
  `delivery_option` INT(2) DEFAULT '0',
  `create_time` DATETIME NOT NULL COMMENT 'message creation time',
  `session_token` VARCHAR(128),
  `device_id` VARCHAR(2048),
  `index_id` BIGINT(20),
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


-- issue-26471 服务热线V1.5（客服聊天记录保存和导出) by huangmingbo 2018.5.8
ALTER TABLE `eh_service_hotlines` ADD COLUMN `status` TINYINT NOT NULL DEFAULT '1' COMMENT '0-deleted 1-active';

-- by zheng
ALTER TABLE `eh_configurations` MODIFY `value` VARCHAR(1024);

-- add by yanjun 增加字段长度 201805091806
 
ALTER TABLE `eh_service_module_apps` MODIFY COLUMN `custom_tag`  VARCHAR(256)  NULL DEFAULT '';
-- issue-23470 by liuyilin
ALTER TABLE `eh_door_access`
ADD `enable_amount` TINYINT COMMENT '是否支持按次数开门的授权';

ALTER TABLE `eh_door_auth`
ADD `auth_rule_type` TINYINT COMMENT '授权规则的种类,0 按时间,1 按次数';
ALTER TABLE `eh_door_auth`
ADD `total_auth_amount` INT COMMENT '授权的总开门次数';
ALTER TABLE `eh_door_auth`
ADD `valid_auth_amount` INT COMMENT '剩余的开门次数';
-- End by liuyilin 


-- added by wh 
-- 增加应打卡时间给punch_log
ALTER TABLE `eh_punch_logs` ADD should_punch_time BIGINT COMMENT '应该打卡时间(用以计算早退迟到时长)';

DROP TABLE IF EXISTS `eh_policies`;
CREATE TABLE `eh_policies` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `namespace_id` int(11) NOT NULL DEFAULT '0',
  `owner_type` varchar(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the standard, community, etc',
  `owner_id` bigint(20) NOT NULL DEFAULT '0',
  `category_id` bigint(20),
	`title` VARCHAR(64) NOT NULL DEFAULT '' COMMENT '',
	`outline` VARCHAR(100) NOT NULL DEFAULT '' COMMENT '',
	`content` text COMMENT 'content data',
	`priority` bigint(20) NOT NULL DEFAULT '0' COMMENT 'the rank of policy',
	`creator_uid` bigint(20) NOT NULL DEFAULT '0',
  `create_time` datetime DEFAULT NULL,
	`updater_uid` bigint(20) NOT NULL DEFAULT '0',
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `eh_policy_categories` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `policy_id` bigint(20) NOT NULL COMMENT 'id of the policy',
  `category_id` bigint(20) NOT NULL COMMENT 'category of policy',
	`active_flag` TINYINT(4) NOT NULL DEFAULT 0 COMMENT '',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `eh_policy_records` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `namespace_id` int(11) NOT NULL DEFAULT '0',
  `owner_type` varchar(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the standard, community, etc',
  `owner_id` bigint(20) NOT NULL DEFAULT '0',
  `category_id` bigint(20),
	`creator_id` bigint(20) NOT NULL COMMENT '',
	`creator_name` varchar(128) NOT NULL COMMENT '',
	`creator_phone` VARCHAR(100) NOT NULL DEFAULT '' COMMENT '',
	`creator_org_id` bigint(20) NOT NULL COMMENT '',
	`creator_org_name` varchar(128) NOT NULL COMMENT '',
	`turnover` varchar(60) NOT NULL DEFAULT '' COMMENT '营业额',
	`tax` varchar(60) NOT NULL DEFAULT '' COMMENT '纳税总额',
	`qualification` varchar(60) NOT NULL DEFAULT '' COMMENT '单位资质',
	`financing` varchar(60) NOT NULL DEFAULT '' COMMENT 'A轮融资',
	`create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `eh_policy_agent_rules` (
  `id` bigint(20) NOT NULL DEFAULT '0' COMMENT 'id',
	`namespace_id` int(11) NOT NULL DEFAULT '0',
  `owner_type` varchar(255) DEFAULT NULL COMMENT 'owner type : community ; organization',
  `owner_id` bigint(20) DEFAULT NULL COMMENT 'community id or organization id',
  `agent_flag` TINYINT(4) DEFAULT NULL COMMENT '是否代办:0为不可代办，1为可代办',
  `agent_phone` varchar(64) DEFAULT NULL COMMENT '联系方式',
	`agent_info` text DEFAULT NULL COMMENT '代办介绍',
	`creator_id` bigint(20) NOT NULL COMMENT '创建人',
	`create_time` datetime DEFAULT NULL COMMENT '创建时间',
	`updater_uid` bigint(20) COMMENT '修改人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;