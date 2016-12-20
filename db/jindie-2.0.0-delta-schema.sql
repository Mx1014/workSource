-- 科技园同步数据备份表，add by tt, 20161212
-- DROP TABLE IF EXISTS `eh_techpark_syncdata_backup`;
CREATE TABLE `eh_techpark_syncdata_backup` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT '0',
  `data_type` TINYINT NOT NULL COMMENT '1: building, 2: apartment, 3: contract',
  `all_flag` TINYINT NOT NULL COMMENT '1: all data, 0: new insert or update',
  `next_page` INTEGER COMMENT 'next page',
  `var_data_list` LONGTEXT COMMENT 'insert or update data',
  `del_data_list` LONGTEXT COMMENT 'delete data',
  `status` TINYINT NOT NULL COMMENT '0: inactive, 2: active',
  `create_time` DATETIME DEFAULT NULL,
  `creator_uid` BIGINT DEFAULT NULL,
  `update_time` DATETIME DEFAULT NULL,
  
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

ALTER TABLE `eh_organization_address_mappings` ADD COLUMN `namespace_type` VARCHAR(64);
ALTER TABLE `eh_organizations` CHANGE COLUMN `name` `name` VARCHAR(128);