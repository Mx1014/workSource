ALTER TABLE eh_meeting_reservations ADD COLUMN  `meeting_manager_user_id` BIGINT COMMENT '���������user_id' AFTER `meeting_sponsor_name`;
ALTER TABLE eh_meeting_reservations ADD COLUMN  `meeting_manager_detail_id` BIGINT COMMENT '���������detail_id' AFTER `meeting_manager_user_id`;
ALTER TABLE eh_meeting_reservations ADD COLUMN  `meeting_manager_name` VARCHAR(64) COMMENT '��������˵�����' AFTER `meeting_manager_detail_id`;

CREATE TABLE `eh_meeting_templates` (
    `id` BIGINT NOT NULL COMMENT '����',
    `namespace_id` INTEGER NOT NULL DEFAULT 0 COMMENT '��ռ�ID',
    `organization_id` BIGINT NOT NULL COMMENT '�ܹ�˾ID',
    `user_id` BIGINT NOT NULL COMMENT 'ģ��������uid',
    `detail_id` BIGINT NOT NULL COMMENT 'ģ��������detailId',
    `subject` VARCHAR(256) COMMENT '��������ģ��',
    `content` TEXT COMMENT '������ϸ����ģ��',
    `meeting_manager_user_id` BIGINT COMMENT '���������user_id',
    `meeting_manager_detail_id` BIGINT COMMENT '���������detail_id',
    `meeting_manager_name` VARCHAR(64) COMMENT '��������˵�����',
    `attachment_flag` TINYINT DEFAULT 0 COMMENT '�Ƿ��и��� 1-�� 0-��',
    `creator_uid` BIGINT NOT NULL COMMENT '��¼������userId',
    `create_time` DATETIME NOT NULL COMMENT '��¼����ʱ��',
    `operate_time` DATETIME COMMENT '��¼����ʱ��',
    `operator_uid` BIGINT COMMENT '��¼������userId',
    PRIMARY KEY (`id`),
    KEY `i_eh_namespace_organization_user_id` (`namespace_id` , `organization_id` , `user_id`)
)  ENGINE=INNODB DEFAULT CHARSET=UTF8MB4 COMMENT='����ԤԼģ��';


CREATE TABLE `eh_meeting_invitation_templates` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0 COMMENT '��ռ�ID',
  `organization_id` BIGINT NOT NULL COMMENT '�ܹ�˾ID',
  `meeting_template_id` BIGINT NOT NULL COMMENT '����ԤԼģ���id',
  `source_type` VARCHAR(45) NOT NULL COMMENT '�������߸��ˣ�ORGANIZATION OR MEMBER_DETAIL',
  `source_id` BIGINT NOT NULL COMMENT '����id��Ա��detail_id',
  `source_name` VARCHAR(64) NOT NULL COMMENT '�������ƻ���Ա��������',
  `creator_uid` BIGINT NOT NULL COMMENT '��¼������userId',
  `create_time` DATETIME NOT NULL COMMENT '��¼����ʱ��',
  `operate_time` DATETIME COMMENT '��¼����ʱ��',
  `operator_uid` BIGINT COMMENT '��¼������userId',
  PRIMARY KEY (`id`),
  KEY `i_eh_meeting_template_id` (`meeting_template_id`)
) ENGINE=INNODB DEFAULT CHARSET=UTF8MB4 COMMENT='����ԤԼ������ģ��';

-- AUTHOR:tangcen
-- REMARK:资产管理V3.6 房源日志表 2018年12月5日
CREATE TABLE `eh_address_events` (
  `id` bigint(20) NOT NULL,
  `namespace_id` int(11) NOT NULL,
  `address_id` bigint(20) NOT NULL COMMENT '房源id',
  `operator_uid` bigint(20) COMMENT '操作人id',
  `operate_time` datetime ON UPDATE CURRENT_TIMESTAMP COMMENT '操作时间',
  `operate_type` tinyint(4) COMMENT '操作类型（1：增加，2：删除，3：修改）',
  `content` text COMMENT '日志内容',
  `status` tinyint(4) COMMENT '状态（0：无效，1：待确认，2：生效）',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='房源日志表';
