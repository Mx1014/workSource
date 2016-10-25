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