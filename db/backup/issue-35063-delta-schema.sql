-- 通用脚本
-- 通用表单打印表
CREATE TABLE `eh_general_form_print_templates`(
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0 COMMENT '域空间ID',
  `name` VARCHAR(64) NOT NULL COMMENT '表单打印模板名称',
  `last_commit` VARCHAR(40) COMMENT '最近一次提交ID',
  `owner_id` BIGINT NOT NULL DEFAULT 0 COMMENT '表单打印模板所属ID',
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT '表单打印所属类型',
  `status` TINYINT DEFAULT 2 COMMENT '打印模板状态,0为失效，2为生效',
  `creator_uid` BIGINT COMMENT 'record creator user id',
  `create_time` DATETIME COMMENT '创建时间',
  `delete_uid` BIGINT COMMENT 'record deleter user id',
  `delete_time` DATETIME COMMENT '删除时间',
  `update_uid` BIGINT COMMENT 'record update user id',
  `update_time` DATETIME COMMENT '更新时间',

  PRIMARY KEY (`id`)
)ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT '通用表单打印模板表';
