
-- AUTHOR: 黄明波 2018-12-03
-- REMARK: 讯飞语音跳转匹配表
CREATE TABLE `eh_xfyun_match` (
	`id` BIGINT(20) NOT NULL,
	`namespace_id` INT(11) NOT NULL DEFAULT '0',
	`vendor` VARCHAR(128) NOT NULL COMMENT '技能提供者',
	`service` VARCHAR(50) NOT NULL COMMENT '技能标识',
	`intent` VARCHAR(128) NOT NULL COMMENT '意图',
	`description` VARCHAR(128) NULL DEFAULT NULL COMMENT '中文描述',
	`module_id` BIGINT(20) NULL DEFAULT NULL COMMENT '对应的模块id',
	`type` TINYINT(4) NOT NULL DEFAULT '0' COMMENT '0-跳转到应用 1-自定义跳转',
	`default_router` VARCHAR(50) NULL DEFAULT NULL COMMENT 'type为1时，填写跳转路由',
	`client_handler_type` TINYINT(4) NULL DEFAULT NULL COMMENT '0-原生 1-外部链接 2-内部链接 3-离线包',
	`access_control_type` TINYINT(4) NULL DEFAULT NULL COMMENT '0-全部 1-登录可见 2-认证可见',
	PRIMARY KEY (`id`)
)
COMMENT='讯飞语音跳转匹配表'
COLLATE='utf8mb4_general_ci'
ENGINE=InnoDB
;
