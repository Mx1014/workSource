-- 机构表里增加字段 记录操作人  2017-04-20 add by sfyan
ALTER TABLE `eh_organizations` ADD COLUMN `creator_uid` BIGINT;
ALTER TABLE `eh_organizations` ADD COLUMN `operator_uid` BIGINT;

-- 机构人员表里增加字段 记录操作人  2017-04-20 add by sfyan
ALTER TABLE `eh_organization_members` ADD COLUMN `creator_uid` BIGINT;
ALTER TABLE `eh_organization_members` ADD COLUMN `operator_uid` BIGINT;

-- 导入文件任务
CREATE TABLE `eh_import_file_tasks` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '',
  `owner_id` BIGINT NOT NULL DEFAULT 0,
  `type` VARCHAR(64) NOT NULL DEFAULT '' COMMENT '',
  `status` TINYINT NOT NULL,
  `result` TEXT,
  `creator_uid` BIGINT,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;