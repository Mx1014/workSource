-- AUTHOR: 智伟
-- REMARK: 会议1.4
ALTER TABLE eh_meeting_reservations ADD COLUMN  `meeting_manager_user_id` BIGINT COMMENT '会议会务人user_id' AFTER `meeting_sponsor_name`;
ALTER TABLE eh_meeting_reservations ADD COLUMN  `meeting_manager_detail_id` BIGINT COMMENT '会议会务人detail_id' AFTER `meeting_manager_user_id`;
ALTER TABLE eh_meeting_reservations ADD COLUMN  `meeting_manager_name` VARCHAR(64) COMMENT '会议会务人的姓名' AFTER `meeting_manager_detail_id`;

CREATE TABLE `eh_meeting_templates` (
    `id` BIGINT NOT NULL COMMENT '主键',
    `namespace_id` INTEGER NOT NULL DEFAULT 0 COMMENT '域空间ID',
    `organization_id` BIGINT NOT NULL COMMENT '总公司ID',
    `user_id` BIGINT NOT NULL COMMENT '模板所有人uid',
    `detail_id` BIGINT NOT NULL COMMENT '模板所有人detailId',
    `subject` VARCHAR(256) COMMENT '会议主题模板',
    `content` TEXT COMMENT '会议详细内容模板',
    `meeting_manager_user_id` BIGINT COMMENT '会议会务人user_id'    `meeting_manager_detail_id` BIGINT COMMENT '会议会务人detail_id',
    `meeting_manager_name` VARCHAR(64) COMMENT '会议会务人的姓名',
    `attachment_flag` TINYINT DEFAULT 0 COMMENT '是否有附件 1-是 0-否',
    `creator_uid` BIGINT NOT NULL COMMENT '记录创建人userId',
    `create_time` DATETIME NOT NULL COMMENT '记录创建时间',
    `operate_time` DATETIME COMMENT '记录更新时间',
    `operator_uid` BIGINT COMMENT '记录更新人userId',
    PRIMARY KEY (`id`),
    KEY `i_eh_namespace_organization_user_id` (`namespace_id` , `organization_id` , `user_id`)
)  ENGINE=INNODB DEFAULT CHARSET=UTF8MB4 COMMENT='会议预约模板';


CREATE TABLE `eh_meeting_invitation_templates` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0 COMMENT '域空间ID',
  `organization_id` BIGINT NOT NULL COMMENT '总公司ID',
  `meeting_template_id` BIGINT NOT NULL COMMENT '会议预约模板的id',
  `source_type` VARCHAR(45) NOT NULL COMMENT '机构或者人：ORGANIZATION OR MEMBER_DETAIL',
  `source_id` BIGINT NOT NULL COMMENT '机构id或员工detail_id',
  `source_name` VARCHAR(64) NOT NULL COMMENT '机构名称或者员工的姓名',
  `creator_uid` BIGINT NOT NULL COMMENT '记录创建人userId',
  `create_time` DATETIME NOT NULL COMMENT '记录创建时间',
  `operate_time` DATETIME COMMENT '记录更新时间',
  `operator_uid` BIGINT COMMENT '记录更新人userId',
  PRIMARY KEY (`id`),
  KEY `i_eh_meeting_template_id` (`meeting_template_id`)
) ENGINE=INNODB DEFAULT CHARSET=UTF8MB4 COMMENT='会议预约参与人模板';




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
ENGINE=INNODB
;

-- AUTHOR:tangcen
-- REMARK:资产管理V3.6 房源日志表 2018年12月5日
CREATE TABLE `eh_address_events` (
  `id` BIGINT(20) NOT NULL,
  `namespace_id` INT(11) NOT NULL,
  `address_id` BIGINT(20) NOT NULL COMMENT '房源id',
  `operator_uid` BIGINT(20) COMMENT '操作人id',
  `operate_time` DATETIME ON UPDATE CURRENT_TIMESTAMP COMMENT '操作时间',
  `operate_type` TINYINT(4) COMMENT '操作类型（1：增加，2：删，3：修改）',
  `content` TEXT COMMENT '日志内容',
  `status` TINYINT(4) COMMENT '状态（0：无效，1：待确认，2：生效）',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='房源日志表';
