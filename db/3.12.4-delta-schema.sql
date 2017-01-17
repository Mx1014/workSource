-- merge doorAuth by sfyan 20170117
-- DROP TABLE IF EXISTS `eh_door_auth_logs`;
CREATE TABLE `eh_door_auth_logs` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `door_id` BIGINT NOT NULL,
  `user_id` BIGINT NOT NULL,
  `is_auth` TINYINT NOT NULL DEFAULT '0',
  `right_content` VARCHAR(1024) NOT NULL DEFAULT '0',
  `create_time` datetime DEFAULT NULL,
  `create_uid` BIGINT NOT NULL,
  `discription` text,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 增加机构类型
ALTER TABLE `eh_organization_members` ADD COLUMN `group_type` varchar(64) DEFAULT NULL COMMENT 'ENTERPRISE, DEPARTMENT, GROUP, JOB_POSITION, JOB_LEVEL, MANAGER';



