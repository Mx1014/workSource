-- AUTHOR: 吴寒
-- REMARK: 福利字段扩容
ALTER TABLE eh_welfare_coupons CHANGE `service_supply_name` `service_supply_name` VARCHAR(4096) COMMENT '适用地点';
ALTER TABLE eh_welfare_coupons CHANGE `service_range` `service_range` VARCHAR(4096) COMMENT '适用范围';
-- AUTHOR: 马世亨 20181122
-- REMARK: 访客1.4 到访类型修改
ALTER TABLE `eh_visitor_sys_visit_reason` ADD COLUMN `reason_type`  tinyint NULL DEFAULT null COMMENT '类型 0为住宅小区,1为商业小区';
ALTER TABLE `eh_visitor_sys_visit_reason` ADD COLUMN `visit_reason_code`  tinyint NULL DEFAULT null COMMENT '到访原因类型码';

-- AUTHOR: 马世亨 20181122
-- REMARK: 访客1.4 添加访客园区类型字段与园区Id
ALTER TABLE `eh_visitor_sys_visitors` ADD COLUMN `community_type`  tinyint NULL DEFAULT NULL COMMENT '园区类型 0为住宅小区,1为商业小区';
ALTER TABLE `eh_visitor_sys_visitors` ADD COLUMN `community_id`  bigint NULL DEFAULT NULL COMMENT '园区Id';


-- AUTHOR: 马世亨 20181122
-- REMARK: 访客1.4 第三方对接映射表
CREATE TABLE `eh_visitor_sys_third_mapping` (
    `id` BIGINT NOT NULL COMMENT '主键',
    `namespace_id` INT (11) NOT NULL DEFAULT '0' COMMENT 'namespace id',
    `owner_type` VARCHAR (64) NOT NULL COMMENT 'community or organization',
    `owner_id` BIGINT (20) NOT NULL DEFAULT '0' COMMENT 'ownerType为community时候，为园区id;ownerType为organization时候，为公司id',
    `visitor_id` BIGINT NOT NULL COMMENT '主键',
    `third_type` VARCHAR (128) NULL COMMENT '关联类型',
    `third_value` VARCHAR (128) NULL COMMENT '关联值',
    `creator_uid` BIGINT (20) DEFAULT NULL,
    `create_time` datetime DEFAULT NULL,
    `operator_uid` BIGINT (20) DEFAULT NULL,
    `operate_time` datetime DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE = INNODB DEFAULT CHARSET = utf8mb4 COMMENT = '访客管理对接映射表';

-- AUTHOR: 马世亨 20181122
-- REMARK: 访客1.4 访客管理对接海康威视人员表
CREATE TABLE `eh_visitor_sys_hkws_user` (
`person_id` int NULL DEFAULT NULL COMMENT '人员ID',
`person_no` varchar(256) NULL DEFAULT NULL COMMENT '人员编号',
`person_name` varchar(256) NULL DEFAULT NULL COMMENT '姓名',
`gender` tinyint NULL DEFAULT NULL COMMENT '性别',
`certificate_type` int NULL DEFAULT NULL COMMENT '证件类型',
`certificate_no` varchar(256) NULL DEFAULT NULL COMMENT '证件号码',
`birthday` bigint NULL DEFAULT NULL COMMENT '出生日期',
`person_pinyin` varchar(256) NULL DEFAULT NULL COMMENT '姓名拼音',
`phone_no` varchar(256) NULL DEFAULT NULL COMMENT '联系电话',
`address` varchar(256) NULL DEFAULT NULL COMMENT '联系地址',
`photo` varchar(256) NULL DEFAULT NULL COMMENT '免冠照',
`english_name` varchar(256) NULL DEFAULT NULL COMMENT '英文名',
`email` varchar(256) NULL DEFAULT NULL COMMENT '邮箱',
`entry_date` bigint NULL DEFAULT NULL COMMENT '入职日期',
`leave_date` bigint NULL DEFAULT NULL COMMENT '离职日期',
`education` varchar(256) NULL DEFAULT NULL COMMENT '学历',
`nation` varchar(256) NULL DEFAULT NULL COMMENT '民族',
`dept_uuid` varchar(256) NULL DEFAULT NULL COMMENT '所属部门UUID',
`dept_no` varchar(256) NULL DEFAULT NULL COMMENT '所属部门编号',
`dept_name` varchar(256) NULL DEFAULT NULL COMMENT '所属部门名称',
`dept_path` varchar(256) NULL DEFAULT NULL COMMENT '所属部门路径',
`status` tinyint NULL DEFAULT NULL COMMENT '人员状态',
`identity_uuid` varchar(256) NULL DEFAULT NULL COMMENT '身份UUID',
`identity_name` varchar(256) NULL DEFAULT NULL COMMENT '身份名称',
`create_time` bigint NULL DEFAULT NULL COMMENT '创建时间',
`update_time` bigint NULL DEFAULT NULL COMMENT '更新时间',
`remark` varchar(256) NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`person_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='访客管理对接海康威视人员表';

-- AUTHOR: 缪洲 20181211
-- REMARK: 工位预定V2.4
CREATE TABLE `eh_office_cubicle_station` (
  `id` BIGINT NOT NULL DEFAULT 0 COMMENT 'id',
  `namespace_id` INTEGER NOT NULL,
  `owner_type`  VARCHAR(255) COMMENT 'owner type: community',
  `owner_id` BIGINT COMMENT '所属的社区ID（和可见范围的不一样）',
  `space_id` BIGINT COMMENT '空间ID',
  `station_name` VARCHAR(127) COMMENT '名称：',
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `operator_uid` BIGINT,
  `operate_time` DATETIME,
  `description` TEXT COMMENT '描述',
  `cover_uri` VARCHAR(1024) COMMENT '封面图uri',
  `rent_flag` TINYINT COMMENT '是否开放预定 1是 0否',
  `price` DECIMAL(10,2) COMMENT '价格',
  `associate_room_id` BIGINT COMMENT '关联办公室id',
  `status` TINYINT COMMENT '1-未预定，2-已预定',
  `begin_time` DATETIME COMMENT '预定开始时间',
  `end_time` DATETIME COMMENT '预定结束时间',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `eh_office_cubicle_room` (
  `id` BIGINT NOT NULL DEFAULT 0 COMMENT 'id',
  `namespace_id` INTEGER NOT NULL,
  `owner_type`  VARCHAR(255) COMMENT 'owner type: community',
  `owner_id` BIGINT COMMENT '所属的社区ID（和可见范围的不一样）',
  `space_id` BIGINT COMMENT '空间ID',
  `room_name` VARCHAR(127) COMMENT '办公室名臣',
  `cover_uri` VARCHAR(1024) COMMENT '封面图uri',
  `rent_flag` TINYINT COMMENT '是否开放预定 1是 0否',
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `operator_uid` BIGINT,
  `operate_time` DATETIME,
  `description` TEXT COMMENT '描述',
  `price` DECIMAL(10,2) COMMENT '价格',
  `status` TINYINT COMMENT '1-未预定，2-已预定',
  `begin_time` DATETIME COMMENT '预定开始时间',
  `end_time` DATETIME COMMENT '预定结束时间',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `eh_office_cubicle_station_rent` (
  `id` BIGINT NOT NULL DEFAULT 0 COMMENT 'id',
  `namespace_id` INTEGER NOT NULL,
  `space_id` BIGINT COMMENT '空间ID',
  `order_id` BIGINT COMMENT '订单号',
  `begin_time` DATETIME COMMENT '预定开始时间',
  `end_time` DATETIME COMMENT '预定结束时间',
  `station_id` BIGINT COMMENT '工位/办公室Id',
  `station_name` VARCHAR(127),
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `operator_uid` BIGINT,
  `operate_time` DATETIME,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


CREATE TABLE `eh_office_cubicle_rent_orders` (
  `id` BIGINT NOT NULL DEFAULT 0 COMMENT 'id',
  `order_no` BIGINT,
  `biz_order_no` VARCHAR(128),
  `namespace_id` INTEGER NOT NULL,
  `owner_type`  VARCHAR(255) COMMENT 'owner type: community',
  `owner_id` BIGINT COMMENT '所属的社区ID（和可见范围的不一样）',
  `space_id` BIGINT COMMENT '空间ID',
  `space_name` VARCHAR(255) COMMENT '空间名称',
  `begin_time` DATETIME COMMENT '预定开始时间',
  `end_time` DATETIME COMMENT '预定结束时间',
  `rent_type` TINYINT COMMENT '1-长租，0-短租',
  `station_type` TINYINT COMMENT '1-普通工位，0-办公室',
  `station_id` BIGINT COMMENT '工位/办公室Id',
  `rental_order_no` BIGINT COMMENT '资源预约订单号',
  `order_status` TINYINT COMMENT '订单状态',
  `request_type` TINYINT COMMENT '订单来源',
  `paid_type` VARCHAR(32) COMMENT '支付方式',
  `paid_mode` TINYINT COMMENT '支付类型',
  `price` DECIMAL(10,2) COMMENT '价格',
  `rent_count` BIGINT COMMENT '预定数量',
  `remark` TEXT COMMENT '备注',
  `reserver_uid` BIGINT COMMENT '预定人id',
  `reserver_name` VARCHAR(32) COMMENT '预定人姓名',
  `reserver_enterprise_name` VARCHAR(64) COMMENT '预定人公司名称',
  `reserver_enterprise_Id` BIGINT COMMENT '预定人公司ID',
  `reserver_contact_token` VARCHAR(64) COMMENT '预定人联系方式',
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `operator_uid` BIGINT,
  `operate_time` DATETIME,
  `use_detail` VARCHAR(255),
  `refund_strategy` TINYINT COMMENT '1-custom, 2-full',
  `account_name` VARCHAR(64) COMMENT '收款账户名称',
  `general_order_id` BIGINT COMMENT '统一订单ID',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `eh_office_cubicle_payee_accounts` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER,
  `owner_type` VARCHAR(32) NOT NULL COMMENT 'community 园区或者其他类型',
  `owner_id` BIGINT NOT NULL COMMENT '园区id或者其他id',
  `account_id` BIGINT,
  `merchant_id` BIGINT,
  `account_name` VARCHAR(64) COMMENT '收款账户名称',
  `create_time` DATETIME,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `eh_office_cubicle_charge_users` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER,
  `owner_type` VARCHAR(32) COMMENT 'community 园区或者其他类型',
  `owner_id` BIGINT COMMENT '园区id或者其他id',
  `space_id` BIGINT,
  `charge_uid` BIGINT,
  `charge_name` VARCHAR(32),
  `create_time` DATETIME,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `eh_office_cubicle_refund_rule` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER,
  `owner_type` VARCHAR(32) NOT NULL COMMENT 'community 园区或者其他类型',
  `owner_id` BIGINT NOT NULL COMMENT '园区id或者其他id',
  `space_id` BIGINT,
  `refund_strategy` TINYINT,
  `duration_type` TINYINT COMMENT '1: 时长内, 2: 时长外',
  `duration_unit` TINYINT COMMENT '时长单位，比如 天，小时',
  `duration` DOUBLE COMMENT '时长',
  `factor` DOUBLE COMMENT '价格系数',
  `create_time` DATETIME,
  `creator_uid` BIGINT,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `eh_office_cubicle_refund_tips` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL,
  `space_id` BIGINT,
  `refund_strategy` TINYINT,
  `tips` VARCHAR(255),
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


ALTER TABLE eh_office_cubicle_spaces ADD COLUMN open_flag TINYINT COMMENT '是否开启空间，1是，0否';
ALTER TABLE eh_office_cubicle_spaces ADD COLUMN short_rent_nums VARCHAR(32) COMMENT '短租工位数量';
ALTER TABLE eh_office_cubicle_spaces ADD COLUMN long_rent_price DECIMAL(10,2) COMMENT '长租工位价格';
ALTER TABLE eh_office_cubicle_spaces ADD COLUMN refund_strategy TINYINT;


ALTER TABLE eh_office_cubicle_attachments ADD COLUMN type TINYINT COMMENT '1,空间，2短租工位，3开放式工位';

ALTER TABLE eh_office_cubicle_orders MODIFY COLUMN employee_number VARCHAR(64) COMMENT '雇员数量';

-- AUTHOR: 张智伟
-- REMARK: 一些云部署的mysql没有开启支持TRIGGER脚本，修改实现方式，删除原有的trigger
DROP TRIGGER IF EXISTS employee_dismiss_trigger_for_auto_remove_payment_limit;

-- AUTHOR: 胡琪
-- REMARK: 工作流-2.8.1，工作流节点关联的全局表单字段配置信息
CREATE TABLE `eh_general_form_fields_config` (
  `id` bigint(20) NOT NULL,
  `namespace_id` int(11) NOT NULL DEFAULT '0',
  `organization_id` bigint(20) NOT NULL DEFAULT '0',
  `owner_id` bigint(20) NOT NULL,
  `owner_type` varchar(64) NOT NULL,
  `module_id` bigint(20) DEFAULT NULL COMMENT 'the module id',
  `module_type` varchar(64) DEFAULT NULL,
  `project_id` bigint(20) NOT NULL DEFAULT '0',
  `project_type` varchar(64) NOT NULL DEFAULT 'EhCommunities',
  `form_origin_id` bigint(20) NOT NULL DEFAULT '0' COMMENT 'The id of the original form',
  `form_version` bigint(20) NOT NULL DEFAULT '0',
  `config_origin_id` bigint(20) NOT NULL DEFAULT '0' COMMENT 'The id of the original form fields config',
  `config_version` bigint(20) NOT NULL DEFAULT '0',
  `config_type` varchar(64) NOT NULL COMMENT '表单节点配置类型',
  `form_fields` text COMMENT 'json 存放表单字段',
  `status` tinyint(4) NOT NULL COMMENT '0: invalid, 1: config, 2: running',
  `create_time` datetime(3) NOT NULL COMMENT 'record create time',
  `creator_uid` bigint(20) NOT NULL DEFAULT '0',
  `update_time` datetime(3) DEFAULT NULL COMMENT 'record update time',
  `updater_uid` bigint(20) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


