-- 通用脚本
-- add by 梁燕龙
-- 报名表增加表单ID字段
ALTER TABLE `eh_activity_roster` ADD COLUMN `form_id` BIGINT COMMENT '表单ID';
-- 表单与项目关联表
CREATE TABLE `eh_community_general_form`(
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL COMMENT '域空间ID',
  `community_id` BIGINT COMMENT '项目ID',
  `form_origin_id` BIGINT COMMENT '表单formOriginID',
  `type` VARCHAR(32) COMMENT '类型',
  PRIMARY KEY (`id`)
)ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT '表单与项目关系表';