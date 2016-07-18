-- category表增加description字段记录规范内容；增加分数字段记录规范所占分值。
ALTER TABLE `eh_quality_inspection_categories` ADD COLUMN `score` DOUBLE NOT NULL DEFAULT 0;
ALTER TABLE `eh_quality_inspection_categories` ADD COLUMN `description` TEXT COMMENT 'content data';
 
-- task表新增category_id字段记录任务所属类型；category_path字段记录类型路径；增加字段记录创建任务的人的id，0为根据标准自动创建的任务；增加manual_flag字段标识是否是自动生成的任务
ALTER TABLE `eh_quality_inspection_tasks` ADD COLUMN `category_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'refernece to the id of eh_categories';
ALTER TABLE `eh_quality_inspection_tasks` ADD COLUMN `category_path` VARCHAR(128) COMMENT 'refernece to the path of eh_categories';
ALTER TABLE `eh_quality_inspection_tasks` ADD COLUMN `create_uid` BIGINT NOT NULL DEFAULT 0;
ALTER TABLE `eh_quality_inspection_tasks` ADD COLUMN `manual_flag` BIGINT NOT NULL DEFAULT 0 COMMENT '0: auto 1:manual';
 
-- 新增表记录修改的记录
CREATE TABLE `eh_quality_inspection_logs` (
  `id` BIGINT NOT NULL COMMENT 'id',
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the log, enterprise, etc',
  `owner_id` BIGINT NOT NULL DEFAULT 0,
  `target_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'standard, etc',
  `target_id` BIGINT NOT NULL DEFAULT 0,
  `process_type` TINYINT NOT NULL DEFAULT 0 COMMENT '0: none, 1: insert, 2: update, 3: delete',
  `operator_uid` BIGINT NOT NULL DEFAULT 0 COMMENT 'record operator user id',
  `create_time` DATETIME,
  
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;