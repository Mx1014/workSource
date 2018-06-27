--
-- 通用脚本
-- ADD BY xq.tian  2018/06/15
-- #30750 代码仓库管理 v1.0
--
CREATE TABLE `eh_gogs_repos` (
  `id` BIGINT,
  `namespace_id` INTEGER NOT NULL DEFAULT '0',
  `module_type` VARCHAR(64) NOT NULL,
  `module_id` BIGINT NOT NULL DEFAULT '0' COMMENT 'the module id',
  `owner_type` VARCHAR(64) DEFAULT NULL,
  `owner_id` BIGINT NOT NULL DEFAULT '0',
  `repo_type` VARCHAR(32) NOT NULL,
  `name` VARCHAR(128) DEFAULT NULL COMMENT 'name',
  `full_name` VARCHAR(512) DEFAULT NULL COMMENT 'full name',
  `description` TEXT COMMENT 'description',
  `status` TINYINT NOT NULL DEFAULT '1' COMMENT '0: invalid, 1: valid',
  `create_time` DATETIME(3) DEFAULT NULL,
  `creator_uid` BIGINT DEFAULT NULL,
  `update_time` DATETIME(3) DEFAULT NULL,
  `update_uid` BIGINT DEFAULT NULL,
  `string_tag1` VARCHAR(128) DEFAULT NULL,
  `string_tag2` VARCHAR(128) DEFAULT NULL,
  `string_tag3` VARCHAR(128) DEFAULT NULL,
  `string_tag4` VARCHAR(128) DEFAULT NULL,
  `string_tag5` VARCHAR(128) DEFAULT NULL,
  `integral_tag1` BIGINT NOT NULL DEFAULT '0',
  `integral_tag2` BIGINT NOT NULL DEFAULT '0',
  `integral_tag3` BIGINT NOT NULL DEFAULT '0',
  `integral_tag4` BIGINT NOT NULL DEFAULT '0',
  `integral_tag5` BIGINT NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='gogs repository';

ALTER TABLE eh_flow_scripts ADD COLUMN last_commit VARCHAR(40) COMMENT 'repository last commit id';

-- #30750 END
