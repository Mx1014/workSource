-- AUTHOR: 梁燕龙  20181009
-- REMARK: 协议管理
CREATE TABLE `eh_protocol_templates`(
  `id` BIGINT NOT NULL,
  `status` TINYINT NOT NULL COMMENT '状态：1为失效，2为生效',
  `content` MEDIUMTEXT COMMENT '文本',
  `type` TINYINT COMMENT '模板类型，1为服务协议，2为隐私协议',
  `creator_uid` BIGINT NOT NULL,
  `create_time` DATETIME NOT NULL,

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT '协议模板表';

CREATE TABLE `eh_protocol_template_variables`(
  `id` BIGINT NOT NULL,
  `owner_id` BIGINT NOT NULL COMMENT '模板ID',
  `name` VARCHAR(64) COMMENT '变量名称',
  `value` VARCHAR(128) COMMENT '变量值',
  `type` TINYINT COMMENT '变量类型，1为固定变量，2为自定义变量',

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT '协议模板变量表';

CREATE TABLE `eh_protocols`(
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `status` TINYINT NOT NULL COMMENT '状态：1为失效，2为生效',
  `content` MEDIUMTEXT COMMENT '文本',
  `type` TINYINT COMMENT '协议类型，1为服务协议，2为隐私协议',
  `creator_uid` BIGINT NOT NULL,
  `create_time` DATETIME NOT NULL,

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT '协议详情表';

CREATE TABLE `eh_protocol_variables`(
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `owner_id` BIGINT NOT NULL COMMENT '模板ID',
  `name` VARCHAR(64) COMMENT '变量名称',
  `value` VARCHAR(128) COMMENT '变量值',
  `type` TINYINT COMMENT '变量类型，1为固定变量，2为自定义变量',

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT '协议详情变量表';