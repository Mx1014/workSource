 ALTER TABLE `eh_punch_time_rules` ADD COLUMN `description` VARCHAR(128) DEFAULT NULL COMMENT 'rule description';
 
 --  打卡排班表
 CREATE TABLE `eh_punch_scheduling` (
  `id` BIGINT NOT NULL COMMENT 'id',
  `owner_type` VARCHAR(128) DEFAULT NULL COMMENT 'owner resource(user/organization) type',
  `owner_id` BIGINT DEFAULT NULL COMMENT 'owner resource(user/organization) id',
  `target_type` VARCHAR(128) DEFAULT NULL COMMENT 'target resource(user/organization) type',
  `target_id` BIGINT DEFAULT NULL COMMENT 'target resource(user/organization) id',
  `date_status` TINYINT DEFAULT NULL COMMENT '0:weekend work date, 1:holiday',
  `rule_date` DATE DEFAULT NULL COMMENT 'date',
  `punch_rule_id` BIGINT COMMENT 'eh_punch_rules id --not necessary',
  `time_rule_id` BIGINT COMMENT 'eh_punch_time_rules id --null:not work day',
  `creator_uid` BIGINT DEFAULT NULL,
  `create_time` DATETIME DEFAULT NULL,
  `operator_uid` BIGINT DEFAULT NULL,
  `operate_time` DATETIME DEFAULT NULL, 
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4

-- 增加字段
ALTER TABLE `eh_punch_statistics` ADD COLUMN `exts` VARCHAR(128) DEFAULT NULL COMMENT 'json string exts:eq[{"name":"事假","timeCount":"1天2小时"},{"name":"丧假","timeCount":"3天2小时30分钟"}]';
  
  ;
  