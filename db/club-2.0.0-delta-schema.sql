-- 圈添加审核状态，add by tt, 20161028
ALTER TABLE `eh_groups` ADD COLUMN `approval_status` TINYINT COMMENT 'approval status';
ALTER TABLE `eh_groups` ADD COLUMN `operator_uid` BIGINT;

-- 设置圈（俱乐部）参数，add by tt, 20161028
-- DROP TABLE IF EXISTS `eh_group_settings`;
CREATE TABLE `eh_group_settings` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0 COMMENT 'namespace id',
  `create_flag` TINYINT COMMENT 'whether allow create club',
  `verify_flag` TINYINT COMMENT 'whether need verify',
  `member_post_flag` TINYINT COMMENT 'whether allow members create post',
  `member_comment_flag` TINYINT COMMENT 'whether allow members comment on the post',
  `admin_broadcast_flag` TINYINT COMMENT 'whether allow admin broadcast',
  `broadcast_count` INTEGER COMMENT 'how many broadcasts can be created per day',
  `creator_uid` BIGINT NOT NULL DEFAULT 0 COMMENT 'news creator uid',
  `create_time` DATETIME COMMENT 'create time',
  `operator_uid` BIGINT NOT NULL DEFAULT 0 COMMENT 'operator uid',
  `update_time` DATETIME COMMENT 'update time',
  
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 广播消息，add by tt, 20161028
-- DROP TABLE IF EXISTS `eh_broadcasts`;
CREATE TABLE `eh_broadcasts` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0 COMMENT 'namespace id',
  `owner_type` VARCHAR(32) COMMENT 'group',
  `owner_id` BIGINT DEFAULT 0 COMMENT 'group_id',
  `title` VARCHAR(1024) COMMENT 'title',
  `content_type` VARCHAR(32) COMMENT 'object content type: text、rich text',
  `content` LONGTEXT COMMENT 'content data, depends on value of content_type',
  `content_abstract` TEXT COMMENT 'abstract of content data',
  `creator_uid` BIGINT NOT NULL DEFAULT 0 COMMENT 'creator uid',
  `create_time` DATETIME COMMENT 'create time',
  `operator_uid` BIGINT NOT NULL DEFAULT 0 COMMENT 'operator uid',
  `update_time` DATETIME COMMENT 'update time',
  
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 版本url表添加图标的链接地址, add by tt, 20161107
ALTER TABLE `eh_version_urls`	ADD COLUMN `icon_url` VARCHAR(50);




-- 以下从3.11.0合并过来
-- merge from pmtask-delta-schema.sql
ALTER TABLE eh_pm_tasks ADD COLUMN `task_category_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'task category id';
ALTER TABLE eh_pm_tasks ADD COLUMN `reserve_time` DATETIME;
ALTER TABLE eh_pm_tasks ADD COLUMN `priority` TINYINT NOT NULL DEFAULT 0 COMMENT 'task rank of request';
ALTER TABLE eh_pm_tasks ADD COLUMN `source_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'task come from ,such as app ,email';
ALTER TABLE eh_pm_tasks ADD COLUMN `organization_id` BIGINT NOT NULL DEFAULT 0;
ALTER TABLE eh_pm_tasks ADD COLUMN `requestor_name` VARCHAR(64) COMMENT 'the name of requestor';
ALTER TABLE eh_pm_tasks ADD COLUMN `requestor_phone` VARCHAR(64) COMMENT 'the phone of requestor';
ALTER TABLE eh_pm_tasks ADD COLUMN `address_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'address id';
ALTER TABLE eh_pm_tasks ADD COLUMN `revisit_content` TEXT COMMENT 'revisit content';
ALTER TABLE eh_pm_tasks ADD COLUMN `revisit_time` DATETIME;
ALTER TABLE eh_pm_task_statistics ADD COLUMN `task_category_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'task category id';

CREATE TABLE `eh_pm_task_targets` (
  `id` bigint(20) NOT NULL,
  `owner_type` varchar(64) NOT NULL,
  `owner_id` bigint(20) DEFAULT NULL,
  `target_type` varchar(64) NOT NULL COMMENT 'target object(user/group) type',
  `target_id` bigint(20) DEFAULT NULL COMMENT 'target object(user/group) id',
  `role_id` BIGINT NOT NULL DEFAULT 0,
  `status` TINYINT NOT NULL DEFAULT 2 COMMENT '0: inactive, 2: active',

  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


-- merge from customer-manage-1.1-delta-schema.sql 20161025 by lqs
--
-- 车辆停车类型     add by xq.tian 2016/10/11
--
CREATE TABLE `eh_parking_card_categories` (
  `id` BIGINT NOT NULL,
  `namespace_id` INT NOT NULL DEFAULT '0',
  `owner_type` VARCHAR(32) DEFAULT NULL,
  `owner_id` BIGINT DEFAULT NULL,
  `card_type` TINYINT NOT NULL COMMENT '1. temp, 2. month, 3. free ,etc.',
  `category_name` VARCHAR(64) NOT NULL COMMENT 'name of category',
  `status` TINYINT NOT NULL COMMENT '1: normal, 0: delete',
  `creator_uid` BIGINT DEFAULT NULL,
  `create_time` DATETIME DEFAULT NULL,

  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- item 类别 by sfyan 20161025
-- DROP TABLE IF EXISTS `eh_item_service_categries`;
CREATE TABLE `eh_item_service_categries` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `name` VARCHAR(64) NOT NULL COMMENT 'service categry name',
  `icon_uri` VARCHAR(1024) COMMENT 'service categry icon uri',
  `order` INTEGER COMMENT 'order ',
  `align` TINYINT DEFAULT '0' COMMENT '0: left, 1: center',
  `status` TINYINT NOT NULL DEFAULT '1' COMMENT '0: inactive, 1: active',
  `namespace_id` INTEGER,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

ALTER TABLE `eh_launch_pad_items` ADD COLUMN `service_categry_id` BIGINT COMMENT 'service categry id';




-- 以下为3.10.4合过来的脚本-------------

--
-- 服务联盟category添加显示类型字段   add by xq.tian  2016/10/18
--
-- ALTER TABLE `eh_service_alliance_categories` ADD COLUMN `display_mode` TINYINT DEFAULT 1;


-- 服务联盟添加新的预约表 add by xiongying20161027
CREATE TABLE `eh_service_alliance_reservation_requests` (
  `id` bigint(20) NOT NULL,
  `namespace_id` int(11) NOT NULL DEFAULT '0',
  `owner_type` varchar(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the category, community, etc',
  `owner_id` bigint(20) NOT NULL DEFAULT '0',
  `type` bigint(20) NOT NULL DEFAULT '0',
  `category_id` bigint(20) NOT NULL DEFAULT '0',
  `creator_uid` bigint(20) NOT NULL DEFAULT '0' COMMENT 'record creator user id',
  `creator_name` varchar(128) DEFAULT NULL,
  `creator_mobile` varchar(128) DEFAULT NULL,
  `creator_organization_id` bigint(20) NOT NULL DEFAULT '0',
  `service_alliance_id` bigint(20) NOT NULL DEFAULT '0',
  `reserve_type` varchar(128) DEFAULT NULL,
  `reserve_organization` varchar(128) DEFAULT NULL,
  `reserve_time` varchar(128) DEFAULT NULL,
  `contact` varchar(128) DEFAULT NULL,
  `mobile` varchar(128) DEFAULT NULL,
  `remarks` varchar(1024) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,

  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 添加人数限制字段, add by tt, 20161027
ALTER TABLE `eh_forum_posts` ADD COLUMN `max_quantity` INT COMMENT 'max person quantity';
ALTER TABLE `eh_activities` ADD COLUMN `max_quantity` INT COMMENT 'max person quantity';
ALTER TABLE `eh_activities` ADD COLUMN `content_type` varchar(128) COMMENT 'content type, text/rich_text';
ALTER TABLE `eh_activities` ADD COLUMN `version` varchar(128) COMMENT 'version';

-- 添加消息提醒设置表, add by tt, 20161027
-- DROP TABLE IF EXISTS `eh_warning_settings`;
CREATE TABLE `eh_warning_settings` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL COMMENT 'namespace id',
  `type` varchar(64) COMMENT 'type',
  `time` BIGINT COMMENT 'millisecond',
  `create_time` DATETIME,
  `creator_uid` BIGINT,
  `update_time` DATETIME,
  `operator_uid` BIGINT,

  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 以上为3.10.4合过来的脚本-------------

-- global table. 添加门禁特殊权限相关用户类型 add by Janson 20161028
-- DROP TABLE IF EXISTS `eh_door_user_permission`;
CREATE TABLE `eh_door_user_permission` (
    `id` BIGINT NOT NULL COMMENT 'id of the record',
    `namespace_id` INT NOT NULL DEFAULT '0',
    `user_id` BIGINT NOT NULL,
    `approve_user_id` BIGINT NOT NULL,
    `auth_type` TINYINT NOT NULL COMMENT '0: Door Guard',
    `owner_type` TINYINT NOT NULL COMMENT '0:community, 1:enterprise, 2: family, 3: user',
    `owner_id` BIGINT NOT NULL DEFAULT 0,

    `integral_tag1` BIGINT DEFAULT 0 NOT NULL,
    `integral_tag2` BIGINT DEFAULT 0 NOT NULL,
    `integral_tag3` BIGINT DEFAULT 0 NOT NULL,
    `integral_tag4` BIGINT DEFAULT 0 NOT NULL,
    `string_tag1` VARCHAR(128),
    `string_tag2` VARCHAR(128),
    `string_tag3` VARCHAR(128),
    `string_tag4` VARCHAR(128),

    `description` VARCHAR(1024),

    `create_time` DATETIME,
    `status` TINYINT NOT NULL,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
