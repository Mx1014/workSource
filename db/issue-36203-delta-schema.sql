-- AUTHOR: 梁燕龙
-- REMARK: 个人中心v3.0
-- 用户表增加showCompanyFlag字段
ALTER TABLE `eh_users` ADD COLUMN `show_company_flag` TINYINT COMMENT '是否展示公司名称';

-- 个人中心配置表
CREATE TABLE `eh_personal_center_settings` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER,
  `name` VARCHAR(32) COMMENT '展示名称',
  `function_name` VARCHAR(32) COMMENT '功能名称',
  `region` TINYINT NOT NULL DEFAULT 0 COMMENT '个人中心展示区域',
  `group_type` TINYINT COMMENT '展示区域分组',
  `sort_num` INTEGER NOT NULL DEFAULT 0 COMMENT '展示顺序',
  `showable` TINYINT COMMENT '是否展示',
  `editable` TINYINT COMMENT '是否可编辑',
  `router_url` VARCHAR(64) COMMENT '跳转路由',
  `icon_uri` VARCHAR(64) COMMENT '图标URI',
  `version` BIGINT NOT NULL DEFAULT 0 COMMENT '版本号',
  `status` TINYINT NOT NULL DEFAULT 0 COMMENT '状态',
  `create_uid` BIGINT COMMENT '创建人ID',
  `create_time` TIMESTAMP COMMENT '创建时间',
  `update_uid` BIGINT COMMENT '修改人ID',
  `update_time` TIMESTAMP COMMENT '修改时间',

  PRIMARY KEY (id)
)ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT '个人中心配置表';
-- END