

-- 考勤月报表
CREATE TABLE `eh_punch_month_reports` (
  `id` BIGINT NOT NULL COMMENT 'id',
  `punch_month` VARCHAR (8) DEFAULT NULL COMMENT 'yyyymm',
  `owner_type` VARCHAR (128) DEFAULT NULL COMMENT 'owner resource(user/organization) type',
  `owner_id` BIGINT DEFAULT NULL COMMENT 'owner resource(user/organization) id',
  `process` INT COMMENT '进度百分比',
  `error_info` VARCHAR (200) COMMENT '错误信息(如果归档错误)',
  `status` TINYINT COMMENT '状态:0-创建更新中 1-创建完成 2-已归档',
  `creator_uid` BIGINT DEFAULT NULL COMMENT '创建者',
  `create_time` DATETIME DEFAULT NULL,
  `filer_uid` BIGINT DEFAULT NULL COMMENT '创建者',
  `file_time` DATETIME DEFAULT NULL,
  `creator_Name` VARCHAR (128) DEFAULT NULL COMMENT '创建者姓名',
  `filer_Name` VARCHAR (128) DEFAULT NULL COMMENT '归档者姓名',
  PRIMARY KEY (`id`)
) ENGINE = INNODB DEFAULT CHARSET = utf8mb4 COMMENT '考勤月报表' ;

