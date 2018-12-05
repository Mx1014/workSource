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

-- AUTHOR: 黄鹏宇 2018-11-6
-- REMARK: 创建 客户统计表，每日
CREATE TABLE `eh_customer_statistics_daily` (
 `id` BIGINT NOT NULL,
  `namespace_id` INT COMMENT '域空间',
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
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT = '客户统计表，每日';

-- REMARK: 创建 客户统计表，每日，按管理公司汇总
CREATE TABLE `eh_customer_statistics_daily_total` (
 `id` BIGINT NOT NULL,
  `namespace_id` INT COMMENT '域空间',
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
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT = '客户统计表，每日，按管理公司汇总';

-- REMARK: 创建 客户统计表，每月
CREATE TABLE `eh_customer_statistics_monthly` (
  `id` BIGINT NOT NULL,
  `namespace_id` INT COMMENT '域空间',
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
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT = '客户统计表，每月';

-- REMARK: 创建 客户统计表，每月，按管理公司汇总
CREATE TABLE `eh_customer_statistics_monthly_total` (
 `id` BIGINT NOT NULL,
  `namespace_id` INT COMMENT '域空间',
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
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT = '客户统计表，每月，按管理公司汇总';

-- REMARK: 创建 客户状态改变记录表
CREATE TABLE `eh_customer_level_change_records` (
  `id` BIGINT NOT NULL,
  `customer_id` BIGINT COMMENT '被改变的用户id',
  `namespace_id` INT COMMENT '域空间',
  `community_id` BIGINT COMMENT '所在园区',
  `change_date` DATETIME COMMENT '被改变状态的日期',
  `old_status` BIGINT COMMENT '被改变之前的状态',
  `new_status` BIGINT COMMENT '被改变之后的状态',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT = '客户状态改变记录表';


-- REMARK: 创建客户累积记录表
CREATE TABLE `eh_customer_statistics_total` (
  `id` BIGINT NOT NULL,
  `namespace_id` INT COMMENT '域空间',
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
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT = '客户统计表，每日累积';

-- AUTHOR: djm
-- 合同报表数据表 园区维度的 每个园区的数据信息
CREATE TABLE `eh_contract_statistic_communities` (
	`id` BIGINT (20) NOT NULL,
	`namespace_id` INT DEFAULT NULL,
	`community_id` BIGINT (20) DEFAULT NULL,
	`date_str` VARCHAR (12) DEFAULT NULL COMMENT '统计月份（格式为xxxx-xx）',
	`date_type` TINYINT (4) DEFAULT '2' COMMENT '统计时间类型： 1-统计月份（格式为xxxx-xx）, 2-统计年份（格式为xxxx）',
	`rent_amount` DECIMAL (12, 2) DEFAULT '0.00' COMMENT '每个园区的合同总额',
	`rental_area` DECIMAL (12, 2) DEFAULT '0.00' COMMENT '每个园区的合同租赁总面积',
	`contract_count` INT DEFAULT '0' COMMENT '一个园区在租合同总份数',
	`customer_count` INT DEFAULT '0' COMMENT '新签客户总数',
	`org_contract_count` INT DEFAULT '0' COMMENT '企业合同总数',
	`org_contract_amount` DECIMAL (12, 2) DEFAULT '0.00' COMMENT '企业合同总额',
	`user_contract_count` INT DEFAULT '0' COMMENT '个人合同总数',
	`user_contract_amount` DECIMAL (12, 2) DEFAULT '0.00' COMMENT '个人合同总额',
	`new_contract_count` INT DEFAULT '0' COMMENT '新签合同总数',
	`new_contract_amount` DECIMAL (12, 2) DEFAULT '0.00' COMMENT '新签合同总额',
	`new_contract_area` DECIMAL (12, 2) DEFAULT '0.00' COMMENT '每个园区新签合同租赁总面积',
	`denunciation_contract_count` INT DEFAULT '0' COMMENT '退约合同总数',
	`denunciation_contract_amount` DECIMAL (12, 2) DEFAULT '0.00' COMMENT '退约合同总额',
	`denunciation_contract_area` DECIMAL (12, 2) DEFAULT '0.00' COMMENT '每个园区续约合同租赁总面积',
	`change_contract_count` INT DEFAULT '0' COMMENT '变更合同总数',
	`change_contract_amount` DECIMAL (12, 2) DEFAULT '0.00' COMMENT '变更合同总额',
	`change_contract_area` DECIMAL (12, 2) DEFAULT '0.00' COMMENT '每个园区变更合同租赁总面积',
	`renew_contract_count` INT DEFAULT '0' COMMENT '续约合同总数',
	`renew_contract_amount` DECIMAL (12, 2) DEFAULT '0.00' COMMENT '续约合同总额',
	`renew_contract_area` DECIMAL (12, 2) DEFAULT '0.00' COMMENT '每个园区续约合同租赁总面积',
	`deposit_amount` DECIMAL (12, 2) DEFAULT '0.00' COMMENT '新增押金总额',
	`status` TINYINT (4) DEFAULT '2' COMMENT '该条的记录状态：0-inactive, 1-confirming, 2-active',
	`create_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
	`update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
	PRIMARY KEY (`id`)
) ENGINE = INNODB DEFAULT CHARSET = utf8mb4 COMMENT = '项目合同报表结果集（项目-月份）';
