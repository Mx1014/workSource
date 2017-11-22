-- 用户打印机映射表，by dengs,2017/11/12
-- DROP TABLE IF EXISTS `eh_siyin_user_printer_mappings`;
CREATE TABLE `eh_siyin_user_printer_mappings` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0 COMMENT 'namespace id',
  `user_id` BIGINT,
  `reader_name` VARCHAR(128) COMMENT 'printer reader name',
  `status` TINYINT NOT NULL DEFAULT 2 COMMENT '0:INACTIVE,2:ACTIVE',
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `operator_uid` BIGINT,
  `operate_time` DATETIME,
  
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;
