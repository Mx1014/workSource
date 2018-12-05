-- AUTHOR: 黄鹏宇 2018-11-6
-- REMARK: 创建 客户统计表，每日
CREATE TABLE `eh_customer_statistics_daily` (
 `id` BIGINT NOT NULL,
  `namespace_id` int COMMENT '域空间',
  `date_str` DATE COMMENT '统计的日期',
  `community_id` BIGINT COMMENT '统计字段锁管理的园区ID',
  `new_customer_num` INT COMMENT '新增客户增量',
  `registered_customer_num` INT COMMENT '成交客户增量',
  `loss_customer_num` INT COMMENT '流失客户增量',
  `history_customer_num` INT COMMENT '历史客户增量',
  `delete_customer_num` INT COMMENT '删除客户增量',
  `tracking_num` INT COMMENT '跟进次数增量',
  `customer_count` INT COMMENT '园区内的总客户数',
  `create_date` DATETIME COMMENT '创建日期',
  `create_uid` BIGINT COMMENT '创建人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT = '客户统计表，每日';

-- REMARK: 创建 客户统计表，每日，按管理公司汇总
CREATE TABLE `eh_customer_statistics_daily_total` (
 `id` BIGINT NOT NULL,
  `namespace_id` int COMMENT '域空间',
  `date_str` DATE COMMENT '统计的日期',
  `organization_id` BIGINT COMMENT '统计字段所在的管理公司ID',
  `community_num` INT COMMENT '管理公司下的园区数量',
  `new_customer_num` INT COMMENT '新增客户增量',
  `registered_customer_num` INT COMMENT '成交客户增量',
  `loss_customer_num` INT COMMENT '流失客户增量',
  `history_customer_num` INT COMMENT '历史客户增量',
  `delete_customer_num` INT COMMENT '删除客户增量',
  `tracking_num` INT COMMENT '跟进次数增量',
  `customer_count` INT COMMENT '园区内的总客户数',
  `create_date` DATETIME COMMENT '创建日期',
  `create_uid` BIGINT COMMENT '创建人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT = '客户统计表，每日，按管理公司汇总';

-- REMARK: 创建 客户统计表，每月
CREATE TABLE `eh_customer_statistics_monthly` (
  `id` BIGINT NOT NULL,
  `namespace_id` int COMMENT '域空间',
  `date_str` DATE COMMENT '统计的日期',
  `community_id` BIGINT COMMENT '统计字段锁管理的园区ID',
  `new_customer_num` INT COMMENT '新增客户增量',
  `registered_customer_num` INT COMMENT '成交客户增量',
  `loss_customer_num` INT COMMENT '流失客户增量',
  `history_customer_num` INT COMMENT '历史客户增量',
  `delete_customer_num` INT COMMENT '删除客户增量',
  `tracking_num` INT COMMENT '跟进次数增量',
  `customer_count` INT COMMENT '园区内的总客户数',
  `create_date` DATETIME COMMENT '创建日期',
  `create_uid` BIGINT COMMENT '创建人',

  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT = '客户统计表，每月';

-- REMARK: 创建 客户统计表，每月，按管理公司汇总
CREATE TABLE `eh_customer_statistics_monthly_total` (
 `id` BIGINT NOT NULL,
  `namespace_id` int COMMENT '域空间',
  `date_str` DATE COMMENT '统计的日期',
  `organization_id` BIGINT COMMENT '统计字段所在的管理公司ID',
  `community_num` INT COMMENT '管理公司下的园区数量',
  `new_customer_num` INT COMMENT '新增客户增量',
  `registered_customer_num` INT COMMENT '成交客户增量',
  `loss_customer_num` INT COMMENT '流失客户增量',
  `history_customer_num` INT COMMENT '历史客户增量',
  `delete_customer_num` INT COMMENT '删除客户增量',
  `tracking_num` INT COMMENT '跟进次数增量',
  `customer_count` INT COMMENT '园区内的总客户数',
  `create_date` DATETIME COMMENT '创建日期',
  `create_uid` BIGINT COMMENT '创建人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT = '客户统计表，每月，按管理公司汇总';

-- REMARK: 创建 客户状态改变记录表
CREATE TABLE `eh_customer_level_change_records` (
  `id` BIGINT NOT NULL,
  `customer_id` BIGINT COMMENT '被改变的用户id',
  `namespace_id` int COMMENT '域空间',
  `community_id` BIGINT COMMENT '所在园区',
  `change_date` datetime COMMENT '被改变状态的日期',
  `old_status` BIGINT COMMENT '被改变之前的状态',
  `new_status` BIGINT COMMENT '被改变之后的状态',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT = '客户状态改变记录表';


-- REMARK: 创建客户累积记录表
CREATE TABLE `eh_customer_statistics_total` (
  `id` BIGINT NOT NULL,
  `namespace_id` int COMMENT '域空间',
  `date_str` DATE COMMENT '统计的日期',
  `organization_id` BIGINT COMMENT '统计字段锁管理的园区ID',
  `community_num` INT COMMENT '管理公司下的园区数量',
  `new_customer_num` INT COMMENT '新增客户 数',
  `registered_customer_num` INT COMMENT '成交客户总数',
  `loss_customer_num` INT COMMENT '流失客户总数',
  `history_customer_num` INT COMMENT '历史客户总数',
  `delete_customer_num` INT COMMENT '删除客户总数',
  `tracking_num` INT COMMENT '跟进总次数',
  `customer_count` INT COMMENT '园区内的总客户数',
  `create_date` DATETIME COMMENT '创建日期',
  `create_uid` BIGINT COMMENT '创建人',

  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT = '客户统计表，每日累积';

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

-- AUTHOR: ������ 2018-12-03
-- REMARK: Ѷ��������תƥ���
CREATE TABLE `eh_xfyun_match` (
	`id` BIGINT(20) NOT NULL,
	`namespace_id` INT(11) NOT NULL DEFAULT '0',
	`vendor` VARCHAR(128) NOT NULL COMMENT '�����ṩ��',
	`service` VARCHAR(50) NOT NULL COMMENT '���ܱ�ʶ',
	`intent` VARCHAR(128) NOT NULL COMMENT '��ͼ',
	`description` VARCHAR(128) NULL DEFAULT NULL COMMENT '��������',
	`module_id` BIGINT(20) NULL DEFAULT NULL COMMENT '��Ӧ��ģ��id',
	`type` TINYINT(4) NOT NULL DEFAULT '0' COMMENT '0-��ת��Ӧ�� 1-�Զ�����ת',
	`default_router` VARCHAR(50) NULL DEFAULT NULL COMMENT 'typeΪ1ʱ����д��ת·��',
	`client_handler_type` TINYINT(4) NULL DEFAULT NULL COMMENT '0-ԭ�� 1-�ⲿ���� 2-�ڲ����� 3-���߰�',
	`access_control_type` TINYINT(4) NULL DEFAULT NULL COMMENT '0-ȫ�� 1-��¼�ɼ� 2-��֤�ɼ�',
	PRIMARY KEY (`id`)
)COMMENT='Ѷ��������תƥ���' COLLATE='utf8mb4_general_ci' ENGINE=InnoDB;

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
