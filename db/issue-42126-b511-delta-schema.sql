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
    `meeting_manager_user_id` BIGINT COMMENT '会议会务人user_id',
    `meeting_manager_detail_id` BIGINT COMMENT '会议会务人detail_id',
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
  `source_type` VARCHAR(45) NOT NULL COMMENT '机构或者个人：ORGANIZATION OR MEMBER_DETAIL',
  `source_id` BIGINT NOT NULL COMMENT '机构id或员工detail_id',
  `source_name` VARCHAR(64) NOT NULL COMMENT '机构名称或者员工的姓名',
  `creator_uid` BIGINT NOT NULL COMMENT '记录创建人userId',
  `create_time` DATETIME NOT NULL COMMENT '记录创建时间',
  `operate_time` DATETIME COMMENT '记录更新时间',
  `operator_uid` BIGINT COMMENT '记录更新人userId',
  PRIMARY KEY (`id`),
  KEY `i_eh_meeting_template_id` (`meeting_template_id`)
) ENGINE=INNODB DEFAULT CHARSET=UTF8MB4 COMMENT='会议预约参与人模板';