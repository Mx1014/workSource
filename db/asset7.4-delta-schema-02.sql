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
	`create_time` DATETIME DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
	`update_time` DATETIME DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
	PRIMARY KEY (`id`)
) ENGINE = INNODB DEFAULT CHARSET = utf8mb4 COMMENT = '项目合同报表结果集（项目-月份）';

-- AUTHOR: 莫谋斌
-- REMARK: eh_communities增加字段；
ALTER TABLE eh_communities ADD COLUMN background_img_url VARCHAR(500) DEFAULT '' COMMENT '小区或园区项目的图片链接';

-- AUTHOR: tangcen
-- REMARK: 增加楼宇资产管理统计字段
ALTER TABLE eh_property_statistic_community ADD COLUMN signedup_apartment_count int(11) DEFAULT 0 COMMENT '园区下的待签约房源数' AFTER saled_apartment_count;
ALTER TABLE eh_property_statistic_community ADD COLUMN waitingroom_apartment_count int(11) DEFAULT 0 COMMENT '园区下的待接房房源数' AFTER signedup_apartment_count;
ALTER TABLE eh_property_statistic_building ADD COLUMN signedup_apartment_count int(11) DEFAULT 0 COMMENT '楼宇内的待签约房源数' AFTER saled_apartment_count;
ALTER TABLE eh_property_statistic_building ADD COLUMN waitingroom_apartment_count int(11) DEFAULT 0 COMMENT '楼宇内的待接房房源数' AFTER signedup_apartment_count;

-- 企业福利1.0 建表sql
CREATE TABLE `eh_welfares` (
  `id` BIGINT NOT NULL COMMENT 'id',
  `namespace_id` INTEGER ,
  `organization_id` BIGINT DEFAULT NULL COMMENT 'organization id',
  `subject` VARCHAR(128) COMMENT '主题名称',
  `content` TEXT COMMENT '祝福语',
  `sender_name` VARCHAR(128) COMMENT '发放人姓名',
  `sender_uid` BIGINT COMMENT '发放人userId',
  `sender_detail_id` BIGINT COMMENT '发放人detailId',
  `img_uri` VARCHAR(1024) COMMENT '附图uri',
  `status` TINYINT COMMENT '0-草稿 1-发送',
  `welfare_type` TINYINT COMMENT '1.0暂时只支持发放一种福利,福利主表加入福利类型 0-卡券 1-积分',
  `is_delete` TINYINT DEFAULT 0 COMMENT '0-没删除 1-删除',
  `send_time` DATETIME DEFAULT NULL,
  `coupon_orders` TEXT COMMENT '卡券系统的交易id 列表',
  `point_orders` TEXT COMMENT '卡券系统的交易id 列表',
  `creator_name` VARCHAR(128)  DEFAULT NULL,
  `creator_uid` BIGINT DEFAULT NULL,
  `create_time` DATETIME DEFAULT NULL,
  `operator_name` VARCHAR(128)  DEFAULT NULL,
  `operator_uid` BIGINT DEFAULT NULL,
  `update_time` DATETIME DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT '企业福利表';

CREATE TABLE `eh_welfare_receivers` (
  `id` BIGINT NOT NULL COMMENT 'id',
  `namespace_id` INTEGER ,
  `organization_id` BIGINT DEFAULT NULL COMMENT 'organization id',
  `welfare_id` BIGINT NOT NULL COMMENT '福利id',
  `receiver_uid` BIGINT COMMENT '接收者userId(必有项目)',
  `receiver_name` VARCHAR(128) COMMENT '接收者姓名',
  `receiver_detail_id` BIGINT COMMENT '接收者detailId',
  `status` TINYINT COMMENT '0-未接收 1-已接受',
  `update_time` DATETIME DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY i_receiver_welfare_id (`welfare_id`),
  KEY i_receiver_user_id (`organization_id`,`receiver_uid`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT '企业福利接收人表';

CREATE TABLE `eh_welfare_coupons` (
  `id` BIGINT NOT NULL COMMENT 'id',
  `namespace_id` INTEGER ,
  `organization_id` BIGINT DEFAULT NULL COMMENT 'organization id',
  `welfare_id` BIGINT NOT NULL COMMENT '福利id',
  `coupon_id` BIGINT COMMENT '卡券的id',
  `coupon_type` VARCHAR(128) COMMENT '卡券类型',
  `coupon_name` VARCHAR(128) COMMENT '卡券名称',
  `denomination` VARCHAR(128) COMMENT '卡券面额',
  `sub_type` VARCHAR(128) COMMENT '购物卡类别，目前仅用于购物卡， 1-小时卡、2-金额卡、3-次数卡',
  `service_supply_name` VARCHAR(128) COMMENT '适用地点',
  `service_range` VARCHAR(128) COMMENT '适用范围',
  `consumption_limit` VARCHAR(128) COMMENT '满多少可以用',
  `valid_date_type` TINYINT COMMENT '截止日期类型',
  `valid_date` DATE COMMENT '截止日期',
  `begin_date` DATE COMMENT '开始日期',
  `amount` INTEGER COMMENT '发放数量(每人)',
  PRIMARY KEY (`id`),
  KEY i_coupon_welfare_id (`welfare_id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT '企业福利卡券表';

CREATE TABLE `eh_welfare_points` (
  `id` BIGINT NOT NULL COMMENT 'id',
  `namespace_id` INTEGER ,
  `organization_id` BIGINT DEFAULT NULL COMMENT 'organization id',
  `welfare_id` BIGINT NOT NULL COMMENT '福利id',
  `point_id` BIGINT COMMENT '积分的id',
  `point_name` VARCHAR(128) COMMENT '积分名称',
  `coupon_type` VARCHAR(128) COMMENT '卡券类型',
  `point_content` VARCHAR(128) COMMENT '积分内容字段',
  `valid_date` DATE COMMENT '截止日期',
  `begin_date` DATE COMMENT '开始日期',
  `amount` VARCHAR(128) COMMENT '发放数量(每人)',
  PRIMARY KEY (`id`),
  KEY i_point_welfare_id (`welfare_id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT '企业福利积分表';

-- AUTHOR: 梁燕龙 20181115
-- 工作台公司推荐应用
CREATE TABLE `eh_work_platform_apps` (
  `id` BIGINT NOT NULL,
  `app_id` BIGINT NOT NULL,
  `scope_type` TINYINT COMMENT '范围，1-园区，4-公司',
  `scope_id` BIGINT COMMENT '范围对象id',
  `order` INTEGER,
  `visible_flag` TINYINT COMMENT '可见性，0:不可见，1:可见',
  `entry_id` BIGINT COMMENT '应用入口id',
  PRIMARY KEY (`id`),
  KEY `u_eh_recommend_app_scope_id` (`scope_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='工作台公司推荐应用';

CREATE TABLE `eh_service_module_app_entry_profiles` (
  `id` BIGINT NOT NULL,
  `origin_id` BIGINT NOT NULL ,
  `entry_id` BIGINT NOT NULL COMMENT '入口ID',
  `entry_category` TINYINT COMMENT '入口类型,1:移动端工作台,2：移动端服务广场,3:PC端运营后台,4:PC端工作台,5:PC门户',
  `entry_name` VARCHAR(128) COMMENT '入口名称',
  `entry_uri` VARCHAR(1024) COMMENT '入口ICON uri',
  `app_entry_setting` TINYINT COMMENT '入口自定义配置开启，0:不开启，1:开启',

  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='应用自定义配置应用入口信息';

CREATE TABLE `eh_service_module_app_entries` (
  `id` BIGINT NOT NULL,
  `app_id` BIGINT NOT NULL,
  `app_name` VARCHAR(256),
  `entry_name` VARCHAR(256),
  `terminal_type` TINYINT NOT NULL COMMENT '终端列表，1-mobile,2-pc',
  `location_type` TINYINT NOT NULL COMMENT '位置，参考枚举ServiceModuleLocationType',
  `scene_type` TINYINT NOT NULL COMMENT '形态，1-管理端，2-客户端，参考枚举ServiceModuleSceneType',
  `app_category_id` BIGINT NOT NULL DEFAULT 0,
  `default_order` INTEGER NOT NULL DEFAULT 0,
  `icon_uri` VARCHAR(255),
  PRIMARY KEY (`id`)

) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT '应用的入口数据';

-- AUTHOR: mengqianxiang
-- REMARK: 增加eh_payment_exemption_items表的状态字段
ALTER TABLE eh_payment_exemption_items ADD `merchant_order_id` BIGINT COMMENT "统一订单标识ID";
ALTER TABLE eh_payment_exemption_items ADD `delete_flag` TINYINT COMMENT "删除状态：0：已删除；1：正常使用";
