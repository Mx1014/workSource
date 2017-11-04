-- 一下到分割线为止已经在true-4上了，应该删除
ALTER TABLE `eh_payment_charging_standards` ADD COLUMN `instruction` VARCHAR(1024) DEFAULT NULL COMMENT '说明';
ALTER TABLE `eh_payment_bill_groups` ADD COLUMN `due_day` INTEGER DEFAULT NULL COMMENT '最晚还款日，距离账单日的距离，单位可以为月 ';
ALTER TABLE `eh_payment_bill_groups` ADD COLUMN `due_day_type` TINYINT DEFAULT 1 COMMENT '1:日，2：月 ';

DROP TABLE IF EXISTS `eh_payment_formula`;
CREATE TABLE `eh_payment_formula` (
  `id` bigint(20) NOT NULL,
  `charging_standard_id` bigint(20) DEFAULT NULL,
  `name` varchar(10) DEFAULT NULL,
  `constraint_variable_identifer` varchar(255) DEFAULT NULL,
  `start_constraint` tinyint(4) DEFAULT NULL COMMENT '1:大于；2：大于等于；3：小于；4：小于等于',
  `start_num` decimal(10,2) DEFAULT '0.00',
  `end_constraint` tinyint(4) DEFAULT NULL COMMENT '1:大于；2：大于等于；3：小于；4：小于等于',
  `end_num` decimal(10,2) DEFAULT '0.00',
  `variables_json_string` varchar(2048) DEFAULT NULL COMMENT 'json strings of variables injected for a particular formula',
  `formula` varchar(1024) DEFAULT NULL,
  `formula_json` varchar(2048) DEFAULT NULL,
  `formula_type` tinyint(4) DEFAULT NULL COMMENT '1: fixed fee; 2: normal formula; 3: gradient varied on variable price; 4: gradients varied functions on each variable section',
  `price_unit_type` tinyint(4) DEFAULT NULL COMMENT '1:日单价; 2:月单价; 3:季单价; 4:年单价',
  `creator_uid` bigint(20) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `operator_uid` bigint(20) DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='收费标准公式表';

ALTER TABLE `eh_payment_charging_standards` ADD COLUMN `suggest_unit_price` DECIMAL(10,2) DEFAULT NULL COMMENT '建议单价';
ALTER TABLE `eh_payment_bill_groups_rules` ADD COLUMN `bill_item_month_offset` INTEGER DEFAULT NULL COMMENT '收费项产生时间偏离当前月的月数';
ALTER TABLE `eh_payment_bill_groups_rules` ADD COLUMN `bill_item_day_offset` INTEGER DEFAULT NULL COMMENT '收费项产生时间偏离当前月的日数';
ALTER TABLE `eh_payment_charging_standards` ADD COLUMN `area_size_type` INTEGER DEFAULT 1 COMMENT '计费面积类型,1：合同面积；2.建筑面积；3：使用面积；4：出租面积';
ALTER TABLE `eh_payment_bill_groups` MODIFY COLUMN `name` VARCHAR(50) DEFAULT NULL COMMENT '账单组名称';

ALTER TABLE `eh_payment_bill_items` ADD COLUMN `due_day_deadline` varchar(30) DEFAULT NULL COMMENT '最后付款日期';
ALTER TABLE `eh_payment_bills` ADD COLUMN `date_str_begin` varchar(30) DEFAULT NULL COMMENT '账期开始日期';
ALTER TABLE `eh_payment_bills` ADD COLUMN `date_str_end` varchar(30) DEFAULT NULL COMMENT '账期结束日期';
ALTER TABLE `eh_payment_bills` ADD COLUMN `date_str_due` varchar(30) DEFAULT NULL COMMENT '出账单日期';
ALTER TABLE `eh_payment_bills` ADD COLUMN `due_day_deadline` varchar(30) DEFAULT NULL COMMENT '最后付款日期';

ALTER TABLE `eh_payment_charging_item_scopes` ADD COLUMN `project_level_name` VARCHAR(30) DEFAULT NULL COMMENT '园区自定义的收费项目名字';

ALTER TABLE `eh_payment_bill_items` ADD COLUMN `date_str_generation` VARCHAR(40) DEFAULT NULL COMMENT '费用产生日期';

ALTER TABLE `eh_payment_bills` ADD COLUMN `charge_status` TINYINT DEFAULT 0 COMMENT '缴费状态，0：正常；1：欠费';


ALTER TABLE `eh_payment_bills` ADD COLUMN `real_paid_time` DATETIME DEFAULT NULL COMMENT '实际付款时间';
-- fun-shenye by wentian
 -- 记得张江高科和物业缴费的重构，修改zjgkcode为wuyecode，但先查下wuyecode是否存在

ALTER TABLE `eh_payment_contract_receiver` ADD COLUMN `in_work` TINYINT DEFAULT 0 COMMENT '0:工作完成；1：正在生成';
ALTER TABLE `eh_payment_contract_receiver` ADD COLUMN `is_recorder` TINYINT DEFAULT 1 COMMENT '0：合同状态记录者，不保存计价数据；1：不是合同状态记录者';
ALTER TABLE `eh_payment_bills` ADD COLUMN `next_switch` TINYINT DEFAULT NULL COMMENT '下一次switch的值';

ALTER TABLE `eh_payment_charging_item_scopes` ADD COLUMN `decoupling_flag` TINYINT DEFAULT 0 COMMENT '解耦标志，0:耦合中，收到域名下全部设置的影响;1:副本解耦';
-- 分割线
-- payment_wentian_v2 new sql

ALTER TABLE `eh_payment_charging_standards_scopes` ADD COLUMN namespace_id INTEGER DEFAULT 0;
ALTER TABLE `eh_payment_contract_receiver` ADD COLUMN bill_group_rule_id BIGINT DEFAULT NULL;
ALTER TABLE `eh_payment_bill_items` ADD COLUMN bill_group_rule_id BIGINT DEFAULT NULL;


-- 4.10.3，合并记得删
-- 园区入驻3.6 add by sw 20171023
DROP TABLE IF EXISTS `eh_lease_configs`;
DROP TABLE IF EXISTS `eh_lease_configs2`;

CREATE TABLE `eh_lease_configs` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `namespace_id` int(11) NOT NULL DEFAULT '0',
  `owner_type` varchar(32) DEFAULT NULL COMMENT 'owner type, e.g EhCommunities',
  `owner_id` bigint(20) DEFAULT NULL COMMENT 'owner id, e.g eh_communities id',
  `config_name` varchar(128) DEFAULT NULL,
  `config_value` varchar(128) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `creator_uid` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `eh_lease_project_communities` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `lease_project_id` bigint(20) NOT NULL COMMENT 'lease project id',
  `community_id` bigint(20) NOT NULL COMMENT 'community id',
  `creator_uid` bigint(20) NOT NULL,
  `create_time` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


CREATE TABLE `eh_lease_projects` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `namespace_id` int(11) NOT NULL DEFAULT '0',
  `project_id` bigint(20) NOT NULL COMMENT 'id of the record',
  `city_id` bigint(20) NOT NULL COMMENT 'city id in region table',
  `city_name` varchar(64) DEFAULT NULL COMMENT 'redundant for query optimization',
  `area_id` bigint(20) NOT NULL COMMENT 'area id in region table',
  `area_name` varchar(64) DEFAULT NULL COMMENT 'redundant for query optimization',
  `address` varchar(512) DEFAULT NULL,
  `longitude` double DEFAULT NULL,
  `latitude` double DEFAULT NULL,
  `contact_name` varchar(128) DEFAULT NULL,
  `contact_phone` varchar(128) DEFAULT NULL COMMENT 'the phone number',
  `description` text,
  `traffic_description` text,
  `poster_uri` varchar(256) DEFAULT NULL,
  `extra_info_json` text,

  `creator_uid` bigint(20) DEFAULT NULL COMMENT 'user who suggested the creation',
  `create_time` datetime DEFAULT NULL,

  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 帖子增加置顶功能  add by yanjun 201710231623
ALTER TABLE `eh_forum_posts` ADD COLUMN `stick_flag`  tinyint(4) NOT NULL DEFAULT 0 COMMENT '是否置顶，0-否，1-是';
ALTER TABLE `eh_activities` ADD COLUMN `stick_flag`  tinyint(4) NOT NULL DEFAULT 0 COMMENT '是否置顶，0-否，1-是';

-- merge from feature-customer add by xiongying20171024
CREATE TABLE `eh_customer_events` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER  NOT NULL COMMENT '域空间id',
  `customer_type` TINYINT NOT NULL COMMENT '所属客户类型',
  `customer_id`  BIGINT    COMMENT '所属客户id',
  `customer_name` VARCHAR(128)   COMMENT '客户名称',
  `contact_name` VARCHAR(64) ,
  `content`  TEXT,
  `creator_uid`  BIGINT   COMMENT '创建人uid',
  `create_time` DATETIME  ,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `eh_customer_tracking_plans` (
  `id` BIGINT  NOT NULL,
  `namespace_id` INTEGER  NOT NULL COMMENT '域空间id',
  `customer_type` TINYINT  NOT NULL COMMENT '所属客户类型',
  `customer_id`  BIGINT   COMMENT '所属客户id',
  `customer_name` VARCHAR(128)   COMMENT '客户名称',
  `contact_name` VARCHAR(64)   COMMENT '联系人',
  `tracking_type` BIGINT   COMMENT '计划跟进类型',
  `tracking_time` DATETIME   COMMENT '跟进时间',
  `notify_time` DATETIME   COMMENT '提醒时间',
  `title` VARCHAR(128) DEFAULT NULL COMMENT '标题',
  `content` TEXT  COMMENT '内容',
  `status`  TINYINT  NOT NULL COMMENT '0: inactive; 1: waiting for approval; 2: active',
  `creator_uid` BIGINT     COMMENT '创建人uid',
  `create_time` DATETIME   COMMENT '创建时间',
  `update_uid`  BIGINT     COMMENT '修改人uid',
  `update_time` DATETIME   COMMENT '修改时间',
  `delete_uid`  BIGINT     COMMENT '删除人uid',
  `delete_time` DATETIME   COMMENT '删除时间',
  `notify_status` TINYINT  DEFAULT NULL COMMENT '提醒状态  0:无需提醒   1:待提醒   2:已提醒',
  `read_status` TINYINT DEFAULT '0' COMMENT 'is read?  0:no  1:yes',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `eh_customer_trackings` (
  `id` BIGINT  NOT NULL,
  `namespace_id` INTEGER NOT NULL COMMENT '域空间id',
  `customer_type` TINYINT NOT NULL COMMENT '所属客户类型',
  `customer_id` BIGINT    COMMENT '所属客户id',
  `customer_name` VARCHAR(128)   COMMENT '客户名称',
  `contact_name` VARCHAR(64)    COMMENT '联系人',
  `tracking_type` BIGINT    COMMENT '跟进类型',
  `tracking_uid` BIGINT     COMMENT '跟进人uid ',
  `intention_grade` INTEGER    COMMENT '意向等级',
  `tracking_time` DATETIME   COMMENT '跟进时间',
  `content` TEXT COMMENT '跟进内容',
  `content_img_uri` VARCHAR(2048) COMMENT '跟进内容图片uri',
  `status` TINYINT NOT NULL COMMENT '0: inactive; 1: waiting for approval; 2: active',
  `creator_uid` BIGINT     COMMENT '创建人uid',
  `create_time` DATETIME   COMMENT '创建时间',
  `update_uid` BIGINT     COMMENT '修改人uid',
  `update_time` DATETIME   COMMENT '修改时间',
  `delete_uid` BIGINT     COMMENT '删除人uid',
  `delete_time` DATETIME    COMMENT '删除时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `eh_tracking_notify_logs` (
  `id` BIGINT NOT NULL,
  `customer_type` TINYINT  NOT NULL,
  `customer_id`  BIGINT  NOT NULL,
  `notify_text` TEXT,
  `receiver_id` BIGINT NOT NULL,
  `create_time` DATETIME NOT NULL ,
  `status` TINYINT NOT NULL ,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

ALTER TABLE `eh_enterprise_customers`
ADD COLUMN `tracking_uid`  BIGINT  NULL DEFAULT -1 COMMENT '跟进人uid' AFTER `update_time`,
ADD COLUMN `tracking_name`   VARCHAR(32) NULL COMMENT '跟进人姓名' AFTER `tracking_uid`,
ADD COLUMN `property_area`  DOUBLE NULL COMMENT '资产面积' AFTER `tracking_name`,
ADD COLUMN `property_unit_price`  DOUBLE NULL COMMENT '资产单价' AFTER `property_area`,
ADD COLUMN `property_type`  BIGINT NULL COMMENT '资产类型' AFTER `property_unit_price`,
ADD COLUMN `longitude`  DOUBLE NULL COMMENT '经度' AFTER `property_type`,
ADD COLUMN `latitude`  DOUBLE NULL COMMENT '纬度' AFTER `longitude`,
ADD COLUMN `geohash`  VARCHAR(32) NULL DEFAULT NULL AFTER `latitude` ,
ADD COLUMN `last_tracking_time`  DATETIME   COMMENT '最后一次跟进时间' AFTER `geohash` ,
ADD COLUMN `contact_duty`  VARCHAR(64) NULL AFTER `contact_mobile`;

ALTER TABLE `eh_buildings` ADD INDEX building_name ( `name`, `alias_name`);

ALTER TABLE `eh_contracts` ADD COLUMN `settled` VARCHAR(128);
ALTER TABLE `eh_contracts` ADD COLUMN `layout` VARCHAR(128);

ALTER TABLE `eh_addresses` ADD COLUMN `apartment_number` VARCHAR(32);
ALTER TABLE `eh_addresses` ADD COLUMN `address_unit` VARCHAR(32);
ALTER TABLE `eh_addresses` ADD COLUMN `address_ownership_id` BIGINT COMMENT '产权归属: 自有、出售、非产权..., refer to the id of eh_var_field_items';
ALTER TABLE `eh_addresses` ADD COLUMN `address_ownership_name` VARCHAR(128) COMMENT '产权归属: 自有、出售、非产权..., refer to the display_name of eh_var_field_items';
ALTER TABLE `eh_addresses` ADD COLUMN `remark` VARCHAR(128);

CREATE TABLE `eh_address_attachments` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `address_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'refer to the id of eh_addresses',
  `name` VARCHAR(128),
  `file_size` INTEGER NOT NULL DEFAULT 0,
  `content_type` VARCHAR(32) COMMENT 'attachment object content type',
  `content_uri` VARCHAR(1024) COMMENT 'attachment object link info on storage',
  `download_count` INTEGER NOT NULL DEFAULT 0,
  `creator_uid` BIGINT NOT NULL DEFAULT 0,
  `create_time` DATETIME NOT NULL,

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `eh_contract_charging_changes` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0 COMMENT 'namespace of owner resource, redundant info to quick namespace related queries',
  `contract_id` BIGINT NOT NULL COMMENT 'id of eh_contracts',
  `charging_item_id` BIGINT COMMENT '收费项',
  `change_type` TINYINT COMMENT '1: 调租; 2: 免租',
  `change_method` TINYINT COMMENT '1: 按金额递增; 2: 按金额递减; 3: 按比例递增; 4: 按比例递减',
  `change_period` INTEGER,
  `period_unit` TINYINT COMMENT '1: 天; 2: 月; 3: 年',
  `change_range` DECIMAL(10,2),
  `change_start_time` DATETIME,
  `change_expired_time` DATETIME,
  `remark` VARCHAR(1024),
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '0: inactive; 1: waiting for approval; 2: active',
  `create_uid` BIGINT NOT NULL DEFAULT 0,
  `create_time` DATETIME,
  `operator_uid` BIGINT,
  `update_time` DATETIME,
  `delete_uid` BIGINT,
  `delete_time` DATETIME,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `eh_contract_charging_change_addresses` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `namespace_id` int(11) NOT NULL DEFAULT '0' COMMENT 'namespace of owner resource, redundant info to quick namespace related queries',
  `charging_change_id` bigint(20) NOT NULL COMMENT 'id of eh_contract_charging_changes',
  `address_id` bigint(20) DEFAULT NULL,
  `status` tinyint(4) NOT NULL DEFAULT '1' COMMENT '0: inactive; 1: waiting for approval; 2: active',
  `create_uid` bigint(20) NOT NULL DEFAULT '0',
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;



--
-- 条件   add by xq.tian  2017/10/24
--
-- DROP TABLE IF EXISTS `eh_flow_conditions`;
CREATE TABLE `eh_flow_conditions` (
  `id` BIGINT,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `flow_main_id` BIGINT NOT NULL,
  `flow_version` INTEGER NOT NULL,
  `condition_level` INTEGER NOT NULL DEFAULT 0,
  `flow_node_id` BIGINT NOT NULL,
  `flow_node_level` INTEGER,
  `flow_link_id` BIGINT,
  `flow_link_level` INTEGER,
  `next_node_id` BIGINT NOT NULL DEFAULT 0,
  `next_node_level` INTEGER NOT NULL DEFAULT 0,
  `status` TINYINT NOT NULL COMMENT '0: invalid, 1: valid',
  `creator_uid` BIGINT,
  `create_time` DATETIME(3),
  `update_uid` BIGINT,
  `update_time` DATETIME(3),
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

--
-- 条件表达式
--
-- DROP TABLE IF EXISTS `eh_flow_condition_expressions`;
CREATE TABLE `eh_flow_condition_expressions` (
  `id` BIGINT,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `flow_main_id` BIGINT NOT NULL,
  `flow_version` INTEGER NOT NULL,
  `flow_condition_id` BIGINT NOT NULL COMMENT 'ref eh_flow_conditions',
  `logic_operator` VARCHAR(24) NOT NULL COMMENT '&&, ||, !',
  `relational_operator` VARCHAR(24) NOT NULL COMMENT '>, <, ==, !=',
  `variable_type1` VARCHAR(32) NOT NULL DEFAULT 1 COMMENT 'const, variable',
  `variable1` VARCHAR(64) NOT NULL DEFAULT 1 COMMENT '${varName} or 2',
  `variable_type2` VARCHAR(32) NOT NULL DEFAULT 1 COMMENT 'const, variable',
  `variable2` VARCHAR(64) NOT NULL DEFAULT 1 COMMENT '${varName} or 2',
  `status` TINYINT NOT NULL COMMENT '0: invalid, 1: valid',
  `creator_uid` BIGINT,
  `create_time` DATETIME(3),
  `update_uid` BIGINT,
  `update_time` DATETIME(3),
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

--
-- 泳道
--
-- DROP TABLE IF EXISTS `eh_flow_lanes`;
CREATE TABLE `eh_flow_lanes` (
  `id` BIGINT,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `flow_main_id` BIGINT NOT NULL,
  `flow_version` INTEGER NOT NULL,
  `display_name` VARCHAR(128) COMMENT 'lane name',
  `display_name_absort` VARCHAR(128) COMMENT 'when flowCase absort display this',
  `flow_node_level` INTEGER COMMENT 'flow_node_level',
  `identifier_node_level` INTEGER COMMENT '标识这个用泳道里的那个节点里的申请人按钮',
  `identifier_node_id` BIGINT COMMENT '标识这个用泳道里的那个节点里的申请人按钮',
  `lane_level` INTEGER COMMENT 'lane level',
  `status` TINYINT NOT NULL COMMENT '0: invalid, 1: valid',
  `creator_uid` BIGINT,
  `create_time` DATETIME(3),
  `update_uid` BIGINT,
  `update_time` DATETIME(3),
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

--
-- 流程图的连接
--
-- DROP TABLE IF EXISTS `eh_flow_links`;
CREATE TABLE `eh_flow_links` (
  `id` BIGINT,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `flow_main_id` BIGINT NOT NULL,
  `flow_version` INTEGER NOT NULL,
  `display_name` VARCHAR(64) COMMENT 'display name',
  `link_level` INTEGER NOT NULL,
  `from_node_id` BIGINT NOT NULL,
  `from_node_level` INTEGER NOT NULL,
  `to_node_id` BIGINT NOT NULL,
  `to_node_level` INTEGER NOT NULL,
  `status` TINYINT NOT NULL COMMENT '0: invalid, 1: valid',
  `creator_uid` BIGINT,
  `create_time` DATETIME(3),
  `update_uid` BIGINT,
  `update_time` DATETIME(3),
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

--
-- 分支
--
-- DROP TABLE IF EXISTS `eh_flow_branches`;
CREATE TABLE `eh_flow_branches` (
  `id` BIGINT,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `flow_main_id` BIGINT NOT NULL,
  `flow_version` INTEGER NOT NULL,
  `original_node_id` BIGINT NOT NULL COMMENT '分发开始节点id',
  `original_node_level` INTEGER NOT NULL COMMENT '分发开始节点level',
  `convergence_node_id` BIGINT NOT NULL COMMENT '最终收敛节点id',
  `convergence_node_level` INTEGER NOT NULL COMMENT '最终收敛节点level',
  `process_mode` VARCHAR(32) NOT NULL DEFAULT '' COMMENT '单一路径：single, 并发执行：concurrent',
  `branch_decider` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'processor, condition',
  `status` TINYINT NOT NULL COMMENT '0: invalid, 1: valid',
  `creator_uid` BIGINT,
  `create_time` DATETIME(3),
  `update_uid` BIGINT,
  `update_time` DATETIME(3),
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

--
-- 节点和按钮的预定义参数
--
-- DROP TABLE IF EXISTS `eh_flow_predefined_params`;
CREATE TABLE `eh_flow_predefined_params` (
  `id` BIGINT,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `owner_id` BIGINT NOT NULL,
  `owner_type` VARCHAR(64) NOT NULL,
  `module_id` BIGINT NOT NULL COMMENT 'the module id',
  `module_type` VARCHAR(64) NOT NULL,
  `entity_type` VARCHAR(64) NOT NULL COMMENT 'flow_node, flow_button',
  `display_name` VARCHAR(128) COMMENT 'display name',
  `name` VARCHAR(64) COMMENT 'param name',
  `text` TEXT,
  `status` TINYINT NOT NULL COMMENT '0: invalid, 1: valid',
  `creator_uid` BIGINT,
  `create_time` DATETIME(3),
  `update_uid` BIGINT,
  `update_time` DATETIME(3),
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

--
-- 业务类别
--
-- DROP TABLE IF EXISTS `eh_flow_service_types`;
CREATE TABLE `eh_flow_service_types` (
  `id` BIGINT,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `service_name` VARCHAR(64),
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

-- 描述字段
ALTER TABLE `eh_flows` ADD COLUMN `description` VARCHAR(128) COMMENT 'flow description';
ALTER TABLE `eh_flows` ADD COLUMN `allow_flow_case_end_evaluate` TINYINT NOT NULL DEFAULT 0 COMMENT 'allow_flow_case_end_evaluate';

-- 节点类型： 开始，结束，普通节点，前置条件，后置条件
ALTER TABLE `eh_flow_nodes` ADD COLUMN `node_type` VARCHAR(32) NOT NULL DEFAULT 'normal' COMMENT 'start, end, normal, condition_front, condition_back';
ALTER TABLE `eh_flow_nodes` ADD COLUMN `goto_process_button_name` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'start, end, normal, condition_front, condition_back';
ALTER TABLE `eh_flow_nodes` ADD COLUMN `flow_lane_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'ref eh_flow_lanes';
ALTER TABLE `eh_flow_nodes` ADD COLUMN `flow_lane_level` INTEGER NOT NULL DEFAULT 0 COMMENT 'ref eh_flow_lanes';
-- 节点会签开关
ALTER TABLE `eh_flow_nodes` ADD COLUMN `need_all_processor_complete` TINYINT NOT NULL DEFAULT 0 COMMENT '节点会签开关';
--
ALTER TABLE `eh_flow_cases` ADD COLUMN `current_lane_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'ref eh_flow_lanes';
ALTER TABLE `eh_flow_cases` ADD COLUMN `parent_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'parentId, ref eh_flow_cases';
ALTER TABLE `eh_flow_cases` ADD COLUMN `start_node_id` BIGINT NOT NULL DEFAULT 0 COMMENT '开始节点id';
ALTER TABLE `eh_flow_cases` ADD COLUMN `end_node_id` BIGINT NOT NULL DEFAULT 0 COMMENT '结束节点id';
ALTER TABLE `eh_flow_cases` ADD COLUMN `start_link_id` BIGINT NOT NULL DEFAULT 0 COMMENT '开始linkId';
ALTER TABLE `eh_flow_cases` ADD COLUMN `end_link_id` BIGINT NOT NULL DEFAULT 0 COMMENT '结束linkId';
ALTER TABLE `eh_flow_cases` ADD COLUMN `evaluate_status` TINYINT NOT NULL DEFAULT 0 COMMENT '评价状态，一般指结束后还可以评价的情况';
ALTER TABLE `eh_flow_cases` ADD COLUMN `delete_flag` TINYINT NOT NULL DEFAULT 0 COMMENT '删除状态';
ALTER TABLE `eh_flow_cases` ADD COLUMN `service_type` VARCHAR(64) COMMENT 'service type';

ALTER TABLE `eh_flow_buttons` ADD COLUMN `param` VARCHAR(64) COMMENT 'the params from other module';
ALTER TABLE `eh_flow_buttons` ADD COLUMN `default_order` INTEGER NOT NULL DEFAULT 0 COMMENT 'default order';
ALTER TABLE `eh_flow_buttons` ADD COLUMN `evaluate_step` VARCHAR(64) COMMENT 'default order';

ALTER TABLE `eh_flow_cases` MODIFY COLUMN `last_step_time` DATETIME(3) COMMENT 'state change time';

-- flowCase增加附加字段，给业务使用
ALTER TABLE `eh_flow_cases` ADD COLUMN `string_tag6` VARCHAR(128);
ALTER TABLE `eh_flow_cases` ADD COLUMN `string_tag7` VARCHAR(128);
ALTER TABLE `eh_flow_cases` ADD COLUMN `string_tag8` VARCHAR(128);
ALTER TABLE `eh_flow_cases` ADD COLUMN `string_tag9` VARCHAR(128);
ALTER TABLE `eh_flow_cases` ADD COLUMN `string_tag10` VARCHAR(128);
ALTER TABLE `eh_flow_cases` ADD COLUMN `string_tag11` VARCHAR(128);
ALTER TABLE `eh_flow_cases` ADD COLUMN `string_tag12` VARCHAR(128);
ALTER TABLE `eh_flow_cases` ADD COLUMN `string_tag13` VARCHAR(128);
ALTER TABLE `eh_flow_cases` ADD COLUMN `integral_tag6` BIGINT;
ALTER TABLE `eh_flow_cases` ADD COLUMN `integral_tag7` BIGINT;
ALTER TABLE `eh_flow_cases` ADD COLUMN `integral_tag8` BIGINT;
ALTER TABLE `eh_flow_cases` ADD COLUMN `integral_tag9` BIGINT;
ALTER TABLE `eh_flow_cases` ADD COLUMN `integral_tag10` BIGINT;
ALTER TABLE `eh_flow_cases` ADD COLUMN `integral_tag11` BIGINT;
ALTER TABLE `eh_flow_cases` ADD COLUMN `integral_tag12` BIGINT;
ALTER TABLE `eh_flow_cases` ADD COLUMN `integral_tag13` BIGINT;

ALTER TABLE eh_customer_talents modify COLUMN status TINYINT DEFAULT 2 COMMENT '0: inactive; 1: waiting for approval; 2: active';
ALTER TABLE eh_customer_apply_projects modify COLUMN status TINYINT DEFAULT 2 COMMENT '0: inactive; 1: waiting for approval; 2: active';
ALTER TABLE eh_customer_certificates modify COLUMN status TINYINT DEFAULT 2 COMMENT '0: inactive; 1: waiting for approval; 2: active';
ALTER TABLE eh_customer_commercials modify COLUMN status TINYINT DEFAULT 2 COMMENT '0: inactive; 1: waiting for approval; 2: active';
ALTER TABLE eh_customer_economic_indicators modify COLUMN status TINYINT DEFAULT 2 COMMENT '0: inactive; 1: waiting for approval; 2: active';
ALTER TABLE eh_customer_investments modify COLUMN status TINYINT DEFAULT 2 COMMENT '0: inactive; 1: waiting for approval; 2: active';
ALTER TABLE eh_customer_patents modify COLUMN status TINYINT DEFAULT 2 COMMENT '0: inactive; 1: waiting for approval; 2: active';
ALTER TABLE eh_customer_tracking_plans modify COLUMN status TINYINT DEFAULT 2 COMMENT '0: inactive; 1: waiting for approval; 2: active';
ALTER TABLE eh_customer_trackings modify COLUMN status TINYINT DEFAULT 2 COMMENT '0: inactive; 1: waiting for approval; 2: active';
ALTER TABLE eh_customer_trademarks modify COLUMN status TINYINT DEFAULT 2 COMMENT '0: inactive; 1: waiting for approval; 2: active';

ALTER TABLE `eh_flow_service_types` ADD COLUMN `module_id` BIGINT NOT NULL DEFAULT 0;
-- 更改号码类型 --by st.zheng
ALTER TABLE `eh_news` CHANGE COLUMN `phone` `phone` VARCHAR(32) NULL DEFAULT '0' ;

-- merge from energy3.0 by xiongying20171030
ALTER TABLE eh_energy_meters ADD COLUMN `cost_formula_source` TINYINT DEFAULT '0' COMMENT '0: 能耗设置, 1: 缴费模块';
ALTER TABLE eh_energy_meter_setting_logs ADD COLUMN `formula_source` TINYINT DEFAULT '0' COMMENT '0: 能耗设置, 1: 缴费模块';
ALTER TABLE eh_energy_meter_reading_logs ADD COLUMN `task_id` BIGINT DEFAULT '0';

-- 表计关联门牌
CREATE TABLE `eh_energy_meter_addresses` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `meter_id` BIGINT NOT NULL DEFAULT '0' COMMENT 'reference to id of eh_groups',
  `building_id` BIGINT NOT NULL DEFAULT '0',
  `building_name` VARCHAR(128) DEFAULT NULL,
  `address_id` BIGINT NOT NULL DEFAULT '0' COMMENT 'reference to id of eh_addresses',
  `apartment_name` VARCHAR(128) DEFAULT NULL,
  `apartment_floor` VARCHAR(16) DEFAULT NULL,
  `status` TINYINT NOT NULL DEFAULT '0' COMMENT '0: inactive, 1: waitingForApproval, 2: active',
  `creator_uid` BIGINT DEFAULT NULL COMMENT 'record creator user id',
  `create_time` DATETIME DEFAULT NULL,
  `operator_uid` BIGINT DEFAULT NULL COMMENT 'redundant auditing info',
  `update_time` DATETIME DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `meter_address_meter_id` (`meter_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 能耗抄表计划
CREATE TABLE `eh_energy_plans` (
  `id` BIGINT NOT NULL COMMENT 'id',		  
  `namespace_id` INTEGER,
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the plan, enterprise, etc',
  `owner_id` BIGINT NOT NULL DEFAULT 0,
  `target_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the plan, community, etc',
  `target_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'reference to the id of who own the plan',
  `name` VARCHAR(1024),
  `repeat_setting_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'refernece to the id of eh_repeat_settings',
  `notify_tick_minutes` INTEGER COMMENT '提前多少分钟',
  `notify_tick_unit` TINYINT COMMENT '提醒时间显示单位',
  `status` TINYINT NOT NULL DEFAULT 0 COMMENT '0: inactive, 1: not completed, 2: active',
  `creator_uid` BIGINT NOT NULL DEFAULT 0 COMMENT 'record creator user id',
  `create_time` DATETIME,
  `operator_uid` BIGINT COMMENT 'operator uid of last operation',
  `update_time` DATETIME,
  `deleter_uid` BIGINT NOT NULL DEFAULT 0 COMMENT 'deleter id',
  `delete_time` DATETIME COMMENT 'mark-deletion policy. historic data may be useful',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `eh_energy_plan_group_map` (
  `id` BIGINT NOT NULL COMMENT 'id',
  `plan_id` BIGINT NOT NULL DEFAULT '0' COMMENT 'refernece to the id of eh_energy_plans',
  `group_id` BIGINT NOT NULL DEFAULT '0' COMMENT 'refernece to the id of eh_organizations',
  `position_id` BIGINT NOT NULL DEFAULT '0' COMMENT 'refernece to the id of eh_organization_job_positions',
  `create_time` DATETIME DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `eh_energy_plan_meter_map` (
  `id` BIGINT NOT NULL COMMENT 'id',
  `plan_id` BIGINT NOT NULL DEFAULT '0' COMMENT 'refernece to the id of eh_energy_plans',
  `meter_id` BIGINT NOT NULL DEFAULT '0' COMMENT 'refernece to the id of eh_energy_meters',
  `default_order` INTEGER DEFAULT 0,
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 按计划生成工单
CREATE TABLE `eh_energy_meter_tasks` (
  `id` BIGINT NOT NULL COMMENT 'id',
  `namespace_id` INTEGER,
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the standard, organization, etc',
  `owner_id` BIGINT NOT NULL DEFAULT 0,
  `target_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the group of who own the task, community, etc',		
  `target_id` BIGINT NOT NULL DEFAULT 0,
  `plan_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'refernece to the id of eh_energy_plans',
  `meter_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'refernece to the id of eh_energy_meters',
  `executive_start_time` DATETIME,
  `executive_expire_time` DATETIME,
  `last_task_reading` DECIMAL(10,1),
  `reading` DECIMAL(10,1),
  `generate_payment_flag` TINYINT NOT NULL DEFAULT 0 COMMENT '0: 未生成, 1: 已生成',
  `status` TINYINT NOT NULL DEFAULT 0 COMMENT '0: 未抄, 1: 已抄',
  `default_order` INTEGER DEFAULT 0,
  `create_time` DATETIME,		
  `operator_uid` BIGINT,
  `update_time` DATETIME,
  		
  PRIMARY KEY (`id`),		
  KEY `plan_id` (`plan_id`),		
  KEY `status` (`status`),		
  KEY `target_id` (`target_id`),		
  KEY `executive_expire_time` (`executive_expire_time`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

-- 操作记录表 目前只有计划操作记录
CREATE TABLE `eh_energy_meter_logs` (
  `id` BIGINT NOT NULL COMMENT 'id',
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the log, enterprise, etc',
  `owner_id` BIGINT NOT NULL DEFAULT 0,
  `target_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'plan, etc',
  `target_id` BIGINT NOT NULL DEFAULT 0,
  `process_type` TINYINT NOT NULL DEFAULT 0 COMMENT '0: none, 1: insert, 2: update, 3: delete',
  `operator_uid` BIGINT NOT NULL DEFAULT 0 COMMENT 'record operator user id',
  `create_time` DATETIME,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

-- 缴费、客户里面增加计价条款的设置
CREATE TABLE `eh_default_charging_item_properties` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0 COMMENT 'namespace of owner resource, redundant info to quick namespace related queries',
  `default_charging_item_id` BIGINT NOT NULL COMMENT 'id of eh_contract_charging_items',
  `property_type` TINYINT COMMENT '0: community; 1: building; 2: apartment',
  `property_id` BIGINT,
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '0: inactive; 1: waiting for approval; 2: active',
  `create_uid` BIGINT NOT NULL DEFAULT 0,
  `create_time` DATETIME,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `eh_default_charging_items` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0 COMMENT 'namespace of owner resource, redundant info to quick namespace related queries',
  `community_id` BIGINT NOT NULL DEFAULT 0,
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the charging, organizationowner,asset, etc',
  `owner_id` BIGINT NOT NULL DEFAULT 0,
  `charging_item_id` BIGINT COMMENT '收费项',
  `charging_standard_id` BIGINT COMMENT '收费标准',
  `formula` VARCHAR(1024),
  `formula_type` TINYINT COMMENT '1: fixed fee; 2: normal formula; 3: gradient varied on variable price; 4: gradients varied functions on each variable section',
  `billing_cycle` TINYINT,
  `late_fee_standard_id` BIGINT COMMENT '滞纳金标准',
  `charging_variables` VARCHAR(1024) COMMENT '计费金额参数 json: {"variables":[{"variableIdentifier":"22","variableName":"面积","variableValue":"960.00"}]}',
  `charging_start_time` DATETIME,
  `charging_expired_time` DATETIME,
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '0: inactive; 1: waiting for approval; 2: active',
  `create_uid` BIGINT NOT NULL DEFAULT 0,
  `create_time` DATETIME,
  `operator_uid` BIGINT,
  `update_time` DATETIME,
  `delete_uid` BIGINT,
  `delete_time` DATETIME,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;