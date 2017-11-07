
-- 薪酬组加上版本 By lei.lv 
ALTER TABLE eh_uniongroup_configures ADD COLUMN `version_code` INT DEFAULT 0 COMMENT '版本号';
ALTER TABLE eh_uniongroup_member_details ADD COLUMN `version_code` INT DEFAULT 0 COMMENT '版本号';
 -- 更改索引
ALTER TABLE `eh_uniongroup_member_details`  DROP INDEX `uniongroup_member_uniqueIndex`;
ALTER TABLE eh_uniongroup_member_details ADD UNIQUE INDEX `uniongroup_member_uniqueIndex` (`group_type`, `group_id`, `detail_id`, `contact_token`,`version_code`) ;

ALTER TABLE `eh_punch_schedulings` ADD COLUMN `status` TINYINT DEFAULT 2 COMMENT ' 规则状态 1-已删除 2-正常 3-次日更新 4-新规则次日生效';  

-- 打卡加入当前生效版本号
-- ALTER TABLE eh_punch_rules ADD COLUMN `version_code` INT DEFAULT 0 COMMENT '当前生效版本号';

-- 维护uniongroup现在使用的是哪个version
-- drop TABLE `eh_uniongroup_version`;
CREATE TABLE `eh_uniongroup_version` (
  `id` BIGINT(20) NOT NULL COMMENT 'id of the record',
  `namespace_id` INT(11) NOT NULL DEFAULT '0',
  `enterprise_id` BIGINT(20) DEFAULT '0',
  `group_type` VARCHAR(32) DEFAULT NULL COMMENT 'SalaryGroup,PunchGroup', 
  `current_version_code` INT(11) DEFAULT '0' COMMENT '当前使用的版本号 从1开始 , 0默认是config版本', 
  `operator_uid` BIGINT(20) DEFAULT NULL,
  `update_time` DATETIME DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 ;
