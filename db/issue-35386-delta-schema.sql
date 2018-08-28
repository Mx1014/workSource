CREATE TABLE `eh_zhenzhihui_user_info` (
  `id` BIGINT NOT NULL,
  `name` VARCHAR (62) NOT NULL DEFAULT '' COMMENT '姓名',
  `identify_token` VARCHAR(64) NOT NULL DEFAULT '' COMMENT '证件号码',
  `identify_type` INTEGER NOT NULL DEFAULT 10 COMMENT '证件类型',
  `user_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'id of eh_users',
  `email` VARCHAR(64) NOT NULL DEFAULT '""' COMMENT '邮箱',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='圳智慧所需用户信息。';

CREATE TABLE `eh_zhenzhihui_enterprise_info` (
  `id` BIGINT NOT NULL,
  `enterprise_name` VARCHAR(64) DEFAULT '' COMMENT '单位名称',
  `enterprise_token` VARCHAR(64) DEFAULT '' COMMENT '单位证件号码',
  `enterprise_type`  INTEGER NOT NULL DEFAULT 10 COMMENT '单位证件类型',
  `corporation_name` VARCHAR(32) DEFAULT '' COMMENT '法人名称',
  `identify_token` VARCHAR(64) NOT NULL DEFAULT '' COMMENT '法人证件号码',
  `identify_type` INTEGER NOT NULL DEFAULT 10 COMMENT '法人证件类型',
  `user_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'id of eh_users',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='圳智慧所需企业信息。';