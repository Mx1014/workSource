-- 人才表，add by tt, 20170511
-- DROP TABLE IF EXISTS `eh_talents`;
CREATE TABLE `eh_talents` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `owner_type` VARCHAR(64),
  `owner_id` BIGINT,
  `name` VARCHAR(64),
  `avatar_uri` VARCHAR(2048),
  `phone` VARCHAR(32),
  `gender` TINYINT,
  `position` VARCHAR(64),
  `category_id` BIGINT,
  `experience` INTEGER,
  `graduate_school` VARCHAR(64),
  `degree` TINYINT,
  `remark` TEXT,
  `enabled` TINYINT,
  `default_order` BIGINT,
  `status` TINYINT NOT NULL COMMENT '0: inactive, 2: active',
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `update_time` DATETIME,
  `operator_uid` BIGINT,

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

-- 人才分类表，add by tt, 20170511
-- DROP TABLE IF EXISTS `eh_talent_categories`;
CREATE TABLE `eh_talent_categories` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `owner_type` VARCHAR(64),
  `owner_id` BIGINT,
  `name` VARCHAR(64),
  `status` TINYINT NOT NULL COMMENT '0: inactive, 2: active',
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `update_time` DATETIME,
  `operator_uid` BIGINT,

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

-- 查询历史记录表，add by tt, 20170511
-- DROP TABLE IF EXISTS `eh_talent_query_histories`;
CREATE TABLE `eh_talent_query_histories` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `owner_type` VARCHAR(64),
  `owner_id` BIGINT,
  `keyword` VARCHAR(64),
  `status` TINYINT NOT NULL COMMENT '0: inactive, 2: active',
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `update_time` DATETIME,
  `operator_uid` BIGINT,

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

-- 添加是否设置了管理员标记，add by tt, 20170522
ALTER TABLE `eh_organizations` ADD COLUMN `set_admin_flag` TINYINT DEFAULT 0;
-- 增加索引，add by tt, 20170522
ALTER TABLE `eh_organization_community_requests` ADD INDEX `member_id` (`member_id`);
ALTER TABLE `eh_organization_community_requests` ADD INDEX `community_id` (`community_id`);

-- 服务联盟	增加排序和是否显示在app端的字段 by dengs, 20170523
ALTER TABLE `eh_service_alliances` ADD COLUMN `display_flag` TINYINT NOT NULL DEFAULT '1' COMMENT '0:hide,1:display';
ALTER TABLE `eh_service_alliances` CHANGE COLUMN `default_order` `default_order` BIGINT COMMENT 'default value is id';

-- 给flowCase增加申请人在当前场景下的公司id字段   add by xq.tian  2017/06/08
ALTER TABLE `eh_flow_cases` ADD COLUMN `applier_organization_id` BIGINT COMMENT 'applier current organization_id';

-- 给eh_content_server_resources表添加前缀索引，并且修改类型为VARCHAR   add by xq.tian  2017/06/21
ALTER TABLE `eh_content_server_resources`	MODIFY COLUMN `resource_id` VARCHAR(512);
ALTER TABLE `eh_content_server_resources`	ADD KEY `i_eh_resource_id` (`resource_id`(20));

  