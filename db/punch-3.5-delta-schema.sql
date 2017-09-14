-- DROP TABLE IF EXISTS `eh_uniongroup_configures`;
CREATE TABLE `eh_uniongroup_configures` (
  `id` BIGINT(20) NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `enterprise_id` BIGINT(20) DEFAULT 0,
  `group_type` VARCHAR(32) COMMENT 'SalaryGroup,PunchGroup',
  `group_id` BIGINT(20) NOT NULL COMMENT 'id of group',
  `current_id` BIGINT(20) DEFAULT NULL COMMENT 'id of target, organization or memberDetail',
  `current_type` VARCHAR(32) COMMENT 'organziation,memberDetail',
  `current_name` VARCHAR(32) COMMENT 'name',
  `operator_uid` BIGINT(20),
  `update_time` DATETIME,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

--  DROP TABLE IF EXISTS `eh_uniongroup_member_details`;
CREATE TABLE `eh_uniongroup_member_details` (
  `id` BIGINT(20) NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `group_type` VARCHAR(32) COMMENT 'SalaryGroup,PunchGroup',
  `group_id` BIGINT(20) NOT NULL COMMENT 'id of group',
  `detail_id` BIGINT(20) DEFAULT NULL COMMENT 'id of target, only memberDetail',
  `target_type` VARCHAR(64),
  `target_id` BIGINT NOT NULL,
  `enterprise_id` BIGINT NOT NULL COMMENT 'enterprise_id' ,
  `contact_name` VARCHAR(64) COMMENT 'the name of the member',
  `contact_token` VARCHAR(128) COMMENT 'phone number, reference for eh_organization_member contact_token',
  `update_time` DATETIME,
  `operator_uid` BIGINT(20),
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

-- 薪酬组加上版本 By lei.lv 
ALTER TABLE eh_uniongroup_configures ADD COLUMN `version_code` INT COMMENT '版本号';
ALTER TABLE eh_uniongroup_member_details ADD COLUMN `version_code` INT COMMENT '版本号';