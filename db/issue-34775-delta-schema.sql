 
-- AUTHOR: 吴寒
-- REMARK: 打卡考勤V8.2 - 支持人脸识别关联考勤；支持自动打卡
ALTER TABLE `eh_punch_logs` ADD COLUMN `create_type` TINYINT NOT NULL DEFAULT 0 COMMENT '创建类型 : 0-正常打卡创建 1-自动打卡创建 2-人脸识别打卡创建 4-其他第三方接口创建(通过第三方接口打卡没有带创建类型)' ;

-- 外出打卡表
CREATE TABLE `eh_punch_go_out_logs` (
  `id` BIGINT NOT NULL COMMENT 'id',
  `user_id` BIGINT DEFAULT NULL COMMENT 'user''s id',
  `detail_id` BIGINT DEFAULT NULL COMMENT 'eh_organization_member_details id',
  `organization_id` BIGINT DEFAULT NULL COMMENT 'organization id',
  `namespace_id` INT DEFAULT NULL COMMENT 'NAMESPACE id',
  `longitude` DOUBLE DEFAULT NULL,
  `latitude` DOUBLE DEFAULT NULL,
  `location_info` VARCHAR(1024) DEFAULT NULL COMMENT '打卡用到的地址定位',
  `wifi_info` VARCHAR(1024) DEFAULT NULL COMMENT '打卡用到的WiFi信息(暂时无用)',
  `img_uri` VARCHAR(2048) DEFAULT NULL COMMENT '打卡上传图片uri地址',
  `description` VARCHAR(1024) DEFAULT NULL COMMENT '备注',
  `punch_date` DATE DEFAULT NULL COMMENT 'user punch date',
  `punch_time` DATETIME DEFAULT NULL COMMENT '打卡时间',
  `identification` VARCHAR(255) DEFAULT NULL COMMENT 'unique identification for a phone',
  `status` TINYINT(4) DEFAULT NULL COMMENT '打卡状态 0-正常 1-迟到 2-早退 3-缺勤 14-缺卡',
  `update_date` DATE DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `i_eh_user_id` (`user_id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

ALTER TABLE `eh_punch_day_logs` ADD COLUMN `go_out_punch_flag` TINYINT DEFAULT 0 COMMENT '是否外出打卡，1：是 0：否' AFTER `normal_flag`;
ALTER TABLE `eh_punch_statistics` ADD COLUMN `go_out_punch_day_count` INT DEFAULT 0 COMMENT '当月外出打卡天数' AFTER `rest_day_count`;
