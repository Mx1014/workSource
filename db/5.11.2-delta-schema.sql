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
`person_id` int NOT NULL COMMENT '人员ID',
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
  `rent_type` TINYINT,
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


-- AUTHOR: 莫谋斌 20181213
-- REMARK: 数据库修改为保存uri
ALTER TABLE eh_communities  CHANGE background_img_url background_img_uri varchar(500) DEFAULT '' COMMENT '小区或园区项目的图片链接';

-- AUTHOR: xq.tian
-- REMARK: 工作流 2.8.1 新增字段
ALTER TABLE eh_flow_nodes ADD COLUMN form_relation_type TINYINT DEFAULT '0' NOT NULL COMMENT '1: 关联自定义表单, 2: 关联eh_flows关联的表单';
ALTER TABLE eh_flow_nodes ADD COLUMN form_relation_data TEXT COMMENT '表单关联数据，json格式';
ALTER TABLE eh_flows ADD COLUMN form_status TINYINT DEFAULT '0' NOT NULL COMMENT '0: disable, 1: enable';
ALTER TABLE eh_flows ADD COLUMN form_relation_type TINYINT DEFAULT '0' NOT NULL COMMENT '1: 关联自定义表单, 2: 关联eh_flows关联的表单';
ALTER TABLE eh_flows ADD COLUMN form_relation_data TEXT COMMENT '表单关联数据，json格式';

ALTER TABLE eh_flow_cases ADD INDEX i_eh_ns_org_module_id(namespace_id, organization_id, module_id);
ALTER TABLE eh_flows ADD INDEX i_eh_flow_main_id_flow_version(flow_main_id, flow_version);
ALTER TABLE eh_flows ADD INDEX i_eh_ns_org_module_id(namespace_id, organization_id, module_id);

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

-- AUTHOR: 杨崇鑫 2018-12-05
-- REMARK: 物业缴费V7.4(瑞安项目-资产管理对接CM系统) ： 一个特殊error标记给左邻系统，左邻系统以此标记判断该条数据下一次同步不再传输
ALTER TABLE `eh_payment_bills` ADD COLUMN `third_sign` TINYINT COMMENT '一个特殊error标记给左邻系统，左邻系统以此标记判断该条数据下一次同步不再传输（0：不传输）';

-- AUTHOR: 杨崇鑫 2018-12-05
-- REMARK: 物业缴费V7.4(瑞安项目-资产管理对接CM系统) ： 增加一个支付状态是否已确认字段
ALTER TABLE `eh_payment_bills` ADD COLUMN `confirm_flag` TINYINT COMMENT '支付状态是否已确认字段，1：已确认；0：待确认';

-- AUTHOR: mengqianxiang
-- REMARK: 增加eh_payment_exemption_items表的状态字段
ALTER TABLE eh_payment_exemption_items ADD `merchant_order_id` BIGINT  COMMENT "账单明细ID";
ALTER TABLE eh_payment_exemption_items ADD `delete_flag` TINYINT NOT NULL DEFAULT 1 COMMENT "删除状态：0：已删除；1：正常使用";


-- AUTHOR: djm 20181206
-- REMARK: 合同套打开关
ALTER TABLE eh_contract_categories ADD COLUMN print_switch_status tinyint(4) NOT NULL DEFAULT 0 COMMENT '合同套打(0 关闭，任何合同状态都能打印 1 打开 只有审批通过，正常合同、即将到期合同才能进行此操作)打开关';
ALTER TABLE eh_contract_categories ADD COLUMN editor_switch_status tinyint(4) NOT NULL DEFAULT 0 COMMENT '合同文档编辑开关';

-- AUTHOR: tangcen 2018年12月10日
-- REMARK: 合同文档表
CREATE TABLE `eh_contract_documents` (
  `id` bigint(20) NOT NULL,
  `namespace_id` int(11) NOT NULL,
  `community_id` bigint(20) NOT NULL,
  `category_id` bigint(20) NOT NULL COMMENT '合同多入口id',
  `contract_id` bigint(20) NOT NULL COMMENT '合同文档关联的合同id',
  `contract_template_id` bigint(20) DEFAULT NULL COMMENT '关联的合同模板id',
  `parent_id` bigint(20) DEFAULT '0' COMMENT '上一版合同文档的id',
  `name` varchar(255) DEFAULT NULL COMMENT '合同文档名称',
  `content_type` varchar(32) DEFAULT NULL COMMENT '文本存储方式（gogs）',
  `content` varchar(128) DEFAULT NULL COMMENT '文本内容（在gogs存储时，存的是文本的commit id）',
  `version` int(11) DEFAULT '0' COMMENT '合同文档的版本号',
  `status` tinyint(4) DEFAULT NULL COMMENT '合同文档的状态-0: inactive, 1: confirming, 2: active',
  `creator_uid` bigint(20) NOT NULL COMMENT '创建人',
  `create_time` datetime NOT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_uid` bigint(20) NOT NULL,
  `update_time` datetime NOT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='合同文档表';

-- AUTHOR: tangcen 2018年12月10日
-- REMARK: 在合同表中添加用于记录合同文档的字段
ALTER TABLE `eh_contracts` ADD COLUMN `document_id` bigint NULL COMMENT '当前生效的合同文档id';

-- 固定添加的动态字段
ALTER TABLE `eh_contracts` ADD COLUMN `apartment_delivery_time` DATETIME DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '房屋交付日期';
ALTER TABLE `eh_contracts` ADD COLUMN `down_payment_rent_time` DATETIME DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '首期租金开始日期';
ALTER TABLE `eh_contracts` ADD COLUMN `monthly_margin` INT DEFAULT '0' COMMENT '保证金月数';
ALTER TABLE `eh_contracts` ADD COLUMN `margin_amount` DECIMAL (12, 2) DEFAULT '0.00' COMMENT '保证金金额';
ALTER TABLE `eh_contracts` ADD COLUMN `monthly_service_charge` DECIMAL (12, 2) DEFAULT '0.00' COMMENT '月服务费金额';
ALTER TABLE `eh_contracts` ADD COLUMN `pre_amount` DECIMAL (12, 2) DEFAULT '0.00' COMMENT '预付金额';
ALTER TABLE `eh_contracts` ADD COLUMN `contracting_place` VARCHAR (64) DEFAULT NULL COMMENT '签约地点';
-- 预留字段
ALTER TABLE `eh_contracts` ADD COLUMN `string_tag1` VARCHAR (64) DEFAULT NULL COMMENT '预留字段1';
ALTER TABLE `eh_contracts` ADD COLUMN `string_tag2` VARCHAR (64) DEFAULT NULL COMMENT '预留字段2';
ALTER TABLE `eh_contracts` ADD COLUMN `string_tag3` VARCHAR (64) DEFAULT NULL COMMENT '预留字段3';
ALTER TABLE `eh_contracts` ADD COLUMN `string_tag4` VARCHAR (64) DEFAULT NULL COMMENT '预留字段4';
ALTER TABLE `eh_contracts` ADD COLUMN `string_tag5` VARCHAR (64) DEFAULT NULL COMMENT '预留字段5';
ALTER TABLE `eh_contracts` ADD COLUMN `string_tag6` VARCHAR (64) DEFAULT NULL COMMENT '预留字段6';
ALTER TABLE `eh_contracts` ADD COLUMN `string_tag7` VARCHAR (64) DEFAULT NULL COMMENT '预留字段7';
ALTER TABLE `eh_contracts` ADD COLUMN `string_tag8` VARCHAR (64) DEFAULT NULL COMMENT '预留字段8';
ALTER TABLE `eh_contracts` ADD COLUMN `string_tag9` VARCHAR (64) DEFAULT NULL COMMENT '预留字段9';
ALTER TABLE `eh_contracts` ADD COLUMN `string_tag10` VARCHAR (64) DEFAULT NULL COMMENT '预留字段10';

-- AUTHOR: tangcen 2018年12月11日
-- REMARK: 在合同模板表中添加用于记录合同模板初始化参数的字段
ALTER TABLE `eh_contract_templates` ADD COLUMN `init_params` varchar(1024) COMMENT '合同模板初始化参数（计价条款、关联资产等的数目），前端会解析这个json';

-- AUTHOR: 黄鹏宇 2018年12月11日
-- REMARK: 新建字段
ALTER table eh_enterprise_customers add column legal_address varchar(512) comment '法定地址';
ALTER table eh_enterprise_customers add column legal_address_zip varchar(32) comment '法定地址邮编';
ALTER table eh_enterprise_customers add column postal_address varchar(512) comment '通讯地址';
ALTER table eh_enterprise_customers add column postal_address_zip varchar(32) comment '通讯地址邮编';
ALTER table eh_enterprise_customers add column taxpayer_identification_code varchar(32) comment '纳税人识别号';
ALTER table eh_enterprise_customers add column identify_card_number varchar(32) comment '身份证号码';
ALTER table eh_enterprise_customers add column opening_bank varchar(64) comment '开户行';
ALTER table eh_enterprise_customers add column opening_name varchar(64) comment '开户名';
ALTER table eh_enterprise_customers add column opening_account varchar(32) comment '开户行账号';
ALTER table eh_enterprise_customers add column string_tag17 varchar(32) comment '预留字段17';
ALTER table eh_enterprise_customers add column string_tag18 varchar(32) comment '预留字段18';
ALTER table eh_enterprise_customers add column string_tag19 varchar(32) comment '预留字段19';
ALTER table eh_enterprise_customers add column string_tag20 varchar(32) comment '预留字段20';
ALTER table eh_enterprise_customers add column string_tag21 varchar(32) comment '预留字段21';
ALTER table eh_enterprise_customers add column corp_legal_person_duty varchar(32) comment '法人代表职务';
ALTER table eh_enterprise_customers add column corp_legal_person_token varchar(32) comment '法人联系电话';


-- AUTHOR: tangcen 2018年12月12日
-- REMARK: 给eh_contract_documents添加合同编号字段
ALTER TABLE `eh_contract_documents` ADD COLUMN `contract_number` varchar(128) COMMENT '合同编号' AFTER `name`;

-- AUTHOR: 杨崇鑫 2018-12-05
-- REMARK: 物业缴费V7.4(瑞安项目-资产管理对接CM系统) ： 一个特殊error标记给左邻系统，左邻系统以此标记判断该条数据下一次同步不再传输
ALTER TABLE `eh_payment_bills` ADD COLUMN `third_sign` TINYINT COMMENT '一个特殊error标记给左邻系统，左邻系统以此标记判断该条数据下一次同步不再传输（0：不传输）';

-- AUTHOR: 杨崇鑫 2018-12-05
-- REMARK: 物业缴费V7.4(瑞安项目-资产管理对接CM系统) ： 增加一个支付状态是否已确认字段
ALTER TABLE `eh_payment_bills` ADD COLUMN `confirm_flag` TINYINT COMMENT '支付状态是否已确认字段，1：已确认；0：待确认';

-- AUTHOR: mengqianxiang
-- REMARK: 增加eh_payment_exemption_items表的状态字段
ALTER TABLE eh_payment_exemption_items ADD `merchant_order_id` BIGINT  COMMENT "账单明细ID";
ALTER TABLE eh_payment_exemption_items ADD `delete_flag` TINYINT NOT NULL DEFAULT 1 COMMENT "删除状态：0：已删除；1：正常使用";

-- AUTHOR: 孟千翔
-- REMARK: eh_payment_bill_groups表添加字段，作用自定义账单周期
ALTER TABLE eh_payment_bill_groups ADD `billing_cycle_expression` VARCHAR(100) COMMENT "账单周期表达式";
ALTER TABLE eh_payment_bill_groups ADD `bills_day_expression` VARCHAR(100) COMMENT "出账单日表达式";
ALTER TABLE eh_payment_bill_groups ADD `due_day_expression` VARCHAR(100) COMMENT "最晚还款日表达式";

