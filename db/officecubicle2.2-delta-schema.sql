-- by dengs. 工位预约添加范围 20180120
ALTER TABLE `eh_office_cubicle_spaces` ADD COLUMN `owner_type` VARCHAR(128);
ALTER TABLE `eh_office_cubicle_spaces` ADD COLUMN `owner_id` BIGINT;

ALTER TABLE `eh_office_cubicle_categories` ADD COLUMN `position_nums` INTEGER;

ALTER TABLE `eh_office_cubicle_orders` ADD COLUMN `flow_case_Id` BIGINT;
ALTER TABLE `eh_office_cubicle_orders` ADD COLUMN `work_flow_status` TINYINT;


CREATE TABLE `eh_office_cubicle_ranges` (
  `id` BIGINT NOT NULL,
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the ranges, community, etc',
  `owner_id` BIGINT NOT NULL DEFAULT 0,
  `space_id` BIGINT NOT NULL DEFAULT 0,
  `creator_uid` BIGINT NOT NULL DEFAULT 0 COMMENT 'record creator user id',
  `create_time` DATETIME,
  `delete_uid` BIGINT NOT NULL DEFAULT 0 COMMENT 'record deleter user id',
  `delete_time` DATETIME,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;
