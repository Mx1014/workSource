-- AUTHOR: 梁燕龙
-- REMARK: 用户认证审核权限配置表
CREATE TABLE `eh_user_authentication_organizations`(
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL  COMMENT '域空间ID',
  `community_id` BIGINT NOT NULL COMMENT '项目ID',
  `organization_id` BIGINT NOT NULL COMMENT '企业ID',
  `auth_flag` TINYINT NOT NULL COMMENT '是否授权，0不授权，1授权',
  `status` TINYINT NOT NULL COMMENT '状态, 1无效，2生效',
  `creator_uid` BIGINT COMMENT 'creator uid',
  `create_time` DATETIME COMMENT 'record create time',

  PRIMARY KEY (`id`)
)ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT '用户认证审核权限配置表';